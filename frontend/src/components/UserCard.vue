<template>
  <div class="user-card">
    <img alt="card-img" class="card-img"
         src="https://se-3-bighomework.oss-cn-beijing.aliyuncs.com/assets/user-card-img.jpeg?OSSAccessKeyId=LTAI5t9BkdFHRepa7WyHJkpZ&Expires=3793588004&Signature=NuK%2FjN5FsWiMfOixFjtWHP603IY%3D">
    <img :src="userInfo.avatar" alt="avatar"
         class="card-avatar">
    <p class="nickname mt-16">
      {{ userInfo.nickname }}
    </p>
    <div class="user-info">
      <div class="info-item">
        <span>项目：</span>
        <span>{{ userInfo.publishTaskNum }}个</span>
      </div>
      <div class="info-item introduction">
        <span>简介：</span>
        <span>{{ userInfo.introduction }}</span>
      </div>
      <div class="info-item">
        <span>邮箱：</span>
        <span>{{ userInfo.email }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from "vue";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  userInfo: {
    type: Object,
    default: function () {
      return {
        role: "",
        nickname: "",
        introduction: "",
        publishTaskNum: 0,
        email: "",
        userId: 0,
      };
    }
  }
});

</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.user-card {
  width: 316px;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  background-color: white;
}

.card-img {
  width: 100%;
  height: 80px;
  object-fit: cover;
}

.card-avatar {
  border-radius: 50%;
  width: 66px;
  height: 66px;
  position: absolute;
  left: 16px;
  top: 60px;
}

.nickname {
  font-size: 14px;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: $theme-gray-pro;
  padding-left: 110px;
  font-weight: 500;
}

.user-info {
  margin-bottom: 24px;
  margin-top: 32px;
  box-sizing: border-box;
  padding-left: 24px;
  padding-right: 16px;


  .introduction {
    overflow: hidden;
    /*文本不会换行*/
    white-space: nowrap;
    /*当文本溢出包含元素时，以省略号表示超出的文本*/
    text-overflow: ellipsis;
  }

  .info-item {
    font-size: 14px;
    margin-bottom: 8px;

    span:first-child {
      color: $theme-gray-pro;
    }

    span:last-child {
      color: $theme-black;
    }
  }
}


</style>
