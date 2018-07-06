<style lang="sass">
	@import '~assets/stylesheets/base';

	.lookup-result-modal {
		@include min-screen(768px) {
			.modal-dialog {
				width: 75%;
				margin: 30px auto;
			}
		}
	}
</style>

<template>
	<Modal v-model="visible" class="lookup-result-modal">
		<ModalHeader title="查询结果">
		</ModalHeader>
		<ModalBody v-loading="dataSource.fetching">
			<div v-if="dataSource.error.length != 0">
				{{ dataSource.error }}
			</div>
			<div style="margin-bottom: 15px;" v-else>
				<div style="margin-bottom:20px;width:150px;">
					<el-select size="small" v-model="queryConds.dateCounter">
						<el-option label="当天" value="0"></el-option>
						<el-option label="近三天" value="1"></el-option>
						<el-option label="近五天" value="2"></el-option>
						<el-option label="近十天" value="3"></el-option>
					</el-select>
				</div>
				<el-table
			        border
			        stripe
			        :data="dataSource.list"
			        class="td-15-padding th-8-15-padding no-th-border">
			        <el-table-column inline-template label="选择" :width="50" :content="_self">
			        	<el-checkbox @input="handleInput(row, arguments[0])" :value="selectedCashFlows.includes(row)"></el-checkbox>
			        </el-table-column>
			        <el-table-column prop="serialNo" label="流水号"></el-table-column>
			        <el-table-column prop="drcrfName" label="借贷标志"></el-table-column>
			        <el-table-column inline-template label="借方发生额">
			        	<div> {{ row.debitAmount | formatMoney }} </div>
			        </el-table-column>
			        <el-table-column inline-template label="贷方发生额">
			        	<div> {{ row.creditAmount | formatMoney }} </div>
			        </el-table-column>
			        <el-table-column inline-template label="余额">
			        	<div> {{ row.balance | formatMoney }}</div>
			        </el-table-column>
			        <el-table-column prop="recipAccNo" label="对方账号"></el-table-column>
			        <el-table-column prop="recipBkName" label="对方开户号"></el-table-column>
			        <el-table-column prop="recipName" label="对方户名"></el-table-column>
			        <el-table-column inline-template label="入账时间">
			        	<div> {{ row.time | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
			        </el-table-column>
			        <el-table-column prop="summary" label="摘要"></el-table-column>
			        <el-table-column prop="postScript" label="附言"></el-table-column>
			        <el-table-column inline-template label="关联金额" :content="_self">
			        	<el-input v-model="row.relatedAmount"></el-input>
			        </el-table-column>
			    </el-table>
			    <div>
			        <PageControl
			            v-model="pageConds.pageIndex"
			            :size="dataSource.size"
			            :per-page-record-number="pageConds.perPageRecordNumber">
			        </PageControl>
			    </div>
			</div>
		</ModalBody>
		<ModalFooter>
			<el-button @click="visible = false">取消</el-button>
			<el-button @click="reTouching" type="success" :loading="reTouchLoading" v-if="ifElementGranted('beneficiary-associate-cashflow')">关联</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise, purify } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import Pagination from 'mixins/Pagination';
    import format from 'filters/format';
    
    import { REGEXPS } from 'src/validators';

	export default {
		mixins: [Pagination],
		props: {
			value: {
				type: Boolean,
				default: false,
			},
			autoload: {
				type: Boolean,
				default: false,
			}
		},
		watch: {
			value: function(current) {
				this.visible = current;
			},
			visible: function(current) {
				if (current) {
					this.fetch();
				} else {
					this.selectedCashFlows = [];
					this.dataSource.list = [];
		            this.dataSource.size = 0;
		            this.dataSource.error = '';
		            this.queryConds.dateCounter = 0;
				}
				this.$emit('input', current);
			},
			'pageConds.pageIndex': function(current) {
				this.selectedCashFlows = [];
			},
			'queryConds.dateCounter': function(current) {
				this.selectedCashFlows = [];
			}
		},
		data: function() {
			return {
				action: `/audit/beneficiary/${this.$route.params.uuid}/clearingCashFlowQuery`,
				reTouchLoading: false,
				visible: this.value,

				selectedCashFlows: [],

				queryConds: {
					dateCounter: 0
				},
				pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
			}
		},
		computed: {
			conditions: function() {
                return Object.assign({},this.queryConds, this.pageConds);
            },
		},
		methods: {
			reTouching: function() {
				if (this.selectedCashFlows.length == 0) {
					MessageBox.open('请先选择关联流水');
					return;
				}

				var isRightMoneyFormat = true;
				this.selectedCashFlows.forEach(row => {
					if(!REGEXPS.MONEY.test(row.relatedAmount)){
						isRightMoneyFormat = false;
						MessageBox.open('关联金额格式不正确！');
						return;
					}
				});

				if (this.reTouchLoading || !isRightMoneyFormat) return;
				this.reTouchLoading = true;

				ajaxPromise({
					url: `/audit/beneficiary/${this.$route.params.uuid}/reTouching`,
					type: 'post',
					data: {
						'data': JSON.stringify(this.selectedCashFlows)
					}
				}).then(data => {
					MessageBox.open('关联成功');
					MessageBox.once('close', () => {
						this.visible = false;
						this.$emit('reTouching');
					});
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.reTouchLoading = false;
				});
			},
			handleInput: function(row, checked) {
				if (checked) {
					this.selectedCashFlows.push(row);
				}else {
					var index = this.selectedCashFlows.indexOf(row);
					if (index != -1) {
						this.selectedCashFlows.splice(index, 1);
					}
				}
			},
			fetch: function() {
				if (this.dataSource.fetching
	                && this.equalTo(purify(this.conditions), purify(this.previousConditions))) {
	                return
	            }

	            this.getData({
	                url: this.action,
	                data: this.conditions,
	                type: 'post'
	            });
			}
		},
	}
</script>