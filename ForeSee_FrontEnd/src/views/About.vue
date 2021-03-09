<template>
  <div class="moreNews">

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
                        <div class="title-page">团队介绍</div>
                    </div>
                    <ol class="breadcrumb">
                      <li class="breadcrumb-item">中山大学信息管理学院</li>
                    </ol>
                </div>
            </div>

        </div>
    </div>

  </div>

    <div class="news-wrapper">
      <div class="widget-title my-topic">
            <!-- 团队成员 <span>Team</span> -->
      </div>

      <!-- TEAM 团队 -->
    <div class="section section-team" :id="'anchor-team'">
        <!-- <div class="content-wrap-140 pb-0"> -->
        <div class="content-wrap-140" style="margin-bottom: 50px;">
            <div class="container">

                <div class="row">
                    <div class="col-sm-12 col-md-12">
                        <!-- <h4 class="section-heading text-center text-black no-after mb-5"> -->
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
                                <div class="position">panzy25@mail2.sysu.edu.cn</div>
                                <p>数据爬取、整合，图数据分析、预测模型构建</p>
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
                                <div class="position">zengy85@mail2.sysu.edu.cn</div>
                                <p>大数据倒排索引构建、智能检索框架设计、自然语言问句检索实现</p>
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
                                <div class="position">chenlh65@mail2.sysu.edu.cn</div>
                                <p>关系抽取、模型构建、实体识别</p>
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
                                <div class="position">zhongshsh5@mail2.sysu.edu.cn</div>
                                <p>后端业务实现、数据库维护</p>
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
                                <div class="position">fengyx8@mail2.sysu.edu.cn</div>
                                <p>前端页面构建、可视化展现</p>
                            </div>
                        </div>

                    </div>

                </div>

                
            </div>
        </div>
    </div>

    <!-- <el-card class="card-box-news" shadow="hover">
      <p class="tip">您正在搜索的企业是：</p>
      <div class="tip-title" :data="company"> {{company}} 
        <span class="tip-stockCode">（股票代码：
          <span :data="stockCode">{{ stockCode }}
          </span>）
        </span>
      </div>
    </el-card> -->
    </div>
  

    <CTA></CTA>
    <Footer></Footer>
  </div>
</template>
<script>
import BackTop from '@/components/BackTop';
import CTA from "@/components/CTA";
import Footer from "@/components/Footer";


export default {
  components: {
    BackTop,
    CTA,
    Footer,
  },
  
};
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
    .moreNews {
      width: 100%;
      margin: 0 auto;
    }
    .backgroundImage{
      background-image: url('../assets/images/banner-bg.png');
      background-attachment:fixed; 
      background-repeat:no-repeat;
      width:calc(100%);
      height: calc(100%);
    }
    
    .subtitle-1 {
      font-family: "Ubuntu", sans-serif;
      color: #ffffff;
      font-size: 18px;
      font-weight: 500;
      /* color: #FFD808; */
      margin-bottom: 10px;
    }
    .my-topic {
      width: 80%;
      margin-left: 10%;
      margin-top: 50px;
    }
    .card-box-news {
      margin-top: 40px;
      margin-bottom: 20px;
      /* width: 84%;
      margin-left: 8%; */
      width: 78%;
      margin-left: 11%;
    }
    .tip {
      color: #232c35;
    }
    .tip-title {
      color: #232c35;
      font-size: 18px;
      font-weight: 700;
      padding-bottom: 10px;
      /* margin-bottom: 10px; */
      margin-bottom: 20px;
    }
    .tip-stockCode {
      font-size: 14px;
      /* font-weight: normal; */
    }
    .news-component {
      margin-top: 50px;
    }
    .tip-result {
      /* color: #9195a3; */
      color: #232c35;
      font-size: 13px;
      padding-top: 5px;
      padding-bottom: 10px;
      /* margin-bottom: 30px; */
      font-style: italic;
    }
    .card-box-news-2 {
      margin-top: 60px;
      margin-bottom: 20px;
      width: 70%;
      margin-left: 15%;
    }
    .block {
        margin-top: 70px;
        margin-bottom: 30px;
    }
    li:hover {
        color: #FFD808 !important;
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
</style>
