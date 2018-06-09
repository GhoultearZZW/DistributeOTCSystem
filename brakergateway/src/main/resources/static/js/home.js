/**
 * Created by loumoon on 2018/6/9.
 */
/*为cookie赋值的函数*/
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

/*从cookie中取值的函数*/
function getCookie(name){
    var strCookie=document.cookie;
    var arrCookie=strCookie.split("; ");
    for(var i=0;i<arrCookie.length;i++){
        var arr=arrCookie[i].split("=");
        if(arr[0]==name)return arr[1];
    }
    return "";
}

/*click事件1：点击获取gold market depth按钮*/
$("#goldDepth").click(function(e) {
    var period=$("#goldPeriod").find("option:selected").text();
    var broker=$("#goldBroker").find("option:selected").text();
    if(period=="period"){
        alert("Please choose one period!");
        return false;
    }
    else if(broker=="broker"){
        alert("Please choose one broker!");
        return false;
    }
    addCookie("depth_product","gold",0);
    addCookie("depth_period",period,0);
    addCookie("depth_broker",broker,0);
    window.location.href="depth.html";
})

/*click事件2：点击获取silver market depth按钮*/
$("#silverDepth").click(function(e) {
    var period=$("#silverPeriod").find("option:selected").text();
    var broker=$("#silverBroker").find("option:selected").text();
    if(period=="period"){
        alert("Please choose one period!");
        return false;
    }
    else if(broker=="broker"){
        alert("Please choose one broker!");
        return false;
    }
    addCookie("depth_product","silver",0);
    addCookie("depth_period",period,0);
    addCookie("depth_broker",broker,0);
    window.location.href="depth.html";
})

/*click事件3：点击获取oil market depth按钮*/
$("#oilDepth").click(function(e) {
    var period=$("#oilPeriod").find("option:selected").text();
    var broker=$("#oilBroker").find("option:selected").text();
    if(period=="period"){
        alert("Please choose one period!");
        return false;
    }
    else if(broker=="broker"){
        alert("Please choose one broker!");
        return false;
    }
    addCookie("depth_product","oil",0);
    addCookie("depth_period",period,0);
    addCookie("depth_broker",broker,0);
    window.location.href="depth.html";
})

/*click事件4：点击获取soybean market depth按钮*/
$("#soybeanDepth").click(function(e) {
    var period=$("#soybeanPeriod").find("option:selected").text();
    var broker=$("#soybeanBroker").find("option:selected").text();
    if(period=="period"){
        alert("Please choose one period!");
        return false;
    }
    else if(broker=="broker"){
        alert("Please choose one broker!");
        return false;
    }
    addCookie("depth_product","soybean",0);
    addCookie("depth_period",period,0);
    addCookie("depth_broker",broker,0);
    window.location.href="depth.html";
})

