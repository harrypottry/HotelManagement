function dateFormat(date) {
	var date = new Date(date);
    var y = date.getFullYear();
    var m = toPadZero(date.getMonth() + 1);
    var d = toPadZero(date.getDate());
    var h = toPadZero(date.getHours());
    var mm = toPadZero(date.getMinutes());
    var s = toPadZero(date.getSeconds());

    return y + '年' + m + '月' + d + '日' + ' ' + h + ':' + mm
    // 2018   -     09    -   08   -    09   -   08
}

function toPadZero(num) {
    return num < 10 ? '0' + num : num
}