package org.promise.test.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author:黄相淇
 * @date:2022年03月31日20:45
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestBatchQueryRequest implements Serializable {

    private Set<Long> testIdSet;
}
