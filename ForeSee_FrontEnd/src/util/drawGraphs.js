/*
  drawGraph.js
  包含画图的公共函数
 */

export default {
  datafromutil: "datafromutil",
  testcolor: ["rgb(129, 212, 250)","rgb(239, 154, 154)","rgb(128, 203, 196)","rgb(129, 199, 132)","rgb(156, 204, 101)","rgb(212, 225, 87)","rgb(255, 213, 79)","rgb(255, 167, 38)","rgb(255, 110, 64)","rgb(161, 136, 127)","rgb(248, 187, 208)","rgb(128, 203, 196)","rgb(209, 196, 233)"],
  initial_style: (chart) => {
    // 获取 chart 的父父结点chartParent，再根据其算宽度
    console.log("图开始自适应")
    let chartParent = chart.parentNode.parentNode;
    // chart.style.width = chartParent.clientWidth - 30 + "px";
    // chart.style.margin = "0 auto";
    return chartParent.clientWidth - 30 + "px";
  },
  set_color : () => {
    let testcolor = ["rgb(129, 212, 250)","rgb(239, 154, 154)","rgb(128, 203, 196)","rgb(129, 199, 132)","rgb(156, 204, 101)","rgb(212, 225, 87)","rgb(255, 213, 79)","rgb(255, 167, 38)","rgb(255, 110, 64)","rgb(161, 136, 127)","rgb(248, 187, 208)","rgb(128, 203, 196)","rgb(209, 196, 233)"]
    let num = Math.floor(Math.random() * testcolor.length);
    return testcolor[num]
  },
  sortNumber : (x,y) => {
    return x["value"]-y["value"];
  },
  get_detail: (name, detail_list) => {
    if (detail_list) {
      let str = "";
      detail_list.forEach(el => {
        if (el.name === name) {
          // 把name放第一位
          str += "NAME: " + name + "<br/>";
          for (let i in el) {
            if(i === "name") continue;
            str += i.toUpperCase() + ": ";
            str += (el[i] ? el[i] : " ");
            str += "<br/>";
          }
        }
      });
      if(!str) str = "NAME: " + name;
      return str;
    } else {
      return "NAME: " + name;
    }
  }
}

// exports.install = function (Vue, options) {
//   Vue.prototype.ajax = function (){
//       console.log('aaaaaaa');
//   };
//   Vue.prototype.initial_style = function(chart) {
//     console.log("图开始自适应")
//     let chartParent = chart.parentNode.parentNode;
//     // chart.style.width = chartParent.clientWidth - 30 + "px";
//     // chart.style.margin = "0 auto";
//     return chartParent.clientWidth - 30 + "px";
//   };
//   Vue.prototype.set_color = function() {
//     let testcolor = ["rgb(129, 212, 250)","rgb(239, 154, 154)","rgb(128, 203, 196)","rgb(129, 199, 132)","rgb(156, 204, 101)","rgb(212, 225, 87)","rgb(255, 213, 79)","rgb(255, 167, 38)","rgb(255, 110, 64)","rgb(161, 136, 127)","rgb(248, 187, 208)","rgb(128, 203, 196)","rgb(209, 196, 233)"]
//     let num = Math.floor(Math.random() * testcolor.length);
//     return testcolor[num]
//   }
// };