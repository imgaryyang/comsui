<style lang="sass">
	.tree-view {
		min-width: 250px;
		height: 100%;
		
		.el-tree {
		    cursor: default;
		    background: #F1F2F5;
		    border: 0px;
			min-height: 500px;
			height: 100%;
			margin-top: -35px;
			padding-top: 35px;

			.el-tree-node__expand-icon {
				border-left-color: #8FB9F8;
				
				&.is-leaf {
					border-color: transparent;
				    cursor: default;
				}
			}
			.el-tree-node {
				
				.first {
					border-bottom-color: #8FB9F8;
				    border-bottom-style: dashed;
				    border-bottom-width: 1px;
				}
			}
		}
	}
</style>

<template>
	<div class="tree-view">
		<p style="font-size: 18px;color: #8FB9F8;font-weight: bold;margin: 5px 0px;">自定义脚本</p>
			<!-- :data="data" -->
		<el-tree 
			:data="scriptOptionsData" 
			@node-click="handleNodeClick" 
			:default-checked-keys="[]"
			:render-content="renderContent"
			:props="{
				children: 'children',
	          	label: 'label'
	        }"
			:highlight-current="true">
		</el-tree>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import format from 'filters/format';

	export default {
		props: {
			scriptOptions: {
				type: Object,
				default: {}
			},
			customizeScript:Object,
		},
		data: function() {
			return {
		        defaultProps: {
		          children: 'children',
		          label: 'label'
		        },
		        scriptOptionsData: [],
		        GETONCE:[]
		     };
		},
		watch: {
			scriptOptions: function(cur){
				this.GETONCE.length = 0;
				this.scriptOptionsData = this.formatScriptOptions(cur)
			},
			customizeScript: function(model){
				this.scriptOptionsData.forEach(item=>{
					if(item.label == model.productLv1Name){
						item.children.forEach((item, index) =>{
							if(item.label == model.productLv2Name){
								var index = item.children.findIndex(item => {
									return item.label == model.label
								})
								if(index != -1){
									item.children.splice(index,1,{
										label: model.productLv3Name + model.postfix,
										value: model.urlDefiner
									})
								}else{
									item.children.push({
										label: model.productLv3Name + model.postfix,
										value: model.urlDefiner
									})
								}
							}
						})
					}
				})
				this.GETONCE.push(model.urlDefiner)
			}
		},
		methods: {
			renderContent: function(h, {node}){
				return h('span',{
					attrs: {
						title: node.label
					},
					domProps: {
						innerHTML: node.label
					},
				})
			},
			handleNodeClick: function(node) {
				if (node.children && node.children.length != 0) return;
				this.fetchTree(node)
			},
			fetchTree: function(node) {
				var url = node.value,
					title = node.label;
				if(this.GETONCE.includes(url)){
					this.$emit('getScriptData',{
						urlDefiner: url,
						intercept: true
					})
					return
				}
				this.$emit('fetching',true)
				ajaxPromise({
					url: '/customize-script/' + url,
					type: 'post'
				}).then(data => {
					node.label = node.label + '(' + data.author + ' ' + format.formatDate(data.lastModifyTime,'yyyy-MM-dd HH:mm:ss') + ')'
					this.$emit('fetching',false)
					this.GETONCE.push(url)
					data.title = title
					data.urlDefiner = url
					data.content = data.script = data.script.replace(/\r\n/g,'\n').replace(/\r/g,'\n')
					data.successCompiled = false
					this.$emit('getScriptData',data)
				}).catch(message => {
					this.$emit('fetching',false)
					MessageBox.open(message);
				})
			},
			formatScriptOptions: function fn(obj){
				var formatTreeData = [];
				if(typeof obj == 'string')
					return obj
				for(var key in obj){
					if(typeof obj[key] != 'object'){
						formatTreeData.push({
							label: key,
							value: obj[key]
						})
					}else{
						formatTreeData.push({
							label: key,
							children: fn(obj[key])
						})
					}
				}
				return formatTreeData
			}
		}
	}
</script>
