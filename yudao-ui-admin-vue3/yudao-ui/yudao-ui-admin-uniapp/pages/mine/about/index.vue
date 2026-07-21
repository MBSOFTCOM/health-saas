<template>
  <view class="about-container">
    <view class="header-section text-center">
      <image style="width: 250px;height: 80px;" src="/static/images/tb/logo.png" mode="widthFix">
      </image>
      <uni-title type="h2" title="校园筛查管理系统"></uni-title>
    </view>

    <view class="content-section" style="font-size: 16px;">
      <view class="menu-list">
        <view class="list-cell list-cell-arrow">
          <view class="menu-item-box">
            <view>版本信息</view>
            <view class="text-right">v{{version}}</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow">
          <view class="menu-item-box">
            <view>公司地址</view>
            <view class="text-right">北京市大兴区宏坤金融谷1号楼2层</view>
          </view>
        </view>
        <view class="list-cell list-cell-arrow">
          <view class="menu-item-box">
            <view>联系电话</view>
            <view class="text-right">010-61277190</view>
          </view>
        </view>
      </view>
    </view>

    <view class="copyright" style="font-size: 16px;">
      <view> &copy; 2020 北京福乐云数据科技有限公司</view>
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        // url: this.$config.appInfo.site_url,
        version: this.$config.appInfo.version
      }
    }
  }
</script>

<style lang="scss">
  page {
    background-color: #f8f8f8;
  }

  .copyright {
    margin-top: 50rpx;
    text-align: center;
    line-height: 60rpx;
    color: #999;
  }

  .header-section {
    display: flex;
    padding: 30rpx 0 0;
    flex-direction: column;
    align-items: center;
  }
</style>
