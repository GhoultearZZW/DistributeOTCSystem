/**
 * Created by loumoon on 2018/6/1.
 */
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
var username=getCookie("username");
var urlStr="/user/"+username+"/orders";
var allOrders;//全局变量,保存后台响应的all orders数据集,其类型是JSON数组(JSONArray)

$.ajax({
    type:"get",
    url: urlStr,
    async: false,
    contentType: "application/json",

    /*后端的响应状态码为200时，表示响应成功，触发success*/
    success:function(response){
        allOrders=response;
    },
    /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
    error:function(){
        alert("Failed to get all orders");
    }
})


/*将json数组depth以表格的形式绘制出来*/
$('#table').bootstrapTable({
    columns: [
        {
            field: 'orderId',
            title: 'orderId'
        },
        {
            field: 'orderType',
            title: 'orderType'
        },
        {
            field: 'product',
            title: 'product'
        },
        {
            field: 'period',
            title: 'period'
        },
        {
            field: 'broker',
            title: 'broker'
        },
        {
            field: 'quantity',
            title: 'quantity'
        },
        {
            field: 'price',
            title: 'price'
        },
        {
            field: 'dealPrice',
            title: 'dealPrice'
        },
        {
            field: 'side',
            title: 'side'
        },
        {
            field: 'orderTime',
            title: 'orderTime'
        },
        {
            field: 'trader',
            title: 'trader'
        },
        {
            field: 'tradeCompany',
            title: 'tradeCompany'
        },
        {
            field: 'restQuantity',
            title: 'restQuantity'
        },
        {
            field: 'status',
            title: 'status'
        }],
    data: allOrders,
    striped:true,
    pagination:true,
    sidePagination: "client",
    pageSize: 5,
    pageList: [10, 25, 50, 100],
})