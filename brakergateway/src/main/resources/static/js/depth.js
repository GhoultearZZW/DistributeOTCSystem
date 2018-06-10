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
var initTable=function() {
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
            "period":depth_period,
            "product":depth_product,
            "broker":depth_broker
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
            break;
        }
        /*当depth里只有卖的订单时*/
        else if(i==depth.length-1){
            for(var j=i;j>=0;j--){
                depth[j]["Buy Vol"]="";
                depth[j]["Buy Level"]="";
                depth[j]["Sell Level"]=i-j+1;
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
        clickToSelect:true,//是否启用点击选中行
        /*onPageChange:function(number,size){//当更改页码或页面大小时触发
            depthPageNumber=number;
        }*/
    })
}

$("#viewMarketPrice").click(function(e){
    $('#table').bootstrapTable('selectPage', marketPricePage);
})

function connect(){
    webSocket=new WebSocket("ws://localhost:8081/webSocket-depth");
    webSocket.onmessage=onMessage;
    webSocket.onopen=onOpen;
    webSocket.onerror=onError;
}
function onOpen(){
    //alert("Connected");
}
function onError(){
    alert("Error happened in webSocket connection!");
}
/*js是单线程、事件触发的，就是说一次触发onMessage还没执行完就来了下一个触发，不会并行执行，而是等这个执行完了再执行下一个*/
function onMessage(evt){
    /*任何产品depth的变动，服务器都会推信息过来，是一个jsonArray*/
    var jsonArray=JSON.parse(evt.data);
    var depthChange=[];
    /*我们需要过滤出当前depth页面需求的depth变动，即满足本页面请求的product、period、broker，保存在数组depthChange中*/
    for(var i in jsonArray){
        if(jsonArray[i].product==depth_product&&jsonArray[i].period==depth_period&&jsonArray[i].broker==depth_broker){
            depthChange.push(jsonArray[i]);
        }
    }
    /*迭代处理所有的depth变动*/
    for(var i in depthChange){
        /*如果当前depth深度为0，过来的变动肯定是增加的，直接插进去*/
        if(depth.length==0){
            if(depthChange[i].side==0){
                $('#table').bootstrapTable('insertRow', {
                    index: 0,
                    row: {
                        'Buy Vol': "",
                        'Buy Level': "",
                        'price': depthChange[i].price,
                        'Sell Vol': depthChange[i].quantity,
                        'Sell Level': 1
                    }
                });
            }
            else if(depthChange[i].side==1){
                $('#table').bootstrapTable('insertRow', {
                    index: 0,
                    row: {
                        'Sell Vol': "",
                        'Sell Level': "",
                        'price': depthChange[i].price,
                        'Buy Vol': depthChange[i].quantity,
                        'Buy Level': 1
                    }
                });
            }
        }
        /*如果当前depth深度不为零*/
        else {
            for (var j = 0; j < depth.length; j++) {
                /*如果表中有相同的价格，说明这次变动有两种可能情况：改变一条depth的Sell Vol/Buy Vol或者直接整条删除*/
                if (depthChange[i].price == depth[j].price) {
                    /*仅改变一条depth的Sell Vol*/
                    if (depthChange[i].side == 0) {
                        depth[j]["Sell Vol"] = depthChange[i].quantity;
                        $('#table').bootstrapTable('updateCell', {
                            index: j,
                            field: 'Sell Vol',
                            value: depthChange[i].quantity
                        });
                    }
                    /*仅改变一条depth的Buy Vol*/
                    else if (depthChange[i].side == 1) {
                        depth[j]["Buy Vol"] = depthChange[i].quantity;
                        $('#table').bootstrapTable('updateCell', {
                            index: j,
                            field: 'Buy Vol',
                            value: depthChange[i].quantity
                        });
                    }
                    /*说明是整条删除，同时要更新影响项的level，
                      删除变动的json格式：{"product":"gold","period":"SEP18","broker":"M","price":40,"status":0}
                      插入或者改变Vol的变动的json格式：{"product":"gold","period":"SEP18","broker":"M","side":0,"price":60,"quantity":1500}*/
                    else if (depthChange[i].side == undefined) {
                        var buyVol = depth[j]["Buy Vol"];
                        /*发现一件特别恐怖的事情，当删除table的某行时，会自动删除数据集depth的对应项！
                         **也就是会自动执行depth.splice(j,1)，debug大坑！因此我在删除行之前先用buyVol保存下删除前的depth[j]*/
                        $('#table').bootstrapTable('remove', {field: 'price', values: [depthChange[i].price]});
                        if (buyVol == "") {
                            for (var k = j - 1; k >= 0; k--) {
                                depth[k]["Sell Level"]--;
                                $('#table').bootstrapTable('updateCell', {
                                    index: k,
                                    field: 'Sell Level',
                                    value: depth[k]["Sell Level"]
                                });
                            }
                        }
                        else {
                            for (var k = j; k < depth.length; k++) {
                                depth[k]["Buy Level"]--;
                                $('#table').bootstrapTable('updateCell', {
                                    index: k,
                                    field: 'Buy Level',
                                    value: depth[k]["Buy Level"]
                                });
                            }
                        }
                        //depth.splice(j,1);不需要再次执行了
                        /*维护marketPricePage*/
                        for (var p = 0; p < depth.length; p++) {
                            if (depth[p]["Buy Level"] == 1) {
                                marketPricePage = Math.ceil(p / pageSize);
                                break;
                            }
                        }
                    }
                    break;
                }
                /*没有找到相等的price却大于了一个price，说明是新增一条depth*/
                else if (depthChange[i].price > depth[j].price) {
                    if (depthChange[i].side == 0) {
                        var sellLevel;
                        if (depth[j]["Sell Level"] == "") {
                            sellLevel = 1;
                        }
                        else {
                            sellLevel = depth[j]["Sell Level"] + 1;
                        }
                        $('#table').bootstrapTable('insertRow', {
                            index: j,
                            row: {
                                'Buy Vol': "",
                                'Buy Level': "",
                                'price': depthChange[i].price,
                                'Sell Vol': depthChange[i].quantity,
                                'Sell Level': sellLevel
                            }
                        });
                        for (var k = j - 1; k >= 0; --k) {
                            depth[k]["Sell Level"]++;
                            $('#table').bootstrapTable('updateCell', {
                                index: k,
                                field: 'Sell Level',
                                value: depth[k]["Sell Level"]
                            });
                        }
                    }
                    else if (depthChange[i].side == 1) {
                        var buyLevel = depth[j]["Buy Level"];
                        $('#table').bootstrapTable('insertRow', {
                            index: j,
                            row: {
                                'Sell Vol': "",
                                'Sell Level': "",
                                'price': depthChange[i].price,
                                'Buy Vol': depthChange[i].quantity,
                                'Buy Level': buyLevel
                            }
                        });
                        for (var k = j + 1; k < depth.length; k++) {
                            depth[k]["Buy Level"]++;
                            $('#table').bootstrapTable('updateCell', {
                                index: k,
                                field: 'Buy Level',
                                value: depth[k]["Buy Level"]
                            });
                        }
                    }
                    for (var p = 0; p < depth.length; p++) {
                        if (depth[p]["Buy Level"] == 1) {
                            marketPricePage = Math.ceil(p / pageSize);
                            break;
                        }
                    }
                    break;
                }
                /*直到遍历到最后一条depth也没有相同的price，说明是增加一条新的depth，而且price还是最小的，要考虑sell buy两种情况*/
                else if (j == depth.length - 1) {
                    if(depthChange[i].side==0){
                        $('#table').bootstrapTable('insertRow', {
                            index: j + 1,
                            row: {
                                'Buy Vol': "",
                                'Buy Level': "",
                                'price': depthChange[i].price,
                                'Sell Vol': depthChange[i].quantity,
                                'Sell Level': 1
                            }
                        });
                        for(var k=j;k>=0;k--){
                            depth[k]["Sell Level"]++;
                            $('#table').bootstrapTable('updateCell', {
                                index: k,
                                field: 'Sell Level',
                                value: depth[k]["Sell Level"]
                            });
                        }
                    }
                    else if(depthChange[i].side==1) {
                        var buyLevel = depth[j]["Buy Level"] + 1;
                        $('#table').bootstrapTable('insertRow', {
                            index: j + 1,
                            row: {
                                'Sell Vol': "",
                                'Sell Level': "",
                                'price': depthChange[i].price,
                                'Buy Vol': depthChange[i].quantity,
                                'Buy Level': buyLevel
                            }
                        });
                    }
                    for (var p = 0; p < depth.length; p++) {
                        if (depth[p]["Buy Level"] == 1) {
                            marketPricePage = Math.ceil(p / pageSize);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
}

var webSocket;
var depth;//这是我维护的一个全局变量，任何一次onmMessage出过来，我在改动表的同时，对depth进行改动和维护，保持和表的同步变动
var depth_product=getCookie("depth_product");
var depth_period=getCookie("depth_period");
var depth_broker=getCookie("depth_broker");
var marketPricePage=1;//market price所在的页码，每次绘表都会更新它，便于用户直接跳转到这一页
var pageSize=9;//每页的行数


/*//先用ajax请求调用api，初始化当前的depth表，之后每当有depth变动，服务器都会推送信息到前台，前台调用onMessage函数对depth表进行修改*/
initTable();
window.addEventListener("load", connect, false);//每次页面加载的时候都连接webSocket