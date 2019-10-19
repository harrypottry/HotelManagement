$(function(){
//查看布草
    $.ajax({
        type: "post",
        url: "/wx/console/api/getClothByEmpType",
        dataType:"json",
        // crossDomain: true,
        success: function(data){
            // console.log(data);
            var $html = '';
            data.list={};
            var x;
            for(var i in data.name) {
                x={};
                x['num'] = data.num[i];
                // x['status'] = data.status[i];
                if(data.status[i]==0){
                    x['status']='干净'
                }else{
                    x['status']='脏'
                }
                for (var j of data.name[i]) {
                    x[j.cloth_kind] = j.desc;
                }
                data.list[i]=x;
                $html+='<tr><td>'+data.list[i].Type+'</td><td>'+data.list[i].Material+'</td><td>'+data.list[i].Size+'</td><td>'+x['status']+'</td><td>'+data.list[i].num+'</td></tr>'
            }
            $('.tbody').html($html);
        }
    });
//历史任务
    $.ajax({
        type: "post",
        url: "/wx/console/api/showHistoryMissionByEmp",
        dataType: "json",
        success: function (data) {
            // console.log(data);
            var $lis='';
            datas=data.data;
            for(var m of datas){
                $lis+='<p><a>'+m.desc+'</a><a>'+m.update_time+'</a><a>'+m.room_name.room_name+'</a></p>'
            }
            $('.historyp').html($lis);
        }
    });
});