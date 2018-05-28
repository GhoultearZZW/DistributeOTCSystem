
var limitModal = document.getElementById('limitModal');
var marketModal=document.getElementById("marketModal");

var openLimitModal = document.getElementById("openLimitModal");
var openMarketModal=document.getElementById("openMarketModal");

var closeLimitModal = document.getElementById("closeLimitModal");
var closeMarketModal=document.getElementById("closeMarketModal");

openLimitModal.onclick = function() {
    limitModal.style.display = "block";
}
openMarketModal.onclick=function(){
    marketModal.style.display="block";
}

closeLimitModal.onclick = function() {
    limitModal.style.display = "none";
}
closeMarketModal.onclick=function(){
    marketModal.style.display="none";
}
// 在用户点击其他地方时，关闭弹窗
window.onclick = function(event) {
    if (event.target == limitModal) {
        limitModal.style.display = "none";
    }
    else if(event.target==marketModal){
        marketModal.style.display="none";
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

jQuery(document).ready(
    /*click事件1：点击提交limit order按钮*/
    $("#submitLimit").click(function(e) {
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

        broker=$("#inputBrokerForLimit").find("option:selected").text();
        product=$("#inputProductForLimit").find("option:selected").text();
        period=$("#inputPeriodForLimit").find("option:selected").text();

        var quantityStr=$("#inputQuantityForLimit").val();
        quantity=Number(quantityStr);//输入的string转换成int,如果输入不能转化成一个number,返回NaN

        var priceStr=$("#inputPriceForLimit").val();
        price=Number(priceStr);//输入的string转换成double

        orderType="LimitOrder";
        trader=getCookie("username");
        tradeCompany="ABC Coop"

        /*取下拉option的text值(就是页面上显示的值),注意也是string类型*/
        var sideText=$("#inputSideForLimit").find("option:selected").text();
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
    }),

    /*click事件2：点击提交market order按钮*/
    $("#submitMarket").click(function(e){
        var broker;
        var orderType;
        var product;
        var period;
        var quantity;
        var side;
        var trader;
        var tradeCompany;

        /*前端所有的input元素,不管type属性是text、number or whatever,js能拿到的用户输入都是字符串类型,
         因此如果需要int、double等类型需要做相应的类型转换，最好用的是Number(string str)方法*/

        broker=$("#inputBrokerForMarket").find("option:selected").text();
        product=$("#inputProductForMarket").find("option:selected").text();
        period=$("#inputPeriodForMarket").find("option:selected").text();

        var quantityStr=$("#inputQuantityForMarket").val();
        quantity=Number(quantityStr);//输入的string转换成int,如果输入不能转化成一个number,返回NaN

        orderType="MarketOrder";
        trader=getCookie("username");
        tradeCompany="ABC Coop"

        /*取下拉option的text值(就是页面上显示的值),注意也是string类型*/
        var sideText=$("#inputSideForMarket").find("option:selected").text();
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

        $.ajax({
            type:"post",
            url:"/depth/marketOrder",
            contentType: "application/json",
            data:JSON.stringify({
                "broker":broker,
                "orderType":orderType,
                "product":product,
                "period":period,
                "quantity":quantity,
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
    }),


    /*click事件3：点击获取gold market depth按钮*/
    $("#goldDepth").click(function(e) {
        alert("xixixi");
    })



)
