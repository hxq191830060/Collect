<template>
  <router-view />
</template>


<script setup>
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import request from "@/plugins/axios/request";


const store = useStore();
const router = useRouter();

//Vuex相关
// 在页面加载时读取sessionStorage里的状态信息
if (sessionStorage.getItem("store")) {
  store.replaceState(Object.assign({}, store.state, JSON.parse(sessionStorage.getItem("store"))));
}
// 在页面刷新时将vuex里的信息保存到sessionStorage里
window.addEventListener("beforeunload", () => {
  sessionStorage.setItem("store", JSON.stringify(store.state));
});


//导航守卫
router.beforeEach((to) => {

  if (to.name !== "login" && to.name !== "home") {
    if (store.getters.isLogin) {
      if (to.meta.permission == null || to.meta.permission.some((role) => role === store.state.currentUserRole)) {
        return true;
      } else return { name: "login" };
    } else return { name: "login" };
  } else {
    return true;
  }
});


//请求拦截器和相应拦截器配置相关
request.interceptors.request.use(
  function(config) {
    // Do something before request is sent
    if (!(config.url === "user/login" || config.url === "user/register")) {
      config.headers = {
        Authorization: store.state.token,
      };
    }
    return config;
  },
  function(error) {
    // Do something with request error
    return Promise.reject(error);
  },
);

// Add a response interceptor
request.interceptors.response.use(
  function(response) {
    // Do something with response data
    //TODO 如果得到了401，代表token超时或者篡改了，需要强行跳转到登录页面
    if (response.data.code === 401) {
      router.push({ name: "login" });
    }
    return response;
  },
  function(error) {
    return Promise.reject(error);
  },
);

</script>


<style lang="scss">
//在单页面中引入common.scss，相当于其他子页面均引入了该样式
@import "~@/style/common.scss";
</style>
