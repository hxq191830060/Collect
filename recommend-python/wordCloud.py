import report
from gensim import corpora
from gensim import matutils
import numpy as np
import recommend
import jieba

def get_error_wordcloud(task_id):
    report_list = report.TestReportList.loadFromDatabase(task_id)
    #report_list = report.TestReportList.loadFromFile("/mnt/SoftwareIII/promise-recommend/data/random.json")
    recommend.generateReportModel(report_list, task_id)
    dictionary = corpora.Dictionary.load("./model/{}/errorDict".format(task_id))
    sorted_pair = sorted(dictionary.cfs.items(), key =
             lambda kv:(kv[1], kv[0]), reverse=True)
    res = []
    for i in sorted_pair:
        item = {}
        item['word'] = dictionary[i[0]]
        item['count'] = i[1]
        res.append(item)

    return res[0:20]

def get_suggestion_wordcloud(task_id):
    report_list = report.TestReportList.loadFromDatabase(task_id)
    recommend.generateReportModel(report_list, task_id)
    suggestion_text = [report.suggestion for report in report_list.reports]
    cuted_text = [[word for word in jieba.cut(doc) if len(word)>1] for doc in suggestion_text]
    dictionary = corpora.Dictionary(cuted_text)
    sorted_pair = sorted(dictionary.cfs.items(), key =
             lambda kv:(kv[1], kv[0]), reverse=True)
    res = []
    for i in sorted_pair:
        item = {}
        item['word'] = dictionary[i[0]]
        item['count'] = i[1]
        res.append(item)

    return res[0:20]

def get_conclusion_wordcloud(task_id):
    report_list = report.TestReportList.loadFromDatabase(task_id)
    recommend.generateReportModel(report_list, task_id)
    conclusion_text = [report.conclusion for report in report_list.reports]
    cuted_text = [[word for word in jieba.cut(doc) if len(word)>1] for doc in conclusion_text]
    dictionary = corpora.Dictionary(cuted_text)
    sorted_pair = sorted(dictionary.cfs.items(), key =
             lambda kv:(kv[1], kv[0]), reverse=True)
    res = []
    for i in sorted_pair:
        item = {}
        item['word'] = dictionary[i[0]]
        item['count'] = i[1]
        res.append(item)

    return res[0:20]


if __name__=="__main__":
    print(get_conclusion_wordcloud(202))