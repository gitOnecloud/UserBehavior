var tsearch_trafficData = [[],[],[],[],[],[]];
/**
 * 初始化数据
 */
function tsearch_initData() {
	if(tsearch_data.status == 1) {
		$('#tsearch_appdata').html(tsearch_data.mess);
		return ;
	}
	$('#tsearch_appdata').html('云架构：' + tsearch_data.appname +
			'&emsp;用户：' + tsearch_data.user +
			'&emsp;OAID：' + tsearch_data.oaid +
			'<br />一级域名：' + tsearch_data.aliases +
			'<br />二级域名：' + tsearch_data.domain);
	tsearch_data.trafficUpCount = 0;
	tsearch_data.trafficDownCount = 0;
	tsearch_data.pageViewCount = 0;
	tsearch_data.trafficunit = $('#tsearch_trafficunit').combobox('getValue');
	tsearch_data.pageunit = $('#tsearch_pageunit').combobox('getValue');
	tsearch_trafficData = [[],[],[],[],[],[]];
	$.each(tsearch_data.trafficpage, function(k, v){//按浏览数排序
		tsearch_data.trafficUpCount += v.request_traffic;
		tsearch_data.trafficDownCount += v.response_traffic;
		tsearch_data.pageViewCount += v.amount;
		tsearch_trafficData[0].push(v.date);
		tsearch_trafficData[1].push((v.request_traffic/tsearch_data.trafficunit).toFixed(1)*1);
		tsearch_trafficData[2].push((v.response_traffic/tsearch_data.trafficunit).toFixed(1)*1);
		tsearch_trafficData[3].push((v.amount/tsearch_data.pageunit).toFixed(1)*1);
		tsearch_trafficData[4].push(v.uploadBandWidth);
		tsearch_trafficData[5].push(v.downBandWidth);
	});
}

//初始化数据
tsearch_initData();
//按下enter键搜索
$('#tsearch_aliases').keydown(function(e) {
	if(e.keyCode==13) {
		tsearch_searchApp();
	}
});
$('#tsearch_domain').keydown(function(e) {
	if(e.keyCode==13) {
		tsearch_searchApp();
	}
});
$('#tsearch_oaid').keydown(function(e) {
	if(e.keyCode==13) {
		tsearch_searchApp();
	}
});
//日期选择框
$('#tsearch_date').datebox({
	onSelect: function(date){
		tsearch_changeData(date);
	},
});
/**
 * 日期输入框选择后更新数据
 */
function tsearch_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	switch($('#tsearch_datesort').combobox('getValue')) {
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
	tsearch_getData(dateStr);
}
/**
 * 读取数据
 */
function tsearch_getData(dateStr) {
	var params = {};
	params["page.date"] = dateStr;
	var str = $('#tsearch_aliases').val();
	if(str != '') {
		params["page.aliases"] = str;
	}
	str = $('#tsearch_domain').val();
	if(str != '') {
		params["page.domain"] = str;
	}
	str = $('#tsearch_oaid').val();
	if(str != '') {
		params["page.oaid"] = str;
	}
	index_mess('读取中...', 0);
	$.getJSON('trafficall_search_js.action', params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		tsearch_data = data;
		tsearch_initData();
		tsearch_updateData();
		tsearch_setShowDate(tsearch_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function tsearch_setShowDate(date) {
	if(tsearch_data.status == 1) {
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
	$('#tsearch_date').datebox('setValue', date);
}
tsearch_setShowDate(tsearch_data.date);
//时间范围选择框
$('#tsearch_datesort').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		var date = $('#tsearch_date').datebox('getValue');
		switch(record.value) {
		case '1' :
			$('#tsearch_pre').linkbutton({text:'上一天'});
			$('#tsearch_next').linkbutton({text:'下一天'});
			break;
		case '2' :
			date = date.substring(0, 4);
			$('#tsearch_pre').linkbutton({text:'上一年'});
			$('#tsearch_next').linkbutton({text:'下一年'});
			break;
		default:
			date = date.substring(0, 7);
			$('#tsearch_pre').linkbutton({text:'上一月'});
			$('#tsearch_next').linkbutton({text:'下一月'});
			
	}
		tsearch_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function tsearch_prenext(flag) {
	var date = new Date($('#tsearch_date').datebox('getValue'));
	switch($('#tsearch_datesort').combobox('getValue')) {
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
	tsearch_changeData(date);
}
//流量单位选择框
$('#tsearch_trafficunit').combobox({
	panelHeight: 100,
	editable: false,
	onSelect: function(record){
		if(tsearch_data.status == 1) {
			index_mess(tsearch_data.mess, 3);
			return ;
		}
		var chart = $('#tsearch_container').highcharts();
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
		tsearch_initData();
		tsearch_updateData();
	},
});
//浏览数单位选择框
$('#tsearch_pageunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(tsearch_data.status == 1) {
			index_mess(tsearch_data.mess, 3);
			return ;
		}
		var chart = $('#tsearch_container').highcharts();
		chart.yAxis[1].setTitle({
			text: '浏览数 (' + record.text + ')'
		});
		chart.series[2].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		tsearch_initData();
		tsearch_updateData();
	},
});
/**
 * 更新图表
 */
function tsearch_updateData() {
	var chart = $('#tsearch_container').highcharts();
	chart.setTitle({
			text: tsearch_showTitle()
		},{
			text: tsearch_showSubTile()
		});
	chart.xAxis[0].setCategories(tsearch_trafficData[0]);
    chart.series[0].setData(tsearch_trafficData[1]);
    chart.series[1].setData(tsearch_trafficData[2]);
    chart.series[2].setData(tsearch_trafficData[3]);
    chart.series[3].setData(tsearch_trafficData[4]);
    chart.series[4].setData(tsearch_trafficData[5]);
}
/**
 * 标题
 */
function tsearch_showTitle() {
	if(tsearch_data.status == 1) {
		return tsearch_data.mess;
	}
	return tsearch_data.date + ' 流量和浏览数';
}
/**
 * 小标题
 */
function tsearch_showSubTile() {
	if(tsearch_data.status == 1) {
		return tsearch_data.mess;
	}
	return '上行' + index_formatTraffic(tsearch_data.trafficUpCount) +
	' 下行' + index_formatTraffic(tsearch_data.trafficDownCount) +
	' 浏览' + index_formatPage(tsearch_data.pageViewCount);
}
//图表
$('#tsearch_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: tsearch_showTitle()
    },
    subtitle: {
    	text: tsearch_showSubTile()
    },
    xAxis: {
        categories: tsearch_trafficData[0],
    },
    yAxis: [{
    	min: 0,
        title: {
            text: '流量 (' + $('#tsearch_trafficunit').combobox('getText') + ')'
        },
    }, {
    	min: 0,
    	title: {
            text: '浏览数 (' + $('#tsearch_pageunit').combobox('getText') + ')',
        },
        opposite: true,
    },{
    	min:0,
    	lineColor:'#019FE9',
        title:{
            text :'上行(Kbps)'
        },
        lineWidth : 1,
    },{ 
    	min:0,
        title:{
            text :'下行(Kbps)'
        },
        lineWidth : 1,
        opposite: true,
    }
    ],
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
        data: tsearch_trafficData[1],
        tooltip: {
            valueSuffix: ' ' + $('#tsearch_trafficunit').combobox('getText')
        }
    }, {
        name: '下行',
        data: tsearch_trafficData[2],
        tooltip: {
            valueSuffix: ' ' + $('#tsearch_trafficunit').combobox('getText')
        }
    },{
        name: '浏览数',
        type: 'spline',
        data: tsearch_trafficData[3],
        yAxis: 1,
        tooltip: {
            valueSuffix: ' ' + $('#tsearch_pageunit').combobox('getText')
        }
    },{
    	name:'上行带宽',
    	type:'spline',
    	data: tsearch_trafficData[4],
        yAxis: 2,
        tooltip: {
            valueSuffix: ' kbps' 
        },
        color: '#DDDF00'
    },{
    	name:'下行带宽',
    	type:'spline',
    	data: tsearch_trafficData[5],
        yAxis: 3,
        tooltip: {
            valueSuffix: ' Kbps' 
        },
        color:'#99FFCC'
    }
    ]
});
/**
 * 显示报表
 */
function tsearch_showdata() {
	$('#tsearch_datawindow').window('open');
	var str = '<h2>' + tsearch_showTitle() +
		'</h2><h3>云架构：' + tsearch_data.appname +
		'&emsp;用户：' + tsearch_data.user +
		'&emsp;OAID：' + tsearch_data.oaid +
		'<br />一级域名：' + tsearch_data.aliases +
		'<br />二级域名：' + tsearch_data.domain +
		'<br />' + tsearch_showSubTile() +
		'</h3><table width="100%"><tr><td>时间</td><td>上行流量</td><td>下行流量</td><td>浏览数</td></tr>';
	
	$.each(tsearch_data.trafficpage, function(k, v) {
		str += '<tr><td>' +
			v.date + '</td><td>' +
			index_formatTraffic(v.request_traffic) + '</td><td>' +
			index_formatTraffic(v.response_traffic) + '</td><td>' +
			index_formatPage(v.amount)+ '</td></tr>';
	});
	str += '</table>';
	$('#tsearch_data').html(str);
}
/**
 * 搜索应用
 */
function tsearch_searchApp() {
	tsearch_changeData(new Date($('#tsearch_date').datebox('getValue')));
}
/**
 * 清除表单数据
 */
function tsearch_clearForm() {
	$('#tsearch_aliases').val('');
	$('#tsearch_domain').val('');
	$('#tsearch_oaid').val('');
}