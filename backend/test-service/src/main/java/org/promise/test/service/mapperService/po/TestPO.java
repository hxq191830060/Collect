package org.promise.test.service.mapperService.po;

import lombok.Data;
import org.promise.test.service.mapperService.constant.TestStateEnum;

import java.time.LocalDateTime;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 15:47
 * @description:
 */
@Data
public class TestPO {

    private Long testId;//主键

    private Long taskId;//任务id,not null

    private Long workerId;//接受该任务的测试人员的workerId,not null

    private TestStateEnum state;//当前测试的状态,not null,插入数据库时为默认值running

    private LocalDateTime acceptTime;//测试接受时间,不用管，插入数据库时有默认值

    private LocalDateTime finishTime;//完成时间,status为finish时才有该字段

    private LocalDateTime cancelTime;//取消时间,status为cancel时才有该字段

    private String testTools;//使用的测试工具

    private String operatingSystem;//测试的操作系统

    private String conclusion;//测试结论

    private String suggestion;//测试建议

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
