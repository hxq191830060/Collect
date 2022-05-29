import request from "@/plugins/axios/request";
import OSS from "ali-oss";

export const getOssClient = () => {
  return request.get("oss/getToken").then((res) => {
    const ossConfig = res.data;
    return new OSS({
      regionId: ossConfig.regionId,
      accessKeyId: ossConfig.accessKeyId,
      accessKeySecret: ossConfig.accessKeySecret,
      bucket: ossConfig.bucket,
      stsToken: ossConfig.stsToken,
    });
  });
};
