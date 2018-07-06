<style lang="sass">
	.help-popover {
		display: inline-block;
	}
</style>

<template>
	<div class="help-popover">
		<el-popover
			ref="popover"
			:placement="placement"
			:trigger="trigger"
			:width="popoverWidth"
			>
			<slot>	{{ content }} </slot>
		</el-popover>
		<i :class="customClass" v-popover:popover></i>
	</div>
</template>

<script>
	export default {
		props: {
			content: {
				type: String,
			},
			popoverWidth: {
				default: 150
			},
			trigger: {
				default: 'hover'
			},
			placement: {
				default: 'top'
			},
			customClass: {
				default: 'icon icon-help'
			}
		}
	}
</script>