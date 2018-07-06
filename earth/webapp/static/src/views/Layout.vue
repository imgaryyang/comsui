<template>
    <div>
        <SHeader :mkey="$route.meta.mkey"></SHeader>
        <div class="web-g-main">
            <SAside :mkey="$route.meta.mkey"></SAside>
            <keep-alive>
                <router-view></router-view>
            </keep-alive>
        </div>
    </div>
</template>

<script>
    export default {
        components: {
            SHeader: require('views/include/Header'),
            SAside: require('views/include/Aside'),
        }
    }
</script>