package org.promise.http.service.vo.report.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author moqi
 * @date 2022/5/24 15:08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportIntegrationVO {

    private List<WordCloudVO> defectWordCloud;

    private List<WordCloudVO> suggestWordCloud;

    private List<OperatingSystemVO> testEnv;

    private List<TestToolVO> testTool;

    private List<CaseStatisticsVO> caseStatistics;

    private ReportRelationVO reportRelation;
}
