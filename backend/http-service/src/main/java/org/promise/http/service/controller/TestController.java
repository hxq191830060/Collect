package org.promise.http.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.promise.common.result.http.HttpResult;
import org.promise.http.service.httpService.TestHttpService;
import org.promise.http.service.util.CommonCheckUtil;
import org.promise.http.service.util.ValidUserThreadLocalUtil;
import org.promise.http.service.vo.test.*;
import org.promise.test.service.api.response.WhetherWorkerHasAcceptTaskResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/2 23:53
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    CommonCheckUtil commonCheckUtil;

    @Resource
    private TestHttpService testHttpService;

    @PostMapping("/create")
    public HttpResult<String> createTest(@RequestBody TestVO testVO) {
        commonCheckUtil.checkNull(testVO);
        commonCheckUtil.checkLong(testVO.getTaskId());
        if(testVO.getWorkerId()==null){
            testVO.setWorkerId(ValidUserThreadLocalUtil.get().getUserId());
        }
        return testHttpService.createTest(testVO);
    }

    @GetMapping("/workerHasTest")
    public HttpResult<WhetherWorkerHasAcceptTaskResponse> workerHasTest(@RequestParam(required = false) Long workerId,
                                                                        @RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        if(workerId==null){
            workerId=ValidUserThreadLocalUtil.get().getUserId();
        }
        return testHttpService.whetherWorkerHasAcceptTask(workerId,taskId);
    }

    @PostMapping("/cancel")
    public HttpResult<String> cancel(@RequestBody TestVO testVO){
        commonCheckUtil.checkNull(testVO);
        commonCheckUtil.checkLong(testVO.getTestId());
        //保证执行此次操作的是当前用户，若test的所有者不是当前用户则无权执行此操作
        Long workerId=ValidUserThreadLocalUtil.get().getUserId();
        return testHttpService.cancelTest(testVO.getTestId(),workerId);
    }

    @GetMapping("/getTestById")
    public HttpResult<TestVO> getTestById(@RequestParam Long testId){
        commonCheckUtil.checkLong(testId);
        return testHttpService.getTestByTestId(testId);
    }


    @PostMapping("/submitTest")
    public HttpResult<String> submitTest(@RequestBody TestVO testVO){
        commonCheckUtil.checkNull(testVO);
        commonCheckUtil.checkLong(testVO.getTestId());
        //保证执行此次操作的是当前用户，若test的所有者不是当前用户则无权执行此操作
        testVO.setWorkerId(ValidUserThreadLocalUtil.get().getUserId());
        return testHttpService.submitTest(testVO);
    }

    @PostMapping("/submitCollaborationRecord")
    public HttpResult<String> submitCollaborationRecord(
            @RequestBody TestCollaborationRecordVO testCollaborationRecordVO){
        commonCheckUtil.checkNull(testCollaborationRecordVO);
        commonCheckUtil.checkLong(testCollaborationRecordVO.getTestId());
        commonCheckUtil.checkInteger(testCollaborationRecordVO.getRate());
        commonCheckUtil.checkString(testCollaborationRecordVO.getComment());
        //保证执行此次操作的是当前用户
        testCollaborationRecordVO.setWorkerId(ValidUserThreadLocalUtil.get().getUserId());
        return testHttpService.submitCollaborationRecord(testCollaborationRecordVO);
    }

    @GetMapping("/getHighEvaluationTestByTaskId")
    public HttpResult<List<HighEvaluationTestVO>> getHighEvaluationTestByTaskId(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return testHttpService.getTestInfoWithHighEvaluationByTaskId(taskId);
    }

    @GetMapping("/getTestListByTaskId")
    public HttpResult<List<SimpleTestVO>> getTestListByTaskId(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return testHttpService.getSimpleTestByTaskId(taskId);
    }

    @PostMapping("/submitCollaboration")
    public HttpResult<String> submitCollaboration(@RequestBody CollaborationVO collaborationVO){
        commonCheckUtil.checkNull(collaborationVO);
        commonCheckUtil.checkLong(collaborationVO.getTestId());
        commonCheckUtil.checkLong(collaborationVO.getWorkerId());
        return testHttpService.submitCollaboration(collaborationVO);
    }

    @GetMapping("/getCollaborationById")
    public HttpResult<CollaborationVO> getCollaborationById(@RequestParam Long id){
        commonCheckUtil.checkLong(id);
        return testHttpService.getCollaborationById(id);
    }

    @GetMapping("/getCollaborationListByTestId")
    public HttpResult<List<CollaborationVO>> getCollaborationListByTestId(@RequestParam Long testId){
        commonCheckUtil.checkLong(testId);
        return testHttpService.getCollaborationListByTestId(testId);
    }

    @GetMapping("/workerHasCollaboration")
    public HttpResult<Long> workerHasCollaboration(@RequestParam Long testId,
                                                   @RequestParam(required = false) Long workerId){
        commonCheckUtil.checkLong(testId);
        if(workerId==null){
            workerId=ValidUserThreadLocalUtil.get().getUserId();
        }
        return testHttpService.workerHasCollaboration(testId,workerId);
    }
}
