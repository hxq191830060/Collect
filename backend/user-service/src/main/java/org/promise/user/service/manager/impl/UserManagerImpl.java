package org.promise.user.service.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.aspectj.weaver.ast.Var;
import org.promise.common.annotation.RecordLog;
import org.promise.common.constant.ExceptionEnum;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.convert.UserConvert;
import org.promise.user.service.manager.UserManager;
import org.promise.user.service.manager.dao.UserDAO;
import org.promise.user.service.mapperService.UserMapper;
import org.promise.user.service.mapperService.po.UserPO;
import org.promise.user.service.util.EmailUtil;
import org.promise.user.service.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author moqi
 * @date 2022/3/3 23:45
 */
@Slf4j
@Component
public class UserManagerImpl implements UserManager {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserConvert userConvert;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(UserDAO userDAO) {
        //要注册的用户首先不能存在于数据库中
        String currRole = userDAO.getRole().get(0);
        UserDAO userInDataBase = userConvert.convertPO2DAO(userMapper.selectUserByUserName(userDAO.getUserName()));
        //如果在数据库中，就查看当前账号是否已经有相应的身份
        if (userInDataBase != null) {
            //说明当前账号已注册，先判断密码是否相同
            if (!bCryptPasswordEncoder.matches(userDAO.getPassword(), userInDataBase.getPassword())) {
                ExceptionEnum.UserPasswordWrongException.maybeThrow();
            }
            List<String> role = userInDataBase.getRole();
            for (String item : role) {
                if (item.equals(currRole)) {
                    ExceptionEnum.UserHasExistException.maybeThrow();
                }
            }
            List<String> newRole = userInDataBase.getRole();
            newRole.add(currRole);
            userDAO.setRole(newRole);
            userDAO.setUserId(userInDataBase.getUserId());
            updateUserInfoByUserId(userDAO);
        } else {
            UserPO userPO = userConvert.convertDAO2PO(userDAO);
            //密码加密
            userPO.setPassword(bCryptPasswordEncoder.encode(userPO.getPassword()));
            userMapper.insert(userPO);
        }
    }


    @Override
    public UserDAO login(UserDAO userDAO) {
        //如果登录的用户其userName在数据库中不存在对应的记录，那么抛出异常
        UserPO userInDatabase = userMapper.selectUserByUserName(userDAO.getUserName());
        if (userInDatabase == null) {
            ExceptionEnum.UserDoesNotExistException.maybeThrow();
        }

        //传入的密码与数据库中的密码进行比较
        if (bCryptPasswordEncoder.matches(userDAO.getPassword(), userInDatabase.getPassword())) {
            return userConvert.convertPO2DAO(userInDatabase);
        } else {
            ExceptionEnum.IncorrectUserException.maybeThrow();
        }
        return null;
    }

    @Override
    public void updateUserInfoByUserId(UserDAO userDAO) {
        UserPO userPO = userConvert.convertDAO2PO(userDAO);
        if (StringUtils.isNotEmpty(userPO.getPassword())) {
            //如果有传入密码，对密码进行加密
            userPO.setPassword(bCryptPasswordEncoder.encode(userPO.getPassword()));
        }
        userMapper.update(userPO);
    }


    @Override
    @RecordLog
    public List<UserDAO> getUserInfoListByUserIdList(List<Long> userIdList) {

        List<UserPO> userPOList = userMapper.getUserInfoListByUserIdList(userIdList);
        if (CollectionUtils.isEmpty(userPOList)) {
            return Collections.emptyList();
        }

        return userPOList.stream().map(userPO -> {
            return userConvert.convertPO2DAO(userPO);
        }).collect(Collectors.toList());

    }

    @Override
    public UserDAO getUserByUserId(Long userId) {
        UserPO userPO = userMapper.selectUserByUserId(userId);
        return userConvert.convertPO2DAO(userPO);
    }

}
