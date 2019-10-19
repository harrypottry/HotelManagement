function descToNum(desc){
	switch (desc[0].desc){
		case '床单': 
			console.log('床单');
			return 0;
			break;
		case '毛巾': 
			console.log('毛巾');
			return 1;
			break;
		case '浴巾': 
			console.log('浴巾');
			return 2;
			break;
		case '被罩': 
			console.log('被罩');
			return 3;
			break;
		case '枕套': 
			console.log('枕套');
			return 4;
			break;
		case '底垫': 
			console.log('底垫');
			return 5;
			break;
		default: console.log('此布草类型暂时不存在');
	}    
}
