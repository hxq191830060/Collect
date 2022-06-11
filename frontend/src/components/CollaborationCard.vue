<template>
  <div class="collaboration-card flex-between flex-vc" @click="collaborationCardClick">
    <img :src="collaborationInfo.avatar" alt="avatar"
         class="avatar">
    <div class="right-content">
      <p class="name">{{ collaborationInfo.nickname }}完善了您的报告</p>
      <p class="updateTime">更新时间：{{ collaborationInfo.updateTime }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "CollaborationCard",
};
</script>

<script setup>
import { defineProps, defineEmits } from "vue";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  collaborationInfo: {
    type: Object,
    default: function() {
      return {
        id: 0,
        nickname: "",
        updateTime: "",
        avatar: "",
      };
    },
  },
});
const emit = defineEmits(["collaborationCardClick"]);
const collaborationCardClick = function() {
  emit("collaborationCardClick", props.collaborationInfo.id);
};
</script>
<style lang="scss" scoped>
@import "~@/style/common.scss";

.collaboration-card {
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

    .updateTime {
      color: $theme-gray-pro;
      font-size: 12px;
      margin-top: 8px;
    }
  }
}

</style>
