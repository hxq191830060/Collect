package org.promise.http.service.vo.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.CollaborationInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 10:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationVO {

    private Long id;

    private Long testId;

    private Long workerId;

    private List<String> testTools;

    private String testEnvironment;

    private String conclusion;

    private String suggestion;

    private List<String> screenshots;

    private List<CollaborationCaseVO> testCases;

    private String createTime;

    private String updateTime;

    private String nickname;

    private String avatar;
}
