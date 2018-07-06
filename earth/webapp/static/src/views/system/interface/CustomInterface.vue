<style lang="sass">
	#customInterface {
		padding: 25px;

		.tree-view {
			float: left;
			margin-right: 15px;
		    margin-top: -3px;
	        border-right: 2px solid #ddd;
		}

		.editor-window {
			overflow: hidden;
		}

		#handle_vertical{
			z-index: 200;
			width: 8px;
			height: 100%;
			padding: 25px 0;
			cursor: col-resize;
			position: absolute;
			top: 0;
			// left: 0;
			margin-left: -4px;
		}
	}
</style>

<template>
	<div class="content" id="customInterface"
		@mousemove="doDrag"
		@mouseup="stopDrag">
		<TreeView
		:style="{width: treeViewWidth+'px'}"
		:scriptOptions="scriptOptions"
		:customizeScript="customizeScript"
		@getScriptData="transmitScriptData"
		@fetching="handlleFetching"></TreeView>
		<div id="handle_vertical" 
			:style="{left: treeViewWidth+25+'px'}"
			@mousedown="startDrag"></div>
		<Editor
		:scriptOptions="scriptOptions"
		:scriptData="scriptData"
		:fetching="fetching"
		@addCustomizeScript="handleAddCustomizeScript"></Editor>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';

	export default {
		components: {
			TreeView: require('./include/TreeView'),
			Editor: require('views/include/Editor/Index')
		},
		data(){
			return {
				scriptOptions: {},
				scriptData: {},
				fetching: false,
				customizeScript: {},
				clearCache: false,

				//resize reference
				treeViewWidth: 250,
				startX: 0,
				startWidth: 0,
				onDrag: false
			}
		},
		activated(){
			this.scriptOptions = {}
			ajaxPromise({
				url: `/customize-script/pre-script/getCatalog`,
				type: 'post'
			}).then(data=>{
				this.scriptOptions = data.list
			}).catch(message => {
                    MessageBox.open(message);
            })
		},
		methods: {
			transmitScriptData: function(dataObj){
				this.scriptData = dataObj
			},
			handlleFetching: function(flag){
				this.fetching = flag
			},
			handleAddCustomizeScript: function(obj){
				this.customizeScript = obj
			},

			//resize reference
			startDrag: function(e){
				this.startX = e.clientX;
				this.startWidth = parseInt(this.treeViewWidth, 10);
				this.onDrag = true;
			},
			doDrag: function(e){
				e.preventDefault()
				if(this.onDrag && this.treeViewWidth >= 250 && this.treeViewWidth <= 800){
					this.treeViewWidth = Math.min(Math.max(this.startWidth + e.clientX - this.startX, 250),800)
				}else{
					this.onDrag = false
				}
			},
			stopDrag: function(e){
				this.onDrag = false;
			}
		}
	}
</script>