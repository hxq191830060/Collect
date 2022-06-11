FROM python:3.7
WORKDIR /project/demo

COPY requirements.txt ./
#RUN pip install -r requirements.txt -i https://mirrors.aliyun.com/pypi/simple/
#RUN pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple/
RUN pip install -U pip -i https://pypi.mirrors.ustc.edu.cn/simple/
RUN pip install -r requirements.txt -i https://pypi.mirrors.ustc.edu.cn/simple/
COPY . .

CMD ["gunicorn", "start:app", "-c", "./gunicorn.conf.py"]