var isp_ipviewData = [[],[],[]],
isp_byIpData = [[],[],[]];
/**
 * 初始化数据
 */
function isp_initData() {
	if(isp_data.status == 1) {
		$('#isp_appdata').html(isp_data.mess);
		return ;
	}
	$('#isp_appdata').html('云架构：' + isp_data.appname +
			'&emsp;用户：' + isp_data.user +
			'&emsp;OAID：' + isp_data.oaid +
			'<br />一级域名：' + isp_data.aliases +
			'<br />二级域名：' + isp_data.domain);
	isp_data.amount = 0;
	isp_data.ipCount = 0;
	isp_data.otherAmount = 0;
	isp_data.otherIpCount = 0;
	isp_data.viewunit = $('#isp_viewunit').combobox('getValue');
	isp_data.ipunit = $('#isp_ipunit').combobox('getValue');
	var ipviewData = new Array();
	var orderByIp = new Array();//按ip排序的全部数据
	isp_data.orderByIp = orderByIp;
	$.each(isp_data.ipview, function(k, v){//按浏览数排序
		orderByIp.push(v);
		isp_data.amount += v.amount;
		isp_data.ipCount += v.ipsum;
		if(ipviewData.length < 20) {
			if(v.isp==null || v.isp=='') {
				isp_data.otherAmount += v.amount;
				isp_data.otherIpCount += v.ipsum;
			} else {
				ipviewData.push({
					'isp': v.isp,
					'amount': (v.amount/isp_data.viewunit).toFixed(1)*1,
					'ipsum': (v.ipsum/isp_data.ipunit).toFixed(1)*1
				});
			}
		}
	});
	if(isp_data.otherAmount != 0) {
		ipviewData.push({
			'isp': '其他',
			'amount': (isp_data.otherAmount/isp_data.viewunit).toFixed(1)*1,
			'ipsum': (isp_data.otherIpCount/isp_data.ipunit).toFixed(1)*1
		});
	}
	//冒泡排序浏览数
	index_sortData(ipviewData, 'amount');
	//添加进图标数据
	isp_ipviewData = [[],[],[]];
	$.each(ipviewData, function(k, v) {
		isp_ipviewData[0].push(v.isp);
		isp_ipviewData[1].push(v.ipsum);
		isp_ipviewData[2].push(v.amount);
	});
	//冒泡排序全部ip数
	index_sortData(orderByIp, 'ipsum');
	//取前二十个按ip数排序的数据，其余的加到'其他'这一项
	ipviewData = new Array();
	isp_data.otherAmount = 0;
	isp_data.otherIpCount = 0;
	$.each(orderByIp, function(k, v) {
		if(ipviewData.length < 20) {
			if(v.isp==null || v.isp=='') {
				isp_data.otherAmount += v.amount;
				isp_data.otherIpCount += v.ipsum;
			} else {
				ipviewData.push({
					'isp': v.isp,
					'amount': (v.amount/isp_data.viewunit).toFixed(1)*1,
					'ipsum': (v.ipsum/isp_data.ipunit).toFixed(1)*1
				});
			}
		}
	});
	if(isp_data.otherAmount != 0) {
		ipviewData.push({
			'isp': '其他',
			'amount': (isp_data.otherAmount/isp_data.viewunit).toFixed(1)*1,
			'ipsum': (isp_data.otherIpCount/isp_data.ipunit).toFixed(1)*1
		});
	}
	//冒泡排序ip数
	index_sortData(ipviewData, 'ipsum');
	//添加进图表数据
	isp_byIpData = [[],[],[]];
	$.each(ipviewData, function(k, v) {
		isp_byIpData[0].push(v.isp);
		isp_byIpData[1].push(v.ipsum);
		isp_byIpData[2].push(v.amount);
	});
}

//初始化数据
isp_initData();
//按下enter键搜索
$('#isp_aliases').keydown(function(e) {
	if(e.keyCode==13) {
		isp_searchApp();
	}
});
$('#isp_domain').keydown(function(e) {
	if(e.keyCode==13) {
		isp_searchApp();
	}
});
$('#isp_oaid').keydown(function(e) {
	if(e.keyCode==13) {
		isp_searchApp();
	}
});
//排序选择框
$('#isp_sort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		isp_updateData();
	},
});
//日期选择框
$('#isp_date').datebox({
	onSelect: function(date){
		isp_changeData(date);
	},
});
/**
 * 更新数据
 */
function isp_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	if($('#isp_datesort').combobox('getValue') == '1') {//按天显示
		dateStr += '-';
		if(date.getDate() < 10) {
			dateStr += '0';
		}
		dateStr += date.getDate();
	}
	isp_getData(dateStr);
}
/**
 * 读取数据
 */
function isp_getData(dateStr) {
	var params = {};
	params["page.date"] = dateStr;
	var str = $('#isp_aliases').val();
	if(str != '') {
		params["page.aliases"] = str;
	}
	str = $('#isp_domain').val();
	if(str != '') {
		params["page.domain"] = str;
	}
	str = $('#isp_oaid').val();
	if(str != '') {
		params["page.oaid"] = str;
	}
	index_mess('读取中...', 0);
	$.getJSON('isp_list_js.action', params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		isp_data = data;
		isp_initData();
		isp_updateData();
		isp_setShowDate(isp_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function isp_setShowDate(date) {
	if(isp_data.status == 1) {
		return ;
	}
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	$('#isp_date').datebox('setValue', date);
}
isp_setShowDate(isp_data.date);
//时间范围选择框
$('#isp_datesort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		var date = $('#isp_date').datebox('getValue');
		if(record.value == '0') {//按月显示
			date = date.substring(0, 7);
			$('#isp_pre').linkbutton({text:'上一月'});
			$('#isp_next').linkbutton({text:'下一月'});
		} else {
			$('#isp_pre').linkbutton({text:'上一天'});
			$('#isp_next').linkbutton({text:'下一天'});
		}
		isp_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function isp_prenext(flag) {
	var date = new Date($('#isp_date').datebox('getValue'));
	if($('#isp_datesort').combobox('getValue') == '1') {//按天显示
		date.setTime(date.getTime() + 1000*60*60*24*flag);
	} else {
		date.setMonth(date.getMonth() + flag); 
	}
	isp_changeData(date);
}
//浏览数单位选择框
$('#isp_viewunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(isp_data.status == 1) {
			index_mess(isp_data.mess, 3);
			return ;
		}
		var chart = $('#isp_container').highcharts();
		chart.yAxis[1].setTitle({
			text: '浏览数 (' + record.text + ')'
		});
		chart.series[1].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		isp_initData();
		isp_updateData();
	},
});
//ip数单位选择框
$('#isp_ipunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(isp_data.status == 1) {
			index_mess(isp_data.mess, 3);
			return ;
		}
		var chart = $('#isp_container').highcharts();
		chart.yAxis[0].setTitle({
			text: 'ip数 (' + record.text + ')'
		});
		chart.series[0].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		isp_initData();
		isp_updateData();
	},
});
/**
 * 更新图表
 */
function isp_updateData() {
	var ipviewData = null;
	switch($('#isp_sort').combobox('getValue')) {
		case '0'://按ip数
			ipviewData = isp_byIpData;
			break;
		case '1'://按浏览数
			ipviewData = isp_ipviewData;
			break;
	}
	var chart = $('#isp_container').highcharts();
	chart.setTitle({
			text: isp_showTitle()
		},{
			text: isp_showSubTile()
		});
	chart.xAxis[0].setCategories(ipviewData[0]);
    chart.series[0].setData(ipviewData[1]);
    chart.series[1].setData(ipviewData[2]);
}
/**
 * 标题
 */
function isp_showTitle() {
	if(isp_data.status == 1) {
		return isp_data.mess;
	}
	return isp_data.date + ' 各运营商IP访问量';
}
/**
 * 小标题
 */
function isp_showSubTile() {
	if(isp_data.status == 1) {
		return isp_data.mess;
	}
	return 'ip' + index_formatNum(isp_data.ipCount) +
	' 浏览' + index_formatPage(isp_data.amount);
}
//图表
$('#isp_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: isp_showTitle()
    },
    subtitle: {
    	text: isp_showSubTile()
    },
    xAxis: {
        categories: isp_byIpData[0],
        labels: {
            rotation: -32,
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
            text: 'ip数 (' + $('#isp_ipunit').combobox('getText') +')'
        },
    }, {
    	min: 0,
        title: {
            text: '浏览数 (' + $('#isp_viewunit').combobox('getText') +')'
        },
        opposite: true,
    }],
    tooltip: {
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
        name: 'ip数',
        data: isp_byIpData[1],
        tooltip: {
            valueSuffix: ' ' + $('#isp_ipunit').combobox('getText')
        }
    }, {
        name: '浏览数',
        type: 'spline',
        yAxis: 1,
        data: isp_byIpData[2],
        tooltip: {
            valueSuffix: ' ' + $('#isp_viewunit').combobox('getText')
        }
    }]
});
/**
 * 显示原始数据
 */
function isp_showdata() {
	$('#isp_datawindow').window('open');
	var str = '<h2>' + isp_showTitle() +
		'</h2><h3>云架构：' + isp_data.appname +
		'&emsp;用户：' + isp_data.user +
		'&emsp;OAID：' + isp_data.oaid +
		'<br />一级域名：' + isp_data.aliases +
		'<br />二级域名：' + isp_data.domain +
		'<br />' + isp_showSubTile() +
		'</h3><table width="100%"><tr><td>运营商</td><td>IP数</td><td>浏览数</td></tr>';
	switch($('#isp_sort').combobox('getValue')) {
		case '0'://按ip数
			str += isp_createDate(isp_data.orderByIp);
			break;
		case '1'://按浏览数
			str += isp_createDate(isp_data.ipview);
			break;
	}
	str += '</table>';
	$('#isp_data').html(str);
}
/**
 * 生成原始数据
 */
function isp_createDate(ipview) {
	var str = '';
	$.each(ipview, function(k, v) {
		str += '<tr><td>' +
			v.isp + '</td><td>' +
			index_formatNum(v.ipsum) + '</td><td>' +
			index_formatPage(v.amount)+ '</td></tr>';
	});
	return str;
}
/**
 * 搜索应用
 */
function isp_searchApp() {
	isp_changeData(new Date($('#isp_date').datebox('getValue')));
}
/**
 * 清除表单数据
 */
function isp_clearForm() {
	$('#isp_aliases').val('');
	$('#isp_domain').val('');
	$('#isp_oaid').val('');
}