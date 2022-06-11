//众测广场所有的任务
import request from "@/plugins/axios/request";
import { apiUrlBuilder } from "@/utils/utils";

/**
 * @return square active tasks
 * @param payload pageSize pageNum
 * */
export const getSquareActiveTasks = (payload) => {
  return request.get(apiUrlBuilder("task/getRunningTasks", payload)).then((response) => {
    return response.data;
  });
};
/**
 * @return taskInfo
 * @param payload
 * */
export const getTaskInfoByTaskId = (payload) => {
  const { taskId } = payload;
  return request.get(`task/getTaskById?taskId=${taskId}`).then((response) => {
    return response.data;
  });
};

/**
 * @return different type tasks
 * @param payload
 * */
export const getTasksByPublisherIdAndJudgeType = (payload) => {
  return request.get(apiUrlBuilder("task/getTasksByPublisherId", payload)).then((response) => {
    return response.data;
  });
};

/**
 * @param payload
 * @return updateTask response
 * */

export const updateTask = (payload) => {
  return request.post("/task/update", payload).then((response) => {
    return response.data;
  });
};

/**
 * @param payload {name,publisherId,startTime,endTime,totalWorker,mode,introduction,profit,status}
 * @return publishTask response
 * */
export const publishTask = (payload) => {
  return request.post("task/publish", payload).then((response) => {
    return response.data;
  });
};

/**
 * @param payload {taskId,name,size,url,uploadTime}
 * @return publishTaskFile response
 * */
export const uploadTaskFile = (payload) => {
  return request.post("task/publishFile", payload).then((response) => {
    return response.data;
  });
};

/**
 *
 * @return {Promise<AxiosResponse<any>>}
 */
export const getAllTasksList = () => {
  return request.get("task/getAllTasks").then((response) => {
    return response.data;
  });
};

/**
 *
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const judgePublisherPublishedTaskByTaskId = (payload) => {
  return request.get(apiUrlBuilder("task/getTaskStateByTaskId", payload)).then((response) => {
    return response.data;
  });
};

/**
 *
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const submitCollaborationRecord = (payload) => {
  return request.post("test/submitCollaborationRecord", payload).then((response) => {
    return response.data;
  });
};
