package org.promise.test.service.api.request;

import lombok.Data;
import org.promise.common.result.page.Page;

import java.io.Serializable;

/**
 * 通用的查询请求参数，按需填充值
 */
@Data
public class TestQueryRequest implements Serializable {

    private static final long serialVersionUID = 4914345143817197848L;

    private Long testId;

    private Long taskId;

    private Long workerId;

    private Page page;

    private Double evaluationThreshold;
}
