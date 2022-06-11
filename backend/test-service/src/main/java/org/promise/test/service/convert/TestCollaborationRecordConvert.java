package org.promise.test.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;
import org.promise.test.service.manager.dao.TestCollaborationRecordDAO;
import org.promise.test.service.mapperService.po.TestCollaborationRecordPO;

import java.util.List;

@Mapper
public interface TestCollaborationRecordConvert {
    TestCollaborationRecordConvert INSTANCE= Mappers.getMapper (TestCollaborationRecordConvert.class );

    TestCollaborationRecordPO convertDAO2PO(TestCollaborationRecordDAO testCollaborationRecordDAO);

    TestCollaborationRecordDAO convertPO2DAO( TestCollaborationRecordPO testCollaborationRecordPO);

    List<TestCollaborationRecordDAO> convertPOList2DAOList( List<TestCollaborationRecordPO> testCollaborationRecordPOList);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestCollaborationRecordInfo convertDAO2Info(TestCollaborationRecordDAO testCollaborationRecordDAO);


    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestCollaborationRecordDAO convertInfo2DAO(TestCollaborationRecordInfo testCollaborationRecordInfo);

    List<TestCollaborationRecordInfo> convertDAOList2InfoList(List<TestCollaborationRecordDAO> testCollaborationRecordDAOList);
}
