<template>
  <!-- <div class="content"> -->
    <div class="amap-wrapper">
      <el-amap
        class="amap-box"
        :zoom="zoom"
        :center="center"
        :mapStyle="mapStyle"
      >
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
  <!-- </div> -->
</template>
<script>
export default {
  // props: ["geo"],
  data() {
    return {
      stockCode: decodeURI(this.$route.query.stockCode),
      //用来接收获取到的raw data
      markers_data: {},
      //center, zoom, mapStyle, windows, window, marker, markers是amap固定的参数，不能随意修改名字
      center: [121.539693, 31.126667], //地图中心点坐标
      zoom: 3, //初始化地图显示层级
      mapStyle: "normal", //设置地图样式, 还有dark等模式
      windows: [], //所有信息窗体的数组
      window: "", //一个信息窗体
      markers: [], //所有地点标志的数组
      geo: {},
      // animation: "AMAP_ANIMATION_BOUNCE"
    };
  },
  methods: {
    async getData () {
      let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode);
      this.geo = data.geo[0];
      // this.graph();
    },
    async graph () {
      while(JSON.stringify(this.geo)=="{}")
        setTimeout(() => {
          this.getData();
        }, 1000);
        // this.getData();
      let windows = [];
      let markers = [];
      let that = this;
      //将获取到的raw data解析为amap可以读取的format
      //position, events等都是amap固定的，不能擅自修改
      // this.markers_data.forEach((item, index) => {

      markers.push({
        position: [this.geo.lng, this.geo.lat],
        events: {
          click() {
            that.windows.forEach((window) => {
              window.visible = false; //关闭窗体
            });
            that.window = that.windows[0];
            that.$nextTick(() => {
              that.window.visible = true; //点击点坐标，出现信息窗体
            });
          },
        },
      });
      windows.push({
        position: [this.geo.lng, this.geo.lat],
        content:
          '<div class="my-content1">' + this.geo.company_name + "</div>" 
          + '<div class="my-content2">' + "股票代码：" + this.geo.stock_code + "</div>", //内容
        offset: [0, -40], //窗体偏移
        visible: false, //初始是否显示
      });
      // });
      //  加位置点
      this.markers = markers;
      //   加弹窗
      this.windows = windows;
      // console.log("企业地图>",this.geo)
    },
    async point() {
      let { data } = await this.$get(
        "http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode
      );
      // let { data } = await this.$get("/data/page3_expect.json");
      // let { data } = await this.$get("/data/page3_expect.json");
      this.geo = data.geo[0];
      // this.markers_data = data.geo;
      // this.markers_data=this.geo;
      // console.log(this.geo.lng)
      // console.log(this.geo);

      let windows = [];
      let markers = [];
      let that = this;
      //将获取到的raw data解析为amap可以读取的format
      //position, events等都是amap固定的，不能擅自修改
      // this.markers_data.forEach((item, index) => {

      markers.push({
        position: [this.geo.lng, this.geo.lat],
        events: {
          click() {
            that.windows.forEach((window) => {
              window.visible = false; //关闭窗体
            });
            that.window = that.windows[0];
            that.$nextTick(() => {
              that.window.visible = true; //点击点坐标，出现信息窗体
            });
          },
        },
      });
      windows.push({
        position: [this.geo.lng, this.geo.lat],
        content:
          '<div class="my-content1">' + this.geo.company_name + "</div>" 
          + '<div class="my-content2">' + "股票代码：" + this.geo.stock_code + "</div>", //内容
        offset: [0, -40], //窗体偏移
        visible: false, //初始是否显示
      });
      // });
      //  加位置点
      this.markers = markers;
      //   加弹窗
      this.windows = windows;
    },
  },
  created () {
      this.point();
      // 将 point() 方法拆解为 getData() 方法和 graph() 方法
      // this.getData();
  },
  watch: {
    // geo () {
    //   this.graph();
    // },
    // stockCode () {
    //   this.getData();
    // }
  }
};
</script>

<style scoped>
.amap-wrapper {
  width: 100%;
  height: 300px;
  margin: 0 auto;
  margin-bottom: 30px;
  padding: 5px;
  border: 1px solid #EBEEF5;
}

</style>

<style>
    .amap-logo {
        z-index: 2 !important;
    }
    div.my-content1 {
        font-size: 14px;
        font-weight: 600;
    }
    div.my-content2 {
        font-size: 14px;
    }
</style>