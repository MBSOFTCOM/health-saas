<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled',size:'22'}" title="昵称" :rightText="user.nickname" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'phone-filled',size:'22'}" title="手机号码" :rightText="user.mobile" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'email-filled',size:'22'}" title="邮箱" :rightText="user.email" />
      <!-- <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled',size:'22'}" title="岗位" :rightText="(user.posts || []).map(post => post.name).join(',')" /> -->
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled',size:'22'}" title="角色" :rightText="(user.roles || []).map(role => role.name).join(',')" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled',size:'22'}" title="创建日期" :rightText="this.parseTime(user.createTime)" />
    </uni-list>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"
  import { parseTime } from "@/utils/ruoyi"

  export default {
    data() {
      return {
        user: {}
      }
    },
    onLoad() {
      this.getUser()
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
        })
      },
      parseTime(time) {
        return parseTime(time)
      }
    }
  }
</script>

<style lang="scss" scoped>
  page {
    background: rgba(244, 247, 252, 1);
  }
</style>
