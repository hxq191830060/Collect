package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.promise.http.service.vo.test.TestBugImgVO;
import org.promise.http.service.vo.test.TestCaseVO;
import org.promise.http.service.vo.test.TestCollaborationRecordVO;
import org.promise.http.service.vo.test.TestVO;
import org.promise.test.service.api.info.TestBugImgInfo;
import org.promise.test.service.api.info.TestCaseInfo;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;
import org.promise.test.service.api.info.TestInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 0:07
 */
@Mapper(componentModel = "spring")
public interface TestConvert {

    @Mapping(target = "screenshots",source = "bugImgList",qualifiedByName = "TestBugImgInfoList2VOList")
    @Mapping(target = "testCases",source = "testCaseList",qualifiedByName = "TestCaseInfoList2VOList")
    @Mapping(target = "testCollaborationRecordList",source = "testCollaborationRecordList",qualifiedByName = "TestCollaborationRecordInfoList2VOList")
    @Mapping(target = "testEnvironment",source = "operatingSystem")
    TestVO convertInfo2VO(TestInfo testInfo);


    TestInfo convertVO2Info(TestVO testVO);


    List<TestVO> convertInfoList2VOList(List<TestInfo> testInfos);

    @Named("TestBugImgInfoList2VOList")
    default List<String> convertBugImgInfoList2VOList(List<TestBugImgInfo> testBugImgInfoList){
        if(testBugImgInfoList==null){
            return Collections.emptyList();
        }
        List<String> res=new ArrayList<>(testBugImgInfoList.size());
        for (TestBugImgInfo testBugImgInfo : testBugImgInfoList) {
            res.add(testBugImgInfo.getImgUrl());
        }
        return res;
    }

    @Mapping(target = "rate",source = "evaluation")
    @Mapping(target = "comment",source = "recommendation")
    TestCollaborationRecordVO convertInfo2VO(TestCollaborationRecordInfo TestCollaborationRecordInfo);
}
