import json

from data_manager import DataManager
class Worker:
    workerSkills = []
    taskPreference = []
    testDevs = []

    def __init__(self):
        pass

    @classmethod
    def loadFromFile(cls, fp):
        file = open(fp, "r", encoding="utf-8")
        json_data = json.load(file)
    
        worker = Worker()
        worker.workerSkills = json_data["workerSkills"]
        worker.taskPreference = json_data["taskPreference"]
        worker.testDevs = json_data["testDevs"]
        return worker

    @classmethod
    def loadFromDataBase(cls, userId):
        worker = Worker()
        json_data = json.loads(DataManager().selectUserByUserId(userId=userId))
        worker.workerSkills = json_data["workerSkills"]
        worker.taskPreference = json_data["taskPreference"]
        worker.testDevs = json_data["testDevs"]
        return worker


if __name__ == "__main__":
    file = open("./data/worker.json", "r", encoding="utf-8")
    data = json.load(file)
    worker = Worker.loadFromFile(data)
    print(worker.taskPreference)