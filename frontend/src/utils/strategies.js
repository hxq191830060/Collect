export const formStrategies = {
  notEmptyStr: (target, errMsg) => {
    if (target === "") return errMsg;
  },
  notEmptyArray: (target, errMsg) => {
    if (target == null || target.length === 0) return errMsg;
  },
  minLength: (target, length, errMsg) => {
    if (target.length < length) return errMsg;
  },
  isEmail: (target, errMsg) => {
    if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(target))
      return errMsg;
  },
  isPassword: (target, errMsg) => {
    //至少包含一个大写字母，一个小写字母，一个数字
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(target))
      return errMsg;
  },
  sameAsTarget: (target, repeatTarget, errMsg) => {
    if (target !== repeatTarget) return errMsg;
  },
  //新增一个策略：限制用户的用户名长度
  betweenLength: (target, minLen, maxLen, errMsg) => {
    if (target.length > maxLen || target.length < minLen) return errMsg;
  },
};
