package org.promise.http.service.vo.report.classify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author moqi
 * @date 2022/5/24 15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportClassifyVO {

    private String name;

    private List<ReportClassVO> children;
}
