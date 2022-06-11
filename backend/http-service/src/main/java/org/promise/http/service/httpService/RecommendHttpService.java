package org.promise.http.service.httpService;

import org.promise.common.result.http.HttpResult;
import org.promise.http.service.vo.publish.TaskSimilarityVO;
import org.promise.http.service.vo.publish.TaskVO;
import org.promise.http.service.vo.test.SimpleTestVO;

import java.net.URISyntaxException;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/31 19:19
 */
public interface RecommendHttpService {


    /**
     * 根据testId推荐其他和该test相似的其他test
     * @param testId
     * @return
     */
    HttpResult<List<SimpleTestVO>> recommendTest(Long testId,Long taskId) throws Exception;

    /**
     * 给一个用户推荐适合他的任务
     * @param userId
     * @return
     */
    HttpResult<List<TaskSimilarityVO>> recommendTask(Long userId) throws Exception;
}
