package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.http.service.vo.test.CollaborationVO;
import org.promise.test.service.api.info.CollaborationInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 10:49
 */
@Mapper(componentModel = "spring")
public interface CollaborationConvert {

    @Mapping(target = "screenshots",source = "bugImgUrl")
    @Mapping(target = "testCases",source = "collaborationCaseInfoList",qualifiedByName = "CollaborationCaseInfoList2VOList")
    @Mapping(target = "testEnvironment",source = "operatingSystem")
    CollaborationVO convertInfo2VO(CollaborationInfo collaborationInfo);

    @Mapping(target = "bugImgUrl",source = "screenshots")
    @Mapping(target = "collaborationCaseInfoList",source = "testCases",qualifiedByName = "CollaborationCaseVOList2InfoList")
    @Mapping(target = "operatingSystem",source = "testEnvironment")
    CollaborationInfo convertVO2Info(CollaborationVO collaborationVO);

    List<CollaborationVO> convertInfoList2VOList(List<CollaborationInfo> collaborationInfoList);

    List<CollaborationInfo> convertVOList2InfoList(List<CollaborationVO> collaborationVOList);
}
