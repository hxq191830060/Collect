package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelTestRequest implements Serializable {
    private static final long serialVersionUID = -590355954320204852L;

    private Long testId;

    private Long workerId;

    private String allowCancelTime;

}
