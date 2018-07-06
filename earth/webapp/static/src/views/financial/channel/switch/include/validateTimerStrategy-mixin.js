function toSeconds(timeObj){
	if(typeof timeObj == 'object' && !Array.isArray(timeObj)){
		return (+timeObj.HH)*60*60 + (+timeObj.mm)*60 + (+timeObj.ss)
	}
	return timeObj
}
export default{
	methods: {
		validateTimerStrategyItems: function(everydayItems, timer){
			var everydayItemsTrans = everydayItems.map(i =>{
				var obj = {}
				for (var k of Object.keys(i)){
					obj[k] = toSeconds(i[k])
				}
				return obj
			})
			var acrossAday = everydayItemsTrans.findIndex(e => {
				return e.timerBeginValue > e.timerEndValue
			})
			if(acrossAday != -1){
				var acrossAdayEle = everydayItemsTrans[acrossAday]
				everydayItemsTrans.splice(acrossAday, 1, {
						strategy: acrossAdayEle.strategy, 
						timerBeginValue:acrossAdayEle.timerBeginValue, 
						timerEndValue: 86399
					}, {
						strategy: acrossAdayEle.strategy, 
						timerBeginValue: 0, 
						timerEndValue: acrossAdayEle.timerEndValue
					})
			}

			everydayItemsTrans.sort((a, b) =>{
				return a.timerBeginValue > b.timerBeginValue
			})

			var overlap = false;
			for(let i =0 , len = everydayItemsTrans.length; i< len-1 ; i++){
				if(everydayItemsTrans[i+1].timerBeginValue <= everydayItemsTrans[i].timerEndValue){
					overlap = true;
					break;
				}
			}
			var alltime = everydayItemsTrans.reduce((sum, item)=>{
				return sum += (item.timerEndValue - item.timerBeginValue + 1)
			},0) == 86400
			
			//overlap: true => 时间段有重叠
			//alltime: true -> 时间段累计24小时
			return {
				overlap: overlap,
				alltime: alltime,
				timer: timer
			}
		}
	}
}