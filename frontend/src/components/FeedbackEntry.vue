<template>
  <div class="feedback-entry flex-vc" @click="feedbackEntryClick">
    <div class="title">
      <span class="new-icon mr-8">{{ textStyle.text }}</span>
      <span>报告提交</span>
    </div>
    <div class="submit-time flex-hc">完成于：{{ feedbackEntryInfo.finishTime }}</div>
    <div class="avatar flex-vc">
      <span class="mr-16">{{ feedbackEntryInfo.nickname }}</span>
      <img :src="feedbackEntryInfo.avatar"
           alt="avatar">
    </div>
  </div>
  <el-divider></el-divider>
</template>
<script>
export default {
  name: "FeedbackEntry",
};
</script>
<script setup>
import { defineProps, defineEmits, computed } from "vue";
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  feedbackEntryInfo: {
    type: Object,
    default: function() {
      return {
        testId: "",
        workerId: "",
        finishTime: "",
        nickname: "",
        avatar: "",
        averageRate: 0.0,
        evaluationNumber: 0,
        state: "",
      };
    },
  },
});

const emit = defineEmits(["feedbackEntryClick"]);
const feedbackEntryClick = function() {
  emit("feedbackEntryClick", props.feedbackEntryInfo.testId);
};
const textStyle = computed(() => {
  if (props.feedbackEntryInfo.averageRate === 0) return {
    text: "新增",
    color: "#2C7DFA",
  };
  if (props.feedbackEntryInfo.averageRate < 6) return {
    text: "低分",
    color: "#E6A23C",
  };
  if (props.feedbackEntryInfo.averageRate < 8) return {
    text: "合格",
    color: "#67C23A",
  };
  return {
    text: "高分",
    color: "#F56C6C",
  };
});

</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.feedback-entry {
  cursor: pointer;
  height: 50px;
  transition: all 0.2s ease-in-out;
  font-size: 14px;
  box-sizing: border-box;
  padding: 0 16px;
  color: $theme-gray-pro;

  &:hover {
    background-color: $bg-orange;
  }
}

.title, .submit-time, .avatar {
  width: 33.3%;
}

.new-icon {
  color: v-bind("textStyle.color");
  border: v-bind("textStyle.color") solid 2px;
  border-radius: 14px;
  font-size: 12px;
  padding: 2px 6px;
}


.avatar {
  display: flex;
  justify-content: flex-end;

  img {
    height: 36px;
    width: 36px;
    border-radius: 50%;
  }
}
</style>
