package org.promise.test.service.http;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.common.annotation.RecordLog;
import org.promise.common.result.http.HttpResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.facotry.TestInfoFactory;
import org.promise.test.service.manager.TestCaseManager;
import org.promise.test.service.manager.TestCollaborationRecordManager;
import org.promise.test.service.manager.TestManager;
import org.promise.test.service.manager.dao.TestCaseDAO;
import org.promise.test.service.manager.dao.TestCollaborationRecordDAO;
import org.promise.test.service.manager.dao.TestDAO;
import org.promise.test.service.mapperService.constant.TestStateEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/testInfo")
public class TestInfoController {

    @Resource
    private TestManager testManager;

    @Resource
    private TestCaseManager testCaseManager;

    @Resource
    private TestCollaborationRecordManager testCollaborationRecordManager;

    @GetMapping("/getTestInfoList")
    @RecordLog
    public HttpResult<TestInfoListVO> getTestInfoListByTaskId(@RequestParam("taskId") Long taskId) {
        if (taskId == null) {
            return HttpResultUtil.fail("参数为空");
        }
        TestInfoListVO testInfoListVO = new TestInfoListVO();
        List<TestDAO> testDAOList = testManager.getTestDAOListByTaskIdAndState(taskId, TestStateEnum.finish);
        if (CollectionUtils.isEmpty(testDAOList)) {
            testInfoListVO.setList(Collections.emptyList());
            return HttpResultUtil.success(testInfoListVO);
        }

        //对于每一个TestDAO，要查询他的评价和测试用例
        List<TestInfo> testInfoList = new ArrayList<>(testDAOList.size());

        for (TestDAO testDAO : testDAOList) {
            List<TestCollaborationRecordDAO> testCollaborationRecordDAOList =
                    testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId(testDAO.getTestId());
            List<TestCaseDAO> testCaseDAOList =
                    testCaseManager.getUsableTestCaseListByTestId(testDAO.getTestId());
            TestInfo testInfo = TestInfoFactory.createTestInfo(testDAO, testCaseDAOList, testCollaborationRecordDAOList);
            testInfoList.add(testInfo);
        }

        testInfoListVO.setList(testInfoList);
        return HttpResultUtil.success(testInfoListVO);
    }


    @GetMapping("/getTestInfoByTestId")
    @RecordLog
    public HttpResult<TestInfoListVO> getTestInfoByTestId( @RequestParam("testId")Long testId){
        if(testId==null){
            return HttpResultUtil.fail ("参数缺省");
        }
        TestInfoListVO testInfoListVO=new TestInfoListVO ();

        TestDAO testDAO=testManager.getTestInfoByTestId (testId);

        if(testDAO==null){
            testInfoListVO.setList (Collections.emptyList ());
            return HttpResultUtil.success ( testInfoListVO );
        }

        List<TestCollaborationRecordDAO> testCollaborationRecordDAOList= testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId (testId );
        List<TestCaseDAO> testCaseDAOList= testCaseManager.getUsableTestCaseListByTestId (testId );
        TestInfo testInfo=TestInfoFactory.createTestInfo (testDAO,testCaseDAOList,testCollaborationRecordDAOList);

        List<TestInfo> testInfoList=new ArrayList<> ( 1 );
        testInfoList.add(testInfo);
        testInfoListVO.setList (testInfoList);

        return HttpResultUtil.success ( testInfoListVO );
    }
}
