# MongoDBDesign


## 表结构

```
ForeSee
    companyInfo -- 分析的文章相关内容
    industryInfo -- 公司基本信息
    news -- 股票新闻
    notice -- 股票公告
    report -- 利润图表数据
    research -- 地理信息
```

表结构示例如structure所示。

其中windowsLoad.txt中存有在Windows上将示例表存入MongoDB的命令行语句，以便调试。

load文件夹下为在192服务器导入表的脚本文件。

## 数据压缩

更新了192服务器上的MongoDB，从2.6更新至3.4，3.2后默认使用存储引擎WiredTiger，默认进行数据压缩。


