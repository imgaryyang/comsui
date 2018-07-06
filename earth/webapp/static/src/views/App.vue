<style lang="sass">
    @import '~assets/stylesheets/base.scss';
    @import '~assets/stylesheets/common.scss';
    @import '~assets/stylesheets/scaffold.scss';
    @import '~assets/stylesheets/form.scss';
    @import '~assets/stylesheets/query-area.scss';
    @import '~assets/stylesheets/table-area.scss';
    @import '~assets/stylesheets/row-layout-detail.scss';
    @import '~assets/stylesheets/col-layout-detail.scss';

    /* 已解除依赖app-last中的：
     * lookup-params, table-layout-detail，help-tip, sketch-item, parcel-input, tab, step,
     * datetimepicker, aside
     */
    

    // 以下这些文件需要逐步移入vue文件中
    // @import '../../css/src/helper.scss';
    // @import '../../css/src/structure.scss';

    // @import '../../css/src/component/top-content.scss';
    // @import '../../css/src/component/bottom-content.scss';
    // @import '../../css/src/component/data-list.scss';
    // @import '../../css/src/component/assembly';
    // // @import 'component/dashboard.scss';

    // @import '../../css/src/view/login.scss';
    // @import '../../css/src/view/welcome.scss';
    // @import '../../css/src/view/yunxin/detail.scss';
    // @import '../../css/src/view/yunxin/passage.scss';
    // @import '../../css/src/view/yunxin/loan.scss';
    // @import '../../css/src/view/yunxin/other.scss';
</style>

<template>
	<keep-alive>
        <router-view></router-view>
    </keep-alive>
</template>
