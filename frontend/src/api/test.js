import request from "@/plugins/axios/request";
import { apiUrlBuilder } from "@/utils/utils";

/**
 * worker 加入测试
 * @param payload {workerId,taskId}
 * @return participateTest response
 * */
export const participateTest = (payload) => {
  return request.post("test/create", payload).then((response) => {
    return response.data;
  });
};

/**
 * @param payload {taskId,workerId,caseDescription,reproduceStep,deviceInfo,screenShots}
 * @return updateTest response
 *
 * */
export const submitTest = (payload) => {
  return request.post("test/submitTest", payload).then((response) => {
    return response.data;
  });
};

/**
 *
 * @param payload taskId
 * @return {Promise<AxiosResponse<any>>}
 */
export const getFeedbackEntry = (payload) => {
  return request.get(apiUrlBuilder("test/getTestListByTaskId", payload)).then((response) => {
    return response.data;
  });
};

/**
 * @param payload {workerId, taskId}
 * @return judgeWorkerParticipateTestByWorkerIdAndTaskId response true
 * */
export const judgeWorkerParticipateTestByWorkerIdAndTaskId = (payload) => {
  return request.get(apiUrlBuilder("test/workerHasTest", payload)).then((response) => {
    return response.data;
  });
};

/**
 * @return different type tasks
 * @param payload
 * */
export const getTestsByWorkerIdAndJudgeType = (payload) => {
  return request.get(apiUrlBuilder("task/getTasksByWorkerId", payload)).then((response) => {
    return response.data;
  });
};
/**
 * 获得高分报告（均分大于6）
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const getHighEvaluationTest = (payload) => {
  return request.get(apiUrlBuilder("test/getHighEvaluationTestByTaskId", payload)).then((response) => {
    return response.data;
  });
};

/**
 *
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const cancelTest = (payload) => {
  return request.post("test/cancel", payload).then((response) => {
    return response.data;
  });
};
/**
 *
 * @param payload testId
 * @return {Promise<AxiosResponse<any>>}
 */
export const getFeedbackReportInfoByTestId = (payload) => {
  return request.get(apiUrlBuilder("test/getTestById", payload)).then((response) => {
    return response.data;
  });
};

/**
 * 查询当前test下的协作列表
 * @param payload testId
 * @return {Promise<AxiosResponse<any>>}
 */
export const getCollaborationListByTestId = (payload) => {
  return request.get(apiUrlBuilder("test/getCollaborationListByTestId", payload)).then((response) => {
    return response.data;
  });
};

/**
 * 提交协作报告
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const submitCollaborationReport = (payload) => {
  return request.post("test/submitCollaboration", payload).then((response) => {
    return response.data;
  });
};

/**
 * 获取协作报告的具体信息
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const getCollaborationReportInfo = (payload) => {
  return request.get(apiUrlBuilder("test/getCollaborationById", payload)).then((response) => {
    return response.data;
  });
};

/**
 *
 * @param payload testId=1 workerId=1
 * @return {Promise<AxiosResponse<any>>}
 */
export const judgeWorkerHasCollaboration = (payload) => {
  return request.get(apiUrlBuilder("test/workerHasCollaboration", payload)).then((response) => {
    return response.data;
  });
};
