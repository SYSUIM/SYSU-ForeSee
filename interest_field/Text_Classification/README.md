# Text-Classification
## 介绍
数据以字为单位输入模型，预训练词向量使用 [金融新闻 Word+Character 300d](https://github.com/Embedding/Chinese-Word-Vectors)

## 环境
python 3.7  
pytorch 1.1  
tqdm  
sklearn  
tensorboardX

## 中文数据集
| label | Description            |
| ----- | ---------------------- |
| 0     | 不能反映研究领域的句子 |
| 1     | 可以反映研究领域的句子 |

| Sentence                                                     | label |
| ------------------------------------------------------------ | ----- |
| 投资艾码科技后,公司目前产品线包括APS、SFT、PLM、MES等领域,在工业4.0 布局较全面 | 1     |
| 16Q1 经营活动现金流量净额为-1863 万元，同比下降153.46%，主要为购买商品、接受劳务支付的现金及支付其他与经营活动有关的现金较上年同期增加 | 0     |
| 第一阶段是通过跟口袋科技合作,把线上客户和公司的线下业务结合;第二阶段是将供应商、销售商纳入环节;第三个是打通制造端,将生产企业联网,缩短产业相应时间,提高产业效率 | 0     |
| 本次定增投入项目致力于研发基于国产处理器芯片的高端计算机、IO 芯片和IO 模块、底层管控固件，有利于提升公司的自主创新核心技术能力 | 1     |
| 目前，公司在玻璃、蓝宝石、陶瓷、金属、触控、贴合、模组等领域具有领先的技术和市场地位，并积极布局材料、设备、智能制造等上下游产业 | 1     |

### 更换自己的数据集

 - 如果用字，按照我数据集的格式来格式化你的数据。  
 - 如果用词，提前分好词，词之间用空格隔开，`python run.py --model TextCNN --word True`  
 - 使用预训练词向量：utils.py的main函数可以提取词表对应的预训练词向量。  


## 效果

模型|备注
--|--
TextCNN|Kim 2014 经典的CNN文本分类
TextRNN|BiLSTM 
TextRCNN|BiLSTM+池化



## 使用说明
```
# 训练并测试：
# TextCNN
python run.py --model TextCNN

# TextRNN
python run.py --model TextRNN

# TextRCNN
python run.py --model TextRCNN
```

### 参数
模型都在models目录下，超参定义和模型定义在同一文件中。  


