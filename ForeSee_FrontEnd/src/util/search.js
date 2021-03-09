import $bus from "./bus"
import $axios from "axios"
import $router from './router'
// import { type } from "os";
let $echarts = require('echarts');
// import $echarts from 'echarts'

export default {
  to_display: (query) => {
    $router.push({
      path: "/displayInfo",
      query: {
        query: query
      }
    })
  },
  search_query: (query) => {
    // ----------------------------------------------------
    //将数据拼接为php可接收的格式
    let send_query = new URLSearchParams();
    send_query.append('page', '1');
    send_query.append('search', query);
    send_query.append('way', 'way');
    send_query.append('info', 'info');
    send_query.append('Seq', 'correlation');
    // 更新 bus 的数据
    $bus.update_query_text(query);
    $bus.clean_last_result();
    // 发送 post 请求
    console.log("发送请求");

    // 开始 loading……
    $bus.$emit("changeLoading", true)

    // 向114的 MINA 发送请求
    $axios.post("/MINA_STEP1/aitest.php", send_query)
    // 开发环境，请求图的静态数据
    // $axios.get("/api/answer_liushi.json")
      .then(res => {
        console.log("接收数据");
        console.log(res.data)
        $bus.receive_result(res.data);
        // this.$bus.$emit("changeLoading", false)
      })
      .catch(err => {
        console.log("Error" + err);
        $bus.$emit("changeLoading", false)
      });
  }
}