package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.FinishedTestInfo;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishedTestInfoResponse implements Serializable {
    private static final long serialVersionUID = 6759088994673816860L;

    List<FinishedTestInfo> highFinishedTestInfoList;
}
