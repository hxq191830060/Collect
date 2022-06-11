<template>
  <header class="global-header bg-white">
    <div class="global-header-inner flex-between flex-vc">
      <div class="flex-vc">
        <img alt="logo" class="logo" src="../assets/logo/logo.png" @click="router.push('/')">
        <span class="toSquare ml-32" @click="toSquare">众测广场</span>
        <span class="toSelfTask ml-32" @click="toPlatform">项目平台</span>
      </div>
      <el-popover :hide-after="20" :offset="12" :show-after="0" :show-arrow="false" :width="160" placement="bottom"
                  trigger="hover">
        <div class="popover-nickname flex-hc flex-vc mb-8 mt-8">
          <img alt="user" class="nickname-avatar mr-8" src="../assets/icon/avatar/user2.svg">
          <span>{{ nickname }}</span>
        </div>
        <div class="flex-around">
          <div class="user-info-item mt-8 mb-16">
            <p class="item-key">当前角色</p>
            <p class="item-value">{{ role === "worker" ? "众包工人" : "发包方" }}</p>
          </div>
          <div v-if="role==='worker'" class="user-info-item mt-8 mb-16">
            <p class="item-key">参与项目</p>
            <p class="item-value">{{ projectNum }}</p>
          </div>
          <div v-else-if="role==='publisher'" class="user-info-item mt-8 mb-16">
            <p class="item-key">发布项目</p>
            <p class="item-value">{{ projectNum }}</p>
          </div>
        </div>
        <div class="popover-item flex-vc flex-hc" @click="router.push('/user')">
          <img alt="img" src="../assets/icon/avatar/userInfo.svg">
          <span>个人中心</span>
        </div>
        <div class="popover-item flex-vc flex-hc" @click="router.push('/')">
          <img alt="img" src="../assets/icon/operation/switch.svg">
          <span>切换角色</span>
        </div>
        <div class="popover-item flex-vc flex-hc" @click="router.push('/login')">
          <img alt="img" src="../assets/icon/operation/exit.svg">
          <span>退出登录</span>
        </div>
        <template #reference>
          <div class="avatar-container">
            <img :src="avatar" alt="user" class="user" @click="router.push('/user')">
          </div>
        </template>
      </el-popover>
    </div>
  </header>
</template>

<script setup>

import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { computed } from "vue";

const store = useStore();
const router = useRouter();
const avatar = computed(() => store.state.userInfo.avatar);
const role = computed(() => store.state.currentUserRole);
const projectNum = computed(() => {
  if (role.value === "worker") return store.state.userInfo.acceptTaskNum;
  else if (role.value === "publisher") return store.state.userInfo.publishTaskNum;
  else return 0;
});
const nickname = computed(() => store.state.userInfo.nickname);
const targetSquarePath = {
  worker: "/worker-square",
  admin: "/admin-square",
  publisher: "/publisher-square",
};
const targetPlatformPath = {
  worker: "/worker-platform",
  admin: "/admin-platform",
  publisher: "/publisher-platform",
};
const toSquare = function() {
  router.push(targetSquarePath[role.value]);
};

const toPlatform = function() {
  router.push(targetPlatformPath[role.value]);
};


</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.global-header {
  position: sticky;
  top: 0;
  z-index: 2120;
}


.global-header-inner {
  height: 60px;
  min-width: 1200px;
  max-width: 1300px;
  width: auto;
  background-color: white;
  margin: 0 auto;

  .logo {
    height: 30px;
    object-fit: cover;
    cursor: pointer;
  }

  .avatar-container {
    display: block;
    position: relative;
    background-size: cover;
    border-radius: 50%;
    width: 40px;
    height: 40px;

    .user {
      cursor: pointer;
      position: absolute;
      width: 100%;
      height: 100%;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      border-radius: 50%;
      transition: all 0.1s ease-in-out;
    }
  }

  .toSquare, .toSelfTask {
    font-size: 16px;
    cursor: pointer;
    font-weight: 500;
    transition: all 0.2s ease-in-out;
    color: $theme-blue-pro;

    &:hover {
      color: $theme-orange;
    }
  }
}

.popover-nickname {
  font-weight: 500;
  font-size: 14px;
  color: $theme-blue-pro;

  .nickname-avatar {
    height: 24px;
    width: 24px;
  }
}

.popover-item {
  height: 36px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;

  &:hover {
    background-color: $bg-orange;
    color: $theme-orange;
  }

  img {
    height: 20px;
    width: 20px;
    margin-right: 6px;
  }
}

.user-info-item {
  width: fit-content;
  text-align: center;

  .item-key {
    color: $theme-gray-lite;
    font-size: 12px;
  }

  .item-value {
    font-weight: 500;
    margin-top: 2px;
    color: $theme-orange;
  }
}
</style>
