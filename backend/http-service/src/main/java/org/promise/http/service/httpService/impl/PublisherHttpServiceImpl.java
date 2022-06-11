package org.promise.http.service.httpService.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.page.Page;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.http.service.convert.TaskConvert;
import org.promise.http.service.convert.TaskFileConvert;
import org.promise.http.service.httpService.PublishHttpService;
import org.promise.http.service.vo.publish.*;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.*;
import org.promise.publish.service.api.response.*;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.api.request.GetWorkerTestInfoRequest;
import org.promise.test.service.api.response.TestInfoResponse;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.request.UserIdRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.promise.user.service.api.response.UserInfoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author moqi
 * @date 2022/3/24 21:06
 */
@Service
@Slf4j
public class PublisherHttpServiceImpl implements PublishHttpService {

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private PublishService publishService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private UserService userService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private TestService testService;

    @Resource
    private TaskConvert taskConvert;

    @Resource
    private TaskFileConvert taskFileConvert;


    /**
     * 发布任务
     * @param taskVO
     * @return
     */
    @Override
    public HttpResult<String> publish(TaskVO taskVO) {
        TaskInfo taskInfo = taskConvert.convertVO2Info(taskVO);
        //构造请求体
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setTaskInfo(taskInfo);

        RpcResult<PublishResponse> rpcResult = publishService.publish(publishRequest);
        if (rpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
    }

    /**
     * 更新任务相关信息
     * @param taskVO
     * @return
     */
    @Override
    public HttpResult<String> updateTask(TaskVO taskVO) {
        TaskInfo taskInfo = taskConvert.convertVO2Info(taskVO);
        //构造请求体
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setTaskInfo(taskInfo);
        RpcResult<PublishResponse> rpcResult = publishService.updateTaskInfo(publishRequest);

        if (rpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
    }

    /**
     * 为任务添加文件
     * @param taskFileVO
     * @return
     */
    @Override
    public HttpResult<String> publishFile(TaskFileVO taskFileVO) {
        ArrayList<TaskFileInfo> taskFileInfoList = new ArrayList<>();
        taskFileInfoList.add(taskFileConvert.convertVO2Info(taskFileVO));
        //构造请求参数
        PublishFileRequest publishFileRequest = new PublishFileRequest();
        publishFileRequest.setTaskFileInfos(taskFileInfoList);

        RpcResult<PublishFileResponse> rpcResult = publishService.publishFile(publishFileRequest);
        if (rpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
    }

    /**
     * 根据发布者的id获取所有的任务信息
     * @param publisherId
     * @param judgeType   当前任务的状态，0为所有，1为未开始，2为正在进行，3为已结束
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getTaskListByPublisherId(Long publisherId, Integer judgeType, Page page) {
        //构造参数
        UserIdAndPageRequest userIdAndPageRequest = new UserIdAndPageRequest();
        userIdAndPageRequest.setUserId(publisherId);
        userIdAndPageRequest.setJudgeType(judgeType);
        userIdAndPageRequest.setPage(page);

        RpcResult<TaskListResponse> rpcResult = publishService.getTasksByPublisherId(userIdAndPageRequest);

        if (rpcResult.getData() == null) {
            log.error("rpc调用出现错误,错误信息：{}", rpcResult.getMessage());
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        page.setTotal(rpcResult.getData().getPage().getTotal());
        List<TaskInfo> taskInfoList = rpcResult.getData().getTaskInfoList();
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(taskInfoList);

        TaskListVO taskListVO = getTaskListVO(page, taskVOList);
        return HttpResultUtil.success(taskListVO);
    }

    /**
     * 1. 使用任务列表查询所有发布者的信息，并进行属性的拼接 2. 将拼接好的列表和page中的元素总量进行合并
     * @param page
     * @param taskVOList
     * @return
     */
    private TaskListVO getTaskListVO(Page page, List<TaskVO> taskVOList) {
        //获取用户信息做信息拼接
        Map<Long, UserInfo> userInfoMap = getUserInfoMapByTaskVOList(taskVOList);
        if (userInfoMap.isEmpty()) {
            return new TaskListVO(taskVOList, page);
        }
        for (TaskVO taskVO : taskVOList) {
            Long userId = taskVO.getPublisherId();
            taskVO.setNickname(userInfoMap.get(userId).getNickName());
            taskVO.setAvatar(userInfoMap.get(userId).getAvatar());
        }
        return new TaskListVO(taskVOList, page);
    }

    /**
     * 获取当前任务列表所有发布人的信息
     * @param taskVOList
     * @return
     */
    private Map<Long, UserInfo> getUserInfoMapByTaskVOList(List<TaskVO> taskVOList) {
        Set<Long> userIds = new HashSet<>();
        for (TaskVO taskVO : taskVOList) {
            userIds.add(taskVO.getPublisherId());
        }
        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        userInfosRequest.setUserIdSet(userIds);

        //获取所有任务的发布者的所有信息，用于返回体拼接
        RpcResult<BatchGetUserInfoResponse> usersRpcResult = userService.batchGetUserInfoByUserId(userInfosRequest);
        if (usersRpcResult.getData() == null) {
            log.error("rpc调用出现错误,错误信息：{}", usersRpcResult.getMessage());
            return Collections.emptyMap();
        }
        return usersRpcResult.getData().getUserInfos();
    }


    /**
     * 根据任务id获取详细的任务信息
     * @param taskId
     * @return
     */
    @Override
    public HttpResult<TaskVO> getTaskById(Long taskId) {
        TaskIdRequest taskIdRequest = new TaskIdRequest();
        taskIdRequest.setTaskId(taskId);
        RpcResult<TaskResponse> rpcResult = publishService.getTaskById(taskIdRequest);
        if (rpcResult.getData() == null) {
            log.error("rpc调用出现错误,错误信息：{}", rpcResult.getMessage());
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        TaskVO taskVO = taskConvert.convertInfo2VO(rpcResult.getData().getTaskInfo());
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(taskVO.getPublisherId());
        RpcResult<UserInfoResponse> userInfoResponse = userService.getUserInfoByUserId(userIdRequest);
        //如果查询不到相关的用户数据，会不做拼接直接返回，但正常情况下不会发生
        if (userInfoResponse.getData() == null) {
            log.error("rpc调用出现错误,错误信息：{}", userInfoResponse.getMessage());
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        UserInfo userInfo = userInfoResponse.getData().getUserInfo();
        taskVO.setAvatar(userInfo.getAvatar());
        taskVO.setNickname(userInfo.getNickName());
        return HttpResultUtil.success(taskVO);
    }

    /**
     * 获取所有正在执行的任务信息
     * @param page
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getRunningTasks(Page page) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        RpcResult<TaskListResponse> tasksRunning = publishService.getTasksRunning(pageRequest);
        if (tasksRunning.getData() == null) {
            log.error("rpc调用出现错误,错误信息：{}", tasksRunning.getMessage());
            return HttpResultUtil.fail(tasksRunning.getMessage());
        }
        page.setTotal(tasksRunning.getData().getPage().getTotal());
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(tasksRunning.getData().getTaskInfoList());

        TaskListVO taskListVO = getTaskListVO(page, taskVOList);
        return HttpResultUtil.success(taskListVO);
    }


    /**
     * 获取所有任务信息，包括已结束和尚未开始
     * @param page
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getAllTasks(Page page) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        RpcResult<TaskListResponse> allTasks = publishService.getAllTasks(pageRequest);
        if (allTasks.getData() == null) {
            log.error("rpc调用出现错误,错误信息：{}", allTasks.getMessage());
            return HttpResultUtil.fail(allTasks.getMessage());
        }
        page.setTotal(allTasks.getData().getPage().getTotal());
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(allTasks.getData().getTaskInfoList());
        TaskListVO taskListVO = getTaskListVO(page, taskVOList);
        return HttpResultUtil.success(taskListVO);
    }

    /**
     * 根据工人的id获取当前工人所接受的所有任务的信息
     * @param workerId
     * @param judgeType
     * @param page
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getTaskListByWorkerId(Long workerId, Integer judgeType, Page page) {
        //从test模块中获取测试信息
        GetWorkerTestInfoRequest workerTestInfoRequest = new GetWorkerTestInfoRequest();
        workerTestInfoRequest.setWorkerId(workerId);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("running");
        arrayList.add("finish");
        workerTestInfoRequest.setStateList(arrayList);
        workerTestInfoRequest.setPage(new Page(1, Integer.MAX_VALUE));
        RpcResult<TestInfoResponse> workerTestInfoRpcResult = testService.getWorkerTestInfo(workerTestInfoRequest);
        if (workerTestInfoRpcResult.getData() == null) {
            return HttpResultUtil.fail(workerTestInfoRpcResult.getMessage());
        }
        List<TestInfo> testInfoList = workerTestInfoRpcResult.getData().getTestInfoList();

        //拼接查询任务的请求信息
        List<Long> taskIdList = new ArrayList<>();
        for (TestInfo testInfo : testInfoList) {
            taskIdList.add(testInfo.getTaskId());
        }
        TaskIdsRequest taskIdsRequest = new TaskIdsRequest();
        taskIdsRequest.setTaskIds(taskIdList);
        taskIdsRequest.setType(judgeType);

        //查询符合条件的task
        RpcResult<TaskMapResponse> taskMapRpcResult = publishService.getTaskMapByTaskIds(taskIdsRequest);
        if (taskMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(taskMapRpcResult.getMessage());
        }
        Map<Long, TaskInfo> taskInfoMap = taskMapRpcResult.getData().getTaskInfoMap();
        page.setTotal(taskInfoMap.size());

        //对获得的任务列表进行分页操作
        List<TaskInfo> taskInfoList = new ArrayList<>(taskInfoMap.values());
        taskInfoList.sort(Comparator.comparing(TaskInfo::getTestStartTime, Comparator.reverseOrder()));
        int pageStart = Math.min((page.getPageNum() - 1) * page.getPageSize(), Math.max(0, page.getTotal() - 1));
        int pageEnd = Math.min(page.getPageNum() * page.getPageSize(), page.getTotal());
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(taskInfoList.subList(pageStart, pageEnd));

        //转化成TaskListVO
        TaskListVO taskListVO=getTaskListVO(page,taskVOList);
        return HttpResultUtil.success(taskListVO);
    }

    /**
     * 查看当前任务是否已经提交，正在运行
     * @param taskId 任务id
     * @return 任务状态
     */
    @Override
    public HttpResult<Boolean> getTaskStateByTaskId(Long taskId) {
        TaskIdRequest taskIdRequest = new TaskIdRequest();
        taskIdRequest.setTaskId(taskId);
        RpcResult<TaskStateResponse> rpcResult = publishService.getTaskStateByTaskId(taskIdRequest);

        if (rpcResult.getData() == null) {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        return HttpResultUtil.success(rpcResult.getData().isSubmitted());
    }

}
