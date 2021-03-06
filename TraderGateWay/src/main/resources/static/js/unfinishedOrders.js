/**
 * Created by loumoon on 2018/6/2.
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
/*表中左边栏绘制cancel按钮*/
function addCancelButton(value,row,index){
    return[
        '<button id="cancel" type="button" class="btn btn-danger btn-sm">cancel</button>'].join("")
}
/*为点击cancel按钮定义触发的事件*/
window.operateCancelEvent={
    "click #cancel":function(e,value,row,index){
        var orderType="CancelOrder";
        var orderId=row.orderId;//选中行的orderId
        $.ajax({
            type:"post",
            url:"/depth/order",
            contentType: "application/json",
            data:JSON.stringify({
                "orderType":orderType,
                "orderId":orderId
            }),
            success:function(){
                alert("The request to cancel the order has been submitted!");
                window.location.href = "unfinishedOrders.html";
            },
            error:function(){
                alert("Failed to summit request");
            }
        })
    }
}

var username=getCookie("username");
var urlStr="/user/"+username+"/orders/unfinished";
var unfinishedOrders;

$.ajax({
    type:"get",
    url: urlStr,
    async: false,
    contentType: "application/json",

    /*后端的响应状态码为200时，表示响应成功，触发success*/
    success:function(response){
        unfinishedOrders=response;
    },
    /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
    error:function(){
        alert("Failed to get unfinished orders");
    }
})


/*将json数组depth以表格的形式绘制出来*/
$('#table').bootstrapTable({
    columns: [
        {
            field:'cancel',
            title:'cancel',
            formatter:addCancelButton,//增加cancel按钮
            events:operateCancelEvent//点击cancel触发事件
        },
        {
            field: 'orderId',
            title: 'orderId'
        },
        {
            field: 'orderType',
            title: 'orderType',
            sortable:true
        },
        {
            field: 'product',
            title: 'product',
            sortable:true
        },
        {
            field: 'period',
            title: 'period',
            sortable:true
        },
        {
            field: 'broker',
            title: 'broker',
            sortable:true
        },
        {
            field: 'quantity',
            title: 'quantity',
            sortable:true
        },
        {
            field: 'price',
            title: 'price',
            sortable:true
        },
        {
            field: 'dealPrice',
            title: 'dealPrice'
        },
        {
            field: 'side',
            title: 'side',
            sortable:true
        },
        {
            field: 'orderTime',
            title: 'orderTime',
            sortable:true//可按此属性排序
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
    data: unfinishedOrders,//数据集
    cache:false,//是否使用缓存，默认为true
    toolbar: '#toolbar', //工具按钮放在id为toolbar的div块中
    striped:true,//是否显示行间隔色
    pagination:true,//分页
    paginationLoop:false,//分页条不可循环，比如处于最后一页时无法点击下一页
    sidePagination: "client",//客户端分页，适合数据量较小的表格
    search:true,//是否显示表格搜索栏，此搜索属于客户端搜索
    pageSize: 10,//一页的条目数
    pageList: [10, 25, 50, 100],//可选择的每页条目数
    showToggle: true,//是否显示详细视图以及切换按钮
    showColumns:true,//是否显示所有的列
    showRefresh:false,//是否显示刷新按钮
    clickToSelect:true,//是否启用点击选中行
    sortable:true,//是否启用排序
    sortOrder:"asc",//排序方式
    sortName:'orderTime'//默认按照orderTime排序
})