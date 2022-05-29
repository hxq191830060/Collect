package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.common.result.page.Page;
import org.promise.test.service.api.info.TestInfo;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestInfoResponse implements Serializable {
    private static final long serialVersionUID = 7908122610603592470L;

    private List<TestInfo> testInfoList;

    private Page page;
}
