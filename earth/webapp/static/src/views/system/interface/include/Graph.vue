<style lang="sass">
	.customer-query-graph {
		&.row-layout-detail {
			width: 50%;
			float: left;
			border-style: solid;
			border-width: 1px;
			margin: 90px 20px 0px; 
			border-color: #CACACA
		}	

		.top-table {
			width: 100%;
		    height: 80px;
		    border: 1px solid #8FB9F8;

		    td {
			    border: 1px solid #8FB9F8;
			    color: #333;
			    text-align: center;
		        width: 20%;

			    &.active {
			    	background-color: #8FB9F8;
			    	color: white;
			    }

			    p {
			    	margin: 0px
			    }

			    .money {
			    	font-weight: bold;
			    	font-size:15px
			    }
		    }
		}

		.tab-menu.nav-menu {
			border-bottom: 1px solid #8FB9F8;

			.tab-menu-item {
				float: right;

				&.active {
					border-style: solid;
					border-color: #8FB9F8;
					border-width: 2.5px 1px 0px;
					border-radius: 0px;

					a {
						color: #333;
						background-color: #FFF;
						border-radius: 0px;
					}
				}
			}
		}
	}
</style>

<template>
	<div class="customer-query-graph row-layout-detail">
		<TabMenu v-model="tabSelected">
			<span style="color:#8FB9F8;font-size:18px;font-weight:bold">无篡改账本</span>
			<TabMenuItem id="b">剩余本息</TabMenuItem>
			<TabMenuItem id="a">本金</TabMenuItem>
		</TabMenu>
		<TabContent v-model="tabSelected">
			<TabContentItem id="a">
				<table class="top-table">
					<tbody>
						<tr>
							<td class="active">
								<p>应收资产</p><p class="money">10000</p>
							</td>
							<td>
								<p>持有至到期资产</p><p class="money">10000</p>
							</td>
							<td>
								<p>银行存款</p><p class="money">10000</p>
							</td>
							<td>
								<p>长期负债</p><p class="money">10000</p>
							</td>
							<td>
								<p>主营业务收入</p><p class="money">10000</p>
							</td>
						</tr>
					</tbody>
				</table>
				<TableWithComboQueryBox
					:action="`/dddd`">
				</TableWithComboQueryBox>
			</TabContentItem>
			<TabContentItem id="b">
				bbbbb
			</TabContentItem>
		</TabContent>
	</div>
</template>

<script>
	import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
	export default {
		components: {
			TableWithComboQueryBox: require('./TableWithComboQueryBox'),
			TabMenu,TabMenuItem, TabContent, TabContentItem,
		},
		data: function() {
			return {
				tabSelected: 'a',
			}
		},
	}
</script>