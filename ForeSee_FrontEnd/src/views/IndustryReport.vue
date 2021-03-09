<template>
  <div>
    <BackTop></BackTop>

  <div>
    <div class="header sticky-header">
    <!-- MIDDLE BAR -->
    <div class="middlebar d-none d-sm-block" :class="flag?'middlebar-fixed':''">
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
    <!-- <Header></Header> -->

    <!-- BANNER -->
    <div class="banner-page backgroundImage">
        <div class="content-wrap pos-relative">
            <div class="container">
                <div class="col-12 col-md-12">
                    <div class="d-flex bd-highlight mb-2">
                        <div class="title-page">行业资讯</div>
                    </div>
                    <nav aria-label="breadcrumb">
                      <ol class="breadcrumb ">
                        <li class="breadcrumb-item">共有  <span :data="totalRecords"> {{ totalRecords }} </span>  篇相关行业资讯</li>
                      </ol>
                    </nav>
                </div>
            </div>
        </div>
    </div>

  </div>

    <div class="section" :id="'anchor-section'">
      <IndustryReportComponent></IndustryReportComponent>
      <CTA></CTA>
      <Footer></Footer>
    </div>
  </div>
</template>
<script>
import BackTop from "@/components/BackTop"
// import Header from "@/components/Header";
import CTA from "@/components/CTA";
import IndustryReportComponent from "@/components/IndustryReportComponent";
import Footer from "@/components/Footer";


export default {
  components: {
    BackTop,
    // Header,
    CTA,
    Footer,
    IndustryReportComponent
  },
  data() {
    return {
      industryCode: decodeURI(this.$route.query.industryCode),
      totalRecords: 11,
      newslist: {},
      query: "互联网",
      myScrollTop: 0,     //用于计算页面高度
      flag: false,
    };
  },
  computed: {
      section_offsettop: function () {
        return (this.$el.querySelector('#anchor-section').offsetTop)
      }
  },
  methods: {
    async getData() {
        // let { data } = await this.$get("/data/Internet_article_info.json");
        let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/report/"+ this.industryCode + "/1");
        this.totalRecords = data.totalRecords;
        // console.log(data)
        
    },
    // 监听页面高度变化
    onScroll () {
        let scrolled = document.documentElement.scrollTop || document.body.scrollTop
        this.myScrollTop = scrolled;
        if (scrolled <= this.section_offsettop)
            this.flag = true;
        else 
            this.flag = false;
    }
    
  },
  created() {
    console.log(this.industryCode)
    this.getData()
  },
  mounted () {
      // 监听页面滚动事件，根据滚动条的距离修改锚点状态
      // this.$nextTick(function() {
      //   window.addEventListener('scroll', this.onScroll)
      // })
  }
};
</script>

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
    /* banner 部分 */
    .backgroundImage{
        background-image: url('../assets/images/banner-bg.png');
        background-attachment:fixed; 
        background-repeat:no-repeat;
        width:calc(100%);
        height: calc(100%);
    }
    /* .banner-page {
        padding-top: 50px;
    } */
    /* 其他 */
    .section {
        background-color: #fff;
        z-index: 8;
       
    }

</style>