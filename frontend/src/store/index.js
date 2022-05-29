import { createStore } from "vuex";
import { SET_CURRENT_USER_ROLE, SET_TOKEN, SET_USER_INFO } from "@/store/types";

export default createStore({
  state: {
    userInfo: {
      role: [],
      nickname: "",
      avatar: "",
      email: "",
      userId: "",
      introduction: "",
      publishTaskNum: 0,
      acceptTaskNum: 0,
    },
    token: localStorage.getItem("token"), //务必从localstorage而非SessionStorage中获取
    currentUserRole: "",
  },
  getters: {
    isLogin: (state) => {
      return state.token != null && state.token.length > 0;
    },
  },
  mutations: {
    //提交载荷payload应是一个对象
    [SET_USER_INFO](state, payload) {
      for (const key in state.userInfo) {
        state.userInfo[key] = payload.userInfo[key];
      }
    },
    [SET_TOKEN](state, payload) {
      state.token = payload.token;
      localStorage.setItem("token", payload.token);
    },
    [SET_CURRENT_USER_ROLE](state, payload) {
      state.currentUserRole = payload.currentUserRole;
    },
  },
  actions: {},
  modules: {},
});
