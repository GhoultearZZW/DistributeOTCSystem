/**
 * Created by loumoon on 2018/6/8.
 */

var webSocket;
function connect(){
    webSocket=new WebSocket("ws://localhost:8081/webSocket-depth");
    webSocket.onmessage=onMessage;
    webSocket.onopen=onOpen;
    webSocket.onerror=onError;
}
function onOpen(){
    alert("Connected");
}
function onError(){
    alert("Error");
}
function onMessage(evt){
    alert("Message");
    alert(evt.data);
}
window.addEventListener("load", connect, false);