function getData() {
	var linenname = '';
	var urlParams = getUrl();
	if (urlParams.id && urlParams.id != "") {
		// console.log('id:', urlParams.id)
		$.ajax({
			type: "post",
			url: "/client/roomClothDetail",
			data: {
				id: urlParams["id"]
			},
			success: function (result) {
				var m;
				var data = result.data;
				var $html = '';
				result.list = {};
				if (result.errorCode == 0) {
					console.log('检测详情：', result)
					for (var i in data.clothList) {
						for (var j in data.clothList[i].cloth) {
							m = {};
							for (var a of data.clothList[i].cloth.clothTypes) {
								m[a.cloth_kind] = a.desc;
							}
						}
						result.list[i] = m;
						if(!result.list[i].Craft) result.list[i].Craft = "工艺未知";
						if (result.list[i].Type != linenname) {
							linenname = result.list[i].Type;
							$html += '<div class="clothList" id="clothList"><div class="clothItem"><div class="typeCon"><div class="left">' +
								result.list[i].Type +
								'</div><div class="rightArrow" id="right"><img src="../assets/images/roomCheck/rightArrow.png"><img src="../assets/images/roomCheck/bottomArrow.png" style="display:none;"></div></div><div class="clothDetail" id="clothDetail"><div class="conDetail"><div class="leftDetail"><span>床单等级：AA级别</span><span>面   料：' +
								result.list[i].Material + '</span><span>工   艺：' +
								result.list[i].Craft + '</span><span>清洗时间：' +
								isWhichDay(data.clothList[i].wash_time) +
								'</span><span>更换时间：' +
								isWhichDay(data.clothList[i].change_time) +
								'</span><span>更 换 人：' +
								data.clothList[i].employee_name + '</span></div><div class="rightDetail"><span>查看</span><span>详情</span></div></div></div></div></div>';
							$(".clothCon").html($html);
						}
					}
				} else {
					console.log(result.errorDesc);
				}
			}
		});
	}
}