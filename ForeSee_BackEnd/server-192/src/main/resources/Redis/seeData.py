import redis   
import json

# 用于2的数据导入，数据比较零散，因此没有封装

# /data/ForeSee_BackEnd/server-192/src/main/resources/Redis
redis_conn = redis.Redis(host='localhost', port=6479, decode_responses=True, 
                        password='nopassword',db=1)   # host是redis主机，需要redis服务端和客户端都启动 redis默认端口是6379


# i = redis_conn.get(*redis_conn.keys(pattern='*蓝思科技*'))

for i in redis_conn.keys(pattern='*互联网*'):
    print(i)