// 获取弹窗
var modal = document.getElementById('limitModal');
// 打开弹窗的按钮对象
var open = document.getElementById("limitOrderWindow");
// 获取 <span> 元素，用于关闭弹窗
var span = document.querySelector('.close');

// 点击按钮打开弹窗
open.onclick = function() {
    modal.style.display = "block";
}

// 点击 <span> (x), 关闭弹窗
span.onclick = function() {
    modal.style.display = "none";
}

// 在用户点击其他地方时，关闭弹窗
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
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

jQuery(document).ready($("#summit").click(function(e){
    var broker;
    var orderType;
    var product;
    var period;
    var quantity;
    var price;
    var side;
    var trader;
    var traderCompany;
    broker=$("input[name='broker']").val();
    product=$("input[name='product']").val();
    period=$("input[name='period']").val();
    quantity=$("input[name='quantity']").val();
    price=$("input[name='price']").val();
    side=$("input[name='side']").val();
    orderType="LimitOrder";
    trader=getCookie("username");
    traderCompany="MG"

    if(broker==''){
        alert("Please choose a broker");
        return false;
    }
    if(product==''){
        alert("Please choose a product");
        return false;
    }
    if(period==''){
        alert("Please input period");
        return false;
    }
    if(quantity==''){
        alert("Please input quantity");
        return false;
    }
    if(price==''){
        alert("Please input price");
        return false;
    }
    if(side==''){
        alert("Please choose your side");
        return false;
    }

    $.ajax({
        type:"post",
        url:"/depth/limitOrder",
        contentType: "application/json",
        data:JSON.stringify({
            "broker":broker,
            "orderType":orderType,
            "product":product,
            "period":period,
            "quantity":quantity,
            "price":price,
            "side":side,
            "trader":trader,
            "traderCompany":traderCompany
        }),
        /*期望后端返回数据类型是json*/
        dataType:"json",
        /*后端的响应状态码为200时，表示响应成功，触发success*/
        success:function(data){
            var str="";
            /*服务器返回的data是json形式：
             * {
             *    "balance":0,
             *    "password":"123456",
             *    "userId":1,
             *    "username":"user1"
             *  }*/
            /*把data中的username取出来存进cookie，作为浏览器关闭前的全局变量*/
            str+=data.username;//字符串形式
            addCookie("username",str,0);

            window.location.href = "home.html";
        },
        /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
        error:function(){
            alert("dd");
        }
    })
}))
