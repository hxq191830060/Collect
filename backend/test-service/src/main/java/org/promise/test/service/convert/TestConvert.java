package org.promise.test.service.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.api.request.SubmitTestRequest;
import org.promise.test.service.manager.dao.TestDAO;
import org.promise.test.service.mapperService.po.TestPO;

import java.util.List;

@Mapper
public interface TestConvert {

    TestConvert INSTANCE= Mappers.getMapper (TestConvert.class);

    @Mapping (target = "testTools",expression = "java(org.promise.common.util.JsonUtil.fromJsonToList(testPO.getTestTools(),String.class))")
    TestDAO convertTestPO2TestDAO(TestPO testPO);

    @Mapping (target = "testTools",expression = "java(org.promise.common.util.JsonUtil.toJson(testDAO.getTestTools()))")
    TestPO convertTestDAO2TestPO(TestDAO testDAO);

    List<TestDAO> convertTestPOList2TestDAOList(List<TestPO> testPOList);

    @Mapping(target = "acceptTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "finishTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "cancelTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestInfo convertTestDAO2TestInfo(TestDAO testDAO);


    List<TestInfo> convertTestDAOList2TestInfoList(List<TestDAO> testDAOList);


    TestDAO convertSubmitTestRequest2TestDAO( SubmitTestRequest request);

}
