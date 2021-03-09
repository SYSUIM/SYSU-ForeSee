import Vue from 'vue';
import Router from 'vue-router';
import Home from './../views/Home.vue';
import DisplayInfo from './../views/DisplayInfo.vue';
import About from "./../views/About.vue"

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/displayInfo',
      name: 'displayInfo',
      component: DisplayInfo
    },
    {
      path: "/about",
      name: "about",
      component: About
    }
  ]
})
