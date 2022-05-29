<template>
  <div class="report-detail">
    <GlobalHeader />
    <div class="page">
      <div class="return-page-header flex-vc" @click="router.go(-1)">
        <el-icon>
          <ArrowLeftBold></ArrowLeftBold>
        </el-icon>
        <span>报告分析</span>
      </div>
      <FeedbackReport :feedback-report-info="feedbackReportInfo" />
    </div>
  </div>
</template>

<script>
export default {
  name: "ReportDetail",
};
</script>
<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import FeedbackReport from "@/components/FeedbackReport";
import { getFeedbackReportInfoByTestId } from "@/api/test";
import { reactive, defineProps } from "vue";
import { deepClone } from "@/utils/utils";
import { useRouter } from "vue-router";

const router = useRouter();

const props = defineProps({
  testId: {
    type: String,
    default: "",
  },
});
const feedbackReportInfo = reactive({
  testId: 0,
  taskId: 0,
  workerId: 0,
  state: "",
  acceptTime: "",
  updateTime: "",
  finishTime: "",
  cancelTime: null,
  testTools: [],
  testEnvironment: "",
  conclusion: "",
  suggestion: "",
  testCollaborationRecordList: [],
  collaborationList: [],
  testCases: [],
  screenshots: [],
  fatal: 0,
  severe: 0,
  common: 0,
  slight: 0,
  total: 0,
  nickname: "",
});

const fetchFeedbackReportInfo = function(testId) {
  getFeedbackReportInfoByTestId({ testId }).then(response => {
    Object.assign(feedbackReportInfo, deepClone(response.data));
  });
};
const onCreate = function() {
  fetchFeedbackReportInfo(props.testId);
};
onCreate();

</script>

<style lang="scss" scoped>
@import "~@/views/Publisher/style/reportDetail.scss";
</style>
