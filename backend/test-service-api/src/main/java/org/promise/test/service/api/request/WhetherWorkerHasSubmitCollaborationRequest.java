package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author:黄相淇
 * @date:2022年04月02日16:20
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhetherWorkerHasSubmitCollaborationRequest implements Serializable {

    private Long testId;

    private Long workerId;

}
