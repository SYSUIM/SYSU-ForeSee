<template>
  <div class="home-container">
  <!-- <div class="home-container" v-loading.fullscreen="fullscreenLoading"> -->
    <!-- LOAD PAGE -->
    <div class="animationload">
        <div class="loader"></div>
    </div>
    <!-- BACK TO TOP SECTION -->
    <BackTop></BackTop>

    <!-- 头部区域 -->
    <!-- <div class="header header-1"> -->
    <div class="header">
        <!-- 顶部 -->
        <!-- <div class="middlebar d-none d-sm-block">
            <div class="container">
                <div class="row align-items-center">
                    <div class="col-3 col-md-3">
                        <div class="logo">
                            <img src="images/logo-black.png" alt="" width="100%" />
                        </div>
                    </div>
                    <div class="col-9 col-md-9">
                        <div class="contact-info">
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
        </div> -->
        <!-- 导航栏 -->
        <!-- <div class="navbar-main">
            <div class="container">
                <nav id="navbar-example" class="navbar navbar-expand-lg navbar-bg">
                    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNavDropdown">
                        <ul class="navbar-nav">
                            <li class="nav-item">
                                <a class="nav-link" href="index.html">Home</a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" href="#section">About</a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" href="#footer">Contact</a>
                            </li>

                        </ul>
                    </div>
                </nav> 

            </div>
        </div> -->

    </div>

    <div class="header-nav">
        <el-row class="my-el-row">
            <el-col :span="17">
                <img src="../../public/images/logo-black.png" alt="" height="55vh"/>
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-search')" :class="active==1?'active':''">主页
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-about')" :class="active==2?'active':''">简介
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-services')" :class="active==3?'active':''">产品
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-technique')" :class="active==4?'active':''">技术
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-display')" :class="active==5?'active':''">展示
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-team')" :class="active==6?'active':''">团队
            </el-col>
            <el-col :span="1" class="nav-title" @click.native="goAnchor('#anchor-contact')" :class="active==7?'active':''">联系
            </el-col>
        </el-row>
    </div>
    
    <div class="backgroundImage" :id="'anchor-search'">
        <SearchSingle></SearchSingle>
    </div>

    <div class="others">
        <!-- ABOUT介绍栏 -->
    <div class="section" :id="'anchor-about'">
        <div class="content-wrap-140 backgroundWhite">
        <!-- <div class="content-wrap"> -->
            <div class="container">
                <div class="row">
                    <div class="col-sm-4 col-md-4">
                        <h4 class="section-heading text-black no-after mb-3">
                            About <span class="text-primary">Us</span>
                        </h4>
                        <h3 class="text-black">
                            通过洞察、事实和数据，为您提供最新最全的行业资讯与企业信息。
                        </h3>
                        <p>ForeSee —— 助你抢占先机</p>
                        <!--a href="#" class="btn btn-secondary">Our projects</a-->
                        <div class="spacer-30"></div>
                    </div>
                    <div class="col-sm-8 col-md-8 pleft">
                        <div class="img-subsco">
                            <img src="../assets/images/index-wordcloud.png" alt="" class="img-fluid">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- SERVICES -->
    <div class="section bg-gray-light" :id="'anchor-services'">
        <!-- <div class="content-wrap pb-0"> -->
        <div class="content-wrap-140">
            <div class="container">

                <div class="row">
                    <div class="col-sm-12 col-md-12">
                        <h4 class="section-heading text-center text-black no-after mb-5">
                            产品服务 <span class="text-primary">Services</span>
                        </h4>
                    </div>
                </div>

                <div class="row">

                    <div class="col-sm-4 col-md-4">
                        <div class="rs-icon-info-3 mb-5">
                            <div class="info-icon">
                                <i class="fas fa-building"></i>
                            </div>
                            <div class="info-text">
                                <h4 class="text-black mb-2">企业竞争力评测</h4>
                                <p>提供公司近十年营业收入、营业成本、营业利润、净利润等数据，据此对未来一年的发展情况进行预测。</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4 col-md-4">
                        <div class="rs-icon-info-3 mb-5">
                            <div class="info-icon">
                                <i class="fas fa-comments"></i>
                            </div>
                            <div class="info-text">
                                <h4 class="text-black mb-2">历史数据可视化</h4>
                                <p>了解企业全部的新闻和公告，动态监控行业资讯，支持全文检索和分析。</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4 col-md-4">
                        <div class="rs-icon-info-3 mb-5">
                            <div class="info-icon">
                                <i class="fas fa-cube"></i>
                            </div>
                            <div class="info-text">
                                <h4 class="text-black mb-2">竞争合作关系网络</h4>
                                <p>了解公司在业内的主要竞争者和合作伙伴</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4 col-md-4">
                        <div class="rs-icon-info-3 mb-5">
                            <div class="info-icon">
                                <i class="fas fa-home"></i>
                            </div>
                            <div class="info-text">
                                <h4 class="text-black mb-2">行业知识图谱</h4>
                                <p>分析行业技术动向，构建行业知识图谱</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4 col-md-4">
                        <div class="rs-icon-info-3 mb-5">
                            <div class="info-icon">
                                <i class="fas fa-brush"></i>
                            </div>
                            <div class="info-text">
                                <h4 class="text-black mb-2">行业可视化分析</h4>
                                <p>行业的头部公司及新兴企业</p>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4 col-md-4">
                        <div class="rs-icon-info-3 mb-5">
                            <div class="info-icon">
                                <i class="fas fa-window-restore"></i>
                            </div>
                            <div class="info-text">
                                <h4 class="text-black mb-2">一框式语义检索</h4>
                                <p>平台便于操作使用，提供基于不同需求的选择检索功能。</p>
                            </div>
                        </div>
                    </div>

                </div>

                
            </div>
        </div>
    </div>


    <!-- 技术 -->
    <!-- <div class="section bgi-cover-center bg-overlay-light section-tech" :id="'anchor-technique'"> -->
    <div class="section bg-overlay-light section-tech" :id="'anchor-technique'">
        <div class="content-wrap-140">
            <div class="container">

                <div class="row">
                    <div class="col-sm-12 col-md-7">
                        <h3 class="text-black">
                            技术支持 <span style="color: #FFD808;">Technique</span>
                        </h3>
                        <p>我们的专业团队致力于用技术为您创造优质服务、巨大价值</p>
                        <div class="row no-gutters">
                            <div class="col-sm-12 col-md-4 border-box">
                                <div class="box-stat-2">
                                    <h2 class="counter">4000+</h2>
                                    <p>上市公司</p>
                                </div>
                            </div>
                            <div class="col-sm-12 col-md-4 border-box">
                                <div class="box-stat-2">
                                    <h2 class="counter">50000+</h2>
                                    <p>行业资讯</p>
                                </div>
                            </div>
                            <div class="col-sm-12 col-md-4 border-box">
                                <div class="box-stat-2">
                                    <h2 class="counter">1000+</h2>
                                    <p>实体标注</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-12 col-md-5">
                        <el-carousel indicator-position="outside">

                            <!-- Item 1 -->
                            <el-carousel-item>
                                <div class="rs-icon-info-1">
                                    <div class="info-icon">
                                        <i class="fas fa-building"></i>
                                    </div>
                                    <div class="info-text">
                                        <h4 class="text-black mb-2 information-title">
                                            Java、MySQL
                                        </h4>
                                        <p>后端</p>
                                    </div>
                                </div>
                            </el-carousel-item>

                            <!-- Item 2 -->
                            <el-carousel-item>
                                <div class="rs-icon-info-1">
                                    <div class="info-icon">
                                        <i class="fas fa-building"></i>
                                    </div>
                                    <div class="info-text">
                                        <h4 class="text-black mb-2 information-title">
                                            MapReducer
                                        </h4>
                                        <p>大数据分析与处理</p>
                                    </div>
                                </div>
                            </el-carousel-item>

                            <!-- Item 3 -->
                            <el-carousel-item>
                                <div class="rs-icon-info-1">
                                    <div class="info-icon">
                                        <i class="fas fa-building"></i>
                                    </div>
                                    <div class="info-text">
                                        <h4 class="text-black mb-2 information-title">
                                            SpringBoot
                                        </h4>
                                        <p>框架</p>
                                    </div>
                                </div>
                            </el-carousel-item>

                            <!-- Item 4 -->
                            <el-carousel-item>
                                <div class="rs-icon-info-1">
                                    <div class="info-icon">
                                        <i class="fas fa-building"></i>
                                    </div>
                                    <div class="info-text">
                                        <h4 class="text-black mb-2 information-title">
                                            Python
                                        </h4>
                                        <p>自然语言处理、语义检索</p>
                                    </div>
                                </div>
                            </el-carousel-item>

                            <!-- Item 5 -->
                            <el-carousel-item>
                                <div class="rs-icon-info-1">
                                    <div class="info-icon">
                                        <i class="fas fa-building"></i>
                                    </div>
                                    <div class="info-text">
                                        <h4 class="text-black mb-2 information-title">
                                            Vue、Echarts
                                        </h4>
                                        <p>前端可视化</p>
                                    </div>
                                </div>
                            </el-carousel-item>

                        </el-carousel>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!-- 经典案例 -->
    <div class="section bg-dark" :id="'anchor-display'">
    <!-- <div class="section my-display-image" :id="'anchor-display'"> -->
        <div class="content-wrap-140">
            <div class="container-fluid">

                <div class="row">
                    <div class="col-sm-12 col-md-12">
                        <h4 class="section-heading text-center text-white no-after mb-5">
                            实例展示 <span class="text-primary">Display</span>
                        </h4>
                    </div>
                </div>

                <el-row :gutter="20">
                    <!-- Item 1 -->
                    <el-col :span="6">
                            <div class="media-img">
                            <!-- <div class="media-img my-blur"> -->
                                <!-- <img src="../../public/images/合作伙伴.png" alt="" class="img-fluid"> -->
                                <!-- <IndexRelation class="my-relation"></IndexRelation> -->
                                <IndexRelation></IndexRelation>
                            </div>
                            <div class="body-content">
                                <div class="text-primary entity-subtitle">竞争合作关系网络</div>
                                <p class="category text-white">为海*公司匹配了100余家竞争者和合作伙伴</p>
                            </div>
                    </el-col>

                    <!-- Item 2 -->
                    <el-col :span="6">
                            <div class="media-img">
                                <img src="../../public/images/公司营收情况.png" alt="" class="img-fluid">
                            </div>
                            <div class="body-content">
                                <div class="text-primary entity-subtitle">公司营收</div>
                                <p class="category text-white">预计公司来年总营收872.54万元，准确度高达97%</p>
                            </div>
                    </el-col>
                    
                    <!-- Item 3 -->
                    <el-col :span="6">
                            <div class="media-img">
                                <img src="../../public/images/行业排名.png" alt="" class="img-fluid">
                            </div>
                            <div class="body-content">
                                <div class="text-primary entity-subtitle">行业排名</div>
                                <p class="category text-white">跻身行业前5名，市场份额16.86%</p>
                            </div>
                    </el-col>
                    
                    <!-- Item 4 -->
                    <el-col :span="6">
                            <div class="media-img">
                                <img src="../../public/images/感兴趣的领域.png" alt="" class="img-fluid">
                            </div>
                            <div class="body-content">
                                <div class="text-primary entity-subtitle">研究热点</div>
                                <p class="category text-white">近三年该公司持续关注科技领域</p>
                            </div>
                    </el-col>
                </el-row>

                
                <router-link :to="{ path: '/detail', query: { stockCode: '600690' } }" target="_blank">
                    <div class="get-all-pic">
                        点击此处快速获取所有分析图表和结果 >>
                    </div>
                </router-link>
                
                
            </div>
        </div>
    </div>    



    <!-- TEAM 团队 -->
    <div class="section section-team" :id="'anchor-team'">
        <div class="content-wrap-140 pb-0">
            <div class="container">

                <div class="row">
                    <div class="col-sm-12 col-md-12">
                        <h4 class="section-heading text-center text-black no-after mb-5">
                            团队成员 <span class="text-primary">Team</span>
                        </h4>
                    </div>
                </div>

                <div class="row team-row">

                    <div class="col-12 col-sm-6 col-md-4">

                        <div class="rs-team-1 mb-5 team-center">
                            <div class="media">
                                <!-- <img src="images/dummy-img-600x600.jpg" alt="" class="img-fluid"> -->
                                <img src="../../public/images/人.png" alt="" class="img-fluid">
                            </div>
                            <div class="body">
                                <div class="title">李岱峰</div>
                                <div class="position">lidaifeng@mail.sysu.edu.cn</div>
                                <p>团队导师</p>
                            </div>
                        </div>

                    </div>

                    <div class="col-12 col-sm-6 col-md-4">

                        <div class="rs-team-1 mb-5 team-center">
                            <div class="media">
                                <img src="../../public/images/人 (1).png" alt="" class="img-fluid">
                            </div>
                            <div class="body">
                                <div class="title">潘子炀</div>
                                <div class="position">123456789@qq.com</div>
                                <p>数据爬取、整合，图数据分析、搜索模型构建</p>
                            </div>
                        </div>

                    </div>

                    <div class="col-12 col-sm-6 col-md-4">

                        <div class="rs-team-1 mb-5 team-center">
                            <div class="media">
                                <!-- 颜色：#dd6572 -->
                                <img src="../../public/images/女人头像.png" alt="" class="img-fluid">
                            </div>
                            <div class="body">
                                <div class="title">曾盈</div>
                                <div class="position">123456789@qq.com</div>
                                <p>大数据架构、词向量匹配、索引构建</p>
                            </div>
                        </div>

                    </div>

                    <div class="col-12 col-sm-6 col-md-4">

                        <div class="rs-team-1 mb-5 team-center">
                            <div class="media">
                                <img src="../../public/images/女人-女性头像.png" alt="" class="img-fluid">
                            </div>
                            <div class="body">
                                <div class="title">陈乐华</div>
                                <div class="position">123456789@qq.com</div>
                                <p>关系抽取、模型构建、数据标注</p>
                            </div>
                        </div>

                    </div>

                    <div class="col-12 col-sm-6 col-md-4">

                        <div class="rs-team-1 mb-5 team-center">
                            <div class="media">
                                <img src="../../public/images/精致女人.png" alt="" class="img-fluid">
                            </div>
                            <div class="body">
                                <div class="title">钟珊珊</div>
                                <div class="position">123456789@qq.com</div>
                                <p>后端数据索引构建，查询系统构建与维护</p>
                            </div>
                        </div>

                    </div>

                    <div class="col-12 col-sm-6 col-md-4">

                        <div class="rs-team-1 mb-5 team-center">
                            <div class="media">
                                <img src="../../public/images/女人1.png" alt="" class="img-fluid">
                            </div>
                            <div class="body">
                                <div class="title">冯雁霞</div>
                                <div class="position">123456789@qq.com</div>
                                <p>前端页面构建、可视化展现</p>
                            </div>
                        </div>

                    </div>

                </div>

                
            </div>
        </div>
    </div>








    <!-- CTA contact us -->
    <div class="section bg-primary" :id="'anchor-contact'">
        <div class="content-wrap-140 py-5">
            <div class="container">
                <div class="row align-items-center cta-block-1">
                    <div class="col-sm-8 col-md-8">
                        <h4 class="my-1 text-black">Don't hesitate to contact us any time</h4>
                    </div>
                </div>
            </div>
        </div>
    </div>

    

    <!-- FOOTER SECTION页尾联系信息 -->
    <Footer></Footer>
    <!-- 路由占位符 -->
    <router-view></router-view>
    </div>

    
 </div>
</template>

<script src="../../public/js/vendor/modernizr.min.js"></script>
<!-- JS VENDOR -->
<script src="../../public/js/vendor/jquery.min.js"></script>
<script src="../../public/js/vendor/bootstrap.min.js"></script>
<script src="../../public/js/vendor/owl.carousel.js"></script>
<script src="../../public/js/vendor/jquery.magnific-popup.min.js"></script>
<!-- SENDMAIL -->
<script src="../../public/js/vendor/validator.min.js"></script>
<script src="../../public/js/vendor/form-scripts.js"></script>
<script src="../../public/js/script.js"></script>

<script>
import BackTop from '@/components/BackTop'
import SearchSingle from '@/components/SearchSingle'
import Footer from "@/components/Footer"
import IndexRelation from "@/components/index/IndexRelation.vue"
import { scrollAnimation } from '../util/smoothScroll'; // 用于平滑滚动的函数

export default {
  components:{
    BackTop,
    SearchSingle,
    Footer,
    IndexRelation
  },
  data() {
    return {
      input: '',
      active: 1, //标记锚点
      // fullscreenLoading: true, // 设置定时的、全屏的加载、3000ms
    };
  },
  computed: {
      // 动态监测视窗位置
      search_offsettop () {
          return (this.$el.querySelector('#anchor-search').offsetTop)
      },
      about_offsettop () {
          return (this.$el.querySelector('#anchor-about').offsetTop)
      },
      services_offsettop () {
          return (this.$el.querySelector('#anchor-services').offsetTop)
      },
      technique_offsettop () {
          return (this.$el.querySelector('#anchor-technique').offsetTop)
      },
      display_offsettop () {
          return (this.$el.querySelector('#anchor-display').offsetTop)
      },
      team_offsettop () {
          return (this.$el.querySelector('#anchor-team').offsetTop)
      },
      contact_offsettop () {
          return (this.$el.querySelector('#anchor-contact').offsetTop)
      },
  },

  methods: {
    trans () {
      setTimeout(() => {
        this.$router.push('/about' + '?query=' + this.input)
      }, 200)
    },
    // 锚点相关
    goAnchor(selector) {
        var anchor = this.$el.querySelector(selector)

        // 方式一：直接前往
        // document.documentElement.scrollTop = (anchor.offsetTop)

        // 方式二：平滑滚动。分为向上滚和向下滚两种情况
        let scrolled = document.documentElement.scrollTop || document.body.scrollTop;
        let goal = anchor.offsetTop;
        scrollAnimation(scrolled,goal);


        if(selector == '#anchor-search')
            this.active = 1;
        if(selector == '#anchor-about')
            this.active = 2;
        if(selector == '#anchor-services')
            this.active = 3;
        if(selector == '#anchor-technique')
            this.active = 4;
        if(selector == '#anchor-display')
            this.active = 5;
        if(selector == '#anchor-team')
            this.active = 6;
        if(selector == '#anchor-contact')
            this.active = 7;
    },
    onScroll () {
        // 检测视窗位置
        let scrolled = document.documentElement.scrollTop || document.body.scrollTop
        if (scrolled < this.about_offsettop -100)
            this.active = 1;
        if (scrolled > this.about_offsettop -100 && scrolled < this.services_offsettop -100)
            this.active = 2;
        if (scrolled > this.services_offsettop -100 && scrolled < this.technique_offsettop -100)
            this.active = 3;
        if (scrolled > this.technique_offsettop -100 && scrolled < this.display_offsettop -100 )
            this.active = 4;
        if (scrolled > this.display_offsettop -100 && scrolled < this.team_offsettop -100)
            this.active = 5;
        if (scrolled > this.team_offsettop -100 && scrolled < this.contact_offsettop -100)
            this.active = 6;
        if (scrolled > this.contact_offsettop -100)
            this.active = 7;
    }
  },

  mounted () {
      this.$nextTick(function() {
          window.addEventListener('scroll', this.onScroll)
      })
    //   setTimeout(() => {
    //       this.fullscreenLoading = false;
    //   }, 3000);
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
    /* 头部锚点导航栏 */
    .header-nav {
        margin: 0 auto;
        width: 100%;
        height: 100px;
        position: fixed;
        background-color: #fff;
        border-top: 4px solid #000;
        z-index: 999;
        /* 阴影 */
        box-shadow: 0px 7px 7px rgba(0,0,0,.3);
        transition: all .2s;

    }
    .my-el-row {
        margin: 0 auto;
        padding: 25px 0px 5px;
        width: 90%;
    }
    .nav-title {
        /* margin-top: 20px; */
        /* margin-bottom: 10px; */
        padding-top: 13px;
        padding-bottom: 5px;
        text-align: center;
        font-family: "Ubuntu", sans-serif;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.5);
    }
    .nav-title:hover {
        color: #000;
        cursor: pointer;
    }
    .active {
        color: #000;
        border-bottom: 3px solid #FFD808;
    }

    /* 搜索框 */
   .backgroundImage {
       padding-top: 70px;
       min-height: 500px;
       background: url('../assets/images/banner-bg.png');
       /* 100%是按照图片大小铺满 cover是根据屏幕大小铺满（可能会截取掉一部分） */
       background-size: cover; 
       /* 是否跟随鼠标一起滑动 */
       background-attachment:fixed;
       /* 是否重复铺满  */
       /* background-repeat:no-repeat; */
       width:calc(100%);
       background-position:center;
       height: calc(50%);
       /* 父元素设置relative 子元素设置absolute 实现相对定位*/
       /* position: relative; */
       z-index: 1;
   }
   .middlebar {
       position: fixed;
       /* height: 100px; */
       height: 60px;
       width: 100%;
       background-color: rgba(255, 255, 255) !important;
       /* z-index: 3; */
       z-index: 999;
       /* 阴影 */
       box-shadow: 0px 7px 7px rgba(0,0,0,.3);
       transition: all .2s;
   }
   .section {
       z-index: 8;   
   }
   .section-tech {
       background-color: #fff;
   }
   .rs-icon-info-1 {
       padding: 33px !important;
   }

   .content-wrap-140 {
       padding: 140px 0;
   }




   /* 展示 */
   .media-img:hover {
       transform: scale(1.05,1.05);
   }
   .get-all-pic {
       width: 80%;
       margin: 0 auto;
       color: #FFD808;
       text-align: center;
   }
   .get-all-pic:hover {
       transform: scale(1.05,1.05);
   }
   /* 团队 */
   .section-team {
       /* background-color: #FFFFF0; */
       background-color: #fff;
       /* background-color: #000; */
   }
    .team-row {
       margin: 0 auto;
       width: 100%;
    }
    .team-center {
        text-align: center;
    }
   div.media {
       /* 人头像周围的背景色，配合 section-team 一起调整 */
       /* background-color: #FFFFF0; */
       background-color: #fff;
       /* background-color: #000; */
   }
   .backgroundWhite {
       background-color: rgba(255, 255, 255) !important;
   }
   /* .img-subsco:after {
       background-color: #FFD808;
   } */
   
    /* 实例展示部分 */
    .entity-subtitle {
        font-family: "Ubuntu", sans-serif;
        font-size: 20px;
        font-weight: 700;
        margin-top: 10px;
        margin-bottom: 5px;
    }
    .my-display-image {
        background: url('../assets/images/banner-bg.png');
            position: relative;
            /* background-size: cover; */
            box-sizing: border-box;
            z-index: 12;
    }
    .my-display-image:after {
        content: "";
            width: 100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            /* 从父元素继承 background 属性的设置 */
            background: inherit;
            filter: blur(10px);
            z-index: 13;

    }
    /* .my-blur:after {
        background-color: #000;
        filter: blur(2px);
        z-index: 20;

    } */
    .my-relation {
        z-index: 40;
    }
</style>

<style>
    .my-blur {
        width: 100%;
            height: 100%;
            position: relative;
            background: #000;
            background-size: cover;
            box-sizing: border-box;
            z-index: 19;
        /* position: relative; */
    }
    .my-blur::after {
        content: "";
            width: 100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            /* 从父元素继承 background 属性的设置 */
            /* background: inherit; */
            /* background-color: #fff; */
            background-color: #000;
            /* filter: blur(30px); */
            filter: blur(50px);
            z-index: 20;
        /* background-color: #000 !important;
        filter: blur(2px) !important;
        z-index: 20 !important; */

    }
</style>
