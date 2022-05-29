import json
from collections import defaultdict
import numpy as np
from data_manager import DataManager
from MyEncoder import MyEncoder

class RandomDataGenerator:
    reportCount = 0
    tool_list = [
        "TestRail", "Xray", "Practitest", "Testpad", "Zephyr Scale", "Spira Test", "Testiny", "TestMonitor", "Avo Assure", "Kobiton",
        "Parasoft", "Zaptest", "ACCELQ", "Keysight Eggplant", "LambdaTest", "Selenium", "QTP", "Watir", "Testim", "AppliTools",
        "TestComplete", "Browsera", "SauceLabs", "Ghostlab", "Webload", "Loadrunner", "Wapt", "Jmeter", "BlazeMeter", "JIRA", "FogBugz",
        "Bugzilla", "BugNet", "Pachno", "RedMine", "Appuim", "Espresso", "Perfecto", "Robotium", "SoapUI", "Postman"
    ]
    os_list = [
        "Windows10", "Windows11", "CentOS7.6", "CentOS8.0", "CentOS8.2", "Ubuntu18.04", "Ubuntu20.04", "Debian10.2", "Debian11.1"
    ]

    @classmethod
    # return a report id(incrementaly)
    def getReportId(self):
        self.reportCount += 1
        return "report_{}".format(self.reportCount)

    @classmethod
    # return a tool list (1-5 tools)
    def getToolList(self):
        listlen = np.random.randint(1, 5)
        tl = []
        for i in range(0, listlen):
            index = np.random.randint(0, len(self.tool_list))
            while self.tool_list[index] in tl:
                index = np.random.randint(0, len(self.tool_list))
            tl.append(self.tool_list[index])
        return tl
    
    @classmethod
    # return an os
    def getOS(self):
        index = np.random.randint(0, len(self.os_list))
        return self.os_list[index]

    @classmethod
    # return a random case_id
    def getCaseId(self):
        id = np.random.randint(1, 21)
        return "Case_{}".format(id)

    @classmethod
    # return a random status
    def getStatus(self):
        p = np.random.rand()
        if p > 0.7:
            return "Fail"
        else:
            return "Success"

    @classmethod
    # return a list of testItem
    # test five case right know
    def getTestList(self):
        tl = []
        case_list = []
        for i in range(0, 5):
            ti = TestItem.random()
            while ti.case_id in case_list:
                ti = TestItem.random()
            tl.append(ti)
            case_list.append(ti.case_id)
        return tl

    @classmethod
    # return an multinomial distribution
    def getDefectStatistics(self, testList):
        count = 0
        for i in testList:
            if i.status == "Fail":
                count += 1
        return np.random.multinomial(count, [0.25, 0.25, 0.25, 0.25])
    
    @classmethod
    # return a defect list
    def getDefectList(cls, testList):
        fail_list = []
        for testItem in testList:
            if testItem.status == "Fail":
                fail_list.append(testItem)

        dl = [DefectItem.random(i.case_id) for i in fail_list]

        return dl
    
    @classmethod
    # return a conclusion
    def getConclusion(cls):
        return "this is conclusion"

    @classmethod
    # return a suggestion
    def getSuggestion(cls):
        return "this is suggestion"

    @classmethod
    # return a defectlevel
    def getDefectLevel(cls):
        levels = ["fatal", "severe", "common", "slight"]
        index = np.random.randint(0, 4)
        return levels[index]

    @classmethod
    # return a error msg
    def getErrorMsg(cls):
        file = open("./data/error.txt", "r", encoding="utf-8")
        error_msg_list = file.readlines()
        index = np.random.randint(0, len(error_msg_list))
        return error_msg_list[index].strip().replace("，", ",").replace("\t", " ")


class TestItem:
    case_id = ""
    status = ""
    running_time = ""
    def __init__(self):
        pass
    @classmethod
    def setFromFile(cls, testItem):
        ti = TestItem()
        ti.case_id = testItem['case_id']
        ti.status  = testItem['status']
        # comment temp
        #ti.running_time=testItem['running_time']
        return ti
    @classmethod
    def random(cls):
        a = TestItem()
        a.case_id = RandomDataGenerator.getCaseId()
        a.status =  RandomDataGenerator.getStatus()
        return a

class DefectItem:
    case_id = ""
    defect_level = ""
    error_msg = ""
    reproduce_steps = ""
    def __init__(self):
        pass
    @classmethod
    def setFromFile(cls, defectItem):
        di = DefectItem()
        di.case_id = defectItem['case_id']
        di.defect_level=defectItem['defect_level']
        di.error_msg=defectItem['error_msg']
        di.reproduce_steps=defectItem['reproduce_steps']
        return di
    @classmethod
    def random(cls, case_id):
        defectItem = DefectItem()
        defectItem.case_id = case_id
        defectItem.defect_level = RandomDataGenerator.getDefectLevel()
        defectItem.error_msg = RandomDataGenerator.getErrorMsg()
        defectItem.reproduce_steps = "reproduce_steps"
        return defectItem
    
class TestReport:
    report_id = ""
    test_tools = []
    operating_system = ""
    test_list = []
    defect_statistics = defaultdict(str)
    defect_list = []
    conclusion = ""
    suggestion = ""
    def __init__(self):
        pass

    @classmethod
    def setFromFile(cls, json_data):
        tr = TestReport()
        tr.report_id = json_data['report_id']
        tr.test_tools = json_data['test_tools']
        tr.operating_system = json_data['operating_system']
        tr.test_list = [TestItem.setFromFile(i) for i in json_data['test_list']]
        tr.defect_statistics = {
            "fatal": json_data['defect_statistics']['fatal'],
            "severe":json_data['defect_statistics']['severe'],
            "common":json_data['defect_statistics']['common'],
            "slight":json_data['defect_statistics']['slight'],
            "total": json_data['defect_statistics']['total']
        }
        tr.defect_list = [DefectItem.setFromFile(i) for i in json_data['defect_list']]
        tr.conclusion = json_data['conclusion']
        tr.suggestion = json_data['suggestion']
        return tr
    
    @classmethod
    def loadFromDataBase(cls, report_id):
        report = json.loads(DataManager().selectReportByTestId(report_id))
        testReport = cls.setFromFile(report)
        return testReport

    @classmethod
    def random(cls):
        tr = TestReport()
        tr.report_id =  RandomDataGenerator.getReportId()
        tr.test_tools = RandomDataGenerator.getToolList()
        tr.operating_system = RandomDataGenerator.getOS()
        tr.test_list = RandomDataGenerator.getTestList()
        statistics = RandomDataGenerator.getDefectStatistics(tr.test_list)
        tr.defect_statistics = {
            "fatal":  statistics[0],
            "severe": statistics[1],
            "common": statistics[2],
            "slight": statistics[3],
            "total":  statistics.sum() 
        }
        tr.defect_list = RandomDataGenerator.getDefectList(tr.test_list)
        tr.conclusion = RandomDataGenerator.getConclusion()
        tr.suggestion = RandomDataGenerator.getSuggestion()
        return tr

    def toJSON(self):
        return json.dumps(self, cls=MyEncoder, ensure_ascii=False, indent=4)


class TestReportList:
    reports = []
    def __init__(self):
        pass
    
    @classmethod
    def loadFromFile(cls, fp):
        trl = TestReportList()
        file = open(fp, "r", encoding="utf-8")
        data = json.load(file)
        trl.reports = [TestReport.setFromFile(i) for i in data["reports"]]
        file.close()
        return trl
    
    
    def save(self, fp):
        file = open(fp, "w", encoding="utf-8")
        json.dump(self, file, cls=MyEncoder, ensure_ascii=False, indent=4)
        file.close()
    
    @classmethod
    def loadFromDatabase(cls, task_id):
        test_report_list = TestReportList()
        reports_list = json.loads(DataManager().selectReportByTaskId(task_id))
        test_report_list.reports = [TestReport.setFromFile(report) for report in reports_list]
        return test_report_list

    @classmethod
    # assume generate 100 reports
    def random(cls, num):
        trl = TestReportList()
        # 这里如果append进去，就无法打印出__dict__,很奇怪
        trl.reports = [TestReport.random() for i in range(0, num)]
        return trl

    def toJSON(self):
        return json.dumps(self, cls=MyEncoder, ensure_ascii=False, indent=4)



        