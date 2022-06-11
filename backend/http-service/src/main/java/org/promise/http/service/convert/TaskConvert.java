package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.http.service.vo.publish.TaskFileVO;
import org.promise.http.service.vo.publish.TaskVO;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.api.info.TaskInfo;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/2 13:35
 */
@Mapper(componentModel = "spring")
public interface TaskConvert {

    @Mapping(target = "taskName",source = "name")
    @Mapping(target = "testStartTime",source = "startTime")
    @Mapping(target = "testEndTime",source = "endTime")
    @Mapping(target = "testWorkerCount",source = "totalWorker")
    @Mapping(target = "testType",source = "mode")
    @Mapping(source = "taskFileList",target ="taskFileInfoList" )
    @Mapping(target = "allowCancelTime",source = "cancellationTime")
    @Mapping(target = "requirementDescription",source = "requestDescription")
    @Mapping(target = "basicFunction",source = "baseFunction")
    @Mapping(target = "taskImgList",source = "previewShots")
    @Mapping(target = "taskSkillList",source = "requireSkills")
    @Mapping(target = "taskOsList",source = "testEnvironments")
    TaskInfo convertVO2Info(TaskVO taskVO);

    @Mapping(target = "name",source = "taskName")
    @Mapping(target = "startTime",source = "testStartTime")
    @Mapping(target = "endTime",source = "testEndTime")
    @Mapping(target = "totalWorker",source = "testWorkerCount")
    @Mapping(target = "mode",source = "testType")
    @Mapping(target = "taskFileList",source ="taskFileInfoList" )
    @Mapping(target = "cancellationTime",source = "allowCancelTime")
    @Mapping(target = "requestDescription",source = "requirementDescription")
    @Mapping(target = "baseFunction",source = "basicFunction")
    @Mapping(target = "previewShots",source = "taskImgList")
    @Mapping(target = "requireSkills",source = "taskSkillList")
    @Mapping(target = "testEnvironments",source = "taskOsList")
    TaskVO convertInfo2VO(TaskInfo taskInfo);

    List<TaskVO> convertInfoList2VOList(List<TaskInfo> taskInfos);

    @Mapping(target = "name",source = "fileName")
    @Mapping(target = "url",source = "fileUrl")
    @Mapping(target = "size",source = "fileSize")
    @Mapping(target = "uploadTime",source = "fileLastUpdateTime")
    TaskFileVO convertInfo2VO(TaskFileInfo taskFileInfo);


    @Mapping(source = "name",target = "fileName")
    @Mapping(source = "url",target = "fileUrl")
    @Mapping(source = "size",target = "fileSize")
    @Mapping(source = "uploadTime",target = "fileLastUpdateTime")
    TaskFileInfo convertVO2Info(TaskFileVO taskFileVO);
}
