<script lang="ts" setup>
/* import { isDark } from '@/utils/is'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache' */
import { useAppStore } from '@/store/modules/app'
import { useDesign } from '@/hooks/web/useDesign'
import routerSearch from '@/components/RouterSearch/index.vue'

defineOptions({ name: 'APP' })

const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('app')
const appStore = useAppStore()
const currentSize = computed(() => appStore.getCurrentSize)
const greyMode = computed(() => appStore.getGreyMode)
// const { wsCache } = useCache()

// 根据浏览器当前主题设置系统主题色
/* const setDefaultTheme = () => {
  let isDarkTheme = wsCache.get(CACHE_KEY.IS_DARK)
  if (isDarkTheme === null) {
    isDarkTheme = isDark()
  }
  appStore.setIsDark(isDarkTheme)
}
setDefaultTheme() */
</script>
<template>
  <ConfigGlobal :size="currentSize">
    <RouterView :class="greyMode ? `${prefixCls}-grey-mode` : ''" />
    <routerSearch />
  </ConfigGlobal>
</template>
<style lang="scss">
$prefix-cls: #{$namespace}-app;

.size {
  width: 100%;
  height: 100%;
}

html,
body {
  @extend .size;

  padding: 0 !important;
  margin: 0;
  overflow: hidden;

  #app {
    @extend .size;
  }
}

.#{$prefix-cls}-grey-mode {
  filter: grayscale(100%);
}

/* 菜单折叠子菜单悬浮的背景和文字 */
.el-menu-item,.el-sub-menu__title {
  &:hover {
    color: #028077 !important;
    background-color: #fff !important;
  }
}
/* 子菜单选中的颜色 */
.el-menu-item {
  color: #fff !important;
}

/* 全局修改el-table 表头和内容颜色 header color content color */
.el-table th {
  background: #ebedf0 !important;
  .cell {
    font-size: 16px;
    color: #000000b8 !important;
    font-weight: 600;
  }
}
/* 表格内字体样式和大小 */
.el-table td {
  .cell {
    font-weight: 500;
    font-size: 16px;
    color: #000;
  }
}
/* 全局修改label字体 */
/* .el-form-item__label{
  font-size: 16px;
} */
</style>
