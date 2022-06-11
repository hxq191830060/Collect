import json

from data_manager import DataManager
class Task:
    taskId = ""
    taskMode = ""
    requireSkills = []
    testEnvironments = []
    
    def __init__(self):
        pass
    
    @classmethod
    def loadFromFile(cls, json_data):
        task = Task()
        task.taskId = json_data['taskId']
        task.taskMode = json_data["taskMode"]
        task.requireSkills = json_data["requireSkills"]
        task.testEnvironments = json_data["testEnvironments"]
        return task

class TaskList:
    tasks = []

    def __init__(self):
        pass

    @classmethod
    def loadFromFile(cls, fp):
        taskList = TaskList()
        
        file = open(fp, "r", encoding="utf-8")
        data = json.load(file)        
        taskList.tasks = [Task.loadFromFile(item) for item in data['tasks']]     

        return taskList
    
    @classmethod
    def loadFromDataBase(cls):
        data = DataManager().selectTaskList()
        taskList = TaskList()
        taskList.tasks = [Task.loadFromFile(item) for item in data]     
        return taskList

if __name__ == "__main__":
    taskList = TaskList.loadFromFile("./data/task.json")
    for item in taskList.tasks:
        print(item.taskMode)



