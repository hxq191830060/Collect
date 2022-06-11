<template>
  <div class="collaboration-report">
    <GlobalHeader />
    <div class="page">
      <div class="return-page-header flex-vc" @click="router.go(-1)">
        <el-icon>
          <ArrowLeftBold></ArrowLeftBold>
        </el-icon>
        <span>众测报告详情</span>
      </div>
      <CollaborationReportDetail :feedback-report-info="collaborationReportInfo" />
    </div>
  </div>
</template>

<script>
export default {
  name: "CollaborationReport",
};
</script>
<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import CollaborationReportDetail from "@/components/CollaborationReportDetail";
import { reactive, defineProps } from "vue";
import { getCollaborationReportInfo } from "@/api/test";
import { deepClone } from "@/utils/utils";
import { useRouter } from "vue-router";

const router = useRouter();

const props = defineProps({
  id: {
    type: String,
    default: "",
  },
});

const collaborationReportInfo = reactive({
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


const fetchCollaborationReportInfo = function(id) {
  getCollaborationReportInfo({ id }).then(response => {
    Object.assign(collaborationReportInfo, deepClone(response.data));
  });
};
const onCreate = function() {
  fetchCollaborationReportInfo(props.id);
};
onCreate();


</script>

<style lang="scss" scoped>
@import "~@/views/Collaboration/style/collaborationReport.scss";
</style>
