package org.promise.http.service.httpService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/3/31 19:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSimilarityDTO {

    private Long reportId;

    private Double sim;
}
