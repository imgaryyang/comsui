<style lang="sass">
    #json2graph {
        .editor-window{
            float: left;
            width: 50%;

            .editor-tab{
                height: 33px;
            }
            .editor-viewport{
                height: 450px;
            }
            .editor-control {
                .tab-content-item {
                    border-width: 0 1px 1px;
                }
            }
        }
    }
</style>

<template>
	<div class="content" id="json2graph">
		<div class="scroller" style="padding: 20px">
	    	<div class="editor-window">
				<editor-tab
					:views="views"
					@select="handleSelect"
					@run="handleRun"></editor-tab>
				<editor-viewport
					ref="viewport"
					:model="currentView"
					:errorline="errorline"
					@change="handleChange"></editor-viewport>
				<editor-control
		            :runResult="runResult"
		            :waitingResult="waitingResult">
	    	</div>
			<Graph 
                v-model="computeTypeFlag"
                :title="graphTitle"
                :dataSource="dataSource"
                :waitingResult="waitingResult"></Graph>
	    </div>
	</div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';

	export default{
		components: {
			'editor-viewport': require('./include/Viewport'),
            Graph: require('./include/Graph'),
            'editor-tab': require('./include/EditorTab'),
            'editor-control': require('views/include/Editor/Control'),
		},
		data(){
			var preViews = [{
				title: '动态池逾期率',
				selected: true,
				content: 
`{
  "computeTypeFlag": 0,
  "financialContractUuid": "",
  "includeRepurchase": false,
  "includeUnconfirmed": false,
  "overdueStage": "0,1,2,3",
  "periodDays": 30
}`
			}, {
				title: '月度静态池逾期率',
				selected: false,
				content: 
`{
  "computeTypeFlag": 0,
  "financialContractUuid": "",
  "includeRepurchase": false,
  "includeUnconfirmed": false,
  "month": 5,
  "overdueStage": "0,1,2,3",
  "periodDays": 30,
  "year": 2017
}`
			}]
			return {
				views: preViews,
				scrollInfo:{},
				runResult: {
                    result: '',
                    error: '',
                    log: ''
                },
                errorline: undefined,
                waitingResult: false,
                computeTypeFlag: '0',
                graphTitle: '',
                dataSource: [],
                result: [],
			}
		},
        computed: {
            currentView: function() {
                var selected = this.views.filter(view => view.selected === true);
                return selected ? selected[0] : null;
            }
        },
        watch: {
            computeTypeFlag: function(){
                this.handleRun()
            }
        },
		methods: {
            handleSelect: function(targetView) {
                var codeDOC = this.$refs.viewport.mainCodeMirror.doc
                this.scrollInfo ?  this.scrollInfo[this.currentView.title] = {
                    left : codeDOC.scrollLeft,
                    top : codeDOC.scrollTop
                }: ''
                this.views.forEach(item => item.selected = false)
                targetView.selected = true;
                if(targetView.title in this.scrollInfo){
                    this.$nextTick(() => {
                        codeDOC.cm.scrollTo(this.scrollInfo[targetView.title].left,this.scrollInfo[targetView.title].top)
                    })
                }
            },
            handleChange: function(cm, model, changeObj) {
                if (!this.views.includes(model)) {
                    model.selected = true; // ?
                    this.views.push(model);
                }
                model.content = cm.getValue();
            },
            handleRun: function(){
                if (this.waitingResult) {
                    return
                }
                if (!this.currentView) {
                    return
                }
                
                this.runResult = Object.assign(this.runResult,{
                    result: '',
                    error: '',
                    log: ''
                })
                this.waitingResult = true
                try{
                    var j = JSON.parse(this.currentView.content)
                }catch(e){
                    this.runResult.error = e.message
                    this.waitingResult = false
                    return
                }
                var action = ""
                action = this.currentView.title === '动态池逾期率'
                    ? '/spark/OverdueAnalyze/DynamicOverdueRate'
                    : '/spark/OverdueAnalyze/StaticOverdueRateOfMonth'
                this.dataSource = []
                ajaxPromise({
                    url: action,
                    data: JSON.stringify(Object.assign({}, j, {
                        computeTypeFlag: +this.computeTypeFlag
                    })),
                    contentType: 'application/json',
                    dataType: 'json',
                    type: 'post'
                }).then(data =>{
                    this.runResult.result = '成功'
                    this.runResult.error = ''
                    this.graphTitle = this.currentView.title
                    this.dataSource = data
                }).catch(e =>{
                    this.runResult.result = ''
                    this.runResult.error = `${e.xhr.status} ${e.xhr.statusText} ${e.message}`
                }).then(_ => {
                    this.waitingResult = false
                })

                // setTimeout(()=>{
                //     this.runResult.result = '成功'
                //     this.waitingResult = false
                //     this.graphTitle = this.currentView.title
                //     this.dataSource = this.result.slice(0, 18)
                //     console.log(this.result.slice(0, 18))
                // }, 1000)
            }
		}
	}

</script>