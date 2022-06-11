package org.promise.http.service.httpService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author moqi
 * @date 2022/5/27 19:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassifySimDTO {

    private String label;

    private Long reportId;

    private Double sim;

    private String labelName;

}
