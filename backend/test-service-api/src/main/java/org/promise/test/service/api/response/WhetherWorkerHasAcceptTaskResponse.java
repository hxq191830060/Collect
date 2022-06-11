package org.promise.test.service.api.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:黄相淇
 * @date:2022年04月02日10:44
 * @description:
 */
@Data
public class WhetherWorkerHasAcceptTaskResponse implements Serializable {

    private boolean hasAccept;

    private Long testId;

    private String state;
}
