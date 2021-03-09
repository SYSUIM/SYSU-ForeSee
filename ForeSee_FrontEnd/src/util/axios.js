// import Vue from "vue"
import axios from "axios"

// 环境的切换
// 开发环境
if (process.env.NODE_ENV == 'development') {    
  axios.defaults.baseURL = '/api/';} 
// 生产环境
  else if (process.env.NODE_ENV == 'production') {    
  axios.defaults.baseURL = '/ai/';
}