<template>
    <div id="mainCooperate"></div>
</template>

<script>
var echarts = require('echarts')

import router from '../../router/index'

function graph(json){
    var myChart = echarts.init(document.getElementById('mainCooperate'));
    //myChart.showLoading();

    // 数据重组格式 1
    var nodes = [];
    var links = [];
    nodes.push({
        name: json.name,
        category: 0,
        symbolSize: 50,
        symbol: "path://M564.525 927.659V435.451a77.701 77.701 0 0 1 22.765-54.952 77.692 77.692 0 0 1 54.951-22.764h155.435a77.696 77.696 0 0 1 54.952 22.764 77.703 77.703 0 0 1 22.764 54.952v492.208H564.525z m233.15-440.397c0-14.305-11.597-25.904-25.908-25.904-14.305 0-25.903 11.598-25.903 25.904v51.812c0 14.305 11.597 25.908 25.903 25.908 14.311 0 25.908-11.603 25.908-25.908v-51.812z m0 181.339c0-14.305-11.597-25.904-25.908-25.904-14.305 0-25.903 11.598-25.903 25.904v51.812c0 14.305 11.597 25.908 25.903 25.908 14.311 0 25.908-11.603 25.908-25.908v-51.812z m0 0 M888.345 927.659H137.083a38.857 38.857 0 0 1-33.655-58.29 38.858 38.858 0 0 1 33.655-19.43h12.954V202.305c0-57.229 46.395-103.624 103.623-103.624h310.865c27.483 0 53.84 10.917 73.274 30.35a103.627 103.627 0 0 1 30.35 73.274v647.634h220.196c21.456 0.005 38.853 17.402 38.853 38.858 0.001 21.462-17.397 38.858-38.853 38.862z m-354.83-621.837c0-14.362-11.751-26.113-26.113-26.113H302.307c-14.362 0-26.113 11.751-26.113 26.113s11.751 26.113 26.113 26.113h205.096c14.361-0.001 26.112-11.752 26.112-26.113z m0 148.144c0-14.362-11.751-26.113-26.113-26.113H302.307c-14.362 0-26.113 11.751-26.113 26.113s11.751 26.113 26.113 26.113h205.096c14.361-0.001 26.112-11.751 26.112-26.113z m0 148.144c0-14.362-11.751-26.113-26.113-26.113H302.307c-14.362 0-26.113 11.751-26.113 26.113s11.751 26.113 26.113 26.113h205.096c14.361 0 26.112-11.751 26.112-26.113z",
        des: json.name,
    })
    for(var i=0; i<json.collaborate.length; i++) {
        var x = json.collaborate[i].symbolSize;
        if( x >= 20 )
            x = 50;
        else 
            x = x + 30;
        nodes.push({
            name: json.collaborate[i].name,
            category: 1,
            // symbolSize: json.collaborate[i].symbolSize + 30,
            symbolSize: x,
            symbol: "path://M0.34429 204.8a204.8 204.8 0 1 0 409.6 0 204.8 204.8 0 1 0-409.6 0zM614.74429 204.8a204.8 204.8 0 1 0 409.6 0 204.8 204.8 0 1 0-409.6 0zM819.54429 460.8a204.8 204.8 0 0 0-204.8 181.248l-102.4 102.4-102.4-102.4A204.8 204.8 0 0 0 0.34429 665.6V1024h409.6V789.504l66.048 66.048a51.2 51.2 0 0 0 72.192 0l66.56-66.048V1024h409.6V665.6a204.8 204.8 0 0 0-204.8-204.8z",
            symbolOffset: [0, '-22%'], // 向上移动 22%
            des: json.collaborate[i].name
        })
        links.push({
            source: json.name,
            target: json.collaborate[i].name,
            // des: "蓝宝石"
        })
    }

    // 数据格式 2
    /*
    var nodes = [];
    var links = [];
    nodes.push({
        name: json.name,
        category: 0,
        symbolSize: 50 //为节点增加一个属性，让节点更大一些，默认是20
    })
    for(let key in json.collaborate) {
        var oneNode = {
            name: key,
            category: 1,
            symbolSize: json.collaborate[key] + 10
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
    categories[1] = { name: '合作者' };

    var option = {
        // title: {
        //     text: '合作伙伴'
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
            data: categories.map(function (a) {
                return a.name;
            }),
            top: 'bottom'
        }],
        // legend: {
        //     orient: 'vertical',
        //     top: 'bottom',
        //     left: 'left',
        //     data: categories.map(function (a) {
        //         return a.name;
        //     })
        // },
        series: [{
            type: 'graph', // 类型:关系图
            layout: 'force', //图的布局，类型为力导图
            symbolSize: 20, // 调整节点的大小
            roam: true, // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
            // edgeSymbol: ['circle', 'arrow'],
            edgeSymbolSize: [2, 10],
            force: {
                // repulsion: 2500,
                repulsion: 500,
                // edgeLength: [10, 50],
                layoutAnimation: false // 关闭动画
            },
            draggable: true,
            lineStyle: {
                // normal: {
                    width: 2,
                    color: '#4b565b',
                    // color: 'source',
                    // curveness: 0.3
                // },
            },
            // 动画
            // animationDuration: 1500,
            // animationEasingUpdate: 'quinticInOut',
            // 不显示边的标签
            // edgeLabel: {
            //     // show: true,
            //     // formatter: function (x) {
            //     //     return x.data.des;
            //     // },
            //     normal: {
            //         show: true,
            //         formatter: function (x) {
            //             return x.data.des;
            //         }
            //     }
            // },
            label: {
                show: true,
                textStyle: {},
                position: 'right'
            },
            // labelLayout: {
            //     hideOverlap: true //自动隐藏重叠标签, 如果要加边标签的话，此处不能设置为true，否则会有bug
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

        if(param.dataType == 'node') { // 点击'节点'
            router.push({
                path: "/whole",
                query: {
                    query: param.data.name
                }
            })
        } else {
            // 点击'边'
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
            graph,
            async getData () {
                let {data} = await this.$get(
                    "http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode
                )

                // 本地测试
                // let  {data}  = await this.$get("/data/detail.json");
                // console.log(data)

                this.json = data.cooperate;

                if(JSON.stringify(this.json)=="{}") {
                    alert('合作者关系图数据为空！')
                    var myChart = echarts.init(document.getElementById('mainCooperate'));
                    var option = {
                        // title: {
                        //     text: '合作伙伴',
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
                    this.graph(this.json);
                }
                
            }
        }
    }
</script>

<style scoped>
    #mainCooperate {
        width: 100%;
        height: 400px;
        border: 1px solid #EBEEF5;
        margin: 0 auto;
        /* margin-top: 60px; */
    }
</style>