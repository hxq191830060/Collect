package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.http.service.vo.test.HighEvaluationTestVO;
import org.promise.test.service.api.info.FinishedTestInfo;

import java.lang.annotation.Target;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/28 18:59
 */
@Mapper(componentModel = "spring")
public interface HighEvaluationTestConvert {

    @Mapping(target = "averageEvaluation",source = "averageRate")
    @Mapping(target = "collaborationNumber",source = "evaluationNumber")
    FinishedTestInfo convertVO2Info( HighEvaluationTestVO highEvaluationTestVO);

    @Mapping(target = "averageRate",source = "averageEvaluation")
    @Mapping(target = "evaluationNumber",source = "collaborationNumber")
    HighEvaluationTestVO convertInfo2VO( FinishedTestInfo highFinishedTestInfo );

    List<HighEvaluationTestVO> convertInfoList2VOList(List<FinishedTestInfo> highFinishedTestInfoList );
}
