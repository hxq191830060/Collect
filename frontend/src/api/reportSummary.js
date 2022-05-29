import request from "@/plugins/axios/request";
import { apiUrlBuilder } from "@/utils/utils";

export const getFinishedReportCount = (payload) => {
  return request.get(apiUrlBuilder("report/getFinishedReportCount", payload)).then((response) => {
    return response.data;
  });
};

export const getWhetherOptimizeReport = (payload) => {
  return request.get(apiUrlBuilder("report/whetherOptimizeReport", payload)).then((response) => {
    return response.data;
  });
};
export const getClusterReport = (payload) => {
  return request.get(apiUrlBuilder("report/clusterReport", payload)).then((response) => {
    return response.data;
  });
};

export const getClassifyReport = (payload) => {
  return request.get(apiUrlBuilder("report/classifyReport", payload)).then((response) => {
    return response.data;
  });
};

export const getSortReport = (payload) => {
  return request.get(apiUrlBuilder("report/sortReport", payload)).then((response) => {
    return response.data;
  });
};

export const getFilterReport = (payload) => {
  return request.get(apiUrlBuilder("report/filterReport", payload)).then((response) => {
    return response.data;
  });
};

export const getIntegrateReport = (payload) => {
  return request.get(apiUrlBuilder("report/integrateReport", payload)).then((response) => {
    return response.data;
  });
};
