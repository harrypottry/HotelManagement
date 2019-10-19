var role_id=sessionStorage.getItem('role_id');//全局获取员工身份Pan-adjust
$(document).ready(function(){
    nav();//加载导航函数
    // 获取时间填入
    if($('.beginTime').length>0){//上月第一天时间
        $('.beginTime').val(getlastDay('')).datepicker({// 年月日事件
            autoclose:true,
            todayHighlight:true,
            language:"zh"
        });
    }
    if($('.endTime').length>0){//上月最后一天时间
        $('.endTime').val(getlastDay('end')).datepicker({// 年月日事件
            autoclose:true,
            todayHighlight:true,
            language:"zh"
        });
    }
    if($('.monthTime').length>0){//上月时间
        $('.monthTime').val(getlastDay('month')).datepicker({//年月事件
            autoclose:true,
            format:'yyyy-mm',
            language:"zh",
            minViewMode:'months'//截止到月份
        });
    }
});
function nav(){//导航条
    if(!sessionStorage.getItem('BackstageNav')){//从未请求过
        $.ajax({
            url :"/console/api/menus",
            data:"",
            type: "GET",
            success:function(data){
                var $lis='';
                for (var i of data){
                    if(!i.permissions){
                        $lis+='<li class="nav-item"><a class="nav-link text-left" href='+i.url+'><img src='+i.icon+' class="align-text-bottom"><p class="p-9">'+i.name+'</p></a></li>';
                    }else {
                        $lis+='<li class="nav-item has-treeview"><a class="nav-link text-left"><img src='+i.icon+' class="align-text-bottom"><p class="p-9">'+i.name+'<img src="../../import/img/minus.png" class="right"><img src="../../import/img/plus.png" class="right plus"></p></a><ul class="nav nav-treeview">';
                        for(var j of i.permissions){
                            $lis+='<li class="nav-item"><a class="nav-link" href='+j.url+'><p>'+j.name+'</p></a></li>';
                        }
                        $lis+='</ul>'
                    }
                }
                // $lis+='<li class="nav-item"><a class="nav-link"><p>退&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出</p></a></li></ul>';
                sessionStorage.setItem('BackstageNav',$lis);
                addlis($lis);
            }
        });
    }else {//已请求过
        addlis(sessionStorage.getItem('BackstageNav'));
    }
    function addlis(lis){//导航插入页面
        $('aside>.sidebar>nav>ul').append(lis);
        $('a[href="'+window.location.pathname+'"]').addClass('active');
        $('a[href="'+window.location.pathname+'"]').parent().parent('ul').parent('li').addClass('menu-open');
        $('a[href="'+window.location.pathname+'"]').parent().parent('ul').parent('li').children('a').addClass('active');
    }
}

$(document).on('click','.jump>a',function(){//点击GO
    if($(this).prev().val()>0){
        $($(this).attr('data-dt-id')).dataTable().fnPageChange($(this).prev().val()-1);
    }
    $(this).prev().val('');
});
function getlastDay(t) {//获取日期
    var dd = new Date();
    var y = dd.getFullYear();
    var m = dd.getMonth();//获取前月份
    if(m==0){m=12;y--;}
    if(m<10){m='0'+m}
    if(t=='month'){return y + "-" + m;}//获取前月份
    if(t=='end'){
        var myDate = new Date(y,m,0);
        return y + "-" + m + "-"+myDate.getDate();
    }
    if(t==''){return y + "-" + m + "-01";}
}
window.warn=function(e){//成功失败-提醒框
    $('.warn>div').html(e);
    $('.warn').fadeToggle(200);
    window.setTimeout(function(){
        $('.warn').fadeToggle(200);
    },1500);
};
function modal(e){//模态框显示隐藏
    $(e).fadeToggle(200);
}
window.downloads=function(url,datas){//导出请求
    var form = document.createElement("form");
    form.style.display = 'none';
    form.action = url;
    form.method ="GET";
    document.body.appendChild(form);
    for(var key in datas){
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = key;
        input.value = datas[key];
        form.appendChild(input);
    }
    form.submit();
    form.remove();
};
function off(){//退出、下班
    $.ajax({
        url :"/logout",
        data:"",
        type: "GET",
        success:function(data){
            sessionStorage.removeItem('BackstageNav');
            location.reload();
        }
    });
}
//酒店前台头部获取酒店信息
var value = localStorage["name"];
var id = localStorage["id"];
var $dds='';
$dds+='<dd>'+value+'</dd>';
$('.leftinfodl').append($dds);
