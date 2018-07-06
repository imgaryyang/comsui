<style lang="sass" scoped>
    .highcharts-container {
        position: relative
    }
    .exportJPEG{
        position: absolute;
        right: 15px;
        bottom: 64px;
        width: 35px;
        height: 35px;
        cursor: pointer;
        background-image: url('./download.png')
    }
</style>


<template>
  <div class="highcharts-container">
    <div id="highcharts-container" style="height: 350px;"></div>
    <div class="exportJPEG" @click="exportJPEG"></div>
  </div>
</template>


<script>
    import Highcharts from 'highcharts';

    Highcharts.setOptions({
      lang:{
         resetZoom:"恢复缩放"
      }
    }); 

    export default {
        props: {
          options: {
            type: Object,
            required: true
          },
          title: {
            type: String
          }
        },
        name: 'highcharts',
        data() {
          return {
            chart: null,
          }
        },
        watch: {
          options: {
            deep: true,
            handler(){
                if (!this.chart) {
                    this.chart = new Highcharts.Chart("highcharts-container", Object.assign({},this.options))
                } else {
                    this.chart.destroy()
                    this.chart = new Highcharts.Chart("highcharts-container", Object.assign({},this.options))
                }
            }
          }
        },
        mounted() {
            this.installEvents();
        },
        beforeDestory: function() {
            $(window).off('resize', this.onResize)
            $(window).off('toggle.aside', this.onResize)  
        },
        methods: {
            installEvents() {
                var onResize = function() {
                    if (this.timer) {
                        clearTimeout(this.timer)
                    }
                    if (this.chart) {
                        this.timer = setTimeout(_ => this.chart.reflow(), 100)
                    }
                }.bind(this)
                $(window).on('toggle.aside', onResize)
                $(window).on('resize', onResize)
                this.onResize = onResize
            },
            exportJPEG(){
                this.chart.exportChartLocal({
                    type: 'image/jpeg',
                    filename: this.title || 'chart'
                });
            }
        }
    }
</script>