package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.promise.http.service.vo.test.TestCaseVO;
import org.promise.http.service.vo.test.TestCollaborationRecordVO;
import org.promise.test.service.api.info.TestCaseInfo;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/28 15:37
 */
@Mapper(componentModel = "spring")
public interface TestCollaborationRecordConvert {

    TestCollaborationRecordConvert INSTANCE= Mappers.getMapper(TestCollaborationRecordConvert.class);

    @Mapping(target = "rate",source = "evaluation")
    @Mapping(target = "comment",source = "recommendation")
    TestCollaborationRecordVO convertInfo2VO(TestCollaborationRecordInfo TestCollaborationRecordInfo);

    @Mapping(target = "evaluation",source = "rate")
    @Mapping(target = "recommendation",source = "comment")
    TestCollaborationRecordInfo convertVO2Info(TestCollaborationRecordVO TestCollaborationRecordVO);

    @Named("TestCollaborationRecordInfoList2VOList")
    List<TestCollaborationRecordVO> convertInfoList2VOList(List<TestCollaborationRecordInfo> testCollaborationRecordInfos);

    List<TestCollaborationRecordInfo> convertVOList2InfoList(List<TestCollaborationRecordVO> testCollaborationRecordVOList);


}
