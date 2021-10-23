# ForeSee触发模块

## 模块介绍

本模块主要用于接收来自前端的请求，将请求转发至ForeSee数据访问模块，收到检索结果后把结果返回给前端。

### 模块结构

```lua
├── client-222
    ├── src
        ├── main
            ├── java  -- java源文件
                ├── com.ForeSee.ForeSee
                    ├── controller  -- controller层代码，接收前端的请求并发送请求给数据访问模块
                    ├── ClientMain.java  -- 启动类
            ├── resource  -- 其他文件
                ├── application.yml  -- 主配置文件，用于指定使用哪个配置文件
                ├── application-dev.yml  -- 开发环境配置文件
                ├── application-prod.yml  -- 生产环境配置文件
        ├── test  -- 测试文件
	├── pom.xml  -- Maven依赖文件
```



### 接口文档

#### 关键词检索公司

请求方法：GET 

请求路径：/ForeSee/companyInfo

请求参数：关键词（公司名、公司股票代号、行业名、行业代号）

返回参数：

| 参数               | 说明         |
| ------------------ | ------------ |
| former_name        | 公司名       |
| registered_address | 公司登记地址 |
| industry_code      | 公司行业代号 |
| stock_code         | 公司股票代号 |
| Logo               | 公司Logo链接 |

返回样例：

```json
[
    {
        "registered_address":"湖南浏阳生物医药园",
        "former_name":"蓝思科技股份有限公司",
        "industry_code":"BK0537",
        "stock_code":"300433",
        "Logo":"https:\/\/zhengxin-pub.cdn.bcebos.com\/logopic\/c79998593a636408683cb625cb928f5c_fullsize.jpg"
    }
]
```



#### 公司的所有相关信息

请求方法：GET

请求路径：/ForeSee/allInfo

请求参数：公司股票代号

返回参数：

| 参数                    | 说明               |
| ----------------------- | ------------------ |
| companyInfo             | 公司基本信息       |
| companyInfo.former_name | 公司名             |
| companyInfo.logo        | 公司Logo链接       |
| companyInfo.describe    | 公司描述           |
| companyInfo.stock_code  | 公司股票代号       |
| tableData               | 公司表格展示信息   |
| tableData.name          | 表格信息名         |
| tableData.value         | 表格信息值         |
| news                    | News数组           |
| news.news_title         | News标题           |
| news.news_time          | News时间           |
| news.news_link          | News链接           |
| news.notice_info        | Notice数组         |
| profit                  | 利润情况表格       |
| profit.stock_code       | 公司股票代号       |
| profit.time             | 利润记录时间       |
| profit.income           | 公司收入           |
| profit.profit           | 公司利润           |
| profit.total_profit     | 公司总利润         |
| profit.net_profit       | 公司净利润         |
| geo                     | 地理信息           |
| geo.lng                 | 经度               |
| geo.lat                 | 纬度               |
| geo.company_name        | 公司名             |
| geo.stock_code          | 股票代号           |
| contact                 | 联系信息           |
| contact.address         | 公司地址           |
| contact.office          | 公司办公室电话号码 |
| contact.fax             | 公司传真机号码     |
| contact.email           | 公司邮箱地址       |

返回样例：

```json
{
    "companyInfo":{
        "former_name":"广西博世科环保科技股份有限公司",
        "logo":"https:\/\/zhengxin-pub.cdn.bcebos.com\/logopic\/565bbd47429a6d372a3549b412bf535a_fullsize.jpg",
        "describe":"广西博世科环保科技股份有限公司成立于1999年04月13日，注册地位于南宁市高新区科兴路12号，法定代表人为宋海农。经营范围包括一般项目：环保设备制造及销售；环保技术研究开发及服务；园林设施及设备的销售、安装；园林绿化技术咨询、服务；自营和代理一般经营项目商品和技术的进出口业务，许可经营项目商品和技术的进出口业务须取得国家专项审批后方可经营（国家限定公司经营或禁止进出口的商品和技术除外）；农林生态开发；水污染治理；农副产品、农林作物的生产、种植、养殖、销售；大气污染治理；固体废物治理；土地使用权租赁；住房租赁；非居住房地产租赁；仓储设备租赁服务；机械设备租赁；物业管理；软件开发；信息系统运行维护服务；信息系统集成服务（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）；许可项目：环保设施运营；市政工程施工、机电设备安装工程、承接环保工程、园林绿化工程；城乡生活垃圾经营性清扫、收集、运输、处理和处置管理服务；道路货物运输；市政工程设计、环境工程设计；建筑工程施工、河湖整治工程施工、水利水电工程施工；消毒产品的生产与销售；工程造价咨询业务。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）广西博世科环保科技股份有限公司对外投资76家公司，具有26处分支机构。",
        "stock_code":"300422"
    },
    "tableData":[
        {
            "name":"法定代表人",
            "value":"宋海农"
        },
        {
            "name":"公司名称",
            "value":"广西博世科环保科技股份有限公司"
        },
        {
            "name":"成立日期",
            "value":"1999-04-13"
        },
        {
            "name":"注册资本",
            "value":"40,571.1821万(元)"
        },
        {
            "name":"工商注册号",
            "value":"450111200005469"
        },
        {
            "name":"登记机关",
            "value":"南宁市市场监督管理局"
        },
        {
            "name":"股票代码",
            "value":"300422"
        }
    ],
    "news":[
        {
            "news_title":"博世科(300422.SZ)：联合体中标淮上区百川高端生物产业园污水处理项目EPC总承包",
            "news_time":"2020-10-15",
            "news_link":"https://cj.sina.cn/articles/view/5115326071/130e5ae7702001562l"
        },
        {
            "news_title":"投资者提问：请问截至10月10日公司股东人数？谢谢！",
            "news_time":"2020-10-14",
            "news_link":"https://finance.sina.com.cn/stock/relnews/dongmiqa/2020-10-14/doc-iiznctkc5450804.shtml"
        },
        {
            "news_title":"博世科(300422.SZ)与中设集团联合体中标EPC项目",
            "news_time":"2020-10-14",
            "news_link":"https://finance.sina.com.cn/stock/hkstock/ggscyd/2020-10-14/doc-iiznctkc5571895.shtml"
        }
    ],
    "notice_info":[

    ],
    "profit":{
        "stock_code":"300422",
        "time":[
            "20200630",
            "20190630",
            "20180630",
            "20170630",
            "20160630",
            "20150630",
            "20140630",
            "20130630",
            "20120630",
            "20110630"
        ],
        "income":[
            "78328000000.00",
            "67829000000.00",
            "57241000000.00",
            "54073000000.00",
            "54769000000.00",
            "46575000000.00",
            "34733000000.00",
            "23426000000.00",
            "19625534000.00",
            "12137267000.00"
        ],
        "expense":[
            "22178000000.00",
            "20588000000.00",
            "39839000000.00",
            "37605000000.00",
            "38613000000.00",
            "31308000000.00",
            "21388000000.00",
            "13532000000.00",
            "10788634000.00",
            "6090059000.00"
        ],
        "profit":[
            "17659000000.00",
            "20037000000.00",
            "17402000000.00",
            "16468000000.00",
            "16156000000.00",
            "15267000000.00",
            "13345000000.00",
            "9894000000.00",
            "8836900000.00",
            "6047208000.00"
        ],
        "total_profit":[
            "17587000000.00",
            "20003000000.00",
            "17367000000.00",
            "16432000000.00",
            "16154000000.00",
            "15259000000.00",
            "13328000000.00",
            "9906000000.00",
            "8878649000.00",
            "6063160000.00"
        ],
        "net_profit":[
            "13678000000.00",
            "15403000000.00",
            "13372000000.00",
            "12554000000.00",
            "12292000000.00",
            "11585000000.00",
            "10072000000.00",
            "7531000000.00",
            "6869564000.00",
            "4731138000.00"
        ]
    },
    "geo":[
        {
            "lng":108.26388528163366,
            "lat":22.868577386164777,
            "company_name":"广西博世科环保科技股份有限公司",
            "stock_code":"300422"
        }
    ],
    "contact":{
        "address":"南宁市高新区科兴路12号",
        "office":"0771-3225158",
        "fax":"0771-3225158",
        "email":"bskdb@bossco.cc"
    }
}
```



#### 公司的Notice

请求方法：GET

请求路径：/ForeSee/allNotice

请求参数：公司股票代号，页数

返回参数：

| 参数         | 说明           |
| ------------ | -------------- |
| page         | Notice页数     |
| notice       | Notice内容数组 |
| notice.date  | Notice日期     |
| notice.title | Notice标题     |
| notice.url   | Notice链接     |

返回样例：

```json
{
    "page":1,
    "notice":[
        {
            "date":"2020-10-18",
            "title":"关于下属子公司签署推广装配式建筑体系合作协议的公告",
            "url":"http://static.sse.com.cn/disclosure/listedinfo/announcement/c/2020-10-19/600496_20201019_2.pdf"
        },
        {
            "date":"2020-10-16",
            "title":"关于控股股东股权质押的公告",
            "url":"http://static.sse.com.cn/disclosure/listedinfo/announcement/c/2020-10-17/600496_20201017_1.pdf"
        }
    ]
}
```



#### 公司的News

请求方法：GET

请求路径：/ForeSee/allNews

请求参数：公司股票代号，页数

返回参数：

| 参数       | 说明     |
| ---------- | -------- |
| page       | 页数     |
| news       | News数组 |
| news.date  | News日期 |
| news.title | News标题 |
| news.url   | News链接 |

返回样例：

```json
{
    "page":1,
    "news":[
        {
            "date":"2020-10-15",
            "title":"博世科(300422.SZ)：联合体中标淮上区百川高端生物产业园污水处理项目EPC总承包",
            "url":"https://cj.sina.cn/articles/view/5115326071/130e5ae7702001562l"
        },
        {
            "date":"2020-10-14",
            "title":"投资者提问：请问截至10月10日公司股东人数？谢谢！",
            "url":"https://finance.sina.com.cn/stock/relnews/dongmiqa/2020-10-14/doc-iiznctkc5450804.shtml"
        }
    ]
}
```



#### 行业分析报告

请求方法：GET

请求路径：/ForeSee/industryReports

请求参数：行业代号，页数

返回参数：

| 参数             | 说明             |
| ---------------- | ---------------- |
| industry         | 行业名           |
| page             | 页数             |
| totalpage        | 总页数           |
| reports          | 报告数组         |
| reports.date     | 报告日期         |
| reports.title    | 报告标题         |
| reports.url      | 报告链接         |
| reports.research | 报告所分析的年份 |

返回样例：

```json
{
    "industry":"互联网",
    "page":1,
    "totalpage":1507,
    "reports":[
        {
            "date":"2020-9-27",
            "title":"2019年中国网络音乐行业用户规模及发展趋势分析[图]",
            "url":"http://www.chyxx.com/industry/202009/897697.html",
            "research":"2019"
        },
        {
            "date":"2020-9-24",
            "title":"2019年中国网上外卖行业用户数量、使用率、发展中存在的问题及未来发展前景分析[图]",
            "url":"http://www.chyxx.com/industry/202009/897200.html",
            "research":"2019"
        }
    ]
}
```



#### 行业信息

请求方法：GET

请求路径：/ForeSee/industryInfo

请求参数：行业代号

返回参数：

| 参数               | 说明             |
| ------------------ | ---------------- |
| introduction       | 行业简介信息     |
| introduction.brief | 行业简介文本     |
| introduction.url   | 行业简介链接     |
| geo                | 行业公司地址信息 |
| geo.lng            | 公司经度         |
| geo.lat            | 公司纬度         |
| geo.company_name   | 公司名           |
| geo.stock_code     | 公司股票代号     |

返回样例：

```json
{
    "introduction":{
        "brief":"目前后端好像还没有这个数据",
        "url":"目前后端好像还没有这个数据"
    },
    "geo":[
        {
            "lng":113.38429103513516,
            "lat":28.221499990630715,
            "company_name":"蓝思科技股份有限公司",
            "stock_code":"300433"
        },
        {
            "lng":108.97773919458935,
            "lat":34.16501602885231,
            "company_name":"隆基绿能科技股份有限公司",
            "stock_code":"601012"
        }
    ]
}
```

### 配置说明

主配置文件

```yaml
spring:
  profiles:
    active: prod  # 指定配置文件，本地测试使用dev文件，服务器部署使用prod文件
  application:
    name: springcloud-client # 服务名，发送请求给微服务处理的服务消费者
```

开发环境配置文件：用于本地测试

```yaml
server:
  port: 8288 # 占用端口
  servlet:
    context-path: /ForeSee # 前端发送请求时需要加上/ForeSee前缀
  # 本次测试发送请求的路径:127.0.0.1:8288/ForeSee/
http:
  REST_URL_PREFIX: http://127.0.0.1:6666 # 把请求转发给对应路径的路由网关
  # 前端请求127.0.0.1:8288/ForeSee/companyInfo/300433
  # 转发给路由网关的请求127.0.0.1:6666/companyInfo/300433
  # 转发给微服务的请求127.0.0.1:8287/companyInfo/300433
```

生产环境配置文件：用于服务器部署

```yaml
server:
  port: 8288 # 占用端口
  servlet:
    context-path: /ForeSee # 前端发送请求时需要加上/ForeSee前缀
  # 服务器部署发送请求的路径:121.46.19.26:8288/ForeSee/
http:
  REST_URL_PREFIX: http://222.200.184.74:6666 # 把请求转发给对应路径的路由网关
  # 前端请求121.46.19.26:8288/ForeSee/companyInfo/300433
  # 转发给路由网关的请求222.200.184.125:6666/companyInfo/300433
  # 转发给微服务的请求192.168.1.103:8288/companyInfo/300433
```

