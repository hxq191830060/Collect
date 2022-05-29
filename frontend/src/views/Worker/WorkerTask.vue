<template>
  <div class="task">
    <GlobalHeader></GlobalHeader>
    <div class="page">
      <div class="main-content">
        <div class="name-and-join flex-between flex-vc">
          <p class="task-name">{{ taskInfo.name }}</p>
          <el-button v-if="!isParticipateTest" class="add-task-btn" type="warning" @click="handleParticipateTest">加入众测
          </el-button>
          <span v-else class="have-add-task flex-vc">已加入</span>
        </div>
        <div v-if="mainContentType==='tabs'" class="task-tab-box mt-16">
          <el-tabs v-model="activeTaskTab" class="demo-tabs" @tab-click="handleTaskTabClick">
            <el-tab-pane label="项目详情" name="detail">
              <div class="detail mt-16">
                <div class="detail-header ml-16 flex-between flex-vc">
                  <span class="detail-title-txt">项目详情</span>
                  <el-popover
                    v-if="cancellableState.able"
                    v-model:visible="confirmDeleteTestPopoverVisible"
                    :width="150"
                    placement="bottom"
                    trigger="click"
                  >
                    <p class="width-100 text-center">确认退出该项目吗？</p>
                    <div class="mt-16 flex-hc">
                      <el-button
                        plain
                        size="small"
                        type="danger"
                        @click="confirmDeleteTestPopoverVisible=false">取消
                      </el-button>
                      <el-button
                        class="ml-32"
                        size="small"
                        type="primary"
                        @click="confirmDeleteTest">确认
                      </el-button>
                    </div>
                    <template #reference>
                      <el-button v-if="isParticipateTest" plain round style="width: 70px"
                                 type="warning" @click="confirmDeleteTestPopoverVisible=true">
                        <span>退出</span>
                        <el-icon class="el-icon--right">
                          <Promotion></Promotion>
                        </el-icon>
                      </el-button>
                    </template>
                  </el-popover>
                </div>
                <div class="status mt-16 ml-16">
                  <span class="info-key">项目状态：</span>
                  <span id="status-color" class="info-value">{{ taskInfo.status.text }}</span>
                </div>
                <div class="mt-16 ml-16">
                  <span class="info-key">计划截止时间：</span>
                  <span class="info-value">{{ taskInfo.endTime }}</span>
                </div>
                <div class="cancellation-time mt-16 ml-16">
                  <span class="info-key">接包方最晚取消时间：</span>
                  <span
                    class="info-value">{{
                      taskInfo.cancellationTime === "" ? "暂未设定" : taskInfo.cancellationTime
                    }}</span>
                  <span id="cancellable-state-txt">{{ `（${cancellableState.text}）` }}</span>
                </div>
                <div class="mode mt-16 ml-16">
                  <span class="info-key">项目类型：</span>
                  <span class="info-value">{{ taskInfo.mode }}</span>
                </div>
                <div class="test-environments mt-16 ml-16">
                  <span class="info-key">测试环境：</span>
                  <!--TODO 应该使用v-for循环罗列测试环境-->
                  <span v-if="taskInfo.testEnvironments.length===0" class="test-environments-value">暂未设定</span>
                  <p v-for="(environment,index) in taskInfo.testEnvironments" v-else :key="index"
                     class="test-environments-value">{{ environment }}</p>
                </div>
                <div class="require-skills mt-16 ml-16">
                  <span class="info-key">技能要求：</span>
                  <!--TODO 应该使用v-for循环罗列技能要求-->
                  <span v-if="taskInfo.requireSkills.length===0" class="require-skills-value">暂未设定</span>
                  <p v-for="(skill,index) in taskInfo.requireSkills" v-else :key="index"
                     class="require-skills-value">{{ skill }}</p>
                </div>
                <div class="worker-number mt-16 ml-16">
                  <span class="info-key">参与人数：</span>
                  <span class="info-value">已加入：{{ taskInfo.currentWorker }}</span>
                  <span>&emsp;</span>
                  <span class="info-value">总招收：{{ taskInfo.totalWorker }}</span>
                </div>
                <div class="profit mt-16 ml-16">
                  <span class="info-key">预计奖金：</span>
                  <span class="info-value">{{ taskInfo.profit === 0 ? "暂未设定" : `${taskInfo.profit}元` }}</span>
                </div>
                <div class="request-description mt-16 ml-16">
                  <span class="info-key request-description-key">需求描述：</span>
                  <div class="request-description-value mt-8 mr-16">
                    {{ taskInfo.requestDescription === "" ? "暂未填写需求描述" : taskInfo.requestDescription }}
                  </div>
                </div>
                <div v-if="taskInfo.baseFunction.length>0" class="base-function mt-16 ml-16">
                  <span class="info-key base-function-key">基本功能：</span>
                  <div class="base-function-value mt-8 mr-16">
                    {{ taskInfo.baseFunction }}
                  </div>
                </div>
                <div v-if="taskInfo.previewShots.length>0" class="preview-shots mt-16 ml-16">
                  <span class="info-key">图片预览：</span>
                  <div class="mt-8 mr-16">
                    <div v-for="(url,index) in taskInfo.previewShots" :key="index" class="preview-shots-value mb-8"
                         @click="openTaskPreviewShotDialog(url)">
                      <img :src=url alt="img">
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane v-if="isParticipateTest" label="文件专区" name="file">
              <div class="file mt-16">
                <div class="file-header flex-start">
                  <el-select v-model="currentFileTypeOption" placeholder="筛选文件类型">
                    <el-option
                      v-for="item in fileTypeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >

                    </el-option>
                  </el-select>
                </div>
                <div class="file-list mt-32">
                  <div class="entry-type mb-16 flex-vc">
                    <span class="name">文件名</span>
                    <span class="size">文件大小</span>
                    <span class="upload-time">上传时间</span>
                    <span class="operation">操作</span>
                  </div>
                  <div v-if="filterTaskFiles.length===0" class="no-entry-txt">
                    <p class="text-center">暂无当前类型的文件，请等待发布者上传</p>
                  </div>
                  <FileEntry v-for="(entry,index) in filterTaskFiles"
                             :key="index"
                             :file-entry-info="entry"
                             @downloadFileIconClick="handleDownloadFileIconClick" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane v-if="isParticipateTest" label="测试反馈" name="feedback">
              <div class="feedback">
                <p class="feedback-header">在线众测报告</p>
                <p class="feedback-sub-header mt-32">一.基本信息</p>
                <div class="feedback-base-info ml-16">
                  <p class="mt-16">
                    <span class="info-key">测试编号：</span>
                    <span class="info-value">{{ feedbackReportInfo.testId }}</span>
                  </p>
                  <p class="mt-16">
                    <span class="info-key">众测方：</span>
                    <span class="info-value">{{ feedbackReportInfo.nickname }}</span>
                  </p>
                  <p class="mt-16">
                    <span class="info-key">测试开始时间：</span>
                    <span class="info-value">{{ feedbackReportInfo.acceptTime }}</span>
                  </p>
                  <p class="mt-16">
                    <span class="info-key">测试完成/更新时间：</span>
                    <span
                      class="info-value">{{
                        feedbackReportInfo.finishTime === null ? "暂未完成测试" : feedbackReportInfo.finishTime
                      }}</span>
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
                      <el-select
                        v-model="feedbackReportInfo.testEnvironment"
                        placeholder="选择测试环境"
                      >
                        <el-option
                          v-for="(testEnvironment,index) in testEnvironmentOptions"
                          :key="index"
                          :label="testEnvironment"
                          :value="testEnvironment"
                        />
                      </el-select>
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
                               class="mb-16"
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
                            class="width-100 mt-16"
                            maxlength="200"
                            placeholder="您对本产品的建议与期望的改进计划"
                            show-word-limit
                            type="textarea">
                          </el-input>
                        </el-form-item>
                      </el-form>
                    </el-form-item>
                    <el-form-item class="mt-32">
                      <el-button size="large" style="font-weight: 400" type="primary" @click="submitFeedbackReport">
                        提交测试报告
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane v-if="isParticipateTest&&testState==='finish'" label="相似推荐" name="similarity">
              <div class="similarity">
                <div v-if="similarityReportEntryList.length===0" class="no-entry-txt">
                  <p class="text-center">相似报告不足，请等待其他用户测试完毕</p>
                </div>
                <div v-for="(entry,index) in similarityReportEntryList" :key="index" class="feedback">
                  <SimilarityReportEntry
                    :similarity-report-entry-info="entry"
                    @similarityReportEntryClick="handleSimilarityReportEntryClick"
                  ></SimilarityReportEntry>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane v-if="isParticipateTest" label="报告列表" name="cooperation">
              <div class="cooperation">
                <div v-if="feedbackEntryList.length===0" class="no-entry-txt">
                  <p class="text-center">暂无可评价的反馈报告，需等待其他用户测试完毕</p>
                </div>
                <div v-for="(entry,index) in feedbackEntryList" :key="index" class="feedback">
                  <FeedbackEntry :feedback-entry-info="entry"
                                 @feedbackEntryClick="handleFeedbackEntryClick"></FeedbackEntry>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div v-else-if="mainContentType==='feedbackDetail'" class="mt-16 feedback-detail-box">
          <div class="return-page-header flex-vc flex-between">
            <p class="flex-vc">
              <el-icon style="cursor: pointer" @click="mainContentType='tabs'">
                <ArrowLeftBold></ArrowLeftBold>
              </el-icon>
              <span>众测反馈详情</span>
            </p>
            <div>
              <el-tooltip :disabled="!isCurrentWorker" content="您无法对自己的报告进行协作" effect="light" placement="bottom">
                <div class="mr-8" style="display: inline">
                  <el-button :disabled="isCurrentWorker"
                             type="primary"
                             @click="collaborateCurrentReport">
                    报告协作
                    <el-icon class="el-icon--right">
                      <Connection />
                    </el-icon>
                  </el-button>
                </div>
              </el-tooltip>
              <el-tooltip :disabled="!isCurrentWorker" content="您无法对自己的报告进行评价" effect="light" placement="bottom">
                <div style="display: inline">
                  <el-button :disabled="isCurrentWorker"
                             type="warning"
                             @click="feedbackEvaluationDialogVisible=true">
                    评价
                    <el-icon class="el-icon--right">
                      <Edit />
                    </el-icon>
                  </el-button>
                </div>
              </el-tooltip>
            </div>
          </div>
          <div class="feedback-detail mt-32">
            <FeedbackReport :feedback-report-info="otherWorkerReportInfo"></FeedbackReport>
          </div>
        </div>
        <div v-else-if="mainContentType==='reportCollaboration'" class="mt-16 report-collaboration-box">
          <p class="flex-vc return-page-header">
            <el-icon style="cursor: pointer" @click="mainContentType='tabs'">
              <ArrowLeftBold></ArrowLeftBold>
            </el-icon>
            <span>报告协作详情</span>
          </p>
          <div class="report-collaboration mt-32">
            <CollaborationReport
              :report-info="collaborationReportInfo"
              @submitCooperationReport="handleCollaborationReportSubmit"
            ></CollaborationReport>
          </div>
        </div>
      </div>
      <div class="right-content">
        <UserCard :user-info="publisherInfo"></UserCard>
        <div v-if="isParticipateTest" class="feedback-report-collaboration-box mt-16">
          <p class="high-score-header mb-16 ml-16">
            <span class="title-small mr-16">报告协作</span>
            <span>其他用户为您的报告进行协同完善</span>
          </p>
          <div v-if="collaborationReportList.length===0" class="no-high-evaluation-txt">
            <p class="text-center">当前测试暂无协作报告</p>
            <p class="text-center">需等待用户对您的报告进行完善</p>
          </div>
          <CollaborationCard v-for="(report,index) in collaborationReportList"
                             :key="index"
                             :collaboration-info="report"
                             @collaborationCardClick="handleCollaborationCardClick"></CollaborationCard>
        </div>
      </div>
    </div>
    <div class="task-preview-shot-dialog">
      <el-dialog v-model="taskPreviewShotVisible">
        <img :src="taskPreviewShotUrl" alt="img" style="width: 100%" />
      </el-dialog>
    </div>
    <div class="feedback-img-dialog">
      <el-dialog v-model="feedbackDialogVisible">
        <img :src="feedbackDialogImgUrl" alt="img" style="width: 100%" />
      </el-dialog>
    </div>
    <div class="feedback-evaluation-dialog">
      <el-dialog v-model="feedbackEvaluationDialogVisible" title="报告评价" top="20vh" width="550px">
        <div class="self-center" style="width: 80%">
          <el-form
            ref="feedbackEvaluationFormRef"
            :model="feedbackEvaluationInfo"
            :rules="feedbackEvaluationFormRules"
            hide-required-asterisk
            label-position="left"
            label-width="72px">
            <el-form-item label="评价者">
              <span>{{ store.state.userInfo.nickname }}</span>
            </el-form-item>
            <el-form-item label="报告评分" prop="rate">
              <el-rate v-model="feedbackEvaluationInfo.rate"
                       :colors="thresholdColors"
                       :high-threshold="8"
                       :low-threshold="5"
                       :max="10"
                       :text-color="feedbackEvaluationRateTxtColor"
                       :texts="feedbackEvaluationRateTxtArr"
                       show-text
                       size="large" />
            </el-form-item>
            <el-form-item label="报告简评" prop="comment">
              <el-input v-model="feedbackEvaluationInfo.comment"
                        :autosize="{ minRows: 2, maxRows: 4 }"
                        maxlength="50"
                        placeholder="请对本测试报告进行简评"
                        show-word-limit
                        style="margin-top: 4px"
                        type="textarea"></el-input>
            </el-form-item>
          </el-form>
          <div class="width-100 flex-hc mt-32">
            <el-button class="submit-evaluation-btn" round type="primary" @click="submitFeedbackEvaluationInfo">提交评价
            </el-button>
          </div>
        </div>
      </el-dialog>
    </div>

  </div>
</template>

<script setup>
import { Delete } from "@element-plus/icons-vue";
import SimilarityReportEntry from "@/components/SimilarityReportEntry";
import FeedbackReport from "@/components/FeedbackReport";
import FeedbackEntry from "@/components/FeedbackEntry";
import GlobalHeader from "@/components/GlobalHeader";
import CollaborationReport from "@/components/CooperationReport";
import CollaborationCard from "@/components/CollaborationCard";
import UserCard from "@/components/UserCard";
import testToolsOptions from "@/enums/option/selector/testToolsOptions";
import FileEntry from "@/components/FileEntry";
import { getTaskInfoByTaskId, submitCollaborationRecord } from "@/api/task";
import { computed, reactive, ref, toRef, watch } from "vue";
import { getUserInfo } from "@/api/user";
import { defineProps } from "vue";
import {
  buildRandomFileName, debounce,
  deepClone,
  judgmentCurrentTimeInterval,
  ossUrlBuilder,
} from "@/utils/utils";
import { useStore } from "vuex";
import {
  cancelTest,
  getCollaborationListByTestId,
  getCollaborationReportInfo,
  getFeedbackEntry,
  getFeedbackReportInfoByTestId,
  judgeWorkerHasCollaboration,
  judgeWorkerParticipateTestByWorkerIdAndTaskId,
  participateTest, submitCollaborationReport,
  submitTest,
} from "@/api/test";
import { ElNotification } from "element-plus";
import { getOssClient } from "@/api/oss";
import ossDir from "@/enums/config/ossDir";
import Validator from "@/utils/validator";
import { formStrategies } from "@/utils/strategies";
import defectLevelOptions from "@/enums/option/selector/defectLevelOptions";
import moment from "moment";
import { getRecommendTests } from "@/api/recommend";

const props = defineProps({
  taskId: {
    type: String,
    default: "",
  },
  publisherId: {
    type: String,
    default: "",
  },
});
const store = useStore();
const activeTaskTab = ref("detail");
const otherWorkerReportTestId = ref(0);
const testId = ref(null);
const testState = ref("running");
const isParticipateTest = ref(false);
const mainContentType = ref("tabs");

//获取task详细信息（项目详情+文件列表）相关
// eslint-disable-next-line no-unused-vars
const taskInfo = reactive({
  taskId: props.taskId,
  publisherId: 0,
  name: "",
  mode: "",
  status: "",
  startTime: "",
  endTime: "",
  cancellationTime: "",
  totalWorker: 0,
  currentWorker: "",
  profit: 0,
  requestDescription: "",
  baseFunction: "",
  taskFileList: [],
  requireSkills: [],
  testEnvironments: [],
  previewShots: [],
});


const cancellableState = reactive({ able: false, text: "", color: "" });
const fetchTaskInfo = function() {
  getTaskInfoByTaskId({ taskId: props.taskId }).then(response => {
    Object.assign(taskInfo, deepClone(response.data));
    const taskStatusTimeMap = {
      0: { text: "未开始", color: "#67C23A" },
      1: { text: "正在进行", color: "#5995fd" },
      2: { text: "已结束", color: "#F56C6C" },
    };
    taskInfo.status = taskStatusTimeMap[judgmentCurrentTimeInterval(taskInfo.startTime, taskInfo.endTime)];
    setTestEnvironmentOptions();
    setCancellableState();
  });

  function setTestEnvironmentOptions() {
    testEnvironmentOptions.splice(0, testEnvironmentOptions.length);
    taskInfo.testEnvironments.forEach(item => {
      testEnvironmentOptions.push(item);
    });
  }

  function setCancellableState() {
    const cancellableStateMap = {
      0: { able: false, text: "不可退出", color: "#F56C6C" },
      1: { able: true, text: "可退出", color: "#67C23A" },
    };
    if (moment().isBefore(taskInfo.cancellationTime)) Object.assign(cancellableState, cancellableStateMap[1]);
    else Object.assign(cancellableState, cancellableStateMap[0]);
  }
};

const judgeWorkerParticipateTest = function() {
  judgeWorkerParticipateTestByWorkerIdAndTaskId({
    workerId: store.state.userInfo.userId,
    taskId: props.taskId,
  }).then(response => {
    testId.value = response.data.testId;
    testState.value = response.data.state;
    isParticipateTest.value = response.data.hasAccept;
    if (isParticipateTest.value) fetchReportCollaborationList(testId.value);
  });
};

//获取发布者基本信息相关
const publisherInfo = reactive({
  nickname: "",
  projectNum: "",
  introduction: "",
  avatar: "",
  email: "",
});

const fetchPublisherInfo = function() {
  getUserInfo({ userId: props.publisherId }).then(response => {
    Object.assign(publisherInfo, deepClone(response.data));
  });
};


//获取报告协作列表
const collaborationReportList = reactive([]);
const fetchReportCollaborationList = function(testId) {
  collaborationReportList.splice(0, collaborationReportList.length);
  getCollaborationListByTestId({ testId }).then(response => {
    deepClone(response.data).forEach((report) => {
      collaborationReportList.push(report);
    });
  });
};

//页面初始化
const onCreate = function() {
  judgeWorkerParticipateTest();
  fetchTaskInfo();
  fetchPublisherInfo();
};
onCreate();


//加入众测页面相关
const handleParticipateTest = function() {
  participateTest({
    workerId: store.state.userInfo.userId,
    taskId: props.taskId,
  }).then((response) => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        title: "成功",
        position: "top-left",
        duration: 3000,
        message: "加入测试成功",
        offset: 64,
      });
      //加入众测后要重新调用一次，从而获取taskId
      judgeWorkerParticipateTest();
    } else {
      ElNotification({
        type: "error",
        title: "失败",
        position: "top-left",
        duration: 3000,
        message: "当前任务参与人数已满，加入失败",
        offset: 64,
      });
    }
  });

};

//点击taskInfo照片查看照片Dialog相关
//点击应用预览截图查看Dialog相关
const taskPreviewShotUrl = ref("");
const taskPreviewShotVisible = ref(false);
const openTaskPreviewShotDialog = function(url) {
  taskPreviewShotUrl.value = url;
  taskPreviewShotVisible.value = true;
};


const confirmDeleteTestPopoverVisible = ref(false);
const confirmDeleteTest = function() {
  confirmDeleteTestPopoverVisible.value = false;
  //then函数传入一个回调函数，用于取消任务后在判断用户是否参加了该test
  cancelTest({ testId: testId.value }).then((response) => {
    if (response.code === 200) {
      ElNotification({
        type: "success",
        position: "top-left",
        duration: 3000,
        message: "取消任务成功",
        title: "成功",
        offset: 64,
      });
      judgeWorkerParticipateTest();
    } else {
      ElNotification({
        type: "error",
        position: "top-left",
        duration: 3000,
        message: response.message,
        title: "失败",
        offset: 64,
      });
    }
  });
};

//获取task-publisher基本信息相关
const userInfo = reactive({});
getUserInfo().then(res => {
  Object.assign(userInfo, res.userInfo);
});


//文件上传相关 //TODO 这里可能要对value值进行修改
const currentFileTypeOption = ref(0);
const fileTypeOptions = [{
  value: 0,
  label: "全部文件",
}, {
  value: 1,
  label: "应用",
}, {
  value: 2,
  label: "文档",
}, {
  value: 3,
  label: "附件",
}];

//根据select选中的值筛选展示的文件相关
const filterTaskFiles = computed(() => {
  if (currentFileTypeOption.value === 0) return taskInfo.taskFileList;
  const fileList = [];
  taskInfo.taskFileList.forEach(file => {
    if (file.fileType === currentFileTypeOption.value) fileList.push(file);
  });
  return fileList;
});


//点击download-icon，下载(查看)文件相关
const handleDownloadFileIconClick = function(url) {
  window.open(url);
};


//和任务tab标签页相关
const hasRequestRecordMap = {
  feedback: false,
};


const handleTaskTabClick = function() {
  if (!hasRequestRecordMap[activeTaskTab.value]) {
    if (activeTaskTab.value === "feedback") {
      fetchFeedbackReportInfo(testId.value);
    } else if (activeTaskTab.value === "cooperation") {
      fetchFeedbackEntry(props.taskId);
    } else if (activeTaskTab.value === "similarity") {
      fetchSimilarityReportEntry();
    }
    hasRequestRecordMap[activeTaskTab.value] = true;
  }
};

//测试报告提交信息
const feedbackReportInfo = reactive({
  taskId: props.taskId,
  workerId: store.state.userInfo.userId,
  nickname: "",
  state: "",
  testId: 0,
  acceptTime: null,
  updateTime: null,
  finishTime: null,
  testEnvironment: "",
  testCollaborationRecordList: [],
  testTools: [],
  testCases: [],
  defectCases: [],
  screenshots: [],
  conclusion: "",
  suggestion: "",
});


//获取测试报告信息相关
//获得测试报告信息

//用于一开始展示的feedback图片反馈
const shotsListView = reactive([]);
const fetchFeedbackReportInfo = function(testId) {
  getFeedbackReportInfoByTestId({ testId }).then(response => {
    Object.assign(feedbackReportInfo, deepClone(response.data));
    shotsListView.splice(0, shotsListView.length);
    feedbackReportInfo.screenshots.forEach((url) => {
      shotsListView.push({ url });
    });
  });
};

//缺陷统计列表数据相关
/*
* defectCase:{
*   defectLevel, errorMsg, reproduceSteps
* }*/
const defectCasesRef = toRef(feedbackReportInfo, "defectCases");
const defectStatistics = reactive({
  slight: 0,
  common: 0,
  severe: 0,
  fatal: 0,
  total: 0,
});
const defectStatisticsTableData = computed(() => {
  return [deepClone(defectStatistics)];
});
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


//测试详情相关
//测试环境（需要从taskInfo获取）Done
const testEnvironmentOptions = reactive([]);
//测试用例相关
//新增测试用例
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
        console.log(feedbackReportInfo.defectCases);
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


//上传应用缺陷截图相关
// eslint-disable-next-line no-unused-vars
const actionUrl = ref("");
let client = null;
let file = null;
let randomFileName = "";

// eslint-disable-next-line no-unused-vars
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


//提交测试报告相关
const submitFeedbackReport = function() {
  const errorMsg = checkFeedbackReportValid();
  if (errorMsg == null) {
    submitTest(refactorFeedbackReportToBackEndFormat(feedbackReportInfo)).then(response => {
      if (response.code === 200) {
        ElNotification({
          type: "success",
          position: "top-left",
          duration: 3000,
          message: "报告提交成功,其他用户可对您的报告进行协作，相似报告也将推荐给您",
          title: "成功",
          offset: 64,
        });
        judgeWorkerParticipateTest();
        fetchFeedbackReportInfo(testId.value);
        //更新报告列表
        fetchFeedbackEntry(props.taskId);
      } else {
        ElNotification({
          type: "error",
          position: "top-left",
          duration: 3000,
          message: response.message,
          title: "失败",
          offset: 64,
        });
      }
    });
  } else {
    ElNotification({
      type: "warning",
      title: "注意",
      position: "top-left",
      duration: 3000,
      message: errorMsg,
      offset: 64,
    });
  }


  function checkFeedbackReportValid() {
    const validator = new Validator();
    validator.add(feedbackReportInfo.testEnvironment, formStrategies.notEmptyStr, "请选择测试环境");
    validator.add(feedbackReportInfo.testTools, formStrategies.notEmptyArray, "请选择测试工具");
    feedbackReportInfo.testCases.forEach((testCase, index) => {
      validator.add(testCase.caseId, formStrategies.notEmptyStr, `请填写第${index + 1}个用例的序号`);
      validator.add(testCase.runningTime, formStrategies.notEmptyStr, `请填写第${index + 1}个用例的测试时长`);
    });
    feedbackReportInfo.defectCases.forEach((defectCase) => {
      validator.add(defectCase.defectLevel, formStrategies.notEmptyStr, `请选择${defectCase.caseId}号用例的缺陷程度`);
      validator.add(defectCase.errorMsg, formStrategies.notEmptyStr, `请选择${defectCase.caseId}号用例的缺陷说明`);
      validator.add(defectCase.reproduceSteps, formStrategies.notEmptyStr, `请选择${defectCase.caseId}号用例的缺陷复现步骤`);
    });
    validator.add(feedbackReportInfo.screenshots, formStrategies.notEmptyArray, "请上传缺陷截图");
    validator.add(feedbackReportInfo.conclusion, formStrategies.notEmptyStr, "请填写测试结论");
    validator.add(feedbackReportInfo.suggestion, formStrategies.notEmptyStr, "请填写测试建议");
    return validator.startCheck();
  }

  function refactorFeedbackReportToBackEndFormat(feedbackReportInfo) {
    const submitFeedbackReportObj = {};
    submitFeedbackReportObj.testId = feedbackReportInfo.testId;
    submitFeedbackReportObj.testEnvironment = feedbackReportInfo.testEnvironment;
    submitFeedbackReportObj.testTools = deepClone(feedbackReportInfo.testTools);
    submitFeedbackReportObj.screenshots = deepClone(feedbackReportInfo.screenshots);
    submitFeedbackReportObj.conclusion = deepClone(feedbackReportInfo.conclusion);
    submitFeedbackReportObj.suggestion = deepClone(feedbackReportInfo.suggestion);
    submitFeedbackReportObj.testCases = deepClone(feedbackReportInfo.testCases);
    for (let i = 0; i < submitFeedbackReportObj.testCases.length; i++) {
      const testCase = submitFeedbackReportObj.testCases[i];
      for (let j = 0; j < feedbackReportInfo.defectCases.length; j++) {
        const defectCase = feedbackReportInfo.defectCases[j];
        if (testCase.caseId === defectCase.caseId) {
          testCase.defectLevel = defectCase.defectLevel;
          testCase.errorMsg = defectCase.errorMsg;
          testCase.reproduceSteps = defectCase.reproduceSteps;
        }
      }
    }
    return submitFeedbackReportObj;

  }
};

//相似推荐相关，为该用户推荐选择了同样任务人提交的其他报告
const similarityReportEntryList = reactive([]);
const fetchSimilarityReportEntry = function() {
  similarityReportEntryList.splice(0, similarityReportEntryList.length);
  getRecommendTests({ testId: testId.value, taskId: props.taskId }).then(response => {
    if (response.data === null) return;
    deepClone(response.data).forEach(entry => {
      similarityReportEntryList.push(entry);
    });
  });
};


//选择了相同项目的用户可以进行评分和评价
//获取他人的报告entry列表
const feedbackEntryList = reactive([]);
const fetchFeedbackEntry = function(taskId) {
  getFeedbackEntry({ taskId }).then(response => {
    feedbackEntryList.splice(0, feedbackEntryList.length);
    deepClone(response.data).forEach((entry) => {
      feedbackEntryList.push(entry);
    });
  });
};
const handleSimilarityReportEntryClick = function(testId) {
  mainContentType.value = "feedbackDetail";
  otherWorkerReportTestId.value = testId;
  //更换为展示报告的页面
  fetchOtherWorkerReportInfo(testId);
};


const otherWorkerReportInfo = reactive({
  taskId: 0,
  workerId: 0,
  nickname: "",
  state: "",
  testId: 0,
  acceptTime: null,
  updateTime: null,
  finishTime: null,
  testEnvironment: "",
  testCollaborationRecordList: [],
  collaborationList: [],
  testTools: [],
  testCases: [],
  defectCases: [],
  screenshots: [],
  conclusion: "",
  suggestion: "",
});
const fetchOtherWorkerReportInfo = function(testId) {
  getFeedbackReportInfoByTestId({ testId }).then(response => {
    Object.assign(otherWorkerReportInfo, deepClone(response.data));
  });
};
const handleFeedbackEntryClick = function(testId) {
  mainContentType.value = "feedbackDetail";
  otherWorkerReportTestId.value = testId;
  //更换为展示报告的页面
  fetchOtherWorkerReportInfo(testId);
};
//判断当前报告的worker是不是当前用户，如果是的话进行隐藏
// eslint-disable-next-line no-unused-vars
const isCurrentWorker = computed(() => otherWorkerReportInfo.workerId === store.state.userInfo.userId);

const feedbackEvaluationDialogVisible = ref(false);
const feedbackEvaluationInfo = reactive({
  rate: 0,
  comment: "",
});
const thresholdColors = ["#99A9BF", "#67C23A", "#F7BA2A"];
const feedbackEvaluationRateTxtColor = computed(() => {
  if (feedbackEvaluationInfo.rate < 6) return thresholdColors[0];
  else if (feedbackEvaluationInfo.rate < 8) return thresholdColors[1];
  else return thresholdColors[2];
});
const feedbackEvaluationFormRules = reactive({
  rate: [{
    type: "number", required: true,
    validator: (rule, value, callback) => {
      if (value === 0) return callback(new Error("请对报告评分"));
      else return callback();
    }, trigger: "change",
  }],
  comment: [{ required: true, message: "请填写报告简评", trigger: "change" }],
});
const feedbackEvaluationRateTxtArr = [];
for (let i = 0; i < 10; i++) {
  if (i < 5) feedbackEvaluationRateTxtArr.push(`${i + 1}分·一般`);
  else if (i < 7) feedbackEvaluationRateTxtArr.push(`${i + 1}分·较好`);
  else feedbackEvaluationRateTxtArr.push(`${i + 1}分·优秀`);
}

//提交评价相关
const feedbackEvaluationFormRef = ref();

const submitFeedbackEvaluationInfo = function() {
  if (checkEvaluationFormValid()) {
    //这里的testId应该是别人报告的testId
    submitCollaborationRecord({ testId: otherWorkerReportTestId.value, ...deepClone(feedbackEvaluationInfo) }).then(response => {
      if (response.code === 200) {
        ElNotification({
          type: "success",
          position: "top-left",
          duration: 3000,
          message: "您已成功提交或更新测试评价",
          title: "成功",
          offset: 64,
        });
        feedbackEvaluationDialogVisible.value = false;
        //评价完毕之后需要更新测试报告信息,注意，这里的testId也是别人报告的testId
        fetchOtherWorkerReportInfo(otherWorkerReportTestId.value);

      } else {
        ElNotification({
          type: "error",
          position: "top-left",
          duration: 3000,
          message: response.message,
          title: "失败",
          offset: 64,
        });
      }
    });
  }

  function checkEvaluationFormValid() {
    let formValid = false;
    feedbackEvaluationFormRef.value.validate((valid) => {
      formValid = valid;
    });
    return formValid;
  }
};
//进行报告协作相关
const collaborationReportInfo = reactive({
  taskId: 0,
  workerId: 0,
  nickname: "",
  state: "",
  testId: 0,
  createTime: null,
  updateTime: null,
  testEnvironment: "",
  testCollaborationRecordList: [],
  testTools: [],
  testCases: [],
  defectCases: [],
  screenshots: [],
  conclusion: "",
  suggestion: "",
});
//点击报告协作列表中的某一项
const fetchCollaborationReportInfo = function(id) {
  getCollaborationReportInfo({ id }).then(response => {
    Object.assign(collaborationReportInfo, deepClone(response.data));
  });

};
const handleCollaborationCardClick = function(id) {
  mainContentType.value = "reportCollaboration";
  fetchCollaborationReportInfo(id);
};


//点击报告协作按钮
const collaborateCurrentReport = function() {
  mainContentType.value = "reportCollaboration";
  //判断当前用户是否已经协作了该报告，如果没有，则将原始报告值赋给collaborationReportInfo
  //如果已经协作了，则fetchCollaborationReportInfo
  judgeWorkerHasCollaboration({
    testId: otherWorkerReportTestId.value,
    workerId: store.state.userInfo.userId,
  }).then(response => {
    if (response.data === null) {
      //将原始报告值赋给collaborationReportInfo
      Object.assign(collaborationReportInfo, deepClone(otherWorkerReportInfo));
      //但是！有几个字段是不正确/不可替换的，需要改回来
      collaborationReportInfo.workerId = store.state.userInfo.userId;
      collaborationReportInfo.nickname = store.state.userInfo.nickname;
    } else {
      const id = response.data;
      fetchCollaborationReportInfo(id);
    }
  });
};

const handleCollaborationReportSubmit = function(collaborationReportInfo) {
  //每一次均请求一遍最新的id
  judgeWorkerHasCollaboration({
    testId: otherWorkerReportTestId.value,
    workerId: store.state.userInfo.userId,
  }).then(response => {
    collaborationReportInfo.id = response.data;
    submitCollaborationReport(refactorCollaborationReportToBackEndFormat(collaborationReportInfo)).then(response => {
      if (response.code === 200) {
        ElNotification({
          type: "success",
          title: "成功",
          position: "top-left",
          duration: 3000,
          message: "提交协作报告成功",
          offset: 64,
        });
      } else {
        ElNotification({
          type: "error",
          title: "失败",
          position: "top-left",
          duration: 3000,
          message: response.message,
          offset: 64,
        });
      }
    });
  });

  function refactorCollaborationReportToBackEndFormat(collaborationReportInfo) {
    const submitFeedbackReportObj = {};
    submitFeedbackReportObj.workerId = collaborationReportInfo.workerId;
    submitFeedbackReportObj.id = collaborationReportInfo.id;
    submitFeedbackReportObj.testId = collaborationReportInfo.testId;
    submitFeedbackReportObj.testEnvironment = collaborationReportInfo.testEnvironment;
    submitFeedbackReportObj.testTools = deepClone(collaborationReportInfo.testTools);
    submitFeedbackReportObj.screenshots = deepClone(collaborationReportInfo.screenshots);
    submitFeedbackReportObj.conclusion = deepClone(collaborationReportInfo.conclusion);
    submitFeedbackReportObj.suggestion = deepClone(collaborationReportInfo.suggestion);
    submitFeedbackReportObj.testCases = deepClone(collaborationReportInfo.testCases);
    for (let i = 0; i < submitFeedbackReportObj.testCases.length; i++) {
      const testCase = submitFeedbackReportObj.testCases[i];
      for (let j = 0; j < collaborationReportInfo.defectCases.length; j++) {
        const defectCase = collaborationReportInfo.defectCases[j];
        if (testCase.caseId === defectCase.caseId) {
          testCase.defectLevel = defectCase.defectLevel;
          testCase.errorMsg = defectCase.errorMsg;
          testCase.reproduceSteps = defectCase.reproduceSteps;
        }
      }
    }
    return submitFeedbackReportObj;

  }

};
</script>

<style lang="scss" scoped>
@import "~@/views/Worker/style/workerTask.scss";

#status-color {
  color: v-bind("taskInfo.status.color");
  font-weight: 500;
}

#cancellable-state-txt {
  color: v-bind("cancellableState.color");
  font-weight: 500;
}
</style>
