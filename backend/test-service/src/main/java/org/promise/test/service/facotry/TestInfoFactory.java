package org.promise.test.service.facotry;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.test.service.api.info.TestBugImgInfo;
import org.promise.test.service.api.info.TestCaseInfo;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.convert.TestBugImgConvert;
import org.promise.test.service.convert.TestCaseConvert;
import org.promise.test.service.convert.TestCollaborationRecordConvert;
import org.promise.test.service.convert.TestConvert;
import org.promise.test.service.manager.dao.TestBugImgDAO;
import org.promise.test.service.manager.dao.TestCaseDAO;
import org.promise.test.service.manager.dao.TestCollaborationRecordDAO;
import org.promise.test.service.manager.dao.TestDAO;
import org.promise.test.service.mapperService.constant.DefectLevelEnum;
import org.promise.test.service.mapperService.constant.TestCaseStatusEnum;

import java.util.Collections;
import java.util.List;

public class TestInfoFactory {

    public static TestInfo createTestInfo( TestDAO testDAO){
        return createTestInfo (testDAO,null,null,null);
    }

    public static TestInfo createTestInfo( TestDAO testDAO, List<TestCaseDAO> testCaseDAOList){
        return createTestInfo (testDAO,testCaseDAOList,null,null);
    }

    public static TestInfo createTestInfo( TestDAO testDAO, List<TestCaseDAO> testCaseDAOList, List<TestCollaborationRecordDAO> testCollaborationRecordDAOList){
        return createTestInfo (testDAO,testCaseDAOList,testCollaborationRecordDAOList,null);
    }

    public static TestInfo createTestInfo( TestDAO testDAO, List<TestCaseDAO> testCaseDAOList, List<TestCollaborationRecordDAO> testCollaborationRecordDAOList, List<TestBugImgDAO> testBugImgDAOList){
        if(testDAO==null){
            return null;
        }
        TestInfo testInfo= TestConvert.INSTANCE.convertTestDAO2TestInfo (testDAO);
        if(CollectionUtils.isNotEmpty (testCaseDAOList)){
            //对failed的测试用例的错误等级进行统计
            for(TestCaseDAO testCase:testCaseDAOList){
                if(TestCaseStatusEnum.failed.equals (testCase.getStatus ())){
                    if(DefectLevelEnum.fatal.equals (testCase.getDefectLevel ())){
                        testInfo.increaseFatal ();
                    }else if(DefectLevelEnum.common.equals (testCase.getDefectLevel ())){
                        testInfo.increaseCommon ();
                    }else if(DefectLevelEnum.severe.equals (testCase.getDefectLevel ())){
                        testInfo.increaseSevere ();
                    }else if(DefectLevelEnum.slight.equals (testCase.getDefectLevel ())){
                        testInfo.increaseSlight ();
                    }
                }
            }
            List<TestCaseInfo> testCaseInfoList= TestCaseConvert.INSTANCE.convertDAOList2InfoList (testCaseDAOList);
            testInfo.setTestCaseList (testCaseInfoList);
        }else{
            testInfo.setTestCaseList (Collections.emptyList ());
        }
        if(CollectionUtils.isNotEmpty (testCollaborationRecordDAOList)){
            List<TestCollaborationRecordInfo> testCollaborationRecordInfoList= TestCollaborationRecordConvert.INSTANCE.convertDAOList2InfoList (testCollaborationRecordDAOList );
            testInfo.setTestCollaborationRecordList (testCollaborationRecordInfoList);
        }else{
            testInfo.setTestCollaborationRecordList (Collections.emptyList ());
        }
        if(CollectionUtils.isNotEmpty (testBugImgDAOList)){
            List<TestBugImgInfo> testBugImgInfoList= TestBugImgConvert.INSTANCE.convertDAOList2InfoList (testBugImgDAOList );
            testInfo.setBugImgList (testBugImgInfoList);
        }else{
            testInfo.setBugImgList (Collections.emptyList ());
        }
        return testInfo;
    }
}
