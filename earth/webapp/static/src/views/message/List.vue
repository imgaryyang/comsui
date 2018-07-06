<template>
	<div class="content">
		<div class="scroller">
			<div class="query-area">
				<el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-input 
                            size="small" 
                            placeholder="客户编号"
                            :value="queryConds.clientId"
                            @change.native="queryConds.clientId = $event.target.value"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.allowedSendStatus"
                            placeholder="发送状态"
                            size="small">
                            <el-option label="所有" :value="-1"></el-option>
                            <el-option label="已允许" :value="1"></el-option>
                            <el-option label="未允许" :value="0"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.smsTemplateEnum"
                            placeholder="所有短信"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in smsTemplateEnumList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startDate"
                                    :end-date="queryConds.endDate"
                                    placeholder="创建起始日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.endDate"
                                    :start-date="queryConds.startDate"
                                    placeholder="创建终止日期"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border"
					stripe
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="客户编号" prop="smsQuene.clientId"></el-table-column>
                    <el-table-column label="短信类型" prop="smsTemplate.title"></el-table-column>
                    <el-table-column min-width="250px" label="短信内容" prop="smsQuene.content"></el-table-column>
                    <el-table-column label="合作商户号" prop="smsQuene.platformCode"></el-table-column>
                    <el-table-column label="创建时间" inline-template prop="smsQuene.createTime">
                        <div>{{ row.smsQuene.createTime | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="请求时间" inline-template prop="smsQuene.requestTime">
                        <div>{{ row.smsQuene.requestTime | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="响应时间" inline-template prop="smsQuene.responseTime">
                        <div>{{ row.smsQuene.responseTime | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="响应结果" prop="smsQuene.responseTxt"></el-table-column>
                    <el-table-column label="允许发送" prop="smsQuene.allowedSendStatus" inline-template>
                        <div>{{ row.smsQuene.allowedSendStatus ? '是' : '否' }}</div>
                    </el-table-column>
                    <el-table-column label="发送状态" prop="smsQuene.smsSendStatus" inline-template>
                        <div>
                            <span v-if="row.smsQuene.smsSendStatus == 'WAITING_SEND'">待发送</span>
                            <span v-else-if="row.smsQuene.smsSendStatus == 'SUCCESS'">成功</span>
                            <span v-else-if="row.smsQuene.smsSendStatus == 'FAILURE'">失败</span>
                        </div>
                    </el-table-column>
                    <el-table-column label="操作" inline-template>
                        <div>
                            <a @click="handleActivate(row.smsQuene.id)" v-if="ifElementGranted('send-message') && row.smsQuene.allowedSendStatus == false" href="javascript: void 0;">发送</a>
                            <a @click="handleReSend(row.smsQuene.id)" v-if="row.smsQuene.smsSendStatus == 2" href="javascript: void 0;">重发</a>
                        </div>
                    </el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
            <div class="pull-left">
                <span v-if="ifElementGranted('open-message-function')">自动发送：<el-button size="small" type="primary" @click="handleChangeAllowSend">{{ allowedSendFlag ? '是（点击关闭）' : '否（点击开启）' }}</el-button></span>
                <span v-if="ifElementGranted('if-send-failed-message')"><el-button size="small" type="primary" @click="handleSendSuccSms">发送所有未发送的成功短信</el-button></span>
                <span v-if="ifElementGranted('if-delete-failed-message')"><el-button size="small" type="primary" @click="handleDeleteNotSuccSms">删除所有非成功未允许发送短信</el-button></span>
            </div>
			<div class="pull-right">
				<PageControl 
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>
	</div>

</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		mixins: [Pagination, ListPage],
		data: function() {
			return {
				action: '/message/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
                    clientId: '',
                    allowedSendStatus: '',
                    smsTemplateEnum: '',
                    startDate: '',
                    endDate: ''
                },

                allowedSendFlag: '',
				smsTemplateEnumList: []
			};
		},
		methods: {
			initialize: function() {
			    return ajaxPromise({
			    	url: `/message/options`
			    }).then(data => {
                    this.allowedSendFlag = data.allowedSendFlag;
			    	this.smsTemplateEnumList = data.smsTemplateEnumList;
			    }).catch(message => {
			    	MessageBox.open(message);
			    });
			},
            handleChangeAllowSend: function() {
                MessageBox.open('是否确认改变自动允许发送状态！', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '继续',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            type: 'post',
                            url: `/message/changeAllowSend`,
                            data: { allowedSendFlag: this.allowedSendFlag }
                        }).then(data => {
                            this.allowedSendFlag = !this.allowedSendFlag;
                            MessageBox.open('操作成功！');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            handleSendSuccSms: function() {
                MessageBox.open('是否确认一键发送所有未发送的成功扣款短信！', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '继续',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            type: 'post',
                            url: `/message/sendSuccSms`,
                        }).then(data => {
                            MessageBox.once('closed', this.fetch);
                            MessageBox.open('操作成功！');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            handleDeleteNotSuccSms: function() {
                MessageBox.open('是否确认删除所有非成功未允许发送短信！', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '继续',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            type: 'post',
                            url: `/message/deleteNotSuccSms`,
                        }).then(data => {
                            MessageBox.once('closed', this.fetch);
                            MessageBox.open('操作成功！');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            handleActivate: function(smsQueneId) {
                ajaxPromise({
                    type: 'post',
                    url: `/message/activate`,
                    data: { smsQueneId }
                }).then(data => {
                    this.fetch();
                    data.message ? MessageBox.open(data.message) : void 0;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleReSend: function(smsQueneId) {
                ajaxPromise({
                    type: 'post',
                    url: `/message/reSend`,
                    data: { smsQueneId }
                }).then(data => {
                    this.fetch();
                    data.message ? MessageBox.open(data.message) : void 0;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
		}
	}
</script>