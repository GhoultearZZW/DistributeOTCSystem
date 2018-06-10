/**
 * Created by loumoon on 2018/6/2.
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

$("#product").val(getCookie("blotter_product"));
$("#period").val(getCookie("blotter_period"));
$("#broker").val(getCookie("blotter_broker"));

$("#blotter").click(function(e) {
    var product=$("#product").find("option:selected").text();
    var period=$("#period").find("option:selected").text();
    var broker=$("#broker").find("option:selected").text();
    if(product==""||product=="product"){
        alert("Please choose one product!");
        return false;
    }
    else if(period==""||period=="period"){
        alert("Please choose one period!");
        return false;
    }
    else if(broker==""||broker=="broker"){
        alert("Please choose one broker!");
        return false;
    }

    addCookie("blotter_product",product,0);
    addCookie("blotter_period",period,0);
    addCookie("blotter_broker",broker,0);
    //drawTable();
    //window.location.href="blotter.html";
    location.reload();//刷新页面
})


var drawTable=function(e) {
    var blotter;
    var blotter_product = getCookie("blotter_product");
    var blotter_period = getCookie("blotter_period");
    var blotter_broker=getCookie("blotter_broker");
    $.ajax({
        type: "post",
        url: "/blotter",
        async: false,
        data: JSON.stringify({
            "product": blotter_product,
            "period": blotter_period,
            "broker":blotter_broker
        }),
        contentType: "application/json",

        /*后端的响应状态码为200时，表示响应成功，触发success*/
        success: function (response) {
            blotter = response;
        },
        /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
        error: function () {
            alert("Failed to get blotter");
        }
    })

    /*没有自己公司参与的交易是不可以看见交易双方的信息的*/
    for(var i in blotter){
        if(blotter[i].initiatorCompany!=getCookie("company")&&blotter[i].completionCompany!=getCookie("company")){
            blotter[i].initiatorTrader="-";
            blotter[i].initiatorCompany="-";
            blotter[i].completionTrader="-";
            blotter[i].completionCompany="-";
        }
    }

    /*将json数组depth以表格的形式绘制出来*/
    $('#table').bootstrapTable({
        columns: [
            {
                field: 'tradeId',
                title: 'tradeId'
            },
            {
                field: 'broker',
                title: 'broker',
                sortable: true
            },
            {
                field: 'product',
                title: 'product',
                sortable: true
            },
            {
                field: 'period',
                title: 'period',
                sortable: true
            },
            {
                field: 'price',
                title: 'price',
                sortable: true
            },
            {
                field: 'quantity',
                title: 'quantity',
                sortable: true
            },
            {
                field: 'initiatorTrader',
                title: 'initiatorTrader',
                sortable: true
            },
            {
                field: 'initiatorCompany',
                title: 'initiatorCompany',
                sortable: true
            },
            {
                field: 'initiatorSide',
                title: 'initiatorSide',
                sortable: true
            },
            {
                field: 'completionCompany',
                title: 'completionCompany',
                sortable: true//可按此属性排序
            },
            {
                field: 'completionTrader',
                title: 'completionTrader',
                sortable: true
            },
            {
                field: 'completionSide',
                title: 'completionSide',
                sortable: true
            },
            {
                field: 'dealTime',
                title: 'dealTime',
                sortable: true
            }],
        data: blotter,//数据集
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
        sortName: 'dealTime'//默认按照status排序
    })
}

drawTable();