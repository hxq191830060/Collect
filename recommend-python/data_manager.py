import requests
import json


class DataManager:

    def __init__(self):
        # 请求数据的接口 online
        #self.__testBaseUrl = 'http://test-service-inner-service.promise:31002/testInfo'
        #self.__userBaseUrl = 'http://user-service-inner-service.promise:31003/userInfo'
        #self.__taskBaseUrl = 'http://publish-service-inner-service.promise:31001/taskInfo'
        
        # 请求数据的接口 test
        self.__testBaseUrl = 'http://150.158.158.176:30902/testInfo'
        self.__userBaseUrl = 'http://150.158.158.176:30903/userInfo'
        self.__taskBaseUrl = 'http://150.158.158.176:30901/taskInfo'        

        self.__headers = {'content-type': 'charset=utf8'}

    def __requestTestInfoList(self, taskId):
        payload = {'taskId': taskId}
        url = self.__testBaseUrl + '/getTestInfoList'
        testInfoJson = requests.get(url=url, headers=self.__headers, params=payload).content
        testInfoList = json.loads(testInfoJson.decode('utf8'))['data']['list']
        return testInfoList

    def __requestTestInfo(self, testId):
        payload = {'testId': testId}
        url = self.__testBaseUrl + '/getTestInfoByTestId'
        testInfoJson = requests.get(url=url, headers=self.__headers, params=payload).content
        testInfoList = json.loads(testInfoJson.decode('utf8'))['data']['list']
        return testInfoList

    def __requestUserInfo(self, userId):
        payload = {'userId': userId}
        url = self.__userBaseUrl + '/getUserByUserId'
        userInfoJson = requests.get(url=url, headers=self.__headers, params=payload).content
        userInfo = json.loads(userInfoJson.decode('utf8'))['data']
        return userInfo

    def __requestTaskInfoList(self):
        url=self.__taskBaseUrl+'/getRunningTask'
        taskInfoListJson=requests.get(url=url,headers=self.__headers).content
        taskInfoList=json.loads(taskInfoListJson.decode('utf8'))['data']
        return taskInfoList

    def __renameKey(self, dictory, oldName, newName):
        dictory = dict(dictory)
        dictory[newName] = dictory[oldName]
        dictory.pop(oldName)
        return dictory

    def __dealTestInfoList(self, testInfoList):
        res = []
        for testInfo in testInfoList:
            dict = {}
            dict['report_id'] = testInfo['testId']
            dict['test_tools'] = testInfo['testTools']
            dict['operating_system'] = testInfo['operatingSystem']
            dict['test_list'] = self.__dealTestList(testInfo['testCaseList'])
            dict['defect_list'] = self.__dealDefectList(testInfo['testCaseList'])
            dict['defect_statistics'] = self.__dealDefectStatistics(testInfo)
            dict['collaboration_record'] = self.__dealCollaborationRecord(testInfo['testCollaborationRecordList'])
            dict['conclusion'] = testInfo['conclusion']
            dict['suggestion'] = testInfo['suggestion']
            res.append(dict)
        return res

    def __dealTestInfo(self, testInfo):
        dict = {}
        dict['report_id'] = testInfo['testId']
        dict['test_tools'] = testInfo['testTools']
        dict['operating_system'] = testInfo['operatingSystem']
        dict['test_list'] = self.__dealTestList(testInfo['testCaseList'])
        dict['defect_list'] = self.__dealDefectList(testInfo['testCaseList'])
        dict['defect_statistics'] = self.__dealDefectStatistics(testInfo)
        dict['collaboration_record'] = self.__dealCollaborationRecord(testInfo['testCollaborationRecordList'])
        dict['conclusion'] = testInfo['conclusion']
        dict['suggestion'] = testInfo['suggestion']
        return dict

    def __dealCollaborationRecord(self, collaborationRecord):
        res = []
        for collaboration in collaborationRecord:
            dict = {}
            dict['collaborator_id'] = collaboration['id']
            dict['evaluation'] = collaboration['evaluation']
            dict['recommendation'] = collaboration['recommendation']
            res.append(dict)
        return res

    def __dealTestList(self, testList):
        res = []
        for testCase in testList:
            dict = {}
            dict['case_id'] = testCase['caseId']
            dict['status'] = testCase['status']
            dict['running_time'] = testCase['runningTime']
            res.append(dict)
        return res

    def __dealDefectList(self, testList):
        res = []
        for testCase in testList:
            if testCase['status'] == 'failed':
                dict = {}
                dict['case_id'] = testCase['caseId']
                dict['defect_level'] = testCase['defectLevel']
                dict['error_msg'] = testCase['errorMsg']
                dict['reproduce_steps'] = testCase['reproduceSteps']
                res.append(dict)
        return res

    def __dealDefectStatistics(self, testInfo):
        dict = {
            "fatal": testInfo['fatal'],
            "severe": testInfo['severe'],
            "common": testInfo['common'],
            "slight": testInfo['slight'],
            "total": testInfo['total']
        }
        return dict

    def __dealUserInfo(self, userInfo):
        dict = {
            'user_id': userInfo['userId'],
            'workerSkills': userInfo['skills'],
            'taskPreference': userInfo['taskPreference'],
            'testDevs': userInfo['testDevs']
        }
        return dict

    def __dealTaskInfo(self,taskInfo):
        dict={
            'taskId':taskInfo['taskId'],
            'taskMode':taskInfo['testType'],
            'requireSkills':taskInfo['taskSkillList'],
            'testEnvironments':taskInfo['taskOsList']
        }
        return dict

    def selectReportByTaskId(self, taskId):
        testInfoList = self.__requestTestInfoList(taskId)
        return json.dumps(self.__dealTestInfoList(testInfoList), ensure_ascii=False)

    def selectReportByTestId(self, testId):
        testInfoList = self.__requestTestInfo(testId)
        return json.dumps(self.__dealTestInfo(testInfoList[0]), ensure_ascii=False)

    def selectUserByUserId(self, userId):
        userInfo = self.__requestUserInfo(userId)
        return json.dumps(self.__dealUserInfo(userInfo), ensure_ascii=False)

    def selectTaskList(self):
        taskInfoList=self.__requestTaskInfoList()
        res=[]
        for taskInfo in taskInfoList:
            res.append(self.__dealTaskInfo(taskInfo))
        return res
