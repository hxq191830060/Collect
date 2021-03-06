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
     * ????????????
     * @param taskVO
     * @return
     */
    @Override
    public HttpResult<String> publish(TaskVO taskVO) {
        TaskInfo taskInfo = taskConvert.convertVO2Info(taskVO);
        //???????????????
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
     * ????????????????????????
     * @param taskVO
     * @return
     */
    @Override
    public HttpResult<String> updateTask(TaskVO taskVO) {
        TaskInfo taskInfo = taskConvert.convertVO2Info(taskVO);
        //???????????????
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
     * ?????????????????????
     * @param taskFileVO
     * @return
     */
    @Override
    public HttpResult<String> publishFile(TaskFileVO taskFileVO) {
        ArrayList<TaskFileInfo> taskFileInfoList = new ArrayList<>();
        taskFileInfoList.add(taskFileConvert.convertVO2Info(taskFileVO));
        //??????????????????
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
     * ??????????????????id???????????????????????????
     * @param publisherId
     * @param judgeType   ????????????????????????0????????????1???????????????2??????????????????3????????????
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getTaskListByPublisherId(Long publisherId, Integer judgeType, Page page) {
        //????????????
        UserIdAndPageRequest userIdAndPageRequest = new UserIdAndPageRequest();
        userIdAndPageRequest.setUserId(publisherId);
        userIdAndPageRequest.setJudgeType(judgeType);
        userIdAndPageRequest.setPage(page);

        RpcResult<TaskListResponse> rpcResult = publishService.getTasksByPublisherId(userIdAndPageRequest);

        if (rpcResult.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", rpcResult.getMessage());
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        page.setTotal(rpcResult.getData().getPage().getTotal());
        List<TaskInfo> taskInfoList = rpcResult.getData().getTaskInfoList();
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(taskInfoList);

        TaskListVO taskListVO = getTaskListVO(page, taskVOList);
        return HttpResultUtil.success(taskListVO);
    }

    /**
     * 1. ??????????????????????????????????????????????????????????????????????????? 2. ????????????????????????page??????????????????????????????
     * @param page
     * @param taskVOList
     * @return
     */
    private TaskListVO getTaskListVO(Page page, List<TaskVO> taskVOList) {
        //?????????????????????????????????
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
     * ????????????????????????????????????????????????
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

        //?????????????????????????????????????????????????????????????????????
        RpcResult<BatchGetUserInfoResponse> usersRpcResult = userService.batchGetUserInfoByUserId(userInfosRequest);
        if (usersRpcResult.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", usersRpcResult.getMessage());
            return Collections.emptyMap();
        }
        return usersRpcResult.getData().getUserInfos();
    }


    /**
     * ????????????id???????????????????????????
     * @param taskId
     * @return
     */
    @Override
    public HttpResult<TaskVO> getTaskById(Long taskId) {
        TaskIdRequest taskIdRequest = new TaskIdRequest();
        taskIdRequest.setTaskId(taskId);
        RpcResult<TaskResponse> rpcResult = publishService.getTaskById(taskIdRequest);
        if (rpcResult.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", rpcResult.getMessage());
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        TaskVO taskVO = taskConvert.convertInfo2VO(rpcResult.getData().getTaskInfo());
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(taskVO.getPublisherId());
        RpcResult<UserInfoResponse> userInfoResponse = userService.getUserInfoByUserId(userIdRequest);
        //??????????????????????????????????????????????????????????????????????????????????????????????????????
        if (userInfoResponse.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", userInfoResponse.getMessage());
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        UserInfo userInfo = userInfoResponse.getData().getUserInfo();
        taskVO.setAvatar(userInfo.getAvatar());
        taskVO.setNickname(userInfo.getNickName());
        return HttpResultUtil.success(taskVO);
    }

    /**
     * ???????????????????????????????????????
     * @param page
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getRunningTasks(Page page) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        RpcResult<TaskListResponse> tasksRunning = publishService.getTasksRunning(pageRequest);
        if (tasksRunning.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", tasksRunning.getMessage());
            return HttpResultUtil.fail(tasksRunning.getMessage());
        }
        page.setTotal(tasksRunning.getData().getPage().getTotal());
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(tasksRunning.getData().getTaskInfoList());

        TaskListVO taskListVO = getTaskListVO(page, taskVOList);
        return HttpResultUtil.success(taskListVO);
    }


    /**
     * ?????????????????????????????????????????????????????????
     * @param page
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getAllTasks(Page page) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(page);
        RpcResult<TaskListResponse> allTasks = publishService.getAllTasks(pageRequest);
        if (allTasks.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", allTasks.getMessage());
            return HttpResultUtil.fail(allTasks.getMessage());
        }
        page.setTotal(allTasks.getData().getPage().getTotal());
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(allTasks.getData().getTaskInfoList());
        TaskListVO taskListVO = getTaskListVO(page, taskVOList);
        return HttpResultUtil.success(taskListVO);
    }

    /**
     * ???????????????id???????????????????????????????????????????????????
     * @param workerId
     * @param judgeType
     * @param page
     * @return
     */
    @Override
    public HttpResult<TaskListVO> getTaskListByWorkerId(Long workerId, Integer judgeType, Page page) {
        //???test???????????????????????????
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

        //?????????????????????????????????
        List<Long> taskIdList = new ArrayList<>();
        for (TestInfo testInfo : testInfoList) {
            taskIdList.add(testInfo.getTaskId());
        }
        TaskIdsRequest taskIdsRequest = new TaskIdsRequest();
        taskIdsRequest.setTaskIds(taskIdList);
        taskIdsRequest.setType(judgeType);

        //?????????????????????task
        RpcResult<TaskMapResponse> taskMapRpcResult = publishService.getTaskMapByTaskIds(taskIdsRequest);
        if (taskMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(taskMapRpcResult.getMessage());
        }
        Map<Long, TaskInfo> taskInfoMap = taskMapRpcResult.getData().getTaskInfoMap();
        page.setTotal(taskInfoMap.size());

        //??????????????????????????????????????????
        List<TaskInfo> taskInfoList = new ArrayList<>(taskInfoMap.values());
        taskInfoList.sort(Comparator.comparing(TaskInfo::getTestStartTime, Comparator.reverseOrder()));
        int pageStart = Math.min((page.getPageNum() - 1) * page.getPageSize(), Math.max(0, page.getTotal() - 1));
        int pageEnd = Math.min(page.getPageNum() * page.getPageSize(), page.getTotal());
        List<TaskVO> taskVOList = taskConvert.convertInfoList2VOList(taskInfoList.subList(pageStart, pageEnd));

        //?????????TaskListVO
        TaskListVO taskListVO=getTaskListVO(page,taskVOList);
        return HttpResultUtil.success(taskListVO);
    }

    /**
     * ???????????????????????????????????????????????????
     * @param taskId ??????id
     * @return ????????????
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
