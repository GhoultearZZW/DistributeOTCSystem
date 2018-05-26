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

jQuery(document).ready($("#submit").click(function(e){
    var broker;
    var orderType;
    var product;
    var period;
    var quantity;
    var price;
    var side;
    var trader;
    var tradeCompany;

    /*前端所有的input元素,不管type属性是text、number or whatever,js能拿到的用户输入都是字符串类型,
    因此如果需要int、double等类型需要做相应的类型转换，最好用的是Number(string str)方法*/

    broker=$("#inputBroker").find("option:selected").text();
    product=$("#inputProduct").find("option:selected").text();
    period=$("#inputPeriod").find("option:selected").text();

    var quantityStr=$("input[name='quantity']").val();
    quantity=Number(quantityStr);//输入的string转换成int,如果输入不能转化成一个number,返回NaN

    var priceStr=$("input[name='price']").val();
    price=Number(priceStr);//输入的string转换成double

    orderType="LimitOrder";
    trader=getCookie("username");
    tradeCompany="ABC Coop"

    /*取下拉option的text值(就是页面上显示的值),注意也是string类型*/
    var sideText=$("#inputSide").find("option:selected").text();
    if(sideText=="Sell-side"){
        side=Number("0");
    }
    else if(sideText=="Buy-side"){
        side=Number("1");
    }


    /*判定quantity、price的输入是否为空,是否为一个number
    * 注意NaN不能使用==来判定，要用专门的方法isNaN()*/
    if(quantity==''){
        alert("Please input quantity");
        return false;
    }
    else if(isNaN(quantity)){
        alert("Please input an int number as quantity");
        return false;
    }
    else if(price==''){
        alert("Please input price");
        return false;
    }
    else if(isNaN(price)){
        alert("Please input a double number as price");
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
            "tradeCompany":tradeCompany
        }),
        /*因为此次ajax提交后，后端除了响应状态码之外不会返回任何数据，所以dataType属性这里就不设置了，
        如果设置了dataType却没接收到返回数据，即使响应状态码是ok200，也会走error*/

        /*后端的响应状态码为200时，表示响应成功，触发success*/
        success:function(){
            alert("Order submitted successfully!")
            window.location.href = "home.html";
        },
        /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
        error:function(){
            alert("Failed to submit order.");
        }
    })
}))
