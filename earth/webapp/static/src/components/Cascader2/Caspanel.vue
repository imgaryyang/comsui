<style lang="sass">
    .caspanel {
        list-style: none;
        display: inline-block;
        vertical-align: top;
        height: 308px;
        overflow: auto;
        border-right: 1px solid #d1dbe5;
        background-color: #fff;
        box-sizing: border-box;
        margin: 0;
        padding: 6px 0;
        min-width: 160px;
    }
</style>

<template>
    <ul class="caspanel" v-if="options && options.length">
        <SelectAllItem
            :collection="collection"
            :options="options"
            :index="parentId"
            :multiple="multiple"
            :selected="selected">
        </SelectAllItem>
        <Casitem
            v-for="item in options"
            :key="item._id"
            :collection="collection"
            :selected="selected"
            :data="item"
            :multiple="multiple"
            :customItem="customItem"
            :ismenu="item._isLastLevel ? false : true">
        </Casitem>
    </ul>
</template>
<script>
    import Casitem from './Casitem.vue';
    import SelectAllItem from './SelectAllItem.vue'

    export default {
        name: 'Caspanel',
        components: { Casitem, SelectAllItem },
        props: {
            parentId: [Number, String],
            collection: {
                type: Array,
                required: true
            },
            selected: {
                type: Array,
                required: true
            },
            multiple: [Number, String],
            customItem: Function,
        },
        data: function(){
            return {
                allSelectFlag: false
            }
        },
        computed: {
            options: function() {
                var options = this.collection.filter(item => item.parentId == this.parentId)
                return options.slice(0)
            }
        },
    };
</script>