function check(form) {

    if(form.inputUsername.value==''){
        alert("Please create your username")
        return false;
    }
    if(form.inputPassword.value==''){
        alert("Please input your password")
        return false;
    }
    if(form.confirmPassword.value==''){
        alert("Please confirm your password")
        return false;
    }
    if(form.inputPassword.value!=form.confirmPassword.value){
        alert("The two passwords you typed do not match.")
        return false;
    }
    document.signup.submit();
}