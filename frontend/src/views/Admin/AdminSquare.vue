<template>
  <div class="square">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <p class="title-small title-black-lite">众测广场</p>
      <div class="public-task-square">
        <div v-for="(task,index) in allTaskList" :key="index" class="mt-32 mb-16 task-card-box"
             @click="handleTaskCardClick(task)">
          <TaskCard :task-info="task"></TaskCard>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import TaskCard from "@/components/TaskCard1";
import GlobalHeader from "@/components/GlobalHeader";
import {reactive} from "vue";
import {getAllTasksList} from "@/api/task";
import {deepClone} from "@/utils/utils";
import {useRouter} from "vue-router";

const router = useRouter();
const allTaskList = reactive([]);
const getAllTasks = function () {
  getAllTasksList().then(response => {
    allTaskList.splice(0, allTaskList.length);
    deepClone(response.data).forEach(task => {
      allTaskList.push(task);
    });
  });
};


const onCreate = function () {
  getAllTasks();
};
onCreate();


const handleTaskCardClick = function (task) {
  //task-card点击后跳转相关
  router.push({name: "admin-task", params: {taskId: task.taskId}, query: {publisherId: task.publisherId}});
};
</script>

<style lang="scss" scoped>
@import "~@/views/Admin/style/adminSquare.scss";
</style>
