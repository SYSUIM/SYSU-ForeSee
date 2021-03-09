<template>
    <div>
        <div id="main"></div>
    </div>
</template>

<script>
    import {graph} from '../assets/js/LineChart.js'
    export default {
        // props: ['jsonObj'],
        data () {
            return {
                stockCode:decodeURI(this.$route.query.stockCode),
                json: {}
            }
        },
        mounted() {
            this.getData();
            // setTimeout(() => {
            //     this.graph(this.jsonObj)
            // }, 500)
        },
        methods: {
            graph,
            async getData () {
                let {data} = await this.$get(
                    "http://121.46.19.26:8288/ForeSee/companyInfo/" + this.stockCode
                )
                
                // 本地测试
                // let  {data}  = await this.$get("/data/detail2.json");
                // console.log('饼图数据')
                // console.log(data)

                this.json = data.profit;

                // 画图
                this.graph(this.json);
            }
        },
        watch: {
            stockCode () {
                this.getData();
            }
        },
    }
</script>


<style scoped>
    #main {
        width: 100%;
        height: 400px;
        border: 1px solid #EBEEF5;
        margin: 0 auto;
        /* margin-top: 60px; */
    }
</style>