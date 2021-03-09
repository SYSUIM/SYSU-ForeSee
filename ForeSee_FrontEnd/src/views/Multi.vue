<template>
  <div data-spy="scroll" data-target="#navbar-example">

    <!-- LOAD PAGE -->
    <div class="animationload">
        <div class="loader"></div>
    </div>

    <!-- BACK TO TOP SECTION -->
    <BackTop></BackTop>
    <!-- <Header></Header> -->

  <div>
    <!-- Header -->
    <div class="header header-1 sticky-header">
    <!-- MIDDLE BAR -->
    <div class="middlebar d-none d-sm-block">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-3 col-md-3">
            <div class="logo">
              <a href="index.html">
              <img src="../assets/images/logo-black.png" alt="" width="100%" />
              </a>
            </div>
          </div>
          
          <div class="col-9 col-md-9">
            <div class="contact-info">
              <div class="rs-icon-1">
                <div class="icon">
                  <a href="index.html">
                  <div class="fas fa-home"></div>
                  </a>
                </div>
                <div class="body-content">
                  <a href="index.html">
                  <div class="heading">HOME</div>
                  </a>
                </div>
              </div>
              <!-- INFO 1 -->
              <div class="rs-icon-1">
                <div class="icon">
                    <div class="fas fa-envelope"></div>
                </div>
                <div class="body-content">
                    <div class="heading">Email Support :</div>
                    info@foresee.com
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

    <!-- BANNER -->
    <!-- <div class="section banner-page" data-background="../assets/images/banner-bg.png"> -->
    <div class="section banner-page backgroundImage">
        <div class="content-wrap pos-relative">
            <div class="container">
                <div class="col-12 col-md-12">
                    <div class="d-flex bd-highlight mb-2">
                        <div class="title-page" :data="industryInfo">{{ industryInfo.industry }}</div>
                    </div>
                    <nav aria-label="breadcrumb">
                      <ol class="breadcrumb ">
                        <!-- <li class="breadcrumb-item">该行业下共有  <span> 20 </span>  家上市企业   ...</li> -->
                        <li class="breadcrumb-item">该行业下共有  <span :data="totalCompanies"> {{ totalCompanies }} </span>  家上市企业</li>
                      </ol>
                    </nav>
                </div>
            </div>

        </div>
    </div>
  
  </div>

    <!-- CONTENT -->
    <div id="class" class="">
        <div class="content-wrap">
            <div class="container">

                      <!-- <div class="single-page"> -->
                          <div class="widget-title">
                              行业简介 <span>Introduction</span>
                        </div>
                      <!-- </div> -->

                      <!-- 简介的词数大于120 -->
                      <div v-if="over120">
                        <p class="introduction" v-show="flag" :data="describe_arr">{{ describe_arr }}
                            <a href="javascript:void(0)" @click="toggle" class="toggle">展开</a>
                        </p>
                        <p class="introduction" v-show="!flag" :data="industryInfo">{{ industryInfo.describe }}
                            <a href="javascript:void(0)" @click="toggle" class="toggle">收起</a>
                        </p>
                      </div>

                      <!-- 简介的词数小于120 -->
                      <div v-if="!over120">
                        <p class="introduction" :data="industryInfo"> {{ industryInfo.describe }} </p>
                      </div>

                        <!-- <Geo></Geo> -->
                        <BmapTest></BmapTest>
                        <LoadList :industry_code="industryInfo.industry_code"></LoadList>
                        <Report :industry_code="industryInfo.industry_code"></Report>

                        <div class="widget-title widget-title-3">
                            行业排名 <span>Top 5</span>
                        </div>
                        <PieIndustry></PieIndustry>
                        

            </div>
        </div>
    </div>

    <CTA></CTA>

    <!-- FOOTER SECTION -->
    <Footer></Footer>

  </div>

</template>

<script src="../../public/js/vendor/modernizr.min.js"></script>
<!-- JS VENDOR -->
<!--script src="../../public/js/vendor/jquery.min.js"></script-->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="../../public/js/vendor/bootstrap.min.js"></script>
<script>$('.collapse').collapse()</script>
<script src="../../public/js/vendor/owl.carousel.js"></script>
<script src="../../public/js/vendor/jquery.magnific-popup.min.js"></script>
<!-- SENDMAIL -->
<script src="../../public/js/vendor/validator.min.js"></script>
<script src="../../public/js/vendor/form-scripts.js"></script>
<script src="../../public/js/script.js"></script>

<script>
// @ is an alias to /src
import Header from "@/components/Header";
import BackTop from "@/components/BackTop"
import Footer from "@/components/Footer";
import CTA from "@/components/CTA";
// import Geo from "@/components/multi/Geo";
import LoadList from "@/components/multi/LoadList";
import Report from "@/components/multi/Report";
import PieIndustry from "@/components/multi/Pie_industry"
import BmapTest from "@/components/multi/BmapTest"

export default {
  name: 'Detail',
  components: {
    Header,
    BackTop,
    Footer,
    CTA,
    // Geo,
    LoadList,
    Report,
    PieIndustry,
    BmapTest
  },
  data() {
      return {
          query: decodeURI(this.$route.query.query),
          // query: "BK0420",
          industryInfo: {},    //公司基本详情，包括名称、股票代码、简介等
          describe_arr: "",    //缩略版的企业简介
          flag: true,          //配合 toggle() 方法，用于控制企业简介的展开与折叠
          totalCompanies: 105, //该行业下的公司数目
          over120: false       //行业简介
      };
  },
  created() {
      //console.log(this.query);
      this.getData();
  },
  methods: {
    async getData () {
            let {data} = await this.$get(
                "http://121.46.19.26:8288/ForeSee/industryInfo/" + this.query
            )

            // console.log('行业详情页')
            // console.log(data)

            // 本地测试
            // let  {data}  = await this.$get("/data/multi.json");
            // console.log(data)
            this.industryInfo = data.IndustryInfo
            if(data.IndustryInfo.describe.length >= 122) {
              this.over120 = true;
              this.describe_arr = data.IndustryInfo.describe.slice(0,120) + "..."
            }
            this.totalCompanies = data.totalCompanies
        },
    toggle () {
        //变换 flag, 用于控制企业简介的展开与折叠
        this.flag = !this.flag
    }
  }
}
</script>

<style scoped>
/* 头部 */
.header {
    /* position: fixed; */
    height: 100px;
    width: 100%;
    background-color: rgba(255, 255, 255) !important;
    z-index: 999;
    /* 阴影 */
    box-shadow: 0px 7px 7px rgba(0,0,0,.3);
    transition: all .2s;
}
.sticky-header {
  position: sticky;
  top: 0;
}
.backgroundImage{
  background-image: url('../assets/images/banner-bg.png');
  background-attachment:fixed; 
  background-repeat:no-repeat;
  width:calc(100%);
  height: calc(100%);
}
.introduction {
    font-size: 16px;
    text-indent: 0em;
}
.introduction::first-letter {
    font-size: 30px;
    color: #FFD808; 
    float: left;
}
.toggle {
    color: #FFD808;
}
.widget-title-3 {
    padding-top: 80px;
}
</style>



