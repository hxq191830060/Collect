package org.promise.http.service.vo.report.classify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/5/24 15:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportClassVO {
    private String name;

    private Integer value=1;

    private Long testId;

    private Long workerId;

    private String avatar;
}
