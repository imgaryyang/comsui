<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '提现详情'}]">
                <el-button size="small" type="primary" @click="revouching">重新核单</el-button>
                <el-button size="small" type="primary" @click="onClickRevoke">作废</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">提现信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>提现单号 ：</p>
                                <p>提现金额 ：</p>
                                <p>提现类型 ：</p>
                                <p>创建时间 ：</p>
                                <p>提现状态 ：</p>
                                <p>冲账状态 ：</p>
                                <p>状态变更时间 ：</p>
                                <p>备注 ：</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">客户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户编号 ：<a href="#"></a></p>
                                <p>账户名称 ：</p>
                                <p>客户类型 ：</p>
                                <p>绑定银行卡 ：</p>
                                <p>绑定卡户名 ：</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">提现银行卡</h5>
                        <div class="bd">
                            <div class="col">
                                <p>银行账户号 ：</p>
                                <p>账户姓名 ：</p>
                                <p>开户行 ：</p>
                                <p>开户行所在地 ：</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        提现凭证
                        <el-button type="text" style="padding: 0 10px; font-weight: normal;" @click="createBank">添加流水</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="withdrawModel"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="" label="凭证编号">
                            </el-table-column>
                            <el-table-column prop="" label="提现网关">
                            </el-table-column>
                            <el-table-column prop="" label="流水号">
                            </el-table-column>
                            <el-table-column prop="" label="流水出账时间">
                            </el-table-column>
                            <el-table-column prop="" label="凭证状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <SystemOperateLog :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            SystemOperateLog
        },
        data: function() {
            return {
                fetching: false,
                withdrawModel: {}
            }
        },
        methods: {
            revouching: function() {
                MessageBox.open('确认重新核单？', '提示', [{
                    text: '取消',
                    handler: () => {
                        MessageBox.close();
                    }
                },{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: ``,
                            data: {

                            },
                            type: 'post'
                        }).then(data => {
                            MessageBox.open('核单成功');
                            setTimeout(()=>{
                                MessageBox.close();
                            },500)
                        }).catch(message => {
                          MessageBox.open(message);
                        });
                    }
                }]);
            },
        }
    }
</script>