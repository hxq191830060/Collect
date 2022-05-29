package org.promise.http.service.httpService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/3/31 19:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSimilarityDTO {

    private Long taskId;

    private Double sim;

}
