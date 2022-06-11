package org.promise.test.service;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.RpcResultUtil;
import org.promise.test.service.api.api.ReportOptimizationService;
import org.promise.test.service.api.request.TestQueryRequest;
import org.promise.test.service.api.response.ReportIntegrationResponse;
import org.promise.test.service.api.response.TestSummaryResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author moqi
 * @date 2022/5/27 16:16
 */
@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
@Transactional  //事务管理，默认回滚,如果配置了多数据源记得指定事务管理器
public class ReportOptimizationServiceTest {

    @Resource
    ReportOptimizationService reportOptimizationService;

    @Test
    void getTestSummaryByTaskIdTest(){
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(260L);
        RpcResult<TestSummaryResponse> testSummaryByTaskId = reportOptimizationService.getTestSummaryByTaskId(testQueryRequest);
        Assertions.assertEquals(testSummaryByTaskId.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getReportIntegrationResponse(){
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(260L);
        RpcResult<ReportIntegrationResponse> reportIntegrationResponse = reportOptimizationService.getReportIntegrationResponse(testQueryRequest);
        Assertions.assertEquals(reportIntegrationResponse.getCode(), RpcCodes.SUCCESS.getCode());
    }

}
