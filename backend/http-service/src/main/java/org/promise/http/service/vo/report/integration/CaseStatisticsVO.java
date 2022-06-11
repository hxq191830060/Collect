package org.promise.http.service.vo.report.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author moqi
 * @date 2022/5/24 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseStatisticsVO {
    private Long caseId;

    private Integer pass;

    private Integer failed;
}
