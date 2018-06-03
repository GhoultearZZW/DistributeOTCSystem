/**
 * Created by loumoon on 2018/5/29.
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
/*向后台发送ajax请求并用返回值绘制bootstrap表格*/
var refresh=function() {
    var depth;//全局变量,保存后台响应的depth数据集,其类型是JSON数组(JSONArray)
    $.ajax({
        type: "post",
        url: "/depth",
        /*async属性默认是true,即为异步方式,ajax向后端发送请求后,会继续执行ajax后面的脚本,
         直到接收到后端返回数据,触发ajax里的success方法,这时候执行的是两个线程
         async设置为false,则所有的请求均为同步请求,在ajax接收到后端的返回值之前,同步请求将锁住浏览器,
         ajax后面的js脚本必须等到整个ajax请求过程完成后才能执行*/
        async: false,//我们必须要等接收到后台返回的depth数据集之后才可以执行后面绘制表格的脚本,不然表格将为空
        contentType: "application/json",
        data: JSON.stringify({
            "period": depth_period,
            "product": depth_product
        }),
        dataType: "json",
        /*后端的响应状态码为200时，表示响应成功，触发success*/
        success: function (response) {
            depth = response;//将后台响应的depth数据集存进全局变量depth中
        },
        /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
        error: function () {
            alert("Failed to get market depth");
        }
    })

    /*depth类型是json对象的数组,格式:[{"price":50.38,"Sell Vol":500},
     {"price":40,"Sell Vol":2000},
     {"price":9,"Sell Vol":60},
     {"price":8,"Buy Vol":30}]*/
    /*处理depth,将每一项json增加Buy Level、Sell Level属性,这两个属性是后台传来的json中不包含的*/
    for (var i = 0; i < depth.length; ++i) {
        if (depth[i]["Sell Vol"] == undefined) {
            /*为sell order增加Sell Level属性*/
            for (var j = i - 1; j >= 0; --j) {
                depth[j]["Buy Vol"] = "";
                depth[j]["Sell Level"] = i - j;
                depth[j]["Buy Level"] = "";
            }
            /*为buy order增加Buy Level属性*/
            for (var k = i; k < depth.length; ++k) {
                depth[k]["Sell Vol"] = "";
                depth[k]["Buy Level"] = k - i + 1;
                depth[k]["Sell Level"] = "";
            }
        }
    }

    /*因为是每隔一秒钟刷新一次页面，每次初始化table之前必须先destroy*/
    $("#table").bootstrapTable('destroy');

    /*将json数组depth以表格的形式绘制出来*/
    $('#table').bootstrapTable({
        columns: [
            {
                field: 'Buy Level',
                title: 'Level'
            },
            {
                field: 'Buy Vol',
                title: 'Buy Vol'
            },
            {
                field: 'price',
                title: 'Price'
            },
            {
                field: 'Sell Vol',
                title: 'Sell Vol'
            },
            {
                field: 'Sell Level',
                title: 'Level'
            }],
        data: depth,
        cache:false,//是否使用缓存，默认为true
        toolbar: '#toolbar', //工具按钮放在id为toolbar的div块中
        striped:false,//是否显示行间隔色
        pagination:true,//分页
        sidePagination: "client",//客户端分页，适合数据量较小的表格
        search:true,//是否显示表格搜索栏，此搜索属于客户端搜索
        pageSize: 9,//一页的条目数
        pageNumber:Number(getCookie("depthPageNumber")),
        showToggle: true,//是否显示详细视图以及切换按钮
        showColumns:true,//是否显示所有的列
        showRefresh:false,//是否显示刷新按钮
        clickToSelect:true,//是否启用点击选中行
        onPageChange:function(number,size){//当更改页码或页面大小时触发
            addCookie("depthPageNumber",number,0);
        }
    })
    $("#b").click(function(e){
        $('#table').bootstrapTable('selectPage', 3);
    })
}
/*全局变量,请求depth的product和period,保存在cookie中*/
var depth_product=getCookie("depth_product");
var depth_period=getCookie("depth_period");
/*js轮询,每隔1s调用refresh函数,重新进行ajax请求并绘制表格*/
window.setInterval(refresh,1000);