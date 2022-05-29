package org.promise.http.service.controller;

import org.promise.common.result.http.HttpResult;
import org.promise.common.util.JsonUtil;
import org.promise.http.service.httpService.RecommendHttpService;
import org.promise.http.service.httpService.dto.TaskSimilarityDTO;
import org.promise.http.service.httpService.dto.TestSimilarityDTO;
import org.promise.http.service.util.CommonCheckUtil;
import org.promise.http.service.util.ValidUserThreadLocalUtil;
import org.promise.http.service.vo.publish.TaskSimilarityVO;
import org.promise.http.service.vo.publish.TaskVO;
import org.promise.http.service.vo.test.SimpleTestVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/31 21:51
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Resource
    private RecommendHttpService recommendHttpService;

    @Resource
    private CommonCheckUtil commonCheckUtil;

    @GetMapping("/test1")
    public String test1() {
        List<TaskSimilarityDTO> taskSimilarityDTOList = new ArrayList<>();
        taskSimilarityDTOList.add(new TaskSimilarityDTO(77L, 0.5));
        taskSimilarityDTOList.add(new TaskSimilarityDTO(78L, 0.66));
        taskSimilarityDTOList.add(new TaskSimilarityDTO(79L,0.3));
        return JsonUtil.toJson(taskSimilarityDTOList);
    }

    @GetMapping("/test2")
    public String test2(){
        List<TestSimilarityDTO> testSimilarityDTOList=new ArrayList<>();
        testSimilarityDTOList.add(new TestSimilarityDTO(1L,0.3));
        testSimilarityDTOList.add(new TestSimilarityDTO(2L,0.4));
        testSimilarityDTOList.add(new TestSimilarityDTO(3L,0.6));
        testSimilarityDTOList.add(new TestSimilarityDTO(7L,0.7));
        return JsonUtil.toJson(testSimilarityDTOList);
    }



    @GetMapping("/recommendTask")
    public HttpResult<List<TaskSimilarityVO>> recommendTask(@RequestParam(required = false) Long userId) throws Exception {
        if (userId == null) {
            userId = ValidUserThreadLocalUtil.get().getUserId();
        }
        return recommendHttpService.recommendTask(userId);
    }

    @GetMapping("/recommendTest")
    public HttpResult<List<SimpleTestVO>> recommendTest(@RequestParam Long testId,
                                                        @RequestParam Long taskId) throws Exception {
        commonCheckUtil.checkLong(testId);
        commonCheckUtil.checkLong(taskId);
        return recommendHttpService.recommendTest(testId,taskId);
    }
}
