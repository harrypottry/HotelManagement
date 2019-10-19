$(function () {
    'use strict';
    $(document).ready(function(){
        nav();//加载导航函数
    });
    function nav(){//导航条
        if(!sessionStorage.getItem('navs')){//从未请求过
            $.ajax({
                url :"/console/api/menus",
                data:"",
                type: "GET",
                success:function(data){
                    console.log(data);
                    var $lis='';
                    for (var i of data){
                        if(!i.permissions){
                            $lis+='<li class="nav-item"><a class="nav-link" href='+i.url+'><i class="nav-icon '+i.icon+'"></i><p>'+i.name+'</p></a></li>';
                        }else {
                            $lis+='<li class="nav-item has-treeview"><a class="nav-link"><i class="nav-icon '+i.icon+'"></i><p>'+i.name+'<i class="right fa fa-angle-left"></i></p></a><ul class="nav nav-treeview">';
                            for(var j of i.permissions){
                                $lis+='<li class="nav-item"><a class="nav-link" href='+j.url+'><i class="fa fa-circle-o nav-icon"></i><p>'+j.name+'</p></a></li>';
                            }
                            $lis+='</ul>'
                        }
                    }
                    $lis+='<li class="nav-item"><a class="nav-link"><i class="nav-icon fa fa-sign-out"></i><p>退&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出</p></a></li></ul>';
                    sessionStorage.setItem('navs',$lis);
                    addlis($lis);
                }
            });
        }else {//已请求过
            addlis(sessionStorage.getItem('navs'));
        }
        function addlis(lis){//导航插入页面
            $('aside>.sidebar>nav>ul').append(lis);
            $('a[href="'+window.location.pathname+'"]').addClass('active');
            $('a[href="'+window.location.pathname+'"]').parent().parent('ul').parent('li').addClass('menu-open');
            $('a[href="'+window.location.pathname+'"]').parent().parent('ul').parent('li').children('a').addClass('active');
            $('aside>.sidebar>nav>ul>li:last-child').click(function (){
                $.ajax({
                    url :"/logout",
                    data:"",
                    type: "GET",
                    success:function(data){
                        sessionStorage.removeItem('navs');
                        location.reload();
                    }
                });
            });
        }
    }
});