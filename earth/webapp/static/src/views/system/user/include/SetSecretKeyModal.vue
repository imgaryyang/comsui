<style lang="sass">
	@import '~assets/stylesheets/base.scss';

    #setSecretKeyModal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 55%;
            }
        }
    }
</style>
<template>
	<Modal v-model="show" id="setSecretKeyModal">
        <ModalHeader title="添加密钥">
        </ModalHeader>
        <ModalBody align="left">
			<SecretKey v-model="show" @showHistoryModal="$emit('showHistoryModal', arguments[0])" :principalId="principalId"></SecretKey>
        </ModalBody>
       	<ModalFooter>
            <el-button @click="show = false">关闭</el-button>
        </ModalFooter>
	</Modal>
</template>

<script>
    import SecretKey from 'views/system/personal/include/SecretKey';
	export default {
		components: {
			SecretKey
		},
		props: {
			value: false,
			principalId: null
		},
		data: function() {
			return {
				show: false,
			}
		},
		watch: {
			value: function(current) {
				this.show = current;
			},
			show: function(current) {
				if (!current) {
					this.$emit('input', current);
				}
			}
		}
	}
</script>