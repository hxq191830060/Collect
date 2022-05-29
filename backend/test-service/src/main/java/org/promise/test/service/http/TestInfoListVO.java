package org.promise.test.service.http;

import lombok.Data;
import org.promise.test.service.api.info.TestInfo;

import java.io.Serializable;
import java.util.List;

@Data
public class TestInfoListVO implements Serializable {
    private static final long serialVersionUID = -7679428776547888027L;

    private List<TestInfo> list;
}
