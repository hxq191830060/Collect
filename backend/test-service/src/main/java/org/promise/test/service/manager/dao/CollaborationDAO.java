package org.promise.test.service.manager.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 0:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationDAO {

    private Long id;

    private Long testId;

    private Long workerId;

    private List<String> testTools;

    private String operatingSystem;

    private String conclusion;

    private String suggestion;

    private List<String> bugImgUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
