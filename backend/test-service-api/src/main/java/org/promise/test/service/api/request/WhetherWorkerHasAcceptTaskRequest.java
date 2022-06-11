package org.promise.test.service.api.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class WhetherWorkerHasAcceptTaskRequest implements Serializable {
    private static final long serialVersionUID = -2497820006247479923L;

    private Long workerId;

    private Long taskId;

}
