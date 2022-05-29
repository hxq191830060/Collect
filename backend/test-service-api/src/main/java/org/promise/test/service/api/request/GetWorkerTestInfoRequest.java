package org.promise.test.service.api.request;

import lombok.Data;
import org.promise.common.result.page.Page;

import java.io.Serializable;
import java.util.List;

@Data
public class GetWorkerTestInfoRequest implements Serializable {
    private static final long serialVersionUID = 5633572146670020354L;

    //工人的workerId
    private Long workerId;

    //要查询的指定状态的test(running,finish,cancel三选多)
    private List<String> stateList;

    //分页参数
    private Page page;
}
