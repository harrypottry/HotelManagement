function getData(num, isClick) {
	var urlParams = getUrl();
	if (urlParams.id && urlParams.id != "") {
		// console.log('id:', urlParams.id)
		var num;
		$.ajax({
			type: "post",
			url: "/client/clothDetail",
			data: {
				id: 1
			},
			success: function (result) {
				var scanNumHtml, scanCodeHtml, hotelTimeHtml, hotelTimeFormatHtml, missionTimeHtml, missionTimeFormatHtml, hotelNameHtml, missionNameHtml;
				console.log(result)
				if (result.errorCode == 0) {
					if (!isClick) {
						var clothTypes = result.data.cloth.clothTypes
						num = descToNum(clothTypes)
					}
					result.hotelUpdateTime = dateFormat(result.data.hotel_worker_info.time);
					result.missionUpdateTime = dateFormat(result.data.mission_worker_info.time);
					result.hotelUpdateNumTime = getDiffDate(result.data.hotel_worker_info.time)
					result.missionUpdateNumTime = getDiffDate(result.data.mission_worker_info.time);
					scanNumHtml = '<lable> ' + result.data.cloth.scan_num + ' </lable>';
					scanCodeHtml = '<lable>' + result.data.cloth.id + '</lable>';
					hotelTimeHtml = '<lable> ' + isWhichDay(result.hotelUpdateNumTime) + '</lable>';
					hotelTimeFormatHtml = '<lable>' + result.hotelUpdateTime + '</lable>';
					missionTimeHtml = '<lable> ' + isWhichDay(result.missionUpdateNumTime) + ' </lable>';
					missionTimeFormatHtml = '<lable>' + result.missionUpdateTime + '</lable>';
					hotelNameHtml = '<lable>' + result.data.hotel_worker_info.employee_name + '</lable>';
					missionNameHtml = '<lable>' + result.data.mission_worker_info.employee_name + '</lable>';
					$('#scanNum').html(scanNumHtml);
					$('#scanCode').html(scanCodeHtml);
					$('#hotelTime').html(hotelTimeHtml);
					$('#hotelTimeFormat').html(hotelTimeFormatHtml);
					$('#missionTime').html(missionTimeHtml);
					$('#missionTimeFormat').html(missionTimeFormatHtml);
					$('#header>img').attr('src', '../assets/images/roomDetail/' + num + '.jpg');
					$('#hotelName').html(hotelNameHtml);
					$('#missionName').html(missionNameHtml);
				} else {
					console.log(result.errorDesc);
				}
			}
		});
	}
}