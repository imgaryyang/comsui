<style lang="sass">
</style>

<template>
    <div class="page-control advanced" :class="{
        'pull-right': float == 'right',
        'pull-left': float == 'left'
    }">
        <div class="inner">
            <slot :data="{size: size}" name="statistics">
                共<span class="total">{{ size }}</span>条
            </slot>
            <span class="nav">
                <a v-if="enableHeadTail" href="#" @click.prevent="onClickFirstPage" :class="['first-page', pageIndex === 1 ? 'disabled' : '']">首页</a>
                <a href="#" @click.prevent="onClickPrevPage" :class="['prev', pageIndex === 1 ? 'disabled' : '']">&lt; 上一页</a>
                <span class="tip">{{ pageIndex }}/{{ pageNumber }}页</span>
                <a href="#" @click.prevent="onClickNextPage" :class="['next', pageIndex === pageNumber ? 'disabled' : '']">下一页 &gt;</a>
                <a v-if="enableHeadTail" href="#" @click.prevent="onClickLastPage" :class="['last-page', pageIndex === pageNumber ? 'disabled' : '']">尾页</a>
            </span>
            <span v-if="enableRedirect" @click="showRedirectForm=true" class="popup-redirect-form">跳转</span>
        </div>
        <div class="redirect-form" v-show="showRedirectForm">
            跳转至
            <input 
                type="text" 
                v-model.lazy.trim="redirectPageIndex"
                :class="['form-control', 'page-index', redirectPageIndex === '' || validatePageIndex(redirectPageIndex) ? '' : 'form-control-error']" 
                @keyup.13="onRedirect">
            页
            <button class="btn btn-defalut redirect" @click="onRedirect">确定</button>
        </div>
    </div>
</template>

<script>
    module.exports = {
        props: {
            enableRedirect: {
                default: true
            },
            enableHeadTail: {
                default: true
            },
            float: {
                default: 'right'
            },
            perPageRecordNumber: {
                type: Number,
                required: true
            },
            value: {
                type: Number,
                default: 1
            },
            size: {
                type: Number,
                required: true
            }
        },
        data: function() {
            return {
                redirectPageIndex: '',
                pageIndex: this.value,
                showRedirectForm: false
            };
        },
        computed: {
            pageNumber: function() {
                var res = Math.max(1, Math.ceil(this.size / this.perPageRecordNumber));
                return res;
            }
        },
        watch: {
            value: function(cur) {
                this.pageIndex = cur;
            },
            pageIndex: function(cur) {
                this.$emit('input', cur);
            }
        },
        beforeMount: function() {
            $(document).on('click.pagecontrol', (e) => {
                if (!$(e.target).closest(this.$el).length) {
                    this.showRedirectForm = false;
                }
            });
        },
        beforeDestory: function() {
            $(document).off('click.pagecontrol');
        },
        methods: {
            validatePageIndex: function(index) {
                return !isNaN(index) && index >= 1 && index <= this.pageNumber
            },
            onRedirect: function() {
                if (!this.validatePageIndex(this.redirectPageIndex)) return;
                this.pageIndex = + this.redirectPageIndex;
            },
            onClickFirstPage: function(e) {
                this.pageIndex = 1;
            },
            onClickLastPage: function(e) {
                this.pageIndex = this.pageNumber;
            },
            onClickPrevPage: function(e) {
                if (!this.validatePageIndex(this.pageIndex - 1)) return;
                this.pageIndex = this.pageIndex - 1;
            },
            onClickNextPage: function(e) {
                if (!this.validatePageIndex(this.pageIndex + 1)) return;
                this.pageIndex = this.pageIndex + 1;
            }
        }
    }
</script>