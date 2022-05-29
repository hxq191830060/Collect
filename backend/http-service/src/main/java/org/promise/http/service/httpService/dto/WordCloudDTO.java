package org.promise.http.service.httpService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author moqi
 * @date 2022/5/25 20:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordCloudDTO {

    private Integer count;

    private String word;
}
