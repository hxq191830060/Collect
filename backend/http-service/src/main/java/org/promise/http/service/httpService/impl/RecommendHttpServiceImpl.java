package org.promise.http.service.httpService.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.common.util.JsonUtil;
import org.promise.http.service.convert.TaskConvert;
import org.promise.http.service.httpService.RecommendHttpService;
import org.promise.http.service.httpService.dto.TaskSimilarityDTO;
import org.promise.http.service.httpService.dto.TestSimilarityDTO;
import org.promise.http.service.util.HttpRequestUtil;
import org.promise.http.service.vo.publish.TaskSimilarityVO;
import org.promise.http.service.vo.test.SimpleTestVO;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.TaskIdsRequest;
import org.promise.publish.service.api.response.TaskMapResponse;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.FinishedTestInfo;
import org.promise.test.service.api.request.TestBatchQueryRequest;
import org.promise.test.service.api.response.FinishedTestInfoMapResponse;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author moqi
 * @date 2022/3/31 19:19
 */
@Slf4j
@Service
public class RecommendHttpServiceImpl implements RecommendHttpService {

    private static final int RECOMMEND_COUNT=4;

    private static final int ALTERNATIVE_COUNT=12;

    @Resource
    private HttpRequestUtil httpRequestUtil;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private PublishService publishService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private UserService userService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private TestService testService;

    @Resource
    private TaskConvert taskConvert;

    /**
     * ??????testId??????????????????test???????????????test
     * @param testId
     * @return
     */
    @Override
    public HttpResult<List<SimpleTestVO>> recommendTest(Long testId,Long taskId) throws Exception{
        String responseBody=httpRequestUtil.requestTest(testId,taskId);
        List<TestSimilarityDTO> testSimilarityDTOList= JsonUtil.fromJsonToList(responseBody,TestSimilarityDTO.class);
        testSimilarityDTOList.sort((o1,o2)-> o2.getSim().compareTo(o1.getSim()));
        //????????????????????????????????????????????????
        int actualSize=testSimilarityDTOList.size()-1;
        if(actualSize<=0){
            return HttpResultUtil.success(Collections.emptyList());
        }
        int actualAlternativeCount=Math.min(ALTERNATIVE_COUNT,actualSize/2+1);
        List<TestSimilarityDTO> actualAlternativeList=testSimilarityDTOList.subList(1,1+actualAlternativeCount);

        //???????????????????????????????????????
        List<TestSimilarityDTO> testSimilarityResultList=getRandomFromList(actualAlternativeList,RECOMMEND_COUNT);

        Set<Long> testIds=new HashSet<>();
        for (TestSimilarityDTO testSimilarityDTO : testSimilarityResultList) {
            testIds.add(testSimilarityDTO.getReportId());
        }
        TestBatchQueryRequest testBatchQueryRequest=new TestBatchQueryRequest(testIds);
        RpcResult<FinishedTestInfoMapResponse> simpleFinishedTestMapRpcResult =
                testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest);
        if(simpleFinishedTestMapRpcResult==null){
            return HttpResultUtil.fail(simpleFinishedTestMapRpcResult.getMessage());
        }
        Map<Long, FinishedTestInfo> testInfoMap=simpleFinishedTestMapRpcResult.getData().getMap();

        Map<Long,UserInfo> userInfoMap=getUserInfoMapByTestInfoList(new ArrayList<>(testInfoMap.values()));

        List<SimpleTestVO> res=new ArrayList<>();
        for (TestSimilarityDTO testSimilarityDTO : testSimilarityResultList) {
            FinishedTestInfo finishedTestInfo=testInfoMap.get(testSimilarityDTO.getReportId());
            UserInfo userInfo=userInfoMap.get(finishedTestInfo.getWorkerId());
            SimpleTestVO simpleTestVO=new SimpleTestVO(testSimilarityDTO,finishedTestInfo,userInfo);
            res.add(simpleTestVO);
        }
        return HttpResultUtil.success(res);
    }

    /**
     * ???????????????????????????????????????
     * @param userId
     * @return
     */
    @Override
    public HttpResult<List<TaskSimilarityVO>> recommendTask(Long userId) throws Exception{
        //??????python?????????????????????
        String responseBody=httpRequestUtil.requestTask(userId);
        log.info("???python????????????json???{}",responseBody);
        List<TaskSimilarityDTO> taskSimilarityDTOList=JsonUtil.fromJsonToList(responseBody,TaskSimilarityDTO.class);
        if(taskSimilarityDTOList.size()<=0){
            return HttpResultUtil.success(Collections.emptyList());
        }
        taskSimilarityDTOList.sort((o1,o2)-> o2.getSim().compareTo(o1.getSim()));

        //??????????????????????????????
        int actualAlternativeCount=Math.min(ALTERNATIVE_COUNT,taskSimilarityDTOList.size()/2+1);
        List<TaskSimilarityDTO> actualAlternativeList=taskSimilarityDTOList.subList(0,actualAlternativeCount);

        List<TaskSimilarityDTO> taskSimilarityResultList =getRandomFromList(actualAlternativeList,RECOMMEND_COUNT);

        //????????????????????????????????????
        List<Long> taskIds=new ArrayList<>();
        for (TaskSimilarityDTO taskSimilarityDTO : taskSimilarityResultList) {
            taskIds.add(taskSimilarityDTO.getTaskId());
        }
        RpcResult<TaskMapResponse> taskMapRpcResult = publishService.getTaskMapByTaskIds(new TaskIdsRequest(taskIds, 0));
        if(taskMapRpcResult.getData()==null){
            return HttpResultUtil.fail(taskMapRpcResult.getMessage());
        }
        Map<Long,TaskInfo> taskInfoMap=taskMapRpcResult.getData().getTaskInfoMap();

        //?????????????????????
        Map<Long,UserInfo> userInfoMap=getUserInfoMapByTaskInfoList(new ArrayList<>(taskInfoMap.values()));

        //??????????????????
        List<TaskSimilarityVO> res=new ArrayList<>();
        for (TaskSimilarityDTO taskSimilarity : taskSimilarityResultList) {
            TaskInfo taskInfo=taskInfoMap.get(taskSimilarity.getTaskId());
            UserInfo userInfo=userInfoMap.get(taskInfo.getPublisherId());
            res.add(new TaskSimilarityVO(taskSimilarity,taskInfo,userInfo));
        }

        return HttpResultUtil.success(res);
    }

    private Map<Long, UserInfo> getUserInfoMapByTestInfoList(List<FinishedTestInfo> taskInfoList) {
        Set<Long> userIds = new HashSet<>();
        for (FinishedTestInfo testInfo : taskInfoList) {
            userIds.add(testInfo.getWorkerId());
        }
        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        userInfosRequest.setUserIdSet(userIds);

        //?????????????????????????????????????????????????????????????????????
        RpcResult<BatchGetUserInfoResponse> usersRpcResult = userService.batchGetUserInfoByUserId(userInfosRequest);
        if (usersRpcResult.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", usersRpcResult.getMessage());
            return Collections.emptyMap();
        }
        return usersRpcResult.getData().getUserInfos();
    }

    private Map<Long, UserInfo> getUserInfoMapByTaskInfoList(List<TaskInfo> taskInfoList) {
        Set<Long> userIds = new HashSet<>();
        for (TaskInfo taskInfo : taskInfoList) {
            userIds.add(taskInfo.getPublisherId());
        }
        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        userInfosRequest.setUserIdSet(userIds);

        //?????????????????????????????????????????????????????????????????????
        RpcResult<BatchGetUserInfoResponse> usersRpcResult = userService.batchGetUserInfoByUserId(userInfosRequest);
        if (usersRpcResult.getData() == null) {
            log.error("rpc??????????????????,???????????????{}", usersRpcResult.getMessage());
            return Collections.emptyMap();
        }
        return usersRpcResult.getData().getUserInfos();
    }


    private <T> List<T> getRandomFromList(List<T> list, int count) {
        List<T> olist = new ArrayList<>();
        if (list.size() <= count) {
            return list;
        } else {
            Random random = new Random();
            for (int i = 0 ;i<count;i++){
                int intRandom = random.nextInt(list.size() - 1);
                olist.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return olist;
        }
    }

}
