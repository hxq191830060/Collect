package org.promise.test.service.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:28
 * @description:
 */
@Data
public class CreateTestRequest implements Serializable {

    private static final long serialVersionUID = 2453489591343326896L;

    private Long taskId;

    private Long workerId;

    private Integer allowedMaxWorkerCount;//允许的最大接受该任务的工人数目

}
