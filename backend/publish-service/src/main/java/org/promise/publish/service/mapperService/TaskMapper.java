package org.promise.publish.service.mapperService;

import org.apache.ibatis.annotations.Param;
import org.promise.common.result.page.Page;
import org.promise.publish.service.mapperService.po.TaskPO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/26 21:40
 */
public interface TaskMapper {

    Integer insert(TaskPO taskPO);

    Integer update(TaskPO taskPO);

    Integer delete(Long taskId);

    TaskPO selectTaskById(Long taskId);

    List<TaskPO> selectTasksByPublisherId(@Param("publisherId") Long publisherId,
                                          @Param("type") Integer type,
                                          @Param("pageStart") Integer pageStart,
                                          @Param("pageSize") Integer pageSize);

    List<TaskPO> selectTaskByPublisherIdAndTaskName(TaskPO taskPO);

    List<TaskPO> selectTasksRunning(@Param("pageStart") Integer pageStart,
                                    @Param("pageSize") Integer pageSize);

    Integer countWorkerByTaskId(Long taskId);

    List<TaskPO> selectAllTasks(@Param("pageStart") Integer pageStart,
                                @Param("pageSize") Integer pageSize);


    List<TaskPO> batchSelectTasksByTaskId(@Param("taskIds") List<Long> taskIds,
                                          @Param("type") Integer type);

    /**
     * 获取符合条件的任务数量
     * @param publisherId 发布者id
     * @param type 任务类型 1为未开始，2为正在进行，3为已结束
     * @return
     */
    Integer countTaskNum(@Param("publisherId") Long publisherId,
                         @Param("type") Integer type);
}
