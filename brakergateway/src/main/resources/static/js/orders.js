/**
 * Created by loumoon on 2018/6/5.
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

$("#orderType").val(getCookie("orderType"));
$("#product").val(getCookie("product"));
$("#period").val(getCookie("period"));


$("#view").click(function(e) {
    var orderType=$("#orderType").find("option:selected").text();
    var product=$("#product").find("option:selected").text();
    var period=$("#period").find("option:selected").text();
    if(orderType==''||orderType=="orderType"){
        alert("Please choose one orderType!");
        return false;
    }
    if(product==''||product=="product"){
        alert("Please choose one product!");
        return false;
    }
    if(period==''||period=="period"){
        alert("Please choose one period!");
        return false;
    }
    addCookie("orderType",orderType,0);
    addCookie("product",product,0);
    addCookie("period",period,0);
    location.reload();//刷新页面
})


var drawTable=function(e) {
    var orders;
    var orderType=getCookie("orderType");
    var product = getCookie("product");
    var period = getCookie("period");
    if(orderType=="AllOrders") {
        $.ajax({
            type: "post",
            url: "/orders",
            async: false,
            data: JSON.stringify({
                "product": product,
                "period": period,
                "broker": getCookie("username")
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                orders = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get orders!");
            }
        })
    }
    else if(orderType=="FinishedOrders"){
        $.ajax({
            type: "post",
            url: "/orders/finished",
            async: false,
            data: JSON.stringify({
                "product": product,
                "period": period,
                "broker": getCookie("username")
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                orders = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get orders!");
            }
        })
    }
    else if(orderType=="UnfinishedOrders"){
        $.ajax({
            type: "post",
            url: "/orders/unfinished",
            async: false,
            data: JSON.stringify({
                "product": product,
                "period": period,
                "broker": getCookie("username")
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                orders = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get orders!");
            }
        })
    }
    else if(orderType=="StoppedOrders"){
        $.ajax({
            type: "post",
            url: "/orders/stopped",
            async: false,
            data: JSON.stringify({
                "product": product,
                "period": period,
                "broker": getCookie("username")
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                orders = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get orders!");
            }
        })
    }
    else if(orderType=="CancelledOrders"){
        $.ajax({
            type: "post",
            url: "/orders/canceled",
            async: false,
            data: JSON.stringify({
                "product": product,
                "period": period,
                "broker": getCookie("username")
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                orders = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get orders!");
            }
        })
    }


    /*将json数组depth以表格的形式绘制出来*/
    $('#table').bootstrapTable({
        columns: [
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
                title: 'dealPrice',
                sortable:true
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
                title: 'restQuantity',
                sortable:true
            },
            {
                field: 'status',
                title: 'status',
                sortable:true//可按此属性排序
            }],
        data: orders,//数据集
        cache: false,//是否使用缓存，默认为true
        toolbar: '#toolbar', //工具按钮放在id为toolbar的div块中
        striped: true,//是否显示行间隔色
        pagination: true,//分页
        paginationLoop:false,//分页条不可循环，比如处于最后一页时无法点击下一页
        sidePagination: "client",//客户端分页，适合数据量较小的表格
        search: true,//是否显示表格搜索栏，此搜索属于客户端搜索
        pageSize: 10,//一页的条目数
        pageList: [10, 25, 50, 100],//可选择的每页条目数
        showToggle: true,//是否显示详细视图以及切换按钮
        showColumns: true,//是否显示所有的列
        showRefresh: false,//是否显示刷新按钮
        clickToSelect: true,//是否启用点击选中行
        sortable: true,//是否启用排序
        sortOrder: "asc",//排序方式
        sortName: 'status',//默认按照status排序
        rowStyle: function (row, index) {//rowStyle参数:给不同状态的订单显示不同的颜色
            //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
            var strColor = "";
            if (row.status == "0") {
                strColor = 'success';//finished order，绿色
            }
            else if(row.status=="2"){
                strColor='warning';//stopped order，黄色
            }
            else if(row.status=="3"){
                strColor='danger';//cancelled order，红色
            }
            else {
                return {};//unfinished order，默认白色
            }
            return { classes: strColor }
        },
    })
}

drawTable();