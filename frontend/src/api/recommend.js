import request from "@/plugins/axios/request";
import { apiUrlBuilder } from "@/utils/utils";

export const getRecommendTasks = (payload) => {
  return request.get(apiUrlBuilder("recommend/recommendTask", payload)).then((response) => {
    return response.data;
  });
};

/**
 *
 * @param payload testId taskId
 * @return {Promise<AxiosResponse<any>>}
 */
export const getRecommendTests = (payload) => {
  return request.get(apiUrlBuilder("recommend/recommendTest", payload)).then((response) => {
    return response.data;
  });
};
