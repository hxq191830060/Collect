<template>
  <div class="platform">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <p class="title-small title-black-lite">我的测试</p>
      <div class="my-task-square mt-16">
        <el-tabs v-model="myTaskMode" class="demo-tabs" @tab-click="handleTabClick">
          <el-tab-pane label="正在执行" name="active">
            <div class="active-task-square">
              <div v-for="(task,index) in myActiveTestList" :key="index" class="mt-32 mb-16 task-card-lite-box">
                <TaskCardLite :task-info="task" @click="handleTaskCardClick(task)"></TaskCardLite>
              </div>
              <div class="flex-hc mt-32">
                <el-pagination
                    v-model:current-page="myActiveTestPagination.pageNum"
                    :hide-on-single-page="myActiveTestPagination.totalNum<=myActiveTestPagination.pageSize"
                    :page-size="myActiveTestPagination.pageSize"
                    :total="myActiveTestPagination.totalNum"
                    background
                    layout="prev, pager, next"
                    @current-change="handleActiveTestPageChange" />
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="历史完成" name="history">
            <div class="history-task-square">
              <div v-for="(task,index) in myHistoryTestList" :key="index" class="mt-32 mb-16 task-card-lite-box">
                <TaskCardLite :task-info="task" @click="handleTaskCardClick(task)"></TaskCardLite>
              </div>
              <div class="flex-hc mt-32">
                <el-pagination
                    v-model:current-page="myHistoryTestPagination.pageNum"
                    :hide-on-single-page="myHistoryTestPagination.totalNum<=myHistoryTestPagination.pageSize"
                    :page-size="myHistoryTestPagination.pageSize"
                    :total="myHistoryTestPagination.totalNum"
                    background
                    layout="prev, pager, next"
                    @current-change="handleHistoryTestPageChange" />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

    </div>
  </div>
</template>

<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import TaskCardLite from "@/components/TaskCard2";
import { reactive, ref } from "vue";
import { getTestsByWorkerIdAndJudgeType } from "@/api/test";
import { useStore } from "vuex";
import router from "@/router";
import { deepClone } from "@/utils/utils";

const userInfo = useStore().state.userInfo;
//控制tab相关
const myTaskMode = ref("active");
const hasRequestRecordMap = {
  history: false,
};
const handleTabClick = function() {
  if (!hasRequestRecordMap[myTaskMode.value]) {
    if (myTaskMode.value === "history") {
      fetchMyHistoryTestList(myHistoryTestPagination.pageSize, myHistoryTestPagination.pageNum);
    }
    hasRequestRecordMap[myTaskMode.value] = true;
  }
};

//我的正在执行的测试相关
const myActiveTestList = reactive([]);
const myActiveTestPagination = reactive({ pageSize: 8, pageNum: 1, totalNum: 0 });
const fetchMyActiveTestList = function(pageSize, pageNum) {
  getTestsByWorkerIdAndJudgeType({ pageSize, pageNum, workerId: userInfo.userId, judgeType: 2 }).then(response => {
    myActiveTestList.splice(0, myActiveTestList.length);
    const { taskVOList, page } = response.data;
    deepClone(taskVOList).forEach((task) => {
      myActiveTestList.push(task);
    });
    myActiveTestPagination.totalNum = page.total;
  });
};

//我的历史完成任务相关
const myHistoryTestList = reactive([]);
const myHistoryTestPagination = reactive({ pageSize: 8, pageNum: 1, totalNum: 0 });
const fetchMyHistoryTestList = function(pageSize, pageNum) {
  getTestsByWorkerIdAndJudgeType({ pageSize, pageNum, workerId: userInfo.userId, judgeType: 3 }).then(response => {
    myHistoryTestList.splice(0, myHistoryTestList.length);
    const { taskVOList, page } = response.data;
    deepClone(taskVOList).forEach((task) => {
      myHistoryTestList.push(task);
    });
    myHistoryTestPagination.totalNum = page.total;
  });
};

//初始化页面相关
const onCreate = function() {
  fetchMyActiveTestList(myActiveTestPagination.pageSize, myActiveTestPagination.pageNum);
};
onCreate();


//页面切换相关
const handleActiveTestPageChange = function() {
  fetchMyActiveTestList(myActiveTestPagination.pageSize, myActiveTestPagination.pageNum);
};
const handleHistoryTestPageChange = function() {
  fetchMyHistoryTestList(myHistoryTestPagination.pageSize, myHistoryTestPagination.pageNum);
};


//task-card点击后跳转相关
const handleTaskCardClick = function(task) {
  router.push({
    name: "worker-task",
    params: { taskId: task.taskId },
    query: { publisherId: task.publisherId },
  });
};

</script>


<style lang="scss" scoped>
@import "~@/views/Worker/style/workerPlatform.scss";
</style>
