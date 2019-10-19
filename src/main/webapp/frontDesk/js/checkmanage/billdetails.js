//lease布草租赁对帐、washing布草洗涤对账、damage布草报损对账、manpower人力众包对账
var leasedata,washingdata,damagedata,manpowerdata;
var leaseTd,washingTd,damageTd,manpowerTd;
$(document).ready(function(){
    $quickQuery({"leasehotelId":hotels, "washinghotelId":hotels,"damagehotelId":hotels, "manpowerhotelId":hotels},{'isPage': false});
    getlist('washing','/console/api/getWashFactory');// 获取洗涤供应商列表
    getlist('manpower','/wx/console/api/getemployeenamebyloginidinworking');// 获取阿姨列表
    btn('lease');btn('washing');btn('damage');btn('manpower');//初始化页面
});
function btn(Ids,datas,url,Tds) {//查询
    datas={};
    datas.hotelId=localStorage["id"];
    datas.beginTime=$('#'+Ids+' .card ul li .beginTime').val();
    datas.endTime=$('#'+Ids+' .card ul li .endTime').val();
    if(Ids=='lease'){//布草租赁对帐
        leasedata=datas;
        url='/console/api/getRentalList';
        Tds=leaseTd;
    }
    if(Ids=='washing'){//布草洗涤对账
        datas.employeeId=$('#'+Ids+' .card ul li .employeeId').val();
        washingdata=datas;
        url='/console/api/getWashList';
        Tds=washingTd;
    }
    if(Ids=='damage'){//布草报损对账
        damagedata=datas;
        url='/console/api/getClothErrorList';
        Tds=damageTd;
    }
    if(Ids=='manpower'){//人力众包对账
        datas.employeeId=$('#'+Ids+' .card ul li .employeeId').val();
        manpowerdata=datas;
        url='/console/api/getmissionbytimeandhotelid';
        Tds=manpowerTd;
    }
    request(Ids,datas,url,Tds);
}
function download(Ids,datas,url) {//导出
    if(Ids=='lease'){
        url='/console/api/exportRentalList';
        datas=leasedata;
    }
    if(Ids=='washing'){
        url='/console/api/exportWashList';
        datas=washingdata;
    }
    if(Ids=='damage'){
        url='/console/api/exportClothErrorList';
        datas=damagedata;
    }
    if(Ids=='manpower'){
        url='/consoles/api/exportmissionbytimeandhotelidlist';
        datas=manpowerdata;
    }
    delete datas.pageNo;
    delete datas.pageSize;
    //导出请求
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
}
function request(Ids,datas,url,Tds,pageCount){//页面请求表格数据   pageCount:总页码数
    $('#'+Ids+'table').dataTable({
        destroy:true,
        ordering:false,//排序
        lengthChange:false,
        paging:true,
        aLengthMenu:[10],
        filter:false,
        serverSide: true,  //启用服务器端分页
        searching:false,  //禁用原生搜索
        ajax: function (data, callback, settings){
            datas.pageNo=(data.start / data.length)+1;//当前页码
            datas.pageSize='10';//每页条数
            $.ajax({
                type:"GET",
                url:url,
                cache: false,//禁用缓存
                data:datas,//传入组装的参数
                dataType:"json",
                success: function (result) {
                    setTimeout(function () {//setTimeout仅为测试延迟效果
                        pageCount=result.data.pageCount;//总页码
                        var returnData = {};//封装返回数据
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.data.count;//返回数据全部记录
                        returnData.recordsFiltered = result.data.count;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data.dataList;//返回的数据列表
                        callback(returnData);
                        $('#'+Ids+' .card ul li .hotelId').val(hotelbug[datas.hotelId]);
                        $('#'+Ids+' .condition>span:first-child').html(localStorage["name"]+'：'+datas.beginTime.replace(/-/g,'.')+'-'+datas.endTime.replace(/-/g,'.'));
                        if(pageCount>0){
                            var $tr='';
                            if(Ids=='lease'){//布草租赁对帐
                                if(pageCount>1){$tr+='<tr role="row"><th>本页合计</th><th colspan="7"></th><th>'+result.pagetotal+'</th></tr>';}
                                $tr+='<tr role="row"><th>总计</th><th colspan="7"></th><th>'+result.total+'</th></tr>';
                            }
                            if(Ids=='washing'){//布草洗涤对账
                                if(pageCount>1){$tr+='<tr role="row"><th>本页合计</th><th colspan="7"></th><th>'+result.pagecollecttotal+'</th><th>'+result.pagepaytotal+'</th></tr>';}
                                $tr+='<tr role="row"><th>总计</th><th colspan="7"></th><th>'+result.collecttotal+'</th><th>'+result.paytotal+'</th></tr>';
                            }
                            if(Ids=='damage'){//布草报损对账
                                if(pageCount>1){$tr+='<tr role="row"><th>本页合计</th><th colspan="5"></th><th>'+result.pagetotal+'</th><th colspan="3"></th></tr>';}
                                $tr+='<tr role="row"><th>总计</th><th colspan="5"></th><th>'+result.total+'</th><th colspan="3"></th></tr>';
                            }
                            if(Ids=='manpower'){//人力众包对账
                                if(pageCount>1){$tr+='<tr role="row"><th>本页合计</th><th colspan="8"></th><th>'+result.pagetotel.pagePrice+'</th><th>'+result.pagetotel.pageReworkPrice+'</th><th></th><th>'+result.pagetotel.pageCleanPrice+'</th><th>'+result.pagetotel.pageTotelSumPrice+'</th><th>'+result.pagetotel.pageTotelSumPrice+'</th></tr>';}
                                $tr+='<tr role="row"><th>总计</th><th colspan="8"></th><th>'+result.totel.totelPrice+'</th><th>'+result.totel.totelReworkPrice+'</th><th></th><th>'+result.totel.totelCleanPrice+'</th><th>'+result.totel.totelSumPrice+'</th><th>'+result.totel.totelSumPrice+'</th></tr>';
                            }
                            $('#'+Ids+'table_wrapper>.row:nth-child(2)>div>table>tbody').append($tr);
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
    $('#'+Ids+'table_wrapper>.row:nth-child(2)').addClass('card');
    $('#'+Ids+'table_wrapper>.row:nth-child(2)>div').addClass('card-body table-responsive');
};
leaseTd={
    columns:[
        {data:'clothName'},
        {data:'clothSize'},
        {data:'clothMaterial'},
        {data:'clothId'},
        {data:'hotelName'},
        {data:'hotelID'},
        {data:'beginDate'},
        {data:'endDate'},
        {data:'rentalPrice'}
    ],
    columnDefs:[]
};
washingTd={
    columns:[
        {data:'clothName'},
        {data:'clothSize'},
        {data:'clothMaterial'},
        {data:'clothId'},
        {data:'hotelName'},
        {data:'hotelID'},
        {data:'beginDate'},
        {data:'endDate'},
        {data:'collectWashPrice'},
        {data:'payWashPrice'}
    ],
    columnDefs:[]
};
damageTd={
    columns:[
        {data:'cloth_type'},
        {data:'cloth_size'},
        {data:'cloth_material'},
        {data:'cloth_id'},
        {data:'error_type'},
        {data:'recycle_num'},
        {data:'suggest_pay'},
        {data:'hotel_name'},
        {data:'error_time'}
    ],
    columnDefs:[
        {
            "render": function (value) {
                var type={Lost:'丢失',Broken:'破损',QRBroken:'二维码破损'};
                value=type[value];
                return value;
            },
            "targets":4 //指定渲染列
        }
    ]
};
manpowerTd={
    columns:[
        {data:'employeeName'},
        {data:'employeeId'},
        {data:'hotelName'},
        {data:'hotelId'},
        {data:'roomName'},
        {data:'missionType'},
        {data:'missionId'},
        {data:'beginTime'},
        {data:'endTime'},
        {data:'price'},
        {data:'reworkPrice'},
        {data:'isOk'},
        {data:'cleanPrice'},
        {data:'isOk'},
        {data:'isOk'}
    ],
    columnDefs:[
        {
            "render": function (value) {
                value='';
                return value;
            },
            "targets":11 //指定渲染列
        },
        {
            "render": function (value,type,row) {
                if(value=='2'){
                    value=(row.cleanPrice-row.reworkPrice).toFixed(2)
                }else {
                    value=row.cleanPrice
                }
                return value;
            },
            "targets":13 //指定渲染列
        },
        {
            "render": function (value,type,row) {
                if(value=='2'){
                    value=(row.cleanPrice-row.reworkPrice).toFixed(2)
                }else {
                    value=row.cleanPrice
                }
                return value;
            },
            "targets":14 //指定渲染列
        }
    ]
};
function getlist(Ids,url){//洗涤供应商、阿姨列表
    $.ajax({// 获取洗涤供应商列表
        url :url,
        data :'',
        type: "GET",
        success:function(data){
            var $options='';
            for(var i of data){
                $options+='<option value='+i.id+'>'+i.name+'</option>'
            }
            $('#'+Ids+' .employeeId').append($options);
        }
    });
}


