<template>
	<Modal v-model="visible">
		<ModalHeader title="放款批次详情"></ModalHeader>
		<ModalBody align="left">
			<div class="row-layout-detail" style="padding: 0;">
				<div class="block">
					<SystemOperateLog :for-object-uuid="loanBatchUuid"></SystemOperateLog>
				</div>
			</div>
		</ModalBody>
	</Modal>
</template>

<script>
	export default {
		components:{
		    SystemOperateLog: require('views/include/SystemOperateLog'),
		},
		props: {
			loanBatchUuid: [String, Number],
			value: Boolean
		},
		data: function() {
			return {
				visible: this.value
			};
		},
		watch: {
			loanBatchUuid: function(current) {
				console.log(current);
			},
			value: function(current) {
				this.visible = current;
			},
            visible: function(current) {
            	this.$emit('input', current);
            }
		}
	}
</script>