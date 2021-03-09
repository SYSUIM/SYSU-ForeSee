<template>
  <div id="home" class="banner">
    <!-- <div class="item"> -->
      <!-- <div class="overlay-bg"></div> -->

      <!-- <div class="container d-flex align-items-center h-center"> -->
      <div class="container h-center">
        <div class="wrap-caption">
          <h1 class="caption-heading text-white">
            Deep insight and quality service
          </h1>
          <!-- <h3 class="caption-heading text-white">
          </h3>
          <h2 class="text-white">
            商业信息可视化平台
          </h2>
          <h4 class="text-white">
            用科技预见未来
          </h4> -->

          <search-box1 class="center search-box1" />
          <div class="suggestion">
            <span class="title">热门搜索:</span>
            <span class="content">
              <div v-for="(suggestion, index) in search_suggestion1" :key="index">
                <router-link :to="{ path: '/whole', query: { query: suggestion } }" tag="a">
                  {{ suggestion }}
                </router-link>
              </div>
            </span>
          </div>
            
        </div>
      </div>
    <!-- </div> -->
  </div>

</template>

<script>
import SearchBoxWhole from "@/components/SearchBoxWhole";
export default {
  components: {
    "search-box1": SearchBoxWhole
  },
  data() {
    return {
      search_suggestion1: ["芯片","材料","国产 大飞机","软件服务","医疗行业","海尔智家股份有限公司","蓝思科技的成立时间是"]
    };
  },
};
</script>

<style>
.banner .inner {
  position: absolute;
  top: 100;
  left: 0;
  right: 0;
  background-color: (0, 0, 0, 0.5);
}
.banner#home{
  height: 700px;
  /* overflow: hidden; */
  padding: 0 0;
  position: relative;
}
.el-tabs__content{
  height: 400px;
}
.banner .overlay-bg {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  /* 把背景图换成滚动的了，具体改动在 index.vue中 */
  /* background-image: url("../assets/images/banner-bg.png");
  background-repeat: no-repeat;
  background-size: cover; */
  background-color: rgba(0, 0, 0, 0.1) !important;
  z-index: 1; /* 较为底层 */
}
.banner .suggestion {
  font-size: 1em;
  margin: 1em;
  color: #ffffff;
}
.banner .suggestion .title {
  display: inline-block;
  margin: 0 1em 0 0.5em;
  font-weight: 700;
}
.banner .suggestion .content div {
  display: inline;
  font-weight: 700;
}
.banner .suggestion a {
  display: inline-block;
  text-decoration: none;
  text-transform: capitalize;
  margin: 0 0.5em;
  color: #ffffff;
}
.banner .suggestion a:hover {
  text-decoration: underline;
  color: #ffd808;
}
.d-flex {
  display: -ms-flexbox !important;
  display: flex !important;
}
.align-items-center {
  -ms-flex-align: center !important;
  align-items: center !important;
}
.banner .h-center > .wrap-caption {
  width: 80%;
  margin: 0 auto;
  text-align: center;
}
.banner .wrap-caption {
  width: 60%;
  position: relative;
  z-index: 2;
}
.banner .wrap-caption .caption-heading {
  font-size: 80px;
  margin-bottom: 20px;
  margin-top: 100px;
  font-family: "Ubuntu", sans-serif;
  font-weight: 900;
  /* color: #080700; */
  color: #080700;
  line-height: 1;
}

.el-tabs__header {
  margin-left: auto;
  margin-right: auto;
  width: 50%;
}

.el-tabs__item {
  font-size: 1.1em;
  font-weight: bold;
  color: #ffffff;
}

.el-tabs__item.is-active {
  color: #ffd808;
  border-bottom-color: #ffd808;
}

.el-tabs__item:hover {
  color: #ffd808;
}
.el-tabs__active-bar {
  background-color: #ffd808;
}
</style>
