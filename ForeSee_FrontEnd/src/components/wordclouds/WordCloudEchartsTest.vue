<template>
    <div class="cloud-wrap">
        <div class="widget-title">
            舆情分析 <span>Public Sentiment</span>
        </div>
        <div class="my-card-box-2">
        <!-- <el-card class="card-box" shadow="hover"> -->
          <div class="cloud-box" ref="cloudEl"></div>
        <!-- </el-card> -->
        </div>
    </div>

</template>

<script>
var echarts = require('echarts')
import 'echarts-wordcloud';

import $router from '../../router/index'

import ccdata from '../../../public/data/词云 - 副本.json'
export default {
  props: {},
  data() {
    return {
      stockCode:decodeURI(this.$route.query.stockCode),
      cloudData: ccdata
    };
  },
  mounted() {
    this.getData();
    // 不知道为什么调用下面的方法会反应迟钝，所以就直接扔进 getData 方法里了
    // setTimeout(this.drawCloud(this.$refs.cloudEl, this.cloudData), 3000);
    // this.drawCloud(this.$refs.cloudEl, this.cloudData);
  },
  methods: {
    async getData () {
        let {data} = await this.$get("http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode)

                // 本地测试
                // let  {data}  = await this.$get("/data/词云.json");
                
        let arr = [];
        let max = 0; // 最高词频
        let min = 9999; //最低词频
        for(var i=0; i<data.wordCloud.length; i++) {
          let myValue = data.wordCloud[i].count;
          arr.push({
            name: data.wordCloud[i].word,
            // value: myValue
            value: Math.ceil(myValue)
          })
          if (myValue < min) {
            min = myValue;
          }
          if (myValue > max) {
            max = myValue;
          }
        }
        this.cloudData = arr;

        console.log('词云：', this.cloudData)

        // this.cloudData = data.wordCloud;
        // this.drawCloud(this.$refs.cloudEl, this.cloudData);

        if(max ==  min) // 否则在visualMap里会出错，刻度置灰
          max = min + 4;
        this.drawCloud(this.$refs.cloudEl, this.cloudData, max, min);
    },
    drawCloud(wrapEl, data, max, min) {
      let myChart = echarts.init(wrapEl);
      
      var option = {
        tooltip: {
          show: true,
        },
        visualMap: {
          min: min,
          max: max,
          inRange: {
            color: ['#2F93C8', '#AEC48F', '#FFDB5C', '#F98862']
          },
          itemWidth: 8,
          itemHeight: 200,
          left: 'right',
          text: ['高频词','低频词'],
          textStyle: {
            overflow: 'breakAll', // 截断
          },
          hoverLink: true
        },
        series: [
          {
            name: "热词",
            type: "wordCloud",
            layoutAnimation: true,
            // sizeRange: [10, 30],
            // rotationRange: [-20, 20],
            rotationRange: [0, 0],
            drawOutOfBound: false, //是否允许词绘制在canvas之外
            shape: 'circle', // 自带的形状，可选circle（默认）、square、triangle、star、pentagon、triangle-forward 等
            // left: "center",
            top: "center",
            // width: "100%",
            left: "0%",
            width: "85%",
            height: "100%",
            // height: "80%",
            gridSize: 4, // 词间距
            textPadding: 0,
            autoSize: {
              enable: true,
              minSize: 6,
            },
            // 更换字体颜色
            textStyle: {
              fontFamily: 'sans-serif',
              // fontWeight: 'bold',
              // Color can be a callback function or a color string
              // color: function () {
              //   // Random color
              //   return 'rgb(' + [
              //       Math.round(Math.random() * 160),
              //       Math.round(Math.random() * 160),
              //       Math.round(Math.random() * 160)
              //   ].join(',') + ')';
              // }
            },
            emphasis: {
              focus: 'self',
              textStyle: {
                shadowBlur: 10,
                shadowColor: '#333'
              }
            },
            data: data,
          },
        ],
      };
      myChart.setOption(option);

      // 获取url
      var href = window.location.href;
      let pos = href.indexOf("#");
      if(pos > -1)
        href = href.substring(0, pos+2);

      // 为 echarts 图表添加点击事件
      myChart.on('click', function(param) {
        // console.log(param);
        // console.log(param.data.name); //获取热词名称
        // window.open(href + 'whole?query='+ param.data.name);

        $router.push({
          path: "/whole",
          query: {
            query: param.data.name
          }
        })


      });
      
    },
  }
};


</script>

<style scoped>
    .cloud-wrap {
      margin-top: 60px;
      width: 100%;
      height: 600px;
    }

    
    .card-box {
      width: 100%;
      /* height: 666px; */
      text-align: left;
    }
    .my-card-box-2 {
      width: 100%;
      border: 1px solid #EBEEF5;
      border-radius: 5px;
      height: 260px;
    }
    .cloud-box {
      width: 100%;
      /* height: 100%; */
      /* height: 220px; */
      height: 260px;
    }
    
</style>

<style>
    /* div.el-card__body {
      padding: 0px !important;
    } */
</style>