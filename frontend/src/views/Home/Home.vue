<template>
  <div class="home bg-white">
    <div class="header flex-vc flex-between">
      <img alt="logo" class="logo" src="../../assets/logo/logo.png">
      <div v-if="!isLogin">
        <el-tooltip
          :width="200"
          content="请先登录"
          placement="bottom"
          trigger="click"
        >
          <img alt="avatar" class="avatar" src="../../assets/icon/avatar/user.svg">
        </el-tooltip>
      </div>
      <div v-else>
        <img :src="avatar" alt="avatar" class="avatar" @click="router.push('/user')">
      </div>
    </div>
    <div class="page flex-column-center">
      <div class="width-100 flex-column-center homeText">
        <div class="logo-box">
          <img alt="logo" class="favicon" src="../../assets/logo/favicon.png">
        </div>
        <div class="title-large title-black mt-32 show-order1">获取属于你的众包项目</div>
        <div class="title-large title-black mt-16 show-order2">简单、高效地创建与发布任务</div>
        <p class="title-description mt-16 show-order3">
          <span>简洁流程、透明信息、丰厚奖励、精准的推荐内容以及更多增值功能——</span>
          <span class="add-promise-txt" @click="router.push({name:'login',query:{mode:'register'}})">加入Promise</span>
          <span>，即可获取所有</span>
        </p>
        <div class="show-order4">
          <div v-if="!isLogin">
            <button class="btn title-small" @click="router.push('/login')">登录 / 注册</button>
          </div>
          <div v-else>
            <button v-if="userRoleList.includes('worker')" class="btn bg-blue-orange title-small"
                    @click="toSquare('worker')">进入众测者广场
            </button>
            <button v-if="userRoleList.includes('publisher')" class="btn border-blue-orange title-small"
                    @click="toSquare('publisher')">
              进入发布者广场
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { onMounted, reactive, ref } from "vue";
import { getUserInfo } from "@/api/user";
import { SET_CURRENT_USER_ROLE, SET_USER_INFO } from "@/store/types";
// import {SET_USER_INFO} from "@/store/types";

const router = useRouter();
const store = useStore();

//获取用户信息相关
const isLogin = store.getters.isLogin;
const avatar = ref("");
const userRoleList = reactive([]);
const fetchUserInfo = async () => {
  if (isLogin) {
    const userInfo = await getUserInfo();
    store.commit(SET_USER_INFO, { userInfo: userInfo.data });
    avatar.value = store.state.userInfo.avatar;
    userInfo.data.role.forEach((role) => {
      userRoleList.push(role);
    });
  }
};
fetchUserInfo();

//存在未登录的情况
const toSquare = function(role) {
  const targetSquarePath = {
    worker: "/worker-square",
    admin: "/admin-square",
    publisher: "/publisher-square",
  };
  store.commit(SET_CURRENT_USER_ROLE, { currentUserRole: role });
  router.push(targetSquarePath[role]);
};


//文字的透明度相关
const opacityController = ref(0);
onMounted(() => {
  setTimeout(() => {
    opacityController.value = 1;
  });
});
</script>

<style lang="scss" scoped>
@import "~@/views/Home/style/home.scss";

.show-order1, .show-order2, .show-order3, .show-order4 {
  opacity: v-bind(opacityController);
}

.show-order1 {
  transition: opacity 0.5s ease-in-out 0.5s;
}

.show-order2 {
  transition: opacity 0.5s ease-in-out 0.9s;

}

.show-order3 {
  transition: opacity 0.5s ease-in-out 1.3s;

}

.show-order4 {
  transition: opacity 0.5s ease-in-out 1.8s;
}

</style>
