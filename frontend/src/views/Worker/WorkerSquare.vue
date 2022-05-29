<template>
  <div class="square">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <p class="title-small title-black-lite">全部众测</p>
      <div class="public-task-square">
        <div v-for="(task,index) in allTaskList" :key="index" class="mt-32 mb-16 task-card-box">
          <TaskCard :task-info="task" @click="handleTaskCardClick(task)"></TaskCard>
        </div>
        <div class="flex-hc mt-32">
          <el-pagination
              v-model:current-page="activeTaskPagination.pageNum"
              :hide-on-single-page="activeTaskPagination.totalNum<=activeTaskPagination.pageSize"
              :page-size="activeTaskPagination.pageSize"
              :total="activeTaskPagination.totalNum"
              background
              layout="prev, pager, next"
              @current-change="handleActiveTaskPageChange" />
        </div>
      </div>
      <p class="title-small title-black-lite mt-32">为您推荐</p>
      <div class="recommend-task-square">
        <div v-for="(task,index) in recommendTaskList" :key="index" class="mt-32 mb-16 task-card-box">
          <RecommendCard :task-info="task" @click="handleTaskCardClick(task)"></RecommendCard>
        </div>
      </div>
    </div>
    <div class="dialog">
      <div class="unset-preference-dialog">
        <el-dialog v-model="workerUnsetPreference" title="温馨提示" top="25vh" width="500px">
          <p>请先选择您的任务偏好，以接收个性化任务推荐</p>
          <p>前往个人中心-->偏好设置选择您的偏好</p>
          <template #footer>
            <button class="edit-btn mt-16" @click="toPreferencePage">立即前往</button>
          </template>
        </el-dialog>
      </div>
    </div>
    <GlobalFooter></GlobalFooter>
  </div>
</template>

<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import GlobalFooter from "@/components/GlobalFooter";
import TaskCard from "@/components/TaskCard1";
import RecommendCard from "@/components/TaskCard3";
import { getSquareActiveTasks } from "@/api/task";
import { reactive, ref } from "vue";
import { deepClone } from "@/utils/utils";
import router from "@/router";
import { useStore } from "vuex";
import { getRecommendTasks } from "@/api/recommend";

const allTaskList = reactive([]);


//task-card点击后跳转相关
const workerUnsetPreference = ref(false);
const store = useStore();
const handleTaskCardClick = function(task) {
  router.push({ name: "worker-task", params: { taskId: task.taskId }, query: { publisherId: task.publisherId } });
};
const activeTaskPagination = reactive({ pageSize: 8, pageNum: 1, totalNum: 0 });
const fetchSquareActiveTasks = function(pageSize, pageNum) {
  getSquareActiveTasks({ pageSize, pageNum }).then(response => {
    allTaskList.splice(0, allTaskList.length);
    const { page, taskVOList } = response.data;
    deepClone(taskVOList).forEach((task) => {
      allTaskList.push(task);
    });
    activeTaskPagination.totalNum = page.total;
  });
};
const recommendTaskList = reactive([]);
const fetchRecommendTaskInfo = function(userId) {
  recommendTaskList.splice(0, recommendTaskList.length);
  getRecommendTasks({ userId }).then(response => {
    //判断是否每个推荐的项目的相似度都是0，如果是的话，则弹出模态框，要求用户进行偏好设置
    workerUnsetPreference.value = response.data.every((item) => item.similarity === 0);
    deepClone(response.data).forEach(task => {
      recommendTaskList.push(task);
    });
  });
};
const onCreate = function() {
  fetchSquareActiveTasks(activeTaskPagination.pageSize, activeTaskPagination.pageNum);
  fetchRecommendTaskInfo(store.state.userInfo.userId);
};
onCreate();

const handleActiveTaskPageChange = function() {
  fetchSquareActiveTasks(activeTaskPagination.pageSize, activeTaskPagination.pageNum);
};

//新用户前往个人中心设置偏好
const toPreferencePage = function() {
  router.push({ name: "user", query: { tabName: "preference" } });
};
</script>

<style lang="scss" scoped>
@import "~@/views/Worker/style/workerSquare.scss";
</style>
