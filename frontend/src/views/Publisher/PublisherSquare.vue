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
    </div>
    <GlobalFooter></GlobalFooter>
  </div>
</template>

<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import GlobalFooter from "@/components/GlobalFooter";
import TaskCard from "@/components/TaskCard1";
import {
  getSquareActiveTasks,
} from "@/api/task";
import { reactive } from "vue";
import { deepClone } from "@/utils/utils";
import { useRouter } from "vue-router";

const router = useRouter();

//页面内容获取的变量和方法
const allTaskList = reactive([]);


//task-card点击后跳转相关
const handleTaskCardClick = function(task) {
  router.push({ name: "publisher-task", params: { taskId: task.taskId }, query: { publisherId: task.publisherId } });
};

const activeTaskPagination = reactive({ pageSize: 8, pageNum: 1, totalNum: 0 });
//迭代二：新增pageSize和pageNum两个参数，而不是强依赖于activeTaskPagination，这是一种解耦合的方式
const fetchSquareActiveTasks = function(pageSize, pageNum) {
  getSquareActiveTasks({
    pageSize: pageSize,
    pageNum: pageNum,
  }).then(response => {
    allTaskList.splice(0, allTaskList.length);
    const { page, taskVOList } = response.data;
    activeTaskPagination.totalNum = page.total;
    deepClone(taskVOList).forEach((task) => {
      allTaskList.push(task);
    });
  });
};

const onCreate = function() {
  fetchSquareActiveTasks(activeTaskPagination.pageSize, activeTaskPagination.pageNum);
};
onCreate();

const handleActiveTaskPageChange = function() {
  fetchSquareActiveTasks(activeTaskPagination.pageSize, activeTaskPagination.pageNum);
};

</script>

<style lang="scss" scoped>
@import "~@/views/Publisher/style/publisherSquare.scss";
</style>
