var btndatas;//查询及换页面参数
$(document).ready(function(){
    request('/wx/console/api/getmissiontypefast','');//任务列表
    request('/wx/console/api/getemployeenamebyloginidfast','');//保洁员列表
    request('/wx/console/api/getStatusfast','');//完成情况
    request('/wx/console/api/getDuringTimeFast','');//时间筛选
    // 获取当前时间填入
    $('.dateend').val(getlastDay(0));//当天时间
    $('.datestart').val(getlastDay(1));//前一天时间
});
$('[name="create_time"]').change(function () {
    if($(this).val()=='0'){//自定义被点击
        $('.datedays').fadeIn();
    }else{
        $('.datedays').fadeOut();
    }
});
// 年月日事件
$('.dateday').datepicker({
    autoclose:true,
    todayHighlight:true,
    language:"zh",
    endDate:getlastDay(0)
});
$('.datestart').on('changeDate',function(e){//开始时间控制结束时间范围
    $('.dateend').datepicker('setStartDate',e.date);
});
function getlastDay(t) {//获取日期
    var dd = new Date();
    dd.setDate(dd.getDate()-t);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;//获取当前月份的日期
    var d = dd.getDate();
    if(m<10){m='0'+m};
    if(d<10){d='0'+d};
    return y + "-" + m + "-" + d;
}
function btn(btn){//查询
    btndatas={};
    btndatas['id']=$('input[name="id"]').val();
    btndatas['room_name']=$('input[name="room_name"]').val();
    btndatas['mission_type_list']=[];
    if($('.mission>label>input:checked').length>0){
        $('.mission>label>input:checked').each(function () {
            btndatas['mission_type_list'].push($(this).val());
        })
    }else {
        alert('请选择需要查询的任务类型');
        return;
    }
    btndatas['accept_employee_id_list']=[];
    if($('.employee>label>input:checked').length>0){
        $('.employee>label>input:checked').each(function () {
            btndatas['accept_employee_id_list'].push($(this).val());
        })
    }else {
        alert('请选择需要查询的保洁员');
        return;
    }
    btndatas['status']=$('select[name="status"]').val();
    btndatas['comment']=$('select[name="comment"]').val();
    if($('select[name="create_time"]').val()!='0'){
        btndatas['endtime']=$('select[name="create_time"]').val();
        btndatas['starttime']='';
    }else{
        btndatas['endtime']=$('.datestart').val();
        btndatas['starttime']=$('.dateend').val();
    }
    request('/wx/console/api/getmissionbyfast6',btndatas,btn);//查询
}
function request(url,datas,btn){
    $.ajax({
        type: "post",
        contentType: "application/json;charset=UTF-8",
        url :url,
        data: JSON.stringify(datas),
        dataType: 'json',
        success:function(data){
            var $label;
            var $option;
            if (url == '/wx/console/api/getmissiontypefast') {//任务列表
                $label = '<b>任务快速筛选</b>';
                for (var i of data) {
                    $label += '<label class="checked"><input type="checkbox" name="mission_type" value='+ i.id + '><span>' + i.desc + '</span></label>';
                    // $label += '<label class="checked"><input type="checkbox" name=' + i.id + '><span>' + i.desc + '</span></label>';
                }
                $('.mission').html($label);
            }
            if (url == '/wx/console/api/getemployeenamebyloginidfast') {//保洁员列表
                $label = '<b>阿姨快速筛选</b>';
                for (var i in data) {
                    $label += '<label class="checked"><input type="checkbox" name="accept_employee_id" value=' + i + '><span>' + data[i] + '</span></label>';
                    // $label += '<label class="checked"><input type="checkbox" name=' + i + '><span>' + data[i] + '</span></label>';
                }
                $('.employee').html($label);
            }
            if (url == '/wx/console/api/getStatusfast') {//完成情况
                $option = '';
                for (var i in data) {
                    $option += '<option value=' + i + '>' + data[i] + '</option>';
                }
                $('[name="status"]').html($option);
            }
            if (url == '/wx/console/api/getDuringTimeFast') {//时间筛选
                $option = '';
                for (var i in data) {
                    $option += '<option value=' + data[i].substring(0,10) + '>' + i + '</option>';
                }
                $option += '<option value="0">自定义</option>';
                $('[name="create_time"]').html($option);
            }
            if(url == '/wx/console/api/getmissionbyfast6'){//查询及换页
                if(btn=='查询'){
                    $('.count').html(data.paging.count);//共多少条
                    //分页
                    if(Math.ceil(data.paging.count/20)==0){
                        $("#page").html('');
                    }else{
                        $("#page").paging({
                            pageNo:1,
                            totalPage:Math.ceil(data.paging.count/20),//共页
                            totalSize: 300,
                            callback:function(num){
                                btndatas['pageNo']=num;
                                request('/wx/console/api/getmissionbyfast6',btndatas);//换页
                            }
                        });
                    }
                    result(data.data);
                }else{//分页
                    result(data.data);
                }
            }
        }
    });
}
function result(data){
    var $result='';
    for(var i of data){
        $result+='<table class="table table-bordered" ><caption><span>任务id：'+i.id+'</span><span>完成时间：'+i.update_time_name.substr(0,16)+'</span></caption>\n' +
            '                        <tbody><tr><td>房间号</td><td>阿姨名称</td><td>任务属性</td><td>完成状态</td><td>备注</td><td>布草信息</td></tr>\n' +
            '                        <tr><td>'+i.room_name+'</td><td>'+i.accept_employee_name+'</td><td>'+i.mission_type_name+'</td><td>'+i.status_name+'</td><td>'+(i.comment||"")+'</td><td><button class="btn btn-primary btn-sm" name='+i.id+'>查询</button></td></tr></tbody></table>'
    }
    $('.result').html($result);
}
$(document).on("click",".result>table>tbody>tr>td>button",function(btnid){//布草信息-查询
    btnid=$(this).attr('name');
    modal('.modal');
    $.ajax({
        type: "post",
        url :'/wx/console/api/getnewoldclothlist',
        data:{missionid:btnid},
        dataType: 'json',
        success:function(data){
            if (JSON.stringify(data.oldcloth) === '{}'&&JSON.stringify(data.newcloth) === '{}') {
               $('.modal-footer').css('display','block');
            }else{
               $('.modal-footer').css('display','none');
            }
            $('.modal-header h4').html('任务id：'+btnid+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;布草信息');
            for(var i in data){
                var $tbody='';
                for(var j in data[i]){
                    $tbody+='<tr><td>'+data[i][j][0]+' （id:'+j+'）</td></tr>'
                }
                $('#'+i).html($tbody);
            }
        }
    })
});
function modal(e){//模态框显示隐藏
    $(e).fadeToggle(200);
}



