package org.promise.user.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.common.result.page.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 15:10
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchGetUserInfoRequest implements Serializable {
    private static final long serialVersionUID = 5026426992093819660L;

    private Set<Long> userIdSet;

}
