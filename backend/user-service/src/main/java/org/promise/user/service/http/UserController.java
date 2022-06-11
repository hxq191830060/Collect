package org.promise.user.service.http;

import org.promise.common.annotation.RecordLog;
import org.promise.common.result.http.HttpResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.convert.UserConvert;
import org.promise.user.service.manager.UserManager;
import org.promise.user.service.manager.dao.UserDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author moqi
 * @date 2022/3/31 15:08
 */
@RestController
@RequestMapping("/userInfo")
public class UserController {

    @Resource
    private UserManager userManager;

    @Resource
    private UserConvert userConvert;

    @GetMapping("/getUserByUserId")
    @RecordLog
    public HttpResult<UserInfo> getUserByUserId(@RequestParam Long userId) {
        if (userId == null) {
            return HttpResultUtil.fail("参数为空");
        }
        UserInfo userInfo = userConvert.convertDAO2Info(userManager.getUserByUserId(userId));
        if (userInfo != null) {
            return HttpResultUtil.success(userInfo);
        } else {
            return HttpResultUtil.fail("未查询到相关用户信息");
        }

    }
}
