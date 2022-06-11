package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.http.service.vo.test.SimpleTestVO;
import org.promise.test.service.api.info.FinishedTestInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/30 0:18
 */
@Mapper(componentModel = "spring")
public interface SimpleTestConvert {


    @Mapping(target = "averageRate",source = "averageEvaluation")
    @Mapping(target = "evaluationNumber",source = "collaborationNumber")
    SimpleTestVO convertInfo2VO(FinishedTestInfo finishedTestInfo);

    @Mapping(target = "averageEvaluation",source = "averageRate")
    @Mapping(target = "collaborationNumber",source = "evaluationNumber")
    FinishedTestInfo convertVO2Info(SimpleTestVO simpleTestVO);

    List<SimpleTestVO> convertInfoList2VOList(List<FinishedTestInfo> finishedTestInfoList);

    List<FinishedTestInfo> convertVOList2InfoList(List<SimpleTestVO> simpleTestVOList);

}
