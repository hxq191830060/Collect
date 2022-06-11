import pandas
import jieba
from gensim import corpora, matutils
import numpy as np
from numpy import linalg as LA
from sklearn.tree import DecisionTreeClassifier
from joblib import dump, load
import report

def generate_classification_model():
    df = pandas.read_excel("./data/Example_Report.xlsx")

    des_text = [df.iloc[i]['description'] for i in range(0, len(df))]
    
    convert = lambda x: 0 if x=='与预期不符' else 1 if x=='无法运行' else 2
    labels = [convert(df.iloc[i]['label']) for i in range(0, len(df)) ]

    cutted_text = [[word for word in jieba.cut(doc) if len(word)>1] for doc in des_text]
    dictionary = corpora.Dictionary(cutted_text)
    dictionary.save("./model/classificationDict")

    feature_matrix = [dictionary.doc2bow(text) for text in cutted_text]
    n = matutils.corpus2dense(feature_matrix, num_terms=len(dictionary))
    n = np.transpose(n)

    clf = DecisionTreeClassifier()
    clf.fit(n, labels)

    dump(clf, "./model/decision_tree_clf.joblib")


def classify_the_error(task_id):
    # load the model
    clf = load("./model/decision_tree_clf.joblib")    
    dictionary = corpora.Dictionary.load("./model/classificationDict")

    # get the report_list
    report_list = report.TestReportList.loadFromDatabase(task_id)
    error_text = []
    for rpt in report_list.reports:
        error_text.append(" ".join(item.error_msg for item in rpt.defect_list))

    cutted_text = [[word for word in jieba.cut(text) if len(word) > 1] for text in error_text]
    error_matrix = [dictionary.doc2bow(text) for text in cutted_text]

    x = np.transpose(matutils.corpus2dense(error_matrix, num_terms=len(dictionary)))
    y = clf.predict(x)
    
    res = []
    convert = lambda x: '与预期不符' if x== 0 else '无法运行' if x==1 else '用户体验不足'
    for i in range(0, len(y)):
        item={}
        item['reportId'] = report_list.reports[i].report_id
        item['label'] = convert(y[i])
        res.append(item)

    return res

def caculate_cos(vec1, vec2):
    cos = np.dot(vec1, vec2) / (LA.norm(vec1) * LA.norm(vec2))
    if (LA.norm(vec1) * LA.norm(vec2)) == 0:
        return 0
    return cos

def get_classification_similarity(task_id):
    # load the model
    clf = load("./model/decision_tree_clf.joblib")    
    dictionary = corpora.Dictionary.load("./model/classificationDict")

    # get the report_list
    report_list = report.TestReportList.loadFromDatabase(task_id)
    #report_list = report.TestReportList.loadFromFile("/mnt/SoftwareIII/promise-recommend/data/random.json")
    error_text = []
    for rpt in report_list.reports:
        error_text.append(" ".join(item.error_msg for item in rpt.defect_list))

    cutted_text = [[word for word in jieba.cut(text) if len(word) > 1] for text in error_text]
    error_matrix = [dictionary.doc2bow(text) for text in cutted_text]
    x = np.transpose(matutils.corpus2dense(error_matrix, num_terms=len(dictionary)))
    y = clf.predict(x)

    class0_count = 0
    class1_count = 0
    class2_count = 0

    avg_class0_vec = np.zeros(len(dictionary))
    avg_class1_vec = np.zeros(len(dictionary))
    avg_class2_vec = np.zeros(len(dictionary))
    avg_total_vec  = np.zeros(len(dictionary))
    
    for index, label in enumerate(y):
        if label == 0:
            class0_count += 1
            avg_class0_vec += x[index]
        if label == 1:
            class1_count += 1
            avg_class1_vec += x[index]
        if label == 2:
            class2_count += 1
            avg_class2_vec += x[index]
        avg_total_vec += x[index]

    if class0_count != 0:
        avg_class0_vec /= class0_count
    if class1_count != 0:
        avg_class1_vec /= class1_count
    if class2_count != 0:
        avg_class2_vec /= class2_count
    avg_total_vec /= len(y)

    class0_to_total_avg = caculate_cos(avg_class0_vec, avg_total_vec)
    class1_to_total_avg = caculate_cos(avg_class1_vec, avg_total_vec)
    class2_to_total_avg = caculate_cos(avg_class2_vec, avg_total_vec)

    #print(class0_to_total_avg)
    #print(class1_to_total_avg)
    #print(class2_to_total_avg)
    res = []
    for index, label in enumerate(y):
        item = {}
        item['reportId'] = report_list.reports[index].report_id
        if label == 0:
            item['label'] = '与预期不符'
            item['sim'] = caculate_cos(x[index], avg_class0_vec)
        if label == 1:
            item['label'] = '无法运行'
            item['sim'] = caculate_cos(x[index], avg_class1_vec)
        if label == 2:
            item['label'] = '用户体验不足'
            item['sim'] = caculate_cos(x[index], avg_class2_vec)
        res.append(item)

    if class0_count != 0:
        item = {}
        item['labelName'] = '与预期不符'
        item['sim'] = class0_to_total_avg        
        res.append(item)
    if class1_count != 0:
        item = {}
        item['labelName'] = '无法运行'
        item['sim'] = class1_to_total_avg        
        res.append(item)
    if class2_count != 0:
        item = {}
        item['labelName'] = '用户体验不足'
        item['sim'] = class2_to_total_avg        
        res.append(item)
    return res
            
if __name__=="__main__":
    #generate_classification_model()
    get_classification_similarity(1)