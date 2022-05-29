package org.promise.user.service.manager;

import org.promise.user.service.manager.dao.UserDAO;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 22:51
 * @description:
 */
public interface UserManager {

    void register( UserDAO userDAO);

    UserDAO login(UserDAO userDAO);

    void updateUserInfoByUserId(UserDAO userDAO);

    List<UserDAO> getUserInfoListByUserIdList(List<Long> userIdList);

    UserDAO getUserByUserId(Long userId);
}
