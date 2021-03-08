from py2neo import Graph,Node,Relationship
import glob
import json

# connect
graph = Graph(
    "http://localhost:7474", 
    username="neo4j", 
    password="nopassword"
)

# d = []
# path = '/data/prj2020/EnterpriseSpider/basic/BasicInfoFile/*'
# dpaths = glob.glob(path)
# for dpath in dpaths:
#     files = glob.glob(path+"/*")
#     for f in files:
#         with open(f, 'r') as fr:
#             data = json.loads(
#                 fr.read().replace('entName', '企业名称')
#                 .replace('stock_code', '股票代码')
#                 .replace('authority', '登记机关')
#                 .replace('district', '地址')
#                 .replace('entType', '企业类型')
#                 .replace('industry', '行业类型')
#                 .replace('legalPerson', '法定代表人')
#                 .replace('startDate', '成立时间')
#                 .replace('regCapital', '注册资本')
#                 .replace('licenseNumber', '工商注册号')
#                 .replace('orgNo', '组织机构代码')
#                 .replace('regNo', '纳税人识别号')
#                 .replace('regAddr', '地址')
#                 .replace('fax', '联系方式')
#                 .replace('email', '邮箱')
#             )
#             key = '企业名称'
#             if (data.get(key) not in d):
#                 d.append(data.get(key))
#                 node = Node(key, name=str(data.get(key)))
#                 for pro in data.keys():
#                     node[pro] = data.get(pro)
#                 graph.create(node)

# print(len(d))
# 删除所有数据
# print('begin')
# graph.run('MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r')


# 根据关系联系节点
# for pro in data:
#     graph.run('MATCH (entity1:stock_code) , (entity2:'+pro+'{name:entity1.'+pro+'}) CREATE (entity1)-[:'+pro+']->(entity2)')


# a = graph.run('CREATE INDEX ON :企业名称(name)')

a = graph.run("MATCH (a:企业名称) WHERE a.name =~ '.*蓝思科技.*' RETURN a AS result")
for j in a :
    print (j)
    

print('finished!')