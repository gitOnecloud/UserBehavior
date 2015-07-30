var tall_trafficData, tall_pageData, tall_trafficUpData;
/**
 * 初始化数据
 */
function tall_initData() {
	var tall_trafficall = tall_data.trafficall;
	var tall_pageviewall = tall_data.pageviewall;
	var tall_trafficUp = new Array();
	tall_data.trafficupall = tall_trafficUp;
	tall_data.trafficallHash = {};
	tall_data.pageviewHash = {};
	tall_data.trafficUpCount = 0;
	tall_data.trafficDownCount = 0;
	tall_data.pageViewCount = 0;
	tall_data.trafficunit = $('#tall_trafficunit').combobox('getValue');
	tall_data.pageunit = $('#tall_pageunit').combobox('getValue');
	$.each(tall_trafficall, function(k, v) {//建立oaid和traffic的哈希表
		tall_data.trafficallHash[v.oaid] = {
				'request_traffic': v.request_traffic,
				'response_traffic': v.response_traffic
			};
		tall_data.trafficUpCount += v.request_traffic;
		tall_data.trafficDownCount += v.response_traffic;
		tall_trafficUp.push(v);
	});
	//冒泡排序上行流量
	index_sortData(tall_trafficUp, 'request_traffic');
	$.each(tall_pageviewall, function(k, v) {//建立oaid和amount的哈希表
		tall_data.pageviewHash[v.oaid] = v.amount;
		tall_data.pageViewCount += v.amount;
	});
	tall_trafficData = [[],[],[],[]];
	initTrafficData(tall_trafficall, tall_trafficData);
	tall_trafficUpData = [[],[],[],[]];
	initTrafficData(tall_trafficUp, tall_trafficUpData);
	tall_pageData = [[],[],[],[]];
	for(var i=0; i<20 && i<tall_pageviewall.length; i++) {//按浏览数排序
		tall_pageData[0].push(tall_pageviewall[i].appname);
		var showOaid = '应用：' + tall_pageviewall[i].appname + '<br/>用户：' +
			tall_pageviewall[i].user + '<br/>' +
			tall_pageviewall[i].oaid + '<br/>' +
			tall_pageviewall[i].domain;
		var traffic = tall_data.trafficallHash[tall_pageviewall[i].oaid];
		var req=0,res=0;
		if(traffic != null) {
			req = (traffic.request_traffic/tall_data.trafficunit).toFixed(1)*1;//乘以1是要转换为数字
			res = (traffic.response_traffic/tall_data.trafficunit).toFixed(1)*1;
		}
		tall_pageData[1].push([showOaid, req]);
		tall_pageData[2].push([showOaid, res]);
		tall_pageData[3].push([showOaid, (tall_pageviewall[i].amount/tall_data.pageunit).toFixed(1)*1]);
	}
}
/**
 * 初始化流量排行数据
 */
function initTrafficData(trafficall, trafficData) {
	for(var i=0; i<20 && i<trafficall.length; i++) {//按下行流量排序
		trafficData[0].push(trafficall[i].appname);
		var showOaid = '应用：' + trafficall[i].appname + '<br/>用户：' +
			trafficall[i].user + '<br/>' +
			trafficall[i].oaid + '<br/>' +
			trafficall[i].domain;
		trafficData[1].push([showOaid, (trafficall[i].request_traffic/tall_data.trafficunit).toFixed(1)*1]);//乘以1是要转换为数字
		trafficData[2].push([showOaid, (trafficall[i].response_traffic/tall_data.trafficunit).toFixed(1)*1]);
		var amount = tall_data.pageviewHash[trafficall[i].oaid];
		if(amount == null) {
			amount = 0;
		} else {
			amount = (amount/tall_data.pageunit).toFixed(1)*1;
		}
		trafficData[3].push([showOaid, amount]);
	}
}
//初始化数据
tall_initData();
//日期选择框
$('#tall_date').datebox({
	onSelect: function(date){
		tall_changeData(date);
	},
});
/**
 * 更新数据
 */
function tall_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	if($('#tall_datesort').combobox('getValue') == '1') {//按天显示
		dateStr += '-';
		if(date.getDate() < 10) {
			dateStr += '0';
		}
		dateStr += date.getDate();
	}
	tall_getData(dateStr);
}
/**
 * 读取数据
 */
function tall_getData(dateStr) {
	index_mess('读取中...', 0);
	$.getJSON('trafficall_list_js.action', 'page.date=' + dateStr, function(data) {
		index_mess('读取成功！', 2);
		tall_data = data;
		tall_initData();
		tall_sortData($('#tall_sort').combobox('getValue'));
		tall_setShowDate(tall_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function tall_setShowDate(date) {
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	$('#tall_date').datebox('setValue', date);
}
tall_setShowDate(tall_data.date);
//排序选择框
$('#tall_sort').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		tall_sortData(record.value);
	},
});
//时间范围选择框
$('#tall_datesort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		var date = $('#tall_date').datebox('getValue');
		if(record.value == '0') {//按月显示
			date = date.substring(0, 7);
			$('#tall_pre').linkbutton({text:'上一月'});
			$('#tall_next').linkbutton({text:'下一月'});
		} else {
			$('#tall_pre').linkbutton({text:'上一天'});
			$('#tall_next').linkbutton({text:'下一天'});
		}
		tall_getData(date);
	},
});
/**
 * 选择排序数据，然后显示
 */
function tall_sortData(sort) {
	switch(sort) {
		case '0'://按上行流量
			tall_showData(tall_trafficUpData);
			break;
		case '1'://按下行流量
			tall_showData(tall_trafficData);
			break;
		case '2'://按浏览数
			tall_showData(tall_pageData);
			break;
	}
}
/**
 * 按排序好的数据，更新图表
 */
function tall_showData(data) {
	var chart = $('#tall_container').highcharts();
	chart.setTitle({
			text: tall_showTitle()
		},{
			text: tall_showSubTile()
		});
	chart.xAxis[0].setCategories(data[0]);
    chart.series[0].setData(data[1]);
    chart.series[1].setData(data[2]);
    chart.series[2].setData(data[3]);
}
/**
 * 上/下一月/天 切换
 */
function tall_prenext(flag) {
	var date = new Date($('#tall_date').datebox('getValue'));
	if($('#tall_datesort').combobox('getValue') == '1') {//按天显示
		date.setTime(date.getTime() + 1000*60*60*24*flag);
	} else {
		date.setMonth(date.getMonth() + flag); 
	}
	tall_changeData(date);
}
//流量单位选择框
$('#tall_trafficunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		var chart = $('#tall_container').highcharts();
		chart.yAxis[0].setTitle({
			text: '流量 (' + record.text + ')'
		});
		chart.series[0].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		chart.series[1].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		tall_initData();
		tall_sortData($('#tall_sort').combobox('getValue'));
	},
});
//浏览数单位选择框
$('#tall_pageunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		var chart = $('#tall_container').highcharts();
		chart.yAxis[1].setTitle({
			text: '浏览数 (' + record.text + ')'
		});
		chart.series[2].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		tall_initData();
		tall_sortData($('#tall_sort').combobox('getValue'));
	},
});
/**
 * 设置主标题
 */
function tall_showTitle() {
	return tall_data.date + ' ' + $('#tall_sort').combobox('getText') +'排行';
}
/**
 * 设置副标题
 */
function tall_showSubTile() {
	return '上行' + index_formatTraffic(tall_data.trafficUpCount) +
		' 下行' + index_formatTraffic(tall_data.trafficDownCount) +
		' 浏览' + index_formatPage(tall_data.pageViewCount);
}

//图表
$('#tall_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: tall_showTitle()
    },
    subtitle: {
    	text: tall_showSubTile()
    },
    xAxis: {
        categories: tall_trafficData[0],
        labels: {
            rotation: -36,
            align: 'right',
            style: {
            	color: '#333',
                fontSize: '13px',
                fontWeight: 'bold'
            }
        }
    },
    yAxis: [{
    	min: 0,
        title: {
            text: '流量 (' + $('#tall_trafficunit').combobox('getText') + ')'
        },
    }, {
    	min: 0,
    	title: {
            text: '浏览数 (' + $('#tall_pageunit').combobox('getText') + ')',
        },
        opposite: true,
    }],
    tooltip: {
        /*formatter: function() {
        	var s = '<span style="font-size:12px">' + this.points[0].key + '</span><table>';
        	$.each(this.points, function(i, point) {
        		s += '<tr><td style="color:' + point.series.color + ';padding:0">' +
        			point.series.name + ': </td>' +
        			'<td style="padding:0"><b>'+ point.y + ' GB</b></td></tr>';
        	});
        	return s + '</table>';
        },*/
    	headerFormat: '<span style="font-size: 12px">{point.key}</span><br/>',
        shared: true,
        useHTML: true
    },
    plotOptions: {
    	column: {
            dataLabels: {
                enabled: true
            }
        }
    },
    series: [{
        name: '上行',
        data: tall_trafficData[1],
        tooltip: {
            valueSuffix: ' ' + $('#tall_trafficunit').combobox('getText')
        }
    }, {
        name: '下行',
        data: tall_trafficData[2],
        tooltip: {
            valueSuffix: ' ' + $('#tall_trafficunit').combobox('getText')
        }
    },{
        name: '浏览数',
        type: 'spline',
        data: tall_trafficData[3],
        yAxis: 1,
        tooltip: {
            valueSuffix: ' ' + $('#tall_pageunit').combobox('getText')
        }
    }]
});
/**
 * 显示原始数据
 */
function tall_showdata() {
	$('#tall_datawindow').window('open');
	var str = '<h2>' + tall_showTitle() + '</h2>' +
		'<h3>' + tall_showSubTile() + '</h3>' +
		'<table width="100%"><tr><td>应用</td><td>用户</td><td>oaid</td><td>域名</td><td>上行流量</td><td>下行流量</td><td>浏览数</td></tr>';
	switch($('#tall_sort').combobox('getValue')) {
		case '0'://按上行流量
			str += tall_createTrafficData(tall_data.trafficupall);
			break;
		case '1'://按下行流量
			str += tall_createTrafficData(tall_data.trafficall);
			break;
		case '2'://按浏览数
			$.each(tall_data.pageviewall, function(k, v) {
				var traffic = tall_data.trafficallHash[v.oaid];
				var req=0, res=0;
				if(traffic != null) {
					req = traffic.request_traffic;
					res = traffic.response_traffic;
				}
				str += '<tr><td>' +
					v.appname + '</td><td>' +
					v.user + '</td><td>' +
					v.oaid + '</td><td>' +
					v.domain + '</td><td>' +
					index_formatTraffic(req) + '</td><td>' +
					index_formatTraffic(res) + '</td><td>' +
					index_formatPage(v.amount)+ '</td></tr>';
			});
			break;
	}
	str += '</table>';
	$('#tall_data').html(str);
}
/**
 * 生成流量数据
 */
function tall_createTrafficData(trafficData) {
	var str = '';
	$.each(trafficData, function(k, v) {
		var amount = tall_data.pageviewHash[v.oaid];
		if(amount == null) {
			amount = 0;
		}
		str += '<tr><td>' +
			v.appname + '</td><td>' +
			v.user + '</td><td>' +
			v.oaid + '</td><td>' +
			v.domain + '</td><td>' +
			index_formatTraffic(v.request_traffic) + '</td><td>' +
			index_formatTraffic(v.response_traffic) + '</td><td>' +
			index_formatPage(amount)+ '</td></tr>';
	});
	return str;
}
