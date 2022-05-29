package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/5/28 18:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCountResponse implements Serializable {

    private static final long serialVersionUID = -5360019657044324093L;

    private Integer count;

}
