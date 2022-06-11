<template>
  <div class="feedback-card flex-between flex-vc" @click="feedbackCardClick">
    <img :src="reportInfo.avatar" alt="avatar"
         class="avatar">
    <div class="right-content">
      <p class="name">{{ reportInfo.nickname }}的众测报告</p>
      <p class="score-number">
        <span class="score mr-8">【推荐】评分:{{ reportInfo.averageRate }}</span>
        <span class="number">共计{{ reportInfo.evaluationNumber }}人评价</span>
      </p>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  reportInfo: {
    type: Object,
    default: function() {
      return {
        testId: 0,
        nickname: "",
        averageRate: 0,
        evaluationNumber: 0,
        avatar: "",
      };
    },
  },
});

const emit = defineEmits(["feedbackCardClick"]);
const feedbackCardClick = function() {
  emit("feedbackCardClick", props.reportInfo.testId);
};
</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.feedback-card {
  padding: 0 16px;
  box-sizing: border-box;
  height: 64px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;


  &:hover {
    background-color: $bg-orange;
  }

  .avatar {
    display: block;
    height: 45px;
    width: 45px;
    border-radius: 50%;
    border: 1px solid $theme-orange;
    margin-right: 16px;
  }

  .right-content {
    height: 100%;
    width: 100%;
    padding: 8px 0;
    box-sizing: border-box;
    font-size: 14px;

    .score-number {
      color: $theme-gray-pro;
      font-size: 12px;
      margin-top: 6px;
    }

  }
}

</style>
