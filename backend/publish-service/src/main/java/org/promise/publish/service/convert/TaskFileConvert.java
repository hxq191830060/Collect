package org.promise.publish.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.manager.dao.TaskFileDAO;
import org.promise.publish.service.mapperService.po.TaskFilePO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 19:22
 */
@Mapper(componentModel = "spring")
public interface TaskFileConvert {

    TaskFileDAO convertPO2DAO(TaskFilePO taskFilePO);


    TaskFilePO convertDAO2PO(TaskFileDAO taskFileDAO);

    @Mapping(target = "fileLastUpdateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TaskFileDAO convertInfo2DAO(TaskFileInfo taskFileInfo);

    @Mapping(target = "fileLastUpdateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TaskFileInfo convertDAO2Info(TaskFileDAO taskFileDAO);

    List<TaskFileDAO> convertPOList2DAOList(List<TaskFilePO> taskFilePOS);

    List<TaskFileInfo> convertDAOList2InfoList(List<TaskFileDAO> taskFileDAOS);
}
