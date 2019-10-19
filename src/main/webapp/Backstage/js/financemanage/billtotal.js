//hotel酒店账单、washing洗涤厂账单、manpower人力众包对账
var hoteldata,washingdata,manpowerdata;
var hotelTd,washingTd,manpowerTd;
$(document).ready(function(){
    $quickQuery({"hotelhotelId":hotels},{'isPage': false});
});
function btn(Ids,datas,url,Tds) {//查询    
    datas={};
    datas.hotelId=$('#'+Ids+' .card ul li .hotelId').attr('name');
    datas.monthTime=$('#'+Ids+' .card ul li .monthTime').val();
    if(Ids=='hotel'){//酒店账单
        hoteldata=datas;
        url='/console/api/exportAccountList';
        Tds=hotelTd;
    }
    request(Ids,datas,url,Tds);
}
function download(Ids,datas,url) {//导出
    if(Ids=='hotel'){
        url='/consoles/api/exportAccountListExtel';
        datas=hoteldata;
    }
    delete datas.pageNo;
    delete datas.pageSize;
    downloads(url,datas);//导出请求
}
function request(Ids,datas,url,Tds,pageCount){//页面请求表格数据   pageCount:总页码数
    $('#'+Ids+'table').dataTable({
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
                url:url,
                cache: false,//禁用缓存
                data:datas,//传入组装的参数
                dataType:"json",
                success: function (result) {
                    // console.log(result);
                    setTimeout(function () {//setTimeout仅为测试延迟效果
                        pageCount=result.data.pageCount;//总页码
                        var returnData = {};//封装返回数据
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.data.count;//返回数据全部记录
                        returnData.recordsFiltered = result.data.count;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data.dataList;//返回的数据列表
                        callback(returnData);
                        $('#'+Ids+' .card ul li .hotelId').val(hotelbug[datas.hotelId]);
                        $('#'+Ids+' .table-header>span:first-child').html($('#'+Ids+' .card ul li .hotelId').val()+'：'+datas.monthTime+'月账单');
                        if(pageCount>0){
                            var $tr='';
                            if(Ids=='hotel'){//酒店账单
                                if(pageCount>1){$tr+='<tr role="row"><th>本页合计</th><th></th><th>'+result.pagetotel.pagePmsPrice+'</th><th>'+result.pagetotel.pageOtaPrice+'</th><th>'+result.pagetotel.pageConveyPrice+'</th><th>'+result.pagetotel.pageCooperationPrice+'</th><th>'+result.pagetotel.pageNightPrice+'</th><th>'+result.pagetotel.pagePersonnelPrice+'</th><th>'+result.pagetotel.pageRentalPrice+'</th><th>'+result.pagetotel.pageWashPrice+'</th><th>'+result.pagetotel.pageDamagePrice+'</th><th>'+Number(result.pagetotel.pagePersonnelPrice+result.pagetotel.pageRentalPrice+result.pagetotel.pageWashPrice+result.pagetotel.pageDamagePrice)+'</th></tr>';}
                                $tr+='<tr role="row"><th>总计</th><th></th><th>'+result.totel.totelPmsPrice+'</th><th>'+result.totel.totelOtaPrice+'</th><th>'+result.totel.totelConveyPrice+'</th><th>'+result.totel.totelCooperationPrice+'</th><th>'+result.totel.totelNightPrice+'</th><th>'+result.totel.totelPersonnelPrice+'</th><th>'+result.totel.totelRentalPrice+'</th><th>'+result.totel.totelWashPrice+'</th><th>'+result.totel.totelDamagePrice+'</th><th>'+Number(result.totel.totelPersonnelPrice+result.totel.totelRentalPrice+result.totel.totelWashPrice+result.totel.totelDamagePrice)+'</th></tr>';
                            }
                            $('#'+Ids+'table_wrapper>.row:nth-child(2)>div .dataTables_scrollBody>table>tbody').append($tr);
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
hotelTd={
    columns:[
        {data:'hotelName'},
        {data:'hotelId'},
        {data:'pmsPrice'},
        {data:'otaPrice'},
        {data:'conveyPrice'},
        {data:'cooperationPrice'},
        {data:'nightPrice'},
        {data:'personnelPrice'},
        {data:'rentalPrice'},
        {data:'washPrice'},
        {data:'damagePrice'},
        {data:'washPrice'}
    ],
    columnDefs:[{
        "render": function (value,type,row) {
            return row.personnelPrice+row.rentalPrice+row.washPrice+row.damagePrice;
        },
        "targets":6 //指定渲染列
    }]
};