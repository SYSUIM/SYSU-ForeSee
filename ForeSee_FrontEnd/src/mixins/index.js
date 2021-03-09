import Vue from 'vue'
import axios from 'axios'

// axios.defaults.baseURL = "api"

Vue.mixin({
    methods:{
        $get(url,data){
            return axios.get(url,{
                params:data
            })
        },
        $post(url,data){
            return axios.post(url,data)
        }
    }
})