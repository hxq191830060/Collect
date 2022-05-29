//策略模式验证器Context，分配用户的请求到对应策略进行处理
function Validator() {
  this.cache = [];
}

/**
 *
 * @param {any} target
 * @param {Function} strategy
 * @param {String} errMsg
 * @param {Array} params
 */
Validator.prototype.add = function (target, strategy, errMsg, params = null) {
  const args = [];
  args.push(target);
  if (params != null) args.push(...params);
  args.push(errMsg);
  this.cache.push(() => strategy(...args));
};

/**
 *
 * @return {String} errMsg
 */
Validator.prototype.startCheck = function () {
  for (const strategy of this.cache) {
    const errMsg = strategy();
    if (errMsg) return errMsg;
  }
};

export default Validator;
