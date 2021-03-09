<template>
  <!-- <div data-spy="scroll" data-target="#navbar-example"> -->
  <div>

    <!-- LOAD PAGE -->
    <!-- <div class="animationload">
        <div class="loader"></div>
    </div> -->

    <!-- BACK TO TOP SECTION -->
    <BackTop></BackTop>
    
    <AnchorList></AnchorList>

    <!-- CONTENT -->
    <div id="class" class="">
        <div class="content-wrap">
            <div class="container">
                <!-- <AnchorList></AnchorList> -->


            </div>
        </div>
    </div>

    <CTA></CTA>

    <!-- FOOTER SECTION -->
    <Footer></Footer>

  </div>

</template>

<script src="../../public/js/vendor/modernizr.min.js"></script>
<!-- JS VENDOR -->
<!--script src="../../public/js/vendor/jquery.min.js"></script-->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="../../public/js/vendor/bootstrap.min.js"></script>
<script>$('.collapse').collapse()</script>
<script src="../../public/js/vendor/owl.carousel.js"></script>
<script src="../../public/js/vendor/jquery.magnific-popup.min.js"></script>
<!-- SENDMAIL -->
<script src="../../public/js/vendor/validator.min.js"></script>
<script src="../../public/js/vendor/form-scripts.js"></script>
<script src="../../public/js/script.js"></script>

<script>
// @ is an alias to /src
import BackTop from "@/components/BackTop"
import Footer from "@/components/Footer";
import CTA from "@/components/CTA";
import AnchorList from "@/components/whole/AnchorList";

export default {
  name: 'Whole',
  components: {
    BackTop,
    Footer,
    CTA,
    AnchorList,
  },
  data() {
      return {
        //   stockCode:decodeURI(this.$route.query.stockCode),
          stockCode: "BK0420",
          industryInfo: {},    //公司基本详情，包括名称、股票代码、简介等
          notices: [],         //公告
          describe_arr: "",    //缩略版的企业简介
          flag: true,          //配合 toggle() 方法，用于控制企业简介的展开与折叠
      };
  },
  created() {
      //console.log(this.stockCode);
    //   this.getData();
  },
  methods: {
    async getData () {
            let {data} = await this.$get(
                "http://121.46.19.26:8288/ForeSee/industryIndex/" + this.stockCode
            )
            console.log(data)

            // 本地测试
            // let  {data}  = await this.$get("/data/multi.json");
            console.log(data)
            this.industryInfo = data.IndustryInfo
            this.notices = data.notices
            this.describe_arr = data.IndustryInfo.describe.slice(0,120) + "..."
        },
    toggle () {
        //变换 flag, 用于控制企业简介的展开与折叠
        this.flag = !this.flag
    }
  }
}
</script>

<style scoped>
div.content-wrap {
  padding-top: 80px;
}
.introduction {
    font-size: 16px;
    text-indent: 0em;
}
.introduction::first-letter {
    font-size: 30px;
    color: #FFD808; 
    float: left;
}
.toggle {
    color: #FFD808;
}

</style>

