$(function(){
    $(document).ready(function(){
        request('/wx/console/api/getmissiontype','');//任务列表
        request('/wx/console/api/getemployeenamebyloginidinworking','');//保洁员列表
        request('/wx/console/api/getroomlistbyloginid','');//房间列表
    });
    function request(url,datas,loginid){//loginid只为判断房间列表是否由发布任何后从新获取
        $.ajax({
            url :url,
            data:datas,
            type: "GET",
            success:function(data){
                var $lis;
                if(url=='/wx/console/api/getmissiontype'){//任务列表
                    $lis='';
                    for(var i of data){
                        $lis+='<li><label><input name="linen" type="radio" value='+i.id+'><a>'+i.desc+'</a><label></li>';
                    }
                    $('.updatelinen ul').html($lis);
                }
                if(url=='/wx/console/api/getemployeenamebyloginidinworking'){//保洁员列表
                    $lis='';
                    for(var i of data){
                        $lis+='<li><label><input name="clerk" type="radio" value='+i.id+'><a>'+i.name+'</a></label></li>';
                    }
                    $('.staffuls').html($lis);
                }
                if(url=='/wx/console/api/getroomlistbyloginid'){//房间列表
                    var $navlis='';
                    $lis='';
                    for(var i in data.data){
                        var item=data.data[i];
                        if(i==1){
                            $navlis+='<li class="nav-item"><a class="nav-link active" href="#a'+i+'" data-toggle="tab">'+i+'层</a></li>';
                            $lis += '<div class="active tab-pane" id="a'+i+'">';
                            $lis += '<ul class="roomnums">';
                        }else{
                            $navlis+='<li class="nav-item"><a class="nav-link" href="#a'+i+'" data-toggle="tab">'+i+'层</a></li>';
                            $lis += '<div class="tab-pane" id="a'+i+'">';
                            $lis += '<ul class="roomnums">';
                        }
                        for(var j of item){
                            if(j.status==1){
                                $lis+='<li><label><input type="checkbox" name='+j.id+' class="checkbox"/><a class="dirty"><span>'+j.room_name+'</span></a></label></li>';
                            }else if(j.status==2){
                                $lis+='<li><label><input type="checkbox" name='+j.id+' class="checkbox"/><a class="wait"><span>'+j.room_name+'</span></a></label></li>';
                            }else{
                                $lis+='<li><label><input type="checkbox" name='+j.id+' class="checkbox"/><a><span>'+j.room_name+'</span></a></label></li>';
                            }
                        }
                        $lis += '</ul>';
                        $lis += '</div>';
                    }
                    $('.roomtopcont .nav').html($navlis);
                    $('.roomwrapper .tab-content').html($lis);
                    // if(loginid!='renewlinen'){//只有不是发布任务后获取才更新楼层及房间列表
                    //     var $navlis='';
                    //     $lis='';
                    //     for(var i in data[0]){
                    //         var item=data[0][i];
                    //         if(i==1){
                    //             $navlis+='<li class="nav-item"><a class="nav-link active" href="#a'+i+'" data-toggle="tab">'+i+'层</a></li>';
                    //             $lis += '<div class="active tab-pane" id="a'+i+'">';
                    //             $lis += '<ul class="roomnums">';
                    //         }else{
                    //             $navlis+='<li class="nav-item"><a class="nav-link" href="#a'+i+'" data-toggle="tab">'+i+'层</a></li>';
                    //             $lis += '<div class="tab-pane" id="a'+i+'">';
                    //             $lis += '<ul class="roomnums">';
                    //         }
                    //         for(var j of item){
                    //             $lis+='<li><label><input type="checkbox" name='+j.id+' class="checkbox"/><a><span>'+j.room_name+'</span></a></label></li>';
                    //         }
                    //         $lis += '</ul>';
                    //         $lis += '</div>';
                    //     }
                    //     $('.roomtopcont .nav').html($navlis);
                    //     $('.roomwrapper .tab-content').html($lis);
                    // }
                    // for(var i of data[1]){
                    //     var $input=$('input[name='+i.room_id+']');
                    //     if($input.length>0){
                    //         if(i.status_name=='发布任务'){
                    //             $input.next('a').css('background','#FFCC00');
                    //         }
                    //         if(i.status_name=='工作中'){
                    //             $input.next('a').css('background','#B4ECB4');
                    //         }
                    //         $input.next('a').html('<span>'+i.room_name+'<br>'+i.accept_employee_name+'<br>'+i.mission_type_name+'</span>');
                    //         $input.remove();
                    //     }
                    // }
                }
            }
        });
    }
    // 发布任务
    $('.renewlinen').click(function(employeeid,missiontypeid,roomlist){
        missiontypeid = $("input[name='linen']:checked").val();//选中工作类型
        employeeid = $('input[name="clerk"]:checked').val();//选中阿姨
        roomlist=[];
        $('.roomnums input:checked').each(function () {
            roomlist.push($(this).attr('name'));
        });
        if(missiontypeid == undefined){
            alert('请选择任务');
            return;
        }
        // if(employeeid == undefined){
        //     alert('请选择工作人员');
        //     return;
        // }
        if(roomlist.length == 0){
            alert('请选择房间');
            return;
        }
        $.ajax({
            url :"/console/api/assigningmission",
            data:{"employeeid":employeeid,"missiontypeid":missiontypeid,"roomlist":roomlist},
            type:"post",
            success:function(data){
                if(data.errorCode=='0'){
                    alert('任务发布成功！');
                }else if(data.errorCode=='30102'){
                    alert('任务重复发布！');
                }else {
                    alert('请重新操作！');
                }
            }
        })
    });
    // 改变房态
    $('.status').click(function (roomlist,status,classs,a_class) {
        status=$(this).attr('name');
        roomlist=[];
        $('.roomnums input:checked').each(function () {
            roomlist.push($(this).attr('name'));
        });
        if(roomlist.length == 0){
            alert('请选择房间');
            return;
        }
        $.ajax({
            url :"/wx/console/api/postRoomStatus",
            data:{room_ids:roomlist,status:status},
            type:"post",
            success:function(data){
                if(data.errorCode==0){
                    classs=['','dirty','wait'];
                    a_class=classs[status];
                    for(var i of roomlist){
                        $('input[name='+i+']').next('a').attr('class',a_class);
                        $('input[name='+i+']').prop("checked",false);
                    }
                }
            }
        })
    })
})


