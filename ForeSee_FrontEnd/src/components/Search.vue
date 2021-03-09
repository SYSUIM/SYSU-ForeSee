<template>
  <div id="home" class="banner">
    <!-- <div class="owl-carousel owl-theme full-screen"> -->
    <div class="item">
      <div class="overlay-bg"></div>
      <div class="container d-flex align-items-center h-center">
        <div class="wrap-caption">
          <h1 class="caption-heading text-white">
            Deep insight and quality service
          </h1>
          <el-tabs
            :stretch="true"
            v-model="activeName"
            @tab-click="handleClick"
          >
            <el-tab-pane label="企业/Enterprise" name="first"
              ><search-box1 class="center search-box1" />
              <div class="suggestion">
                <span class="title">Try:</span>
                <span class="content">
                  <div
                    v-for="(suggestion, index) in search_suggestion1"
                    :key="index"
                  >
                    <router-link
                      :to="{ path: '/retrieval', query: { query: suggestion } }"
                      tag="a"
                      >{{ suggestion }}</router-link
                    >
                  </div>
                </span>
              </div></el-tab-pane
            >
            <el-tab-pane label="行业/Industry" name="second"
              ><search-box2 class="center search-box2" />
              <div class="suggestion">
                <span class="title">Try:</span>
                <span class="content">
                  <div
                    v-for="(suggestion, index) in search_suggestion2"
                    :key="index"
                  >
                    <!-- <router-link
                      :to="{ path: '/industry', query: { query: suggestion } }"
                      tag="a"
                      >{{ suggestion }}</router-link
                    > -->
                    <router-link
                      :to="{ path: '/multi', query: { query: suggestion } }"
                      tag="a"
                      >{{ suggestion }}</router-link
                    >
                  </div>
                </span>
              </div></el-tab-pane
            >
            <el-tab-pane label="新闻/News" name="third"
              ><search-box3 class="center search-box3" />
              <div class="suggestion">
                <span class="title">Try:</span>
                <span class="content">
                  <div
                    v-for="(suggestion, index) in search_suggestion3"
                    :key="index"
                  >
                    <router-link
                      :to="{ path: '/news', query: { query: suggestion } }"
                      tag="a"
                      >{{ suggestion }}</router-link
                    >
                  </div>
                </span>
              </div></el-tab-pane
            >
          </el-tabs>
          <!--a href="#" class="btn btn-secondary">Contact Us</a-->
        </div>
      </div>
    </div>
  </div>

  <!-- </div> -->
</template>

<script>
import SearchBox1 from "./SearchBox1.vue";
import SearchBox2 from "./SearchBox2.vue";
import SearchBox3 from "./SearchBox3.vue";
export default {
  components: {
    "search-box1": SearchBox1,
    "search-box2": SearchBox2,
    "search-box3": SearchBox3,
  },
  data() {
    return {
      search_suggestion1: ["蓝思科技","重庆燃气","新天绿色能源股份有限公司"],
      search_suggestion2: ["玻璃陶瓷","煤炭采选","民航机场","高速公路"],
      search_suggestion3: ["商誉","玻璃","移动"],
      activeName: "first",
    };
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
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

<style src="../../public/css/vendor/bootstrap.min.css"></style>
<style src="../../public/css/vendor/owl.carousel.min.css"></style>
<style src="../../public/css/vendor/owl.theme.default.min.css"></style>
<style src="../../public/css/vendor/magnific-popup.css"></style>
<style src="../../public/css/vendor/animate.min.css"></style>
<style src="../../public/css/fontawesome.css"></style>
<style src="../../public/css/brands.css"></style>
<style src="../../public/css/solid.css"></style>
<style src="../../public/css/style.css"></style>


