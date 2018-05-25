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
        success:function(data,textStatus){
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

