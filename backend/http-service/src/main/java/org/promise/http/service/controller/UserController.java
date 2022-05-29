package org.promise.http.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.promise.common.result.http.HttpResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.http.service.httpService.UserHttpService;
import org.promise.http.service.util.CommonCheckUtil;
import org.promise.http.service.util.ValidUserThreadLocalUtil;
import org.promise.http.service.vo.user.UserRegisterVO;
import org.promise.http.service.vo.user.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 21:54
 * @description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Resource
    UserHttpService userHttpService;

    @Resource
    private CommonCheckUtil commonCheckUtil;


    @PostMapping("/login")
    public HttpResult<String> login(@RequestBody UserVO userVO) {
        if (userVO == null) {
            return HttpResultUtil.fail("参数为空");
        }
        if (StringUtils.isEmpty(userVO.getEmail())) {
            return HttpResultUtil.fail("email为空");
        }
        if (StringUtils.isEmpty(userVO.getPassword())) {
            return HttpResultUtil.fail("password为空");
        }
        return userHttpService.login(userVO);
    }

    @PostMapping("/register")
    public HttpResult<String> register(@RequestBody UserRegisterVO userRegisterVO) {
        if (userRegisterVO == null) {
            return HttpResultUtil.fail("参数为空");
        }
        if (StringUtils.isEmpty(userRegisterVO.getEmail())) {
            return HttpResultUtil.fail("email为空");
        }
        if (StringUtils.isEmpty(userRegisterVO.getPassword())) {
            return HttpResultUtil.fail("password为空");
        }
        if (StringUtils.isEmpty(userRegisterVO.getNickname())) {
            return HttpResultUtil.fail("昵称为空");
        }
        if(StringUtils.isEmpty(userRegisterVO.getVerificationCode())){
            return HttpResultUtil.fail("验证码为空");
        }
       return userHttpService.register(userRegisterVO);
    }

    @PostMapping("/update")
    public HttpResult<String> update(@RequestBody UserVO userVO) {
        log.info("需要更新的用户信息:{}",userVO);
        if (userVO.getUserId() == null) {
            userVO.setUserId(ValidUserThreadLocalUtil.get().getUserId());
        }
        return userHttpService.update(userVO);
    }

    @GetMapping("/getUser")
    public HttpResult<UserVO> getUser(@RequestParam(required = false) Long userId) {
        if(userId==null){
            userId = ValidUserThreadLocalUtil.get().getUserId();
        }
        return userHttpService.getUser(userId);
    }

    @GetMapping("/getVerificationCode")
    public HttpResult<UserVO> getVerificationCode(@RequestParam String username){
        commonCheckUtil.checkString(username);
        return userHttpService.getVerificationCode(username);
    }
}
