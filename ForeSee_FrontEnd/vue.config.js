module.exports = {
    devServer: {
        port: 8888,
        host: "0.0.0.0",
        proxy: {
            '/api': {
                // target: 'http://localhost:8084',
                target: 'http://180.76.249.27:80/weibo/',
                ws: true,  
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            },
        }
    },

    publicPath: "./",
    outputDir: undefined,
    assetsDir: 'static',
    runtimeCompiler: undefined,
    productionSourceMap: undefined,
    parallel: undefined,
    css: undefined,

    configureWebpack:{
		externals:{
	   	   'BMap': 'BMap',
	   	   'BMap_Symbol_SHAPE_POINT':'BMap_Symbol_SHAPE_POINT'
	    }
	}
};