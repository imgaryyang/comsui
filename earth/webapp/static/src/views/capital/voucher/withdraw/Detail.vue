<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '提现详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">凭证信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>凭证编号 ：</p>
                                <p>凭证来源 ：</p>
                                <p>提现类型 ：</p>
                                <p>凭证金额 ：</p>
                                <p>创建时间 ：</p>
                            </div>
                            <div class="col">
                                <p>交易网关 ：</p>
                                <p>接口请求编号 ：</p>
                                <p>凭证状态 ：</p>
                                <p>状态变更时间 ：</p>
                                <p>备注 ：</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">提现银行卡</h5>
                        <div class="bd">
                            <div class="col">
                                <p>项目名称 ：<a href="#"></a></p>
                                <p>贷款合同编号 ：<a href="#"></a></p>
                                <p>客户姓名  ：</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        提现单
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="withdrawModel"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="" label="提现单号">
                            </el-table-column>
                            <el-table-column prop="" label="账户编号">
                            </el-table-column>
                            <el-table-column prop="" label="账户名称">
                            </el-table-column>
                            <el-table-column prop="" label="银行账户号">
                            </el-table-column>
                            <el-table-column prop="" label="账户姓名">
                            </el-table-column>
                            <el-table-column prop="" label="开户行">
                            </el-table-column>
                            <el-table-column prop="" label="提现方式">
                            </el-table-column>
                            <el-table-column prop="" label="创建时间">
                            </el-table-column>
                            <el-table-column prop="" label="提现金额">
                            </el-table-column>
                            <el-table-column prop="" label="状态">
                            </el-table-column>
                            <el-table-column prop="" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">
                        凭证资金流水
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="withdrawModel"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="" label="流水号">
                            </el-table-column>
                            <el-table-column prop="" label="交易金额">
                            </el-table-column>
                            <el-table-column prop="" label="账户姓名">
                            </el-table-column>
                            <el-table-column prop="" label="银行账户号">
                            </el-table-column>
                            <el-table-column prop="" label="开户行">
                            </el-table-column>
                            <el-table-column prop="" label="出账时间">
                            </el-table-column>
                            <el-table-column prop="" label="摘要">
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