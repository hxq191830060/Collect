package org.promise.test.service.manager;

public interface TestCountManager {

    /**
     * @param taskId
     * @return 获取接受了指定任务的工人数目
     */
    Integer getWorkerNumberByTaskId(Long taskId);
}
