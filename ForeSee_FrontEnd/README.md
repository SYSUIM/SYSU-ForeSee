# ForeSee_FrontEnd

### 1. Project setup
```
npm install
```

### 2. Compiles and hot-reloads for development
```
npm run serve
```

### 3. Compiles and minifies for production
```
npm run build
```
1. 静态资源文件需要放在public文件夹，如img/css/icon/json等
2. 可以使用混入mixin，将axios变成vue实例的一部分，方便各组件调用请求
3. 为避免main.js文件复杂，可将路由配置提取到router/index.js中
4. 为避免所有组件打包到app.js导致文件过大，首屏加载缓慢，可以使用路由懒加载来分别打包成多个文件，可以使用注释分组打包
    ```
    component: () => import(/* webpackChunkName:"Home" */ '@/page/Home')
    ```
5. 路由中可以使用meta保存元数据信息，如title,是否需要登录等
6. 导航：路由发生改变，页面跳转
7. 导航守卫：拦截器
    ```
    //导航守卫 路由跳转之前会被此拦截
    // to 跳转到的页面 from 源页面 next 调用该方法后才跳转

    router.beforeEach((to, from, next) => { 
        //设置标题、判断登录操作等操作 
        next()
    })

    router.afterEach(() => {
    })
    ```
8. axios请求方法
    ```
    axios.get({url,
        params:{
            id:1
        }
    })

    axios.post({
        id:1
    })

    //同时发起多个请求
    
    axios.all

    axios({
        method:'post',
        url:'/user/12345',
        data:{
            id:1
        }
    })
    ```
