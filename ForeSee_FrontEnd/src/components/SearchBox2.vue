<template>
  <!-- 检索框 -->
  <div class="search-box">
    <el-input 
      v-model="input" 
      @keyup.enter.native="trans" 
      class="search keyword" placeholder="Search Industry" clearable @clear="setValueNull">
    </el-input>
    <button class="btn btn-primary" v-bind:disabled="dis" type="button" @click="trans" id="searchclick">Search</button>

    <!-- 实时检索下拉菜单 -->
    <div class="search-content" ref="search" v-show="input" style="background:white">
        <ul>
          <li class="search-item " v-for="item in returnlist" :key="item.id" @click="transFromRealtime(item)" >
              <p>{{ item.industry }}</p>
          </li>
          <li class="search-item nodata" v-show="hasNoData">没有找到匹配数据</li>
        </ul>
    </div>
  </div>
</template>

<script>
  import data from '../../public/data/real-time-industry.json';
  //引入拼音检索匹配，参考：https://github.com/xmflswood/pinyin-match
  import PinyinMatch from 'pinyin-match'; 
  
  export default {
    name: "SearchBox2",
    data() {
      return {
        input: '',
        list: data,
        returnlist: [],
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
      trans () {
        if(this.input=== ''){
        // 将鼠标设置为不可点击状态
        this.dis=true;
      } else{
        setTimeout(() => {
        // this.$router.push('/industry' + '?query=' + this.input)
          this.$router.push('/multi' + '?query=' + this.input)
        }, 200)
      }
      },
      setValueNull(){
        this.dis=true;
      },
      async transFromRealtime(item){
        await this.$router.push("/multi" + "?query=" + item.industry);
      },
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
        if (this.input!='') {
          this.dis=false;
        }
        this.timer = setTimeout(() => {
          const result = []
          for (let i in this.list) {
            if(result.length>5) break;
            let index = this.list[i].query.indexOf(this.input);
            if (index > -1 ) {
              // 实现优先级赋值，index：第几个字符匹配到
              this.list[i].index=index;
              result.push(this.list[i]);
            } else { 
              // 直接用字符匹配不上，试试拼音检索匹配
              // 用法：PinyinMatch.match(input, keyword)。input是目标字符串，keyword是用户输入的拼音
              // 如果没有匹配上，返回false; 成功匹配，返回input子串的索引[a,b]
              let index2 = PinyinMatch.match(this.list[i].query, this.input);
              if(index2 != false && index2[0] >= 0) {
                this.list[i].index = index2[0];
                result.push(this.list[i]);
              }
            }
          }
          //实现优先级排序
          result.sort(function(a,b){return a.index-b.index});
          // console.log(result);
          this.returnlist = result;
        }, 100)
      }
    }
  }
</script>

<style>
  #searchclick.btn.btn-primary{
  background-color: #ffd808;
    font-size: 18px;
  font-weight: bold;
}
#searchclick.btn.btn-primary:hover{
  background-color: #000000;
}
  .el-input__inner {
  height: 54px;
  width: 280px;
  padding: 10px;
  border: none;
  border-radius: 28px;
  outline: none;
  margin-bottom: 10px;
}
.el-input__icon {
  position: relative;
  top: 13px;
  left: 12px;
}
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
  background-color: #FFD808;
  color: #fff;
  font-size: 18px;
  font-weight: bold; 
  border-radius: 28px;
}
 .btn-primary:hover {
    background-color: #000000;
    color: #FFD808; 
}
</style>