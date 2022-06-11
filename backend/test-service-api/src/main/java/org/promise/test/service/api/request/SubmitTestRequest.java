package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.TestBugImgInfo;
import org.promise.test.service.api.info.TestCaseInfo;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitTestRequest implements Serializable {
    private static final long serialVersionUID = -8384369554711764619L;

    private Long testId;

    private Long workerId;

    private String operatingSystem;

    private List<String> testTools;

    private List<TestBugImgInfo> bugImgList;

    private List<TestCaseInfo> testCaseList;

    private String conclusion;//总结

    private String suggestion;//建议

    private String lastSubmitTime;

}
