/**
 * Created by loumoon on 2018/6/8.
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
    //var depth;//全局变量,保存后台响应的depth数据集,其类型是JSON数组(JSONArray)
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
            //"period": depth_period,
            //"product": depth_product,
            //"broker": depth_broker
            "period":"SEP16",
            "product":"gold",
            "broker":"M"
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
            marketPricePage=Math.ceil(i/pageSize);//更新market price所在页码，注意js除法结果带小数位，Math.ceil()向上取整
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
                title: 'Level',
                align: 'center'
            },
            {
                field: 'Buy Vol',
                title: 'Buy Vol',
                align: 'center'
            },
            {
                field: 'price',
                title: 'Price',
                align: 'center',
                cellStyle:function(value,row,index){
                    if (row["Sell Level"]==1){
                        return {classes: 'danger'}
                    }
                    else if(row["Sell Level"]==2||row["Sell Level"]==3){
                        return {classes: 'warning'}
                    }
                    else if(row["Buy Level"]==1||row["Buy Level"]==2||row["Buy Level"]==3){
                        return {classes:'success'}
                    }
                    else{
                        return {}
                    }
                }
            },
            {
                field: 'Sell Vol',
                title: 'Sell Vol',
                align: 'center'
            },
            {
                field: 'Sell Level',
                title: 'Level',
                align: 'center'
            }],
        data: depth,
        cache:false,//是否使用缓存，默认为true
        striped:true,//是否显示行间隔色
        pagination:true,//分页
        paginationLoop:false,//分页条不可循环，比如处于最后一页时无法点击下一页
        pageList:[pageSize],//只提供固定pageSize的分页
        sidePagination: "client",//客户端分页，适合数据量较小的表格
        pageSize: pageSize,//一页的条目数
        pageNumber:depthPageNumber,//初始时显示的页码
        clickToSelect:true,//是否启用点击选中行
        onPageChange:function(number,size){//当更改页码或页面大小时触发
            depthPageNumber=number;
        }
    })
    $("#viewMarketPrice").click(function(e){
        $('#table').bootstrapTable('selectPage', marketPricePage);
    })
}
/*全局变量,请求depth的product和period,保存在cookie中*/
var depth_product=getCookie("depth_product");
var depth_period=getCookie("depth_period");
var depth_broker=getCookie("depth_broker");
var depthPageNumber=1;//用户所处的当前页，保存在一个全局变量中，这样每次表格刷新后依然处于用户所在的页码，而不是跳到第一页，一定要有初始化的值1，不然第一次绘表显示时会出错
var marketPricePage=1;//market price所在的页码，每次绘表都会更新它，便于用户直接跳转到这一页
var pageSize=9;//每页的行数

var depth;
refresh();











var webSocket;
function connect(){
    webSocket=new WebSocket("ws://localhost:8081/webSocket-depth");
    webSocket.onmessage=onMessage;
    webSocket.onopen=onOpen;
    webSocket.onerror=onError;
}
function onOpen(){
    alert("Connected");
}
function onError(){
    alert("Error");
}
function onMessage(evt){
    var jsonArray=JSON.parse(evt.data);
    var depthChange=[];
    for(var i in jsonArray){
        if(jsonArray[i].product=="gold"&&jsonArray[i].period=="SEP16"&&jsonArray[i].broker=="M"){
            depthChange.push(jsonArray[i]);
        }
    }
    for(var i in depthChange){
        for(var j=0;j<depth.length;j++){
            if(depthChange[i].price==depth[j].price){
                if(depthChange[i].side==0) {
                    depth[j]["Sell Vol"]=depthChange[i].quantity;
                    alert(depthChange[i].quantity);
                    $('#table').bootstrapTable('updateCell', {index:j, field: 'Sell Vol',value: depthChange[i].quantity});
                }
                else if(depthChange[i].side==1){
                    depth[j]["Buy Vol"]=depthChange[i].quantity;
                    $('#table').bootstrapTable('updateCell', {index:j, field: 'Buy Vol',value: depthChange[i].quantity});
                }
                else if(depthChange[i].side==undefined){
                    var buyVol=depth[j]["Buy Vol"];
                    /*发现一件特别恐怖的事情，当删除table的某行时，会自动删除数据集depth的对应项！
                    **也就是会自动执行depth.splice(j,1)，debug大坑！因此我在删除行之前先用buyVol保存下删除前的depth[j]*/
                    $('#table').bootstrapTable('remove',{field:'price',values:[depthChange[i].price]});
                    if(buyVol==""){
                        for(var k=j-1;k>=0;k--){
                            depth[k]["Sell Level"]--;
                            $('#table').bootstrapTable('updateCell', {index:k, field: 'Sell Level',value: depth[k]["Sell Level"]});
                        }
                    }
                    else{
                        for(var k=j;k<depth.length;k++){
                            depth[k]["Buy Level"]--;
                            $('#table').bootstrapTable('updateCell', {index:k, field: 'Buy Level',value: depth[k]["Buy Level"]});
                        }
                    }
                    //depth.splice(j,1);不需要再次执行了
                }
            }
        }
        var newPrice=depthChange[i].price;



    }
}
window.addEventListener("load", connect, false);