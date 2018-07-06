<style>
    .wrap {
       padding: 30px; 
    }
</style>

<template>
    <div class="content">
        <div class="scroller">
            <!-- 路径导航 -->
            <Breadcrumb :routes="[{ title: '通道配置' }, {title: '配置通道'}]"></Breadcrumb>

            <!-- 选项卡 -->
            <div class="wrap">
                <TabMenu v-model="selected">
                    <TabMenuItem id="home">主页</TabMenuItem>
                    <TabMenuItem id="about">关于</TabMenuItem>
                </TabMenu>
                <TabContent v-model="selected">
                    <TabContentItem id="home">
                        <div>
                            <i>主页</i>
                        </div>
                    </TabContentItem>
                    <TabContentItem id="about">
                        <div>
                            <i>关于</i>
                        </div>
                    </TabContentItem>
                </TabContent>
            </div>

            <!-- 步骤卡 -->
            <div class="wrap">
                <StepTip v-model="stepIndex">
                    <StepTipItem :index="1">选择通道策略</StepTipItem>
                    <StepTipItem :index="2">配置通道</StepTipItem>
                </StepTip>
                <StepContent v-model="stepIndex">
                    <StepContentItem :index="1">
                        <div>
                            <i>选择通道策略</i>
                        </div>
                    </StepContentItem>
                    <StepContentItem :index="2">
                        <div>
                            <i>配置通道</i>
                        </div>
                    </StepContentItem>
                </StepContent>
                <StepFooter v-model="stepIndex" :step-number="2">
                </StepFooter>
            </div>

            <!-- 拖动指令 -->
            <div class="wrap">
                <div v-dragmove style="height: 50px; width: 50px; background: red;"></div>
            </div>

            <!-- Popover -->
            <div class="wrap">
             
            </div>
        </div>
    </div>
</template>

<script>
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import { StepContent, StepContentItem, StepTip, StepTipItem, StepFooter } from 'components/StepOperation';

    export default {
        components: {
            TabMenu,TabMenuItem, TabContent, TabContentItem,
            StepContent, StepContentItem, StepTip, StepTipItem, StepFooter 
        },
        data: function() {
            return { 
                selected: 'home',
                stepIndex: 1
            };
        }
    }
</script>