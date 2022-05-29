package org.promise.http.service.vo.report.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

/**
 * @author moqi
 * @date 2022/5/27 20:22
 */
@Data
@AllArgsConstructor
public class ReportRelationVO {

    private List<ReportRelationNodeVO> nodes;

    private List<ReportRelationLinkVO> links;

    private List<ReportRelationCategoryVO> categories;

    public ReportRelationVO() {
        this.nodes = new ArrayList<>();
        this.links = new ArrayList<>();
        this.categories = new ArrayList<>();
    }
}
