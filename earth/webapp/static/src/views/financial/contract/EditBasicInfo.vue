<style>
    
</style>

<template>
    <div class="content" id="financialContractEdit">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: $utils.locale('financialContract') }, {title: '修改合同基础信息'}]"></Breadcrumb>

            <div style="width: 80%; margin: 30px auto;">
                <IncludeEditBasicInfo
                    :isUpdate="true"
                    :model="model"
                    @next="submit"></IncludeEditBasicInfo>
            </div>
        </div>
    </div>
</template>

<script>
    import IncludeEditBasicInfo from './include/_EditBasicInfo';
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';
    import format from 'filters/';

    export default {
        components: {
            IncludeEditBasicInfo
        },
        data: function() {
            return {
                fetching: false,
                model: {}
            };
        },
        activated: function() {
            this.fetch()
                .then(data => {
                    data.financialContractNo = data.contractNo;
                    data.financialContractName = data.contractName;
                    data.financialContractShortName = data.financialContractShortName || '';
                    data.advaStartDate = format.formatDate(data.advaStartDate);
                    data.thruDate = format.formatDate(data.thruDate);
                    data.financialContractType = data.financialContractType + '';
                    data.capitalParty = data.capitalParty || [];
                    data.otherParty = data.otherParty || [];
                    this.model = data;
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                })
        },
        methods: {
            submit: function(data) {
                this.model = Object.assign({}, this.model, data);
                var { financialContractUuid } = this.$route.params;

                return ajaxPromise({
                    type: 'post',
                    url: `/financialContract/edit-financialContractBasicInfo/${financialContractUuid}`,
                    contentType: 'application/json',
                    data: JSON.stringify(this.model)
                }).then(data => {
                    MessageBox.once('closed', () => {
                        this.$router.push({
                            name: 'financialContractdetail',
                            params: { financialContractUuid }
                        });
                    });
                    MessageBox.open('修改成功');
                }).catch(message => {
                    MessageBox.open(message)
                });
            },
            fetch: function() {
                this.fetching = true;
                return ajaxPromise({
                    url: `/financialContract/edit-basicInfo/data`,
                    data: {
                        financialContractUuid: this.$route.params.financialContractUuid
                    }
                });
            }
        }
    }

</script>