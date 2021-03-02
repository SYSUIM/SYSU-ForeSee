# 导入redis模块，通过python操作redis 也可以直接在redis主机的服务端操作缓存数据库
import redis   
import glob 
# /data/ForeSee_BackEnd/server-192/src/main/resources/Redis

# v = redis_conn.scard('stockCode') # set 数目
# v = redis_conn.smembers('stockCode') # set 元素,返回set object {}
# v = redis_conn.sismember('stockCode', 'value') # value是否在set中

# key   value value
# 通过key 返回相关的value值
# 存储：数据库words，key为key，value存储在key，通过word定位key，返回value

# 行业11 : 
industry = {
    'path': '/data/prj2020/EnterpriseSpider/Index/index/industry_index.txt',
    'db': 11
    }

# 资讯12 : 
report = {
    'path': '/data/prj2020/EnterpriseSpider/Index/index/report_index.txt',
    'db': 12
    }

# 企业13 : 
company = {
    'path': '/data/prj2020/EnterpriseSpider/Index/index/stock_index.txt',
    'db': 13
    }

# 公告14 ：
notice = {
    'path': '/data/prj2020/EnterpriseSpider/Index/index/notice/notice_index_*',
    'db': 14
    }
# 新闻15 : 
news = {
    'path': '/data/prj2020/EnterpriseSpider/Index/index/news/news_index_*.txt',
    'db': 15
    }

# select
n = notice


redis_conn = redis.Redis(host='localhost', port=6479, decode_responses=True, 
                        password='nopassword',db=n['db'])   
print("成功连接")
# with open(n['path'],'r') as f:
#     data = f.read().strip().split('\n')
#     for i in data:
#         i = i.split('\t')
#         codes = i[1].split(' ')
#         redis_conn.sadd(i[0], *codes)
# print("完成")

# news
files = glob.glob(n['path'])


# 导入部分 /data/prj2020/EnterpriseSpider/Index/index/news/news_index_16.txt
for k in files[:]:
    print(k)
    nw = k
    with open(nw,'r') as f:
        data = f.read().strip().split('\n')
        for i in data:
            i = i.split('\t')
            codes = i[1].split(' ')
            try:
                redis_conn.sadd(i[0], *codes)
            except:
                for code in codes:
                    redis_conn.sadd(i[0], code)
