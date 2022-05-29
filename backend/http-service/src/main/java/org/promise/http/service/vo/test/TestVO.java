package org.promise.http.service.vo.test;

import lombok.Data;
import org.promise.http.service.convert.TestCollaborationRecordConvert;
import org.promise.test.service.api.info.TestCaseInfo;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 0:00
 */
@Data
public class TestVO implements Serializable {

    private static final long serialVersionUID = -2800759649423200275L;

    private Long testId;

    private Long taskId;

    private Long workerId;

    private String state;

    private String acceptTime;

    private String updateTime; //最后一次更新时间

    private String finishTime; //完成测试的时间

    private String cancelTime; //取消测试的时间

    private List<String> testTools=new ArrayList<>();//测试使用的工具

    private String testEnvironment;//测试使用的操作系统

    private String conclusion;//总结

    private String suggestion;//建议

    private List<TestCollaborationRecordVO> testCollaborationRecordList=new ArrayList<> ();

    private List<TestCaseVO> testCases =new ArrayList<>();

    private List<String> screenshots =new ArrayList<>();

    private List<CollaborationVO> collaborationList=new ArrayList<>();

    private Integer fatal=0;

    private Integer severe=0;

    private Integer common=0;

    private Integer slight=0;

    private Integer total=0;

    private String nickname;

    private String avatar;

}
