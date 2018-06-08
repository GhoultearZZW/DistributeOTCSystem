jQuery(document).ready(function () {
    $("#signin").click(function (e) {
        var username;
        var password;

        username = $("input[name='username']").val();
        password = $("input[name='password']").val();
        if (username == '') {
            alert("Please input your username");
            return false;
        }
        if (password == '') {
            alert("Please input your password");
            return false;
        }

        $.ajax({
            type: "post",
            url: "http://localhost:8080/user/login",
            contentType: "application/json",
            data: JSON.stringify({
                "username": "user1",
                "password": "123456"
            }),
            /*期望后端返回数据类型是json*/
            dataType: "json",
            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (data, textStatus) {
                alert("Sign in successfully!");
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to sign in.You may have entered an incorrect username or password.");
            }
        })
    })

});