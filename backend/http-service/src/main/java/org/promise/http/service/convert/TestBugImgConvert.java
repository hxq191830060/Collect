package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.TestBugImgInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 0:09
 */
@Mapper(componentModel = "spring")
public interface TestBugImgConvert {

    TestBugImgConvert INSTANCE= Mappers.getMapper (TestBugImgConvert.class);

    default String convertInfo2VO(TestBugImgInfo testBugImgInfo){
        return testBugImgInfo.getImgUrl();
    }

    default TestBugImgInfo convertVO2Info(String testBugImgVO){
        TestBugImgInfo testBugImgInfo=new TestBugImgInfo();
        testBugImgInfo.setImgUrl(testBugImgVO);
        return testBugImgInfo;
    }

    List<String> convertInfoList2VOList(List<TestBugImgInfo> testBugImgInfoList);

    List<TestBugImgInfo> convertVOList2InfoList(List<String> testBugImgVOList);
}
