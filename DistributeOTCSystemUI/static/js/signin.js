jQuery(document).ready(function(){
    $("#signin").click(function(e){
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
            dataType:"json",
            success:function(resp){
                alert("Sign in successfully!");
                window.location.href = "#";
            },
            error:function(){
                alert("Failed to sign in.You may have entered an incorrect username or password.");
            }
        })
    })

});