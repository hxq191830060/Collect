package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/4/2 10:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaborationIdRequest implements Serializable {

    private static final long serialVersionUID = -5138636980087179126L;

    private Long id;
}
