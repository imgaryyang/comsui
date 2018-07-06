<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller">
			<Breadcrumb :routes="[{ title: '个人中心'}]">
			</Breadcrumb>
			<div style="margin-left: 12%; margin-top: 50px;margin-right: 12%">
				<TabMenu v-model="tabSelected" >
					<TabMenuItem id="baseInfo">基础信息</TabMenuItem>
		            <TabMenuItem id="modifyPwd">修改密码</TabMenuItem>
		            <TabMenuItem id="secretKey">密钥管理</TabMenuItem>
				</TabMenu>
				<TabContent v-model="tabSelected">
					<TabContentItem id="baseInfo">
						<BaseInfo :model="baseModel"></BaseInfo>
					</TabContentItem>
					<TabContentItem id="modifyPwd">
						<UpdatePassword></UpdatePassword>
					</TabContentItem>
					<TabContentItem id="secretKey">
						<SecretKey v-model="refreshSecretKey" @showHistoryModal="showHistoryModal(arguments[0])"></SecretKey>
					</TabContentItem>
				</TabContent>
			</div>
		</div>

		<HistoryModal v-model="historyModal.show" :uuid="historyModal.uuid">
        </HistoryModal>
	</div>
</template>

<script>
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import BaseInfo from './include/BaseInfo';
    import UpdatePassword from './include/UpdatePassword';
    import HistoryModal from './include/HistoryModal';
    import SecretKey from './include/SecretKey';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
		components: {
			TabMenu,TabMenuItem, TabContent, TabContentItem, BaseInfo, UpdatePassword, SecretKey, HistoryModal
		},
		data: function() {
			return {
				tabSelected: 'baseInfo',
				baseModel: {},
				refreshSecretKey: false,

				historyModal: {
					show: false,
					uuid: ''
				},
			}
		},
		activated: function() {
			this.tabSelected = 'baseInfo';
			this.refreshSecretKey = true;
			this.fetchBaseInfo();
		},
		methods: {
			fetchBaseInfo: function() {
				ajaxPromise({
                    url: '/show-principal'
                }).then(data => {
                	this.baseModel = data.detailModel || {};
                }).catch(message => {
                    MessageBox.open(message);
                });
			},
			showHistoryModal: function(uuid) {
                var { historyModal } = this;
                historyModal.show = true;
                historyModal.uuid = uuid;
            }
		}
	}
</script>