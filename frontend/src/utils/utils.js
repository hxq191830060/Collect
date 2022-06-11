//生成一个在min~max之间的随机数

export const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min);
};
//将YYYY-MM—DD hh:mm:ss的时间格式化为需要的格式
export const dateTimeFormatter = (dateTime, mode) => {
  //mode===0:YYYY-MM-DD hh:mm
  //mode===1:MM-DD hh:mm
  const formatterMap = {
    0: () => dateTime.substring(0, 16),
    1: () => dateTime.substring(5, 16),
  };
  return formatterMap[mode]();
};

//获取文件后缀（文件类型）
export const getFileSuffix = (fileName) => {
  const pos = fileName.lastIndexOf(".");
  let suffix = "";
  if (pos !== -1) {
    suffix = fileName.substring(pos);
  }
  return suffix;
};
//获取长度为len的随机字符串
export const randomString = (len) => {
  //默认使用20位结果字符串长度
  const chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
  const maxPos = chars.length;
  let pwd = "";
  for (let i = 0; i < len; i++) {
    pwd += chars.charAt(Math.floor(Math.random() * maxPos));
  }
  return pwd;
};

//生成随机文件名（文件类型不变）
export const buildRandomFileName = (fileName, len = 20) => {
  return randomString(len) + getFileSuffix(fileName);
};

//将字节为单位的文件代销转换为
/**
 * Format bytes as human-readable text.
 *
 * @param bytes Number of bytes.
 * @param dp Number of decimal places to display.
 * @return Formatted string.
 */
export const getFileSizeString = (bytes, dp = 1) => {
  const thresh = 1024;
  const r = Math.pow(10, dp);
  if (Math.abs(bytes) < thresh) {
    return bytes + "B";
  }
  const units = ["kB", "MB", "GB"];
  let u = -1;
  while (Math.round(Math.abs(bytes) * r) / r >= thresh && u < units.length - 1) {
    bytes = bytes / thresh;
    u++;
  }
  return bytes.toFixed(dp) + units[u];
};

/**
 * 这里有一个问题，对传入的是null的话，返回的是{}
 * @param target target object to be deep cloned
 * @return deep cloned object
 * */
export const deepClone = (target) => {
  //fpr of 任意顺序迭代一个对象的除Symbol以外的可枚举属性，包括继承的可枚举属性
  if (typeof target !== "object" || target == null) return target;
  let cloneTarget = Array.isArray(target) ? [] : {};
  for (const key in target) {
    cloneTarget[key] = deepClone(target[key]);
  }
  return cloneTarget;
};

/**
 * 防抖函数
 * @param callback
 * @param delay
 * @return {(function(): void)|*}
 */
export const debounce = function (callback, delay) {
  let flag = null;
  return function () {
    if (flag !== null) clearTimeout(flag);
    flag = setTimeout(callback, delay);
  };
};

//判断当前时间的所属区间
import moment from "moment";

export const judgmentCurrentTimeInterval = (startTime, endTime) => {
  if (moment().isBetween(startTime, endTime)) return 1;
  else if (moment().isBefore(startTime)) return 0;
  else return 2;
};

//根据目录和文件名获取ossUrl
export const ossUrlBuilder = (directory, fileName) => {
  return `https://se-3-promise.oss-cn-hangzhou.aliyuncs.com/${directory}${fileName}`;
};

//构建api的url
export const apiUrlBuilder = (url, params) => {
  if (params == null) return url;
  const keys = Object.keys(params);
  url = url + "?";
  keys.forEach((key) => {
    url = `${url}${key}=${params[key]}&`;
  });
  return url.substring(0, url.length - 1);
};

//倒计时
export const countDown = async function (delay, callback) {
  for (let i = delay - 1; i >= 0; i--) {
    const remainSeconds = await secondConsume(i);
    callback(remainSeconds);
  }

  function secondConsume(i) {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(i);
      }, 1000);
    });
  }
};
