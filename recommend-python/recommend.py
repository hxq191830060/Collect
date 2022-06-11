import report
from gensim import corpora
from gensim import models
from gensim.test.utils import get_tmpfile
from gensim.similarities import Similarity
import os
import jieba

# train the model
def generateReportModel(test_report_list, task_id):
    trl = test_report_list
    # test whether the directory exists
    if os.path.exists("./model/{}".format(task_id)):
        pass
    else:
        os.mkdir("./model/{}".format(task_id))
    # train the model (tools)
    tools_text = [item.test_tools for item in trl.reports]
    toolDict = corpora.Dictionary(tools_text)
    toolDict.save("./model/{}/toolDict".format(task_id))
    toolMatrix = [toolDict.doc2bow(text) for text in tools_text]
    corpora.MmCorpus.serialize("./model/{}/toolsMatrix".format(task_id), corpus=toolMatrix)
    tmp_tool = get_tmpfile("tmp_tool")
    toolIndex = Similarity(tmp_tool, corpus=toolMatrix, num_features=len(toolDict))
    toolIndex.save("./model/{}/toolsIndex".format(task_id))
    
    # train the model (cases)
    case_text = [[str(case.case_id) for case in item.test_list] for item in trl.reports]
    caseDict = corpora.Dictionary(case_text)
    caseDict.save("./model/{}/caseDict".format(task_id))
    caseMatrix = [caseDict.doc2bow(text) for text in case_text]
    corpora.MmCorpus.serialize("./model/{}/caseMatrix".format(task_id), corpus=caseMatrix)
    tmp_case = get_tmpfile("tmp_case")
    caseIndex = Similarity(tmp_case, corpus=caseMatrix, num_features=len(caseDict))
    caseIndex.save("./model/{}/caseIndex".format(task_id))
    
    # train the errorMsg model
    error_text = []
    for report in trl.reports:
        error_text.append(" ".join(item.error_msg for item in report.defect_list))
    stop_list = [",", ".", " ", "。","的", "我", "，","/","*", "-", "_", "在", "将", "但", "地", ""]
    cuted_text = [[word for word in jieba.cut(doc) if word not in stop_list and len(word)>1] for doc in error_text]
    errorDict = corpora.Dictionary(cuted_text)
    errorDict.save("./model/{}/errorDict".format(task_id))
    errorMatrix = [errorDict.doc2bow(text) for text in cuted_text]
    #tfidf = models.TfidfModel(corpus=errorMatrix)
    corpora.MmCorpus.serialize("./model/{}/errorMatrix".format(task_id), corpus=errorMatrix)
    tmp_error = get_tmpfile("tmp_error")
    errorIndex = Similarity(tmp_error, corpus=errorMatrix, num_features=len(errorDict))
    errorIndex.save("./model/{}/errorIndex".format(task_id))

def upDateModel(test_report_list, task_id):
    corpus = corpora.MmCorpus("./model/{}/toolsMatrix".format(task_id))
    if (len(test_report_list.reports) - len(corpus) >= 20):
        print("Update the model, before {}, after {}".format(len(corpus), len(test_report_list.reports)))
        generateReportModel(test_report_list, task_id)


# return the weight by computing the similarity in tools
def getToolWeight(testReport, task_id):
    # load the trained model
    dictionary = corpora.Dictionary.load("./model/{}/toolDict".format(task_id))
    #toolsMatrix = corpora.MmCorpus.load("toolsMatrix")
    index = Similarity.load("./model/{}/toolsIndex".format(task_id))
    tool_list = testReport.test_tools
    new_vec = dictionary.doc2bow(tool_list)
    sims = index[new_vec]
    #sims = sorted(enumerate(sims), key=lambda item : -item[1])
    return sims

# return the weight by computing the similarity in cases
def getCaseWeight(testReport, task_id):
    # load the trained model
    dictionary = corpora.Dictionary.load("./model/{}/caseDict".format(task_id))
    index = Similarity.load("./model/{}/caseIndex".format(task_id))
    case_list = [str(testItem.case_id) for testItem in  testReport.test_list]
    new_vec = dictionary.doc2bow(case_list)
    sims = index[new_vec]
    #sims = sorted(enumerate(sims), key=lambda item : -item[1])
    return sims

def getErrorWeight(testReport, task_id):
    dictionary = corpora.Dictionary.load("./model/{}/errorDict".format(task_id))
    index = Similarity.load("./model/{}/errorIndex".format(task_id))
    error_msg = "".join(item.error_msg for item in testReport.defect_list)
    stop_list = [",", ".", " ", "。","的", ]
    error_list = [word for word in jieba.cut(error_msg) if word not in stop_list]
    new_vec = dictionary.doc2bow(error_list)
    sims = index[new_vec]
    return sims

# get the model for task recommendation
def generateTaskModel(taskList):
    if not os.path.exists("./model/task"):
        os.mkdir("./model/task")
    mode_text = [[item.taskMode] for item in taskList.tasks]
    modeDict = corpora.Dictionary(mode_text)
    modeDict.save("./model/task/modeDict")
    modeMatrix = [modeDict.doc2bow(text) for text in mode_text]
    corpora.MmCorpus.serialize("./model/task/modeMatrix", corpus=modeMatrix)
    tmp_mode = get_tmpfile("tmp_model")
    modeIndex = Similarity(tmp_mode, corpus=modeMatrix, num_features=len(modeDict))
    modeIndex.save("./model/task/modeIndex")

    skill_text = [item.requireSkills for item in taskList.tasks]
    skillDict = corpora.Dictionary(skill_text)
    skillDict.save("./model/task/skillDict")
    skillMatrix = [skillDict.doc2bow(text) for text in skill_text]
    corpora.MmCorpus.serialize("./model/task/skillMatrix", corpus=skillMatrix)
    tmp_skill = get_tmpfile("tmp_skill")
    skillIndex = Similarity(tmp_skill, corpus=skillMatrix, num_features=len(skillDict))
    skillIndex.save("./model/task/skillIndex")

    env_text = [item.testEnvironments for item in taskList.tasks]
    envDict = corpora.Dictionary(env_text)
    envDict.save("./model/task/envDict")
    envMatrix = [envDict.doc2bow(text) for text in env_text]
    corpora.MmCorpus.serialize("./model/task/envMatrix", corpus=envMatrix)
    tmp_env = get_tmpfile("tmp_env")
    envIndex = Similarity(tmp_env, corpus=envMatrix, num_features=len(envDict))
    envIndex.save("./model/task/envIndex")



def getModeWeight(worker):
    dictionary = corpora.Dictionary.load("./model/task/modeDict")
    index = Similarity.load("./model/task/modeIndex")
    new_vec = dictionary.doc2bow(worker.taskPreference)
    sims = index[new_vec]
    return sims

def getSkillsWeight(worker):
    dictionary = corpora.Dictionary.load("./model/task/skillDict")
    index = Similarity.load("./model/task/skillIndex")
    new_vec = dictionary.doc2bow(worker.workerSkills)
    sims = index[new_vec]
    return sims

def getDevsWeight(worker):
    dictionary = corpora.Dictionary.load("./model/task/envDict")
    index = Similarity.load("./model/task/envIndex")
    new_vec = dictionary.doc2bow(worker.testDevs)
    sims = index[new_vec]
    return sims

if __name__ == "__main__":
    generateReportModel()
    trl = report.TestReportList.loadFromFile("./data/random.json")
    sims = getToolWeight(trl.reports[6])
    print(trl.reports[6].test_tools)
    for i, sim in sims[:5]:
        print(i, trl.reports[i].test_tools, sim)


    

