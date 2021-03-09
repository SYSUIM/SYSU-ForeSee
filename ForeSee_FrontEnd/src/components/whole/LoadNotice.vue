<template>
    <div class="loadList">
        <!-- 使用 Element-ui 进行布局，8：16 -->
        <!-- 动态数据 -->
        <el-row class="line" v-for="(item,index) in list" :key="item+index">
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
                <div class="type-wrap"><span class="text-type">公告</span></div>
                <div class="title">
                    <a :href="item.link" target="_blank">
                        {{ item.notice_title }}
                    </a>
                </div>
                <div class="date"><span>时间：</span>{{ item.notice_time }}</div>
            </el-col>
        </el-row>

    </div>
</template>

<script>
export default {
    data () {
        return {
            query: decodeURI(this.$route.query.query),
            list: [],
            totalRecords: 1,
        }
    },
    methods: {
        async getData () {
            let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/noticeQuery/" + this.query + "/1");
            this.list = data.notice.slice(0,2);
            this.totalRecords = data.totalRecords;
            // console.log('新闻文本分析 >',this.list);
        },
        // 子组件向父组件传值
        toParent () {
            this.$emit('listenToChildren', this.totalRecords)
        }
    },
    mounted () {
        setTimeout(this.getData());
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

    .line {
        /* margin-top: 40px; */
        /* padding-top: 40px; */
        /* margin-bottom: 40px; */
        padding-bottom: 40px;
        padding-top: 10px;
        border-top: 1px solid #EBEEF5;
    }
    .my-company-info:hover {
        transform: scale(1.05,1.05);
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