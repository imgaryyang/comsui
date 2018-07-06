<style lang="sass" scoped>
	.item-container{
	    min-height: 40px;
	    padding: 5px;
		border: 1px solid #ddd;
		.item{
			display: inline-block;
			line-height: 24px;
			margin: 5px 10px 5px 0;
			padding: 5px;
			border: 1px solid #ddd;
			.f-delete-item{
				color: #436ba7;
				margin-left: 7px;
				cursor: pointer;
			}
		}
		.item.el-icon-plus{
			width: 36px;
			text-align: center;
			cursor: pointer;
		}
	}
</style>

<template>
	<div v-if="items.length == 0 && !config">暂无数据</div>
	<div class="item-container" v-else>
		<div  v-for="(item,index) in items" class="item">{{property ? item[property] : item}}<span v-if="config" @click="deleteItem(index)" class="f-delete-item">x</span></div>
		<div class="item el-icon-plus" v-if="config" @click="createItem"></div>
	</div>
</template>

<script>
	export default{
		props: {
			value: Array,
			config: {
				type: Boolean,
				default: false
			},
			property: String//若值为对象，提供显示label
		},
		computed: {
			items: {
				get(){
					return this.value;
				},
				set(v){
					this.$emit('input', v)
				}
			}
		},
		methods: {
			deleteItem(index){
				this.items.splice(index, 1)
			},
			createItem(){
				this.$emit('create')
			}
		}
	}
</script>