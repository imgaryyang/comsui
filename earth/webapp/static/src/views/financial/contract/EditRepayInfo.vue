<style>
    
</style>

<template>
    <div class="content" id="financialContractEdit">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: $utils.locale('financialContract') }, {title: '修改还款信息'}]"></Breadcrumb>

            <div style="width: 80%; margin: 30px auto;">
                <IncludeEditRepayInfo
                    :isUpdate="true"
                    :model="model"
                    @next="submit"></IncludeEditRepayInfo>
            </div>
        </div>
    </div>
</template>

<script>
    import IncludeEditRepayInfo from './include/_EditRepayInfo';
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        components: {
            IncludeEditRepayInfo
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
                    data.assetPackageFormat = data.assetPackageFormat + '';
                    data.repurchaseRule = data.repurchaseRule + '';
                    data.repurchaseApproach = data.repurchaseApproach + '';
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
                    url: `/financialContract/edit-financialContractRepaymentInfo/${financialContractUuid}`,
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
                    url: `/financialContract/edit-repaymentInfo/data`,
                    data: {
                        financialContractUuid: this.$route.params.financialContractUuid
                    }
                });
            }
        }
    }

</script>