var signup=function(e){
    var username;
    var password;
    var confirmpassword;

    username=$("input[name='username']").val();
    password=$("input[name='password']").val();
    confirmpassword=$("input[name='confirmpassword']").val();
    if(username==''){
        alert("Please create your username");
        return false;
    }
    if(password==''){
        alert("Please input your password");
        return false;
    }
    if(confirmpassword==''){
        alert("Please confirm your password");
        return false;
    }
    if(password!=confirmpassword){
        alert("The two passwords you typed do not match");
        return false;
    }

    $.ajax({
        type:"post",
        url:"/broker/create",
        contentType: "application/json",
        data:JSON.stringify({
            "username":username,
            "password":password
        }),
        dataType:"json",
        success:function(resp){
            alert("Sign up successfully!");
            window.location.href = "signin.html";
        },
        error:function(){
            alert("Failed to sign up");
        }
    })
}

jQuery(document).ready(
    $("#signup").click(signup),
    $(".form-signup").keypress(function (e){
        var code = event.keyCode;
        if (13 == code) {
            signup()
        }
    })

);