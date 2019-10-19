var surveyId=sessionStorage.getItem('surveyId');
var basic={id:surveyId};//数据库酒店概况数据
$(document).ready(function(){
    getrequest('basic','/console/api/getIntentionStoreSurvey');// 获取酒店概况
    // getrequest('agree','/console/api/getIntentionStoreInfo');// 获取签约信息
});
//酒店概况
$('#basic>.edit .btn').click(function(goon){//保存或提交
    basic['type']=$(this).attr('data-id');//1保存2提交
    goon=true;
    $('#basic>.edit .form-control').each(function(name){
        name=$(this).attr('data-id');
        basic[name]=$(this).val();
        if(basic.type=='1'&&name=='hotelName'){//1保存--酒店名称必填
            if(!$(this).val()){
                goon=false;alert('请填写酒店名称！');$(this).focus();return false;
            }
        }
        if(basic.type=='2'&&!(name=='hotelCity'||name=='hotelCounty'||name=='owernEmail'||name=='remark')){//2提交
            if(!$(this).val()){
                goon=false;
                alert('请填写必填项！');
                $(this).focus();return false;
            }
        }
    });
    if(goon){
        if(basic.type=='2'){//2提交
            if(!basic['doorwayPhoto']){alert('请上传门头照片！');return false;}
            if(!basic['owernIdcardPhotoz']){alert('请上传业主身份证正面！');return false;}
            if(!basic['owernIdcardPhotof']){alert('请上传业主身份证反面！');return false;}
        }
        $.ajax({
            url:'/console/api/updateIntentionStoreSurvey',
            type: 'post',
            contentType: 'application/json',
            data:JSON.stringify(basic),
            success:function (res){
                warn(res.msg);
            }
        })
    }
});
// 照片上传
$('#basic>.edit .import-btn').change(function($input,file){
    $input=$(this);
    file=$input[0].files[0];
    var formData = new FormData();
    formData.append('file',file);
    $.ajax({
        url:'/console/api/addIntentionStoreSurveyPhoto',
        type: 'post',
        processData: false,// 告诉jQuery不要去处理发送的数据
        contentType: false,// 告诉jQuery不要去设置Content-Type请求头
        data:formData,
        success: function (res) {
            if(res.length){
                basic[$input.attr('data-id')]=res[0];
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function(e){
                    $input.prev('img').attr('src',e.target.result).show();
                }
            }else{
                $input.prev('img').hide();
                basic[$input.attr('data-id')]='';
            }
        }
    })
});

// 签约信息
$('#agree>.edit .check').click(function(){
    $(this).toggleClass('active');
});
// 添加床型
$('#agree>.edit img.add').click(function(){
    $(this).parents('.col-md-12').find('.addpre').before($(this).parents('.col-md-12').find('.addpre').clone().attr('class','addreal'));
});
//删除床型
$(document).on("click","#agree>.edit .addreal>img.delete",function(Id){//点击操作内按钮--进入--status状态判断跳转不同页面
    $(this).parent('.addreal').remove();
});
$('#agree>.edit .btn').click(function(agree){//保存或提交
    console.log();
    agree={surveyId:surveyId,chargeConfig:[],intentionStoreInfo:{giveMaterial:[]},isiBed:[]};//签约信息
    agree.intentionStoreInfo['typeS']=$(this).attr('data-id');//1保存2提交
    goon=true;
    $('#agree>.edit .cancel').each(function(arr,$input){//可减免费用：履约保证金、项目咨询费、合作服务费、输送客源费、PMS 初始安装费、PMS系统维护费、OTA 代运营费
        arr={},$input=$(this).find('input');
        arr.chargeItem=$input.attr('data-chargeItem');
        arr.chargeRate=$input.attr('data-chargeRate');
        arr.chargeStandard=$input.attr('data-chargeStandard');
        if($(this).next('.check').attr('class')=='check active'){//减免
            arr.reduction='1';arr.chargeRule='0';
        }else{//未减免
            arr.reduction='0';
            arr.chargeRule=$input.val();
            if(agree.intentionStoreInfo.typeS=='2'&&arr.chargeRule==''){
                goon=false;
                alert('请填写必填项！');
                $input.focus();return false;
            }
        }
        agree.chargeConfig.push(arr);
    });
    if(goon){
        $('#agree>.edit .addreal').each(function(arr){
            arr={};
            arr.bedType=$(this).find('.bedType').val();
            arr.bedSize=$(this).find('.bedSize').val();
            arr.bedNum=$(this).find('.bedNum').val();
            if(agree.intentionStoreInfo.typeS=='1'&&(arr.bedType||arr.bedSize||arr.bedNum)){//保存时输入框只要非空
                agree.isiBed.push(arr);
            }
            if(agree.intentionStoreInfo.typeS=='2'){//提交时-最少填写一种床型/页面床型不可有空
                for(var i in arr){
                    if(!arr[i]){
                        goon=false;
                        alert('请将床型信息填写完整！');
                        $(this).find('.'+i).focus();return false;
                    }
                }
                agree.isiBed.push(arr);
            }
        });
    }
    if(goon){
        $('#agree>.edit div.give>.active').each(function(){//赠送物资
            agree.intentionStoreInfo.giveMaterial.push($(this).html());
        });
        if($('#agree>.edit span.give.active').length){//赠送物资-选中其他请填写
            if(!$('#agree>.edit textarea.give').val()){
                alert('请填写赠送物资！');
                $('#agree>.edit textarea.give').focus();return;
            }
            agree.intentionStoreInfo.giveMaterial.push($('#agree>.edit textarea.give').val());
            agree.intentionStoreInfo.giveOther='1';
        }else{
            agree.intentionStoreInfo.giveOther='0';
        }
        if($('#agree>.edit span.mend.active').length){//补充协议
            agree.intentionStoreInfo.type='1';
            if(!$('#agree>.edit textarea.mend').val()){
                alert('请填写补充协议内容！');
                $('#agree>.edit textarea.mend').focus();return;
            }
            agree.intentionStoreInfo.content=$('#agree>.edit textarea.mend').val();
        }else{//无补充协议
            agree.intentionStoreInfo.type='0';
            agree.intentionStoreInfo.content='';
        }
        $.ajax({
            url:'/console/api/addIntentionStoreInfo',
            type: 'post',
            contentType: 'application/json',
            data:JSON.stringify(agree),
            success:function (res){
                warn(res.msg);
            }
        })
    }
});





// 获取酒店概况、签约信息
function getrequest(Id,url){
    $.ajax({
        url :url,
        data :{surveyId:surveyId},
        type: "GET",
        success:function(res){
            if(Id=='basic'){// 获取酒店概况
                var Province='',City='',County='';//省市县
                if(res.hotelProvince){Province=ChineseDistricts['86'][res.hotelProvince];}//省
                if(res.hotelProvince&&res.hotelCity){City=ChineseDistricts[res.hotelProvince][res.hotelCity];}//市
                if(res.hotelCity&&res.hotelCounty){ County=ChineseDistricts[res.hotelCity][res.hotelCounty];}//县
                if(res.status=='1'){//未提交-可编辑
                    $('#basic>.edit input.form-control').each(function(){
                        $(this).val(res[$(this).attr('data-id')]);
                    });
                    $('#basic>.edit textarea.form-control').val(res['remark']);
                    $("#distpicker").distpicker({province:Province,city:City,district:County});//省市县
                    $('#basic>.edit .import-btn').each(function(Photo){//照片
                        Photo=res[$(this).attr('data-id')].split('&').slice(-1)[0];
                        if(Photo!='null'&&Photo!=''){
                            $(this).prev('img').attr('src',res[$(this).attr('data-id')]).show();
                            basic[$(this).attr('data-id')]=Photo;
                        }
                    });
                }
                if(res.status=='2'){//已提交-可查看
                    getrequest('agree','/console/api/getIntentionStoreInfo');// 获取签约信息
                    $('#basic>.edit').hide();$('#basic>.browse').show();
                    $('#basic>.browse .form-txt').each(function(){
                        $(this).html(res[$(this).attr('data-id')]);
                    });
                    $('#basic>.browse .distpicker').html(Province+'-'+City+'-'+County);
                    $('#basic>.browse .upimg').each(function(Photo){//照片
                        Photo=res[$(this).attr('data-id')].split('&').slice(-1)[0];
                        if(Photo!='null'&&Photo!=''){
                            $(this).find('img').attr('src',res[$(this).attr('data-id')]).show();
                        }
                    });
                }

            }
            if(Id=='agree'){//获取签约信息
                $('a.nav-link.agree').show();
                console.log(res);
                if(!res.intentionStoreInfo){//服务器没有签约信息
                    console.log('11111');
                }else{
                    console.log('1');
                    if(res.intentionStoreInfo.typeS=='1'){//未提交-可编辑
                        $('#agree>.edit .cancel').each(function(index,$input){//基本费用
                            $input=$(this).find('input');
                            if($input.attr('data-chargeItem')==res.chargeConfig[index].chargeItem){
                                $input.val(res.chargeConfig[index].chargeRule);
                                $input.attr('data-id',res.chargeConfig[index].id);
                                console.log(res.chargeConfig[index].reduction);
                                if(res.chargeConfig[index].reduction=='1'){
                                    $(this).next('.check').attr('class','check active');
                                }
                            }
                        });
                        //赠送物资
                        if(res.intentionStoreInfo.type=='1'){//存在其他赠送物资
                            $('#agree>.edit span.give').attr('class','check give active');
                            $('#agree>.edit textarea.give').val(res.intentionStoreInfo.giveMaterial[res.intentionStoreInfo.giveMaterial.length-1]);
                        }
                    }
                    if(res.intentionStoreInfo.typeS=='2'){//已提交-可查看
                        console.log('1');
                        $('#agree>.edit').hide();$('#agree>.browse').show();
                        $('#agree>.browse .cancel>span').each(function(index){//基本费用
                            $(this).html(res.chargeConfig[index].chargeRule);
                        });
                        //赠送物资
                        if(res.intentionStoreInfo.type=='1'){//存在其他赠送物资---pan--
                            $('#agree>.browse span.give').html('其他：'+res.intentionStoreInfo.giveMaterial.pop());
                        }
                        for(var i of res.intentionStoreInfo.giveMaterial){
                            $('#agree>.browse div.give').append('<span class="check">'+i+'</span>');
                        }
                        // 补充协议
                        if(res.intentionStoreInfo.type=='1'){//存在补充协议
                            $('#agree>.browse span.mend').html(res.intentionStoreInfo.content);
                        }
                    }
                }
            }
        }
    });
}