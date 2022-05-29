from flask import Flask, request, jsonify

from report import TestReportList, TestReport
from worker import Worker
from task import TaskList
from recommend import generateReportModel, generateTaskModel, getDevsWeight, getErrorWeight, getModeWeight, getSkillsWeight, getToolWeight, getCaseWeight, upDateModel
from cluster import caculate_cluster, to_case_vector, to_tool_vector, merge_vector, normalize_vector
from wordCloud import get_error_wordcloud, get_suggestion_wordcloud, get_conclusion_wordcloud
from classification import classify_the_error, get_classification_similarity

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

@app.route("/recommend/report", methods=["GET"])
# 根据报告id推荐同任务下的已提交报告
def get_report_recommend():
    # 先查询同任务下报告个数
    # 如果报告个数小于20，则不进行推荐
    # todo
    task_id = request.args['taskId']
    test_report_list = TestReportList.loadFromDatabase(task_id)
    #test_report_list = TestReportList.loadFromFile("./data/random.json")
    #if (len(test_report_list.reports) < 20):
    #    return "Current reports {}, won't do the recommend".format(len(test_report_list.reports))


    generateReportModel(test_report_list, task_id)
    # 进行特征生成
#    if not os.path.exists("./model/{}".format(task_id)):
#        generateModel(test_report_list, task_id)
    # 当新增20条报告后，更新模型
#    else:
#        upDateModel(test_report_list, task_id)


    # 进行推荐
    #test_report = TestReport.random()
    report_id = request.args['reportId']
    #test_report = TestReport.setFromFile(report_id)
    test_report = TestReport.loadFromDataBase(report_id)

    # 获取推荐列表
    toolSims = getToolWeight(test_report, task_id)
    caseSims = getCaseWeight(test_report, task_id)
    errorSims = getErrorWeight(test_report, task_id)
    total_sims= toolSims * 0.4 + caseSims * 0.4 + errorSims * 0.2
    sims = sorted(enumerate(total_sims), key=lambda item : -item[1])
    res = []
    for i, sim in sims:
        #res[test_report_list.reports[i].report_id] = "{:.2f}".format(sim)
        item = {}
        item['reportId'] = test_report_list.reports[i].report_id
        item['sim'] = "{:.4f}".format(sim)
        res.append(item)
    return jsonify(res)

@app.route('/recommend/task', methods=["GET"])
def get_task_recommend():
    #taskList = TaskList.loadFromFile("./data/task.json")
    taskList = TaskList.loadFromDataBase()
    #worker = Worker.loadFromFile("./data/worker.json")
    userId = request.args['userId']
    worker = Worker.loadFromDataBase(userId=userId)

    generateTaskModel(taskList)
    modeWeight = getModeWeight(worker)
    skillWeight = getSkillsWeight(worker)
    testDevs = getDevsWeight(worker)
    total_sims = modeWeight * 0.4 + skillWeight * 0.4 + testDevs * 0.2
    sims = sorted(enumerate(total_sims), key=lambda item:-item[1])
    res = []
    for i, sim in sims:
        #res[taskList.tasks[i].taskId] = "{:.2f}".format(sim)
        item = {}
        item['taskId'] = taskList.tasks[i].taskId
        item['sim'] = "{:.4f}".format(sim)
        res.append(item)

    return jsonify(res)

@app.route('/health', methods=["GET"])
def health():
    return "health"


@app.route('/optimization/cluster', methods=["GET"])
def get_report_cluster():

    task_id = request.args['taskId']
    n_clusters = request.args['k']

    report_list = TestReportList.loadFromDatabase(task_id)

    n1 = to_tool_vector(report_list, task_id)

    n2 = to_case_vector(report_list, task_id)
    ## convert it to the vector format

    n = merge_vector(n1, n2)
    n = normalize_vector(n)

    xy, labels = caculate_cluster(n, n_clusters)
    res = []
    for i, report in enumerate(report_list.reports):
        item = {}
        item['reportId'] = report.report_id
        item['feature'] = ["{:.4f}".format(xy[i][0]), "{:.4f}".format(xy[i][1])]
        item['label'] = "{}".format(labels[i])
        res.append(item)
    return jsonify(res)

@app.route('/optimization/wordcloud_err', methods=["GET"])
def get_Error_wordcloud():
    task_id = request.args['taskId']
    res = get_error_wordcloud(task_id)
    return jsonify(res)


@app.route('/optimization/wordcloud_sug', methods=['GET'])
def get_Suggestion_wordcloud():
    task_id = request.args['taskId']
    res = get_suggestion_wordcloud(task_id)
    return jsonify(res)

@app.route('/optimization/wordcloud_clu', methods=['GET'])
def get_Conclusion_wordcloud():
    task_id = request.args['taskId']
    res = get_conclusion_wordcloud(task_id)
    return jsonify(res)

@app.route('/optimization/classify', methods=['GET'])
def get_report_classification():    
    task_id = request.args['taskId']
    res = classify_the_error(task_id)
    
    return jsonify(res)

@app.route('/optimization/get_classification_sim', methods=['GET'])
def get_classification_sim():
    task_id = request.args['taskId']
    res = get_classification_similarity(task_id)

    return jsonify(res)

if __name__ == "__main__":
    app.run(debug=True)