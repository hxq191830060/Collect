package org.promise.test.service.api.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 10:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationInfo implements Serializable {

    private static final long serialVersionUID = -3286540311782291539L;

    private Long id;

    private Long testId;

    private Long workerId;

    private List<String> testTools;

    private String operatingSystem;

    private String conclusion;

    private String suggestion;

    private List<String> bugImgUrl;

    private String createTime;

    private String updateTime;

    private List<CollaborationCaseInfo> collaborationCaseInfoList;
}
