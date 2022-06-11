package org.promise.publish.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.page.Page;
import org.promise.common.result.rpc.RpcResult;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.*;
import org.promise.publish.service.api.response.*;
import org.promise.publish.service.http.TaskController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/26 23:59
 */
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
@Transactional  //事务管理，默认回滚,如果配置了多数据源记得指定事务管理器
public class PublishServiceTest{

    @Resource
    PublishService publishService;

    @Resource
    TaskController taskController;

    private static final String SUCCESS ="success";

    @Test
    public void publishTest(){
        TaskInfo taskInfo=new TaskInfo();
        taskInfo.setTaskName("实际测试任务名");
        taskInfo.setPublisherId(3L);
        String timeNow=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        taskInfo.setTestStartTime(timeNow);
        taskInfo.setTestEndTime(timeNow);
        taskInfo.setTestWorkerCount(15);
        taskInfo.setTestType("bug测试");
        taskInfo.setProfit(0);
        taskInfo.setAllowCancelTime(timeNow);
        taskInfo.setBasicFunction("基本功能");
        taskInfo.setRequirementDescription("需求描述");
        ArrayList<String> taskImgList=new ArrayList<>();
        taskImgList.add("http://url");
        taskInfo.setTaskImgList(taskImgList);
        PublishRequest publishRequest=new PublishRequest();
        publishRequest.setTaskInfo(taskInfo);
        RpcResult<PublishResponse> result=publishService.publish(publishRequest);
        Assertions.assertEquals(result.getMessage(),SUCCESS);
    }

    @Test
    public void updateTest(){
        TaskInfo taskInfo=new TaskInfo();
        taskInfo.setTaskId(202L);
        taskInfo.setTaskName("更改后的任务名");
        taskInfo.setTestType("尝试更改任务类型");
        //尝试更新图片信息
        ArrayList<String> taskImgList=new ArrayList<>();
        taskImgList.add("http://url");
        taskInfo.setTaskImgList(taskImgList);
        PublishRequest publishRequest=new PublishRequest();
        publishRequest.setTaskInfo(taskInfo);
        RpcResult<PublishResponse> rpcResult = publishService.updateTaskInfo(publishRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    public void publishFileTest(){
        ArrayList<TaskFileInfo> arrayList=new ArrayList<>();
        for(int i=0;i<3;i++){
            TaskFileInfo taskFileInfo =new TaskFileInfo();
            taskFileInfo.setTaskId((i+1L));
            taskFileInfo.setFileName("doc"+i);
            taskFileInfo.setFileSize(i+"MB");
            taskFileInfo.setFileUrl("https://localhost/"+i);
            taskFileInfo.setFileLastUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            taskFileInfo.setFileType(0);
            arrayList.add(taskFileInfo);
        }
        PublishFileRequest publishFileRequest =new PublishFileRequest();
        publishFileRequest.setTaskFileInfos(arrayList);
        RpcResult<PublishFileResponse> rpcResult = publishService.publishFile(publishFileRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }


    @Test
    public void getTasksByPublisherIdTest1(){
        UserIdAndPageRequest userIdAndPageRequest =new UserIdAndPageRequest();
        userIdAndPageRequest.setUserId(243L);
        userIdAndPageRequest.setJudgeType(1);
        userIdAndPageRequest.setPage(new Page(1,8));
        RpcResult<TaskListResponse> rpcResult = publishService.getTasksByPublisherId(userIdAndPageRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    public void getTasksByPublisherIdTest2(){
        UserIdAndPageRequest userIdAndPageRequest =new UserIdAndPageRequest();
        userIdAndPageRequest.setUserId(243L);
        userIdAndPageRequest.setJudgeType(2);
        userIdAndPageRequest.setPage(new Page(1,8));
        RpcResult<TaskListResponse> rpcResult = publishService.getTasksByPublisherId(userIdAndPageRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    public void getTasksByPublisherIdTest3(){
        UserIdAndPageRequest userIdAndPageRequest =new UserIdAndPageRequest();
        userIdAndPageRequest.setUserId(243L);
        userIdAndPageRequest.setJudgeType(3);
        userIdAndPageRequest.setPage(new Page(1,8));
        RpcResult<TaskListResponse> rpcResult = publishService.getTasksByPublisherId(userIdAndPageRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }


    @Test
    public void getTaskByIdTest(){
        TaskIdRequest taskIdRequest=new TaskIdRequest();
        taskIdRequest.setTaskId(202L);
        RpcResult<TaskResponse> rpcResult = publishService.getTaskById(taskIdRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    void getTasksRunningTest(){
        PageRequest pageRequest=new PageRequest();
        Page page=new Page();
        page.setPageNum(1);
        page.setPageSize(8);
        pageRequest.setPage(page);
        RpcResult<TaskListResponse> rpcResult = publishService.getTasksRunning(pageRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    void getAllTasksTest(){
        PageRequest pageRequest=new PageRequest();
        Page page=new Page();
        page.setPageSize(8);
        page.setPageNum(1);
        pageRequest.setPage(page);
        RpcResult<TaskListResponse> rpcResult = publishService.getAllTasks(pageRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    void getTaskMapByTaskIdsTest(){
        List<Long> taskIds=new ArrayList<>();
        taskIds.add(202L);
        taskIds.add(203L);
        taskIds.add(204L);
        TaskIdsRequest taskIdsRequest=new TaskIdsRequest();
        taskIdsRequest.setTaskIds(taskIds);
        taskIdsRequest.setType(2);
        RpcResult<TaskMapResponse> rpcResult = publishService.getTaskMapByTaskIds(taskIdsRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    void getTaskStateByIdTest(){
        TaskIdRequest taskIdRequest=new TaskIdRequest();
        taskIdRequest.setTaskId(202L);
        RpcResult<TaskStateResponse> rpcResult = publishService.getTaskStateByTaskId(taskIdRequest);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);
    }

    @Test
    void getMaxWorkerCountByTaskIdTest(){
        TaskIdRequest taskIdRequest=new TaskIdRequest();
        taskIdRequest.setTaskId(202L);
        RpcResult<TaskMaxWorkerCountResponse> rpcResult = publishService.getMaxWorkerCountByTaskId(taskIdRequest);
        Assertions.assertEquals(rpcResult.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getAllowCancelTimeByTaskIdTest(){
        TaskIdRequest taskIdRequest=new TaskIdRequest();
        taskIdRequest.setTaskId(202L);
        RpcResult<TaskTimeResponse> rpcResult = publishService.getAllowCancelTimeByTaskId(taskIdRequest);
        Assertions.assertEquals(rpcResult.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getTaskEndTimeByTaskIdTest(){
        TaskIdRequest taskIdRequest=new TaskIdRequest();
        taskIdRequest.setTaskId(202L);
        RpcResult<TaskTimeResponse> rpcResult = publishService.getTaskEndTimeByTaskId(taskIdRequest);
        Assertions.assertEquals(rpcResult.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getTaskCountByPublisherIdTest(){
        PublisherIdRequest publisherIdRequest =new PublisherIdRequest();
        publisherIdRequest.setUserId(1L);
        RpcResult<TaskCountResponse> rpcResult = publishService.getTaskCountByPublisherId(publisherIdRequest);
        Assertions.assertEquals(rpcResult.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getPublisherIdByTaskId(){
        TaskIdRequest taskIdRequest=new TaskIdRequest();
        taskIdRequest.setTaskId(202L);
        RpcResult<PublisherIdResponse> rpcResult = publishService.getPublisherIdByTaskId(taskIdRequest);
        Assertions.assertEquals(rpcResult.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getRunningTaskHttpTest(){
        HttpResult<List<TaskInfo>> runningTask = taskController.getRunningTask();
        Assertions.assertEquals(runningTask.getCode(), HttpCodes.SUCCESS.getCode());
    }

}
