var WS = {};
var ws_url = 'ws://' + window.location.hostname + ':2048/ws';

var MSG_CMD = {
    CONNECT: 1, // 第一次(或重连)初始化连接
    CHAT: 2, // 聊天消息
    SIGNED: 3, // 消息签收
    KEEPALIVE: 4,// 客户端保持心跳
    GROUP_MSG: 5, // 群消息
    ONLINE_STATUS: 6, // 在线状态
    MSG_BOX: 7, // 消息盒子的消息数量
}

/**
 * 初始化连接
 */
WS.wscall = {}
WS.init = function () {
    try {
        if (!window.WebSocket) window.WebSocket = window.MozWebSocket;
        if (window.WebSocket){
            WS.socket = new WebSocket(ws_url);

            WS.socket.onmessage = function (event) {
                WS.wscall.onmessage(event)
            }
            WS.socket.onopen = function (event) {
                WS.wscall.onopen(event)
            }
            WS.socket.onclose = function (event) {
                WS.wscall.onclose(event)
            }
            WS.socket.onerror = function () {
                WS.wscall.onerror()
            }
        }else{
            alert('您的浏览器不支持WebSocket协议,无法进行连接')
        }
    }catch (e) {
        WS.reconnect()
    }
}
/**
 * 重新连接
 */
WS.reconnect = function() {
    if (WS.socket && WS.socket.readyState == WebSocket.OPEN) return;
    setTimeout(function () {
        WS.init()
    },2*1000)
}

/**
 * 发送数据
 * @param cmd 指令
 * @param message 消息体
 */
WS.sendMessage = function(cmd,message){
    if (!cmd || !message || message.length < 1){
        return
    }
    if(!window.WebSocket){
        return;
    }
    //当websocket状态打开
    if(WS.socket.readyState == WebSocket.OPEN){
        var msg = {
            cmd: cmd,
            message: JSON.stringify(message)
        }
        WS.socket.send(JSON.stringify(msg));
    }else{
        if(cmd !== 4){
            alert("连接没有开启");
        }else{
            // 心跳包尝试重连
            WS.reconnect()
        }
    }
}

/**
 * 每30秒进行心跳检查
 */
WS.keepAlive = function (mine) {
    setInterval(() => {
        console.log(mine);
        console.log('keep-alive , id = '+mine.id);
        WS.sendMessage(MSG_CMD.KEEPALIVE,mine)
    },30*1000)
}