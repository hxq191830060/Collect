package org.promise.test.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.TestCaseInfo;
import org.promise.test.service.manager.dao.TestCaseDAO;
import org.promise.test.service.mapperService.po.TestCasePO;

import java.util.List;

@Mapper
public interface TestCaseConvert {
    TestCaseConvert INSTANCE= Mappers.getMapper (TestCaseConvert.class);

    TestCaseDAO convertTestCasePO2DAO( TestCasePO testCasePO);
    
    List<TestCaseDAO> convertPOList2DAOList( List<TestCasePO> testCasePOList);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestCaseInfo convertTestCaseDAO2Info(TestCaseDAO testCaseDAO);

    List<TestCaseInfo> convertDAOList2InfoList(List<TestCaseDAO> testCaseDAOList);

    @Mapping(target = "createTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestCaseDAO convertTestCaseInfo2TestCaseDAO(TestCaseInfo testCaseInfo);


    List<TestCaseDAO> convertTestCaseInfoList2TestCaseDAOList(List<TestCaseInfo> testCaseInfoList);


    TestCasePO convertTestCaseDAO2TestCasePO(TestCaseDAO testCaseDAO);

    List<TestCasePO> convertTestCaseDAOList2TestCasePOList(List<TestCaseDAO> testCaseDAOList);
}
