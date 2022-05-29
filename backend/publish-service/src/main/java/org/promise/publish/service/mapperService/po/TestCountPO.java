package org.promise.publish.service.mapperService.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/3/9 16:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCountPO {

    private Long id;

    private Long taskId;

    private Integer testNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public TestCountPO(Long taskId){
        this.taskId=taskId;
    }
}
