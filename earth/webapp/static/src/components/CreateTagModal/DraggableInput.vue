<template>
	<div class="create_tag" @click="getFocus">
        <span class="draggable-title">{{draggableTitle}}:</span>
        <draggable 
            ref="reference"
            :options="dragOptions" 
            v-model="tagList" 
            :key="type + '-draggable'">
            <transition-group :type="type == 'transitivity' ? 'transition' : ''" :name="type + '-list'">
                <div 
                    class="tag" 
                    v-for="tag in tagList" 
                    :key="tag">
                    {{ tag }}
                    <span class="el-icon-close" @click="deletTag(tag)"></span>
                </div>
                <input
                    :key="type + '-input'"
                    v-model="tagName"
                    autocomplete="off"
                    class="create_input"
                    :style="{ width: inputLength + 'px' }"
                    @keyup.enter.prevent="createTag()"
                    :ref="type + 'Input'">
                </input>
            </transition-group>
        </draggable>
    </div>
</template>

<script>
    import draggable from 'vuedraggable';

	export default {
		components: {
			draggable
		},
		props: {
			draggableTitle: String,
			bothTagList: {
				type: Array,
				default: () => []
			},
			defaultData: {
				type: Array,
				default: () => []
			},
			type: {
				type: String,
				required: true
			}
		},
		data: function() {
			return {
                tagList: this.defaultData,
                tagName: '',
                inputLength: 40
			}
		},
		computed: {
			dragOptions: function() {
                return  {
                    animation: 0,
                    group: 'description',
                    ghostClass: 'ghost'
                };
            },
		},
		watch: {
			tagName: function (current) {
                if(current != ''){
                    this.inputLength = this.$refs[this.type + 'Input'].value.trim().length * 15 + 40;
                }else {
                    this.inputLength = 40;
                }
            },
            tagList: function (current) {
            	this.$emit('input', current);
            },
            defaultData: function(current) {
            	this.tagList = [].concat(current);
                this.tagName = '';
            },
		},
		methods: {
            createTag: function() {
        		if (this.tagName.trim() != '' && this.bothTagList.findIndex(item => item == this.tagName) == -1) {
        			this.tagList.push(this.tagName);
        		}
        		this.tagName = '';
        		this.inputLength = 40;
            },
            deletTag: function(tag) {
            	this.tagList = this.tagList.filter(item => item != tag);
            },
            getFocus: function() {
                this.$refs[this.type + 'Input'].focus();
            },
		}
	}
</script>