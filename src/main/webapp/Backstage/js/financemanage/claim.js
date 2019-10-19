//claim款项认领、statistics收款统计
var claimdata,statisticsdata;
var claimTd,statisticsTd;
var claimAccountId;//认领账单ID
$(document).ready(function(){
    $quickQuery({"claimhotelId":hotels,"statisticshotelId":hotels,"hotelId":hotels.slice(1)},{'isPage':false});
    $('.modal .modal-card:last-child').height(window.innerHeight/2-130);
});
function btn(Ids,datas,url,Tds) {//查询页面内容信息
    if(Ids=='claim'){//款项认领
        url='/console/api/getClaimAccountData';
        Tds=claimTd;
        if(datas==undefined){
            datas={};
            datas.hotelId=$('#'+Ids+' .card ul li .hotelId').attr('name');
            datas.status=$('#'+Ids+' .card ul li .status').val();
            datas.beginTime=$('#'+Ids+' .card ul li .beginTime').val();
            datas.endTime=$('#'+Ids+' .card ul li .endTime').val();
        }
        claimdata=datas;
    }
    if(Ids=='statistics'){//收款统计
        datas={};
        datas.hotelId=$('#'+Ids+' .card ul li .hotelId').attr('name');
        datas.status=$('#'+Ids+' .card ul li .status').val();
        datas.monthTime=$('#'+Ids+' .card ul li .monthTime').val();
        statisticsdata=datas;
        url='/console/api/exportCollectedClaimList';
        Tds=statisticsTd;
    }
    request(Ids,datas,url,Tds);
}
function download(Ids,datas,url) {//导出
    if(Ids=='EXCEL'){//下载EXCEL表格(模板)
        url='/console/api/exportClaimTemplate ';
        datas={};
    }
    if(Ids=='claim'){//款项认领
        url='/console/api/exportClaimAccountData';
        datas=claimdata;
    }
    if(Ids=='statistics'){//收款统计
        url='/consoles/api/exportCollectedClaimListExtel';
        datas=statisticsdata;
    }
    delete datas.pageNo;
    delete datas.pageSize;
    downloads(url,datas);//导出请求
}
$('.import-btn').change(function (){
    $(this).prev('span').html('认领任务上传中...');
    var formData = new FormData();
    formData.append('file',$(this)[0].files[0]);// 此处可传入多个参数
    $(this).val('');//清空上传过的数据
    $.ajax({
        url:'/console/api/claimExcelImport',
        type: 'post',
        data: formData,
        processData: false,// 告诉jQuery不要去处理发送的数据
        contentType: false,// 告诉jQuery不要去设置Content-Type请求头
        success: function (res) {
            if(res.status=='success'){
                request('claim',{status:'0',hotelId:'0'},'/console/api/getClaimAccountData',claimTd);//款项未认领所有时间
            }
            $('.import-btn').prev('span').html('上传待认领任务');
            warn(res.msg);
        }
    })
});
$(document).on("click",".claim",function (hotelId) {//点击认领
    hotelId=$(this).attr('name').split("/")[0];//酒店Id
    claimAccountId=$(this).attr('name').split("/")[1];
    $('.modal').fadeToggle(200);
    if(hotelId!='null'){//存在酒店Id
        $('#hotelId').attr('name',hotelId).val(hotelbug[hotelId]).attr('disabled','disabled');
        $(".getAccountSum").click();
    }else{
        $('#hotelId').attr('name','').val('').removeAttr('disabled','');
        $('#a1 tbody').html('');
        $('#a2 tbody').html('');
        $('#a3 tbody').html('');
        $('.modal .modal-body>div:first-child>b').html('账单金额：');
    }
    $('.claimPriceSum').html('0');
    $('.adjustPriceSum').html('0');
});
$(".getAccountSum").click(function (hotelId,$tr1,$tr2,$tr3,$tr,Type,total1,total2,total3) {//查询账单
    $('.claimPriceSum').html('0');
    $('.adjustPriceSum').html('0');
    hotelId=$('#hotelId').attr('name');
    Type=['','布草租赁费','布草洗涤费','报损费用','人力众包费','输送客源费','合作服务费','会员服务费','PMS系统维护费','OTA代运营费'];
    if(hotelId>0){
        $.ajax({
            url :'/console/api/getAccountSum',
            data :{hotelId:hotelId},
            type: "GET",
            success:function(data){
                $('#hotelId').val(hotelbug[hotelId]);
                $('.modal .modal-body>div:first-child>b').html('账单金额：'+data.claimSum.toFixed(2));
                $tr1='',$tr2='',$tr3='';
                total1=0,total2=0,total3=0;
                if(data.data.length>0){
                    for(var i of data.data){
                        if(i.status==0){
                            $tr='<tr><td>'+Type[i.type]+'</td><td>'+i.updatePrice+'</td><td>'+i.beginTime.substr(0,7)+'</td><td><input class="form-control claimP" name="'+i.id+'" max="'+i.updatePrice+'" type="text"></td><td><input class="form-control adjustP" max="'+i.updatePrice+'" type="text"></td></tr>';
                            if(i.type=='1'||i.type=='2'||i.type=='3'){//$tr1
                                $tr1+=$tr;
                                total1+=i.updatePrice;
                            }
                            if(i.type=='4'){//$tr2
                                $tr2+=$tr;
                                total2+=i.updatePrice;
                            }
                            if(i.type!='1'&&i.type!='2'&&i.type!='3'&&i.type!='4'){//$tr3
                                $tr3+=$tr;
                                total3+=i.updatePrice;
                            }
                        }
                    }
                    $tr1+='<tr><td>本页合计</td><td>'+total1.toFixed(2)+'</td><td></td><td class="claimtotal">0</td><td class="adjusttotal">0</td></tr>';
                    $tr2+='<tr><td>本页合计</td><td>'+total2.toFixed(2)+'</td><td></td><td class="claimtotal">0</td><td class="adjusttotal">0</td></tr>';
                    $tr3+='<tr><td>本页合计</td><td>'+total3.toFixed(2)+'</td><td></td><td class="claimtotal">0</td><td class="adjusttotal">0</td></tr>';
                }
                $('#a1 tbody').html($tr1);
                $('#a2 tbody').html($tr2);
                $('#a3 tbody').html($tr3);
            }
        });
    }else{
        alert('请选择正确的酒店');
    }
});
$(document).on("change",".modal input.claimP",function(Id,total,claimPriceSum){//认领金额输入
    Id=$(this).parents('.tab-pane').attr('id');
    total=0;claimPriceSum=0;
    if($(this).val()>=0){
        if($(this).val()<=Number($(this).attr('max'))){
            if(Number($(this).val())+Number(-$(this).parents('tr').find('.adjustP').val())<=Number($(this).attr('max'))){
                $('#'+Id+' table input.claimP').each(function () {
                    total+=Number($(this).val());
                });
                $('#'+Id+' table tbody tr:last-child td.claimtotal').html(total);
                $('.claimtotal').each(function () {
                    claimPriceSum+=Number($(this).html());
                });
                $('.claimPriceSum').html(claimPriceSum);
            }else{
                alert('认领金额与调整金额的绝对值之和需小于等于账单金额');$(this).val('');
            }
        }else{
            alert('认领金额不可大于账单金额');$(this).val('');
        }
    }else{
        alert('认领金额为正，且小于等于账单金额');$(this).val('');
    }
});
$(document).on("change",".modal input.adjustP",function(Id,total,adjustPriceSum){//认领金额输入
    Id=$(this).parents('.tab-pane').attr('id');
    total=0;adjustPriceSum=0;
    if($(this).val()<=0){
        if(-$(this).val()<=Number($(this).attr('max'))){
            if(Number(-$(this).val())+Number($(this).parents('tr').find('.claimP').val())<=Number($(this).attr('max'))){
                $('#'+Id+' table input.adjustP').each(function (){
                    total+=Number($(this).val());
                });
                $('#'+Id+' table tbody tr:last-child td.adjusttotal').html(total);
                $('.adjusttotal').each(function () {
                    adjustPriceSum+=Number($(this).html());
                });
                $('.adjustPriceSum').html(adjustPriceSum);
            }else{
                alert('认领金额与调整金额的绝对值之和需小于等于账单金额');$(this).val('');
            }
        }else{
            alert('调整金额绝对值不可大于账单金额');$(this).val('');
        }
    }else{
        alert('调整金额为负，且其绝对值小于等于账单金额');$(this).val('');
    }
});
$('.saveClaimAccount').click(function (datas) {//认领款项
    datas={};
    datas.claimPriceSum=$('.claimPriceSum').html();
    datas.adjustPriceSum=$('.adjustPriceSum').html();
    if(datas.claimPriceSum>0||datas.adjustPriceSum<0){
        datas.claimAccountId=claimAccountId;
        datas.hotelId=$('#hotelId').attr('name');
        datas.list=[];
        $('.modal-card>.tab-content>.tab-pane>table>tbody input.claimP').each(function(){
            datas.list.push({id:$(this).attr('name'),claimPrice:$(this).val()||'0',adjustPrice:$(this).parents('tr').find('.adjustP').val()||'0'});
        });
        $.ajax({
            url: '/console/api/saveClaimAccount',
            contentType: 'application/json',
            data:JSON.stringify(datas),
            type: "POST",
            success: function (data) {
                if(data.status=='success'){
                    $('.modal').fadeToggle(200);
                    btn('claim');
                }
                warn(data.msg);
            }
        });
    }else {
        alert('请输入认领金额或调整金额');
    }
});
function request(Ids,datas,url,Tds,pageCount){//页面请求表格数据   pageCount:总页码数
    $('#'+Ids+'table').dataTable({
        scrollY:window.innerHeight-420,
        scrollX:true,
        destroy:true,
        ordering:false,//排序
        lengthChange:false,
        paging:true,
        filter:false,
        serverSide:true,  //启用服务器端分页
        aLengthMenu:[20],
        searching:false,  //禁用原生搜索
        ajax: function (data, callback, settings){
            datas.pageNo=(data.start / data.length)+1;//当前页码
            datas.pageSize='20';//每页条数
            $.ajax({
                type:"GET",
                url:url,
                cache: false,//禁用缓存
                data:datas,//传入组装的参数
                dataType:"json",
                success: function (result) {
                    if(Ids=='claim'&&!datas.beginTime&&JSON.stringify(result.data.dataList) === '[]'){
                        $('.empty').show();
                        $('.empty').nextAll().hide();
                        $('#'+Ids+' .beginTime').val('');
                        $('#'+Ids+' .endTime').val('');
                        return;
                    }
                    if(Ids=='claim'&&!datas.beginTime&&JSON.stringify(result.data.dataList)!= '[]'){//上传成功后
                        $('.empty').hide();
                        $('.empty').nextAll().show();
                        // $('.import-btn').prev('span').html('上传待认领任务');
                        $('#'+Ids+' .beginTime').val('');
                        $('#'+Ids+' .endTime').val('');
                    }
                    setTimeout(function () {//setTimeout仅为测试延迟效果
                        pageCount=result.data.pageCount;//总页码
                        var returnData = {};//封装返回数据
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.data.count;//返回数据全部记录
                        returnData.recordsFiltered = result.data.count;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data.dataList;//返回的数据列表
                        callback(returnData);
                        $('#'+Ids+' .card ul li .hotelId').val(hotelbug[datas.hotelId]);
                        if(Ids=='claim'&&!datas.beginTime){
                            $('#'+Ids+' .table-header>span:first-child').html($('#'+Ids+' .card ul li .hotelId').val()+'-全部时间：'+getstatus[datas.status]);
                        }
                        if(Ids=='claim'&&datas.beginTime){
                            $('#'+Ids+' .table-header>span:first-child').html($('#'+Ids+' .card ul li .hotelId').val()+'：'+datas.beginTime.replace(/-/g,'.')+'-'+datas.endTime.replace(/-/g,'.')+' '+getstatus[datas.status]);
                        }
                        if(Ids=='statistics'){
                            $('#'+Ids+' .table-header>span:first-child').html($('#'+Ids+' .card ul li .hotelId').val()+'：'+datas.monthTime+'月 收款统计');
                            if(pageCount>0){//本页合计
                                var $tr='';
                                var Type=['Pms','Ota','Convey','Cooperation','Night','Rental','Wash','Damage','Personnel'];
                                var State=['Ought','Actual','Adjust','Wait'];
                                var Key,pageOught=0,pageActual=0,pageAdjust=0,pageWait=0,totelOught=0,totelActual=0,totelAdjust=0,totelWait=0;
                                if(pageCount>1){//页面大于1时
                                    $tr+='<tr role="row"><th colspan="2">本页合计</th><th></th>';
                                    for(var i of Type){
                                        for(var j of State){
                                            Key="page"+i+j+"Price";
                                            $tr+='<th>'+result["pagetotel"][Key]+'</th>';
                                            if(j=='Ought'){pageOught+=Number(result["pagetotel"][Key])};
                                            if(j=='Actual'){pageActual+=Number(result["pagetotel"][Key])};
                                            if(j=='Adjust'){pageAdjust+=Number(result["pagetotel"][Key])};
                                            if(j=='Wait'){pageWait+=Number(result["pagetotel"][Key])};
                                        }
                                    }
                                    $tr+='<th>'+pageOught+'</th><th>'+pageActual+'</th><th>'+pageAdjust+'</th><th>'+pageWait+'</th></tr>';
                                }
                                //总计
                                $tr+='<tr role="row"><th colspan="2">总计</th><th></th>';
                                for(var i of Type){
                                    for(var j of State){
                                        Key="totel"+i+j+"Price";
                                        $tr+='<th>'+result["totel"][Key]+'</th>';
                                        if(j=='Ought'){totelOught+=Number(result["totel"][Key])};
                                        if(j=='Actual'){totelActual+=Number(result["totel"][Key])};
                                        if(j=='Adjust'){totelAdjust+=Number(result["totel"][Key])};
                                        if(j=='Wait'){totelWait+=Number(result["totel"][Key])};
                                    }
                                }
                                $tr+='<th>'+totelOught+'</th><th>'+totelActual+'</th><th>'+totelAdjust+'</th><th>'+totelWait+'</th></tr>';
                                $('#'+Ids+'table_wrapper>.row:nth-child(2)>div .dataTables_scrollBody>table>tbody').append($tr);
                            }
                        }
                    },100);
                }
            });
        },
        // 列表表头字段
        columns:Tds.columns,
        columnDefs:Tds.columnDefs,
        language:{
            "sProcessing": "加载中...",
            "sLengthMenu": "每页 _MENU_ 项",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
            "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "未加载到匹配的数据",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "<",
                "sNext": ">",
                "sLast": "末页",
                "sJump": "跳转"
            }
        },
        fnDrawCallback: function(table) {
            if(pageCount>7){//页码大于7出现页码跳转方式
                $('#'+Ids+'table_paginate .pagination').append('<li class="jump">跳至&nbsp;&nbsp;<input type="text">&nbsp;&nbsp;页&nbsp;&nbsp;<a href="javascript:void(0);" class="page-link" data-dt-id="#'+Ids+'table">GO</a></li>');
            }
        }
    });
};
claimTd={
    columns:[
        {data:'flowNumber'},
        {data:'transactionDate'},
        {data:'hotelName'},
        {data:'hotelId'},
        {data:'drawee'},
        {data:'type'},
        {data:'paymentAccount'},
        {data:'transactionAmount'},
        {data:'summary'},
        {data:'receivingBank'},
        {data:'claimedAmount'},
        {data:'tobeClaimedAmount'},
        {data:'claimTimes'},
        {data:'updateDate'}
    ],
    columnDefs:[
        {
            "render": function (value,type,row) {
                if(row.status==1){
                    return value;
                }else {
                    return '<span style="display:inline-block;width:50%;">'+value+'</span><span style="display:inline-block;width:50%;"><button name="'+row.hotelId+'/'+row.id+'" class="btn btn-danger btn-sm claim" style="margin:0 10px">认领</button></span>';
                }
            },
            "targets":7 //指定渲染列
        }
    ]
};
statisticsTd={
    columns:[
        {data:'monthTime'},
        {data:'hotelName'},
        {data:'hotelId'},
        {data:'pmsOughtPrice'},//PMS系统维护费
        {data:'pmsActualPrice'},
        {data:'pmsAdjustPrice'},
        {data:'pmsWaitPrice'},
        {data:'otaOughtPrice'},//OTA代运营费
        {data:'otaActualPrice'},
        {data:'otaAdjustPrice'},
        {data:'otaWaitPrice'},
        {data:'conveyOughtPrice'},//输送客源费
        {data:'conveyActualPrice'},
        {data:'conveyAdjustPrice'},
        {data:'conveyWaitPrice'},
        {data:'cooperationOughtPrice'},//合作服务费
        {data:'cooperationActualPrice'},
        {data:'cooperationAdjustPrice'},
        {data:'cooperationWaitPrice'},
        {data:'nightOughtPrice'},//会员服务费
        {data:'nightActualPrice'},
        {data:'nightAdjustPrice'},
        {data:'nightWaitPrice'},
        {data:'rentalOughtPrice'},//布草租赁收款
        {data:'rentalActualPrice'},
        {data:'rentalAdjustPrice'},
        {data:'rentalWaitPrice'},
        {data:'washOughtPrice'},//布草洗涤收款
        {data:'washActualPrice'},
        {data:'washAdjustPrice'},
        {data:'washWaitPrice'},
        {data:'damageOughtPrice'},//布草报损赔偿款
        {data:'damageActualPrice'},
        {data:'damageAdjustPrice'},
        {data:'damageWaitPrice'},
        {data:'personnelOughtPrice'},//人力众包收款
        {data:'personnelActualPrice'},
        {data:'personnelAdjustPrice'},
        {data:'personnelWaitPrice'},
        {data:''},//合计
        {data:''},
        {data:''},
        {data:''}
    ],
    columnDefs:[
        {
            "render": function (value,type,row) {
                value=row.pmsOughtPrice+row.otaOughtPrice+row.conveyOughtPrice+row.cooperationOughtPrice+row.nightOughtPrice+row.rentalOughtPrice+row.washOughtPrice+row.damageOughtPrice+row.personnelOughtPrice;
                return value;
            },
            "targets":-4 //合计应收
        },
        {
            "render": function (value,type,row) {
                value=row.pmsActualPrice+row.otaActualPrice+row.conveyActualPrice+row.cooperationActualPrice+row.nightActualPrice+row.rentalActualPrice+row.washActualPrice+row.damageActualPrice+row.personnelActualPrice;
                return value;
            },
            "targets":-3 //合计实收
        },
        {
            "render": function (value,type,row) {
                value=row.pmsAdjustPrice+row.otaAdjustPrice+row.conveyAdjustPrice+row.cooperationAdjustPrice+row.nightAdjustPrice+row.rentalAdjustPrice+row.washAdjustPrice+row.damageAdjustPrice+row.personnelAdjustPrice;
                return value;
            },
            "targets":-2 //合计调整
        },
        {
            "render": function (value,type,row) {
                value=row.pmsWaitPrice+row.otaWaitPrice+row.conveyWaitPrice+row.cooperationWaitPrice+row.nightWaitPrice+row.rentalWaitPrice+row.washWaitPrice+row.damageWaitPrice+row.personnelWaitPrice;
                return value;
            },
            "targets":-1 //合计未收
        }
    ]
};


