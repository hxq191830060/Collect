package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.CollaborationInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/4/2 10:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationInfoResponse implements Serializable {

    private static final long serialVersionUID = 145290401816877731L;

    //如果为null，那么就是没查到数据
    private CollaborationInfo collaborationInfo;
}
