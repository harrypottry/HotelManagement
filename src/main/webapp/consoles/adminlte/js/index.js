$(document).ready(function(){
    $('.text-height').outerHeight(window.innerHeight-200);//固定酒店列表酒店详情高度
    $.ajax({//获得酒店列表
        url :"/console/api/getHotelList",
        data:'',
        type: "GET",
        success:function(data){
            var $lis='';
            for(var i of data){
                $lis+='<li class="nav-item"><a name='+i.id+' class="nav-link">'+i.name+'</a></li>';
            }
            $('.hotels').prepend($lis);
        }
    });
});
var btn_hotelid='';
$(document).on("click",".card-body>.hotels>li>a", function (hotelid){//点击酒店
    $('.card-body>ul>li>a.active').removeClass('active');
    $(this).addClass('active');//被选择酒店状态切换
    btn_hotelid=$(this).attr('name');
    $.ajax({//获得酒店详情
        url :"/console/api/getHotelView",
        data:{id:btn_hotelid},
        type: "GET",
        success:function(data){
            var $lis='',owner='',manage='',bd='';//owner业主manage店长bd推广员
            //暂隐藏的用来添加推广员的li
            $lis+='<li class="member_model"><div class="timeline-item card collapsed-card"><h3 class="timeline-header" data-widget="collapse">推广员：<span></span></h3><div class="timeline-body card-body"><div class="row query"><div class="col-md-6"><div class="form-group"><span>手机：</span><input class="form-control"><button class="btn btn-primary btn-sm querybtn">查询</button></div></div></div><div class="row"><div class="col-md-6"><div class="form-group"><span>账号：</span><input class="form-control" name="phone"><input class="form-control" name="id" style="display:none"></div><div class="form-group"><span>姓名：</span><input class="form-control" name="name"></div><div class="form-group"><span>密码：</span><input class="form-control" name="password"></div></div><div class="col-md-6"><div class="form-group"><span>银行账号：</span><input class="form-control" name="bank_account"></div><div class="form-group"><span>开户姓名：</span><input class="form-control" name="account_name"></div><div class="form-group"><span>开户行：</span><input class="form-control" name="bank_name"></div></div></div><div class="text-right"><button class="btn btn-primary btn-sm reset">重置</button><button class="btn btn-danger btn-sm delete" style="display:none">删除</button><button class="btn btn-primary btn-sm save" name="3">保存</button></div></div></div></li>';
            //酒店分润信息
            $lis+='<li><div class="timeline-item card collapsed-card"><h3 class="timeline-header" data-widget="collapse">酒店信息</h3><div class="timeline-body card-body"><div class="row"><div class="col-md-6"><div class="form-group" style="position:relative"><span>收益比例：</span><input class="form-control" name="profit_rate" value='+(data.hotelExtra.profit_rate||"")+'><span>%</span></div><div class="form-group" style="position:relative"><span>客房数量：</span><input class="form-control" name="room_num" value='+(data.hotelExtra.room_num||"")+'></div></div><div class="col-md-6"><div class="form-group" style="position:relative"><span>业主分润：</span><input class="form-control" name="profit_rate_owner" value='+(data.hotelExtra.profit_rate_owner||"")+'><span>%</span></div><div class="form-group" style="position:relative"><span>店长分润：</span><input class="form-control" name="profit_rate_manager" value='+(data.hotelExtra.profit_rate_manager||"")+'><span>%</span></div><div class="form-group" style="position:relative"><span>推广员分润：</span><input class="form-control" name="profit_rate_bd" value='+(data.hotelExtra.profit_rate_bd||"")+'><span>%</span></div></div></div><div class="text-right"><button class="btn btn-primary btn-sm hotel_details">保存</button></div></div></div></li>';
            for(var i of data.employees){
                if(i.type==1){owner=exis('业主',i,'1');}//业主
                if(i.type==2){manage=exis('店长',i,'2');}//店长
                if(i.type==3){bd+=exis('推广员',i,'3');}//推广员
            }
            if(!owner){owner=empty('业主','1');}//无业主
            if(!manage){manage=empty('店长','2');}//无店长
            // 已有店长业主推广员
            function exis(job,i,type){
                return '<li><div class="timeline-item card collapsed-card"><h3 class="timeline-header" data-widget="collapse">'+job+'：<span>'+i.name+'</span></h3><div class="timeline-body card-body"><div class="row query" style="display:none"><div class="col-md-6"><div class="form-group"><span>手机：</span><input class="form-control"><button class="btn btn-primary btn-sm querybtn">查询</button></div></div></div><div class="row"><div class="col-md-6"><div class="form-group"><span>账号：</span><input class="form-control" disabled name="phone" value='+i.phone+'><input class="form-control" name="id" value='+i.id+' style="display:none"></div><div class="form-group"><span>姓名：</span><input class="form-control" name="name" value='+i.name+'></div><div class="form-group"><span>密码：</span><input class="form-control" name="password" value='+(i.password||'')+'></div></div><div class="col-md-6"><div class="form-group"><span>银行账号：</span><input class="form-control" name="bank_account" value='+i.bank_account+'></div><div class="form-group"><span>开户姓名：</span><input class="form-control" name="account_name" value='+i.account_name+'></div><div class="form-group"><span>开户行：</span><input class="form-control" name="bank_name" value='+i.bank_name+'></div></div></div><div class="text-right"><button class="btn btn-primary btn-sm reset" style="display:none">重置</button><button class="btn btn-danger btn-sm delete">删除</button><button class="btn btn-primary btn-sm save" name='+type+'>保存</button></div></div></div></li>';
            }
            // 未有业主店长
            function empty(job,type){
                return '<li><div class="timeline-item card collapsed-card"><h3 class="timeline-header" data-widget="collapse">'+job+'：<span></span></h3><div class="timeline-body card-body"><div class="row query"><div class="col-md-6"><div class="form-group"><span>手机：</span><input class="form-control"><button class="btn btn-primary btn-sm querybtn">查询</button></div></div></div><div class="row"><div class="col-md-6"><div class="form-group"><span>账号：</span><input class="form-control" name="phone"><input class="form-control" name="id" style="display:none"></div><div class="form-group"><span>姓名：</span><input class="form-control" name="name"></div><div class="form-group"><span>密码：</span><input class="form-control" name="password"></div></div><div class="col-md-6"><div class="form-group"><span>银行账号：</span><input class="form-control" name="bank_account"></div><div class="form-group"><span>开户姓名：</span><input class="form-control" name="account_name"></div><div class="form-group"><span>开户行：</span><input class="form-control" name="bank_name"></div></div></div><div class="text-right"><button class="btn btn-primary btn-sm reset">重置</button><button class="btn btn-danger btn-sm delete" style="display:none">删除</button><button class="btn btn-primary btn-sm save" name='+type+'>保存</button></div></div></div></li>';
            }
            $lis+=owner+manage+bd;
            $('.member').html($lis);
        }
    });
});
$(document).on("click",".hotel_details",function($input){//酒店信息添加修改保存
    $input=$(this).parents('li').find('input');
    var data={};
    data.id=btn_hotelid;
    $input.each(function () {
        data[$(this).attr('name')]=$(this).val();
    });
    $.ajax({
        url :"/console/api/postHotelBasicInfo",
        contentType: 'application/json',
        data:JSON.stringify(data),
        type: "POST",
        success:function(data){
            if(data.errorCode==0){
                alert('保存成功');
            }else{
                alert('保存失败');
            }
        }
    });
});
$(document).on("click",".reset",function($input){//用户信息重置
    $input=$(this).parents('li').find('input');
    $input.each(function(){
        $(this).val('');
        if($(this).attr('name')=='phone'){$(this).removeAttr('disabled')}
    });
});
$(document).on("click",".querybtn",function($input,$inputs){//查询member信息
    $input=$(this).prev('input');
    $inputs=$(this).parents('.row').next('.row').find('input');
    if(/^[1][3,4,5,7,8][0-9]{9}$/.test($input.val())){//正确手机号
        $.ajax({
            url :"/console/api/getEmployeeByPhone",
            data:{phone:$input.val()},
            type:"GET",
            success:function(data){
                if(data){//查询到员工数据
                    $input.parents('li').find('.timeline-header>span').html(data.name);
                    $inputs.each(function(i){
                        i=$(this).attr('name');
                        $(this).val(data[i]);
                        if(i=='phone'){$(this).attr('disabled','disabled')}
                    });
                }else{//未查询到员工数据
                    $input.parents('li').find('.timeline-header>span').html('');
                    $inputs.each(function(){
                        $(this).val('');
                        if($(this).attr('name')=='phone'){$(this).removeAttr('disabled')}
                    });
                }
            }
        });
    }else {
        alert('请填写正确的手机号');
    }
});
$(document).on("click",".save",function($inputs,required,id,$query,$reset,$delete){//保存member信息
    $inputs=$(this).parent().prev('.row').find('input');
    $query=$(this).parent().prevAll('.query');//查询行row
    $reset=$(this).prevAll('.reset');//重置按钮
    $delete=$(this).prevAll('.delete');//删除按钮
    required=0;
    var data={};
    data.type=$(this).attr('name');
    data.hotel_id=btn_hotelid;
    id=$(this).parent().prev('.row').find('input[name="id"]').val();
    $inputs.each(function(i){
        i=$(this).attr('name');
        data[i]=$(this).val();//为请求存储data数据
        if(i!='id'){//id为空无所谓
            if(id){//存在id密码为空无所谓
                if(i!='password'){
                    if($(this).val()==''){
                        $(this).addClass('required');
                        required++;
                    }
                }
            }else{//无id密码不可为空
                if($(this).val()==''){
                    $(this).addClass('required');
                    required++;
                }
            }
        }
    });
    if(required>0){
        alert('"红框"为必填信息');
    }else{
        $.ajax({
            url :"/console/api/postEmployeeInfo",
            contentType: 'application/json',
            data:JSON.stringify(data),
            type: "POST",
            success:function(data){
                console.log(data);
                if(data.errorCode==0){
                    alert('保存成功');
                    $query.fadeOut();
                    $reset.fadeOut();
                    $delete.fadeIn();
                }else{
                    alert('保存失败');
                }
            }
        });
    }
});
$(document).on("focus","input.required",function(){//必填提示input获得焦点
    $(this).removeClass('required');
});
$(document).on("click",".delete",function(employee,$liinputs,$query,$reset,$delete){//删除member信息
    employee=$(this).parents('li').find('input[name="id"]').val();
    $liinputs=$(this).parents('li').find('input');//li中所有input
    $query=$(this).parent().prevAll('.query');//查询行row
    $reset=$(this).prevAll('.reset');//重置按钮
    $delete=$(this);//删除按钮
    $.ajax({
        url :"/console/api/unboundHotelEmployee",
        data:{employee_id:employee,hotel_id:btn_hotelid},
        type: "GET",
        success:function(data){
            console.log(data);
            if(data.errorCode==0){
                alert('删除成功');
                $delete.parents('li').find('.timeline-header>span').html('');
                $liinputs.each(function(){
                    $(this).val('');
                    if($(this).attr('name')=='phone'){$(this).removeAttr('disabled')}
                });
                $delete.fadeOut();
                $query.fadeIn();
                $reset.fadeIn();
            }else{
                alert('删除失败');
            }
        }
    });
});
// 模态框事件
function modal(e){//模态框显示隐藏
    $(e).fadeToggle(200);
}
$('.hotelidbtn>.btn-primary').click(function(hotelid){//添加酒店
    hotelid=$('input.hotelid').val();
    if(hotelid==''){
        $('.hotelidtext').html('请输入酒店Id');
        setTimeout(function(){
            $('.hotelidtext').html('');
        },1500);
    }else {
        $(this).html('申请中...');
        $('.hotelidbtn>button').attr('disabled','disabled');
        $.ajax({//酒店查询
            url :"/console/api/postHotelId",
            data:{hotel_id:hotelid},
            type: "POST",
            success:function(data){
                if(data.errorCode){
                    $('.hotelidtext').html(data.errorDesc);
                    $('.hotelidbtn>.btn-primary').html('确定');
                    $('.hotelidbtn>button').removeAttr('disabled');
                    $('input.hotelid').val('');
                    setTimeout(function(){
                        $('.hotelidtext').html('');
                    },2000);
                }else {
                    $('.hotelidtext').html('添加成功');
                    $('.hotelidbtn>.btn-primary').html('确定');
                    $('.hotels').prepend('<li class="nav-item"><a name='+data.hotel_id+' class="nav-link">'+data.hotel_name+'</a></li>');
                    $('.hotels>li:nth-child(1)>a').click();//添加新酒店默认被点击
                    setTimeout(function(){
                        $('.addhotel').fadeToggle();
                    },1000);
                    setTimeout(function(){
                        $('.hotelidtext').html('');
                        $('.hotelidbtn>button').removeAttr('disabled');
                        $('input.hotelid').val('');
                    },2000);
                }
            }
        });

    }
});
$('button.addmember').click(function () {// 添加推广员
    if($('.member_model').length){
        $('.member_model').clone(true).removeClass('member_model').appendTo('.member');
    }
});