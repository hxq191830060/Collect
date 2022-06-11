import report
import numpy as np
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.preprocessing import normalize
from gensim import corpora
from gensim import matutils
import recommend

def caculate_cluster(data, n_clusters):
    print(type(int(n_clusters)))
    model = KMeans(n_clusters=int(n_clusters))
    model.fit(data)

    color_list = ['teal', 'gold', 'maroon', 'olive', 'red']
    mapf = lambda x: color_list[x]

    pca = PCA(n_components=2)
    n = pca.fit_transform(data)

    return n, model.labels_

def merge_vector(n1, n2):
    n = []
    for i in range(0, len(n1)):
        n.append(np.concatenate((n1[i], n2[i]), axis=0))
    n = np.array(n)
    return n

def normalize_vector(n):
    return normalize(n)

# use the trained dictionary to generate vector
def to_tool_vector(report_list, task_id):
    ## get the tool vector first
    # load the trained model
    dictionary = corpora.Dictionary.load("./model/{}/toolDict".format(task_id))
    tool_list = [test_report.test_tools for test_report in report_list.reports]
    tool_vec = [dictionary.doc2bow(tool) for tool in tool_list]
    n = matutils.corpus2dense(tool_vec, num_terms=len(dictionary))
    return np.transpose(n)

def to_case_vector(report_list, task_id):
    ## get the test case vector
    dictionary = corpora.Dictionary.load("./model/{}/caseDict".format(task_id))

    case_list = [[str(test_item.case_id) for test_item in test_report.test_list] for test_report in report_list.reports]
    case_vec = [dictionary.doc2bow(case) for case in case_list]

    n = matutils.corpus2dense(case_vec, num_terms=len(dictionary))
    return np.transpose(n)

if __name__=="__main__":
    ## load the report

    report_list = report.TestReportList.loadFromDatabase(202)
    #report_list = report.TestReportList.loadFromFile("/mnt/SoftwareIII/promise-recommend/data/random.json")
    
    n1 = to_tool_vector(report_list, 202)
    #print(n1)

    n2 = to_case_vector(report_list, 202)
    #print(n2)
    ## convert it to the vector format

    n = []
    for i in range(0, len(report_list.reports)):
        n.append(np.concatenate((n1[i], n2[i]), axis=0))
    n = np.array(n)

    n = normalize(n)
    caculate_cluster(n, 2)
    ## KMeans

    ## convert to the required return format
    

