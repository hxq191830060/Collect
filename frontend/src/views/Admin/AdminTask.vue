<template>
  <div class="task">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <div class="main-content">
        <div class="name-and-join flex-between">
          <p class="task-name">{{ taskInfo.name }}</p>
          <el-tooltip v-if="isCurrentPublisher" :disabled="publishTaskAble" content="请将项目信息填写完整" effect="light"
                      placement="left">
            <div class="tooltip-helper">
              <el-button
                  :disabled="!publishTaskAble"
                  class="add-task-btn"
                  type="warning"
                  @click="publishTask">发布众测
              </el-button>
            </div>
          </el-tooltip>
        </div>
        <div v-if="!showFeedbackDetail" class="task-tab-box mt-16">
          <el-tabs v-model="activeTaskTab" class="demo-tabs" @tab-click="handleTaskTabClick">
            <el-tab-pane label="项目详情" name="detail">
              <div class="detail mt-16">
                <p class="detail-header ml-16 flex-between flex-vc">
                  <span>项目详情</span>
                  <el-button v-if="isCurrentPublisher" plain type="primary" @click="showEditTaskDialogBtnClick">编辑详情
                  </el-button>
                </p>
                <div class="introduction mt-16">
                  {{ taskInfo.introduction.length === 0 ? "暂无简介~" : taskInfo.introduction }}
                </div>
                <div class="status mt-32 ml-16">
                  <span class="info-key">项目状态：</span>
                  <!-- <span class="circle"></span> TODO 根据时间显示不同的圆圈颜色-->
                  <span class="info-value">{{ taskInfo.status }}</span>
                  <span class="divider">|</span>
                  <span class="info-key">计划截止时间：</span>
                  <span class="info-value">{{ taskInfo.endTime }}</span>
                </div>
                <div class="mode mt-16 ml-16">
                  <span class="info-key">项目类型：</span>
                  <span class="info-value">{{ taskInfo.mode }}</span>
                </div>
                <div class="worker-number mt-16 ml-16">
                  <span class="info-key">参与人数：</span>
                  <span class="info-value">已加入：{{ taskInfo.currentWorker }}</span>
                  <span>&emsp;</span>
                  <span class="info-value">总招收：{{ taskInfo.totalWorker }}</span>
                </div>
                <div class="profit mt-16 ml-16">
                  <span class="info-key">预计奖金：</span>
                  <span class="info-value">{{ taskInfo.profit }}元</span>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="文件列表" name="file">
              <div class="file mt-16">
                <div class="file-header ml-16 flex-between flex-vc">
                  <span>全部文件</span>
                  <div v-if="isCurrentPublisher">
                    <el-upload
                        :action="actionUrl"
                        :http-request="handleFileUploading"
                        :on-success="handFileUploadSuccess"
                        :show-file-list="false"
                        class="upload-demo width-100"
                        method="put"
                    >
                      <el-button plain type="primary">上传文件</el-button>
                    </el-upload>
                  </div>
                </div>
                <div class="file-list mt-32">
                  <div class="entry-type mb-16 flex-vc">
                    <span class="name">文件名</span>
                    <span class="size">文件大小</span>
                    <span class="upload-time">上传时间</span>
                    <span class="operation">操作</span>
                  </div>
                  <FileEntry v-for="(entry,index) in taskInfo.taskDocVOS"
                             :key="index"
                             :file-entry-info="entry"
                             @downloadFileIconClick="handleDownloadFileIconClick"
                  ></FileEntry>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="测试反馈" name="feedback">
              <div v-for="(feedback,index) in feedbackEntryList" :key="index" class="feedback">
                <FeedbackEntry :feedback-entry-info="feedback"
                               @feedbackEntryClick="handleFeedbackEntryClick"></FeedbackEntry>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div v-else class="feedback-detail-box mt-16">
          <div class="return-page-header flex-vc" @click="showFeedbackDetail=false">
            <el-icon>
              <ArrowLeftBold></ArrowLeftBold>
            </el-icon>
            <span>众测反馈详情</span>
          </div>
          <div class="feedback-detail mt-32">
            <p class="feedback-detail-header ml-16">在线众测报告</p>
            <div class="feedback-detail-content">
              <div class="mt-32 ml-16">
                <span class="item-key">1.测试设备信息</span>
                <div class="item-value mt-16">
                  <el-input
                      v-model="feedbackReportInfo.deviceInfo"
                      :autosize="{ minRows: 4, maxRows: 10 }"
                      :disabled="true"
                      maxlength="500"
                      placeholder="设备信息"
                      show-word-limit
                      type="textarea"
                  />
                </div>
              </div>
              <div class="mt-32 ml-16">
                <span class="item-key">2.缺陷情况说明</span>
                <div class="item-value mt-16">
                  <el-input
                      v-model="feedbackReportInfo.caseDescription"
                      :autosize="{ minRows: 4, maxRows: 10 }"
                      :disabled="true"
                      maxlength="500"
                      placeholder="缺陷说明"
                      show-word-limit
                      type="textarea"
                  />
                </div>
              </div>
              <div class="mt-32 ml-16">
                <span class="item-key">3.缺陷复现步骤</span>
                <div class="item-value mt-16">
                  <el-input
                      v-model="feedbackReportInfo.reproduceStep"
                      :autosize="{ minRows: 4, maxRows: 10 }"
                      :disabled="true"
                      maxlength="500"
                      placeholder="复现步骤"
                      show-word-limit
                      type="textarea"
                  />
                </div>
              </div>
              <div class="mt-32 ml-16">
                <span class="item-key">4.缺陷应用截图</span>
                <div class="item-value mt-16">
                  <div v-for="(imgObj,index) in feedbackReportInfo.screenShots" :key="index"
                       class="shot-box mb-16" @click="downloadShotImg(imgObj.imgUrl)">
                    <img :src="imgObj.imgUrl" alt="img">
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="right-content">
        <UserCard :user-info="publisherInfo"></UserCard>
      </div>
    </div>
    <div class="edit-task-dialog">
      <el-dialog
          v-model="showEditTaskDialog"
          title="编辑众测项"
          width="600px"
      >
        <div class="edit-task-content">
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">项目名称：</span>
            <el-input v-model="editTaskInfo.name" placeholder="任务名称"/>
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">项目类型：</span>
            <el-input v-model="editTaskInfo.mode" placeholder="项目类型"/>
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">终止时间：</span>
            <el-date-picker v-model="editTaskInfo.endTime"
                            format="YYYY-MM-DD hh:mm:ss"
                            placeholder="选择终止时间"
                            type="datetime"
                            value-format="YYYY-MM-DD hh:mm:ss"
            >
            </el-date-picker>
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">招收人数：</span>
            <el-input v-model="editTaskInfo.totalWorker" placeholder="招收人数"/>
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">奖励金额：</span>
            <el-input v-model="editTaskInfo.profit" placeholder="奖励金额"/>
          </div>
          <div class="mb-16 flex-hc flex-vc">
            <span class="mr-16">添加简介：</span>
            <el-input
                v-model="editTaskInfo.introduction"
                :autosize="{minRows:4,maxRows:6}"
                placeholder="项目简介"
                type="textarea"
            />
          </div>
        </div>

        <div class="cancel-confirm mr-16 mt-32">
          <el-button type="danger" @click="showEditTaskDialog=false">取消</el-button>
          <el-button :disabled="!editTaskDoneAble" type="primary" @click="editTaskDoneBtnClick">确认修改</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import {ArrowLeftBold} from "@element-plus/icons-vue";
import GlobalHeader from "@/components/GlobalHeader";
import UserCard from "@/components/UserCard";
import FileEntry from "@/components/FileEntry";
import {getTaskInfoByTaskId, updateTask, uploadTaskFile} from "@/api/task";
import {computed, defineProps, reactive, ref} from "vue";
import {getUserInfo} from "@/api/user";
import FeedbackEntry from "@/components/FeedbackEntry";
import {
  buildRandomFileName,
  deepClone,
  getFileSizeString,
  judgmentCurrentTimeInterval,
  ossUrlBuilder
} from "@/utils/utils";
import {ElNotification} from "element-plus";
import moment from "moment";
import {getOssClient} from "@/api/oss";
import ossDir from "@/enums/config/ossDir";
import {getTestByWorkerIdAndTaskId} from "@/api/test";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  taskId: {
    type: String,
    default: ""
  },
  publisherId: {
    type: String,
    default: ""
  }
});
const isCurrentPublisher = false;
const activeTaskTab = ref("detail");

//获取task基本信息(项目详情tab)相关
const taskInfo = reactive({
  taskId: props.taskId,
  name: "",
  mode: "",
  status: "",
  startTime: "",
  endTime: "",
  totalWorker: 0,
  currentWorker: "",
  profit: 0,
  introduction: "",
  taskDocVOS: "",
  publisherId: 0
});

const timeMap = {
  0: "未开始",
  1: "正在进行",
  2: "已结束"
};

const fetchTaskInfo = function () {
  getTaskInfoByTaskId({taskId: props.taskId}).then(response => {
    Object.assign(taskInfo, deepClone(response.data));
    taskInfo.status = timeMap[judgmentCurrentTimeInterval(taskInfo.startTime, taskInfo.endTime)];
    console.log(taskInfo.status);
  });
};
//获取发布者基本信息相关
const publisherInfo = reactive({
  nickname: "",
  projectNum: "",
  introduction: "",
  avatar: "",
  email: ""
});
const fetchPublisherInfo = function () {
  getUserInfo({userId: props.publisherId}).then(response => {
    Object.assign(publisherInfo, deepClone(response.data));
  });
};

const onCreate = function () {
  fetchTaskInfo();
  fetchPublisherInfo();
};
onCreate();

//发布众测相关
//判断是否可以发布众测
const publishTaskAble = computed(() => {
  const nameAble = (taskInfo.name.length > 0);
  const modeAble = (taskInfo.mode.length > 0);
  const endTimeAble = (taskInfo.endTime.length > 0);
  const totalWorkerAble = parseInt(taskInfo.totalWorker) > 0;
  const profitAble = parseInt(taskInfo.profit) > 0;
  const introductionAble = taskInfo.introduction.length > 0;
  return nameAble && modeAble && endTimeAble && totalWorkerAble && profitAble && introductionAble;
});

const publishTask = function () {
  //发布任务信息
  taskInfo.startTime = moment().format("YYYY-MM-DD HH:mm:ss");
  taskInfo.testAppUrl = "";
  updateTask(deepClone(taskInfo)).then(response => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "发布众测成功，工人可以参与该项目"
      });
    }
  });
};


//编辑task基本信息相关
const showEditTaskDialog = ref(false);
const editTaskInfo = reactive({});
const showEditTaskDialogBtnClick = function () {//点击编辑信息按钮
  showEditTaskDialog.value = true;
  Object.assign(editTaskInfo, deepClone(taskInfo));
  if (editTaskInfo.profit === 0) editTaskInfo.profit = "";
  if (editTaskInfo.totalWorker === 0) editTaskInfo.totalWorker = "";
};
const editTaskDoneBtnClick = function () {//点击确认修改信息按钮
  editTaskInfo.testAppUrl = "";
  editTaskInfo.profit = parseInt(editTaskInfo.profit);
  updateTask(deepClone(editTaskInfo)).then((response) => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "修改详情成功"
      });
      setTimeout(() => {
        showEditTaskDialog.value = false;
      }, 1000);
      fetchTaskInfo();
    }
  });
};
const editTaskDoneAble = computed(() => {
      const nameAble = (editTaskInfo.name.length > 0);
      const modeAble = (editTaskInfo.mode.length > 0);
      const endTimeAble = (editTaskInfo.endTime.length > 0);
      const totalWorkerAble = parseInt(editTaskInfo.totalWorker) > 0;
      const profitAble = parseInt(editTaskInfo.profit) > 0;
      const introductionAble = (editTaskInfo.introduction.length > 0);
      return nameAble && modeAble && endTimeAble && totalWorkerAble && profitAble && introductionAble;
    }
);


//和任务tab标签页相关
//tab切换相关
const hasRequestRecordMap = {
  feedback: false
};
const handleTaskTabClick = function () {
  if (!hasRequestRecordMap[activeTaskTab.value]) {
    fetchFeedbackEntry();
  }
  hasRequestRecordMap[activeTaskTab.value] = true;
};

//获取反馈列表和反馈的详细信息相关
const feedbackEntryList = reactive([]);
const fetchFeedbackEntry = function () {
  getTestByWorkerIdAndTaskId({taskId: props.taskId}).then(response => {
    feedbackEntryList.splice(0, feedbackEntryList.length);
    console.log(response.data);
    deepClone(response.data).forEach((entry) => {
      feedbackEntryList.push(entry);
    });
  });
};


//点击download-icon，下载(查看)文件相关
const handleDownloadFileIconClick = function (url) {
  window.open(url);
};


//查看由worker提交的测试报告相关
const showFeedbackDetail = ref(false);
const feedbackReportInfo = reactive({
  deviceInfo: "",
  caseDescription: "",
  reproduceStep: "",
  screenShots: []
});


//点击图片下载
const downloadShotImg = function (url) {
  window.open(url);
};

const handleFeedbackEntryClick = function (info) {//todo do request and show dialog
  //TODO do get 获取当前id下feedback详情
  showFeedbackDetail.value = true;
  Object.assign(feedbackReportInfo, info);
};


//文件上传相关
const actionUrl = ref("");//http-request 会覆盖掉 action 的地址，仅当占位符用
let client = null;
let file = null;
let randomFileName = "";
const uploadFileInfo = {
  taskId: props.taskId,
  url: "",
  size: "",
  name: "",
  uploadTime: ""
};
const handleFileUploading = async function (option) {
  client = await getOssClient();
  file = option.file;
  randomFileName = buildRandomFileName(file.name);
  client.put(ossDir.testFileDir + randomFileName, file);
};
const handFileUploadSuccess = function () {
  //获取新上传的文件地址
  uploadFileInfo.url = ossUrlBuilder(ossDir.testFileDir, randomFileName);
  uploadFileInfo.size = getFileSizeString(file.size);
  uploadFileInfo.name = file.name;
  uploadFileInfo.uploadTime = moment().format("YYYY-MM-DD HH:mm:ss");
  uploadTaskFile(uploadFileInfo).then((response) => {
    if (response.code === 200) {
      fetchTaskInfo();
      ElNotification({
        type: "success",
        title: "成功",
        duration: 3000,
        position: "top-left",
        message: `成功发布${uploadFileInfo.name}`
      });
    }
  });
};

</script>

<style lang="scss" scoped>
@import "~@/views/Admin/style/adminTask.scss";
</style>
