package org.promise.publish.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.manager.dao.TaskDAO;
import org.promise.publish.service.manager.dao.TaskFileDAO;
import org.promise.publish.service.mapperService.po.TaskPO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 16:17
 */
@Mapper(componentModel = "spring")
public interface TaskConvert {


    @Mapping(target = "taskImgList",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(taskPO.getTaskImgList(),String.class))")
    @Mapping(target = "taskSkillList",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(taskPO.getTaskSkillList(),String.class))")
    @Mapping(target = "taskOsList",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(taskPO.getTaskOsList(),String.class))")
    TaskDAO convertPO2DAO(TaskPO taskPO);

    @Mapping(target = "taskImgList", expression = "java(org.promise.common.util.JsonUtil.toJson(taskDAO.getTaskImgList()))")
    @Mapping(target = "taskSkillList", expression = "java(org.promise.common.util.JsonUtil.toJson(taskDAO.getTaskSkillList()))")
    @Mapping(target = "taskOsList", expression = "java(org.promise.common.util.JsonUtil.toJson(taskDAO.getTaskOsList()))")
    TaskPO convertDAO2PO(TaskDAO taskDAO);

    @Mapping(target = "testStartTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "testEndTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "allowCancelTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "taskFileDAOList", source = "taskFileInfoList")
    TaskDAO convertInfo2DAO(TaskInfo taskInfo);

    @Mapping(target = "testStartTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "testEndTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "allowCancelTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "taskFileInfoList", source = "taskFileDAOList")
    TaskInfo convertDAO2Info(TaskDAO taskDAO);

    List<TaskInfo> convertDAOList2InfoList(List<TaskDAO> taskDAOList);

    /**
     * 该方法作用是为其他方法提供嵌套映射
     * @param taskDocInfo
     * @return
     */
    @Mapping(target = "fileLastUpdateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TaskFileDAO convertInfo2DAO(TaskFileInfo taskDocInfo);

    /**
     * 该方法作用是为其他方法提供嵌套映射
     * @param taskDocDAO
     * @return
     */
    @Mapping(target = "fileLastUpdateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TaskFileInfo convertDAO2Info(TaskFileDAO taskDocDAO);
}
