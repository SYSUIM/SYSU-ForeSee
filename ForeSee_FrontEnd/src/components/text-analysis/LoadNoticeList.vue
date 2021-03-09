<template>
    <div class="loadList">
        <div class="widget-title-pd">
            企业公告 <span>Notice</span>
        </div>

        <!-- 检索结果提示 -->
        <!-- <div class="grey">Foresee 为您找到相关结果约<span>12,300,000</span>个</div> -->

        <!-- 使用 Element-ui 进行布局，8：16 -->
        <!-- 动态数据 -->
        <div v-loading="loading">
            <!-- 无数据 -->
            <div v-if="flag" class="noData">数据暂无</div>
            
            <!-- 有数据 -->
            <div v-if="!flag">
        <el-row class="line" v-for="(item,index) in list" :key="item+index">
            <el-col :span="8">
                <router-link :to="'/detail'+'?stockCode='+item.companyInfo.stock_code" target="_blank">
                    <div class="icon">
                        <img :src="item.companyInfo.logo" alt="">
                    </div>
                    <div class="name-wrap">
                        <span class="name">{{ item.companyInfo.former_name }}</span>
                    </div>
                    <div class="name-wrap">
                        <span class="red-1">股票代码:</span>
                        <span class="red">{{ item.companyInfo.stock_code }}</span>
                    </div>
                </router-link>
            </el-col>
            <el-col :span="16" class="text-col">
                <div class="type-wrap"><span class="text-type">公告</span></div>
                <div class="title">
                    <a :href="item.link" target="_blank">
                        {{ item.notice_title }}
                    </a>
                </div>
                <div class="date"><span>时间：</span>{{ item.notice_time }}</div>
            </el-col>
        </el-row>

        <!-- 静态数据示例 -->
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
                <div class="type-wrap"><span class="text-type">公告</span></div>
                <div class="title">A股10月开门红：光伏板块掀涨停潮 四季度将如何演绎？</div>
                <div class="date"><span>时间：</span>2020-10-10</div>
            </el-col>
        </el-row> -->

        <!-- Item 3 -->
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
                <div class="type-wrap"><span class="text-type">新闻</span></div>
                <div class="title">A股10月开门红：光伏板块掀涨停潮 四季度将如何演绎？</div>
                <div class="date"><span>时间：</span>2020-10-10</div>
            </el-col>
        </el-row> -->

        <!-- 分页组件 -->
        <div class="block">
            <el-pagination
            :page-size="10"
            @current-change="handleCurrentChange"
            layout="prev, pager, next"
            :total="totalRecords">
            </el-pagination>
        </div>

            </div>
        </div>

    </div>
</template>

<script>
export default {
    data () {
        return {
            query: decodeURI(this.$route.query.query),
            // query: "玻璃",
            list: [],
            totalRecords: 0, //总记录数totalRecords = 82 时，默认每页展示 10 条，因此绑定后总页数为 9
            currentPage: 1,
            loading: true,
        }
    },
    computed: {
        flag () {
            // 数据是否暂无：无数据，返回true；有数据，返回false。
            return (this.totalRecords == 0);
        }
    },
    methods: {
        async getData (val) {
            this.loading = true;
            let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/noticeQuery/" + this.query + "/" + val);
            this.list = data.notice;
            // console.log('新闻文本分析 >',this.list);
            this.totalRecords = data.totalRecords;
            this.loading = false;
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.currentPage = val;
            this.getData(val);
        },
        // 子组件向父组件传值
        toParent () {
            this.$emit('notice-records', this.totalRecords)
        }
    },
    mounted () {
        setTimeout(this.getData(1));
    },
    watch: {
        totalRecords () {
            this.toParent();
        }
    }

    
}
</script>

<style scoped>
    /* .loadList {
        margin-top: 80px;
    } */

    /* 检索结果提示 */
    /* .grey {
        color: #9195a3;
        font-size: 13px;
        margin-bottom: 10px;
    } */
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
    .noData {
        text-align: center;
        color: #ccc;
    }
    .line {
        /* margin-top: 40px; */
        /* padding-top: 40px; */
        /* margin-bottom: 40px; */
        padding-bottom: 40px;
        padding-top: 10px;
        border-top: 1px solid #EBEEF5;
    }
    img {
        width: 48%;
        height: 8vw;
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
    .text-col {
        padding-left: 3%;
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
        /* padding-top: 30px; */
        font-size: 20px;
        font-weight: 700;
        color: #000;
        width: 85%;
    }
    .type-wrap {
        margin-top: 20px;
        margin-bottom: 5px;   
    }
    .text-type {
        font-size: 12px;
        background-color: #F4F4F4;
        border-radius: 3px;
        color: #585858;
        font-weight: 600;
        padding: 0px 8px;
    }
    .block {
        margin-top: 70px;
    }
    div.el-pagination {
        text-align: center;
    }
</style>