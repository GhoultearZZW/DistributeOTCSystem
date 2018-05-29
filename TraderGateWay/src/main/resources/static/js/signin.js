/*在cookie中插入键值对，注意是string形式，cookie其实就是一条字符串string*/
function addCookie(name,value,expiresHours){
    var cookieString=name+"="+escape(value);
    //判断是否设置过期时间,如果是0，则cookie在浏览器关闭之前永远有效
    if(expiresHours>0){
        var date=new Date();
        date.setTime(date.getTime+expiresHours*3600*1000);
        cookieString=cookieString+"; expires="+date.toGMTString();
    }
    document.cookie=cookieString;
}

function getCookie(name){
    var strCookie=document.cookie;
    var arrCookie=strCookie.split("; ");
    for(var i=0;i<arrCookie.length;i++){
        var arr=arrCookie[i].split("=");
        if(arr[0]==name)return arr[1];
    }
    return "";
}


/*这个函数在点击登录按钮或者按下回车键后调用*/
var signin=function(e){
    var username;
    var password;

    username=$("input[name='username']").val();
    password=$("input[name='password']").val();
    if(username==''){
        alert("Please input your username");
        return false;
    }
    if(password==''){
        alert("Please input your password");
        return false;
    }

    $.ajax({
        type:"post",
        url:"/user/login",
        contentType: "application/json",
        data:JSON.stringify({
            "username":username,
            "password":password
        }),
        /*期望后端返回数据类型是json*/
        dataType:"json",
        /*后端的响应状态码为200时，表示响应成功，触发success*/
        success:function(response){
            var username="";
            /*服务器返回的response数据是json对象：
            * {
            *    "balance":0,
            *    "password":"123456",
            *    "userId":1,
            *    "username":"user1"
            *  }*/
            /*把response中的username取出来存进cookie，作为浏览器关闭前的全局变量*/
            username+=response.username;//字符串形式
            addCookie("username",username,0);

            window.location.href = "home.html";
        },
        /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
        error:function(){
            alert("Failed to sign in.You may have entered an incorrect username or password.");
        }
    })
}

jQuery(document).ready(
    /*点击登录按钮*/
    $("#signin").click(signin),

    /*敲击回车键*/
    $(".form-signin").keypress(function (e){
        var code = event.keyCode;
        if (13 == code) {
            signin()
        }
    })
);

