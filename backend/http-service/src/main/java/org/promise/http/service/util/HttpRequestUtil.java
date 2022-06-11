package org.promise.http.service.util;

import lombok.Data;
import org.promise.common.util.HttpClientUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author moqi
 * @date 2022/3/31 19:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "python")
public class HttpRequestUtil {

    private String taskRecommendUrl;

    private String testRecommendUrl;

    private String reportClusterUrl;

    private String reportClassifyUrl;

    private String wordCloudErrUrl;

    private String wordCloudSugUrl;

    private String wordCloudCluUrl;

    private String getClassifySimUrl;

    public String requestTask(Long userId) throws Exception {
        HashMap<String,Object> param=new HashMap<>();
        param.put("userId",userId);
        return HttpClientUtil.doGet(taskRecommendUrl,param);
    }

    public String requestTest(Long testId,Long taskId) throws Exception {
        HashMap<String,Object> param=new HashMap<>();
        param.put("reportId",testId);
        param.put("taskId",taskId);
        return HttpClientUtil.doGet(testRecommendUrl,param);
    }

    public String clusteringReport(Long taskId,int k) throws Exception{
        HashMap<String,Object> param=new HashMap<>();
        param.put("taskId",taskId);
        param.put("k",k);
        return HttpClientUtil.doGet(reportClusterUrl,param);
    }

    public String classifyReport(Long taskId) throws Exception{
        HashMap<String,Object> param=new HashMap<>();
        param.put("taskId",taskId);
        return HttpClientUtil.doGet(reportClassifyUrl,param);
    }

    public String getWordCloudErr(Long taskId) throws Exception{
        HashMap<String,Object> param=new HashMap<>();
        param.put("taskId",taskId);
        return HttpClientUtil.doGet(wordCloudErrUrl,param);
    }

    public String getWordCloudSug(Long taskId) throws Exception{
        HashMap<String,Object> param=new HashMap<>();
        param.put("taskId",taskId);
        return HttpClientUtil.doGet(wordCloudSugUrl,param);
    }

    public String getWordCloudClu(Long taskId) throws Exception{
        HashMap<String,Object> param=new HashMap<>();
        param.put("taskId",taskId);
        return HttpClientUtil.doGet(wordCloudCluUrl,param);
    }

    public String getClassifySim(Long taskId) throws Exception{
        HashMap<String,Object> param=new HashMap<>();
        param.put("taskId",taskId);
        return HttpClientUtil.doGet(getClassifySimUrl,param);
    }

}
