<template>
  <div data-spy="scroll" data-target="#navbar-example">

    <!-- LOAD PAGE -->
    <div class="animationload">
        <div class="loader"></div>
    </div>

    <!-- BACK TO TOP SECTION -->
    <BackTop></BackTop>

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
                        <div class="title-page" :data="companyInfo">{{ companyInfo.former_name }}</div>
                    </div>
                    <nav aria-label="breadcrumb">
                      <ol class="breadcrumb ">
                        <li class="breadcrumb-item" :data="companyInfo">股票代码：{{ companyInfo.stock_code }}</li>
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
                <div class="row">
                <el-row>

                    <!-- <div class="col-sm-12 col-md-12 col-lg-8"> -->

                    <!-- <div class="col-lg-8"> -->
                    <el-col :span="18">

                        <div class="single-page">
                            <div class="widget-title">
                                公司简介 <span>Introduction</span>
                            </div>
                        </div>

                        <!-- 下面这种写法好像移动端不适配，换！ -->
                        <p class="introduction" v-show="flag" :data="describe_arr">{{ describe_arr }}
                            <a href="javascript:void(0)" @click="toggle" class="toggle">展开</a>
                        </p>
                        <p class="introduction" v-show="!flag" :data="companyInfo">{{ companyInfo.describe }}
                            <a href="javascript:void(0)" @click="toggle" class="toggle">收起</a>
                        </p>


                        <!-- <div class="single-page"> -->
                            <TableBasic></TableBasic>
                            <LineChart></LineChart>
                            <!-- <LineChart :jsonObj="profit"></LineChart> -->
                            <PieIndustrySingle></PieIndustrySingle>
                            <Radar></Radar>

                            <RelationCompete></RelationCompete>
                            <RelationCooperate></RelationCooperate>
                            <!-- <WordCloudEchartsTest></WordCloudEchartsTest> -->
                            <SunInterest></SunInterest>

                            <!-- 气泡图测试：带时间轴 -->
                            <!-- <BubbleTimelineTest></BubbleTimelineTest> -->

                            <!-- 树图 -->
                            <!-- <TreeTest></TreeTest> -->

                            <!-- <BmapSingle></BmapSingle> -->

                            

                        
                        <!-- </div> -->

                    </el-col>
                    <!-- </div> -->

                    <!-- <div class="col-sm-12 col-md-12 col-lg-4 pleft"> -->
                    <el-col :span="4">

                        <div class="widget widget-text" :data="contact">
                            <div class="widget-title">
                                联系方式 <span>Contact</span>
                            </div>
                            <div class="row mb-3">
                                <div class="col-3">地址 :</div>
                                <div class="col-9">{{ contact.address }}</div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-3">电话 :</div>
                                <div class="col-9">{{ contact.office }}</div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-3">传真 :</div>
                                <div class="col-9">{{ contact.fax }}</div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-3">邮箱 :</div>
                                <div class="col-9">{{ contact.email }}</div>
                            </div>

                        </div>

                        <div class="widget widget-text">
                            <div class="widget-title">
                                企业地图 <span>Map</span>
                            </div>
                            <GeoSingle :geo="geo"></GeoSingle>
                            <!-- <BmapSingle></BmapSingle> -->

                        </div>

                        <!-- 新闻、公告、研究报告 -->
                        <NewsList :companyName="companyInfo.former_name"></NewsList>
                        <ResearchList :companyName="companyInfo.former_name"></ResearchList>
                        <NoticeTab :companyName="companyInfo.former_name"></NoticeTab>
                        <!-- <NoticesList :companyName="companyInfo.former_name"></NoticesList> -->
                        
                        
                        
                        <!-- 词云 -->
                        <WordCloudEchartsTest></WordCloudEchartsTest>


                        

                    </el-col>
                    <!-- </div> -->

                </el-row>
                </div>
            </div>
        </div>
    </div>

    <el-row :gutter="20">
    <el-col :span="16">
    
    </el-col>
    <el-col :span="8">
    <BmapSingle></BmapSingle>
    </el-col>
    </el-row>
    

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
import BackTop from "@/components/BackTop"
import Footer from "@/components/Footer";
import CTA from "@/components/CTA";
import TableBasic from '@/components/detail/TableBasic.vue';
import LineChart from '../components/LineChart';
import GeoSingle from '../components/GeoSingle';
import BmapSingle from '@/components/graphs/Bmap_single'
import NewsList from '../components/NewsList';
import RelationCompete from '@/components/graphs/Relation_compete'
import RelationCooperate from '@/components/graphs/Relation_cooperate'
import PieIndustrySingle from '@/components/graphs/Pie_industry_single'
// import SunInterest from '@/components/graphs/Sun_interest'
import SunInterest from '@/components/graphs/Sun_interest_timeline'
// import NoticesList from '@/components/detail/NoticesList'
import Radar from '@/components/graphs/Radar'
import WordCloudEchartsTest from '@/components/wordclouds/WordCloudEchartsTest'
import NoticeTab from '@/components/detail/NoticeTab'
import ResearchList from '@/components/detail/ResearchList'
// import BubbleTimelineTest from '@/components/graphs/Bubble_timeline_test'
// import TreeTest from '@/components/graphs/TreeTest'

export default {
  name: 'Detail',
  components: {
    BackTop,
    Footer,
    CTA,
    TableBasic,
    LineChart,
    Radar,
    GeoSingle,
    BmapSingle,
    RelationCompete,
    RelationCooperate,
    PieIndustrySingle,
    SunInterest,
    // NoticesList,
    NoticeTab,
    NewsList,
    ResearchList,
    WordCloudEchartsTest,
    // BubbleTimelineTest,
    // TreeTest
  },
  data() {
      return {
          stockCode:decodeURI(this.$route.query.stockCode),
          companyInfo: {},    //公司基本详情，包括名称、股票代码、简介等
        //   profit: {},         //营收折线图
          contact: {},        //企业联系方式
          describe_arr: "",   //缩略版的企业简介
          flag: true,         //配合 toggle() 方法，用于控制企业简介的展开与折叠
          geo: {}             //地图
      };
  },
  created() {
      //console.log(this.stockCode);
      this.getData();
  },
  methods: {
    async getData () {
            let {data} = await this.$get(
                // 老接口，包含动态和静态数据，已被关闭
                // "http://121.46.19.26:8288/ForeSee/allInfo/" + this.stockCode
                // 新接口，只包含企业的静态数据
                "http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode
            )
            console.log('全数据：')
            console.log(data)

            // 本地测试
            // let  {data}  = await this.$get("/data/detail.json");
            // console.log(data)

            this.companyInfo = data.companyInfo
            this.contact = data.contact
            this.describe_arr = data.companyInfo.describe.slice(0,120) + "..."
            // this.profit = data.profit
            // 获取图表数据
            this.geo = data.geo[0]
        },
    toggle () {
        //变换 flag, 用于控制企业简介的展开与折叠
        this.flag = !this.flag
    }
  }
}
</script>

<style src="../../public/css/vendor/bootstrap.min.css"></style>
<style src="../../public/css/vendor/owl.carousel.min.css"></style>
<style src="../../public/css/vendor/owl.theme.default.min.css"></style>
<style src="../../public/css/vendor/magnific-popup.css"></style>
<style src="../../public/css/vendor/animate.min.css"></style>
<style src="../../public/css/fontawesome.css"></style>
<style src="../../public/css/brands.css"></style>
<style src="../../public/css/solid.css"></style>
<style src="../../public/css/style.css"></style>
<style scoped>
/* 头部 */
.header {
    height: 100px;
    width: 100%;
    background-color: rgba(255, 255, 255) !important;
    z-index: 99999;
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

</style>
