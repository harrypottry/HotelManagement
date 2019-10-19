function isWhichDay(num){
	switch (num){
		case 0: 
			return '今天';
		case 1: 
			return '昨天';
		case 2: 
			return '前天';
		default: return '三天前'
	}
}
