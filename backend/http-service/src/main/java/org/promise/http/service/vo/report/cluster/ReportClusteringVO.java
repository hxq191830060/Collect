package org.promise.http.service.vo.report.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/5/24 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportClusteringVO {

    private Double xAxis;

    private Double yAxis;

    private Integer clusteringType;

    private Long testId;

    private String nickname;

    private Long workerId;

    private String avatar;
}
