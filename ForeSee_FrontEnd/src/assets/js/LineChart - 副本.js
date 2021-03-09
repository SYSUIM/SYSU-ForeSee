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
                str1 = str.substring(0, str.length - 4);
                str2 = str.substring(str.length - 4, str.length);
                if (str2.length == 0) {
                    str3 = str1 + "." + 0;
                } else {
                    str3 = str1 + "." + str2.substring(0, 2);
                }
                change.push(str3);

            } else {
                str1 = str.substring(0, 1);
                str2 = "0." + str1.substring(0, 1);
                change.push(str2);
            }
        }
        return change;
    };
    myChart.hideLoading();
    var option = {
        title: {
            text: '公司营收情况'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['营业收入', '营业成本', '营业利润', '利润总额', '净利润']
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
                data: numberFormat(ret.income.reverse())
            },
            {
                name: '营业成本',
                type: 'line',
                data: numberFormat(ret.expense.reverse())
            },
            {
                name: '营业利润',
                type: 'line',
                data: numberFormat(ret.profit.reverse())
            },
            {
                name: '利润总额',
                type: 'line',
                data: numberFormat(ret.total_profit.reverse())
            },
            {
                name: '净利润',
                type: 'line',
                data: numberFormat(ret.net_profit.reverse())
            }
        ]
    }
    myChart.setOption(option);
    
}

export{
    graph
}