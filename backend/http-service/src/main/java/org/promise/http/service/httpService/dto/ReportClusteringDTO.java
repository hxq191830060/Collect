package org.promise.http.service.httpService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author moqi
 * @date 2022/5/25 19:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportClusteringDTO {

    private List<Double> feature;

    private String label;

    private Long reportId;

}
