<template>
  <div class="feedback-report">
    <p class="feedback-header">在线众测报告</p>
    <p class="feedback-sub-header mt-32">1. 基本信息</p>
    <div class="feedback-base-info ml-16">
      <p class="mt-16">
        <span class="info-key">1.1 测试编号：</span>
        <span class="info-value">{{ feedbackReportInfo.testId }}</span>
      </p>
      <p class="mt-16">
        <span class="info-key">1.2 众测方：</span>
        <span class="info-value">{{ feedbackReportInfo.nickname }}</span>
      </p>
      <p class="mt-16">
        <span class="info-key">1.3 测试开始时间：</span>
        <span class="info-value">{{ feedbackReportInfo.acceptTime }}</span>
      </p>
      <p class="mt-16">
        <span class="info-key">1.4 测试结束时间：</span>
        <span class="info-value">{{ feedbackReportInfo.finishTime }}</span>
      </p>
      <div class="mt-16">
        <span class="info-key">1.5 缺陷统计：</span>
        <el-table :data="defectStatisticsTableData"
                  border
                  stripe
                  style="width: 100%;margin-top: 8px">
          <el-table-column align="center" label="轻微" prop="slight" />
          <el-table-column align="center" label="一般" prop="common" />
          <el-table-column align="center" label="严重" prop="severe" />
          <el-table-column align="center" label="致命" prop="fatal" />
          <el-table-column align="center" label="总计" prop="total" />
        </el-table>
      </div>

    </div>
    <p class="feedback-sub-header mt-32">2. 评分记录</p>
    <div class="feedback-collaboration-record ml-16 mt-16">
      <el-table :data="collaborationRecordTableData"
                border
                empty-text="暂无用户对该报告进行评价"
                max-height="160"
                stripe
                style="width: 100%;margin-top: 8px">
        <el-table-column align="center" label="用户编号" prop="workerId" width="100" />
        <el-table-column align="center" label="用户名" prop="nickname" width="120" />
        <el-table-column align="center" label="更新时间" prop="updateTime" width="170" />
        <el-table-column align="center" label="报告评分" prop="rate" width="100" />
        <el-table-column align="center" label="报告简评" prop="comment" />
      </el-table>
    </div>
    <p class="feedback-sub-header mt-32">3. 协作记录</p>
    <div class="feedback-collaboration-record ml-16 mt-16">
      <el-table :data="collaborationListTableData"
                border
                empty-text="暂无用户对该报告进行协作"
                max-height="160"
                stripe
                style="width: 100%;margin-top: 8px">
        <el-table-column align="center" label="协作者编号" prop="workerId" width="140" />
        <el-table-column align="center" label="协作人" prop="nickname" width="160" />
        <el-table-column align="center" label="协作时间" prop="updateTime" width="210" />
        <el-table-column align="center" label="查看协作报告" prop="comment">
          <template #default="scope">
            <el-button plain type="primary" @click="handleCollaborationReportBtnClick(scope.row.id)">去查看
              <el-icon class="el-icon--right">
                <Search />
              </el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <p class="feedback-sub-header mt-32">4. 测试详情</p>
    <div class="feedback-test-detail ml-16">
      <div class="test-environments mt-16">
        <span class="info-key">4.1 测试环境：</span>
        <span class="test-environments-value">{{ feedbackReportInfo.testEnvironment }}</span>
      </div>
      <div class="test-tools mt-16">
        <span class="info-key">4.2 测试工具：</span>
        <span v-for="(tool,index) in feedbackReportInfo.testTools" :key="index"
              class="test-tools-value">{{ tool }}</span>
      </div>
      <div class="test-cases mt-16">
        <p class="info-key">4.3 测试用例：</p>
        <el-table :data="testCaseTableData"
                  border
                  max-height="160"
                  stripe
                  style="width: 100%;margin-top: 8px">
          <el-table-column align="center" label="用例序号" prop="caseId" />
          <el-table-column align="center" label="测试时长" prop="runningTime" />
          <el-table-column align="center" label="是否通过" prop="status" />
        </el-table>
      </div>
      <div class="defect-analysis mt-16">
        <p class="info-key">4.4 缺陷分析：</p>
        <div v-for="(defectCase,index) in defectCasesList" :key="index" class="defect-case-info mt-16">
          <p style="font-weight: 500">({{ index + 1 }})用例序号:{{ defectCase.caseId }}</p>
          <p class="mt-16">
            <span>缺陷程度：</span>
            <span class="defect-level-color">{{ defectCase.defectLevel }}</span>
          </p>
          <div class="mt-16 level-description-form">
            <span>缺陷描述：</span>
            <p class="defect-case-description">
              {{ defectCase.errorMsg }}
            </p>
          </div>
          <div class="mt-16 level-description-form">
            <span>复现步骤：</span>
            <p class="defect-case-reproduce-steps">
              {{ defectCase.reproduceSteps }}
            </p>
          </div>
        </div>
      </div>
      <div class="screen-shot mt-16">
        <p class="info-key">4.5 缺陷截图：</p>
        <div class="mt-16 mr-16">
          <div v-for="(imgUrl,index) in feedbackReportInfo.screenshots" :key="index" class="preview-shots-value mb-8"
               @click="openPreviewShotDialog(imgUrl)">
            <img :src="imgUrl" alt="img">
          </div>
        </div>
      </div>
      <div class="test-conclusion mt-16">
        <p class="info-key">4.6 测试总结：</p>
        <p class="test-conclusion-txt-box mt-8">
          {{ feedbackReportInfo.conclusion }}
        </p>
      </div>
      <div class="test-suggestion mt-16">
        <p class="info-key">4.7 测试建议：</p>
        <p class="test-suggestion-txt-box mt-8 text-bg">
          {{ feedbackReportInfo.suggestion }}
        </p>
      </div>
    </div>
    <div class="dialog">
      <div class="preview-shot-dialog">
        <el-dialog v-model="previewShotDialogVisible">
          <img :src="previewShotUrl" alt="img" style="width: 100%" />
        </el-dialog>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "FeedbackReport",
};
</script>
<script setup>
import { defineProps, reactive, ref, watch } from "vue";
import { Search } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

const router = useRouter();
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  feedbackReportInfo: {
    type: Object,
    default: function() {
      return {
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
        testCases: [],
        collaborationList: [],
        screenshots: [],
        fatal: 0,
        severe: 0,
        common: 0,
        slight: 0,
        total: 0,
        nickname: "",
        avatar: "",
      };
    },
  },
});
const defectStatisticsTableData = reactive([]);
const setDefectStatisticsTableData = function() {
  defectStatisticsTableData.splice(0, defectStatisticsTableData.length);
  defectStatisticsTableData.push({
    slight: props.feedbackReportInfo.slight,
    common: props.feedbackReportInfo.common,
    severe: props.feedbackReportInfo.severe,
    fatal: props.feedbackReportInfo.fatal,
    total: props.feedbackReportInfo.total,
  });

};
const collaborationRecordTableData = reactive([]);
const setCollaborationRecordTableData = function() {
  const recordList = props.feedbackReportInfo.testCollaborationRecordList;
  collaborationRecordTableData.splice(0, collaborationRecordTableData.length);
  recordList.forEach(record => {
    collaborationRecordTableData.push({
      workerId: record.workerId,
      nickname: record.nickname,
      updateTime: record.updateTime,
      rate: `${record.rate}分`,
      comment: record.comment,
    });
  });
};

const testCaseTableData = reactive([]);
const setTestCasesTableData = function() {
  const testCases = props.feedbackReportInfo.testCases;
  testCaseTableData.splice(0, testCaseTableData.length);
  testCases.forEach(testCase => {
    testCaseTableData.push({
      caseId: testCase.caseId,
      status: testCase.status === "success" ? "✅" : "❌",
      runningTime: `${testCase.runningTime}分钟`,
    });
  });
};
const defectCasesList = reactive([]);
const setDefectCasesList = function() {
  const defectLevelTxtMap = { slight: "轻微", common: "一般", severe: "严重", fatal: "致命" };
  defectCasesList.splice(0, defectCasesList.length);
  const testCases = props.feedbackReportInfo.testCases;
  testCases.forEach(testCase => {
    if (testCase.status === "failed") {
      defectCasesList.push({
        caseId: testCase.caseId,
        defectLevel: defectLevelTxtMap[testCase.defectLevel],
        errorMsg: testCase.errorMsg,
        reproduceSteps: testCase.reproduceSteps,
      });
    }
  });
};
const collaborationListTableData = reactive([]);
const setCollaborationListTableData = function() {
  collaborationListTableData.splice(0, collaborationListTableData.length);
  const collaborationList = props.feedbackReportInfo.collaborationList;
  collaborationList.forEach((item) => {
    collaborationListTableData.push({
      id: item.id,
      nickname: item.nickname,
      workerId: item.workerId,
      testId: item.testId,
      updateTime: item.updateTime,
    });
  });
};
const onInit = function() {
  setDefectStatisticsTableData();
  setCollaborationRecordTableData();
  setCollaborationListTableData();
  setTestCasesTableData();
  setDefectCasesList();
};
//监听props的变化，而不是直接在mounted or created中执行
watch(() => props, () => {
  onInit();
}, { deep: true });

const onCreat = function() {
  onInit();
};
onCreat();
//展示报告照片相关
const previewShotUrl = ref("");
const previewShotDialogVisible = ref(false);
const openPreviewShotDialog = function(url) {
  previewShotDialogVisible.value = true;
  previewShotUrl.value = url;
};


//查看协作报告详情相关
const handleCollaborationReportBtnClick = function(id) {
  router.push({ name: "collaboration-report", params: { id } });
};

</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.feedback-report {
  box-sizing: border-box;
  padding: 0 16px;
  color: $theme-black-lite;

  .info-key {
    font-size: 16px;
    color: $theme-gray-pro;
    font-weight: 500;
  }

  .info-value {
    font-size: 16px;
    color: $theme-black;
  }


  .feedback-header {
    font-size: 26px;
    color: $theme-black;
    font-weight: 500;
    text-align: center;
  }

  .feedback-sub-header {
    font-size: 20px;
    color: $theme-black;
    font-weight: 500;
  }

  .feedback-test-detail {
    .test-environments-value, .test-tools-value {
      border-radius: 6px;
      margin-right: 10px;
      padding: 4px 6px;
      transition: all 0.2s ease-in-out;

      &:hover {
        box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
        cursor: default;
      }
    }

    .test-environments-value {
      color: $theme-orange;
      background-color: $bg-orange;
    }

    .test-tools-value {
      color: $theme-blue-pro;
      background-color: $bg-blue;
    }

    .preview-shots-value {
      display: inline-block;
      width: 134px;
      height: 134px;
      margin-right: 22px;
      position: relative;
      background-color: $bg-blue-lite;
      border: 1px $theme-blue-pro dashed;
      border-radius: 4px;
      transition: all 0.2s ease-in-out;
      cursor: pointer;

      &:hover {
        box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.2);
      }


      &:nth-child(5n) {
        margin-right: 0;
      }

      img {
        width: 100%;
        object-fit: cover;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
      }
    }

    .defect-case-info {
      box-sizing: border-box;
      padding: 0 16px;

      .level-description-form {
        display: flex;

        span {
          min-width: 80px;
        }

        p {
          word-break: break-word;
          color: $theme-gray-pro;
        }

      }

      .defect-level-color {
        color: $theme-red;
        font-weight: 500;
      }

      .defect-case-reproduce-steps, .defect-case-description {
        width: 100%;
        box-sizing: border-box;
        padding: 12px;
        background-color: #F8F8F8;
        border-radius: 6px;
      }


    }

    .test-conclusion-txt-box, .test-suggestion-txt-box {
      box-sizing: border-box;
      padding: 16px;
      word-break: break-word;
      color: $theme-gray-pro;
      background-color: #F8F8F8;
      border-radius: 6px;

    }

  }
}
</style>
