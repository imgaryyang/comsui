<style lang="sass">
	
</style>

<template>
	<div>
		<SketchItem v-for="(item, index) in temporaryRepurchases">
            <div class="text">
              <p>回购时间 : {{ item.repurchaseDate }}</p>
              <p>有效期 : {{ item.effectStartDate }} ~ {{ item.effectEndDate }}</p>
            </div>
            <span class="operate" v-if="canModify">
              <a class="edit" @click="onEditTask(item, index)"></a>
              <a class="delete" @click="onDeleteTask(item, index)"></a>
            </span>
        </SketchItem>
        <div style="display: flex" v-if="canModify">
            <el-button @click="onAddRepurchaseTask"><i class="el-icon-plus" style="margin-right: 10px"></i>添加回购任务</el-button>
            <span class="muted font-size-12 color-dim">可添加五个回购任务</span>
        </div>

        <EditRepurchaseTask
            v-model='repurchaseTaskModal.show'
            @submit="$emit('submitTask', arguments[0], repurchaseTaskModal.isUpdate, repurchaseTaskModal.index)"
            :is-update="repurchaseTaskModal.isUpdate"
            :model="repurchaseTaskModal.model">
        </EditRepurchaseTask>
	</div>
</template>

<script>
    import SketchItem from 'components/SketchItem';
    import EditRepurchaseTask from './EditRepurchaseTask';
    import MessageBox from 'components/MessageBox';
	
	export default {
		components: {
			SketchItem, EditRepurchaseTask
		},
		props: {
			temporaryRepurchases: {
				default: () => {[]}
			},
			canModify: {
				type: Boolean,
				default: true
			}
		},
		data: function() {
			return {
				repurchaseTaskModal: {
					show: false,
                    isUpdate: false,
                    index: -1,
                    model: {},
				}
			}
		},
		methods: {
			onEditTask: function(task, index) {
				var modal = this.repurchaseTaskModal;
                modal.show = true;
                modal.isUpdate = true;
                modal.index = index;
                modal.model = Object.assign({}, task);
			},
			onDeleteTask: function(task, index) {
				this.$emit('deleteTask', index);
			},
			onAddRepurchaseTask: function() {
				if(this.temporaryRepurchases.length > 4) {
					MessageBox.open('最多可添加五个临时回购任务','添加回购任务');
					return;
				}
				 
				var modal = this.repurchaseTaskModal;
                modal.show = true;
                modal.isUpdate = false;
                modal.model = {
                    repurchaseDate: '',
                    effectStartDate: '',
                    effectEndDate: '',
                }
			},
			closeModal: function() {
				this.repurchaseTaskModal.show = false;
			}
		}
	} 
</script>