package org.promise.http.service.vo.report.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/5/27 20:27
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRelationLinkVO {

    private String source;

    private String target;
}
