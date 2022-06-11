package org.promise.user.service.mapperService;


import org.promise.user.service.mapperService.po.UserPO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 23:41
 */
public interface UserMapper {

    Integer insert(UserPO userPO);

    Integer update(UserPO userPO);

    UserPO selectUserByUserName(String userName);

    UserPO selectUserByUserId(Long userId);

    List<UserPO> getUserInfoListByUserIdList(List<Long> userIdList);

}
