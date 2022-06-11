package org.promise.test.service.api.info;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author moqi
 * @date 2022/5/19 14:49
 */
@Data
@AllArgsConstructor
public class ReportIntegrationInfo implements Serializable {

    private static final long serialVersionUID = 5682399851871009204L;

    private Map<Long, Integer> rightCaseCountMap;

    private Map<Long, Integer> wrongCaseCountMap;

    private Map<String, Integer> operatingSystemCountMap;

    private Map<String, Integer> testToolCountMap;

    public ReportIntegrationInfo() {
        this.rightCaseCountMap = new HashMap<>();
        this.wrongCaseCountMap = new HashMap<>();
        this.operatingSystemCountMap = new HashMap<>();
        this.testToolCountMap = new HashMap<>();
    }
}
