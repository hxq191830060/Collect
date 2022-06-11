package org.promise.test.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.CollaborationCaseInfo;
import org.promise.test.service.api.info.CollaborationInfo;
import org.promise.test.service.manager.dao.CollaborationCaseDAO;
import org.promise.test.service.mapperService.po.CollaborationCasePO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 1:03
 */
@Mapper
public interface CollaborationCaseConvert {

    CollaborationCaseConvert INSTANCE= Mappers.getMapper (CollaborationCaseConvert.class );

    CollaborationCaseDAO convertPO2DAO(CollaborationCasePO collaborationCasePO);

    CollaborationCasePO convertDAO2PO(CollaborationCaseDAO collaborationCaseDAO);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    CollaborationCaseDAO convertInfo2DAO(CollaborationCaseInfo collaborationCaseInfo);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    CollaborationCaseInfo convertDAO2Info(CollaborationCaseDAO collaborationCaseDAO);


    List<CollaborationCaseDAO> convertPOList2DAOList(List<CollaborationCasePO> collaborationCasePOList);

    List<CollaborationCasePO> convertDAOList2POList(List<CollaborationCaseDAO> collaborationCaseDAOList);

    List<CollaborationCaseDAO> convertInfoList2DAOList(List<CollaborationCaseInfo> collaborationCaseInfoList);

    List<CollaborationCaseInfo> convertDAOList2InfoList( List<CollaborationCaseDAO> collaborationCaseDAOList);
}
