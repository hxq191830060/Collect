import request from "@/plugins/axios/request";
import { apiUrlBuilder } from "@/utils/utils";

/**
 * @param payload {password: string, email: string}
 * @return response {nickname,email,projectNum,role}
 * */

export const userLogin = (payload) => {
  return request.post("user/login", payload).then((response) => {
    return response.data;
  });
};

/**
 * @param payload {email,nickname,password,role}
 * @return response
 * */
export const userRegister = (payload) => {
  return request.post("user/register", payload).then((resonse) => {
    return resonse.data;
  });
};

/**
 *
 * @param payload
 * @return {Promise<AxiosResponse<any>>}
 */
export const getVerificationCode = (payload) => {
  return request.get(apiUrlBuilder("user/getVerificationCode", payload)).then((response) => {
    return response.data;
  });
};
