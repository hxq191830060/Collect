package org.promise.http.service.controller;

import org.promise.common.result.http.HttpResult;
import org.promise.http.service.httpService.ReportOptimizationHttpService;
import org.promise.http.service.util.CommonCheckUtil;
import org.promise.http.service.vo.report.classify.ReportClassifyVO;
import org.promise.http.service.vo.report.cluster.ReportClusteringVO;
import org.promise.http.service.vo.report.integration.ReportIntegrationVO;
import org.promise.http.service.vo.report.ReportScoreVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moqi
 * @date 2022/5/16 20:23
 */
@RestController
@RequestMapping("/report")
public class ReportOptimizationController {

    @Resource
    CommonCheckUtil commonCheckUtil;

    @Resource
    private ReportOptimizationHttpService reportOptimizationHttpService;

    @GetMapping("/clusterReport")
    public HttpResult<List<ReportClusteringVO>> clusterReport(@RequestParam Long taskId,
                                                              @RequestParam Integer k) throws Exception {
        commonCheckUtil.checkLong(taskId);
        commonCheckUtil.checkInteger(k);
        return reportOptimizationHttpService.clusteringReport(taskId,k);
    }

    @GetMapping("/classifyReport")
    public HttpResult<List<ReportClassifyVO>> classifyReport(@RequestParam Long taskId) throws Exception {
        commonCheckUtil.checkLong(taskId);
        return reportOptimizationHttpService.classifyReport(taskId);
    }

    @GetMapping("/sortReport")
    public HttpResult<List<ReportScoreVO>> sortReport(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return reportOptimizationHttpService.sortReport(taskId);
    }

    @GetMapping("/filterReport")
    public HttpResult<List<ReportScoreVO>> filterReport(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return reportOptimizationHttpService.filterReport(taskId);
    }

    @GetMapping("/integrateReport")
    public HttpResult<ReportIntegrationVO> integrateReport(@RequestParam Long taskId) throws Exception {
        commonCheckUtil.checkLong(taskId);
        return reportOptimizationHttpService.integrateReport(taskId);
    }

    @GetMapping("/whetherOptimizeReport")
    public HttpResult<Boolean> whetherOptimizeReport(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return reportOptimizationHttpService.whetherOptimizeReport(taskId);
    }

    @GetMapping("/getFinishedReportCount")
    public HttpResult<Integer> getFinishedReportCount(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return reportOptimizationHttpService.getFinishedReportCount(taskId);
    }
}
