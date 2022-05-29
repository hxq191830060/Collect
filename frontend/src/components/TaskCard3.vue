<template>
  <div class="task-card-3">
    <p class="name-profit flex-vc flex-between">
      <span class="name">{{ taskInfo.name }}</span>
      <span class="profit">{{ taskInfo.profit }}元</span>
    </p>
    <p class="worker-number mt-8">匹配度：<span style="font-size: 16px">{{ `${taskInfo.similarity * 100}`.substring(0, 5)
      }}%</span></p>
    <p class="avatar-nickname-mode flex-vc">
      <img :src="taskInfo.avatar"
           alt="img"
           class="avatar">
      <span class="nickname-mode ml-8">{{ taskInfo.nickname }} · {{ taskInfo.mode }}</span>
    </p>
    <div class="introduction mt-16">
      {{ taskInfo.requestDescription }}
    </div>
  </div>
</template>
<script>
export default {
  name: "TaskCard3",
};
</script>
<script setup>
import { defineProps } from "vue";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  taskInfo: {
    type: Object,
    default: function() {
      return {
        taskId: 0,
        avatar: "",
        nickname: "",
        name: "",
        publisherId: 0,
        startTime: "",
        endTime: "",
        totalWorker: 0,
        mode: "",
        profit: 0,
        currentWorker: 0,
        cancellationTime: "",
        requestDescription: "",
        baseFunction: "",
        similarity: 0,
      };
    },
  },
});

</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.task-card-3 {
  overflow: hidden;
  width: 260px;
  border-radius: 16px;
  box-sizing: border-box;
  background-color: white;
  padding: 16px;
  transition: all ease-in-out 0.2s;
  cursor: pointer;

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
  }

  .name-profit {
    font-weight: 500;
    font-size: 16px;
  }

  .name {
    color: $theme-black-lite;
  }

  .profit {
    color: $theme-blue-pro;
  }

  .worker-number {
    font-size: 14px;
    color: $theme-orange;
    font-weight: 500;
  }


  .introduction {
    background-color: #F8F9FB;
    padding: 5px;
    box-sizing: border-box;
    font-size: 12px;
    color: $theme-gray-lite;
    width: 100%;
    height: 58px;
    max-height: 58px;
    overflow: hidden;
    text-overflow: ellipsis;
    border-radius: 8px;

  }

  .avatar-nickname-mode {
    margin-top: 12px;

    .avatar {
      height: 34px;
      width: 34px;
      border-radius: 50%;
    }

    .nickname-mode {
      font-size: 13px;
      color: $theme-gray-pro;
    }
  }
}


</style>
