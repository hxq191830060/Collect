package org.promise.http.service.vo.report.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/5/27 20:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRelationNodeVO {

    private String id;

    private String name;

    private Double symbolSize;

    private Double x;

    private Double y;

    private Integer value;

    private Integer category;

}
