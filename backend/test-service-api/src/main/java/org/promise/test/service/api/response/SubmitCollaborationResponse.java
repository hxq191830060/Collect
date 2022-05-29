package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/4/2 10:14
 */
@Data
public class SubmitCollaborationResponse implements Serializable {

    private static final long serialVersionUID = 7885118927431374952L;

    //报告创建/修改后的主键id
    private Long id;
}
