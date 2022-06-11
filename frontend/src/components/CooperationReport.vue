<template>
  <div class="cooperation-report">
    <p class="feedback-header">在线协作报告</p>
    <p class="feedback-sub-header mt-32">一.基本信息</p>
    <div class="feedback-base-info ml-16">
      <p class="mt-16">
        <span class="info-key">测试编号：</span>
        <span class="info-value">{{ feedbackReportInfo.testId }}</span>
      </p>
      <p class="mt-16">
        <span class="info-key">协作方：</span>
        <span class="info-value">{{ feedbackReportInfo.nickname }}</span>
      </p>
      <div class="mt-16">
        <span class="info-key">缺陷统计：</span>
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
    <p class="feedback-sub-header mt-32">二.协作记录</p>
    <div class="feedback-collaboration-record ml-16 mt-16">
      <el-table :data="feedbackReportInfo.testCollaborationRecordList"
                border
                empty-text="暂无用户对您的报告进行评价"
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
    <p class="feedback-sub-header mt-32">三.测试详情</p>
    <div class="feedback-test-detail ml-16 mt-16">
      <el-form ref="environmentAndToolFormRef"
               :model="feedbackReportInfo"
               label-position="left"
               label-width="100px">
        <el-form-item class="info-key" label="1.测试环境">
          {{ feedbackReportInfo.testEnvironment }}
        </el-form-item>
        <el-form-item class="info-key" label="2.测试工具">
          <el-select
            v-model="feedbackReportInfo.testTools"
            :reserve-keyword="false"
            collapse-tags
            collapse-tags-tooltip
            default-first-option
            multiple
            placeholder="选择测试工具(多选)"
          >
            <el-option-group
              v-for="(testToolGroup) in testToolsOptions"
              :key="testToolGroup.label"
              :label="testToolGroup.label"
            >
              <el-option
                v-for="(testTool) in testToolGroup.values"
                :key="testTool.key"
                :label="testTool.key"
                :value="testTool.key"
              >
                <div style="display: flex;justify-content: space-between">
                  <span>{{ testTool.key }}</span>
                  <span style="font-size: 8px;color: #999999">{{ testTool.intro }}</span>
                </div>
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item class="info-key" label="3.测试用例">
          <el-form v-for="(testCaseItem,index) in feedbackReportInfo.testCases"
                   :key="index"
                   :inline="true"
                   :model="testCaseItem"
                   label-position="left"
                   label-width="70px">
            <el-form-item>
              <el-input v-model="testCaseItem.caseId" placeholder="序号" style="width: 189px">
                <template #prepend>用例序号</template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="testCaseItem.runningTime" placeholder="时长/分" style="width: 189px">
                <template #prepend>测试时长</template>
              </el-input>
            </el-form-item>
            <el-form-item label="是否通过">
              <el-switch
                v-model="testCaseItem.status"
                :width="40"
                active-color="#409EFF"
                active-text="是"
                active-value="success"
                inactive-color="#FF4949"
                inactive-text="否"
                inactive-value="failed"
                inline-prompt
              />
            </el-form-item>
            <el-form-item>
              <el-button :icon="Delete" circle style="margin-left: 16px;" type="danger"
                         @click="deleteTestCase(index)" />
            </el-form-item>
          </el-form>
          <el-button round size="small" style="width: 80px;height: 32px" type="warning"
                     @click="addTestCase">
            <span style="margin-right: 2px">添加项</span>
            <el-icon :size="16">
              <CirclePlus></CirclePlus>
            </el-icon>
          </el-button>
        </el-form-item>
        <el-form-item class="info-key" label="4.缺陷分析">
          <span v-if="feedbackReportInfo.defectCases.length===0" class="block-font-style">暂无缺陷用例</span>
          <div v-for="(defectCase,index) in feedbackReportInfo.defectCases" :key="index"
               class="width-100">
            <div>({{ index + 1 }}) 用例序号{{ defectCase.caseId }}：</div>
            <el-form
              class="width-100 mb-16"
              label-position="left"
              label-width="70px"
            >
              <el-form-item class="mt-16" label="缺陷程度">
                <el-select v-model="defectCase.defectLevel" placeholder="缺陷程度"
                           style="width: 120px">
                  <el-option
                    v-for="(defectLevelOption) in defectLevelOptions"
                    :key="defectLevelOption.value"
                    :label="defectLevelOption.label"
                    :value="defectLevelOption.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item class="mt-16" label="缺陷描述">
                <el-input v-model="defectCase.errorMsg"
                          :autosize="{ minRows: 3, maxRows: 6 }"
                          class="mr-16 width-100"
                          maxlength="200"
                          placeholder="对本用例未通过的原因进行阐述说明"
                          show-word-limit
                          type="textarea"
                ></el-input>
              </el-form-item>
              <el-form-item class="mt-16" label="复现步骤">
                <el-input v-model="defectCase.reproduceSteps"
                          :autosize="{ minRows: 3, maxRows: 6 }"
                          class="mr-16 width-100"
                          maxlength="200"
                          placeholder="复现该产品或应用未通过该用例的过程"
                          show-word-limit
                          type="textarea"
                ></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-form-item>
        <el-form-item class="info-key" label="5.缺陷截图">
          <el-upload
            :action="actionUrl"
            :file-list="shotsListView"
            :http-request="handleScreenshotUploading"
            :on-preview="openFeedbackScreenShotDialog"
            :on-remove="handleScreenshotRemove"
            :on-success="handleScreenshotUploadSuccess"
            :show-file-list="true"
            class="upload-demo width-100"
            list-type="picture-card"
            method="put"
          >
            <el-icon>
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item class="info-key" label="6.结论及建议">
          <el-form
            ref="conclusionAndSuggestionFormRef"
            :model="feedbackReportInfo"
            class="width-100"
            label-position="right"

          >
            <el-form-item class="info-key">
              <el-input
                v-model="feedbackReportInfo.conclusion"
                :autosize="{ minRows: 4, maxRows: 6 }"
                class="width-100"
                maxlength="200"
                placeholder="本次测试的分析结果与解决方案"
                show-word-limit
                type="textarea">
              </el-input>
            </el-form-item>
            <el-form-item class="info-key">
              <el-input
                v-model="feedbackReportInfo.suggestion"
                :autosize="{ minRows: 4, maxRows: 6 }"
                class="width-100"
                maxlength="200"
                placeholder="您对本产品的建议与期望的改进计划"
                show-word-limit
                type="textarea">
              </el-input>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item class="mt-16">
          <el-button size="large" style="font-weight: 400" type="primary" @click="submitCooperationReport">
            提交协作报告
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "CooperationReport",
};
</script>
<script setup>
//测试报告提交信息
import { useStore } from "vuex";
import { Delete } from "@element-plus/icons-vue";
import testToolsOptions from "@/enums/option/selector/testToolsOptions";
import defectLevelOptions from "@/enums/option/selector/defectLevelOptions";
import { getOssClient } from "@/api/oss";
import { buildRandomFileName, debounce, deepClone, ossUrlBuilder } from "@/utils/utils";
import ossDir from "@/enums/config/ossDir";
import { ref, reactive, defineEmits, computed, toRef, watch, defineProps } from "vue";
import Validator from "@/utils/validator";
import { formStrategies } from "@/utils/strategies";
// eslint-disable-next-line no-unused-vars
const store = useStore();
// eslint-disable-next-line no-unused-vars
const props = defineProps({
  reportInfo: {
    type: Object,
    default: function() {
      return {
        id: null,
        taskId: 0,
        workerId: 0,
        nickname: "",
        state: "",
        testId: 0,
        testEnvironment: "",
        testCollaborationRecordList: [],
        testTools: [],
        testCases: [],
        defectCases: [],
        screenshots: [],
        conclusion: "",
        suggestion: "",
      };
    },
  },
});
const shotsListView = reactive([]);
const feedbackReportInfo = reactive({
  id: null,
  taskId: 0,
  workerId: 0,
  nickname: "",
  state: "",
  testId: 0,
  testEnvironment: "",
  testCollaborationRecordList: [],
  testTools: [],
  testCases: [],
  defectCases: [],
  screenshots: [],
  conclusion: "",
  suggestion: "",
});

watch(() => props.reportInfo, () => {
  Object.assign(feedbackReportInfo, deepClone(props.reportInfo));
  shotsListView.splice(0, shotsListView.length);
  feedbackReportInfo.screenshots.forEach((url) => {
    shotsListView.push({ url });
  });
}, { deep: true });

const actionUrl = ref("");
let client = null;
let file = null;
let randomFileName = "";

const handleScreenshotUploading = async function(option) {
  client = await getOssClient();
  file = option.file;
  randomFileName = buildRandomFileName(file.name);
  client.put(ossDir.screenshotDir + randomFileName, file);
};

/**
 * 照片上传成功
 */
// eslint-disable-next-line no-unused-vars
const handleScreenshotUploadSuccess = function() {
  const imgUrl = ossUrlBuilder(ossDir.screenshotDir, randomFileName);
  feedbackReportInfo.screenshots.push(imgUrl);
};
/**
 * 移出照片
 * @param file 移出的目标照片
 */
// eslint-disable-next-line no-unused-vars
const handleScreenshotRemove = function(file) {
  for (let i = 0; i < feedbackReportInfo.screenshots.length; i++) {
    if (file.url === feedbackReportInfo.screenshots[i]) {
      feedbackReportInfo.screenshots.splice(i, 1);
      break;
    }
  }
};

//点击照片的放大查看按钮相关
const feedbackDialogImgUrl = ref("");
const feedbackDialogVisible = ref(false);
// eslint-disable-next-line no-unused-vars
const openFeedbackScreenShotDialog = function(file) {
  feedbackDialogImgUrl.value = file.url;
  feedbackDialogVisible.value = true;
};

//缺陷统计相关


const defectCasesRef = toRef(feedbackReportInfo, "defectCases");
const defectStatistics = reactive({
  slight: 0,
  common: 0,
  severe: 0,
  fatal: 0,
  total: 0,
});
//缺陷统计列表数据相关
/*
* defectCase:{
*   defectLevel, errorMsg, reproduceSteps
* }*/
const defectStatisticsTableData = computed(() => {
  return [deepClone(defectStatistics)];
});
//将失败的测试用例加入到defectCases中or将不再符合条件的用例移出
const operateDefectStatisticsWithDebounce = debounce(() => {
  Object.assign(defectStatistics, { slight: 0, common: 0, severe: 0, fatal: 0, total: 0 });
  defectCasesRef.value.forEach(defectCase => {
    const msg = checkDefectCaseValid(defectCase);
    if (msg == null) {
      defectStatistics[defectCase.defectLevel]++;
    }
  });
  const { slight, common, severe, fatal } = defectStatistics;
  defectStatistics.total = slight + common + severe + fatal;

  function checkDefectCaseValid(defectCase) {
    const validator = new Validator();
    const { defectLevel, errorMsg, reproduceSteps } = defectCase;
    validator.add(defectLevel, formStrategies.notEmptyStr, "缺陷等级未填");
    validator.add(errorMsg, formStrategies.notEmptyStr, "缺陷说明未填");
    validator.add(reproduceSteps, formStrategies.notEmptyStr, "复现步骤未填");
    return validator.startCheck();
  }
}, 500);
watch(() => deepClone(defectCasesRef), () => {
  operateDefectStatisticsWithDebounce();
});


//提交删除测试用例相关
const addTestCase = function() {
  feedbackReportInfo.testCases.push({
    caseId: "",
    runningTime: "",
    status: "success",
  });
};
//删除测试用例
const deleteTestCase = function(index) {
  feedbackReportInfo.testCases.splice(index, 1);
};


//缺陷分析相关
//将失败的测试用例加入到defectCases中or将不再符合条件的用例移出
const operateDefectCaseWithDebounce = debounce(() => {
  //先检测有没有删除testCases，如果在defectCases中找不到对应的testCase，则说明被删除了
  for (let i = 0; i < feedbackReportInfo.defectCases.length; i++) {
    let isDelete = true;
    for (const testCase of feedbackReportInfo.testCases) {
      if (testCase.caseId === feedbackReportInfo.defectCases[i].caseId) {
        isDelete = false;
        break;
      }
    }
    if (isDelete) {
      feedbackReportInfo.defectCases.splice(i, 1);
      return;
    }
  }
  for (const testCase of feedbackReportInfo.testCases) {
    const errorMsg = checkTestCaseIsDefectCase(testCase);
    if (errorMsg) {
      //如果有，说明当前的testCase不是DefectCase
      //1.是新定义的testCase，还没有填写完毕
      //2.旧的defectCase不符合条件，需要移出
      for (let i = 0; i < feedbackReportInfo.defectCases.length; i++) {
        if (feedbackReportInfo.defectCases[i].caseId === testCase.caseId) {
          feedbackReportInfo.defectCases.splice(i, 1);
          return;
        }
      }
    } else {
      //符合defectCase条件
      //1.是以前的defectCase，不动
      //2.新加入的defectCase，加入到数组中
      let newCaseFlag = true;
      for (let i = 0; i < feedbackReportInfo.defectCases.length; i++) {
        if (feedbackReportInfo.defectCases[i].caseId === testCase.caseId) {
          newCaseFlag = false;
          break;
        }
      }
      if (newCaseFlag) {
        const { caseId, defectLevel, errorMsg, reproduceSteps } = testCase;
        //可能是一开始传入的testCase，这时候后端的testCase是包含了defectLevel等字段
        //也可能是后来加入的testCase，这些testCase是defectCase
        //使用空值合并运算符解决这一问题
        feedbackReportInfo.defectCases.push({
          caseId: caseId,
          defectLevel: defectLevel ?? "",
          errorMsg: errorMsg ?? "",
          reproduceSteps: reproduceSteps ?? "",
        });
      }
    }
  }

  function checkTestCaseIsDefectCase(testCase) {
    const { caseId, runningTime, status } = testCase;
    const validator = new Validator();
    validator.add(caseId, formStrategies.notEmptyStr, "caseId为空");
    validator.add(runningTime, formStrategies.notEmptyStr, "runningTIme为空");
    validator.add(status, (target, errorMsg) => {
      if (target === "success") return errorMsg;
    }, "status为success状态");
    return validator.startCheck();
  }
}, 500);
watch(() => deepClone(feedbackReportInfo.testCases), () => {
  operateDefectCaseWithDebounce();
}, { deep: true });


//提交测试报告相关
const emit = defineEmits(["submitCooperationReport"]);
const submitCooperationReport = function() {
  //TODO 这里要做表单验证
  emit("submitCooperationReport", deepClone(feedbackReportInfo));
};
</script>

<style lang="scss" scoped>
@import "~@/style/common.scss";

.cooperation-report {
  box-sizing: border-box;
  padding: 0 8px;
  font-size: 14px;
  color: $theme-black-lite;

  .info-key {
    color: $theme-gray-pro;
    font-weight: 500;
  }

  .info-value {
    color: $theme-black;
  }

  .feedback-header {
    font-size: 22px;
    color: $theme-black;
    font-weight: 500;
    text-align: center;
  }

  .feedback-sub-header {
    font-size: 16px;
    color: $theme-black;
    font-weight: 500;
  }


  .block-font-style {
    background-color: $bg-green;
    color: $theme-green;
    padding: 0 12px;
    border-radius: 18px;
    font-weight: 500;
    font-size: 12px;
    transition: all 0.2s ease-in-out;
    cursor: default;

    &:hover {
      box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.12);
    }
  }
}


.cooperation-report::v-deep(.el-form-item--default) {
  margin-bottom: 18px;
}


.cooperation-report::v-deep(.el-upload), .cooperation-report::v-deep(.el-upload-list--picture-card>li) {
  width: 116px !important;
  height: 116px !important;
  margin-right: 20px;
}

.cooperation-report::v-deep(.el-upload-list--picture-card>li:nth-child(5n)) {
  margin-right: 0;
}


.cooperation-report::v-deep(.el-upload--picture-card) {
  position: relative;

  i {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    margin: 0;
  }
}

.el-select-dropdown__item {
  padding-right: 20px;
  padding-left: 20px;
}
</style>
