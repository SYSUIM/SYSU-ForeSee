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
                        <div class="title-page">企业公告</div>
                    </div>
                    <!-- <div class="subtitle-1">
                        <span :data="company">{{ company }}</span> （股票代码：<span :data="stockCode"> {{ stockCode }}</span>）
                    </div> -->
                    <ol class="breadcrumb">
                      <li class="breadcrumb-item">共有  <span :data="totalRecords"> {{ totalRecords }} </span>  篇相关企业公告</li>
                    </ol>
                </div>
            </div>

        </div>
    </div>


  </div>

    <div class="news-wrapper">
      <!-- <div class="widget-title my-topic">
            新闻动态 <span>News</span>
      </div> -->
    <el-card class="card-box-news" shadow="hover">
      
      <p class="tip">您正在搜索的企业是：</p>
      <div class="tip-title" :data="company"> {{company}} 
        <span class="tip-stockCode">（股票代码：
          <span :data="stockCode">{{ stockCode }}
          </span>）
        </span>
      </div>
      <!-- <div class="tip-result">Foresee为您找到相关结果约<span>18953</span>个</div> -->
      
    </el-card>
    </div>
  
    <div class="card-box-news-2">
      <div class="news-component">
        <el-table :data="newslist" class="table">
          <!-- <el-table-column label="序号" type="index" width="150"></el-table-column> -->
          <el-table-column prop="notice_title" label="标题">
            <template slot-scope="newslist">
              <a :href="newslist.row.link" target="_blank" class="TestCSS"
              >{{ newslist.row.notice_title }}
              </a>
            </template>
          </el-table-column>
          <el-table-column prop="notice_time" label="日期" width="180"></el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <div class="block">
          <el-pagination
           ref="changepage"
           :page-size="10"
           :pager-count="7"
           layout="prev, pager, next"
           :total="totalRecords"
           :current-page="this.page"
           @current-change="currentChange"
           @prev-click="prevpage"
           @next-click="nextpage"
          ></el-pagination>
        </div>
      
      </div>
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
  data() {
    return {
      totalRecords: 10530,
      newslist: [],
      stockCode: decodeURI(this.$route.query.stockCode),
      company: decodeURI(this.$route.query.company),
      page: parseInt(this.$route.query.page),
    };
  },
  methods: {
    async loadMoreNews(stockCode, page) {
      this.page = page;
      console.log(page);

      //处理url的显示问题
      if(page!=1)
      {
        this.$router.replace({
        path: this.$route.path,
        // query: { ...this.$route.query, page },
        query: {
          stockCode: this.stockCode,
          page: page,
          company: this.company
        }
      });
      }
      

      // let { data } = await this.$get("/data/morenews_expect.json");
      let { data } = await this.$get(
        "http://121.46.19.26:8288/ForeSee/notice/" + this.stockCode + "/" + page
      );
      this.newslist = data.notice;
      this.totalRecords = data.totalRecords;
    },
    currentChange(val) {
      this.page = val;
      // console.log(this.page);
      this.loadMoreNews(this.stockCode, val);
    },
    prevpage(val) {
      this.page = val;
      // console.log(val);
      this.loadMoreNews(this.stockCode, val);
    },
    nextpage(val) {
      this.page = val;
      // console.log(val);
      this.loadMoreNews(this.stockCode, val);
    },
  },
  created() {
    this.loadMoreNews(this.stockCode,1);

  },
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
    /* .banner-page {
      padding-top: 50px;
    } */
    /* .bigTitle {
      color: #FFD808;
      font-size: 30px;
    } */

    /* .breadcrumb-item {
      color: #FFD808;
    } */
    
    /* news-wrapper {
      width: 65%;
      margin-left: 17.5%;
    } */
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
    div.el-pagination {
        text-align: center;
    }
    li:hover {
        color: #FFD808 !important;
    }
</style>
