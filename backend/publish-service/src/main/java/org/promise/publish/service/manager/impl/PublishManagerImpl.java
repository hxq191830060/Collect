package org.promise.publish.service.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.common.constant.ExceptionEnum;
import org.promise.common.result.page.Page;
import org.promise.publish.service.convert.TaskFileConvert;
import org.promise.publish.service.convert.TaskConvert;
import org.promise.publish.service.manager.PublishManager;
import org.promise.publish.service.manager.dao.TaskDAO;
import org.promise.publish.service.manager.dao.TaskFileDAO;
import org.promise.publish.service.mapperService.*;
import org.promise.publish.service.mapperService.po.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 15:48
 */
@Service
@Slf4j
public class PublishManagerImpl implements PublishManager {

    @Resource
    private TaskConvert taskConvert;

    @Resource
    private TaskFileConvert taskFileConvert;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TaskFileMapper taskFileMapper;

    @Resource
    private TestCountMapper testCountMapper;

    /**
     * 发布任务，注意需要在两个表进行插入操作
     * @return 所发布的任务的id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer publishTask(TaskDAO taskDAO) {
        if (taskDAO == null) {
            return -1;
        }
        TaskPO taskPO = taskConvert.convertDAO2PO(taskDAO);
        taskMapper.insert(taskPO);
        Long taskId = taskPO.getTaskId();
        taskDAO.setTaskId(taskId);
        testCountMapper.insert(new TestCountPO(taskId));
        return 1;
    }

    @Override
    public Integer publishFile(List<TaskFileDAO> taskFileDAOS) {
        ArrayList<TaskFilePO> list = new ArrayList<>();
        for (TaskFileDAO taskFileDAO : taskFileDAOS) {
            list.add(taskFileConvert.convertDAO2PO(taskFileDAO));
        }
        return taskFileMapper.insertBatch(list);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateTask(TaskDAO taskDAO) {
        if (taskDAO == null) {
            ExceptionEnum.NullException.maybeThrow();
        }
        TaskPO taskPO=taskConvert.convertDAO2PO(taskDAO);
        TaskPO taskPOInDatabase=taskMapper.selectTaskById(taskDAO.getTaskId());
        LocalDateTime endTime=taskPO.getTestEndTime()==null?taskPOInDatabase.getTestEndTime():taskPO.getTestEndTime();
        LocalDateTime allowCancelTime=taskPO.getAllowCancelTime()==null?taskPOInDatabase.getAllowCancelTime():taskPO.getAllowCancelTime();
        if(allowCancelTime!=null&&allowCancelTime.isAfter(endTime)){
            ExceptionEnum.CancelTimeWrongException.maybeThrow();
        }
        taskMapper.update(taskPO);
        return 1;
    }


    /**
     * 根据发布者ID查询任务
     * @param publishId 发布者ID
     * @param type      查询类型，0为查询所有，1为查询未开始，2为查询开始但未结束，3为查询结束
     */
    @Override
    public List<TaskDAO> getTasksByPublisherId(Long publishId, Integer type, Page page) {
        List<TaskPO> taskPOS = taskMapper.selectTasksByPublisherId(publishId, type,
                (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
        return getTaskDAOList(taskPOS);
    }

    /**
     * 将taskPO的列表转换成DAO的列表并对当前接受任务的人数进行赋值
     * @param taskPOS taskPO的列表
     * @return DAO的列表
     */
    private List<TaskDAO> getTaskDAOList(List<TaskPO> taskPOS) {
        if (CollectionUtils.isEmpty(taskPOS)) {
            return Collections.emptyList();
        }
        HashMap<Long, Integer> taskIdMap = getWorkerCountMap(taskPOS);
        ArrayList<TaskDAO> taskDAOS = new ArrayList<>();
        for (TaskPO taskPO : taskPOS) {
            TaskDAO taskDAO = taskConvert.convertPO2DAO(taskPO);
            taskDAO.setCurrentWorker(taskIdMap.getOrDefault(taskDAO.getTaskId(), 0));
            taskDAOS.add(taskDAO);
        }
        return taskDAOS;
    }

    /**
     * 根据任务id批量获取任务
     * @param taskPOS
     * @return
     */
    private HashMap<Long, Integer> getWorkerCountMap(List<TaskPO> taskPOS) {
        if (CollectionUtils.isEmpty(taskPOS)) {
            ExceptionEnum.NullException.maybeThrow();
        }
        ArrayList<Long> taskIdList = new ArrayList<>();
        for (TaskPO po : taskPOS) {
            taskIdList.add(po.getTaskId());
        }
        List<TestCountPO> testCountPOList = testCountMapper.batchCountWorkerByTaskId(taskIdList);
        HashMap<Long, Integer> taskIdMap = new HashMap<>();
        for (TestCountPO testCountPO : testCountPOList) {
            taskIdMap.put(testCountPO.getTaskId(), testCountPO.getTestNum());
        }
        return taskIdMap;
    }

    /**
     * 根据任务id获取任务详细信息
     * @param taskId
     * @return
     */
    @Override
    public TaskDAO getTaskById(Long taskId) {
        TaskPO taskPO = taskMapper.selectTaskById(taskId);
        if (taskPO == null) {
            ExceptionEnum.TaskDoesNotExistException.maybeThrow();
        }
        List<TaskFileDAO> taskFileDAOS =
                taskFileConvert.convertPOList2DAOList(taskFileMapper.getTaskFileByTaskId(taskId));
        TaskDAO taskDAO = taskConvert.convertPO2DAO(taskPO);
        taskDAO.setTaskFileDAOList(taskFileDAOS);
        taskDAO.setCurrentWorker(taskMapper.countWorkerByTaskId(taskId));
        return taskDAO;
    }

    @Override
    public List<TaskFileDAO> getTaskFileByTaskId(Long taskId) {
        List<TaskFilePO> taskFilePOList = taskFileMapper.getTaskFileByTaskId(taskId);
        if (CollectionUtils.isNotEmpty(taskFilePOList)) {
            ArrayList<TaskFileDAO> taskFileDAOS = new ArrayList<>();
            for (TaskFilePO taskFilePO : taskFilePOList) {
                taskFileDAOS.add(taskFileConvert.convertPO2DAO(taskFilePO));
            }
            return taskFileDAOS;
        }
        return Collections.emptyList();
    }

    /**
     * 确认当前用户是否已经发布过同名任务,存在则返回true
     * @param taskDAO
     * @return
     */
    @Override
    public boolean checkTaskExist(TaskDAO taskDAO) {
        TaskPO taskPO = taskConvert.convertDAO2PO(taskDAO);
        List<TaskPO> res = taskMapper.selectTaskByPublisherIdAndTaskName(taskPO);
        return CollectionUtils.isNotEmpty(res);
    }

    @Override
    public List<TaskDAO> getTasksRunning(Page page) {
        List<TaskPO> taskPOS = taskMapper.selectTasksRunning(
                (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
        return getTaskDAOList(taskPOS);
    }


    @Override
    public List<TaskDAO> getAllTasks(Page page) {
        List<TaskPO> taskPOS = taskMapper.selectAllTasks(
                (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
        return getTaskDAOList(taskPOS);
    }

    /**
     * 根据任务id的列表批量查询任务信息
     * @param taskIdList
     * @param type 查询类型，0为查询所有，1为查询未开始，2为查询开始但未结束，3为查询结束
     * @return
     */
    @Override
    public List<TaskDAO> batchGetTaskById(List<Long> taskIdList, int type) {
        List<TaskPO> taskPOS = taskMapper.batchSelectTasksByTaskId(taskIdList, type);
        return getTaskDAOList(taskPOS);
    }

    /**
     * 根据条件查询任务数量
     * @param publisherId
     * @param type 查询类型，0为查询所有，1为查询未开始，2为查询开始但未结束，3为查询结束
     * @return
     */
    @Override
    public Integer countTaskNum(Long publisherId, Integer type) {
        return taskMapper.countTaskNum(publisherId, type);
    }

    /**
     * 根据任务id查看当前任务的最大员工数量
     * @param taskId
     * @return
     */
    @Override
    public Integer getMaxWorkerCountByTaskId(Long taskId) {
        TaskPO taskPO =taskMapper.selectTaskById(taskId);
        if (taskPO == null) {
            ExceptionEnum.TaskDoesNotExistException.maybeThrow();
        }
        return taskPO.getTestWorkerCount();
    }

    /**
     * 根据任务Id获取当前任务的最大可取消时间
     * @param taskId
     * @return
     */
    @Override
    public LocalDateTime getAllowCancelTimeByTaskId(Long taskId) {
        TaskPO taskPO =taskMapper.selectTaskById(taskId);
        if (taskPO == null) {
            ExceptionEnum.TaskDoesNotExistException.maybeThrow();
        }
        return taskPO.getAllowCancelTime();
    }

    /**
     * 获取当前任务的结束时间
     * @param taskId
     * @return
     */
    @Override
    public LocalDateTime getTestEndTimeByTaskId(Long taskId) {
        TaskPO taskPO =taskMapper.selectTaskById(taskId);
        if (taskPO == null) {
            ExceptionEnum.TaskDoesNotExistException.maybeThrow();
        }
        return taskPO.getTestEndTime();
    }

    /**
     * 获取当前任务的发布者id
     * @param taskId
     * @return
     */
    @Override
    public Long getPublisherIdByTaskId(Long taskId) {
        TaskPO taskPO =taskMapper.selectTaskById(taskId);
        if (taskPO == null) {
            ExceptionEnum.TaskDoesNotExistException.maybeThrow();
        }
        return taskPO.getPublisherId();
    }
}
