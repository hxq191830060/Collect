package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.http.service.vo.user.UserRegisterVO;
import org.promise.http.service.vo.user.UserVO;
import org.promise.user.service.api.info.UserInfo;

/**
 * @author moqi
 * @date 2022/3/4 19:11
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    @Mapping(target = "nickName", source = "nickname")
    @Mapping(target = "userName", source = "email")
    UserInfo convertVO2Info(UserVO userVO);

    @Mapping(target = "nickName", source = "nickname")
    @Mapping(target = "userName", source = "email")
    UserInfo convertVO2Info(UserRegisterVO userVO);

    @Mapping(target = "nickname", source = "nickName")
    @Mapping(target = "email", source = "userName")
    UserVO convertInfo2VO(UserInfo userInfo);

}
