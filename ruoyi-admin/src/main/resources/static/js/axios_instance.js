const request = axios.create({
    baseURL: 'http://'+window.location.host+'/',
    timeout: 7000
});
// 添加请求拦截器
request.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
request.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    const res = response.data
    if (response.status < 200 || response.status > 300 || (!!res.code && res.code !== 0 && res.code !== 1000)) {
        layer.msg(res.message || 'Error')
        return Promise.reject(new Error(res.message || 'Error'))
    }
    return res;
}, function (error) {
    let message = ''
    if (error && error.response){
        switch (error.response.status) {
            case 401:
                location.href = ctx + '/login'
                return
            case 403:
                message = error.response.data.path + ',' + error.response.data.message
                break
            case 429:
                message = '访问太过频繁，请稍后再试!'
                break
            default:
                message = error.response.data.message ? error.response.data.message : '服务器错误'
                break
        }
    }else{
        message = '连接服务器失败'
    }
    layer.msg(message)
    // 对响应错误做点什么
    return Promise.reject(error);
});