<template>
  <div
    :class="prefixCls"
    class="main relative h-[100%] lt-xl:bg-[var(--login-bg-color)] lt-md:px-10px lt-sm:px-10px lt-xl:px-10px"
  >
    <div class="header">
      <span>校园筛查管理系统</span>
    </div>
    <div>
      <div class="content">
        <!-- 左边图片 -->
        <div>
          <img :src="imgLogin" style="width: 590px;"/>
        </div>
        <div>
          <!-- 右边的登录界面 -->
          <Transition appear enter-active-class="animate__animated animate__bounceInRight">
            <!-- 账号登录 -->
            <LoginForm />
          </Transition>
        </div>
      </div>
    </div>
    <!-- 页脚 -->
    <div class="bottom">
      <span>技术支持：北京福乐云数据科技有限公司</span>
    </div>
  </div>
</template>
<script lang="ts" setup>
import  loginImg  from "@/assets/imgs/tb/login.png";
import { underlineToHump } from '@/utils'

import { useDesign } from '@/hooks/web/useDesign'
import { useAppStore } from '@/store/modules/app'
import { ThemeSwitch } from '@/layout/components/ThemeSwitch'
import { LocaleDropdown } from '@/layout/components/LocaleDropdown'

import { LoginForm, MobileForm, QrCodeForm, RegisterForm, SSOLoginVue } from './components'

defineOptions({ name: 'Login' })

const imgLogin = loginImg
const { t } = useI18n()
const appStore = useAppStore()
const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('login')
</script>

<style lang="scss" scoped>
.main {
  background: linear-gradient(119deg, #00e7bd -2%, #007ee5 97%);
  .content{
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-top: 100px;
  }
  .header {
    position: fixed;
    top: 0;
    height: 65px;
    width: 100%;
    background: #ffffff;
    span {
      position: absolute;
      left: 230px;
      top: 22px;
      width: 432px;
      height: 30px;
      opacity: 1;

      font-size: 36px;
      font-weight: bold;
      line-height: 17px;
      letter-spacing: 0em;

      color: #505050;
    }
  }
  .bottom {
    position: fixed;
    bottom: 0;
    height: 34px;
    width: 100%;
    background: #3a3a3a;
    color: #fff;
    font-size: 16px;
    span {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }
}
$prefix-cls: #{$namespace}-login;

.#{$prefix-cls} {
  overflow: auto;

  &__left {
    &::before {
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100%;
      height: 100%;
      background-image: url('@/assets/svgs/login-bg.svg');
      background-position: center;
      background-repeat: no-repeat;
      content: '';
    }
  }
}
</style>
