<template>
  <div class="user bg-white">
    <GlobalHeader></GlobalHeader>
    <div class="page bg-white">
      <div class="user-menu flex-between mt-32">
        <div class="tab-left">
          <div :id="tabIndex===0? 'active-tab':''" class="tab-name flex-vc flex-hc" @click="tabIndex=0">
            <el-icon :size="24" class="mr-8">
              <UserFilled></UserFilled>
            </el-icon>
            <span>个人信息</span>
          </div>
          <div :id="tabIndex===1? 'active-tab':''" class="tab-name flex-vc flex-hc" @click="tabIndex=1">
            <el-icon :size="24" class="mr-8">
              <Histogram></Histogram>
            </el-icon>
            <span>偏好设置</span>
          </div>
        </div>
        <div class="tab-right">
          <div v-if="tabIndex===0" class="user-info flex-column-center">
            <p class="title-mid">
              <img alt="favicon" class="favicon" src="../../assets/logo/favicon.png">
              <span>个人信息</span>
            </p>
            <p class="title-description mt-8">您在使用 Promise 时的个人信息和偏好设置</p>
            <div class="base-info-form mt-32">
              <p class="title-small ml-32 blue-pro">基本信息</p>
              <p class="title-description ml-32 mt-8">使用 Promise 的其他用户会看到的信息</p>
              <div class="info-item avatar mt-32 flex-vc flex-between" @click="showEditAvatarDialog=true">
                <span class="info-item-key">照片</span>
                <img :src="userInfo.avatar"
                     alt="avatar">
              </div>
              <div class="info-item flex-vc flex-between" @click="showEditNicknameDialog=true">
                <span class="info-item-key">昵称</span>
                <span class="info-item-value">{{ userInfo.nickname }}</span>
              </div>
              <div class="info-item flex-vc flex-between">
                <span class="info-item-key">邮箱</span>
                <span class="info-item-value">{{ userInfo.email }}</span>
              </div>
              <div class="info-item flex-vc flex-between" @click="showEditIntroductionDialog=true">
                <span class="info-item-key">简介</span>
                <span class="info-item-value">{{ userInfo.introduction }}</span>
              </div>
            </div>
          </div>
          <div v-else-if="tabIndex===1" class="preference-settings flex-column-center">
            <p class="title-mid">
              <img alt="favicon" class="favicon" src="../../assets/logo/favicon.png">
              <span>偏好设置</span>
            </p>
            <p class="title-description mt-8">设置您的个人偏好，以接受个性化项目推荐</p>
            <div class="mt-32 task-mode-checkbox-group">
              <p class="title-small blue-pro">1.任务类型</p>
              <p class="title-description mt-8 mb-32">您所偏向接受哪些类型的任务</p>
              <div v-for="(mode,index) in taskMode" :key="index" class="check-box-container">
                <CheckboxItem
                    :checkbox-flag="userPreference.taskMode[mode.value]"
                    :checkbox-info="mode"
                    checkbox-type="taskMode"
                    @change="handleCheckboxClick"
                ></CheckboxItem>
              </div>
            </div>
            <div class="knowledge-domain-checkbox-group">
              <p class="title-small blue-pro">2.技能特长</p>
              <p class="title-description mt-8 mb-32">您所了解和擅长的知识域</p>
              <div v-for="(knowledge,index) in knowledgeDomain" :key="index" class="check-box-container">
                <CheckboxItem
                    :checkbox-flag="userPreference.knowledgeDomain[knowledge.value]"
                    :checkbox-info="knowledge"
                    checkbox-type="knowledgeDomain"
                    @change="handleCheckboxClick"
                ></CheckboxItem>
              </div>
            </div>
            <div class="test-environment-checkbox-group">
              <p class="title-small blue-pro">3.测试环境</p>
              <p class="title-description mt-8 mb-32">可用于测试的操作系统等</p>
              <div v-for="(environment,index) in testEnvironment" :key="index" class="check-box-container">
                <CheckboxItem
                    :checkbox-flag="userPreference.testEnvironment[environment.value]"
                    :checkboxInfo="environment"
                    checkbox-type="testEnvironment"
                    @change="handleCheckboxClick"
                ></CheckboxItem>
              </div>
            </div>
            <div>
              <button class="submit-preference-btn" @click="submitPreferenceSettings">提交偏好设置</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="info-dialog">
      <div class="avatar-dialog">
        <el-dialog
            v-model="showEditAvatarDialog"
            width="500px"
        >
          <img :src="userInfo.avatar" alt="avatar"
               class="avatar-dialog-img">
          <p class="avatar-img-info1 mt-32">个人资料照片</p>
          <p class="avatar-img-info2 mt-16">照片可帮助他人认出您，并可让您了解自己已登录帐号</p>
          <el-upload
              :action="actionUrl"
              :http-request="handleAvatarUploading"
              :on-success="handleAvatarUploadSuccess"
              :show-file-list="false"
              class="upload-demo width-100"
              method="put"
          >
            <button class="edit-btn mt-32">更换头像</button>
          </el-upload>
        </el-dialog>
      </div>
      <div class="nickname-dialog">
        <el-dialog
            v-model="showEditNicknameDialog"
            title="编辑昵称"
            width="500px"
        >
          <el-input v-model="editNicknameTxt"
                    :maxlength="10"
                    :minlength="4"
                    :placeholder="userInfo.nickname"
                    show-word-limit>
          </el-input>
          <p :class="showAlertColor?'red':'gray'" class="text-end alert-txt">昵称长度为4~10个字符</p>
          <button class="edit-btn mt-32" @click="onNicknameChange">确认更改</button>
        </el-dialog>
      </div>
      <div class="introduction-dialog">
        <el-dialog
            v-model="showEditIntroductionDialog"
            title="编辑简介"
            width="500px"
        >
          <el-input v-model="editIntroductionTxt" :placeholder="userInfo.introduction" maxlength="20"
                    show-word-limit></el-input>
          <button class="edit-btn mt-32" @click="onIntroductionChange">确认更改</button>
        </el-dialog>
      </div>
    </div>

  </div>
</template>

<script setup>
//TODO 如果当前用户没有头像需要显示为默认头像
import { UserFilled } from "@element-plus/icons-vue";
import { Histogram } from "@element-plus/icons-vue";
import GlobalHeader from "@/components/GlobalHeader";
import { reactive, ref, defineProps } from "vue";
import { buildRandomFileName, deepClone, ossUrlBuilder } from "@/utils/utils";
import { getOssClient } from "@/api/oss";
import ossDir from "@/enums/config/ossDir";
import { ElNotification } from "element-plus";
import { getUserInfo, updateUserInfo } from "@/api/user";
import { SET_USER_INFO } from "@/store/types";
import { useStore } from "vuex";
import CheckboxItem from "@/components/CheckboxItem";
import taskMode from "@/enums/option/checkbox/taskMode";
import knowledgeDomain from "@/enums/option/checkbox/knowledgeDomain";
import testEnvironment from "@/enums/option/checkbox/testEnvironment";

const store = useStore();
//处理tab相关
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  tabName: {
    type: String,
    default: "baseInfo",
  },
});
const tabNameIndexMap = {
  "baseInfo": 0,
  "preference": 1,
};
const tabIndex = ref(tabNameIndexMap[props.tabName]);
//tab-基本信息
const showEditAvatarDialog = ref(false);
const showEditNicknameDialog = ref(false);
const showEditIntroductionDialog = ref(false);

//页面初始加载相关
const userInfo = reactive({
  avatar: "",
  email: "",
  introduction: "",
  nickname: "",
});
const userPreference = reactive({
  taskMode: {},
  knowledgeDomain: {},
  testEnvironment: {},
});

const fetchUserInfo = async () => {
  const info = await getUserInfo();
  Object.assign(userInfo, deepClone(info.data));
  store.commit(SET_USER_INFO, { userInfo: info.data });
  const { skills, taskPreference, testDevs } = info.data;
  //设置用户偏好的初始数据
  setUserPreference(taskPreference, skills, testDevs);

  function setUserPreference(taskMode, knowledgeDomain, testEnvironment) {
    initPreferenceItem(taskMode, "taskMode");
    initPreferenceItem(knowledgeDomain, "knowledgeDomain");
    initPreferenceItem(testEnvironment, "testEnvironment");

    function initPreferenceItem(array, type) {
      array.forEach(item => {
        userPreference[type][item] = true;
      });
    }
  }
};


const setUserPreferenceMap = function() {
  setPreferenceItemMap(taskMode, "taskMode");
  setPreferenceItemMap(knowledgeDomain, "knowledgeDomain");
  setPreferenceItemMap(testEnvironment, "testEnvironment");

  function setPreferenceItemMap(preferenceArr, preferenceType) {
    preferenceArr.forEach((item) => {
      userPreference[preferenceType][item.value] = false;
    });
  }
};
const onCreate = function() {
  setUserPreferenceMap();
  fetchUserInfo();

};
onCreate();


//处理头像上传相关
const actionUrl = ref("");//http-request 会覆盖掉 action 的地址，仅当占位符用
let client = null;
let file = null;
let randomFileName = "";
const handleAvatarUploading = async function(option) {
  client = await getOssClient();
  file = option.file;
  randomFileName = buildRandomFileName(file.name);
  client.put(ossDir.avatarDir + randomFileName, file);
};
const handleAvatarUploadSuccess = function() {
  //获取新上传的头像地址
  const url = ossUrlBuilder(ossDir.avatarDir, randomFileName);
  updateUserInfo({ avatar: url }).then(response => {
    setTimeout(() => {
      showEditAvatarDialog.value = false;
    }, 500);
    if (response.code === 200) {
      ElNotification({
        type: "success",
        position: "top-left",
        duration: 3000,
        message: "更新您的头像成功",
        title: "成功",
        offset: 64,
      });
    }
    fetchUserInfo();
  });
};


//处理昵称相关
const editNicknameTxt = ref("");
const showAlertColor = ref(false);
const onNicknameChange = function() {
  if (editNicknameTxt.value.length < 4) {
    showAlertColor.value = true;
  } else {
    showAlertColor.value = false;
    updateUserInfo({ nickname: editNicknameTxt.value }).then(response => {
      setTimeout(() => {
        showEditNicknameDialog.value = false;
      }, 500);
      if (response.code === 200) {
        ElNotification({
          type: "success",
          position: "top-left",
          duration: 3000,
          message: "更新您的昵称成功",
          title: "成功",
          offset: 64,
        });
      }
      fetchUserInfo();
    });
  }
};
//处理简介相关
const editIntroductionTxt = ref("");
const onIntroductionChange = function() {
  updateUserInfo({ introduction: editIntroductionTxt.value }).then(response => {
    setTimeout(() => {
      showEditIntroductionDialog.value = false;
    }, 500);
    if (response.code === 200) {
      ElNotification({
        type: "success",
        position: "top-left",
        duration: 3000,
        message: "更新您的简介成功",
        title: "成功",
        offset: 64,
      });
    }
    fetchUserInfo();
  });
};

// eslint-disable-next-line no-unused-vars
/**
 *
 * @param type 是哪种类型的用户偏好
 * @param preferenceKey 具体用户偏好项
 * @param flag 是取消勾选还是勾选
 */
const handleCheckboxClick = function(type, preferenceKey, flag) {
  userPreference[type][preferenceKey] = flag;
};
//提交用户偏好相关
const submitPreferenceSettings = function() {
  updateUserInfo(refactorUserPreferenceToBackEndFormat(userPreference)).then((response) => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        position: "top-left",
        duration: 3000,
        message: "您的偏好设置成功",
        title: "成功",
        offset: 64,
      });
    }
  });

  function refactorUserPreferenceToBackEndFormat(userPreference) {
    const payload = {};
    payload.skills = convertPreferenceMapToList(userPreference.knowledgeDomain);
    payload.taskPreference = convertPreferenceMapToList(userPreference.taskMode);
    payload.testDevs = convertPreferenceMapToList(userPreference.testEnvironment);
    return payload;

    function convertPreferenceMapToList(preferenceMap) {
      const list = [];
      for (const key in preferenceMap) {
        if (preferenceMap[key]) {
          list.push(key);
        }
      }
      return list;
    }
  }
};

</script>

<style lang="scss" scoped>
@import "~@/views/User/style/user.scss";
</style>
