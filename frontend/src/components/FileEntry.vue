<template>
  <div class="file-entry flex-vc">
    <div class="name">
      <span class="file-type mr-8">{{ fileType.text }}</span>
      <span>{{ fileEntryInfo.name }}</span>
    </div>
    <span class="size">{{ fileEntryInfo.size }}</span>
    <span class="upload-time">{{ fileEntryInfo.uploadTime }}</span>
    <span class="operation">
      <img alt="download" src="../assets/icon/operation/download.svg" @click="downloadIconClick">
    </span>
  </div>
  <el-divider></el-divider>
</template>

<script setup>
// eslint-disable-next-line no-unused-vars
import {computed, defineEmits, defineProps, reactive, ref} from "vue";

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  fileEntryInfo: {
    type: Object,
    default: function () {
      return {
        id: 0,
        name: "",
        size: "",
        fileType: 0,
        uploadTime: "",//TODO 可能要改字段名
        url: ""
      };
    }
  }
});


//响应open和download点击按钮相关
const emit = defineEmits(["downloadFileIconClick"]);
//向父组件传当前被点击的url
const downloadIconClick = function () {
  emit("downloadFileIconClick", props.fileEntryInfo.url);
};
const fileTypeMap = {
  1: {
    color: "#2C7DFA",
    text: "应用"
  },
  2: {
    color: "#E6A23C",
    text: "文档"
  },
  3: {
    color: "#67C23A",
    text: "附件"
  },
}
const fileType = computed(() => fileTypeMap[props.fileEntryInfo.fileType])


</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";


.file-entry {
  height: 50px;
  transition: all 0.2s ease-in-out;
  font-size: 12px;
  box-sizing: border-box;
  padding: 0 16px;
  color: $theme-gray-pro;

  &:hover {
    background-color: $bg-orange;
  }

  .name {
    width: 278px;
    text-align: left;
    overflow: hidden;
    /*文本不会换行*/
    white-space: nowrap;
    /*当文本溢出包含元素时，以省略号表示超出的文本*/
    text-overflow: ellipsis;

    .file-type {
      color: v-bind('fileType.color');
      display: inline-block;
      border: solid 2px;
      box-sizing: border-box;
      padding: 2px 4px;
      font-size: 12px;
      border-radius: 32px;
    }
  }

  .upload-time {
    width: 230px;
    text-align: left;
  }

  .size {
    width: 232px;
    text-align: left;
  }

  .operation {
    width: fit-content;
    text-align: right;
  }

  .operation img {
    height: 18px;
    width: 18px;
    cursor: pointer;
  }
}

</style>
