package org.promise.http.service.vo.publish;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.common.result.page.Page;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/25 21:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListVO {

    private List<TaskVO> taskVOList;

    private Page page;

    public TaskListVO(List<TaskVO> taskVOList){
        this.taskVOList=taskVOList;
    }

}
