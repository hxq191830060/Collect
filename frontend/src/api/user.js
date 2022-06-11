import request from "@/plugins/axios/request";
import { apiUrlBuilder } from "@/utils/utils";

/**
 * @param payload
 * @return {userId,email,password,role,nickname,avatar,introduction,projectNum}
 * */
export const getUserInfo = (payload = null) => {
  return request
    .get(apiUrlBuilder("user/getUser", payload))
    .then((response) => {
      return response.data;
    });
};

export const updateUserInfo = (payload) => {
  return request.post("user/update", payload).then((response) => {
    return response.data;
  });
};
