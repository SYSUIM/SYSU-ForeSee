import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)



let router = new VueRouter({
    routes: [
        // {
        //     path: "/",
        //     component: () => import(/*webpackChunkName:"Home"*/'@/views/Home'),
        //     meta: { title: "首页" }
        // },
        {
            path: '/',
            name: 'Index',
            component: () => import(/* webpackChunkName: "Index" */ '@/views/Index'),
            meta: { title: "首页" }
        },
        {
            path: "/detail",
            props: true,
            component: () => import(/*webpackChunkName:"Detail"*/'@/views/Detail'),
            meta: { title: "企业详情" }
        },
        {
            path: "/multi",
            props: true,
            component: () => import(/*webpackChunkName:"Multi"*/'@/views/Multi'),
            meta: { title: "行业详情" }
        },
        {
            path: "/morenews",
            props: true,
            component: () => import(/*webpackChunkName:"MoreNews"*/'@/views/MoreNews'),
            meta: { title: "企业新闻" }
        },
        {
            path: "/morenotices",
            props: true,
            component: () => import(/*webpackChunkName:"MoreNotice"*/'@/views/MoreNotice'),
            meta: { title: "企业公告" }
        },
        {
            path: "/industryrepo",
            props: true,
            component: () => import(/*webpackChunkName:"IndustryReport"*/'@/views/IndustryReport'),
            meta: { title: "行业资讯" }
        },
        {
            path: "/news",
            props: true,
            component: () => import(/*webpackChunkName:"TextAnalysis"*/'@/views/TextAnalysis'),
            meta: { title: "新闻文本" }
        },
        {
            path: "/whole",
            props: true,
            component: () => import(/*webpackChunkName:"Whole"*/'@/views/Whole'),
            meta: { title: "检索结果" }
        },
        {
            path: "/research",
            props: true,
            component: () => import(/*webpackChunkName:"Research"*/'@/views/Research'),
            meta: { title: "研究报告" }
        },
        {
            path: "/about",
            props: true,
            component: () => import(/*webpackChunkName:"About"*/'@/views/About'),
            meta: { title: "关于我们" }
        }
    ],
    scrollBehavior() {
        return { x: 0, y: 0 };
    },
})

router.beforeEach((to, from, next) => {
    // to and from are both route objects. must call `next`.
    if (to.meta.title)
        document.title = to.meta.title
    next()
})
export default router