<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller">
            <el-form
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item label="开户行所在地">
                    <el-col :span="12">
                        <el-form-item>
                            <select-province 
                                group="address"
                                v-model="provinceCode"></select-province>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item>
                            <select-city 
                                group="address"
                                :province-code="provinceCode"
                                v-model="cityCode"></select-city>
                        </el-form-item>
                    </el-col>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data: function() {
            return {
                provinceCode: '',
                cityCode: ''
            };
        },
        beforeMount: function() {
            setTimeout(() => {
                this.provinceCode = '330000';
                this.cityCode = '331100';
            }, 1000);
        },
        methods: {

        }
    }
</script>