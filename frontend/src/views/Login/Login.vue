<template>
  <div :class="signUpMode?'sign-up-mode':''" class="container">
    <div class="forms-container">
      <div class="signin-signup">
        <div class="sign-in-form form">
          <div class="tittle mb-16">
            <img alt="logo" class="logo" src="../../assets/logo/logo.png">
            <h2>众包测试平台</h2>
          </div>
          <div class="input-field flex-vc">
            <img alt="email" class="input-icon" src="../../assets/icon/information/email.svg">
            <input v-model="loginInfo.email" placeholder="邮箱" type="text" />
          </div>
          <div class="input-field flex-vc">
            <img alt="password" class="input-icon" src="../../assets/icon/information/password.svg">
            <input v-model="loginInfo.password" placeholder="密码" type="password" />
          </div>
          <button class="btn mt-16" @click="doLogin">登录</button>
        </div>
        <div class="sign-up-form form">
          <h2 class="tittle mb-16 flex-vc">
            <span>加入</span>
            <img alt="logo" class="logo" src="../../assets/logo/logo.png">
          </h2>
          <div class="input-field flex-vc">
            <img alt="nickname" class="input-icon" src="../../assets/icon/information/user.svg">
            <input v-model="registerInfo.nickname" placeholder="昵称" type="text" />
          </div>
          <div class="input-field flex-vc">
            <img alt="email" class="input-icon" src="../../assets/icon/information/email.svg">
            <input v-model="registerInfo.email" placeholder="邮箱" type="text" />
          </div>
          <div class="input-field flex-vc">
            <img alt="password" class="input-icon" src="../../assets/icon/information/password.svg">
            <input v-model="registerInfo.password" placeholder="密码" type="password" />
          </div>
          <div class="input-field flex-vc">
            <img alt="password" class="input-icon" src="../../assets/icon/information/password.svg">
            <input v-model="repeatPassword" placeholder="确认密码" type="password" />
          </div>
          <div class="flex-vc role-verification-field">
            <div class="selector-field">
              <el-select v-model="registerInfo.role" class="m-2" placeholder="用户类型" size="large">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
            <!--验证码相关-->
            <div class="verification-field flex-vc flex-between">
              <input v-model="registerInfo.verificationCode" placeholder="邮箱验证码" />
              <p v-if="!verificationCodeSend" class="verification-text" @click="beginCountDown">获取</p>
              <p v-else class="seconds-remain-text">{{ secondRemains }}</p>
            </div>
          </div>
          <button class="btn mt-16" @click="doRegister">注册</button>
        </div>
      </div>
    </div>
    <div class="panels-container">
      <div class="panel left-panel">
        <div class="content">
          <h1>新用户?</h1>
          <button id="sign-up-btn" class="btn transparent" @click="signUpMode=true">加入我们</button>
        </div>
        <img alt="" class="image" src="../../assets/images/rockets.svg" />
      </div>
      <div class="panel right-panel">
        <div class="content">
          <h1>我们中的一员?</h1>
          <button id="sign-in-btn" class="btn transparent" @click="signUpMode=false">前往登录</button>
        </div>
        <img alt="" class="image" src="../../assets/images/desk.svg" />

      </div>
    </div>
  </div>
</template>

<script setup>
import { UserRole } from "@/enums/user/userRole";
import { defineProps, reactive, ref } from "vue";
import { ElNotification } from "element-plus";
import { getVerificationCode, userLogin, userRegister } from "@/api/login";
import { deepClone } from "@/utils/utils";
import { useStore } from "vuex";
import { countDown } from "@/utils/utils";
import { SET_TOKEN } from "@/store/types";
import router from "@/router";
import Validator from "@/utils/validator";
import { formStrategies } from "@/utils/strategies";

const store = useStore();
//判断进入这个页面的时候是登录态还是注册态
const props = defineProps({
  mode: {
    type: String,
    default: "login",
  },
});
const signUpMode = ref(false);
if (props.mode === "register") signUpMode.value = true;

const roleOptions = [
  {
    value: UserRole.WORKER,
    label: "众包工人",
  },
  {
    value: UserRole.PUBLISHER,
    label: "发布者",
  },
];


//用户登录注册相关
//登录相关
const loginInfo = reactive({
  email: "",
  password: "",
});
const doLogin = async function() {
  const errMsg = checkLoginInfoValid();
  if (errMsg) {
    ElNotification({
      type: "warning",
      title: "注意",
      position: "top-right",
      duration: 3000,
      message: errMsg,

    });
  } else {
    //do request for login
    const loginRes = await userLogin(deepClone(loginInfo));
    if (loginRes.code === 200) {
      store.commit(SET_TOKEN, { token: loginRes.data });
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-right",
        duration: 3000,
        message: "登录成功",

      });
      setTimeout(() => {
        router.push("/");
      }, 1000);
    } else {
      ElNotification({
        type: "error",
        title: "注意",
        position: "top-right",
        duration: 3000,
        message: "用户名或密码错误",

      });
    }
  }

  function checkLoginInfoValid() {
    const validator = new Validator();
    validator.add(loginInfo.email, formStrategies.notEmptyStr, "请填写邮箱");
    validator.add(loginInfo.email, formStrategies.isEmail, "请填写正确的邮箱格式");
    validator.add(loginInfo.password, formStrategies.notEmptyStr, "请填写用户密码");
    return validator.startCheck();
  }
};

//注册相关
const repeatPassword = ref("");
const registerInfo = reactive({
  nickname: "",
  email: "",
  password: "",
  role: "",
  verificationCode: "",
});
const doRegister = async function() {
  const errMsg = checkRegisterInfoValid();
  if (errMsg) {
    ElNotification({
      type: "warning",
      title: "注意",
      position: "top-left",
      duration: 3000,
      message: errMsg,

    });
  } else {
    //do register post
    //这个是在回调函数中展示的
    // eslint-disable-next-line no-unused-vars
    const { nickname, email, password, role, verificationCode } = registerInfo;
    const postRegisterInfo = { nickname, email, password, role: [role], verificationCode };
    const registerRes = await userRegister(postRegisterInfo);
    if (registerRes.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "注册成功，去登录吧",

      });
      setTimeout(() => {
        signUpMode.value = false;
      }, 500);
    } else {
      ElNotification({
        type: "error",
        title: "失败",
        position: "top-left",
        duration: 3000,
        message: registerRes.message,
      });
    }
  }

  function checkRegisterInfoValid() {
    const validator = new Validator();
    validator.add(registerInfo.nickname, formStrategies.notEmptyStr, "请填写用户名");
    validator.add(registerInfo.nickname, formStrategies.betweenLength, "用户名长度应为4~10个字符", [4, 10]);
    validator.add(registerInfo.email, formStrategies.notEmptyStr, "请输入邮箱");
    validator.add(registerInfo.email, formStrategies.isEmail, "请输入正确的邮箱格式");
    validator.add(registerInfo.password, formStrategies.notEmptyStr, "请输入密码");
    validator.add(registerInfo.password, formStrategies.isPassword, "密码应不少于8位，且含大写字母、小写字母、数字");
    validator.add(repeatPassword.value, formStrategies.notEmptyStr, "请填写确认密码");
    validator.add(registerInfo.password, formStrategies.sameAsTarget, "两次密码不一致", [repeatPassword.value]);
    validator.add(registerInfo.role, formStrategies.notEmptyStr, "请选择用户类型");
    validator.add(registerInfo.verificationCode, formStrategies.notEmptyStr, "请输入验证码");
    return validator.startCheck();
  }
};


//发送验证码相关
const verificationCodeSend = ref(false);
const secondRemains = ref(60);
const beginCountDown = function() {
  const errMsg = checkVerificationInputValid();
  if (errMsg) {
    ElNotification({
      type: "warning",
      title: "注意",
      position: "top-left",
      duration: 3000,
      message: errMsg,
    });
  } else {
    //在这里请求接口
    getVerificationCode({ username: registerInfo.email }).then(response => {
      if (response.code === 200) {
        ElNotification({
          type: "success",
          title: "成功",
          position: "top-left",
          duration: 3000,
          message: "验证邮件已发送至邮箱，请注意查收",
        });
      }
      verificationCodeSend.value = true;
      const totalSeconds = secondRemains.value;
      countDown(totalSeconds, (remains) => {
        if (remains === 0) {
          verificationCodeSend.value = false;
          secondRemains.value = totalSeconds;
        } else {
          secondRemains.value = remains;
        }
      });
    });
  }

  function checkVerificationInputValid() {
    const validator = new Validator();
    validator.add(registerInfo.email, formStrategies.notEmptyStr, "请输入邮箱");
    validator.add(registerInfo.email, formStrategies.isEmail, "请输入正确的邮箱格式");
    return validator.startCheck();
  }
};

</script>

<style lang="scss" scoped>
@import "~@/views/Login/style/login.scss";
</style>
