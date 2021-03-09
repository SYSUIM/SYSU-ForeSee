<template>
    <div class="loadList" v-loading="loading">

        <!-- 使用 Element-ui 进行布局，8：16 -->
        <!-- 动态数据 -->
        <el-row class="line" v-for="(item,index) in lists" :key="item + index">
            <el-col :span="8" class="my-company-info">
                <router-link :to="'/detail'+'?stockCode='+item.companyInfo.stock_code">
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
                <div class="type-wrap"><span class="text-type">新闻</span></div>
                <div class="title">
                    <a :href="item.url" target="_blank">
                        {{ item.title }}
                    </a>
                </div>
                <div class="date"><span>时间：</span>{{ item.date }}</div>
            </el-col>
        </el-row>

        <!-- 导入企业公告组件 -->
        <LoadNotice @listenToChildren="changeNoticeRecords"></LoadNotice>

        <!-- 导入行业资讯组件 -->
        <LoadInformation @listenToChildren="changeInformationRecords"></LoadInformation>

        <!-- 跳转 -->
        <router-link :to="'/news'+'?query='+query+'&page='+page" target="_blank"> 
            <div class="seeMore">查看更多 >></div>
        </router-link>

        <!-- 静态数据 -->
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

        <!-- Item 4 -->
        <!-- <el-row class="line">
            <el-col :span="8">
                <div class="icon">
                    <div class="rs-icon-info-1">
                        <div class="info-icon">
                            <i class="fas fa-building"></i>
                        </div>
                    </div>
                </div>
                <div class="name-wrap">
                    <span class="name"><span>玻璃</span>行业</span>
                </div>
            </el-col>
            <el-col :span="16">
                <div class="type-wrap"><span class="text-type">行业资讯</span></div>
                <div class="title">A股10月开门红：光伏板块掀涨停潮 四季度将如何演绎？</div>
                <div class="date"><span>时间：</span>2020-10-10</div>
            </el-col>
        </el-row> -->

    </div>
</template>

<script>
import LoadNotice from '@/components/whole/LoadNotice'
import LoadInformation from '@/components/whole/LoadInformation'
export default {
    components: {
        LoadNotice,
        LoadInformation
    },
    data () {
        return {
            query: decodeURI(this.$route.query.query),
            lists: [],
            page: 1, // 不能删，仅用于页面跳转
            //通过子组件向父组件传值的方式进行修改
            newsRecords: 0,
            noticeRecords: 0,
            informationRecords: 0,
            loading: true
        }
    },
    computed: {
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
    methods: {
        async getData () {
            let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/newsQuery/" + this.query + "/1");
            // console.log('企业新闻>',data)
            this.lists = data.news.slice(0,2);
            this.newsRecords = data.totalRecords;
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
        this.getData();
    },
    watch: {
        totalRecords () {
            this.$emit('listenToChildren', this.totalRecords); // 将数据从Text组件传给上级<AnchorList>组件
            this.loading = false;
        }
    }
    
}
</script>

<style scoped>
    .rs-icon-info-1 {
        border: 1px solid white;
        padding: 0px;
        margin-bottom: 0px;
    }
    .rs-icon-info-1 .info-icon {
        margin-bottom: 5px;
    }
    .line {
        padding-bottom: 40px;
        padding-top: 10px;
        border-top: 1px solid #EBEEF5;
    }
    img {
        width: 48%;
        height: 8vw;
    }
    .my-company-info:hover {
        transform: scale(1.05,1.05);
    }
    .icon {
        /* 图片居中显示 */
        text-align: center;
    }
    .name-wrap {
        margin-top: 10px;
        text-align: center;
    }
    .name {
        color: #000;
        font-weight: 700;
    }
    .red-1 {
        color: #585858;
        font-size: 12px;
        font-weight:600;
    }
    .red {
        background-color: #F4F4F4;
        border-radius: 3px;
        color: #585858;
        font-size: 12px;
        font-weight: 600;
        margin: 4px 0px 0px 8px;
        padding: 0px 8px;
    }
    .text-col {
        padding-left: 3%;
    }
    .date {
        font-family: "Open Sans", sans-serif;
        margin-top: 10px;
        font-size: 16px;
        color: #666666;
    }
    .title {
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
    .seeMore {
        padding-top: 10px;
        text-align: right;
        font-size: 14px;
        border-top: 1px solid #EBEEF5;
        padding-bottom: 10px;
    }

</style>