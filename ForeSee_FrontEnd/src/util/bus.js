/*
bus作为数据存储中心，并负责组件间数据通信
在 main.js 中已将 bus 写入vue的原型，可用 this.$bus 全局调用bus

组件直接使用bus的数据
使用 $bus 的 methods 获取数据

事件监听
发送 this.$bus.$emit(eventname, value)
监听 this.$bus.$on(eventname, function(value){ 对应操作 })

资料来源：https://www.jb51.net/article/136758.htm
*/

import Vue from 'vue';

export default new Vue({
  methods: {
    get_query_text() {
      return this.query_data.query_text;
    },
    return_testdata() {
      return this.test_data;
    },
    update_query_text(val) {
      if (val != "") {
        this.query_data.query_text = val;
      }
    },
    // 接收数据结果
    receive_result(val) {
      if(val === "   [delete] Deleting directory /var/www/html/ai/MINA_STEP1/classes/main") {
        console.log("error: 114 server is not running!");
        // el组件，警告框
        this.$notify.error({
          title: 'ERROR',
          message: "114 server is not running!",
          offset: 100
        });
      }
      else if(val === "114Wrongnull") {
        // el组件，警告框
        this.$notify.error({
          title: 'ERROR',
          message: "Back end error! " + val,
          offset: 100,
          duration: 0
        });
      }
      else {
        this.highlight_query(val);
        this.query_data.query_result = val;
      }
      // 停止 loading
      this.$bus.$emit("changeLoading", false)
    },
    // 取出数据
    get_result() {
      return this.query_data.query_result;
    },
    // 取出图的数据
    get_result_graph() {
      return this.query_data.query_result.visual;
    },
    clean_last_result() {
      this.query_data.query_result = "";
    },
    // 高亮检索词前的处理
    highlight_query(data) {
      // 使用 replace() 方法和正则 RegExp，将样式写入结果的query中
      let query = this.get_query_text().toUpperCase();
      // i不区分大小写，g全文匹配
      let replaceReg = new RegExp(query, "gi");
      // let replaceStr = "<span style=\"background-color:rgba\(250,250,0,0.5\);font-weight:normal;\">" + query + "</span>";
      // let replaceStr = "<span style=\"background-color:#8FC2FF;\">" + query + "</span>";
      let replaceStr = "<span style=\"text-decoration:underline;display:inline-block;margin:0 .1em;color:#409EFF;\">" + query + "</span>";
      for(let type in data) {
        if(type === "visual") continue;
        // 进入paper，news层级
        for(let item in data[type]) {
          for(let item_data in data[type][item]) {
            let info = data[type][item][item_data]
            if(typeof(info) === "string") {
              data[type][item][item_data] = data[type][item][item_data].replace(replaceReg, replaceStr);
            }
            // typeof()中数组也返回"object"
            else if(typeof(info) === "object") {
              data[type][item][item_data].forEach(e => {
                e = e.replace(replaceReg, replaceStr);
              });
            }
          }
        }
      }
    }
  },
  // created() {
  //   // 监听事件
  //   this.$on("update_data", (val) => {
  //     this.data = val;
  //   })
  // },
  data() {
    return {
      data: "origin data",
      query_data: {
        query_text: "",
        type: "",
        seq: "",
        query_result: {}
      },
    }
  }
});