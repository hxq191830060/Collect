package org.promise.http.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.page.Page;
import org.promise.http.service.httpService.PublishHttpService;
import org.promise.http.service.util.CommonCheckUtil;
import org.promise.http.service.util.ValidUserThreadLocalUtil;
import org.promise.http.service.vo.publish.TaskFileVO;
import org.promise.http.service.vo.publish.TaskListVO;
import org.promise.http.service.vo.publish.TaskVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/2/27 12:58
 */

@Slf4j
@RestController
@RequestMapping("/task")
public class PublishController {

    @Resource
    private PublishHttpService publishHttpService;

    @Resource
    private CommonCheckUtil commonCheckUtil;

    @GetMapping("test")
    public String test() {
        log.info("进入test方法");
        return "hello world";
    }

    @RequestMapping(value = "option", method = RequestMethod.OPTIONS)
    public String option() {
        log.info("测试option");
        return "option";
    }

    @GetMapping("/token")
    public String token() {
        log.info("测试token");
        return "token";
    }

    @GetMapping("/time")
    public String time(){
        log.info("测试当前时间{}", LocalDateTime.now());
        return "time";
    }


    @PostMapping("/publish")
    public HttpResult<String> publish(@RequestBody TaskVO taskVO) {
        commonCheckUtil.checkString(taskVO.getName());
        if (taskVO.getPublisherId() == null) {
            taskVO.setPublisherId(ValidUserThreadLocalUtil.get().getUserId());
        }
        commonCheckUtil.checkLong(taskVO.getPublisherId());
        commonCheckUtil.checkString(taskVO.getEndTime());
        commonCheckUtil.checkInteger(taskVO.getTotalWorker());
        commonCheckUtil.checkString(taskVO.getMode());
        if(taskVO.getStartTime()==null){
            taskVO.setStartTime(taskVO.getEndTime());
        }
        return publishHttpService.publish(taskVO);
    }

    @PostMapping("/update")
    public HttpResult<String> updateTask(@RequestBody TaskVO taskVO) {
        commonCheckUtil.checkLong(taskVO.getTaskId());

        return publishHttpService.updateTask(taskVO);
    }

    @PostMapping("/publishFile")
    public HttpResult<String> publishFile(@RequestBody TaskFileVO taskFileVO) {
        commonCheckUtil.checkLong(taskFileVO.getTaskId());
        commonCheckUtil.checkString(taskFileVO.getName());
        commonCheckUtil.checkString(taskFileVO.getSize());
        commonCheckUtil.checkString(taskFileVO.getUrl());
        commonCheckUtil.checkString(taskFileVO.getUploadTime());
        commonCheckUtil.checkInteger(taskFileVO.getFileType());
        log.info ("PublishController.publishDoc: 参数为: {}",taskFileVO);
        return publishHttpService.publishFile(taskFileVO);
    }

    @GetMapping("/getTasksByPublisherId")
    public HttpResult<TaskListVO> getTaskListByPublisherId(@RequestParam(required = false) Long publisherId,
                                                           @RequestParam Integer judgeType,
                                                           @RequestParam Integer pageNum,
                                                           @RequestParam Integer pageSize) {
        if (publisherId == null) {
            publisherId = ValidUserThreadLocalUtil.get().getUserId();
        }
        commonCheckUtil.checkLong(publisherId);
        commonCheckUtil.checkInteger(pageNum);
        commonCheckUtil.checkInteger(pageSize);
        Page page=new Page(pageNum,pageSize);

        return publishHttpService.getTaskListByPublisherId(publisherId,judgeType,page);
    }

    @GetMapping("/getTaskById")
    public HttpResult<TaskVO> getTaskById(@RequestParam Long taskId) {
        commonCheckUtil.checkLong(taskId);

        return publishHttpService.getTaskById(taskId);
    }

    @GetMapping("/getRunningTasks")
    public HttpResult<TaskListVO> getRunningTasks(@RequestParam Integer pageNum,
                                                  @RequestParam Integer pageSize) {
        commonCheckUtil.checkInteger(pageNum);
        commonCheckUtil.checkInteger(pageSize);
        Page page=new Page(pageNum,pageSize);

        return publishHttpService.getRunningTasks(page);
    }


    @GetMapping("/getAllTasks")
    public HttpResult<TaskListVO> getAllTasks(@RequestParam Integer pageNum,
                                                @RequestParam Integer pageSize) {
        commonCheckUtil.checkInteger(pageNum);
        commonCheckUtil.checkInteger(pageSize);
        Page page=new Page(pageNum,pageSize);
        return publishHttpService.getAllTasks(page);
    }

    @GetMapping("/getTaskStateByTaskId")
    public HttpResult<Boolean> getTaskStateByTaskId(@RequestParam Long taskId){
        commonCheckUtil.checkLong(taskId);
        return publishHttpService.getTaskStateByTaskId(taskId);
    }

    @GetMapping("/getTasksByWorkerId")
    public HttpResult<TaskListVO> getTasksByWorkerId(@RequestParam(required = false) Long workerId,
                                                           @RequestParam Integer judgeType,
                                                           @RequestParam Integer pageNum,
                                                           @RequestParam Integer pageSize){
        if(workerId==null){
            workerId=ValidUserThreadLocalUtil.get().getUserId();
        }
        return publishHttpService.getTaskListByWorkerId(workerId,judgeType,new Page(pageNum,pageSize));
    }
}
