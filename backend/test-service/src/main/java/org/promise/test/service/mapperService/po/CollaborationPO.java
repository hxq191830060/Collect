package org.promise.test.service.mapperService.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/4/2 0:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaborationPO {

    private Long id;

    private Long testId;

    private Long workerId;

    private String testTools;

    private String operatingSystem;

    private String conclusion;

    private String suggestion;

    private String bugImgUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
