package org.promise.http.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.http.service.vo.publish.TaskFileVO;
import org.promise.publish.service.api.info.TaskFileInfo;

/**
 * @author moqi
 * @date 2022/3/2 14:12
 */
@Mapper(componentModel = "spring")
public interface TaskFileConvert {

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
