<style>
</style>

<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item label="机构账户号">
                        <DateTimePicker 
                            size="small"></DateTimePicker>
                    </el-form-item>
                    <el-form-item>
                        <DateTimePicker 
                            size="small"></DateTimePicker>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker 
                                    size="small"></DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2"><div class="text-align-center color-dim">至</div></el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker 
                                    size="small"></DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" class="middle" placeholder="请输入内容">
                            <el-select class="short" slot="prepend" placeholder="请选择">
                              <el-option label="餐厅名" value="1"></el-option>
                              <el-option label="订单号" value="2"></el-option>
                              <el-option label="用户电话" value="3"></el-option>
                            </el-select>
                        </el-input>
                    </el-form-item>
                </el-form>
            </div>

            <div class="table-area">
                <table class="data-list">
                    <thead>
                        <tr>
                            <th>信托产品代码</th>
                            <th>信托合同名称</th>
                            <th>信托商户名称</th>
                            <th>信托合同类型</th>
                            <th>信托合同起始日</th>
                            <th>信托合同截止日</th>
                            <th>信托专户账号</th>
                            <th>放款通道名称</th>
                            <th>回款通道名称</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in dataSource.list">
                            <td><a href="detail">{{ item.contractNo }}</a></td>
                            <td>{{ item.contractName }}</td>
                            <td>{{ item.app.name }}</td>
                            <td>{{ item.financialContractTypeMsg }}</td>
                            <td>{{ new Date(item.advaStartDate).format('yyyy/MM/dd') }}</td>
                            <td>{{ item.thruDate ? new Date(item.thruDate).format('yyyy/MM/dd') : '' }}</td>
                            <td>{{ item.capitalAccount.accountNo }}</td>
                            <td></td>
                            <td>{{ item.paymentChannel ? item.paymentChannel.channelName : '' }}</td>
                            <td>
                                <router-link :to="{name: 'form'}">详情</router-link>
                            </td>
                        </tr>                       
                    </tbody>
                </table>
            </div>

            <div class="operations">
                <PageControl 
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>

        <Modal v-model="showModal">
            <ModalHeader title="作废"></ModalHeader>
            <ModalBody>
                <div class="form-wrapper">
                    <form class="form adapt">
                        <div class="field-row">
                            <label for="" class="field-title">信托合同名称</label>
                            <div class="field-value">
                                <input type="text" name="contractName" v-model="activeData.contractName" class="form-control real-value">
                            </div>
                        </div>
                    </form>
                </div>
            </ModalBody>
            <ModalFooter>
                <button type="button" class="btn btn-default"  @click="showModal = false">取消</button>
                <button type="button" class="btn btn-success" @click="disable">确定</button>
            </ModalFooter>
        </Modal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';

    export default {
        mixins: [Pagination, ListPage],
        components: {
        },
        data: function() {
            return {
                action: './financialContract/query',
                queryConds: {
                    financialContractNo: '',
                    financialContractName: '',
                    appId: '',
                    financialContractType: '',
                    financialAccountNo: '',
                    startDate: '',
                    endDate: '',
                },
                activeData: {},
                showModal: false
            };
        },
        methods: {
            onShowDisableDialog: function(item) {
                this.activeData = item;
                this.showModal = true;
            },
            disable: function() {
                this.dataSource.list = this.dataSource.list.map((item) => {
                    if (item.uuid === this.activeData.uuid) {
                        return this.activeData;
                    }
                    return item;
                });
                this.showModal = false;
            }
        }
    }
</script>
