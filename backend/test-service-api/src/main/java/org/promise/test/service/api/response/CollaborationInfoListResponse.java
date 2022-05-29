package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.CollaborationInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 10:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaborationInfoListResponse implements Serializable {

    private static final long serialVersionUID = 1319754243048958634L;

    private List<CollaborationInfo> collaborationInfoList;
}
