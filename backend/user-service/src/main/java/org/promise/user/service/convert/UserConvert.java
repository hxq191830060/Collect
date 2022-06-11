package org.promise.user.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.manager.dao.UserDAO;
import org.promise.user.service.mapperService.po.UserPO;

/**
 * @author moqi
 * @date 2022/3/4 0:07
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserDAO convertInfo2DAO(UserInfo userInfo);

    UserInfo convertDAO2Info(UserDAO userDAO);

    @Mapping(target = "role",expression = "java(org.promise.common.util.JsonUtil.toJson(userDAO.getRole()))")
    @Mapping(target = "skills",expression = "java(org.promise.common.util.JsonUtil.toJson(userDAO.getSkills()))")
    @Mapping(target = "taskPreference",expression = "java(org.promise.common.util.JsonUtil.toJson(userDAO.getTaskPreference()))")
    @Mapping(target = "testDevs",expression = "java(org.promise.common.util.JsonUtil.toJson(userDAO.getTestDevs()))")
    UserPO convertDAO2PO(UserDAO userDAO);

    @Mapping(target = "role",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(userPO.getRole(),String.class))")
    @Mapping(target = "skills",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(userPO.getSkills(),String.class))")
    @Mapping(target = "taskPreference",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(userPO.getTaskPreference(),String.class))")
    @Mapping(target = "testDevs",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(userPO.getTestDevs(),String.class))")
    UserDAO convertPO2DAO(UserPO userPO);
}
