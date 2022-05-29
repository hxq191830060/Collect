package org.promise.publish.service.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.promise.common.result.page.Page;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.LocalDateTimeUtil;
import org.promise.common.util.PageCheckUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.*;
import org.promise.publish.service.api.response.*;
import org.promise.publish.service.convert.TaskFileConvert;
import org.promise.publish.service.convert.TaskConvert;
import org.promise.publish.service.manager.PublishManager;
import org.promise.publish.service.manager.dao.TaskDAO;
import org.promise.publish.service.manager.dao.TaskFileDAO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/26 15:07
 */

@Slf4j
@DubboService(interfaceClass = PublishService.class, group = "${dubbo.registry.group}")
public class PublishServiceImpl implements PublishService {

    @Resource
    private TaskConvert taskConvert;

    @Resource
    private PublishManager publishManager;

    @Resource
    private TaskFileConvert taskFileConvert;

    /**
     * 发布者发布众包测试任务
     * @param publishRequest
     * @return
     */
    @Override
    public RpcResult<PublishResponse> publish(PublishRequest publishRequest) {
        if (publishRequest == null) {
            return RpcResultUtil.fail("发布任务失败，publishRequest为空");
        }
        TaskInfo taskInfo = publishRequest.getTaskInfo();
        if (taskInfo == null) {
            return RpcResultUtil.fail("发布任务失败，taskInfo为空");
        }
        TaskDAO taskDAO = taskConvert.convertInfo2DAO(taskInfo);
        if (publishManager.checkTaskExist(taskDAO)) {
            return RpcResultUtil.fail("发布任务失败，该用户已存在同名任务");
        }
        if (publishManager.publishTask(taskDAO) > 0) {
            return RpcResultUtil.success();
        }
        return RpcResultUtil.fail("任务发布失败");
    }

    @Override
    public RpcResult<PublishFileResponse> publishFile(PublishFileRequest publishFileRequest) {
        if (publishFileRequest == null) {
            return RpcResultUtil.fail("文件发布失败，参数为空");
        }
        List<TaskFileInfo> taskFileInfos = publishFileRequest.getTaskFileInfos();

        if (CollectionUtils.isEmpty(taskFileInfos)) {
            return RpcResultUtil.fail("文件发布失败，当前文件信息为空");
        }
        ArrayList<TaskFileDAO> taskFileDAOS = new ArrayList<>();
        for (TaskFileInfo taskFileInfo : taskFileInfos) {
            taskFileDAOS.add(taskFileConvert.convertInfo2DAO(taskFileInfo));
        }
        publishManager.publishFile(taskFileDAOS);
        return RpcResultUtil.success ();
    }

    /**
     * 发布者更新测试任务相关信息
     * @param publishRequest
     * @return
     */
    @Override
    public RpcResult<PublishResponse> updateTaskInfo(PublishRequest publishRequest) {
        if (publishRequest == null) {
            return RpcResultUtil.fail("request参数为空");
        }
        TaskInfo taskInfo = publishRequest.getTaskInfo();
        if(taskInfo==null){
            return RpcResultUtil.fail ("传入的信息为空");
        }
        publishManager.updateTask (taskConvert.convertInfo2DAO (taskInfo));
        return RpcResultUtil.success ();
    }


    /**
     * 根据发布者的id获取发布者发布的所有任务
     * @param userIdAndPageRequest
     * @return
     */
    @Override
    public RpcResult<TaskListResponse> getTasksByPublisherId(UserIdAndPageRequest userIdAndPageRequest) {
        if (userIdAndPageRequest == null) {
            return RpcResultUtil.fail("查询任务失败，参数为空");
        }
        if(userIdAndPageRequest.getUserId ()==null){
            return RpcResultUtil.fail ("用户ID为空");
        }
        if(userIdAndPageRequest.getJudgeType ()==null){
            return RpcResultUtil.fail ("请选择正确的查询类型");
        }
        if (!PageCheckUtil.pageParamIsLegal(userIdAndPageRequest.getPage())) {
            return RpcResultUtil.fail("分页参数不合法");
        }

        Long publisherId = userIdAndPageRequest.getUserId();
        List<TaskDAO> taskDAOS = publishManager.getTasksByPublisherId(publisherId,
                userIdAndPageRequest.getJudgeType(), userIdAndPageRequest.getPage());
        ArrayList<TaskInfo> taskInfos = new ArrayList<>();
        //注意这里的逻辑，taskDAOS查出来是可能为空列表的
        for (TaskDAO taskDAO : taskDAOS) {
            taskInfos.add(taskConvert.convertDAO2Info(taskDAO));
        }
        taskInfos.sort(Comparator.comparing(TaskInfo::getTestStartTime, Comparator.reverseOrder()));
        TaskListResponse taskListResponse = new TaskListResponse(taskInfos);
        int count=publishManager.countTaskNum(publisherId, userIdAndPageRequest.getJudgeType());
        taskListResponse.setPage(new Page(count));
        return RpcResultUtil.success(taskListResponse);
    }

    /**
     * 根据任务Id获得任务的详细信息
     * @param taskIdRequest
     * @return
     */
    @Override
    public RpcResult<TaskResponse> getTaskById(TaskIdRequest taskIdRequest) {
        if (taskIdRequest == null) {
            return RpcResultUtil.fail("获取任务失败，参数为空");
        }
        if(taskIdRequest.getTaskId ()==null){
            return RpcResultUtil.fail ("任务ID为空");
        }
        Long taskId = taskIdRequest.getTaskId();
        TaskDAO taskDAO = publishManager.getTaskById(taskId);
        if (taskDAO == null) {
            return RpcResultUtil.fail("未查询到相关任务信息");
        }
        TaskInfo taskInfo = taskConvert.convertDAO2Info(taskDAO);
        return RpcResultUtil.success(new TaskResponse(taskInfo));
    }

    /**
     * 获取当前所有进行中的任务
     * @return
     */
    @Override
    public RpcResult<TaskListResponse> getTasksRunning(PageRequest pageRequest) {
        if (pageRequest == null) {
            return RpcResultUtil.fail("获取任务失败，request为空");
        }
        if (!PageCheckUtil.pageParamIsLegal(pageRequest.getPage())) {
            return RpcResultUtil.fail("分页参数不合法");
        }
        TaskListResponse tasksResponse = new TaskListResponse();
        List<TaskDAO> taskDAOS = publishManager.getTasksRunning(pageRequest.getPage());
        ArrayList<TaskInfo> taskInfos = new ArrayList<>();
        for (TaskDAO taskDAO : taskDAOS) {
            taskInfos.add(taskConvert.convertDAO2Info(taskDAO));
        }
        taskInfos.sort(Comparator.comparing(TaskInfo::getTestStartTime, Comparator.reverseOrder()));
        tasksResponse.setPage(new Page(publishManager.countTaskNum(null, 2)));
        tasksResponse.setTaskInfoList(taskInfos);
        return RpcResultUtil.success(tasksResponse);
    }

    /**
     * 获取平台中的所有任务
     * @param pageRequest
     * @return
     */
    @Override
    public RpcResult<TaskListResponse> getAllTasks(PageRequest pageRequest) {
        if (pageRequest == null) {
            return RpcResultUtil.fail("获取任务失败，request为空");
        }
        if (!PageCheckUtil.pageParamIsLegal(pageRequest.getPage())) {
            return RpcResultUtil.fail("分页参数不合法");
        }
        TaskListResponse tasksResponse = new TaskListResponse();
        List<TaskDAO> taskDAOS = publishManager.getAllTasks(pageRequest.getPage());
        ArrayList<TaskInfo> taskInfos = new ArrayList<>();
        for (TaskDAO taskDAO : taskDAOS) {
            taskInfos.add(taskConvert.convertDAO2Info(taskDAO));
        }
        taskInfos.sort(Comparator.comparing(TaskInfo::getTestStartTime, Comparator.reverseOrder()));
        tasksResponse.setTaskInfoList(taskInfos);
        tasksResponse.setPage(new Page(publishManager.countTaskNum(null, 0)));
        return RpcResultUtil.success(tasksResponse);
    }

    /**
     * 根据任务id的列表获取任务信息的map
     * @param taskIdsRequest
     * @return
     */
    @Override
    public RpcResult<TaskMapResponse> getTaskMapByTaskIds(TaskIdsRequest taskIdsRequest) {
        if (taskIdsRequest == null) {
            return RpcResultUtil.fail("获取任务失败，request为空");
        }
        if (CollectionUtils.isEmpty(taskIdsRequest.getTaskIds())) {
            return RpcResultUtil.fail("获取任务失败，id列表为空");
        }
        TaskMapResponse taskMapResponse = new TaskMapResponse();
        List<TaskDAO> taskDAOS = publishManager.batchGetTaskById(taskIdsRequest.getTaskIds(), taskIdsRequest.getType());
        HashMap<Long, TaskInfo> taskInfoMap = new HashMap<>();
        for (TaskDAO taskDAO : taskDAOS) {
            taskInfoMap.put(taskDAO.getTaskId(), taskConvert.convertDAO2Info(taskDAO));
        }
        taskMapResponse.setTaskInfoMap(taskInfoMap);
        return RpcResultUtil.success(taskMapResponse);
    }

    /**
     * 根据任务id获取当前的任务状态，分为未提交和已提交
     * @param taskIdRequest
     * @return
     */
    @Override
    public RpcResult<TaskStateResponse> getTaskStateByTaskId(TaskIdRequest taskIdRequest) {
        if(taskIdRequest==null){
            return RpcResultUtil.fail("获取任务状态失败，request为空");
        }
        if(taskIdRequest.getTaskId()==null){
            return RpcResultUtil.fail("获取任务状态失败，id为空");
        }
        TaskDAO taskDAO=publishManager.getTaskById(taskIdRequest.getTaskId());
        TaskStateResponse taskStateResponse=new TaskStateResponse();
        taskStateResponse.setSubmitted(!taskDAO.getTestStartTime().equals(taskDAO.getTestEndTime()));
        return RpcResultUtil.success(taskStateResponse);
    }

    /**
     * 根据当前任务id获取当前任务的最大工人数量
     * @param taskIdRequest
     * @return
     */
    @Override
    public RpcResult<TaskMaxWorkerCountResponse> getMaxWorkerCountByTaskId(TaskIdRequest taskIdRequest) {
        if(taskIdRequest==null){
            return RpcResultUtil.fail("获取最大工人数量失败，request为空");
        }
        Long taskId=taskIdRequest.getTaskId();
        if(taskId==null){
            return RpcResultUtil.fail("获取最大工人数量失败，id为空");
        }
        Integer maxWorkerCountByTaskId = publishManager.getMaxWorkerCountByTaskId(taskId);
        TaskMaxWorkerCountResponse taskMaxWorkerCountResponse=new TaskMaxWorkerCountResponse();
        taskMaxWorkerCountResponse.setMaxWorkerCount(maxWorkerCountByTaskId);
        return RpcResultUtil.success(taskMaxWorkerCountResponse);
    }

    /**
     * 根据当前任务Id获取当前任务的最晚可取消时间
     * @param taskIdRequest
     * @return
     */
    @Override
    public RpcResult<TaskTimeResponse> getAllowCancelTimeByTaskId(TaskIdRequest taskIdRequest) {
        if(taskIdRequest==null){
            return RpcResultUtil.fail("获取任务最晚取消时间失败，request为空");
        }
        Long taskId=taskIdRequest.getTaskId();
        if(taskId==null){
            return RpcResultUtil.fail("获取任务最晚取消时间失败，id为空");
        }
        LocalDateTime allowCancelTime=publishManager.getAllowCancelTimeByTaskId(taskId);
        //防止真的没填allowcanceltime
        if(allowCancelTime==null){
            allowCancelTime=publishManager.getTestEndTimeByTaskId(taskId);
        }
        TaskTimeResponse taskTimeResponse =new TaskTimeResponse();
        taskTimeResponse.setAllowCancelTime(LocalDateTimeUtil.asString(allowCancelTime));
        return RpcResultUtil.success(taskTimeResponse);
    }

    /**
     * 根据当前任务id获取当前任务的结束时间
     * @param taskIdRequest
     * @return
     */
    @Override
    public RpcResult<TaskTimeResponse> getTaskEndTimeByTaskId(TaskIdRequest taskIdRequest) {
        if(taskIdRequest==null){
            return RpcResultUtil.fail("获取任务最晚取消时间失败，request为空");
        }
        Long taskId=taskIdRequest.getTaskId();
        if(taskId==null){
            return RpcResultUtil.fail("获取任务最晚取消时间失败，id为空");
        }
        LocalDateTime allowCancelTime=publishManager.getTestEndTimeByTaskId(taskId);
        TaskTimeResponse taskTimeResponse =new TaskTimeResponse();
        taskTimeResponse.setTestEndTime(LocalDateTimeUtil.asString(allowCancelTime));
        return RpcResultUtil.success(taskTimeResponse);
    }

    @Override
    public RpcResult<TaskCountResponse> getTaskCountByPublisherId(PublisherIdRequest publisherIdRequest) {
        if(publisherIdRequest ==null){
            return RpcResultUtil.fail("获取当前用户发布任务数失败，request为空");
        }
        Long publisherId= publisherIdRequest.getUserId();
        if(publisherId==null){
            return RpcResultUtil.fail("获取当前用户发布任务数失败，id为空");
        }
        Integer count=publishManager.countTaskNum(publisherId,0);
        TaskCountResponse taskCountResponse=new TaskCountResponse();
        taskCountResponse.setTaskCount(count);
        return RpcResultUtil.success(taskCountResponse);
    }

    /**
     * 获取当前任务的发布者id
     * @param taskIdRequest
     * @return
     */
    @Override
    public RpcResult<PublisherIdResponse> getPublisherIdByTaskId(TaskIdRequest taskIdRequest) {
        if(taskIdRequest==null){
            return RpcResultUtil.fail("获取发布者id失败，request为空");
        }
        Long taskId=taskIdRequest.getTaskId();
        if(taskId==null){
            return RpcResultUtil.fail("获取发布者id失败，id为空");
        }
        Long publisherId=publishManager.getPublisherIdByTaskId(taskId);
        PublisherIdResponse publisherIdResponse=new PublisherIdResponse(publisherId);
        return RpcResultUtil.success(publisherIdResponse);
    }

}
