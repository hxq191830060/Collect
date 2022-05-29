package org.promise.http.service.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 21:11
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidUserInfo {
    private Long userId;

    private List<String> role;
}
