package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitCollaborationRecordRequest implements Serializable {
    private static final long serialVersionUID = -4946629835208014027L;

    //评价信息
    private TestCollaborationRecordInfo testCollaborationRecordInfo;


}
