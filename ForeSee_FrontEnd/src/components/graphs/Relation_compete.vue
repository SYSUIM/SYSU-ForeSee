<template>
    <div id="mainCompete"></div>
</template>

<script>
var echarts = require('echarts')

import router from '../../router/index'

function graph2(json){
    var myChart = echarts.init(document.getElementById('mainCompete'));
    //myChart.showLoading();

    // 数据重组格式 1
    var nodes = [];
    var links = [];
    nodes.push({
        name: json.name,
        category: 0,
        symbolSize: 50,
        // 将节点设置为图像，支持 url（http网络地址或本地）、base64、svg 等，其中 svg 格式最佳，下为 svg 示例。
        // 注意：svg 格式需添加 path:// 前缀，其他格式需添加 image:// 前缀
        symbol: "path://M524.288 573.44c43.58144 0 85.23776 8.617984 123.363328 24.240128C572.891136 652.034048 524.288 740.179968 524.288 839.68a297.672704 297.672704 0 0 0 36.552704 143.368192L32.768 983.04v-81.92c0-180.224 147.456-327.68 327.68-327.68h163.84zM442.368 0c139.264 0 245.76 106.496 245.76 245.76S581.632 491.52 442.368 491.52 196.608 385.024 196.608 245.76 303.104 0 442.368 0zM794.624 630.784c108.527616 0 196.608 88.080384 196.608 196.608s-88.080384 196.608-196.608 196.608-196.608-88.080384-196.608-196.608 88.080384-196.608 196.608-196.608z m-37.748736 114.556928l-39.845888 119.27552-39.845888-119.27552a12.98432 12.98432 0 0 0-16.515072-8.126464 12.98432 12.98432 0 0 0-8.126464 16.515072l52.166656 156.237824c1.835008 5.24288 6.815744 8.912896 12.320768 8.912896s10.48576-3.670016 12.320768-8.912896L781.5168 753.729536c2.359296-6.815744-1.572864-14.155776-8.388608-16.515072a12.599296 12.599296 0 0 0-16.252928 8.126464z m90.43968 17.301504c23.068672 0 39.059456 10.747904 39.059456 25.952256 0 7.077888 5.767168 13.1072 13.1072 13.1072 7.340032 0 13.1072-5.767168 13.1072-13.1072 0-30.14656-27.52512-52.166656-65.273856-52.166656-40.108032 0-65.273856 19.922944-65.273856 52.166656 0 31.195136 26.2144 52.166656 65.273856 52.166656 6.5536 0 39.059456 1.31072 39.059456 25.952256 0 22.544384-24.641536 25.952256-39.059456 25.952256-23.068672 0-39.059456-10.747904-39.059456-25.952256 0-7.077888-5.767168-13.1072-13.1072-13.1072-7.077888 0-13.1072 5.767168-13.1072 13.1072 0 30.14656 27.52512 52.166656 65.273856 52.166656 40.108032 0 65.273856-19.922944 65.273856-52.166656 0-31.195136-26.2144-52.166656-65.273856-52.166656-6.5536 0-39.059456-1.31072-39.059456-25.952256 0-22.544384 24.379392-25.952256 39.059456-25.952256z",
        symbolOffset: ['15%', '-15%'], // 向右移动15%, 向上移动 15%
        des: json.name,
    })
    for(var i=0; i<json.compete.length; i++) {
        var x = json.compete[i].symbolSize; // 防止节点过大
        if( x >= 20 )
            x = 50;
        else 
            x = x + 30;
        nodes.push({
            name: json.compete[i].name,
            category: 1,
            // symbolSize: json.compete[i].symbolSize + 30,
            symbolSize: x,
            symbol: "path://M1031.5776 964.334933V641.706667L667.511467 403.182933V0L60.689067 139.537067v824.797866H0V1024h1092.266667v-59.665067h-60.689067z m-60.689067-290.4064v284.4672H667.511467v-484.693333zM121.378133 187.255467l485.444267-112.093867v885.623467H121.378133V187.255467z m121.309867 121.105066h242.756267v59.5968H242.688v-59.5968z m0 178.858667h242.756267v59.665067H242.688v-59.665067z m0 178.926933h242.756267v59.665067H242.688v-59.665067z",
            des: json.compete[i].name
        })
        links.push({
            source: json.name,
            target: json.compete[i].name,
            des: json.compete[i].des
        })
    }

    

    // 数据重组格式 2
    /*
    var nodes = [];
    var links = [];
    nodes.push({
        name: json.name,
        category: 0,
        symbolSize: 50 //为节点增加一个属性，让节点更大一些，默认是20
    })

    for(let key in json.compete) {
        var oneNode = {
            name: key,
            category: 1,
            symbolSize: json.compete[key] + 10
        }
        var oneLink = {
            source: json.name,
            target: key
        }
        nodes.push(oneNode);
        links.push(oneLink);
    }
    */
    
    var categories = [];
    categories[0] = { name: '本公司' };
    categories[1] = { name: '竞争者' };

    var option = {
        // title: {
        //     text: '主要竞争者'
        // },
        // tooltip: {
        //     formatter: '{b}'
        // },
        tooltip: {
            formatter: function (x) {
                return x.data.des;
            }
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: true
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                },
                dataView: {
                    show: true
                },
                dataZoom: { //缩放,实际上只支持直角坐标系
                    show: true
                }
            }
        },
        legend: [{
            // selectedMode: 'single',
            top: 'bottom'
            // data: categories.map(function (a) {
            //     return a.name;
            //     })
        }],
        series: [{
            type: 'graph', // 类型:关系图
            layout: 'force', //图的布局，类型为力导图
            // symbol:"circle",
            symbolSize: 20, // 调整节点的大小
            roam: true, // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
            // edgeSymbol: ['circle', 'arrow'],
            edgeSymbolSize: [2, 10],
            force: {
                // repulsion: 2500,
                repulsion: 500,
                edgeLength: [10, 50],
                layoutAnimation: false
            },
            draggable: true,
            lineStyle: {
                normal: {
                    width: 2,
                    color: '#4b565b',
                    // color: 'source',
                    // curveness: 0.3
                },
            },
            // 动画
            // animationDuration: 1500,
            // animationEasingUpdate: 'quinticInOut',

            // 不显示边的标签
            /*edgeLabel: {
                normal: {
                    show: true,
                    formatter: function (x) {
                        return x.data.name;
                    }
                }
            },*/
            // 边标签
            edgeLabel: {
                // show: true,
                // formatter: function (x) {
                //     return x.data.des;
                // },
                normal: {
                    show: true,
                    formatter: function (x) {
                        return x.data.des;
                    }
                }
            },
            label: {
                show: true,
                textStyle: {},
                position: 'right'
            },
            // labelLayout: {
            //     hideOverlap: true //自动隐藏重叠标签
            // },
            
            // 数据
            // data: nodes,
            // 节点过多，按权重高低进行截取，前 200
            data: nodes.sort(function (a,b) {
                return b.symbolSize - a.symbolSize;
            }).slice(0,150),

            links: links,
            categories: categories,
        }]
    };

    myChart.setOption(option);

    // 为 echarts 图表添加点击事件
    myChart.on('click', function(param) {
        // console.log(param);
        // console.log(param.data.name); //获取热词名称
        // window.open(href + 'whole?query='+ param.data.name);

        if(param.dataType == 'node') {
            router.push({
                path: "/whole",
                query: {
                    query: param.data.name
                }
            })
        }
        
    });

}

export default {
        data () {
            return {
                stockCode:decodeURI(this.$route.query.stockCode),
                json: {}
            }
        },
        mounted() {
            this.getData();
            // setTimeout(() => {
            //     this.graph(this.json)
            // }, 500)
        },
        methods: {
            graph2,
            async getData () {
                let {data} = await this.$get(
                    // "http://121.46.19.26:8288/ForeSee/allInfo/" + this.stockCode
                    "http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode
                )

                // 本地测试
                // let  {data}  = await this.$get("/data/detail.json");
                console.log('竞争者关系图')
                console.log(data.compete)

                this.json = data.compete;

                if(JSON.stringify(this.json)=="{}") {
                    alert('竞争者关系图数据为空！')
                    var myChart = echarts.init(document.getElementById('mainCompete'));
                    var option = {
                        // title: {
                        //     text: '主要竞争者',
                        //     subtext: '数据暂无',
                        //     // left: 'center'
                        // },
                        title: {
                            text: '数据暂无',
                            left: 'center'
                        },
                    };
                    myChart.setOption(option);
                } else {
                    // 画图
                    this.graph2(this.json);
                }

                
            }
        }
    }
</script>

<style scoped>
    #mainCompete {
        width: 100%;
        height: 400px;
        border: 1px solid #EBEEF5;
        margin: 0 auto;
        /* margin-top: 60px; */
    }
</style>