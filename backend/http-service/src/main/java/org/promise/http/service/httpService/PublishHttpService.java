package org.promise.http.service.httpService;

import org.promise.common.result.http.HttpResult;
import org.promise.common.result.page.Page;
import org.promise.http.service.vo.publish.TaskFileVO;
import org.promise.http.service.vo.publish.TaskListVO;
import org.promise.http.service.vo.publish.TaskVO;

/**
 * @author moqi
 * @date 2022/3/24 21:04
 */
public interface PublishHttpService {

    /**
     * 发布相关的任务
     * @param taskVO
     * @return
     */
    HttpResult<String> publish(TaskVO taskVO);

    /**
     * 更新任务相关信息
     * @param taskVO
     * @return
     */
    HttpResult<String> updateTask(TaskVO taskVO);

    /**
     * 为任务添加文件
     * @param taskFileVO
     * @return
     */
    HttpResult<String> publishFile(TaskFileVO taskFileVO);

    /**
     * 根据发布者的id获取所有的任务信息
     * @param publisherId
     * @param judgeType 当前任务的状态，0为所有，1为未开始，2为正在进行，3为已结束
     * @return
     */
    HttpResult<TaskListVO> getTaskListByPublisherId(Long publisherId, Integer judgeType, Page page);

    /**
     * 根据任务id获取详细的任务信息
     * @param taskId
     * @return
     */
    HttpResult<TaskVO> getTaskById(Long taskId);

    /**
     * 获取所有正在执行的任务信息
     * @param page
     * @return
     */
    HttpResult<TaskListVO> getRunningTasks(Page page);


    /**
     * 获取所有任务信息，包括已结束和尚未开始
     * @param page
     * @return
     */
    HttpResult<TaskListVO> getAllTasks(Page page);

    /**
     * 根据工人的id获取当前工人所接受的所有任务的信息
     * @param workerId
     * @param judgeType
     * @param page
     * @return
     */
    HttpResult<TaskListVO> getTaskListByWorkerId(Long workerId, Integer judgeType, Page page);

    /**
     * 查看当前任务是否已经提交，正在运行
     * @param taskId 任务id
     * @return 任务状态
     */
    HttpResult<Boolean> getTaskStateByTaskId(Long taskId);
}
