package org.promise.http.service.httpService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author moqi
 * @date 2022/5/25 20:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportClassifyDTO {

    private String label;

    private Long reportId;
}
