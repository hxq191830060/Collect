package org.promise.publish.service.api.api;

import org.promise.common.result.rpc.RpcResult;
import org.promise.publish.service.api.request.*;
import org.promise.publish.service.api.response.*;

/**
 * @author moqi
 * @date 2022/2/26 14:57
 */
public interface PublishService {

    /**
     * 发布者发布众包测试任务
     * @param publishRequest
     * @return
     */
    RpcResult<PublishResponse> publish(PublishRequest publishRequest);

    /**
     * 发布者上传测试说明文档
     * @param publishFileRequest
     * @return
     */
    RpcResult<PublishFileResponse> publishFile(PublishFileRequest publishFileRequest);

    /**
     * 发布者更新测试任务相关信息
     * @param publishRequest 只会更改taskInfo中不为null的值
     * @return
     */
    RpcResult<PublishResponse> updateTaskInfo(PublishRequest publishRequest);

    /**
     * 根据发布者的id获取发布者发布的所有任务
     * @param userIdAndPageRequest
     * @return
     */
    RpcResult<TaskListResponse> getTasksByPublisherId(UserIdAndPageRequest userIdAndPageRequest);

    /**
     * 根据任务Id获得任务的详细信息
     * @param taskIdRequest
     * @return
     */
    RpcResult<TaskResponse> getTaskById(TaskIdRequest taskIdRequest);

    /**
     * 获取当前所有进行中的任务
     * @return
     */
    RpcResult<TaskListResponse> getTasksRunning(PageRequest pageRequest);

    /**
     * 获取平台中的所有任务
     * @param pageRequest
     * @return
     */
    RpcResult<TaskListResponse> getAllTasks(PageRequest pageRequest);

    /**
     * 根据任务id的列表获取任务信息的map
     * @param taskIdsRequest
     * @return
     */
    RpcResult<TaskMapResponse> getTaskMapByTaskIds(TaskIdsRequest taskIdsRequest);


    /**
     * 根据任务id获取当前的任务状态，分为未提交和已提交
     * @param taskIdRequest
     * @return
     */
    RpcResult<TaskStateResponse> getTaskStateByTaskId(TaskIdRequest taskIdRequest);


    /**
     * 根据当前任务id获取当前任务的最大工人数量
     * @param taskIdRequest
     * @return
     */
    RpcResult<TaskMaxWorkerCountResponse> getMaxWorkerCountByTaskId(TaskIdRequest taskIdRequest);

    /**
     * 根据当前任务Id获取当前任务的最晚可取消时间
     * @param taskIdRequest
     * @return
     */
    RpcResult<TaskTimeResponse> getAllowCancelTimeByTaskId(TaskIdRequest taskIdRequest);

    /**
     * 根据当前任务id获取当前任务的结束时间
     * @param taskIdRequest
     * @return
     */
    RpcResult<TaskTimeResponse> getTaskEndTimeByTaskId(TaskIdRequest taskIdRequest);

    /**
     * 获取当前用户发布的任务数量
     * @param publisherIdRequest
     * @return
     */
    RpcResult<TaskCountResponse> getTaskCountByPublisherId(PublisherIdRequest publisherIdRequest);

    /**
     * 获取当前任务的发布者id
     * @param taskIdRequest
     * @return
     */
    RpcResult<PublisherIdResponse> getPublisherIdByTaskId(TaskIdRequest taskIdRequest);

}
