import redis   
import glob 

# /data/ForeSee_BackEnd/server-192/src/main/resources/Redis

# key   value value
# 通过key 返回相关的value值
# 存储：数据库words，key为key，value存储在key，通过word定位key，返回value

class loadQuery():
    def __init__(self, n):
        self.n = n
        self.redis_conn = redis.Redis(host='localhost', port=6479, decode_responses=True, 
                        password='nopassword',db=n['db'])  
    def load(self, fileName):
        print("正在导入：", fileName, "……")
        with self.redis_conn.pipeline(transaction=False) as p:
            with open(fileName,'r') as f:
                data = f.read().strip().split('\n')
                for i in data:
                    i = i.split('\t')
                    codes = i[1].split(' ')
                    for code in codes:
                        try:
                            if (self.n['type'] == 'int'):
                                p.sadd(i[0], int(code))
                            else :
                                p.sadd(i[0], code)
                        except Exception as e:
                            print(e)
                            print(code)
            p.execute()

    def run(self):
        if (self.n['add'] == 'files'):
            files = glob.glob(self.n['path'])
            for f in files:
                self.load(f)
        else:
            self.load(self.n['path'])
        print('finished!')

if __name__ == '__main__':
    # 行业11 : 
    industry = {
        'path': '/data/prj2020/EnterpriseSpider/Index/index/industry_index.txt',
        'db': 11,
        'type':'string',
        'add':'file'
        }

    # 资讯12 : 
    report = {
        'path': '/data/prj2020/EnterpriseSpider/Index/index/report_index.txt',
        'db': 12,
        'type':'string',
        'add':'file'
        }

    # 企业13 : 
    company = {
        'path': '/data/prj2020/EnterpriseSpider/Index/index/stock_index.txt',
        'db': 13,
        'type':'string',
        'add':'file'
        }

    # 公告14 ：
    notice = {
        'path': '/data/prj2020/EnterpriseSpider/Index/index/notice/notice_index_*',
        'db': 14,
        'type':'int',
        'add':'files'
        }
    # 新闻15 : 
    news = {
        'path': '/data/prj2020/EnterpriseSpider/Index/index/news/news_index_*.txt',
        'db': 15,
        'type':'int',
        'add':'files'
        }

    # 新闻标题9
    newsTitle = {
        'path': '/data/prj2020/EnterpriseSpider/index/index/title_index.txt',
        'db': 9,
        'type':'int',
        'add':'file'
        }

    n = loadQuery(newsTitle)
    n.run()


