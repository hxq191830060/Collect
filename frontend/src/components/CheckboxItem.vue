<template>
  <div class="checkbox">
    <el-checkbox v-model="checked" border label="" size="large" @change="checkboxItemClick">
      <div class="checkbox-inner flex-hc flex-between flex-vc">
        <div class="img-box">
          <img v-if="!checked" :src="checkboxInfo.img[0]" alt="img">
          <img v-else :src="checkboxInfo.img[1]" alt="img">
        </div>
        <span>{{ checkboxInfo.value }}</span>
      </div>
    </el-checkbox>
  </div>
</template>

<script>
export default {
  name: "CheckboxItem"
}
</script>
<script setup>
import {defineProps, defineEmits, ref} from 'vue'

const props = defineProps({
  checkboxType: {
    type: String,
    default: ""
  },
  checkboxFlag: {
    type: Boolean,
    default: false
  },
  checkboxInfo: {
    type: Object,
    default: function () {
      return {
        value: "",
        img: []
      }
    }
  }
})
const checked = ref(props.checkboxFlag)


const emit = defineEmits(["change"]);
const checkboxItemClick = function (flag) {
  emit("change", props.checkboxType, props.checkboxInfo.value, flag);
};
</script>
<style lang="scss" scoped>
@import "~@/style/common.scss";

.checkbox {
  display: inline-block;


  .checkbox-inner {
    font-size: 16px;

    .img-box {
      height: 40px;
      min-height: 40px;

      img {
        object-fit: cover;
        height: 100%;
      }
    }
  }
}

.checkbox::v-deep(.el-checkbox.el-checkbox--large) {
  height: 60px;
  width: 195px;
  border-radius: 48px;
  transition: all 0.2s ease-in-out;

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
    transform: scale(1.06);
  }


  .el-checkbox__label {
    padding-left: 4px;
    display: block;
    width: 100%;
  }

  .el-checkbox__input {
    display: none;
  }
}

.checkbox::v-deep(.el-checkbox.is-bordered.is-checked) {
  border-color: $theme-blue-pro;
}

.checkbox::v-deep(.el-checkbox__input.is-checked+.el-checkbox__label) {
  color: $theme-blue-pro;
}
</style>
