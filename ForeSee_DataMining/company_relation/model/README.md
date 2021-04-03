## Introduction

基于 Pytorch 的Transformer中文关系抽取处理模型。


## 环境依赖:

- python >= 3.6

- torch >= 1.2
- hydra-core >= 0.11
- tensorboard >= 2.0
- matplotlib >= 3.1
- transformers >= 2.0
- jieba >= 0.39

## 主要目录

```
├── conf                      # 配置文件夹
│  ├── config.yaml            # 配置文件主入口
│  ├── preprocess.yaml        # 数据预处理配置
│  ├── train.yaml             # 训练过程参数配置
│  ├── hydra                  # log 日志输出目录配置
│  ├── embedding.yaml         # embeding 层配置
│  ├── model                  # 模型配置文件夹
│  │  ├── transformer.yaml    # transformer 模型参数配置
├── module                    # 可复用模块
│  ├── Embedding.py           # embedding 层
│  ├── Attention.py           # attention
│  ├── Transformer.py         # transformer
├── models                    # 模型目录
│  ├── BasicModule.py         # 模型基本配置
│  ├── Transformer.py         # Transformer 模型
├── utils                     # 常用工具函数目录
├── metrics.py                # 评测指标文件
├── serializer.py             # 预处理数据过程序列化字符串文件
├── preprocess.py             # 训练前预处理数据文件
├── vocab.py                  # token 词表构建函数文件
├── dataset.py                # 训练过程中批处理数据文件
├── trainer.py                # 训练验证迭代函数文件
├── main.py                   # 主入口文件（训练）
├── predict_test.py           # 测试入口文件（测试）            
├── README.md                 # read me 文件
```

## 数据说明

train.csv 文件，样式范例为：

| sentence                                                     | relation | head     | head_offset | tail                         | tail_offset |
| ------------------------------------------------------------ | -------- | -------- | ----------- | ---------------------------- | ----------- |
| 和讯股票消息信息安全概念午后走强，截至13：05，立思辰涨停，航天信息涨超9%，北信源涨超6%，蓝盾股份、网宿科技、绿盟科技、美亚柏科涨超5% | 竞争     | 航天信息 | 31          | 北信源                       | 40          |
| 新浪财经App：直播上线博主一对一指导A股变不变盘就看今天紧盯一指标新浪财经讯2月9日消息，周四晚间，多家上市公司晚间发布公告，以下为利好消息汇总：龙泉股份：签订5913万元合同龙泉股份近日与恒力石化（大连）炼化有限公司签订了《恒力炼化海水循环水预应力钢筒混凝土管（PCCP）制作安装工程施工合同》，合同总金额暂定人民币59，131，314.00元 | 合作     | 龙泉股份 | 74          | 恒力石化（大连）炼化有限公司 | 96          |
| 眼下，五大养猪企业2020年9月份的生猪出栏数据都已出炉：牧原股份出栏165万头，收入57.54亿元；正邦科技出栏121.6万头，收入49.96亿元；新希望出栏99.4万头，收入33.3亿元；温氏股份出栏74万头，收入30.7亿元；天邦股份出栏41.6万头，收入11.22亿元 | None     | 牧原股份 | 29          | 正邦科技                     | 51          |

安装依赖： pip install -r requirements.txt

开始训练：python main.py

每次训练的日志保存在 `logs` 文件夹内，模型结果保存在 `checkpoints` 文件夹内。



