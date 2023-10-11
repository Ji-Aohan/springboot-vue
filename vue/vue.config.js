const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 7070
    },
    chainWebpack: config => {
        config.plugin('html')
            .tap(args => {
                args[0].title = "网页标题";
                return args;
            })
    }
})
