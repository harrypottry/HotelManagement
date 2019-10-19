$(document).ready(function(){
    $quickQuery({"hotelId":hotels},{'isPage':false});
    getrequest('.contractRecipientId','/console/api/exportemployeeList');// 获取领用人列表
});
$('.query').click(function(datas,pageCount){//页面请求表格数据   pageCount:总页码数
    datas={
        contractType:$('select.contractType').val(),
        contractStatus:$('li>select.contractStatus').val(),
        contractRecipientId:$('li>select.contractRecipientId').val(),
        contractSignatoryId:$('input.contractSignatoryId').attr('name')
    };
    $('.contractSignatoryId').val(hotelbug[datas.contractSignatoryId]);
    $('#table').dataTable({
        scrollY:window.innerHeight-420,
        scrollX:true,
        destroy:true,
        ordering:false,//排序
        lengthChange:false,
        paging:true,
        aLengthMenu:[20],
        filter:false,
        serverSide: true,  //启用服务器端分页
        searching:false,  //禁用原生搜索
        ajax: function (data, callback, settings){
            datas.pageNo=(data.start / data.length)+1;//当前页码
            datas.pageSize='20';//每页条数
            $.ajax({
                type:"GET",
                url:'/console/api/exportGetContractListList',
                cache: false,//禁用缓存
                data:datas,//传入组装的参数
                dataType:"json",
                success: function (result) {
                    pageCount=result.pageCount;//总页码
                    var returnData = {};//封装返回数据
                    returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                    returnData.recordsTotal = result.count;//返回数据全部记录
                    returnData.recordsFiltered = result.count;//后台不实现过滤功能，每次查询均视作全部结果
                    returnData.data = result.dataList;//返回的数据列表
                    callback(returnData);
                }
            });
        },
        // 列表表头字段
        columns:[
            {data:'contractNumber'},
            {data:'contractTypeName'},
            {data:'contractNumberTouch'},
            {data:'contractRecipientName'},
            {data:'contractReceiveTime'},
            {data:'contractSignatoryName'},
            {data:'contractSignatoryId'},
            {data:'contractStatusName'},
            {data:'comments'},
            {data:'contractNumber'}
        ],
        columnDefs:[
            {
                "render": function (value,type,row){
                    var $btns='';
                    if(row.contractStatus=='1'||row.contractStatus=='2'||row.contractStatus=='9'){//未领用、领用中、拒收
                        $btns+='<span class="getbtn" data-btn="receive" name='+row.id+'>领用</span>';
                    }else{
                        $btns+='<span class="nobtn">领用</span>';
                    }
                    if(row.contractStatus=='5'||row.contractStatus=='8'){//待归档、已作废
                        $btns+='<span class="getbtn" data-btn="file" name='+row.id+'>归档</span>';
                    }else{
                        $btns+='<span class="nobtn">归档</span>';
                    }
                    if(row.contractStatus=='7'||row.contractStatus=='8'){//已丢失、已作废
                        $btns+='<span class="nobtn">丢失作废</span>';
                    }else{
                        $btns+='<span class="getbtn" data-btn="destroy" name='+row.id+'>丢失作废</span>';
                    }
                    if(row.contractType=='1'){//服务协议addrepair
                        $btns+='<span class="getbtn" data-btn="addrepair" name='+row.contractNumber+'>添加补充协议</span>';
                    }else{
                        $btns+='<span class="nobtn">添加补充协议</span>';
                    }
                    return $btns;
                },
                "targets":-1 //操作列
            }
        ],
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
                $('#table_paginate .pagination').append('<li class="jump">跳至&nbsp;&nbsp;<input type="text">&nbsp;&nbsp;页&nbsp;&nbsp;<a href="javascript:void(0);" class="page-link" data-dt-id="#table">GO</a></li>');
            }
        }
    });
});
$('.download').click(function(datas){//导出
    datas={
        contractType:$('select.contractType').val(),
        contractStatus:$('li>select.contractStatus').val(),
        contractRecipientId:$('li>select.contractRecipientId').val(),
        contractSignatoryId:$('input.contractSignatoryId').attr('name')
    };
    downloads('/console/api/exportGetContractListExtel',datas);//导出请求
});
$('.addservice').click(function () {//添加服务协议
    $('.addservicemodal').fadeToggle(200);
    $('.addservicemodal input.contractNumber').val('');
});
$('.getaddservice').click(function(datas){//添加服务协议-点击确定
    datas={contractNumber:$('.addservicemodal input.contractNumber').val()};
    if(datas.contractNumber){
        getrequest('.addservicemodal','/console/api/insertContract',datas);
    }else{
        $('.addservicemodal input.contractNumber').focus();
    }
});
$(document).on("click","span.getbtn",function(btn){//点击操作内按钮
    btn=$(this).attr('data-btn');
    if(btn=='receive'){//分配合同（领用）
        $('.receivemodal').fadeToggle(200);
        $('.receivemodal select.contractRecipientId').attr('name',$(this).attr('name'));
    }
    if(btn=='file'){//归档
        $('.filemodal').fadeToggle(200);
        $('.filemodal .getfile').attr('name',$(this).attr('name'));
    }
    if(btn=='destroy'){//丢失作废
        $('.destroymodal').fadeToggle(200);
        $('.destroymodal select.contractStatus').attr('name',$(this).attr('name'));
        $('.destroymodal .comments').val('');
    }
    if(btn=='addrepair'){//添加补充协议
        $('.addrepairmodal').fadeToggle(200);
        $('.addrepairmodal input.contractNumberTouch').val($(this).attr('name'));
        $('.addrepairmodal input.contractNumber').val('');
    }
});
$('.getreceive').click(function(datas){//分配合同（领用）-点击确定
    datas={id:$('.receivemodal select.contractRecipientId').attr('name'),contractRecipientId:$('.receivemodal select.contractRecipientId').val()};
    getrequest('.receivemodal','/console/api/contractReceive',datas);
});
$('.getfile').click(function(datas){//归档-点击确定
    datas={id:$(this).attr('name')};
    getrequest('.filemodal','/console/api/contractPigeonhole',datas);
});
$('.getdestroy').click(function(datas){//丢失作废-点击确定
    datas={id:$('.destroymodal select.contractStatus').attr('name'),contractStatus:$('.destroymodal select.contractStatus').val(),comments:$('.destroymodal .comments').val()};
    getrequest('.destroymodal','/console/api/contractDrop',datas);
});
$('.getaddrepair').click(function(datas){//添加补充协议-点击确定
    datas={contractNumber:$('.addrepairmodal input.contractNumber').val(),contractNumberTouch:$('.addrepairmodal input.contractNumberTouch').val()};
    if(datas.contractNumber){
        getrequest('.addrepairmodal','/console/api/insertContractTouch',datas);
    }else{
        $('.addrepairmodal input.contractNumber').focus();
    }
});
function getrequest(dom,url,datas){//get请求
    $.ajax({
        url :url,
        data :datas,
        type: "GET",
        success:function(data){
            if(dom=='.contractRecipientId'){//领用人
                var $options='';
                for(var i of data){
                    $options+='<option value='+i.contractRecipientId+'>'+i.contractRecipientName+'</option>';
                }
                $(dom).append($options);
            }else{//添加服务协议、分配合同（领用）、归档、丢失作废、添加补充协议
                if(data.status=='succeed'){
                    $(dom).fadeToggle(200);
                    $('.query').click();
                }
                warn(data.msg);
            }
        }
    });
}