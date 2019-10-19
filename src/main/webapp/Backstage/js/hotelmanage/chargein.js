var hotelId;//酒店Id
var againtableTd,recordtableTd;
var recordunit;//recordtable表中计费规则单位
var chargeItem={'1':'PMS系统维护费','2':'OTA代运营费','3':'输送客源费','4':'合作服务费','5':'会员服务费'};
var chargeRate={'1':'元/年','2':'元/季','3':'元/月','4':'元'};
var chargeRaTe={'1':'年度','2':'季度','3':'月度','4':'一次性'};
$(document).ready(function(){
    $quickQuery({"hotelId":hotels.slice(1)},{'isPage':false});
});
$("#setting>.card>ul>li>#hotelId").focus(function(){
    $('#setting>#first').hide();
    $('#setting>#again').hide();
});
function confirm(){//点击确定-酒店
    hotelId=$('#hotelId').attr('name');
    if(hotelId){
        $('#hotelId').val(hotelbug[hotelId]);
        $('#setting>#first input').val('');
        $('#setting>#first .remark').val('');
        setting('againtable',{hotelId:hotelId},'/console/api/getChargeConfigByHotelId',againtableTd);//收费项目设置初请求
    }else{
        alert('请选择酒店');
    }
}
// 年月日
$('#first .startDate').datepicker({//合同开始时间
    autoclose:true,
    todayHighlight:true,
    language:"zh"
}).on('changeDate',function(e){//开始时间控制结束时间及生效时间
    $('#first .endDate').datepicker('setStartDate',e.date);
    $('#first .validDate').datepicker('setStartDate',e.date);
});
$('#first .endDate').datepicker({//合同结束时间
    autoclose:true,
    todayHighlight:true,
    language:"zh"
}).on('changeDate',function(e){//结束时间控制生效时间
    $('#first .validDate').datepicker('setEndDate',e.date);
});
$('.validDate').datepicker({//生效日期
    autoclose:true,
    todayHighlight:true,
    language:"zh"
});
$('#first select').change(function(){
    $(this).parents('tr').find('span').attr('name',$(this).val()).html(chargeRate[$(this).val()]);
});
function putIn(datas,chargenum,stopputIn){
    datas={list:[],cca:{}};chargenum=0;stopputIn=false;
    datas.cca.contractNum=$('#first .contractNum').val();
    if(!datas.cca.contractNum){alert('请填写合同编号！');return;}
    datas.cca.startDate=$('#first .startDate').val();
    datas.cca.endDate=$('#first .endDate').val();
    if(!datas.cca.startDate&&!datas.cca.endDate){alert('请填写计费周期！');return;}
    $('#first table tbody input.chargeRule').each(function(){        
        var inputs={};
        inputs.chargeItem=$(this).attr('name');//收费项目
        inputs.chargeRate=$(this).next('span').attr('name');//收费频率
        inputs.chargeStandard=$(this).parent('td').prev('td').html();//收费基准
        inputs.chargeRule=$(this).val();//收费金额
        inputs.beginDate=$(this).parent('td').next('td').find('.validDate').val();//生效日期
        if(inputs.chargeRule){
            if(inputs.beginDate){
                chargenum++;
            }else{
                stopputIn=true;
                alert('请填写'+chargeItem[inputs.chargeItem]+'的生效日期！');return false;
            }
        }
        datas.list.push(inputs);
    });
    if(stopputIn){return;}//因存在未写的生效日期停止putIn函数
    if(!chargenum){alert('最少填写一个收费项目');return;}
    datas.hotelId=hotelId;
    datas.cca.remark=$('#first .remark').val();
    $.ajax({
        url :'/console/api/saveChargeConfig',
        contentType: 'application/json',
        data:JSON.stringify(datas),
        type: "POST",
        success:function(data){
            if(data.status=='success'){
                $('#first input').val('');
                $('#first .remark').val('');
                setting('againtable',{hotelId:hotelId},'/console/api/getChargeConfigByHotelId',againtableTd);//收费项目设置请求
            }
            warn(data.msg);
        }
    });
}
$(document).on('click','#setting>#again #againtable>tbody>tr>td>.adjust',function(Charge,chargeItem){//点击调整
    Charge=$(this).attr('name').split("/")[1];//有没有设置金额
    chargeItem=$(this).attr('name').split("/")[2];//收费项目
    $('.adjustmodal').fadeToggle(200);
    $('.adjustmodal .charge').attr('name',$(this).attr('name').split("/")[0]);//配置id
    $('.adjustmodal .contractNum').val('');//配置id
    if(Charge!='null'){
        $('.adjustmodal .charge').val(Charge);
    }else{
        $('.adjustmodal .charge').val('');
    }
    if(chargeItem<3){//PMS系统维护费、OTA代运营费
        if(Charge!='null'){
            $('.adjustmodal select').html('<option value='+$(this).attr('name').split("/")[3]+'>'+chargeRate[$(this).attr('name').split("/")[3]]+'</option>');
        }else{
            $('.adjustmodal select').html('<option value="1">元/年</option><option value="2">元/季</option><option value="3">元/月</option><option value="4">元/一次性</option>');
        }
    }
    if(chargeItem==3||chargeItem==4){//输送客源费、合作服务费
        $('.adjustmodal select').html('<option value="3">%/月</option>');
    }
    if(chargeItem==5){//会员服务费
        $('.adjustmodal select').html('<option value="3">元/间夜</option>');
    }
});
$('.getadjust').click(function(datas){//调整收费-点击确定
    datas={hotelId:hotelId,chargeRenewConfig:{}};
    datas.charge=$(this).parents('.modal-content').find('.charge').val();
    if(!datas.charge){alert('请填写计费规则！');return;}
    datas.contractNum=$(this).parents('.modal-content').find('.contractNum').val();
    if(!datas.contractNum){alert('请填写合同编号！');return;}
    datas.beginDate=$(this).parents('.modal-content').find('.beginTime').val();
    datas.chargeConfigId=$(this).parents('.modal-content').find('.charge').attr('name');
    datas.chargeRate=$(this).parents('.modal-content').find('select').val();
    $.ajax({
        url: '/console/api/updateChargeRenewConfig',
        contentType: 'application/json',
        data:JSON.stringify(datas),
        type: "POST",
        success: function (data) {
            if(data.status=='success'){
                $('.adjustmodal').fadeToggle(200);
            }
            warn(data.msg);
        }
    });
});
$(document).on('click','#setting>#again #againtable>tbody>tr>td>.record',function(){//点击历史收费
    $('.recordmodal').fadeToggle(200);
    recordunit=$(this).parent().prev().find('span').html();//费用单位
    setting('recordtable',{hotelId:hotelId,chargeConfigId:$(this).attr('name')},'/console/api/getChargeRenewConfig',recordtableTd);//查看历史收费
});
function setting(Ids,datas,url,Tds){
    $('#'+Ids).dataTable({
        ordering:false,//排序
        scrollCollapse:true,
        paging:false,
        filter:false,
        info:false,
        destroy:true,
        ajax: function (data, callback, settings){
            $.ajax({
                type:"GET",
                url:url,
                cache: false,//禁用缓存
                data:datas,//传入组装的参数
                dataType:"json",
                success: function (result) {
                    var returnData = {};//封装返回数据
                    if(Ids=='againtable'){
                        if(result.list.length=='0'){
                            $('#first').show();
                            $('#again').hide();
                        }else{
                            $('#again').show();
                            $('#first').hide();
                            $('#again>ul>li:first-child>span').html(result.cca.contractNum);
                            $('#again>ul>li:last-child>span').html(result.cca.startDate.replace(/-/g,'.')+'-'+result.cca.endDate.replace(/-/g,'.'));
                            $('#again>.card-body>div>p>.remark').html(result.cca.remark);
                            returnData.data = result.list;//返回的数据列表
                            callback(returnData);
                        }
                    }
                    if(Ids=='recordtable'){
                        returnData.data = result.data;//返回的数据列表
                        callback(returnData);
                    }
                }
            });
        },
        // 列表表头字段
        columns:Tds.columns,
        columnDefs:Tds.columnDefs
    });
};
againtableTd={
    columns:[
        {data:'chargeItem'},
        {data:'chargeRate'},
        {data:'chargeStandard'},
        {data:'chargeRule'},
        {data:'chargeItem'}
    ],
    columnDefs:[
        {
            "render": function (value){
                return chargeItem[value];
            },
            "targets":0 //操作列
        },
        {
            "render": function (value){
                return chargeRaTe[value];
            },
            "targets":1 //操作列
        },
        {
            "render": function (value,type,row,unit){
                unit={'3':'%/月','4':'%/月','5':'元/间夜'};
                if(value){
                    if(row.chargeItem>2){
                        value=value+'<span>'+unit[row.chargeItem]+'</span>';
                    }else{
                        value=value+'<span>'+chargeRate[row.chargeRate]+'</span>';
                    }
                }else {
                    value='';
                }
                return value;
            },
            "targets":3 //操作列
        },
        {
            "render": function (value,type,row){
                return '<span class="adjust" name="'+row.id+'/'+row.chargeRule+'/'+row.chargeItem+'/'+row.chargeRate+'">调整</span><span class="record" name="'+row.id+'">历史收费</span>';
            },
            "targets":4 //操作列
        }
    ]
};
recordtableTd={
    columns:[
        {data:'charge'},
        {data:'beginDate'},
        {data:'contractNum'}
    ],
    columnDefs:[
        {
            "render": function (value){
                return value+' '+recordunit;
            },
            "targets":0 //操作列
        }
    ]
};
