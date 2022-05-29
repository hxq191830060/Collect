<template>
  <div class="task-card-lite">
    <div class="flex-between mb-16">
      <div class="time-box">
        {{ taskInfo.endTime }} 截止
      </div>
      <div class="status-box">
        {{ taskInfo.status }}
      </div>
    </div>
    <div class="task-info-item">
      <span class="key">测试名称：</span>
      <span class="value">{{ taskInfo.name }}</span>
    </div>
    <div class="task-info-item">
      <span class="key">测试类型：</span>
      <span class="value">{{ taskInfo.mode }}</span>
    </div>
    <div class="flex-between card-bottom flex-vc">
      <div class="avatar-and-nickname flex-vc">
        <img :src="taskInfo.avatar" alt="avatar" class="img">
        <span class="nickname ml-16">{{ taskInfo.nickname }}</span>
      </div>
      <span class="worker-number">
        {{ taskInfo.currentWorker }}/{{ taskInfo.totalWorker }}人参与
      </span>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from "vue";
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  taskInfo: {
    type: Object,
    default: function () {
      return {
        endTime: "",
        status: "",
        name: "",
        mode: "",
        nickname: "",
        avatar: "",
        currentWorker: 0,
        totalWorker: 0
      };
    }
  }
});
</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.task-card-lite {
  overflow: hidden;
  width: 260px;
  border-radius: 16px;
  box-sizing: border-box;
  background-color: white;
  padding: 16px 20px 0 20px;
  transition: all ease-in-out 0.2s;
  cursor: pointer;

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
  }

  .status-box {
    font-size: 14px;
    /*
    TODO
    需要根据status改变颜色，如果是已结束就是灰色
    */
    color: $theme-orange;
  }


  .time-box {
    font-size: 10px;
    display: inline-block;
    height: 20px;
    line-height: 20px;
    border-radius: 10px;
    padding: 2px 10px;
    color: $theme-gray;
    background: $bg-gray;
  }


  .task-info-item {
    margin-bottom: 8px;
    box-sizing: border-box;
    padding: 0 6px;
    font-size: 14px;

    .value {
      color: $theme-gray-pro;
    }
  }

  .card-bottom {
    margin-top: 20px;
    margin-bottom: 12px;

    .avatar-and-nickname {
      .img {
        border-radius: 50%;
        height: 40px;
        width: 40px;
      }

      .nickname {
        font-size: 12px;
        text-overflow: ellipsis;
        white-space: nowrap;
        color: $theme-gray-pro;
      }
    }

    .worker-number {
      font-size: 12px;
      color: $theme-gray-lite;
    }
  }

}

</style>
