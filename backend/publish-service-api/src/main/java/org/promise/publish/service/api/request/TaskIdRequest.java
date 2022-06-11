package org.promise.publish.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/2/27 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskIdRequest implements Serializable {

    private static final long serialVersionUID = -3179938436139404674L;

    private Long taskId;

}
