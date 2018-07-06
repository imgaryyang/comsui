<style lang="sass">
	.dropdown-indicator {
		.indicator {
			display: inline-block;
		    width: 14px;
		    height: 12px;
		    margin-right: 5px;
		    background-image: url(~assets/images/icons.png);
		    background-repeat: no-repeat;
		    vertical-align: middle;
		    background-position: -172px -273px;
		}

		&:hover {
			.indicator {
				background-position: -190px -273px;
			}
		}
	}
</style>

<template>
	<el-dropdown 
		class="dropdown-indicator"
		@command="$emit('command', arguments[0])">
		<el-button type="default" size="small">
			<i class="indicator el-icon--left"></i>{{ title }}
		</el-button>
		<el-dropdown-menu slot="dropdown">
			<slot></slot>
		</el-dropdown-menu>
	</el-dropdown>
</template>

<script>
	// 名字要改为：DropdownIndicator
	export default {
		props: {
			title: {
				default: '导出Excel'
			}
		}
	}
</script>