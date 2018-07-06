<template>
    <Modal 
        @close="$emit('close')"
        @closed="$emit('closed')"
        v-model="visible" 
        ref="modal">
        <ModalHeader :title="title"></ModalHeader>
        <ModalBody>
            <div v-if="messageIsHtml" v-html="message"></div>
            <div v-else :style="{margin: '50px 0'}" v-text="message"></div>
        </ModalBody>
        <ModalFooter>
            <el-button 
                v-for="btn in buttons" 
                :type="btn.type" 
                @click="btn.handler">
                {{ btn.text }}
            </el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { Modal, ModalBody, ModalHeader, ModalFooter } from '../Modal';

    export default {
        components: {
            Modal, ModalBody, ModalHeader, ModalFooter 
        },
        data: function() {
            return {
                visible: false,

                title: '',
                message: '',
                buttons: [],
                messageIsHtml: false
            };
        }
    }
</script>