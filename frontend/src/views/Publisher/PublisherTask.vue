<template>
  <div class="task">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <div class="main-content">
        <div class="name-and-join flex-between flex-vc">
          <p class="task-name">{{ taskInfo.name }}</p>
          <el-tooltip v-if="isCurrentPublisher" :disabled="publishTaskAble" content="请将项目必填信息填写完整" effect="light"
                      placement="left">
            <div class="tooltip-helper">
              <el-button
                v-if="!isPublishedTask"
                :disabled="!publishTaskAble"
                class="add-task-btn"
                type="warning"
                @click="confirmTaskPublishDialogVisible=true">发布众测
              </el-button>
              <span v-else class="have-published-task flex-vc">已发布</span>
            </div>
          </el-tooltip>
        </div>
        <div v-if="whetherOptimizeReport&&isCurrentPublisher"
             class="flex-between flex-vc report-summary-navigator mt-16">
          <div>
            <el-icon :size="20" color="#67C23A" style="vertical-align: middle">
              <SuccessFilled />
            </el-icon>
            <span>
              已生成该项目的分析报告
            </span>
          </div>
          <div class="report-summary-link" @click="navigateToReportSummary">
            <span>
              前往查看
            </span>
            <el-icon :size="22" color="#409EFC" style="vertical-align: middle">
              <Link />
            </el-icon>
          </div>

        </div>
        <div v-if="mainContentType==='tabs'" class="task-tab-box mt-16">
          <el-tabs v-model="activeTaskTab" class="demo-tabs" @tab-click="handleTaskTabClick">
            <el-tab-pane label="项目详情" name="detail">
              <div class="detail mt-16">
                <p class="detail-header ml-16 flex-between flex-vc">
                  <span class="detail-title-txt">项目详情</span>
                  <el-button v-if="isCurrentPublisher" plain type="primary"
                             @click="showEditTaskDialogBtnClick">
                    <span>编辑详情</span>
                    <el-icon class="el-icon--right">
                      <Edit></Edit>
                    </el-icon>
                  </el-button>
                </p>
                <div class="status mt-16 ml-16">
                  <span class="info-key">项目状态：</span>
                  <span id="status-color" class="info-value">{{ taskInfo.status.text }}</span>
                </div>
                <div class="mt-16 ml-16">
                  <span class="info-key">计划截止时间：</span>
                  <span class="info-value">{{ taskInfo.endTime }}</span>
                </div>
                <div class="cancellation-time mt-16 ml-16">
                  <span class="info-key">接包方最晚取消时间：</span>
                  <span
                    class="info-value">{{
                      taskInfo.cancellationTime === null ? "暂未设定" : taskInfo.cancellationTime
                    }}</span>
                  <span v-if="taskInfo.cancellationTime!==null"
                        id="cancellable-state-txt">{{ `（${cancellableState.text}）` }}</span>
                </div>
                <div class="mode mt-16 ml-16">
                  <span class="info-key">项目类型：</span>
                  <span class="info-value">{{ taskInfo.mode }}</span>
                </div>
                <div class="test-environments mt-16 ml-16">
                  <span class="info-key">测试环境：</span>
                  <span v-if="taskInfo.testEnvironments.length===0" class="test-environments-value">暂未设定</span>
                  <p v-for="(environment,index) in taskInfo.testEnvironments" v-else :key="index"
                     class="test-environments-value">{{ environment }}</p>
                </div>
                <div class="require-skills mt-16 ml-16">
                  <span class="info-key">技能要求：</span>
                  <span v-if="taskInfo.requireSkills.length===0" class="require-skills-value">暂未设定</span>
                  <p v-for="(skill,index) in taskInfo.requireSkills" v-else :key="index"
                     class="require-skills-value">{{ skill }}</p>
                </div>
                <div class="worker-number mt-16 ml-16">
                  <span class="info-key">参与人数：</span>
                  <span class="info-value">已加入：{{ taskInfo.currentWorker }}</span>
                  <span>&emsp;</span>
                  <span class="info-value">总招收：{{ taskInfo.totalWorker }}</span>
                </div>
                <div class="profit mt-16 ml-16">
                  <span class="info-key">预计奖金：</span>
                  <span class="info-value">{{ taskInfo.profit === 0 ? "暂未设定" : `${taskInfo.profit}元` }}</span>
                </div>
                <div class="request-description mt-16 ml-16">
                  <span class="info-key request-description-key">需求描述：</span>
                  <div class="request-description-value mt-8 mr-16">
                    {{ taskInfo.requestDescription === "" ? "暂未填写需求描述" : taskInfo.requestDescription }}
                  </div>
                </div>
                <div v-if="taskInfo.baseFunction.length>0" class="base-function mt-16 ml-16">
                  <span class="info-key base-function-key">基本功能：</span>
                  <div class="base-function-value mt-8 mr-16">
                    {{ taskInfo.baseFunction }}
                  </div>
                </div>
                <div v-if="taskInfo.previewShots.length>0" class="preview-shots mt-16 ml-16">
                  <span class="info-key">图片预览：</span>
                  <div class="mt-8 mr-16">
                    <div v-for="(imgUrl,index) in taskInfo.previewShots" :key="index" class="preview-shots-value mb-8"
                         @click="openTaskPreviewShotDialog(imgUrl)">
                      <img :src="imgUrl" alt="img">
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane v-if="isCurrentPublisher" label="文件列表" name="file">
              <div class="file mt-16">
                <div class="file-header ml-16 flex-between flex-vc">
                  <el-select v-model="currentFileTypeOption" placeholder="筛选文件类型">
                    <el-option
                      v-for="item in fileTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                  <div class="upload-file-btn">
                    <el-popover :width="160" placement="bottom" trigger="click">
                      <el-upload
                        :action="actionUrl"
                        :http-request="handleFileUploading"
                        :on-success="handFileUploadSuccess"
                        :show-file-list="false"
                        class="file-type-sel upload-demo width-100"
                        method="put"
                      >
                        <p class="file-type-sel" style="color:#2C7DFA" @click="uploadFileInfo.fileType=1">
                          <span>应用</span>
                          <span>待测试的可执行文件</span>
                        </p>
                        <p class="file-type-sel" style="color:#E6A23C" @click="uploadFileInfo.fileType=2">
                          <span>文档</span>
                          <span>测试内容及操作指导</span>
                        </p>
                        <p class="file-type-sel" style="color: #67C23A" @click="uploadFileInfo.fileType=3">
                          <span>附件</span>
                          <span>其他说明文件等</span>
                        </p>
                      </el-upload>
                      <template #reference>
                        <el-button plain type="primary">
                          <span>上传文件</span>
                          <el-icon class="el-icon--right">
                            <Upload></Upload>
                          </el-icon>
                        </el-button>
                      </template>
                    </el-popover>
                  </div>
                </div>
                <div class="file-list mt-32">
                  <div class="entry-type mb-16 flex-vc">
                    <span class="name">文件名</span>
                    <span class="size">文件大小</span>
                    <span class="upload-time">上传时间</span>
                    <span class="operation">操作</span>
                  </div>
                  <div v-if="filterTaskFiles.length===0" class="no-entry-txt">
                    <p class="text-center">暂无当前类型的文件，请上传</p>
                  </div>
                  <FileEntry v-for="(entry,index) in filterTaskFiles"
                             :key="index"
                             :file-entry-info="entry"
                             @downloadFileIconClick="handleDownloadFileIconClick"
                  ></FileEntry>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane v-if="isCurrentPublisher" label="测试反馈" name="feedback">
              <div v-if="feedbackEntryList.length===0" class="no-entry-txt">
                <p class="text-center">暂无测试反馈报告，需等待用户测试完毕</p>
              </div>
              <div v-for="(feedback,index) in feedbackEntryList" :key="index" class="feedback">
                <FeedbackEntry :feedback-entry-info="feedback"
                               @feedbackEntryClick="handleFeedbackEntryClick"></FeedbackEntry>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div v-else-if="mainContentType==='feedback'" class="feedback-detail-box mt-16">
          <div class="return-page-header flex-vc">
            <el-icon>
              <ArrowLeftBold></ArrowLeftBold>
            </el-icon>
            <span @click="mainContentType='tabs'">众测反馈详情</span>
          </div>
          <div class="feedback-detail mt-32">
            <FeedbackReport :feedback-report-info="feedbackReportInfo"></FeedbackReport>
          </div>
        </div>
      </div>
      <div class="right-content">
        <UserCard :user-info="publisherInfo"></UserCard>
        <div v-if="isCurrentPublisher" class="high-score-feedback-box mt-16">
          <p class="high-score-header mb-16 ml-16">
            <span class="title-small mr-16">高分报告</span>
            <span>本次测试的优质反馈报告</span>
          </p>
          <div v-if="highEvaluationFeedbackReport.length===0" class="no-high-evaluation-txt">
            <p class="text-center">当前测试暂无优质反馈报告</p>
            <p class="text-center">需等待众测者对报告进行评价和打分</p>
          </div>
          <FeedbackCard v-for="(report,index) in highEvaluationFeedbackReport"
                        :key="index"
                        :report-info="report"
                        @feedbackCardClick="handleHighRateFeedbackCardClick" />
        </div>
      </div>
    </div>
    <div class="edit-task-dialog">
      <el-dialog
        v-model="showEditTaskDialog"
        title="编辑众测项"
        width="550px"
      >
        <div class="edit-task-content">
          <div v-show="editTaskDialogPageIndex===0" class="flex-column-center">
            <el-form :model="editTaskInfo" label-position="top">
              <el-form-item label="项目名称">
                <el-input v-model="editTaskInfo.name" :maxlength="12" placeholder="任务名称" show-word-limit />
              </el-form-item>
              <el-form-item label="项目类型">
                <el-select
                  v-model="editTaskInfo.mode"
                  :reserve-keyword="false"
                  allow-create
                  default-first-option
                  filterable
                  placeholder="选择项目类型"
                >
                  <el-option-group
                    v-for="group in taskMode"
                    :key="group.label"
                    :label="group.label"
                  >
                    <el-option
                      v-for="mode in group.values"
                      :key="mode"
                      :label="mode"
                      :value="mode"
                    ></el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item label="终止时间">
                <el-date-picker v-model="editTaskInfo.endTime"
                                :clearable="false"
                                :disabled="isPublishedTask"
                                format="YYYY-MM-DD hh:mm:ss"
                                placeholder="选择终止时间"
                                type="datetime"
                                value-format="YYYY-MM-DD hh:mm:ss"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item label="可取消时间">
                <el-date-picker v-model="editTaskInfo.cancellationTime"
                                :clearable="false"
                                :disabled="isPublishedTask"
                                format="YYYY-MM-DD hh:mm:ss"
                                placeholder="用户最晚可取消该项目时间"
                                type="datetime"
                                value-format="YYYY-MM-DD hh:mm:ss"
                >
                </el-date-picker>
                <div class="cancel-time-tooltip-box">
                  <el-tooltip
                    class="cancel-time-tooltip"
                    effect="dark"
                    placement="top"
                  >
                    <template #content>
                      <p>众测者最晚取消该任务时间</p>
                      <p>晚于设定时间，则不可取消</p>
                    </template>
                    <el-icon :size="12" color="#409EFC">
                      <InfoFilled></InfoFilled>
                    </el-icon>
                  </el-tooltip>
                </div>
              </el-form-item>
            </el-form>
            <div class="flex-end width-100 mt-16">
              <el-button type="danger" @click="setEditTaskInfoDialogPage(1)">下一页</el-button>
            </div>
          </div>
          <div v-show="editTaskDialogPageIndex===1" class="flex-column-center">
            <el-form :model="editTaskInfo" label-position="top">
              <el-form-item label="技能要求">
                <el-select
                  v-model="editTaskInfo.requireSkills"
                  :reserve-keyword="false"
                  :teleported="false"
                  allow-create
                  default-first-option
                  filterable
                  multiple
                  placeholder="技能要求(多选)"
                >
                  <el-option-group
                    v-for="domain in knowledgeDomain"
                    :key="domain.label"
                    :label="domain.label"
                  >
                    <el-option
                      v-for="value in domain.values"
                      :key="value"
                      :label="value"
                      :value="value"
                    >
                      <span>{{ value }}</span>
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>
              <el-form-item label="测试环境">
                <el-select
                  v-model="editTaskInfo.testEnvironments"
                  :reserve-keyword="false"
                  allow-create
                  default-first-option
                  filterable
                  multiple
                  placeholder="测试环境(多选)"
                >
                  <el-option-group v-for="group in testEnvironment"
                                   :key="group.label"
                                   :label="group.label">
                    <el-option v-for="value in group.values"
                               :key="value"
                               :label="value"
                               :value="value">
                      <span>{{ value }}</span>
                    </el-option>
                  </el-option-group>
                </el-select>
              </el-form-item>

              <el-form-item label="招收人数">
                <el-input-number v-model="editTaskInfo.totalWorker" :disabled="isPublishedTask" :min="0"
                                 controls-position="right" placeholder="招收人数" />
              </el-form-item>
              <el-form-item label="奖励金额">
                <el-input-number v-model="editTaskInfo.profit" :min="0" controls-position="right"
                                 placeholder="奖励金额/元" />
              </el-form-item>
            </el-form>
            <div class="flex-end width-100 mt-16">
              <el-button type="primary" @click="setEditTaskInfoDialogPage(-1)">上一页</el-button>
              <el-button type="danger" @click="setEditTaskInfoDialogPage(1)">下一页</el-button>
            </div>
          </div>
          <div v-show="editTaskDialogPageIndex===2" class="flex-column-center">
            <el-form :model="editTaskInfo" label-position="top">
              <el-form-item label="需求描述">
                <el-input
                  v-model="editTaskInfo.requestDescription"
                  :autosize="{minRows:3,maxRows:4}"
                  maxlength="200"
                  placeholder="给众包工人说明的有关本次测试需求简介"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
              <el-form-item label="基本功能·选填">
                <el-input
                  v-model="editTaskInfo.baseFunction"
                  :autosize="{minRows:3,maxRows:5}"
                  maxlength="200"
                  placeholder="待测产品具备的基础功能及可提供的服务"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
              <el-form-item label="图片预览·选填">
                <div class="edit-task-picture-wall">
                  <el-upload
                    :action="actionUrl"
                    :file-list="shotsListView"
                    :http-request="handleEditTaskPreviewShotUploading"
                    :on-preview="openEditTaskPreviewShotDialog"
                    :on-remove="handleEditTaskPreviewShotRemove"
                    :on-success="handleEditTaskPreviewShotUploadSuccess"
                    :show-file-list="true"
                    class="upload-demo width-100"
                    list-type="picture-card"
                    method="put"
                  >
                    <el-icon>
                      <Plus />
                    </el-icon>
                  </el-upload>
                </div>
              </el-form-item>
            </el-form>
            <div class="flex-end width-100 mt-16">
              <el-button type="danger" @click="setEditTaskInfoDialogPage(-1)">上一页</el-button>
              <el-button type="primary" @click="editTaskDoneBtnClick">保存修改</el-button>
            </div>
          </div>
        </div>
      </el-dialog>
    </div>
    <div class="task-preview-shot-dialog">
      <el-dialog v-model="taskPreviewShotVisible">
        <img :src="taskPreviewShotUrl" alt="img" style="width: 100%" />
      </el-dialog>
    </div>
    <div class="edit-task-preview-shot-dialog">
      <el-dialog v-model="editTaskPreviewShotVisible">
        <img :src="editTaskPreviewShotUrl" alt="img" style="width: 100%" />
      </el-dialog>
    </div>
    <div class="feedback-screen-shot-dialog">
      <el-dialog v-model="showFeedbackShotDialog">
        <template #title>
          <el-button type="primary" @click="downloadShotImg">下载</el-button>
        </template>
        <img :src="feedbackShotDialogImageUrl" alt="img" style="width: 100%" />
      </el-dialog>
    </div>
    <div class="confirm-task-publish-dialog">
      <el-dialog v-model="confirmTaskPublishDialogVisible"
                 title="确认发布"
                 top="25vh"
                 width="500px"
      >
        <div class="confirm-task-publish-txt">
          <p>众测任务发布后，以下内容不可编辑和更改:</p>
          <p>1、终止时间</p>
          <p>2、可取消时间</p>
          <p>3、招收人数</p>
        </div>
        <template #footer>
          <el-button plain type="warning" @click="confirmTaskPublishDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="publishTask">确认发布</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import testEnvironment from "@/enums/option/selector/testEnvironment";
import knowledgeDomain from "@/enums/option/selector/knowledgeDomain";
import taskMode from "@/enums/option/selector/taskMode";
import UserCard from "@/components/UserCard";
import FileEntry from "@/components/FileEntry";
import FeedbackReport from "@/components/FeedbackReport";
import { getTaskInfoByTaskId, judgePublisherPublishedTaskByTaskId, updateTask, uploadTaskFile } from "@/api/task";
import { computed, defineProps, reactive, ref } from "vue";
import { getUserInfo } from "@/api/user";
import FeedbackEntry from "@/components/FeedbackEntry";
import FeedbackCard from "@/components/FeedbackCard";
import {
  buildRandomFileName,
  deepClone,
  getFileSizeString,
  judgmentCurrentTimeInterval,
  ossUrlBuilder,
} from "@/utils/utils";
import { ElNotification } from "element-plus";
import moment from "moment";
import { getOssClient } from "@/api/oss";
import ossDir from "@/enums/config/ossDir";
import { getFeedbackEntry, getFeedbackReportInfoByTestId, getHighEvaluationTest } from "@/api/test";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { getWhetherOptimizeReport } from "@/api/reportSummary";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  taskId: {
    type: String,
    default: "",
  },
  publisherId: {
    type: String,
    default: "",
  },
});
const store = useStore();
const isCurrentPublisher = parseInt(props.publisherId) === parseInt(store.state.userInfo.userId);
const activeTaskTab = ref("detail");

//main content中的展示内容
//tabs feedback
const mainContentType = ref("tabs");

//是否展示报告优化的结果相关
const whetherOptimizeReport = ref(false);
const fetchWhetherOptimizeReport = function(taskId) {
  getWhetherOptimizeReport({ taskId }).then(response => {
    whetherOptimizeReport.value = response.data;
  });
};

//获取task基本信息(项目详情tab)相关 Readonly
const taskInfo = reactive({
  taskId: props.taskId,
  publisherId: 0,
  name: "",
  mode: "",
  status: "",
  startTime: "",
  endTime: "",
  cancellationTime: null,
  totalWorker: 0,
  currentWorker: "",
  profit: 0,
  requestDescription: "",
  baseFunction: "",
  taskFileList: [],
  requireSkills: [],
  testEnvironments: [],
  previewShots: [],
});


const taskStatusTimeMap = {
  0: { text: "未开始", color: "#67C23A" },
  1: { text: "正在进行", color: "#5995fd" },
  2: { text: "已结束", color: "#F56C6C" },
};

const cancellableState = reactive({ able: false, text: "", color: "" });
const cancellableStateMap = {
  0: { able: false, text: "众测者当前不可退出", color: "#F56C6C" },
  1: { able: true, text: "众测者当前可退出", color: "#67C23A" },
};

const fetchTaskInfo = function() {
  getTaskInfoByTaskId({ taskId: props.taskId }).then(response => {
    Object.assign(taskInfo, deepClone(response.data));
    taskInfo.status = taskStatusTimeMap[judgmentCurrentTimeInterval(taskInfo.startTime, taskInfo.endTime)];
    setCancellableState();
  });

  function setCancellableState() {
    if (taskInfo.cancellationTime != null) {
      if (moment().isBefore(taskInfo.cancellationTime)) {
        Object.assign(cancellableState, cancellableStateMap[1]);
      } else {
        Object.assign(cancellableState, cancellableStateMap[0]);
      }
    }
  }
};
//获取发布者基本信息相关
const publisherInfo = reactive({
  nickname: "",
  introduction: "",
  avatar: "",
  email: "",
  publishTaskNum: 0,
});
const fetchPublisherInfo = function() {
  getUserInfo({ userId: props.publisherId }).then(response => {
    Object.assign(publisherInfo, deepClone(response.data));
  });
};
//判断任务是否已经发布相关
const isPublishedTask = ref(false);
const judgePublisherPublishedTask = function() {
  judgePublisherPublishedTaskByTaskId({ taskId: props.taskId }).then(response => {
    isPublishedTask.value = response.data;
  });
};

//获得评分较高的report相关
const highEvaluationFeedbackReport = reactive([]);
const fetchHighEvaluationTest = function() {
  getHighEvaluationTest({ taskId: props.taskId }).then(response => {
    highEvaluationFeedbackReport.splice(0, highEvaluationFeedbackReport.length);
    response.data.forEach((report) => {
      highEvaluationFeedbackReport.push(report);
    });
  });
};

const onCreate = function() {
  fetchTaskInfo();
  fetchPublisherInfo();
  fetchHighEvaluationTest();
  judgePublisherPublishedTask();
  fetchWhetherOptimizeReport(props.taskId);
};
onCreate();


//导航至报告总结跳转相关
const router = useRouter();
const navigateToReportSummary = function() {
  router.push({ name: "report-summary", params: { taskId: props.taskId } });
};

//点击应用预览截图查看Dialog相关
//TODO 这里传的参数不一定是url，还可能是file对象，可能会改动
const taskPreviewShotUrl = ref("");
const taskPreviewShotVisible = ref(false);
// eslint-disable-next-line no-unused-vars
const openTaskPreviewShotDialog = function(url) {
  taskPreviewShotUrl.value = url;
  taskPreviewShotVisible.value = true;
};

//发布众测相关
//判断是否可以发布众测，要求必填项均已经填写
const publishTaskAble = computed(() => {
  const nameAble = (taskInfo.name.length > 0);
  const modeAble = (taskInfo.mode.length > 0);
  const requireSkillsAble = taskInfo.requireSkills.length > 0;
  const cancellationTimeAble = taskInfo.cancellationTime != null;
  const endTimeAble = taskInfo.endTime.length > 0;
  const totalWorkerAble = taskInfo.totalWorker > 0;
  const profitAble = taskInfo.profit > 0;
  const testEnvironmentAble = (taskInfo.testEnvironments.length) > 0;
  const requestDescriptionAble = taskInfo.requestDescription.length > 0;
  return nameAble && testEnvironmentAble && modeAble && endTimeAble && totalWorkerAble && profitAble && requireSkillsAble && cancellationTimeAble && requestDescriptionAble;
});

//发布众测相关
//唤起发布众测的模态框
const confirmTaskPublishDialogVisible = ref(false);

const publishTask = function() {
  //发布任务信息
  confirmTaskPublishDialogVisible.value = false;//隐藏模态框
  taskInfo.startTime = moment().format("YYYY-MM-DD HH:mm:ss");
  taskInfo.testAppUrl = "";
  updateTask(deepClone(taskInfo)).then(response => {
    judgePublisherPublishedTask();
    if (response.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "发布众测成功，工人可以参与该项目",
        offset: 64,
      });
    } else {
      ElNotification({
        type: "error",
        title: "失败",
        position: "top-left",
        duration: 3000,
        message: response.message,
        offset: 64,
      });
    }
  });
};


//编辑task基本信息Dialog相关
const showEditTaskDialog = ref(false);
const editTaskInfo = reactive({
  taskId: props.taskId,
  name: "",
  mode: "",
  status: "",
  startTime: null,
  endTime: null,
  cancellationTime: null,
  totalWorker: 0,
  currentWorker: 0,
  profit: 0,
  requestDescription: "",
  baseFunction: "",
  taskFileList: [],
  publisherId: 0,
  requireSkills: [],
  previewShots: [],
  testEnvironments: [],
});
//edit taskInfo dialog 中环境要求多选框相关


//用于在edit taskInfo dialog 中一开始展示的预览图片
const shotsListView = reactive([]);
//点击编辑任务信息按钮
const showEditTaskDialogBtnClick = function() {
  showEditTaskDialog.value = true;
  //将taskInfo的所有属性均深拷贝到editTaskInfo中
  Object.assign(editTaskInfo, deepClone(taskInfo));
  //将初始预览图片置入shotsListView中用于初始展示
  shotsListView.splice(0, shotsListView.length);
  editTaskInfo.previewShots.forEach(url => {
    shotsListView.push({ url });
  });

};
//点击保存修改任务信息按钮
const editTaskDoneBtnClick = function() {
  // formattedPostDataStructure
  updateTask(deepClone(editTaskInfo)).then((response) => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "修改详情成功",
        offset: 64,
      });
      setTimeout(() => {
        showEditTaskDialog.value = false;
      }, 1000);
      fetchTaskInfo();
    } else {
      ElNotification({
        type: "error",
        title: "失败",
        position: "top-left",
        duration: 3000,
        message: response.message,
        offset: 64,
      });
    }
  });
};

//edit taskInfo dialog页面切换关
const editTaskDialogPageIndex = ref(0);
const setEditTaskInfoDialogPage = function(val) {
  editTaskDialogPageIndex.value = editTaskDialogPageIndex.value + val;
};


const actionUrl = ref("");//仅当占位符用
let client = null;
let file = null;
let randomFileName = "";
//edit taskInfo dialog 上传图片预览相关
//预览照片上传到数据库
const handleEditTaskPreviewShotUploading = async function(option) {
  console.log(1);
  client = await getOssClient();
  file = option.file;
  randomFileName = buildRandomFileName(file.name);
  client.put(ossDir.previewShotDir + randomFileName, file);
};

//预览照片上传成功后的操作
const handleEditTaskPreviewShotUploadSuccess = function() {
  const imgUrl = ossUrlBuilder(ossDir.previewShotDir, randomFileName);
  editTaskInfo.previewShots.push(imgUrl);
};

//移出照片相关
const handleEditTaskPreviewShotRemove = function(file) {
  for (let i = 0; i < editTaskInfo.previewShots.length; i++) {
    if (file.url === editTaskInfo.previewShots[i]) {
      editTaskInfo.previewShots.splice(i, 1);
      break;
    }
  }
};

//放大查看预览照片相关
//点击照片的放大查看按钮相关
const editTaskPreviewShotUrl = ref("");
const editTaskPreviewShotVisible = ref(false);
const openEditTaskPreviewShotDialog = function(file) {
  editTaskPreviewShotUrl.value = file.url;
  editTaskPreviewShotVisible.value = true;
};


//和任务tab标签页相关
//tab切换相关
const hasRequestRecordMap = {
  feedback: false,
};
const handleTaskTabClick = function() {
  if (!hasRequestRecordMap[activeTaskTab.value]) {
    if (activeTaskTab.value === "feedback") {
      fetchFeedbackEntry();
    }
  }
  hasRequestRecordMap[activeTaskTab.value] = true;
};

//获取反馈列表和反馈的详细信息相关
const feedbackEntryList = reactive([]);
const fetchFeedbackEntry = function() {
  getFeedbackEntry({ taskId: props.taskId }).then(response => {
    feedbackEntryList.splice(0, feedbackEntryList.length);
    deepClone(response.data).forEach((entry) => {
      feedbackEntryList.push(entry);
    });
  });
};
//点击download-icon，下载(查看)文件相关
const handleDownloadFileIconClick = function(url) {
  window.open(url);
};


//选择一个feedback条目，展示该feedback详情内容相关
const feedbackReportInfo = reactive({
  testId: 0,
  taskId: 0,
  workerId: 0,
  state: "",
  acceptTime: "",
  updateTime: "",
  finishTime: "",
  cancelTime: null,
  testTools: [],
  testEnvironment: "",
  conclusion: "",
  suggestion: "",
  testCollaborationRecordList: [],
  testCases: [],
  screenshots: [],
  fatal: 0,
  severe: 0,
  common: 0,
  collaborationList: [],
  slight: 0,
  total: 0,
  nickname: "",
  avatar: "",
});
const fetchFeedbackReportInfo = function(testId) {
  getFeedbackReportInfoByTestId({ testId }).then(response => {
    Object.assign(feedbackReportInfo, deepClone(response.data));
  });
};
const handleFeedbackEntryClick = function(testId) {
  mainContentType.value = "feedback";
  fetchFeedbackReportInfo(testId);
};


//点击图片展示图片dialog相关
const showFeedbackShotDialog = ref(false);
const feedbackShotDialogImageUrl = ref("");
// eslint-disable-next-line no-unused-vars
const openFeedbackShotDialog = function(url) {
  showFeedbackShotDialog.value = true;
  feedbackShotDialogImageUrl.value = url;
};
//下载图片
const downloadShotImg = function() {
  window.open(feedbackShotDialogImageUrl.value);
};


//文件上传相关
const currentFileTypeOption = ref(0);
const fileTypeOptions = [{
  value: 0,
  label: "全部文件",
}, {
  value: 1,
  label: "应用",
}, {
  value: 2,
  label: "文档",
}, {
  value: 3,
  label: "附件",
}];

//根据select选中的值筛选展示的文件相关 TODO 这里直接用数组的filter方法即可
const filterTaskFiles = computed(() => {
  if (currentFileTypeOption.value === 0) return taskInfo.taskFileList;
  const fileList = [];
  taskInfo.taskFileList.forEach(file => {
    if (file.fileType === currentFileTypeOption.value) fileList.push(file);
  });
  return fileList;
});

const uploadFileInfo = {
  taskId: props.taskId,
  url: "",
  size: "",
  name: "",
  uploadTime: "",
  fileType: 0,
};
// eslint-disable-next-line no-unused-vars
const handleFileUploading = async function(option) {
  //上传至oss
  client = await getOssClient();
  file = option.file;
  randomFileName = buildRandomFileName(file.name);
  client.put(ossDir.testFileDir + randomFileName, file);
  //设置要给后端发送的信息
  uploadFileInfo.url = ossUrlBuilder(ossDir.testFileDir, randomFileName);
  uploadFileInfo.size = getFileSizeString(file.size);
  uploadFileInfo.name = file.name;
  uploadFileInfo.uploadTime = moment().format("YYYY-MM-DD HH:mm:ss");
};
// eslint-disable-next-line no-unused-vars
const handFileUploadSuccess = function() {
  //获取新上传的文件地址
  uploadTaskFile(uploadFileInfo).then((response) => {
    if (response.code === 200) {
      fetchTaskInfo();
      ElNotification({
        type: "success",
        title: "成功",
        duration: 3000,
        position: "top-left",
        message: `成功发布${uploadFileInfo.name}`,
        offset: 64,
      });
    }
  });
};
//点击右侧高分的众测报告相关
const handleHighRateFeedbackCardClick = function(testId) {
  mainContentType.value = "feedback";
  fetchFeedbackReportInfo(testId);
};

</script>

<style lang="scss" scoped>
@import "~@/views/Publisher/style/publisherTask.scss";

#status-color {
  color: v-bind('taskInfo.status.color');
  font-weight: 500;
}


#cancellable-state-txt {
  color: v-bind("cancellableState.color");
  font-weight: 500;
}

</style>
