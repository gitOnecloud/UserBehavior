var tsum_trafficData = [[],[],[],[]];
/**
 * 初始化数据
 */
function tsum_initData() {
	tsum_data.trafficUpCount = 0;
	tsum_data.trafficDownCount = 0;
	tsum_data.pageViewCount = 0;
	tsum_data.trafficunit = $('#tsum_trafficunit').combobox('getValue');
	tsum_data.pageunit = $('#tsum_pageunit').combobox('getValue');
	tsum_trafficData = [[],[],[],[]];
	$.each(tsum_data.trafficpage, function(k, v){//按浏览数排序
		tsum_data.trafficUpCount += v.request_traffic;
		tsum_data.trafficDownCount += v.response_traffic;
		tsum_data.pageViewCount += v.amount;
		tsum_trafficData[0].push(v.date);
		tsum_trafficData[1].push((v.request_traffic/tsum_data.trafficunit).toFixed(1)*1);
		tsum_trafficData[2].push((v.response_traffic/tsum_data.trafficunit).toFixed(1)*1);
		tsum_trafficData[3].push((v.amount/tsum_data.pageunit).toFixed(1)*1);
	});
}

//初始化数据
tsum_initData();
//日期选择框
$('#tsum_date').datebox({
	onSelect: function(date){
		tsum_changeData(date);
	},
});
/**
 * 日期输入框选择后更新数据
 */
function tsum_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	switch($('#tsum_datesort').combobox('getValue')) {
		case '1' ://按天
			dateStr += '-';
			if(date.getDate() < 10) {
				dateStr += '0';
			}
			dateStr += date.getDate();
			break;
		case '2' ://按年
			dateStr = date.getFullYear();
			break;
	}
	tsum_getData(dateStr);
}
/**
 * 读取数据
 */
function tsum_getData(dateStr) {
	var params = {};
	params["page.date"] = dateStr;
	index_mess('读取中...', 0);
	$.getJSON('trafficall_sum_js.action', params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		tsum_data = data;
		tsum_initData();
		tsum_updateData();
		tsum_setShowDate(tsum_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function tsum_setShowDate(date) {
	if(tsum_data.status == 1) {
		return ;
	}
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	if(date.length == 4) {//只有年，日期置为今天
		var today = new Date();
		today.getMonth();
		date += '-';
		if(today.getMonth() < 9) {
			date += '0';
		}
		date += (today.getMonth()+1) + '-';
		if(today.getDate() < 10) {
			date += '0';
		}
		date += today.getDate();
	}
	$('#tsum_date').datebox('setValue', date);
}
tsum_setShowDate(tsum_data.date);
//时间范围选择框
$('#tsum_datesort').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		var date = $('#tsum_date').datebox('getValue');
		switch(record.value) {
			case '1' :
				$('#tsum_pre').linkbutton({text:'上一天'});
				$('#tsum_next').linkbutton({text:'下一天'});
				break;
			case '2' :
				date = date.substring(0, 4);
				$('#tsum_pre').linkbutton({text:'上一年'});
				$('#tsum_next').linkbutton({text:'下一年'});
				break;
			default:
				date = date.substring(0, 7);
				$('#tsum_pre').linkbutton({text:'上一月'});
				$('#tsum_next').linkbutton({text:'下一月'});
				
		}
		tsum_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function tsum_prenext(flag) {
	var date = new Date($('#tsum_date').datebox('getValue'));
	switch($('#tsum_datesort').combobox('getValue')) {
		case '1' ://按天
			date.setTime(date.getTime() + 1000*60*60*24*flag);
			break;
		case '2' ://按年
			date.setFullYear(date.getFullYear() + flag,
					date.getMonth(), date.getDay());
			break;
		default ://按月
			date.setMonth(date.getMonth() + flag); 
	}
	tsum_changeData(date);
}
//流量单位选择框
$('#tsum_trafficunit').combobox({
	panelHeight: 100,
	editable: false,
	onSelect: function(record){
		if(tsum_data.status == 1) {
			index_mess(tsum_data.mess, 3);
			return ;
		}
		var chart = $('#tsum_container').highcharts();
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
		tsum_initData();
		tsum_updateData();
	},
});
//浏览数单位选择框
$('#tsum_pageunit').combobox({
	panelHeight: 100,
	editable: false,
	onSelect: function(record){
		if(tsum_data.status == 1) {
			index_mess(tsum_data.mess, 3);
			return ;
		}
		var chart = $('#tsum_container').highcharts();
		chart.yAxis[1].setTitle({
			text: '浏览数 (' + record.text + ')'
		});
		chart.series[2].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		tsum_initData();
		tsum_updateData();
	},
});
/**
 * 更新图表
 */
function tsum_updateData() {
	var chart = $('#tsum_container').highcharts();
	chart.setTitle({
			text: tsum_showTitle()
		},{
			text: tsum_showSubTile()
		});
	chart.xAxis[0].setCategories(tsum_trafficData[0]);
    chart.series[0].setData(tsum_trafficData[1]);
    chart.series[1].setData(tsum_trafficData[2]);
    chart.series[2].setData(tsum_trafficData[3]);
}
/**
 * 标题
 */
function tsum_showTitle() {
	if(tsum_data.status == 1) {
		return tsum_data.mess;
	}
	return tsum_data.date + ' 总流量和浏览数';
}
/**
 * 小标题
 */
function tsum_showSubTile() {
	if(tsum_data.status == 1) {
		return tsum_data.mess;
	}
	return '上行' + index_formatTraffic(tsum_data.trafficUpCount) +
	' 下行' + index_formatTraffic(tsum_data.trafficDownCount) +
	' 浏览' + index_formatPage(tsum_data.pageViewCount);
}
//图表
$('#tsum_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: tsum_showTitle()
    },
    subtitle: {
    	text: tsum_showSubTile()
    },
    xAxis: {
        categories: tsum_trafficData[0],
    },
    yAxis: [{
    	min: 0,
        title: {
            text: '流量 (' + $('#tsum_trafficunit').combobox('getText') + ')'
        },
    }, {
    	min: 0,
    	title: {
            text: '浏览数 (' + $('#tsum_pageunit').combobox('getText') + ')',
        },
        opposite: true,
    }],
    tooltip: {
    	headerFormat: '<span style="font-size: 12px">{point.key}</span><br/>',
        shared: true,
        useHTML: true
    },
    /*plotOptions: {
    	column: {
            dataLabels: {
                enabled: true
            }
        }
    },*/
    series: [{
        name: '上行',
        data: tsum_trafficData[1],
        tooltip: {
            valueSuffix: ' ' + $('#tsum_trafficunit').combobox('getText')
        }
    }, {
        name: '下行',
        data: tsum_trafficData[2],
        tooltip: {
            valueSuffix: ' ' + $('#tsum_trafficunit').combobox('getText')
        }
    },{
        name: '浏览数',
        type: 'spline',
        data: tsum_trafficData[3],
        yAxis: 1,
        tooltip: {
            valueSuffix: ' ' + $('#tsum_pageunit').combobox('getText')
        }
    }]
});
/**
 * 显示报表
 */
function tsum_showdata() {
	$('#tsum_datawindow').window('open');
	var str = '<h2>' + tsum_showTitle() + '</h2>' +
		'<h3>' + tsum_showSubTile() + '</h3>' +
		'<table width="100%">' +
		'<tr><td>时间</td><td>上行流量</td><td>下行流量</td><td>浏览数</td></tr>';
	
	$.each(tsum_data.trafficpage, function(k, v) {
		str += '<tr><td>' +
			v.date + '</td><td>' +
			index_formatTraffic(v.request_traffic) + '</td><td>' +
			index_formatTraffic(v.response_traffic) + '</td><td>' +
			index_formatPage(v.amount)+ '</td></tr>';
	});
	str += '</table>';
	$('#tsum_data').html(str);
}
/**
 * 搜索应用
 */
function tsum_searchApp() {
	tsum_changeData(new Date($('#tsum_date').datebox('getValue')));
}
/**
 * 清除表单数据
 */
function tsum_clearForm() {
	$('#tsum_aliases').val('');
	$('#tsum_domain').val('');
	$('#tsum_oaid').val('');
}