<template>
    <div class="notice-tab">
        <div class="widget-title">
            研究报告 <span>Research</span>
        </div>

        <div v-for="(item,index) in list" :key="item+index">
            <el-row class="my-reports-row">
                    <a :href="item.research_link" target="_blank">
                    <!-- <el-col :span="3">
                        <img src="../../../public/images/新闻.png">
                    </el-col>
                    <el-col :span="21" class="my-title"> -->
                    <el-col class="my-title">
                        {{ item.research_title }}
                    </el-col>
                    </a>
            </el-row>
            <!-- <div class="my-time">{{ item.research_time }}</div> -->
        </div>

        <router-link :to="'/research'+'?stockCode='+stockCode+'&page='+page+'&company='+companyName" target="_blank"> 
            <div class="seeMore">查看更多 >></div>
        </router-link>

    </div>
</template>
<script>
  export default {
    props: ['companyName'],
    data() {
      return {
        page:1,
        list: [],
        stockCode: decodeURI(this.$route.query.stockCode)
      };
    },
    methods: {
      async getData () {
        let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/research/" + this.stockCode + "/" + 1 );
        this.list = data.research.slice(0,5);
        console.log('研究报告测试 >',this.list)
      }
    },
    created () {
      this.getData()
    }
  };
</script>

<style scoped>
    .notice-tab {
        margin-top: 60px;
    }
    .my-reports-row {
      margin-bottom: 10px;
      padding: 5%;
      background-color: #FFFFF0;
      /* border: 1px solid #EBEEF5; */
      /* border-radius: 5px; */
      
    }
    /* .my-reports-row:hover {
      background-color: #FFD808;
    }
    .my-reports-row:hover .my-title {
      color: #000;
    } */
    img {
        width: 70%;
    }
    .my-title {
        font-family: "Ubuntu", sans-serif;;
        /* font-family: "Open Sans", sans-serif; */
        font-size: 14px;
        /* font-weight: 600; */
        /* color: #000; */
    }
    .my-time {
        font-size: 12px;
        text-align: right;
        /* margin-right: 1em; */
        padding-top: 3%;
        margin-bottom: 12px;
    }
    .seeMore {
        margin-top: 20px;
        text-align: right;
        font-size: 14px;
        border-bottom: 1px solid #EBEEF5;
        padding-bottom: 10px;
    }
</style>