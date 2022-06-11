package org.promise.test.service.api.info;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class TestInfo implements Serializable {

    private static final long serialVersionUID = 5556358828133787167L;

    private Long testId;

    private Long taskId;

    private Long workerId;

    private String state;//测试的状态

    private String acceptTime;//接受测试的时间

    private String updateTime;//最后一次更新时间

    private String finishTime;//完成测试的时间

    private String cancelTime;//取消测试的时间

    private List<String> testTools;//测试使用的工具

    private String operatingSystem;//测试使用的操作系统

    private String conclusion;//总结

    private String suggestion;//建议

    private List<TestCollaborationRecordInfo> testCollaborationRecordList=new ArrayList<> ();

    private List<TestCaseInfo> testCaseList;

    private List<TestBugImgInfo> bugImgList;

    private Integer fatal=0;

    private Integer severe=0;

    private Integer common=0;

    private Integer slight=0;

    private Integer total=0;

    public void increaseFatal(){
        this.fatal+=1;
        this.total+=1;
    }

    public void increaseSevere(){
        this.severe+=1;
        this.total+=1;
    }

    public void increaseCommon(){
        this.common+=1;
        this.total+=1;
    }

    public void increaseSlight(){
        this.slight+=1;
        this.total+=1;
    }


}
