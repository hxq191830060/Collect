package org.promise.test.service.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.common.constant.ExceptionEnum;
import org.promise.common.result.page.Page;
import org.promise.test.service.convert.TestBugImgConvert;
import org.promise.test.service.convert.TestCaseConvert;
import org.promise.test.service.convert.TestConvert;
import org.promise.test.service.manager.TestManager;
import org.promise.test.service.manager.dao.TestBugImgDAO;
import org.promise.test.service.manager.dao.TestCaseDAO;
import org.promise.test.service.manager.dao.TestDAO;
import org.promise.test.service.mapperService.TestBugImgMapper;
import org.promise.test.service.mapperService.TestCaseMapper;
import org.promise.test.service.mapperService.TestCountMapper;
import org.promise.test.service.mapperService.TestMapper;
import org.promise.test.service.mapperService.constant.TestStateEnum;
import org.promise.test.service.mapperService.po.TestPO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:53
 * @description:
 */
@Service
@Slf4j
public class TestManagerImpl implements TestManager {

    @Resource
    private TestMapper testMapper;

    @Resource
    private TestCountMapper testCountMapper;

    @Resource
    private TestCaseMapper testCaseMapper;

    @Resource
    private TestBugImgMapper testBugImgMapper;


    @Override
    public TestDAO whetherWorkerHasAcceptTask ( Long taskId, Long workerId ) {
        //获取状态为running的记录数目

        List<TestStateEnum> stateList=new ArrayList<>(2);
        stateList.add (TestStateEnum.running);
        stateList.add (TestStateEnum.finish);
        TestPO testPO=testMapper.getTestInfoByTaskIdAndWorkerAndStateList (taskId,workerId,stateList);

        if(testPO==null){
            return null;
        }
        return TestConvert.INSTANCE.convertTestPO2TestDAO (testPO );

    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNewTestIfIncreaseSuccess ( Long taskId, Long wordId, Integer expect ) {
        Integer optimismRes=testCountMapper.optimismIncreaseTestNum (taskId,expect,1);

        if(optimismRes!=null&&optimismRes!=0){
            //更新成功,执行插入操作
            log.info ("乐观更新成功,在test中插入一条新记录");
            TestPO testPO=new TestPO ();
            testPO.setTaskId (taskId);
            testPO.setWorkerId (wordId);
            testPO.setState (TestStateEnum.running);
            testPO.setAcceptTime (LocalDateTime.now ());
            testMapper.insert (testPO);
        }
    }


    @Override
    public List<TestDAO> getTestDAOByWorkerIdAndTaskIdAndState ( Long taskId, Long workerId, TestStateEnum state ) {

        List<TestPO> testPOList=testMapper.getTestByWorkerIdAndTaskIdAndState (taskId,workerId,state);
        if(CollectionUtils.isEmpty (testPOList)){
            return Collections.emptyList ();
        }
        return TestConvert.INSTANCE.convertTestPOList2TestDAOList (testPOList);
    }


    /**
     * 取消测试任务的前提是test的状态为running
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTest (Long testId) {

        TestPO testPO=testMapper.getTestInfoByTestId (testId);
        if(testPO==null||(!TestStateEnum.running.equals (testPO.getState ()))){
            ExceptionEnum.NullException.maybeThrow ("传入的testId不存在对应的记录or对应的记录的状态不是running");
        }

        //开始修改数据库
        TestPO updateTestPO=new TestPO ();
        updateTestPO.setTestId (testId);
        updateTestPO.setState (TestStateEnum.cancel);
        updateTestPO.setCancelTime (LocalDateTime.now ());
        //更新test表中的记录
        testMapper.updateByTestId (updateTestPO);
        testCountMapper.decreaseNum (1);
    }


    @Override
    public Integer getWorkerAcceptedTaskNumber ( Long workerId ) {
        return testMapper.countTestByWorkerIdAndState (workerId,TestStateEnum.running)+testMapper.countTestByWorkerIdAndState (workerId,TestStateEnum.finish);
    }


    @Override
    public TestDAO getTestInfoByTestId ( Long testId ) {
        TestPO testPO=testMapper.getTestInfoByTestId (testId);
        if(testPO==null){
            return null;
        }
        return TestConvert.INSTANCE.convertTestPO2TestDAO (testPO);
    }

    @Override
    public List<TestDAO> getTestDAOListByWorkerIdAndStateByPage ( Long workerId, TestStateEnum state, Page page ) {
        Integer begin=(page.getPageNum ()-1)*page.getPageSize ();
        Integer pageSize=page.getPageSize ();

        List<TestPO> testPOList=testMapper.getTestListByWorkerIdAndStateByPage (workerId,state,begin,pageSize);
        if(CollectionUtils.isEmpty (testPOList)){
            return Collections.emptyList ( );
        }
        return TestConvert.INSTANCE.convertTestPOList2TestDAOList (testPOList);
    }

    @Override
    public List<TestDAO> getTestDAOListByWorkerIdAndStateListByPage ( Long workerId, List<TestStateEnum> stateList, Page page ) {
        Integer begin=(page.getPageNum ()-1)*page.getPageSize ();
        Integer pageSize=page.getPageSize ();

        List<TestPO> testPOList=testMapper.getTestListByWorkerIdAndStateListByPage (workerId,stateList,begin,pageSize);
        if(CollectionUtils.isEmpty (testPOList)){
            return Collections.emptyList ( );
        }
        return TestConvert.INSTANCE.convertTestPOList2TestDAOList (testPOList);
    }

    @Override
    public Integer countTestInfoByWorkerIdAndState ( Long workerId, TestStateEnum state ) {
        return testMapper.countTestByWorkerIdAndState (workerId,state);
    }


    @Override
    public List<TestDAO> getTestDAOListByTaskIdAndState ( Long taskId, TestStateEnum state ) {
        List<TestPO> testPOList=testMapper.getTestListByTaskIdAndState (taskId,state);
        if(CollectionUtils.isEmpty (testPOList)){
            return Collections.emptyList ();
        }
        return TestConvert.INSTANCE.convertTestPOList2TestDAOList (testPOList);
    }


    @Override
    @Transactional
    public void submitTest ( TestStateEnum originalState, TestDAO testDAO, List<TestBugImgDAO> testBugImgDAOList, List<TestCaseDAO> testCaseDAOList ) {
        TestPO updateTestPO=TestConvert.INSTANCE.convertTestDAO2TestPO (testDAO);

        updateTestPO.setState (TestStateEnum.finish);
        updateTestPO.setFinishTime (LocalDateTime.now ());
        testMapper.updateByTestId (updateTestPO);

        //如果原本是finish状态,那么这边需要将原来提交的bug截图和测试用例删除掉
        if(TestStateEnum.finish.equals (originalState)){
            testBugImgMapper.deleteAllBugImgByTestId (testDAO.getTestId ());
            testCaseMapper.deleteAllTestCaseByTestId (testDAO.getTestId ());
        }

        testBugImgMapper.insertBatch (TestBugImgConvert.INSTANCE.convertTestBugImgDAOList2POList (testBugImgDAOList));
        testCaseMapper.insertBatch (TestCaseConvert.INSTANCE.convertTestCaseDAOList2TestCasePOList (testCaseDAOList));

    }


    @Override
    public Integer countTestInfoByWorkerIdAndStateList ( Long workerId, List<TestStateEnum> stateList ) {
        return testMapper.countTestInfoByWorkerIdAndStateList (workerId,stateList);
    }


    @Override
    public List<TestDAO> getTestDAOListByTestIdList ( List<Long> testIdList ) {
        return TestConvert.INSTANCE.convertTestPOList2TestDAOList (testMapper.getTestListByTestIdList (testIdList));
    }
}
