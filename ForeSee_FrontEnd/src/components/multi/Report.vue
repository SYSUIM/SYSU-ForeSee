<template>
    <el-card shadow="hover"  class="swiper" v-if="isReady">
    
        <div class="widget-title line">
            行业资讯 <span>Information</span>
        </div>
        <el-carousel indicator-position="outside" type="card">

            <!-- Item -->
            <el-carousel-item v-for="(item,index) in industryNotices" :key="item.link+index">
                <div class="rs-icon-info-1">
                    <div class="info-icon">
                        <i class="fas fa-building"></i>
                    </div>
                    <div class="info-text">
                        <h4 class="text-black mb-2 information-title">
                            <a :href="item.link" target="_blank">{{ item.title }}</a>
                        </h4>
                        <p>{{ item.pub_date }}</p>
                        <!-- <p><a :href="item.link">发表日期：<span>{{ item.notice_time }}</span></a></p> -->
                        <!-- <a :href="item.link">点击查看</a> -->
                    </div>
                </div>
            </el-carousel-item>

        </el-carousel>
        <div class="seeMore">
                <!-- <router-link :to="'/morenotices'+'?stockCode='+stockCode+'&page='+page"> -->
                <!-- <router-link :to="'/industryrepo'+'?stockCode='+stockCode+'&page='+page"> -->
                <router-link :to="'/industryrepo'+'?industryCode='+industry_code+'&page=1'" target="_blank">
                    查看更多 >>
                </router-link>
        </div>
    
    </el-card>
</template>

<script>
export default {
    props: ["industry_code"],
    data () {
        return {
            // query: decodeURI(this.$route.query.query),
            // industryCode: "BK0422",
            industryNotices: [],
            isReady: false
        }
    },
    methods: {
        async getData () {
            // let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/industryIndex/" + this.industryCode);
            // let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/report/" + this.industryCode +"/1");
            let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/report/" + this.industry_code +"/1");
            for(var i=0; i<5; i++)
                this.industryNotices.push(data.information[i]);
            this.isReady = true;
            // console.log("行业资讯>", this.industryNotices);
        }
    },
    watch: {
        industry_code () {
            this.getData();
        }
    }
    
}
</script>

<style scoped>
    a:hover {
        color: #FFD808 !important;
    }
    .swiper {
        margin-top: 90px;
        padding-top: 10px;
        /* border-top: 1px solid #EBEEF5; */
    }
    .line {
        padding-bottom: 10px;
        border-bottom: 1px solid #EBEEF5;
        /* width: 20%; */
    }
    .rs-icon-info-1 {
        min-height: 280px;
        max-height: 300px;
    }
    .seeMore {
        text-align: right;
        font-size: 14px;
        /* border-bottom: 1px solid #EBEEF5; */
        padding-bottom: 10px;
    }
    .el-carousel__item {
        background-color: #fff;
    }
</style>
