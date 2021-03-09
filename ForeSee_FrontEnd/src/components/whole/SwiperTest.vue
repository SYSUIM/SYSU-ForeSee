<template>
    <div style="position: relative;" v-loading="loading">
        <!-- 左箭头 -->
        <!-- <div class="swiper-button-prev my-btn" slot="button-prev"></div>  -->
        <div class="swiper-button-prev my-btn">`</div> 

        
        <swiper :options="swiperOption" class="my-swiper">

            <!-- 静态数据 -->
            <!-- Item 1 -->
            <!-- <swiper-slide class="swiper-slide">
                <router-link :to="'/multi'+'?query=BK0910'" target="_blank">
                <el-card class="my-slide" shadow="hover">
                    <i class="fas fa-building" style="color: #FFD808;"></i>
                    <span class="industry">互联网</span>
                    <div class="industry-describe">随着芯片制造商产能的持续紧张，半导体行业涨价地震仍在继续。在汽车行业，芯片断供带来的停工、减产已经成为困扰不少汽车厂商的问题。美国伯恩斯坦研究公司预计，2021年全球范围内的汽车芯片短缺或将造成多达450万辆汽车产量的损失</div>
                </el-card>
                </router-link>
            </swiper-slide> -->

            <!-- Item 2 -->
            <!-- <swiper-slide class="swiper-slide">
                <router-link :to="'/multi'+'?query=BK0910'" target="_blank">
                <el-card shadow="hover" class="my-slide">
                    <i class="fas fa-building" style="color: #FFD808;"></i>
                    <span class="industry">专用设备</span>
                    <div class="industry-describe">随着芯片制造商产能的持续紧张，半导体行业涨价地震仍在继续。在汽车行业，芯片断供带来的停工、减产已经成为困扰不少汽车厂商的问题。美国伯恩斯坦研究公司预计，2021年全球范围内的汽车芯片短缺或将造成多达450万辆汽车产量的损失</div>
                </el-card>
                </router-link>
            </swiper-slide> -->

            <!-- 动态数据 -->
            <swiper-slide class="swiper-slide" v-for="(item,index) in list" :key="item+index">
                <router-link :to="'/multi'+'?query='+item.industry_code">
                    <div class="my-slide new-offset">
                    <!-- <div class="my-slide"> -->
                    <!-- <el-card shadow="hover" class="my-slide"> -->
                       <i class="fas fa-building my-icon"></i>
                       <span class="industry">{{ item.industry }}</span>
                       <div class="industry-describe">  <span>{{ item.describe }}</span></div>
                    <!-- </el-card> -->
                    <!-- </div> -->
                    </div>
                </router-link>
            </swiper-slide>

            <!-- 左右箭头 --> 
            <!-- <div v-if="over4"> -->
                <!-- <div class="swiper-button-prev" slot="button-prev"></div> 
                <div class="swiper-button-next" slot="button-next"></div>  -->
            <!-- </div> -->
        </swiper>

        <!-- 右箭头 -->
        <div class="swiper-button-next my-btn" slot="button-next"></div> 
    </div>
</template>

<script>
    export default {
        data(){
            return {
                query: decodeURI(this.$route.query.query),
                list: [],
                // over4: false,
                swiperOption:{
                    //设置点击箭头
                    navigation: {
                      nextEl: '.swiper-button-next', 
                      prevEl: '.swiper-button-prev'
                    },
                    //自动轮播
                    autoplay: false,
                    //关闭循环模式
                    loop: false,
                    slidesPerView: 3,  //一行显示3个
                    spaceBetween: 20, //间隔30
                },
                totalRecords: 0,
                loading: true // 是否正在加载，与根元素绑定，v-loading="loading"
            }
        },
        methods: {
            async getData () {
                let { data } = await this.$get("http://121.46.19.26:8288/ForeSee/industryQuery/" + this.query);
                // console.log('行业检索>',data)

                // 数据处理
                let n = data.length;
                let arr = [];
                for(var i=0; i<n; i++) {
                    arr.push({
                        industry: data[i].industry,
                        industry_code: data[i].industry_code,
                        describe: data[i].describe.slice(0,109)
                    })
                }
                this.list = arr;
                this.totalRecords = n;
                this.loading = false;
                // 根据列表的长度确定是否显示翻页的左右箭头
                // if(n>=4)
                //     this.over4 = true;
            }
        },
        created () {
            setTimeout(this.getData());
        },
        watch: {
            totalRecords () {
                this.$emit('listenToChildren', this.totalRecords); // 将数据从Swiper组件传给上级<AnchorList>组件
            }
        }
    }
</script>

<style scoped>
    .my-icon {
        color: #FFD808;
        /* padding: 10%; */
        /* padding: 8% 0% 0% 8%; */
    }
    .industry {
        font-size: 18px;
        font-weight: 600;
        padding-left: 4%;
        /* padding: 8% 0% 0% 8%; */
        color: #000;
    }
    .industry-describe {
        padding-top: 5%;
        /* padding: 5% 8% 5% 8%; */
        font-size: 15px;
        color: #4D4D4D;
    }
    .my-btn {
        width: 18px;
        text-align: left;
        font-size: 15px;
        overflow: hidden;
        padding-right: 5px;
        color: #EBEEF5;
    }
    .my-swiper {
        padding-top: 10px;
        width: 90% !important;
        height: 280px;
    }
    .my-slide {
        height: 255px;
        /* border: 1px solid #EBEEF5; */
        /* border-radius: 8px; */
    }
    .my-slide:hover {
        /* border: 1px dotted #FFD808; */
        /* background-color: #FFD808; */
        border: 1px solid #fff;
    }
    /* 父元素 hover 时，子元素变色 */
    /*
    .my-slide:hover .my-icon {
        color: #000;
    } 
    .my-slide:hover .industry {
        color: #000;
    } 
    .my-slide:hover .industry-describe {
        color: #fff;
    } 
    */
    .new-offset {
    padding: 15px;
    border: 1px solid #EBEEF5;
    border-radius: 5px;
    display: inline-block;
    vertical-align: top;
    transition: box-shadow .4s,transform .4s;
    transition-property: box-shadow, transform;
    transition-duration: 0.4s, 0.4s;
    transition-timing-function: ease, ease;
    transition-delay: 0s, 0s;
    }
    .new-offset:hover {
        transform: translateY(-4%);
        border: 1px solid #EBEEF5;
        box-shadow: 0px 2px #EBEEF5;
        background-color: rgb(249, 249, 250, 0.3);
        /* transform: translateY(-2%); */
        /* box-shadow: 1px 3px 3px 2px #ccc; */
    }
</style>
