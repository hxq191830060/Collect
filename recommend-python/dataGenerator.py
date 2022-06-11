import report
import numpy as np

if __name__ == "__main__": 
    trl1 = report.TestReportList.random(50)
    trl1.save("./data/random.json")
