package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.TestSummaryInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/5/18 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSummaryResponse implements Serializable {

    private static final long serialVersionUID = 972207800982675187L;

    List<TestSummaryInfo> testSummaryInfoList;
}
