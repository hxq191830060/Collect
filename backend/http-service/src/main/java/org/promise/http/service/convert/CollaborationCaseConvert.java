package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.promise.http.service.vo.test.CollaborationCaseVO;
import org.promise.test.service.api.info.CollaborationCaseInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 10:50
 */
@Mapper(componentModel = "spring")
public interface CollaborationCaseConvert {

    CollaborationCaseConvert INSTANCE= Mappers.getMapper(CollaborationCaseConvert.class);

    CollaborationCaseVO convertInfo2VO(CollaborationCaseInfo collaborationCaseInfo);

    CollaborationCaseInfo convertVO2Info(CollaborationCaseVO collaborationCaseVO);

    @Named("CollaborationCaseInfoList2VOList")
    List<CollaborationCaseVO> convertInfoList2VOList(List<CollaborationCaseInfo> collaborationCaseInfoList);

    @Named("CollaborationCaseVOList2InfoList")
    List<CollaborationCaseInfo> convertVOList2InfoList(List<CollaborationCaseVO> collaborationCaseVOList);
}
