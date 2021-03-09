<template>
    <div class="loadList">
        <div class="widget-title">
            相关企业 <span>Company</span>
        </div>

        <!-- 检索结果提示 -->
        <!-- <div class="grey">Foresee 为您找到相关结果约<span>12,300,000</span>个</div> -->

        <!-- 使用 Element-ui 进行布局，8：16 -->
        <el-row class="line" v-for="(item,index) in industryLists" :key="index+item">
            <el-col :span="8">
                <router-link :to="'/detail'+'?stockCode='+item.companyInfo.stock_code">
                    <div class="icon">
                        <img :src="item.companyInfo.logo" alt="">
                    </div>
                    <div class="name-wrap">
                        <span class="name">{{ item.companyInfo.former_name }}</span>
                        <!-- <span class="red">{{ item.companyInfo.stock_code }}</span> -->
                    </div>
                    <div class="name-wrap">
                        <span class="red-1">股票代码:</span>
                        <span class="red">{{ item.companyInfo.stock_code }}</span>
                    </div>
                </router-link>
            </el-col>
            <el-col :span="16">
                <!-- 版本 1 -->
                <!-- <a :href="item.news_link">
                    <div class="title">{{ item.news_title }}</div>
                    <div>{{ item.sentence }}</div>
                    <div class="date"><span>时间：</span>{{ item.news_time }}</div>
                </a> -->

                <!-- 版本 2 -->
                <div class="title">
                    <a :href="item.url" target="_blank">
                        {{ item.title }}
                    </a>
                </div>
                <div class="date"><span>时间：</span>{{ item.date }}</div>
                
            </el-col>
        </el-row>

        <!-- Item 2 -->
        <!-- <el-row class="line">
            <el-col :span="8">
                <div class="icon">
                    <img src="https:\/\/zhengxin-pub.cdn.bcebos.com\/logopic\/05d70d75852ce3a1a9b5d8e593b53250_fullsize.jpg" alt="icon.jpg">
                </div>
                <div class="name-wrap">
                    <span class="name">重庆燃气</span>
                    <span class="red">600433</span>
                </div>
            </el-col>
            <el-col :span="16">
                <div class="title">A股10月开门红：光伏板块掀涨停潮 四季度将如何演绎？</div>
                <div>看好行业前景，光伏龙头通威股份与隆基股份强强联合扩张产能</div>
                <div class="date"><span>时间：</span>2020-10-10</div>
            </el-col>
        </el-row> -->

        <!-- 分页组件 -->
        <div class="block">
            <el-pagination
            :page-size="3"
            @current-change="handleCurrentChange"
            layout="prev, pager, next"
            :total="totalRecords">
            </el-pagination>
        </div>

    </div>
</template>

<script>
export default {
    props: ['industry_code'],
    data () {
        return {
            query: decodeURI(this.$route.query.query),
            // industryCode: "BK0420",
            industryLists: [],
            totalRecords: 2, //总记录数totalRecords = 82 时，默认每页展示 10 条，因此绑定后总页数为 9
            currentPage: 1
        }
    },
    methods: {
        async getData (val) {
            // let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/industryNews/" + this.industry_code + "/" + val);
            let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/industryNews/" + this.query + "/" + val);
            this.industryLists = data.news;
            console.log('行业新闻测试>',this.industry_code)
            console.log(this.industryLists);
            this.totalRecords = data.totalRecords;
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.currentPage = val;
            this.getData(val);
        }
    },
    mounted () {
        // this.getData(1);
        setTimeout(this.getData(1),1000);
    },
    watch: {
        // industry_code () {
        //     this.getData(1)
        // }
    }
    
}
</script>

<style scoped>
    .loadList {
        margin-top: 80px;
    }

    /* 检索结果提示 */
    /* .grey {
        color: #9195a3;
        font-size: 13px;
        margin-bottom: 10px;
    } */

    .line {
        /* margin-top: 40px; */
        /* padding-top: 40px; */
        /* margin-bottom: 40px; */
        padding-bottom: 40px;
        padding-top: 10px;
        border-top: 1px solid #EBEEF5;
    }
    img {
        width: 60%;
        height: 10vw;
    }
    .icon {
        /* 图片居中显示 */
        text-align: center;
    }
    .name-wrap {
        margin-top: 10px;
        /* padding-left: 40px; */
        text-align: center;
    }
    .name {
        color: #000;
        font-weight: 700;
    }
    .red-1 {
        /* color: #000; */
        color: #585858;
        font-size: 12px;
        font-weight:600;
    }
    .red {
        /* color: #FFD808; */
        /* color: #FF3B30; */
        /* background-color: #EBEEF5; */
        /* background-color: #FAFAFA; */
        background-color: #F4F4F4;
        border-radius: 3px;
        color: #585858;
        font-size: 12px;
        font-weight: 600;
        margin: 4px 0px 0px 8px;
        padding: 0px 8px;
        /* border: 1px solid #EBEEF5; */
    }
    .date {
        /* text-align: right; */
        font-family: "Open Sans", sans-serif;
        margin-top: 10px;
        /* padding-right: 10px; */
        font-size: 16px;
        color: #666666;
    }
    .title {
        padding-top: 30px;
        font-size: 20px;
        font-weight: 700;
        color: #000;
    }
    .block {
        margin-top: 70px;
    }
    div.el-pagination {
        text-align: center;
    }
</style>