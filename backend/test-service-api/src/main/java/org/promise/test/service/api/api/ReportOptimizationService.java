package org.promise.test.service.api.api;

import org.promise.common.result.rpc.RpcResult;
import org.promise.test.service.api.request.TestQueryRequest;
import org.promise.test.service.api.response.ReportIntegrationResponse;
import org.promise.test.service.api.response.TestSummaryResponse;

/**
 * @author moqi
 * @date 2022/5/19 14:38
 */
public interface ReportOptimizationService {

    /**
     * 获取指定任务下所有已完成测试的总结数据
     * @param testQueryRequest
     * @return
     */
    RpcResult<TestSummaryResponse> getTestSummaryByTaskId(TestQueryRequest testQueryRequest);

    RpcResult<ReportIntegrationResponse> getReportIntegrationResponse(TestQueryRequest testQueryRequest);
}
