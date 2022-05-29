<template>
  <div class="task-card">
    <img :src="taskInfo.avatar" alt="avatar" class="img">
    <p class="nickname">
      {{ taskInfoView.nickname }}
    </p>
    <div class="task-info-item">
      <span class="key">任务名称：</span>
      <span class="value">{{ taskInfoView.name }}</span>
    </div>
    <div class="task-info-item">
      <span class="key">任务类型：</span>
      <span class="value">{{ taskInfoView.mode }}</span>
    </div>
    <div class="task-info-item">
      <span class="key">截止时间：</span>
      <span class="value">{{ taskInfoView.endTime }}</span>
    </div>
    <div class="worker-profit flex-between">
      <span class="worker-number">{{ taskInfoView.currentWorker }}/{{ taskInfoView.totalWorker }}人</span>
      <span class="profit">{{ taskInfoView.profit }}元</span>
    </div>
    <p class="card-divider"></p>
  </div>
</template>

<script setup>
import {computed, defineProps} from "vue";
import {deepClone} from "@/utils/utils";
// import {taskCardAvatarUrl} from "@/enums/default";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  taskInfo: {
    type: Object,
    default: function () {
      return {
        publisherId: 0,
        taskId: 0,
        avatar: "",
        nickname: "",
        name: "",
        mode: "",
        startTime: "",
        endTime: "",
        currentWorker: 0,
        totalWorker: 0,
        profit: 0
      };
    }
  }
});
const taskInfoView = computed(() => props.taskInfo);
taskInfoView.value.endTime = deepClone(props.taskInfo.endTime);

</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.task-card {
  cursor: pointer;
  width: 260px;
  border-radius: 16px;
  box-sizing: border-box;
  background-color: white;
  padding: 16px 16px 0 16px;
  position: relative;
  display: inline-block;
  transition: all ease-in-out 0.2s;

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
  }

  .img {
    width: 54px;
    height: 54px;
    border-radius: 50%;
    vertical-align: middle;
    border: 4px solid #F8F8F8;
    position: absolute;
    top: -16px;
  }

  .nickname {
    font-size: 12px;
    margin-bottom: 24px;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: $theme-gray-lite;
    padding-left: 72px;
  }

  .task-info-item {
    font-size: 14px;
    margin-bottom: 8px;

    .value {
      color: $theme-gray-pro;
    }
  }


  .worker-profit {
    margin-top: 16px;
    margin-bottom: 8px;

    .profit {
      font-size: 18px;
      color: $theme-orange;
      font-weight: 500;
    }

    .worker-number {
      font-size: 18px;
      color: $theme-blue-pro;
      font-weight: 500;

    }
  }

  .card-divider {
    position: absolute;
    width: 100%;
    height: 6px;
    background: linear-gradient(45deg, $theme-blue, $theme-orange);
    left: 0;
    bottom: 0;
    border-bottom-left-radius: 6px;
    border-bottom-right-radius: 6px;
  }
}

</style>
