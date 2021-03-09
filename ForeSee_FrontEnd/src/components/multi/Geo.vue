<template>
  <div class="content">
    <div class="amap-wrapper">
      <el-amap
        class="amap-box"
        :zoom="zoom"
        :center="center"
        :mapStyle="mapStyle"
      >
        <!-- <el-amap-marker
          v-for="(marker, index) in markers"
          :position="marker.position"
          :key="index"
          :events="marker.events"
          :title="marker.title"
          :label="marker.label"
        ></el-amap-marker> -->

        <el-amap-marker
          v-for="(marker, index) in markers"
          :position="marker.position"
          :key="index"
          :events="marker.events"
        ></el-amap-marker>

        <el-amap-info-window
          v-if="window"
          :position="window.position"
          :visible="window.visible"
          :content="window.content"
          :offset="window.offset"
        ></el-amap-info-window>
      </el-amap>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      href: "", //用于点击跳转
      query:decodeURI(this.$route.query.query),
      // industryCode: "BK0420",
      //用来接收获取到的raw data
      markers_data: [],
      //center, zoom, mapStyle, windows, window, marker, markers是amap固定的参数，不能随意修改名字
      center: [121.539693, 31.126667], //地图中心点坐标
      zoom: 5, //初始化地图显示层级
      mapStyle: "normal", //设置地图样式, 还有dark等模式
      windows: [], //所有信息窗体的数组
      window: "", //一个信息窗体
      markers: [] //所有地点标志的数组
    };
  },
  methods: {
    async point() {
      // let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/industryInfo/" + this.industryCode);
      /* 上面是旧的接口，下面是新的接口 */
      let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/industryInfo/" + this.query);
      this.markers_data = data.geo;
      let windows = [];
      let markers = [];
      let that = this;
      //将获取到的raw data解析为amap可以读取的format
      //position, events等都是amap固定的，不能擅自修改
      this.markers_data.forEach((item, index) => {
        markers.push({
          position: [item.lng, item.lat],
          events: {
            click() {
              // 测试代码
              // alert(item.stock_code);
              // window.location.href='http://localhost:8888/#/detail?stockCode='+item.stock_code;
              // window.open('/detail?stockCode='+item.stock_code);
              // console.log(that.href + 'detail?stockCode='+ item.stock_code);

              // 打开新标签，要用 that 指代父对象
              window.open(that.href + 'detail?stockCode='+ item.stock_code);

              // 先提示用户，是否打开新页面
              // that.open(item.stock_code);

              that.windows.forEach((window) => {
                window.visible = false; //关闭窗体
              });
              that.window = that.windows[index];
              that.$nextTick(() => {
                that.window.visible = true; //点击点坐标，出现信息窗体
              });
            },
            mouseover () {
              // 鼠标移近点标记marker时触发
              that.windows.forEach((window) => {
                window.visible = false; //关闭窗体
              });
              that.window = that.windows[index];
              that.$nextTick(() => {
                that.window.visible = true; //出现信息窗体
              });
            }
          },
          // title: item.company_name + '\n股票代码：' + item.stock_code,
          // 标签
          // label: {
          //   content: item.company_name,
          //   offset: [0,35]
          // }
        });
        windows.push({
          position: [item.lng, item.lat],
          content: "<div>" + item.company_name + "<br/>股票代码：" + item.stock_code + "</div>", //内容
          offset: [0, -40], //窗体偏移
          visible: false, //初始是否显示
        });
      });
      //  加位置点
      this.markers = markers;
      //  加弹窗
      this.windows = windows;
      //  默认显示第一个窗体
      this.windows[0].visible = true;
      this.window = this.windows[0];
    },
    // 方法2，获取当前 url，用于点击 marker 跳转
    getURL () {
      this.href = window.location.href;
      let pos = this.href.indexOf("#");
      if(pos > -1)
          this.href = this.href.substring(0, pos+2);
      // console.log(this.href);
    },
    // 方法3，先提示用户，是否跳转
    open(stock_code) {
        this.$confirm('将打开该公司详情页, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 用户点击'确定'
          window.open(this.href + 'detail?stockCode='+ stock_code);
        })
    }
  },
  mounted() {
    this.point();
    setTimeout(this.getURL(), 1000);
  },
};

</script>

<style scoped>
.amap-wrapper {
  width: 100%;
  height: 600px;
  margin: 0 auto;
  margin-bottom: 30px;
  margin-top: 60px;
}
.amap-wrapper {
  box-shadow: 10px 10px 10px rgba(0,0,0,.5)
}

</style>

<style>
/* 必须定义成全局样式，否则无法覆盖（？） */
.amap-logo {
  z-index: 2 !important;
}
.amap-copyright {
  z-index: 2 !important;
}

/* 修改高德地图的标签样式 */
.amap-marker-label{ 
    position: absolute; 
    z-index: 2; 
    border: 1px solid #ccc; 
    background-color: white; 
    white-space: nowrap; 
    cursor: default; 
    padding: 3px; 
    font-size: 12px; 
    line-height: 14px; 
}

    /* .amap-logo {
            display: none!important; ;
     } 
   .amap-copyright {
          bottom:-100px;
           display: none!important;;
    }   */

</style>
