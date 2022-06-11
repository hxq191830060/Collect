<template>
  <footer class="global-footer">
    <div class="global-footer-inner">
      <img alt="img" class="user-img mb-32"
           src="https://se-3-promise.oss-cn-hangzhou.aliyuncs.com/footer-img.png">
      <p>每一位Promise众测用户，都可以获得更简洁高效的众包测试体验</p>
    </div>

  </footer>
</template>

<script>
export default {
  name: "GlobalFooter",
};
</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.global-footer {
  height: 240px;
  box-sizing: border-box;
  text-align: center;
  font-size: 16px;
  padding-top: 32px;

  .global-footer-inner {
    min-width: 1200px;
    max-width: 1300px;
    margin: 0 auto;

    .user-img {
      height: 60px;
      object-fit: cover;
    }

  }

}
</style>
