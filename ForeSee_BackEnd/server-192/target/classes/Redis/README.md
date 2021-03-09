# RedisDesign

## 功能定位

在前后端之间使用**redis数据库**建立索引，连接前后端。利用redis数据库基于内存、可进行高速查询的特点，处理前端的请求，并将处理后的请求传输给后端以实现更快速的检索。

## 数据库逻辑

索引借助python将数据存储至redis库中。各数据库内容如下：

| 数据库 | 内容                                                | 存储方式                |
| ------ | --------------------------------------------------- | ----------------------- |
| 1      | stock_code/company_name/industry_name/industry_code | 对应stock_code的集合    |
| 2      | industry_name/industry_code                         | 对应industry_code字符串 |
| 9      | 新闻标题分词                                        | news_ids |
| 11     | 行业简介分词                                        | industry_codes          |
| 12     | 行业资讯分词                                        | report_ids              |
| 13     | 企业分词                                            | stock_codes             |
| 14     | 企业公告分词                                        | notice_ids              |
| 15     | 新闻分词                                            | news_ids                |

## 数据库分布

| 地址 | 端口 | 存储内容             | 备注   |
| ---- | ---- | -------------------- | ------ |
| 107  | 6479 | 简化的新闻完全索引   |        |
| 106  | 6479 | 新闻分词，1*         |        |
| 112  | 6479 | 新闻分词，2\*/3\*/4* |        |
| 113  | 6479 | 新闻分词，5\*/6\*/7* |        |
| 105  | 6479 | 企业公告分词         |        |
| 222  | 6379 |                      | 未启用 |

## 数据压缩

由于新闻和公告数据过大，原来的字符串索引修改为整型索引。

## 快速导入

借助python，测试出最优、最稳定的pipeline导入方式，示例代码如下：

```python
import redis

n = {
    'path': '../data/news_index_*.txt',
    'db': 15
    }

redis_conn = redis.Redis(host='localhost', port=6479, decode_responses=True, 
                        password='nopassword',db=n['db'])   

files = glob.glob(n['path'])

with redis_conn.pipeline(transaction=False) as p:
    for k in files:
        print(k)
        nw = k
        with open(nw,'r') as f:
            data = f.read().strip().split('\n')
            for i in data:
                i = i.split('\t')
                codes = i[1].split(' ')
                for code in codes:
                    try:
                        p.sadd(i[0], int(code))
                    except Exception as e:
                        print(e)
                        print(code)
        p.execute()
```

## 高可用性

参考哨兵集群，编写shell脚本，每分钟判断redis进程是否正常。

nc -vz IP Port

crontab 1分钟判断一次ip端口状态

