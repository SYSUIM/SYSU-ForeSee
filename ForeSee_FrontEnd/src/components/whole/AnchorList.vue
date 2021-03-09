<template>
  <div class="anchorList">

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
              <img src="../../assets/images/logo-black.png" alt="" width="100%" />
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
                        <div class="title-page" :data="query">{{ query }}</div>
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
    
    <!-- <div class="my-content-wrap container"> -->
    <!-- <div class="my-content-wrap container"> -->
      <!-- <Knowledge></Knowledge> -->
      <KnowledgeTable style="width: 850px;"></KnowledgeTable>
    <!-- </div> -->
    <div class="my-content-wrap" :id="'anchor-whole-title'">
      <div class="container">
        
    <!-- flex 属性可以使同行的两列等高 -->
    <el-row :gutter="20" type="flex">
      
      <el-col :span="20">
        <div :id="'anchor-industry-title'">
          <div class="widget-title">
            相关行业 <span>Industry</span>
          </div>
          <SwiperTest @listenToChildren="changeIndustryRecords"></SwiperTest>
        </div>

        <div :id="'anchor-company-title'">
          <div class="widget-title-pd">
            相关企业 <span>Company</span>
          </div>
          <Company @listenToChildren="changeCompanyRecords"></Company>
        </div>

        <div :id="'anchor-text-title'">
          <div class="widget-title-pd">
            资讯 <span>Information</span>
          </div>
          <TextPart @listenToChildren="changeTextRecords"></TextPart>
        </div>

      </el-col>
      <el-col :span="4">
        <!-- 测试用 -->
        <!-- <div><a href="javascript:void(0)" rel="external nofollow" @click="goAnchor('#anchor-'+'industry-title')">行业检索</a></div>
        <div><a href="javascript:void(0)" rel="external nofollow" @click="goAnchor('#anchor-company-title')">企业检索</a></div>
        <div><a href="javascript:void(0)" rel="external nofollow" @click="goAnchor('#anchor-text-title')">新闻检索</a></div> -->


        <div style="height: 200px;" class="my-aside-nav sticky">
          <el-steps direction="vertical" :active="active">
            <el-step title="行业检索" icon="el-icon-menu" :status="active==1?'finish':'wait'" @click.native="goAnchor('#anchor-'+'industry-title')"></el-step>
            <el-step title="企业检索" icon="el-icon-trophy" :status="active==2?'finish':'wait'" @click.native="goAnchor('#anchor-company-title')"></el-step>
            <el-step title="资讯检索" icon="el-icon-document" :status="active==3?'finish':'wait'" @click.native="goAnchor('#anchor-text-title')"></el-step>
            <!-- <el-step title="新闻检索" description="这是一段很长很长很长的描述性文字"></el-step> -->
          </el-steps>
          
        </div>
      </el-col>
    </el-row>
      </div>
    </div>
  </div>
</template>
<script>
import SwiperTest from "@/components/whole/SwiperTest"
import Company from "@/components/whole/Company"
import TextPart from "@/components/whole/Text"
import { scrollAnimation } from '../../util/smoothScroll'; // 用于平滑滚动的函数
// import Knowledge from "@/components/whole/Knowledge";
import KnowledgeTable from "@/components/whole/KnowledgeTable";

export default {
    components: {
      SwiperTest,
      Company,
      TextPart,
      // Knowledge,
      KnowledgeTable
    },
    data() {
      return {
        query: decodeURI(this.$route.query.query),
        active: 1,

        //通过子组件向父组件传值的方式进行修改
        industryRecords: 0,
        companyRecords: 0,
        textRecords: 0,
      };
    },
    computed: {
      whole_offsettop: function () {
        return (this.$el.querySelector('#anchor-whole-title').offsetTop)
      },
      industry_offsettop: function () {
        return (this.$el.querySelector('#anchor-industry-title').offsetTop)
      },
      company_offsettop: function () {
        return (this.$el.querySelector('#anchor-company-title').offsetTop)
      },
      text_offsettop: function () {
        return (this.$el.querySelector('#anchor-text-title').offsetTop)
      },
      // 总记录数 = 行业 + 企业 + 文本
      totalRecords () {
          var result = 0;
          if(this.industryRecords > 0)
            result += this.industryRecords;
          if(this.companyRecords > 0)
            result += this.companyRecords;
          if(this.textRecords > 0)
            result += this.textRecords;
          return result;
      }
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
            // document.documentElement.scrollTop = (anchor.offsetTop + this.whole_offsettop)

            // 方式二：平滑滚动。分为向上滚和向下滚两种情况
            let scrolled = document.documentElement.scrollTop || document.body.scrollTop;
            let goal = anchor.offsetTop + this.whole_offsettop;
            scrollAnimation(scrolled,goal);

            
            if(selector == '#anchor-industry-title')
              this.active = 1;
            if(selector == '#anchor-company-title')
              this.active = 2;
            if(selector == '#anchor-text-title')
              this.active = 3;
        },
        onScroll () {
          let scrolled = document.documentElement.scrollTop || document.body.scrollTop
          // 609、1640分别为第二个和第三个锚点对应的距离
          // 静态数字，可能会出 bug
          // 在组件中计算容器的高度，动态设置
          // if (scrolled >= this.text_offsettop + 280) {
          if (scrolled >= this.text_offsettop + this.whole_offsettop-5) {
            this.active = 3
          // } else if (scrolled < (this.text_offsettop + 280) && scrolled >= (this.company_offsettop + 280)) {
          } else if (scrolled < (this.text_offsettop + this.whole_offsettop) && scrolled >= (this.company_offsettop + this.whole_offsettop)) {
            this.active = 2
          } else {
            this.active = 1
          }
        },
        changeIndustryRecords (data) { 
            // data 就是刚刚从子组件传过来的值
            this.industryRecords = data;
        },
        changeCompanyRecords (data) { 
            // data 就是刚刚从子组件传过来的值
            this.companyRecords = data;
        },
        changeTextRecords (data) { 
            // data 就是刚刚从子组件传过来的值
            this.textRecords = data;
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
      background-image: url('../../assets/images/banner-bg.png');
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

    /* main */
    .my-content-wrap {
      padding: 80px 0px 0px;
    }
  .widget-title-pd {
    font-size: 21px;
    font-weight: 700;
    color: #000000;
    font-family: "Ubuntu", sans-serif;
    margin-top: 50px;
    margin-bottom: 30px;
    position: relative;
  }
  .widget-title-pd span {
    color: #FFD808; 
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
