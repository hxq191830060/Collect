package org.promise.test.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.TestBugImgInfo;
import org.promise.test.service.manager.dao.TestBugImgDAO;
import org.promise.test.service.mapperService.po.TestBugImgPO;

import java.util.List;

@Mapper
public interface TestBugImgConvert {
    TestBugImgConvert INSTANCE= Mappers.getMapper (TestBugImgConvert.class);

    TestBugImgDAO convertTestBugImgPO2DAO( TestBugImgPO  testBugImgPO);

    List<TestBugImgDAO> convertPOList2DAOList(List<TestBugImgPO> testBugImgPOList);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestBugImgInfo convertTestBugImgDAO2Info(TestBugImgDAO testBugImgDAO);


    List<TestBugImgInfo> convertDAOList2InfoList(List<TestBugImgDAO> testBugImgDAOList);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestBugImgDAO convertTestBugImgInfo2DAO(TestBugImgInfo testBugImgInfo);


    List<TestBugImgDAO> convertInfoList2DAOList(List<TestBugImgInfo> testBugImgInfoList);

    TestBugImgPO convertTestBugImgDAO2PO(TestBugImgDAO testBugImgDAO);


    List<TestBugImgPO> convertTestBugImgDAOList2POList(List<TestBugImgDAO> testBugImgDAOList);
}
