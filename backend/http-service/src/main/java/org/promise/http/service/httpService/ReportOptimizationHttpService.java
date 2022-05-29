package org.promise.http.service.httpService;

import org.promise.common.result.http.HttpResult;
import org.promise.http.service.vo.report.classify.ReportClassifyVO;
import org.promise.http.service.vo.report.cluster.ReportClusteringVO;
import org.promise.http.service.vo.report.integration.ReportIntegrationVO;
import org.promise.http.service.vo.report.ReportScoreVO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/5/16 20:24
 */

public interface ReportOptimizationHttpService {

    HttpResult<List<ReportClusteringVO>> clusteringReport(Long taskId, Integer k) throws Exception;

    HttpResult<List<ReportClassifyVO>> classifyReport(Long taskId) throws Exception;

    HttpResult<List<ReportScoreVO>> sortReport(Long taskId);

    HttpResult<List<ReportScoreVO>> filterReport(Long taskId);

    HttpResult<ReportIntegrationVO> integrateReport(Long taskId) throws Exception;

    HttpResult<Boolean> whetherOptimizeReport(Long taskId);

    HttpResult<Integer> getFinishedReportCount(Long taskId);
}
