
  // 获取URL参数
  function getUrl(value) {
    var urlData = {};
    var url = decodeURI(location.search);
    // url中存在问号，也就说有参数
    if (url.indexOf("?") != -1){
      var str = url.substr(1);
      var strArr = str.split("&");
      for (var i = 0; i < strArr.length; i++) {
        urlData[strArr[i].split("=")[0]] = strArr[i].split("=")[1]
      }
    }
    return urlData
  }
  