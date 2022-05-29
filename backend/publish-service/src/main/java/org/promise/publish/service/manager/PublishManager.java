package org.promise.publish.service.manager;

import org.promise.common.result.page.Page;
import org.promise.publish.service.manager.dao.TaskDAO;
import org.promise.publish.service.manager.dao.TaskFileDAO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/9 17:21
 */
public interface PublishManager {

    /**
     * 发布任务，注意需要在两个表进行插入操作
     * @param taskDAO
     * @return 所发布的任务的id
     */
    @Transactional
    Integer publishTask(TaskDAO taskDAO);

    /**
     * 在相应任务中发布一个文件
     * @param taskFileDAOS
     * @return
     */
    Integer publishFile(List<TaskFileDAO> taskFileDAOS);

    /**
     * 更新任务的信息
     * @param taskDAO
     * @return
     */
    Integer updateTask(TaskDAO taskDAO);

    /**
     * 根据发布者ID查询任务
     * @param publishId 发布者ID
     * @param type      查询类型，0为查询所有，1为查询未开始，2为查询开始但未结束，3为查询结束
     * @return
     */
    List<TaskDAO> getTasksByPublisherId(Long publishId, Integer type, Page page);

    /**
     * 根据任务id获取任务详细信息
     * @param taskId
     * @return
     */
    TaskDAO getTaskById(Long taskId);

    /**
     * 根据任务id获取相应的文件列表
     * @param taskId
     * @return
     */
    List<TaskFileDAO> getTaskFileByTaskId(Long taskId);

    /**
     * 确认当前用户是否已经发布过同名任务,存在则返回true
     * @param taskDAO
     * @return
     */
    boolean checkTaskExist(TaskDAO taskDAO);

    /**
     * 获取所有正在进行中的任务
     * @param page
     * @return
     */
    List<TaskDAO> getTasksRunning(Page page);

    /**
     * 获取当前平台上的所有任务列表，仅供管理员使用
     * @param page
     * @return
     */
    List<TaskDAO> getAllTasks(Page page);


    /**
     * 根据任务id的列表批量查询任务信息
     * @param taskIdList
     * @param type 查询类型，0为查询所有，1为查询未开始，2为查询开始但未结束，3为查询结束
     * @return
     */
    List<TaskDAO> batchGetTaskById(List<Long> taskIdList,int type);

    /**
     * 根据条件查询任务数量
     * @param publisherId
     * @param type 查询类型，0为查询所有，1为查询未开始，2为查询开始但未结束，3为查询结束
     * @return
     */
    Integer countTaskNum(Long publisherId,Integer type);

    /**
     * 根据任务id查看当前任务的最大员工数量
     * @param taskId
     * @return
     */
    Integer getMaxWorkerCountByTaskId(Long taskId);

    /**
     * 根据任务Id获取当前任务的最大可取消时间
     * @param taskId
     * @return
     */
    LocalDateTime getAllowCancelTimeByTaskId(Long taskId);

    /**
     * 获取当前任务的结束时间
     * @param taskId
     * @return
     */
    LocalDateTime getTestEndTimeByTaskId(Long taskId);

    /**
     * 获取当前任务的发布者id
     * @param taskId
     * @return
     */
    Long getPublisherIdByTaskId(Long taskId);

}
