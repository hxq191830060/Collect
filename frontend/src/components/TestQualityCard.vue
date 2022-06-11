<template>
  <div class="test-quality-card flex-between flex-vc">
    <img
      :src="avatar"
      alt="img"
      class="avatar">
    <div class="score-name">
      <p class="score">
        <span style="font-weight: 600;font-size: 24px;">{{ score.toString().substring(0, 4) }}</span>
        <span style="font-size: 14px">分</span>
      </p>
      <p class="name">
        <span class="nameTxt" style="font-weight: 500;font-size: 20px">{{ nickname }}</span>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "TestQualityCard",
};
</script>
<script setup>
import { defineProps } from "vue";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  nickname: {
    type: String,
    default: "",
  },
  score: {
    type: Number,
    default: 0,
  },
  avatar: {
    type: String,
    default: "",
  },
});
</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.test-quality-card {
  overflow: hidden;
  width: 205px;
  border-radius: 8px;
  box-sizing: border-box;
  background-color: #F3F4F9;
  padding: 8px 12px 8px 12px;
  transition: all ease-in-out 0.2s;
  cursor: pointer;

  .avatar {
    height: 52px;
    width: 52px;
    border-radius: 50%;
    border: white 2px solid;
  }

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
  }

  .score-name {
    .score {
      color: #1973e8;
      text-align: right;
    }

    .name {
      color: $theme-orange;

    }
  }

}

</style>
