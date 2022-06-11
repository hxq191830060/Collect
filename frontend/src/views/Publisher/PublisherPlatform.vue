<template>
  <div class="platform">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <p class="title-small title-black-lite">我的发布</p>
      <div class="my-task-square mt-16">
        <el-tabs v-model="myTaskMode" class="demo-tabs" @tab-click="handleTabClick">
          <el-tab-pane label="已发测试" name="published">
            <div class="published-task-square">
              <div v-for="(task,index) in myActiveTaskList" :key="index" class="mt-32 mb-16 task-card-lite-box">
                <TaskCardLite :task-info="task" @click="handleTaskCardClick(task)"></TaskCardLite>
              </div>
              <div class="flex-hc mt-32">
                <el-pagination
                  v-model:current-page="myActiveTaskPagination.pageNum"
                  :hide-on-single-page="myActiveTaskPagination.totalNum<=myActiveTaskPagination.pageSize"
                  :page-size="myActiveTaskPagination.pageSize"
                  :total="myActiveTaskPagination.totalNum"
                  background
                  layout="prev, pager, next"
                  @current-change="handleActiveTaskPageChange" />
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="待发测试" name="pending">
            <div class="pending-task-square">
              <div class="mt-32 mb-16 task-card-lite-box">
                <div class="add-new-task" @click="showNewTaskDialog=true">
                  <img alt="plus" src="../../assets/icon/operation/plus.svg">
                </div>
              </div>
              <div v-for="(task,index) in myPendingTaskList" :key="index" class="mt-32 mb-16 task-card-lite-box">
                <TaskCardLite :task-info="task" @click="handleTaskCardClick(task)"></TaskCardLite>
              </div>
              <div class="pending-pagination flex-hc mt-32">
                <el-pagination
                  v-model:current-page="myPendingTaskPagination.pageNum"
                  :hide-on-single-page="myPendingTaskPagination.totalNum<=myPendingTaskPagination.pageSize"
                  :page-size="myPendingTaskPagination.pageSize"
                  :total="myPendingTaskPagination.totalNum"
                  background
                  layout="prev, pager, next"
                  @current-change="handlePendingTaskPageChange" />
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="结束测试" name="history">
            <div class="history-task-square">
              <div v-for="(task,index) in myHistoryTaskList" :key="index" class="mt-32 mb-16 task-card-lite-box">
                <TaskCardLite :task-info="task" @click="handleTaskCardClick(task)"></TaskCardLite>
              </div>
              <div class="flex-hc mt-32">
                <el-pagination
                  v-model:current-page="myHistoryTaskPagination.pageNum"
                  :hide-on-single-page="myHistoryTaskPagination.totalNum<=myHistoryTaskPagination.pageSize"
                  :page-size="myHistoryTaskPagination.pageSize"
                  :total="myHistoryTaskPagination.totalNum"
                  background
                  layout="prev, pager, next"
                  @current-change="handleHistoryTaskPageChange" />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

    </div>
    <div class="dialog">
      <el-dialog
        v-model="showNewTaskDialog"
        title="新建众测项"
        width="600px"
      >
        <div class="new-task-content">
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">项目名称：</span>
            <el-input v-model="newTaskInfo.name" :maxlength="12" placeholder="任务名称" show-word-limit />
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">项目类型：</span>
            <el-select
              v-model="newTaskInfo.mode"
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
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">终止时间：</span>
            <el-date-picker v-model="newTaskInfo.endTime"
                            :clearable="false"
                            format="YYYY-MM-DD hh:mm:ss"
                            placeholder="选择终止时间"
                            type="datetime"
                            value-format="YYYY-MM-DD hh:mm:ss"
            >
            </el-date-picker>
          </div>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">参与人数：</span>
            <el-input-number v-model="newTaskInfo.totalWorker" :min="0" controls-position="right" placeholder="参与人数" />
          </div>
        </div>
        <div v-if="!showOptionalContent" class="option-txt flex-vc" @click="showOptionalContent=true">
          <span>选填</span>
          <img alt="arrow" src="../../assets/icon/arrow/double-down.svg">
        </div>
        <div v-else class="optional-content">
          <el-divider class="mt-32 mb-32">选填 (可后续填写)</el-divider>
          <div class="flex-hc flex-vc mb-16">
            <span class="mr-16">奖励金额：</span>
            <el-input-number v-model="newTaskInfo.profit" :min="0" controls-position="right" placeholder="奖励金额/元" />
          </div>
          <div class="mb-16 flex-hc flex-vc">
            <span class="mr-16">需求描述：</span>
            <el-input
              v-model="newTaskInfo.requestDescription"
              :autosize="{minRows:4,maxRows:6}"
              maxlength="200"
              placeholder="项目简介"
              show-word-limit
              type="textarea"
            />
          </div>
        </div>
        <div class="cancel-confirm mr-16 mt-32">
          <el-button type="danger" @click="showNewTaskDialog=false">取消</el-button>
          <el-button :disabled="!createTaskAble" type="primary" @click="createNewTask">确认创建</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import taskMode from "@/enums/option/selector/taskMode";
import GlobalHeader from "@/components/GlobalHeader";
import TaskCardLite from "@/components/TaskCard2";
import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { getTasksByPublisherIdAndJudgeType, publishTask } from "@/api/task";
import { deepClone } from "@/utils/utils";
import { useStore } from "vuex";
import { ElNotification } from "element-plus";

const store = useStore();
const router = useRouter();
const publisherId = store.state.userInfo.userId;
const myTaskMode = ref("published");
const myActiveTaskList = reactive([]);
const myPendingTaskList = reactive([]);
const myHistoryTaskList = reactive([]);
const showNewTaskDialog = ref(false);

//tab切换相关
const hasRequestRecordMap = {
  pending: false,
  history: false,
};

const handleTabClick = function() {
  if (!hasRequestRecordMap[myTaskMode.value]) {
    if (myTaskMode.value === "pending") {
      fetchMyPendingTaskList(myPendingTaskPagination.pageSize, myPendingTaskPagination.pageNum);
    } else if (myTaskMode.value === "history") {
      fetchMyHistoryTaskList(myHistoryTaskPagination.pageSize, myHistoryTaskPagination.pageNum);
    }
    hasRequestRecordMap[myTaskMode.value] = true;
  }
};
const myActiveTaskPagination = reactive({ pageSize: 8, pageNum: 1, totalNum: 0 });
const fetchMyActiveTaskList = function(pageSize, pageNum) {
  getTasksByPublisherIdAndJudgeType({ publisherId, pageSize, pageNum, judgeType: 2 }).then(response => {
    myActiveTaskList.splice(0, myActiveTaskList.length);
    const { taskVOList, page } = response.data;
    deepClone(taskVOList).forEach((task) => {
      myActiveTaskList.push(task);
    });
    myActiveTaskPagination.totalNum = page.total;
  });
};
const myPendingTaskPagination = reactive({ pageSize: 7, pageNum: 1, totalNum: 0 });
const fetchMyPendingTaskList = function(pageSize, pageNum) {
  getTasksByPublisherIdAndJudgeType({ publisherId, pageSize, pageNum, judgeType: 1 }).then(response => {
    myPendingTaskList.splice(0, myPendingTaskList.length);
    const { taskVOList, page } = response.data;
    deepClone(taskVOList).forEach((task) => {
      myPendingTaskList.push(task);
    });
    myPendingTaskPagination.totalNum = page.total;
  });
};
const myHistoryTaskPagination = reactive({ pageSize: 8, pageNum: 1, totalNum: 0 });
const fetchMyHistoryTaskList = function(pageSize, pageNum) {
  getTasksByPublisherIdAndJudgeType({ publisherId, pageSize, pageNum, judgeType: 3 }).then(response => {
    myHistoryTaskList.splice(0, myHistoryTaskList.length);
    const { taskVOList, page } = response.data;
    deepClone(taskVOList).forEach((task) => {
      myHistoryTaskList.push(task);
    });
    myHistoryTaskPagination.totalNum = page.total;
  });
};
const onCreate = function() {
  fetchMyActiveTaskList(myActiveTaskPagination.pageSize, myActiveTaskPagination.pageNum);
};
onCreate();
//task-card点击后跳转相关

//页面切换相关
const handlePendingTaskPageChange = function() {
  fetchMyPendingTaskList(myPendingTaskPagination.pageSize, myPendingTaskPagination.pageNum);
};
const handleActiveTaskPageChange = function() {
  fetchMyActiveTaskList(myActiveTaskPagination.pageSize, myActiveTaskPagination.pageNum);
};
const handleHistoryTaskPageChange = function() {
  fetchMyHistoryTaskList(myHistoryTaskPagination.pageSize, myHistoryTaskPagination.pageNum);
};
const handleTaskCardClick = function(task) {
  router.push({ name: "publisher-task", params: { taskId: task.taskId }, query: { publisherId: task.publisherId } });
};


const newTaskInfo = reactive({
  publisherId: publisherId,
  name: "",
  mode: "",
  endTime: null,
  totalWorker: 0,
  profit: 0,
  startTime: "",
  requestDescription: "",
});
//是否展示选填内容
const showOptionalContent = ref(false);
//判断是否可以创建新的任务
const createTaskAble = computed(() => {
  const nameAble = newTaskInfo.name.length > 0;
  const modeAble = newTaskInfo.mode.length > 0;
  const endTimeAble = newTaskInfo.endTime != null;
  const totalWorkerAble = newTaskInfo.totalWorker > 0;
  return nameAble && modeAble && endTimeAble && totalWorkerAble;
});
//创建新的众测项目
const createNewTask = function() {
  newTaskInfo.startTime = newTaskInfo.endTime;
  publishTask(deepClone(newTaskInfo)).then(response => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "成功创建新的众测项目",
        offset: 64,
      });
      setTimeout(() => {
        showNewTaskDialog.value = false;
      }, 1000);
      fetchMyPendingTaskList(myPendingTaskPagination.pageSize, myPendingTaskPagination.pageNum);
      fetchMyHistoryTaskList(myHistoryTaskPagination.pageSize, myHistoryTaskPagination.pageNum);
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

</script>

<style lang="scss" scoped>
@import "~@/views/Publisher/style/publisherPlatform.scss";
</style>
