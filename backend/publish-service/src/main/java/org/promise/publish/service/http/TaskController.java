package org.promise.publish.service.http;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.common.annotation.RecordLog;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.page.Page;
import org.promise.common.util.HttpResultUtil;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.convert.TaskConvert;
import org.promise.publish.service.manager.PublishManager;
import org.promise.publish.service.manager.dao.TaskDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/31 15:18
 */

@RestController
@RequestMapping("/taskInfo")
public class TaskController {

    @Resource
    private PublishManager publishManager;

    @Resource
    private TaskConvert taskConvert;

    @GetMapping("/getRunningTask")
    @RecordLog
    public HttpResult<List<TaskInfo>> getRunningTask(){
        List<TaskDAO> runningTasksDAO = publishManager.getTasksRunning(new Page(1, Integer.MAX_VALUE));
        if(CollectionUtils.isEmpty(runningTasksDAO)){
            return HttpResultUtil.fail("未查询到相关任务信息");
        }
        List<TaskInfo> taskInfoList=taskConvert.convertDAOList2InfoList(runningTasksDAO);
        return HttpResultUtil.success(taskInfoList);
    }

}
