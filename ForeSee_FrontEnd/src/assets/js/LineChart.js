var echarts = require('echarts')
function graph(ret){
    var myChart = echarts.init(document.getElementById('main'));
    myChart.showLoading();
    var numberFormat = function (value) {
        var change = [];
        var str, str1, str2, str3;
        for (var i = 0; i < value.length; i++) {
            str = Math.floor(value[i]).toString()
            if (str.length > 4) {
                str1 = str.substring(0, str.length - 4); //单位：万元
                str2 = str.substring(str.length - 4, str.length); //处理千位和百位
                if (str2.length == 0) {
                    str3 = str1 + "." + 0;
                } else {
                    str3 = str1 + "." + str2.substring(0, 2);
                }
                change.push(str3);

            } else { //不足1万元
                str1 = str.substring(0, 1);
                str2 = "0." + str1.substring(0, 1);
                change.push(str2);
            }
        }
        return change;
    };
    myChart.hideLoading();
    var option = {
        // title: [{
        //     text: '公司营收情况'
        // }],
        tooltip: {
            trigger: 'axis',
            axisPointer: { //出现标度和刻线
                type: 'cross',
                snap: true //自动吸附到点上
            },
            formatter: function(params){ //自定义tooltip的内容，参数params个数关联series数组的个数
                var res=params[0].name.slice(0,4)+'年'+params[0].name.slice(4,6)+'月'+params[0].name.slice(6,8)+'日'
                res+='</br>'+params[0].marker+params[0].seriesName+'：'+params[0].data
                for(var i=1;i<params.length;i++){
                    if(params[i].data.length!=0 && Number(params[i].data) != Number(params[i-1].data))
                        res+='</br>'+params[i].marker+params[i].seriesName+'：'+params[i].data
                }
                if(params[0].name.slice(0,4) == '2020' || params[0].name.slice(0,4) == '2021') {
                    res += '</br>'+'---------'+'</br>'
                            +'<span style="font-weight: 600;">'+'预测指标及权重'+'</span>'
                            // +'</br>'+'负债：53.8%' //+ret.income[0]
                            // +'</br>'+'盈利：27.9%'
                            // +'</br>'+'成本：10.5%'
                            +'</br>'+'舆论资讯：' + ret.explain[0]
                            +'</br>'+'历史指标：' + ret.explain[1]
                            +'</br>'+'企业信息：' + ret.explain[2]
                }
                return res;
            }
        },
        legend: {
            data: ['营业收入', '营业成本', '营业利润', '利润总额', '净利润'],
            selected: {
                '营业收入': true,
                '营业成本': true,
                '营业利润': false,
                '利润总额': false,
                '净利润': true,
            }
        },
        grid: {
            left: '6%',
            right: '8%',
            bottom: '10%',
            top: '15%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ret.time.reverse(),
            name: '时间'
        },
        yAxis: {
            type: 'value',
            name: '金额（万元）'
        },
        dataZoom: [{
            startValue: '20110630'
        }, {
            type: 'inside'
        }],
        series: [{
                name: '营业收入',
                type: 'line',
                data: numberFormat(ret.income.slice(1).reverse())
            },
            {
                name: '营业收入',
                type: 'line',
                smooth: false, //设置为虚线。实线则true
                lineStyle: {
                    width: 2,
                    type:'dotted' //dotted虚线
                },
                data:["","","","","","","","",""].concat(numberFormat(ret.income.slice(0,2).reverse())),
                // symbol: 'pin',
                symbol: function(x) {
                    // console.log(x);
                    if( x == numberFormat(ret.income.slice(0,1)))
                        return 'pin' // 标记
                    else
                        return 'none' // 空心
                },
                // symbolSize: 20,
                symbolSize: function(x) {
                    if( x == numberFormat(ret.income.slice(0,1)))
                        return 20;
                },
            },
            {
                name: '营业成本',
                type: 'line',
                data: numberFormat(ret.expense.slice(1).reverse())
            },
            {
                name: '营业成本',
                type: 'line',
                smooth: false, //设置为虚线。实线则true
                lineStyle: {
                    width: 2,
                    type:'dotted' //dotted虚线
                },
                data:["","","","","","","","",""].concat(numberFormat(ret.expense.slice(0,2).reverse())),
                // symbol: 'pin',
                symbol: function(x) {
                    // console.log(x);
                    if( x == numberFormat(ret.expense.slice(0,1)))
                        return 'pin' // 标记
                    else
                        return 'none' // 空心
                },
                // symbolSize: 20,
                symbolSize: function(x) {
                    if( x == numberFormat(ret.expense.slice(0,1)))
                        return 20;
                },
            },
            {
                name: '营业利润',
                type: 'line',
                data: numberFormat(ret.profit.slice(1).reverse())
            },
            {
                name: '营业利润',
                type: 'line',
                smooth: false, //设置为虚线。实线则true
                lineStyle: {
                    width: 2,
                    type:'dotted' //dotted虚线
                },
                data:["","","","","","","","",""].concat(numberFormat(ret.profit.slice(0,2).reverse())),
                // symbol: 'pin',
                symbol: function(x) {
                    // console.log(x);
                    if( x == numberFormat(ret.profit.slice(0,1)))
                        return 'pin' // 标记
                    else
                        return 'none' // 空心
                },
                // symbolSize: 20,
                symbolSize: function(x) {
                    if( x == numberFormat(ret.profit.slice(0,1)))
                        return 20;
                },
            },
            {
                name: '利润总额',
                type: 'line',
                data: numberFormat(ret.total_profit.slice(1).reverse())
            },
            {
                name: '利润总额',
                type: 'line',
                smooth: false, //设置为虚线。实线则true
                lineStyle: {
                    width: 2,
                    type:'dotted' //dotted虚线
                },
                data:["","","","","","","","",""].concat(numberFormat(ret.total_profit.slice(0,2).reverse())),
                // symbol: 'pin',
                symbol: function(x) {
                    // console.log(x);
                    if( x == numberFormat(ret.total_profit.slice(0,1)))
                        return 'pin' // 标记
                    else
                        return 'none' // 空心
                },
                // symbolSize: 20,
                symbolSize: function(x) {
                    if( x == numberFormat(ret.total_profit.slice(0,1)))
                        return 20;
                },
            },
            {
                name: '净利润',
                type: 'line',
                data: numberFormat(ret.net_profit.slice(1).reverse())
            },{
                name: '净利润',
                type: 'line',
                smooth: false, //设置为虚线。实线则true
                lineStyle: {
                    width: 2,
                    type:'dotted' //dotted虚线
                },
                data:["","","","","","","","",""].concat(numberFormat(ret.net_profit.slice(0,2).reverse())),
                // symbol: 'pin',
                symbol: function(x) {
                    // console.log(x);
                    if( x == numberFormat(ret.net_profit.slice(0,1)))
                        return 'pin' // 标记
                    else
                        return 'none' // 空心
                },
                // symbolSize: 20,
                symbolSize: function(x) {
                    if( x == numberFormat(ret.net_profit.slice(0,1)))
                        return 20;
                },
                markArea: {
                    itemStyle: {
                        // color: 'rgba(255, 173, 177, 0.4)'
                        color: 'rgba(0, 0, 180, 0.1)'
                    },
                    data: [ 
                    [{
                        name: '预测值',
                        xAxis: '20200630'
                    }, {
                        xAxis: '20210630'
                    }]
                    ]
                }
            }
        ]
    }
    myChart.setOption(option);
    
}

export{
    graph
}