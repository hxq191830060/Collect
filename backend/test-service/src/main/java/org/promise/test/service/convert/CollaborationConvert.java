package org.promise.test.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.CollaborationInfo;
import org.promise.test.service.manager.dao.CollaborationDAO;
import org.promise.test.service.mapperService.po.CollaborationPO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 1:03
 */
@Mapper
public interface CollaborationConvert {

    CollaborationConvert INSTANCE= Mappers.getMapper (CollaborationConvert.class);

    @Mapping (target = "bugImgUrl",expression = "java(org.promise.common.util.JsonUtil.fromJson(collaborationPO.getBugImgUrl(),List.class))")
    @Mapping (target = "testTools",expression = "java(org.promise.common.util.JsonUtil.fromJson(collaborationPO.getTestTools(),List.class))")
    CollaborationDAO convertPO2DAO(CollaborationPO collaborationPO);


    @Mapping (target = "bugImgUrl",expression = "java(org.promise.common.util.JsonUtil.toJson(collaborationDAO.getBugImgUrl()))")
    @Mapping (target = "testTools",expression = "java(org.promise.common.util.JsonUtil.toJson(collaborationDAO.getTestTools()))")
    CollaborationPO convertDAO2PO(CollaborationDAO collaborationDAO);


    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    CollaborationDAO convertInfo2DAO( CollaborationInfo collaborationInfo);



    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    CollaborationInfo convertDAO2Info(CollaborationDAO collaborationDAO);

    List<CollaborationDAO> convertPOList2DAOList( List<CollaborationPO> collaborationPOList);

    List<CollaborationInfo> convertDAOList2InfoList(List<CollaborationDAO> collaborationDAOList);
}
