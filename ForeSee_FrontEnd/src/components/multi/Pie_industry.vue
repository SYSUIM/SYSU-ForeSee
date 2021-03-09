<template>
    <div id="pieIndustry"></div>
</template>

<script>
var echarts = require('echarts')
import router from '../../router/index'

function graph(json){
    var myChart = echarts.init(document.getElementById('pieIndustry'));
    //myChart.showLoading();

    var numberFormat2 = function (value) {
        // 数据处理：50000000（元） -> 5000.00（万元）
        var change;
        var str, str1, str2, str3;
        str = Math.floor(value).toString()
        if (str.length > 4) {
            str1 = str.substring(0, str.length - 4); //单位：万元
            str2 = str.substring(str.length - 4, str.length); //处理千位和百位
            if (str2.length == 0) {
                str3 = str1 + "." + 0;
            } else {
                str3 = str1 + "." + str2.substring(0, 2);
            }
            change=str3;
        } else { //不足1万元
            str1 = str.substring(0, 1);
            str2 = "0." + str1.substring(0, 1);
            change=str2;
        }
        return change;
    };

    json = json.pieIndustry.map(item => {
        return {
            name: item.name,
            value: numberFormat2(item.value)
        }
    })

    json[0] = {
        name: json[0].name,
		value: json[0].value,
		selected: true //为扇形增加一个属性，让它凸现出来
    }

var option = {
    title: {
        // text: '行业排名 Top5',
        subtext: '单位：万元'
    },
    tooltip: {
        trigger: 'item',
        formatter: {}
    },
    toolbox: {
            show: true,
            feature: {
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                },
                dataView: {
                    show: true
                }
            }
    },
    legend: {
        orient: 'vertical',
        top: 'bottom',
        left: 'left'
    },
    series: [
        {
            name: '市场份额',
            type: 'pie',
            selectedMode: 'single',
            label: {
                // position: 'inner',
                // fontSize: 12,
                // formatter: '{b}：{c}（{d}%）',
                formatter: '{b}：{d}%',
            },
            radius: '50%',
            data: json,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

    myChart.setOption(option);

    // 为 echarts 图表添加点击事件
    myChart.on('click', function(param) {
        // console.log(param);
        // console.log(param.data.name); //获取热词名称
        // window.open(href + 'whole?query='+ param.data.name);

        if(param.data.name != "其他") {
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
                query: decodeURI(this.$route.query.query),
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
                    "http://121.46.19.26:8288/ForeSee/industryInfo/" + this.query
                )
                
                // 本地测试
                // let  {data}  = await this.$get("/data/行业前五名饼图.json");
                // console.log('饼图数据')
                // console.log(data)

                this.json = data;

                // 画图
                this.graph(this.json);
            }
        },
        watch: {
            json () {
                this.graph(this.json)
            }
        }
    }
</script>

<style scoped>
    #pieIndustry {
        width: 100%;
        height: 400px;
        border: 1px solid #EBEEF5;
        margin: 0 auto;
        /* margin-top: 60px; */
        padding: 0 auto;
    }
</style>