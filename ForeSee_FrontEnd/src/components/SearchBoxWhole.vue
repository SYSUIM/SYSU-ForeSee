<template>
  <!-- 检索框 -->
<div >
  <div class="search-box">
      <el-input
        v-model="input"
        @keyup.enter.native="trans"
        class="search keyword"
        placeholder="请输入检索词" clearable @clear="setValueNull"
      >
      </el-input>
    <button class="btn btn-primary" v-bind:disabled="dis" type="button" @click="trans" id="searchclick">搜索</button>
  </div>
  <!-- 实时检索下拉菜单 -->
  <div class="search-content" ref="search" v-show="input" style="background:white">
        <ul>
          <li class="search-item " v-for="item in returnlist" :key="item.id" @click="transFromRealtime(item)" >
              <p>{{ item.company_name }}</p>
          </li>
          <li class="search-item nodata" v-show="hasNoData">没有找到匹配数据</li>
        </ul>
  </div>
</div>
</template>

<script>
//import $ from 'jquery'
import data from '../../public/data/real-time-retrieve.json';

// 将中文字符转换成拼音，自己打包好的代码，参考博文：https://www.cnblogs.com/ning-xiaowo/p/14343580.html
// 测试方法：
// console.log('中文1234english拼音jichang的拼音是:',PinyinTrans.chineseToPinYin("中文1234english拼音jichang"));
import PinyinTrans from '../util/chinese2pinyin/vue-py';

// 引入拼音检索匹配，参考：https://github.com/xmflswood/pinyin-match
import PinyinMatch from 'pinyin-match';

// 将用户input的query：全角转为半角，参考：https://www.cnblogs.com/moqiutao/archive/2017/05/17/6869794.html
// 用法：console.log('全角字符串"中国，中文，标点符号！你好？１２３４５＠＃【】+=-（）。"对应的半角为>',ToCDB('中国，中文，标点符号！你好？１２３４５＠＃【】+=-（）。'))
function ToCDB(str) { 
    var tmp = ""; 
    for(var i=0;i<str.length;i++){ 
        if (str.charCodeAt(i) == 12288){ //空格
            tmp += String.fromCharCode(str.charCodeAt(i)-12256);
            continue;
        }
        if(str.charCodeAt(i) > 65280 && str.charCodeAt(i) < 65375){ //英文字符等
            tmp += String.fromCharCode(str.charCodeAt(i)-65248); 
        } 
        else{ 
            tmp += String.fromCharCode(str.charCodeAt(i)); 
        } 
    } 
    return tmp 
} 

// 引入外部函数，将用户input的query：繁体转成简体，包含2500个词的对应表，参考：https://www.cnblogs.com/mq0036/p/4010339.html
import traditional2simple from '../../src/util/traditional2simple/const'
// 用法：
// console.log('繁体> 板球，又稱木球，一向給人稱頌為“紳士的遊戲”，是一項崇尚體育精神和公平比賽的運動。板球起源於英國，盛行於澳大利亞、新西蘭、巴基斯坦、印度、孟加拉、尼泊爾等國家。板球項目是鍛煉手眼的協調能力，集上肢動作控製能力、技巧與力量為一體的綜合性運動。')
// console.log('简体>',traditional2simple.simplized('板球，又稱木球，一向給人稱頌為“紳士的遊戲”，是一項崇尚體育精神和公平比賽的運動。板球起源於英國，盛行於澳大利亞、新西蘭、巴基斯坦、印度、孟加拉、尼泊爾等國家。板球項目是鍛煉手眼的協調能力，集上肢動作控製能力、技巧與力量為一體的綜合性運動。'))

export default {
  name: "SearchBoxWhole",
  data() {
    return {
      input: "",
      list:data,
      returnlist:[],
      dis:true,
    };
  },
  computed: {
    hasNoData () {
      return !this.returnlist.length
    },
  },
  methods: {
    // 发送检索请求
    trans() {
      if(this.input=== ''){
        /*$(".btn btn-primary").addClass("huise")
        // 将鼠标设置为不可点击状态
        document.getElementById('searchclick').style.cursor = 'not-allowed'*/
        this.dis=true;
      }
      else{
        setTimeout(() => {
          // this.$router.push("/whole" + "?query=" + this.input);
          // 后端的索引全部是小写，将用户输入的英文query转化成小写
          // this.$router.push("/whole" + "?query=" + this.input.toLowerCase());
          this.$router.push("/whole" + "?query=" + traditional2simple.simplized(ToCDB(this.input).toLowerCase()));
        }, 200);
      }
    },
    setValueNull(){
      this.dis=true;
    },
    async transFromRealtime(item){
      await this.$router.push("/whole" + "?query=" + item.short_name);
    },
  },
  created(){
    // this.realtimeSearch();
    
    
    
  },
  watch: {
    input () {
      if (this.timer) {
        clearTimeout(this.timer)
      }
      if (!this.input) {
        this.returnlist = []
        return
      }
      if (this.input!='')
      {
        //this.hideIcon();
        this.dis=false;
        /*$(".btn btn-primary").removeClass("huise") //移除不可点击状态
        document.getElementById('searchclick').style.cursor = 'pointer'*/
      }
      this.timer = setTimeout(() => {
        const result = []
        for (let i in this.list) {
            if(result.length>5) break;
            let index = this.list[i].company_name.indexOf(this.input);
            if (index >= 0 ) {
              // 实现优先级赋值，index：第几个匹配到
              this.list[i].index=index;
              this.list[i].priority = 1;
              result.push(this.list[i]);
            } else {
              // 直接用字符匹配不上，将 query 转换成拼音，再进行匹配。如 "蓝思科技" 输入成 "兰斯科技"，也返回。
              // 此外，由于索引全部为小写，是否应当转化为小写（？）str.toLowerCase()
              let newInput = PinyinTrans.chineseToPinYin(this.input);

              // 直接用字符匹配不上，试试拼音检索匹配
              // 用法：PinyinMatch.match(input, keyword)。input是目标字符串，keyword是用户输入的拼音
              // 如果没有匹配上，返回false; 成功匹配，返回input子串的索引[a,b]
              // let index2 = PinyinMatch.match(this.list[i].company_name, this.input);
              let index2 = PinyinMatch.match(this.list[i].company_name, newInput);
              if(index2 != false && index2[0] >= 0) {
                this.list[i].index = index2[0];
                this.list[i].priority = 10;
                result.push(this.list[i]);
              }
            }
        }
        //实现优先级排序
        result.sort(function(a,b){
          // 确保index不是0，所以要+1
          return (a.priority * (a.index+1) - b.priority * (b.index+1));
          /*
          // 方法2
          if(a.priority == b.priority)
            return (a.index-b.index);
          else return (a.priority-b.priority);
          */

          /*
          // 方法3
          return (a.index-b.index);
          */
        });
        console.log(result);
        this.returnlist = result;
      }, 100)
    }
  }
};
</script>

<style>
.search-box{
  margin-bottom: 0;
  position: relative;
  height: 100px;
}

.search-content{
  margin-top:-40px;
  padding:0;
  width:280px;
  /* margin-left: 220px; */
  margin-left: 224px;
  /* text-align: left; */
  /* border-top: 1px solid #eee; */
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  overflow: hidden;
  position: absolute;
  z-index: 9999;

}
.search-content ul{
  margin-left: 0px;
}
.search-content .search-item{
  margin-left: 0;
  height: 40px;
  /* text-align: left; */
  display:flex;
  vertical-align: middle;
  align-items: center;
  align-content: center;
  border-top: 1px solid #eee;
}

.search-content .search-item p{
  margin-top: 30px;
  margin-left: 10px;
  font-size: 12px;
}
.banner .wrap-caption .search-content .search-item p {
  color: #606266;
}

.search-item:hover{
  cursor: pointer;
}

.search-item.nodata{
  margin-left: 10px;
  border-bottom: none;
}
#searchclick.btn.btn-primary{
  background-color: #ffd808;
  font-size: 18px;
  font-weight: bold;
  border-radius: 5px;
}
#searchclick.btn.btn-primary:hover{
  background-color: #000000;
}
/* input.el-input__inner {
  width: 350px !important;
} */
 .el-input__inner {
  height: 54px;
  width: 280px;
  padding: 10px;
  border: none;
  border-radius: 28px;
  outline: none;
  margin-bottom: 10px;
}
/*.el-input__icon {
  position: relative;
  top: 13px;
  left: 12px;
}*/
.el-input__suffix {
  /*width: 48px;
  height: 48px;*/
  right: 28px;
}
.btn {
  font-size: 14px;
  color: #ffffff;
  padding: 14px 50px;
  border: 0;
  min-width: 150px;
  font-family: "Ubuntu", sans-serif;
  -webkit-border-radius: 0;
  -moz-border-radius: 0;
  -ms-border-radius: 0;
  border-radius: 8;
  font-size: 16px;
  -webkit-box-shadow: 0px 5px 30px rgba(0, 0, 0, 0.1);
  -moz-box-shadow: 0px 5px 30px rgba(0, 0, 0, 0.1);
  box-shadow: 0px 5px 30px rgba(0, 0, 0, 0.1);
}
.btn-primary {
  background-color: #ffd808;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-radius: 28px;
}
.btn-primary:hover {
  background-color: #000000;
  color: #ffd808;
}
.search-box .el-input .el-input__inner {
  height: 54px;
  width: 280px;
  padding: 10px;
  border: none;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  border-bottom-left-radius: 5px;
  outline: none;
  margin-bottom: 0;
}
</style>