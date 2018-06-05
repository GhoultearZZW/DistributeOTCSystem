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

/*当下拉框选项变成AllTransactions时，将输入框锁定*/
function selectOnChange(obj){
    if(obj.options[obj.selectedIndex].text=="AllTransactions"){
        $("#company").val("");
        document.getElementById("company").readOnly=true;
    }
    if(obj.options[obj.selectedIndex].text=="RestrictParticipator"
    ||obj.options[obj.selectedIndex].text=="RestrictInitiator"
    ||obj.options[obj.selectedIndex].text=="RestrictCompletion"){
        document.getElementById("company").readOnly=false;
    }
}

$("#scope").val(getCookie("scope"));
if(getCookie("scope")=="AllTransactions"){//此时cookie中没有company或者有上次遗留的company，不予显示
    $("#company").val("");//输入框内容清空
    document.getElementById("company").readOnly=true;//锁定输入框
}
else {
    $("#company").val(decodeURI(getCookie("company")));
}


$("#view").click(function(e) {
    var scope=$("#scope").find("option:selected").text();
    if(scope=="Scope"){
        alert("Please choose scope!");
        return false;
    }
    if(scope=="AllTransactions"){
        addCookie("scope",scope,0);
        //addCookie("company","none",0);
        location.reload();
    }
    else{
        var company=$("input[id='company']").val();
        if(company==""){
            alert("Please input company!");
            return false;
        }
        addCookie("scope",scope,0);
        addCookie("company",company,0);
    }
    location.reload();//刷新页面
})


var drawTable=function(e) {
    var blotter;
    var scope = getCookie("scope");
    if(scope=="AllTransactions"){
        $.ajax({
            type: "post",
            url: "/blotter/all",
            async: false,
            data: JSON.stringify({
                "broker":"M"
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                blotter = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get blotter!");
            }
        })
    }
    else if(scope=="RestrictParticipator"){
        var company=decodeURI(getCookie("company"));
        $.ajax({
            type: "post",
            url: "/blotter",
            async: false,
            data: JSON.stringify({
                "company":company,
                "broker":"M"
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                blotter = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get blotter!");
            }
        })
    }
    else if(scope=="RestrictInitiator"){
        var company=decodeURI(getCookie("company"));
        $.ajax({
            type: "post",
            url: "/blotter/initiator",
            async: false,
            data: JSON.stringify({
                "company":company,
                "broker":"M"
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                blotter = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get blotter!");
            }
        })
    }
    else if(scope=="RestrictCompletion"){
        var company=decodeURI(getCookie("company"));
        $.ajax({
            type: "post",
            url: "/blotter/completion",
            async: false,
            data: JSON.stringify({
                "company":company,
                "broker":"M"
            }),
            contentType: "application/json",

            /*后端的响应状态码为200时，表示响应成功，触发success*/
            success: function (response) {
                blotter = response;
            },
            /*其他响应状态码，触发error，ajax还会在下列情况走error：1.返回数据类型不是json。2.网络中断。3.后台响应中断。*/
            error: function () {
                alert("Failed to get blotter!");
            }
        })
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