<template>
  <div data-spy="scroll" data-target="#navbar-example">

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
    <div class="section banner-page backgroundImage">
        <div class="content-wrap pos-relative">
            <div class="container">
                <div class="col-12 col-md-12">
                    <div class="d-flex bd-highlight mb-2">
                        <div class="title-page" :data="query"> {{ query }} </div>
                    </div>
                    <nav aria-label="breadcrumb">
                      <ol class="breadcrumb ">
                        <li class="breadcrumb-item">共有  <span :data="totalRecords"> {{ totalRecords }} </span>  条相关结果</li>
                      </ol>
                    </nav>
                </div>
            </div>

        </div>
    </div>

  </div>

    <!-- CONTENT -->
        <!-- <div class="content-wrap"> -->
        <div class="content-wrap container" :id="'anchor-begin-title'">
          <el-row :gutter="20" type="flex">
            <el-col :span="20">
            <!-- <div class="container"> -->
                <LoadList :id="'anchor-news-title'" @news-records="changeNewsRecords"></LoadList>
                <LoadNoticeList :id="'anchor-notice-title'" @notice-records="changeNoticeRecords"></LoadNoticeList>
                <LoadInformationList :id="'anchor-information-title'" @information-records="changeInformationRecords"></LoadInformationList>
            <!-- </div> -->
            </el-col>
            <el-col :span="4">
              <div style="height: 200px;" class="my-aside-nav sticky">
                <el-steps direction="vertical" :active="active">
                  <el-step title="企业新闻" icon="el-icon-menu" :status="active==1?'finish':'wait'" @click.native="goAnchor('#anchor-'+'news-title')"></el-step>
                  <el-step title="企业公告" icon="el-icon-trophy" :status="active==2?'finish':'wait'" @click.native="goAnchor('#anchor-notice-title')"></el-step>
                  <el-step title="行业资讯" icon="el-icon-document" :status="active==3?'finish':'wait'" @click.native="goAnchor('#anchor-information-title')"></el-step>
                </el-steps>
              </div>
            </el-col>
          </el-row>
        </div>

    <!-- FOOTER SECTION -->
    <CTA></CTA>
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
import LoadList from "@/components/text-analysis/LoadList";
import LoadNoticeList from "@/components/text-analysis/LoadNoticeList";
import LoadInformationList from "@/components/text-analysis/LoadInformationList";
import { scrollAnimation } from '../util/smoothScroll'; //用于平滑滚动的函数

export default {
  name: 'TextAnalysis',
  components: {
    BackTop,
    Footer,
    CTA,
    LoadList,
    LoadNoticeList,
    LoadInformationList
  },
  data() {
      return {
        query: decodeURI(this.$route.query.query),
        active: 1,
        //通过子组件向父组件传值的方式进行修改
        newsRecords: 0,
        noticeRecords: 0,
        informationRecords: 0,
        

      };
  },
  computed: {
      // 用来计算锚点
      begin_offsettop: function () {
        return (this.$el.querySelector('#anchor-begin-title').offsetTop)
      },
      news_offsettop: function () {
        return (this.$el.querySelector("#anchor-news-title").offsetTop)
      },
      notice_offsettop: function () {
        return (this.$el.querySelector("#anchor-notice-title").offsetTop)
      },
      information_offsettop: function () {
        return (this.$el.querySelector('#anchor-information-title').offsetTop)
      },
      // 总记录数 = 新闻 + 公告 + 资讯
      totalRecords () {
        var result = 0;
        if(this.newsRecords > 0)
          result += this.newsRecords;
        if(this.noticeRecords > 0)
          result += this.noticeRecords;
        if(this.informationRecords > 0)
          result += this.informationRecords;
        return result;
      }
  },
  created() {
  },
  methods: {
    goAnchor(selector) {
            // console.log(selector)
            var anchor = this.$el.querySelector(selector)
            // console.log(anchor)
            // alert(anchor.offsetTop)
            // 加上 banner 的距离
            // alert(Number(anchor.offsetTop)+280)
            // document.documentElement.scrollTop = (anchor.offsetTop + 280)

            // 方式一：直接前往
            // document.documentElement.scrollTop = (anchor.offsetTop + this.begin_offsettop)

            // 方式二：平滑滚动。分为向上滚和向下滚两种情况
            let scrolled = document.documentElement.scrollTop || document.body.scrollTop;
            let goal = anchor.offsetTop + this.begin_offsettop;
            scrollAnimation(scrolled,goal);


            if(selector == '#anchor-news-title')
              this.active = 1;
            if(selector == '#anchor-notice-title')
              this.active = 2;
            if(selector == '#anchor-information-title')
              this.active = 3;
    },
    onScroll () {
          let scrolled = document.documentElement.scrollTop || document.body.scrollTop;
          // 609、1640分别为第二个和第三个锚点对应的距离
          // 静态数字，可能会出 bug
          // 在组件中计算容器的高度，动态设置
          // if (scrolled >= this.text_offsettop + 280) {
          if (scrolled >= this.information_offsettop + this.begin_offsettop-5) {
            this.active = 3
          // } else if (scrolled < (this.text_offsettop + 280) && scrolled >= (this.company_offsettop + 280)) {
          } else if (scrolled < (this.information_offsettop + this.begin_offsettop) && scrolled >= (this.notice_offsettop + this.begin_offsettop)) {
            this.active = 2
          } else {
            this.active = 1
          }
    },
    async getData () {
      let {data} = await this.$get(
        "http://121.46.19.26:8288/ForeSee/industryIndex/" + this.stockCode
      )
    },
    changeNewsRecords (data) { 
      // data 就是刚刚从子组件传过来的值
      this.newsRecords = data;
    },
    changeNoticeRecords (data) { 
      // data 就是刚刚从子组件传过来的值
      this.noticeRecords = data;
    },
    changeInformationRecords (data) { 
      // data 就是刚刚从子组件传过来的值
      this.informationRecords = data;
    }
    
  },
  mounted () {
      // 监听页面滚动事件，根据滚动条的距离修改锚点状态
      this.$nextTick(function() {
        window.addEventListener('scroll', this.onScroll)
      })
  }
}
</script>


<style scoped>
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
/* .banner-page {
    padding-top: 50px;
} */

    /* 侧边栏，锚点 */
    .my-aside-nav {
      margin-left: 20px;
    }
    div.sticky {
      position: -webkit-sticky;
      position: sticky;
      top: 10%;
      /* top: 20%; */
      /* top: 30%; */
    }
    el-step:hover {
      color: #FFD808 !important;
    }

</style>

<style>
    /* 显示手形 */
    div.el-step__title:hover {
      cursor: pointer !important;
    }
    i.el-step__icon-inner {
      cursor: pointer !important;
    }
</style>

