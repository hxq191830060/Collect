package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.CollaborationInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/4/2 10:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitCollaborationRequest implements Serializable {

    private static final long serialVersionUID = 8370764920400209997L;

    private CollaborationInfo collaborationInfo;
}
