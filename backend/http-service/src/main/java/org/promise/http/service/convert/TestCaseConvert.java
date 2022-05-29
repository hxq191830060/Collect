package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.promise.http.service.vo.test.TestBugImgVO;
import org.promise.http.service.vo.test.TestCaseVO;
import org.promise.test.service.api.info.TestBugImgInfo;
import org.promise.test.service.api.info.TestCaseInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/28 15:35
 */
@Mapper(componentModel = "spring")
public interface TestCaseConvert {

    TestCaseConvert INSTANCE= Mappers.getMapper(TestCaseConvert.class);

    TestCaseVO convertInfo2VO(TestCaseInfo testCaseInfo);

    TestCaseInfo convertVO2Info(TestCaseVO testCaseVO);

    @Named("TestCaseInfoList2VOList")
    List<TestCaseVO> convertInfoList2VOList(List<TestCaseInfo> testCaseInfoList);

    List<TestCaseInfo> convertVOList2InfoList(List<TestCaseVO> testCaseVOList);
}
