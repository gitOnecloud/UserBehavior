var ipvl_ipviewData = [[],[],[]],
ipvl_byIpData = [[],[],[]];
/**
 * 初始化数据
 */
function ipvl_initData() {
	if(ipvl_data.status == 1) {
		$('#ipvl_appdata').html(ipvl_data.mess);
		return ;
	}
	$('#ipvl_appdata').html('云架构：' + ipvl_data.appname +
			'&emsp;用户：' + ipvl_data.user +
			'&emsp;OAID：' + ipvl_data.oaid +
			'<br />一级域名：' + ipvl_data.aliases +
			'<br />二级域名：' + ipvl_data.domain);
	ipvl_data.amount = 0;
	ipvl_data.ipCount = 0;
	ipvl_data.otherAmount = 0;
	ipvl_data.otherIpCount = 0;
	ipvl_data.viewunit = $('#ipvl_viewunit').combobox('getValue');
	ipvl_data.ipunit = $('#ipvl_ipunit').combobox('getValue');
	var ipviewData = new Array();
	var orderByIp = new Array();//按ip排序的全部数据
	ipvl_data.orderByIp = orderByIp;
	$.each(ipvl_data.ipview, function(k, v){//按浏览数排序
		orderByIp.push(v);
		ipvl_data.amount += v.amount;
		ipvl_data.ipCount += v.ipsum;
		if(ipviewData.length < 20) {
			if(v.province==null || v.province=='') {
				ipvl_data.otherAmount += v.amount;
				ipvl_data.otherIpCount += v.ipsum;
			} else {
				ipviewData.push({
					'province': v.province,
					'amount': (v.amount/ipvl_data.viewunit).toFixed(1)*1,
					'ipsum': (v.ipsum/ipvl_data.ipunit).toFixed(1)*1
				});
			}
		}
	});
	if(ipvl_data.otherAmount != 0) {
		ipviewData.push({
			'province': '其他',
			'amount': (ipvl_data.otherAmount/ipvl_data.viewunit).toFixed(1)*1,
			'ipsum': (ipvl_data.otherIpCount/ipvl_data.ipunit).toFixed(1)*1
		});
	}
	//冒泡排序浏览数
	index_sortData(ipviewData, 'amount');
	//添加进图标数据
	ipvl_ipviewData = [[],[],[]];
	$.each(ipviewData, function(k, v) {
		ipvl_ipviewData[0].push(v.province);
		ipvl_ipviewData[1].push(v.ipsum);
		ipvl_ipviewData[2].push(v.amount);
	});
	//冒泡排序全部ip数
	index_sortData(orderByIp, 'ipsum');
	//取前二十个按ip数排序的数据，其余的加到'其他'这一项
	ipviewData = new Array();
	ipvl_data.otherAmount = 0;
	ipvl_data.otherIpCount = 0;
	$.each(orderByIp, function(k, v) {
		if(ipviewData.length < 20) {
			if(v.province==null || v.province=='') {
				ipvl_data.otherAmount += v.amount;
				ipvl_data.otherIpCount += v.ipsum;
			} else {
				ipviewData.push({
					'province': v.province,
					'amount': (v.amount/ipvl_data.viewunit).toFixed(1)*1,
					'ipsum': (v.ipsum/ipvl_data.ipunit).toFixed(1)*1
				});
			}
		}
	});
	if(ipvl_data.otherAmount != 0) {
		ipviewData.push({
			'province': '其他',
			'amount': (ipvl_data.otherAmount/ipvl_data.viewunit).toFixed(1)*1,
			'ipsum': (ipvl_data.otherIpCount/ipvl_data.ipunit).toFixed(1)*1
		});
	}
	//冒泡排序ip数
	index_sortData(ipviewData, 'ipsum');
	//添加进图表数据
	ipvl_byIpData = [[],[],[]];
	$.each(ipviewData, function(k, v) {
		ipvl_byIpData[0].push(v.province);
		ipvl_byIpData[1].push(v.ipsum);
		ipvl_byIpData[2].push(v.amount);
	});
}

//初始化数据
ipvl_initData();
//按下enter键搜索
$('#ipvl_aliases').keydown(function(e) {
	if(e.keyCode==13) {
		ipvl_searchApp();
	}
});
$('#ipvl_domain').keydown(function(e) {
	if(e.keyCode==13) {
		ipvl_searchApp();
	}
});
$('#ipvl_oaid').keydown(function(e) {
	if(e.keyCode==13) {
		ipvl_searchApp();
	}
});
//排序选择框
$('#ipvl_sort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		ipvl_updateData();
	},
});
//日期选择框
$('#ipvl_date').datebox({
	onSelect: function(date){
		ipvl_changeData(date);
	},
});
/**
 * 更新数据
 */
function ipvl_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	if($('#ipvl_datesort').combobox('getValue') == '1') {//按天显示
		dateStr += '-';
		if(date.getDate() < 10) {
			dateStr += '0';
		}
		dateStr += date.getDate();
	}
	ipvl_getData(dateStr);
}
/**
 * 读取数据
 */
function ipvl_getData(dateStr) {
	var params = {};
	params["page.date"] = dateStr;
	var str = $('#ipvl_aliases').val();
	if(str != '') {
		params["page.aliases"] = str;
	}
	str = $('#ipvl_domain').val();
	if(str != '') {
		params["page.domain"] = str;
	}
	str = $('#ipvl_oaid').val();
	if(str != '') {
		params["page.oaid"] = str;
	}
	index_mess('读取中...', 0);
	$.getJSON('ipview_list_js.action', params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		ipvl_data = data;
		ipvl_initData();
		ipvl_updateData();
		ipvl_setShowDate(ipvl_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function ipvl_setShowDate(date) {
	if(ipvl_data.status == 1) {
		return ;
	}
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	$('#ipvl_date').datebox('setValue', date);
}
ipvl_setShowDate(ipvl_data.date);
//时间范围选择框
$('#ipvl_datesort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		var date = $('#ipvl_date').datebox('getValue');
		if(record.value == '0') {//按月显示
			date = date.substring(0, 7);
			$('#ipvl_pre').linkbutton({text:'上一月'});
			$('#ipvl_next').linkbutton({text:'下一月'});
		} else {
			$('#ipvl_pre').linkbutton({text:'上一天'});
			$('#ipvl_next').linkbutton({text:'下一天'});
		}
		ipvl_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function ipvl_prenext(flag) {
	var date = new Date($('#ipvl_date').datebox('getValue'));
	if($('#ipvl_datesort').combobox('getValue') == '1') {//按天显示
		date.setTime(date.getTime() + 1000*60*60*24*flag);
	} else {
		date.setMonth(date.getMonth() + flag); 
	}
	ipvl_changeData(date);
}
//浏览数单位选择框
$('#ipvl_viewunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(ipvl_data.status == 1) {
			index_mess(ipvl_data.mess, 3);
			return ;
		}
		var chart = $('#ipvl_container').highcharts();
		chart.yAxis[1].setTitle({
			text: '浏览数 (' + record.text + ')'
		});
		chart.series[1].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		ipvl_initData();
		ipvl_updateData();
	},
});
//ip数单位选择框
$('#ipvl_ipunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(ipvl_data.status == 1) {
			index_mess(ipvl_data.mess, 3);
			return ;
		}
		var chart = $('#ipvl_container').highcharts();
		chart.yAxis[0].setTitle({
			text: 'ip数 (' + record.text + ')'
		});
		chart.series[0].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		ipvl_initData();
		ipvl_updateData();
	},
});
/**
 * 更新图表
 */
function ipvl_updateData() {
	var ipviewData = null;
	switch($('#ipvl_sort').combobox('getValue')) {
		case '0'://按ip数
			ipviewData = ipvl_byIpData;
			break;
		case '1'://按浏览数
			ipviewData = ipvl_ipviewData;
			break;
	}
	var chart = $('#ipvl_container').highcharts();
	chart.setTitle({
			text: ipvl_showTitle()
		},{
			text: ipvl_showSubTile()
		});
	chart.xAxis[0].setCategories(ipviewData[0]);
    chart.series[0].setData(ipviewData[1]);
    chart.series[1].setData(ipviewData[2]);
}
/**
 * 标题
 */
function ipvl_showTitle() {
	if(ipvl_data.status == 1) {
		return ipvl_data.mess;
	}
	return ipvl_data.date + ' 各省IP访问量';
}
/**
 * 小标题
 */
function ipvl_showSubTile() {
	if(ipvl_data.status == 1) {
		return ipvl_data.mess;
	}
	return 'ip' + index_formatNum(ipvl_data.ipCount) +
	' 浏览' + index_formatPage(ipvl_data.amount);
}
//图表
$('#ipvl_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: ipvl_showTitle()
    },
    subtitle: {
    	text: ipvl_showSubTile()
    },
    xAxis: {
        categories: ipvl_byIpData[0],
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
            text: 'ip数 (' + $('#ipvl_ipunit').combobox('getText') +')'
        },
    }, {
    	min: 0,
        title: {
            text: '浏览数 (' + $('#ipvl_viewunit').combobox('getText') +')'
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
        data: ipvl_byIpData[1],
        tooltip: {
            valueSuffix: ' ' + $('#ipvl_ipunit').combobox('getText')
        }
    }, {
        name: '浏览数',
        data: ipvl_byIpData[2],
        type: 'spline',
        yAxis: 1,
        tooltip: {
            valueSuffix: ' ' + $('#ipvl_viewunit').combobox('getText')
        }
    }]
});
/**
 * 显示原始数据
 */
function ipvl_showdata() {
	$('#ipvl_datawindow').window('open');
	var str = '<h2>' + ipvl_showTitle() +
		'</h2><h3>云架构：' + ipvl_data.appname +
		'&emsp;用户：' + ipvl_data.user +
		'&emsp;OAID：' + ipvl_data.oaid +
		'<br />一级域名：' + ipvl_data.aliases +
		'<br />二级域名：' + ipvl_data.domain +
		'<br />' + ipvl_showSubTile() +
		'</h3><table width="100%"><tr><td>省份</td><td>IP数</td><td>浏览数</td></tr>';
	switch($('#ipvl_sort').combobox('getValue')) {
		case '0'://按ip数
			str += ipvl_createDate(ipvl_data.orderByIp);
			break;
		case '1'://按浏览数
			str += ipvl_createDate(ipvl_data.ipview);
			break;
	}
	str += '</table>';
	$('#ipvl_data').html(str);
}
/**
 * 生成原始数据
 */
function ipvl_createDate(ipview) {
	var str = '';
	$.each(ipview, function(k, v) {
		str += '<tr><td>' +
			v.province + '</td><td>' +
			index_formatNum(v.ipsum) + '</td><td>' +
			index_formatPage(v.amount)+ '</td></tr>';
	});
	return str;
}
/**
 * 搜索应用
 */
function ipvl_searchApp() {
	ipvl_changeData(new Date($('#ipvl_date').datebox('getValue')));
}
/**
 * 清除表单数据
 */
function ipvl_clearForm() {
	$('#ipvl_aliases').val('');
	$('#ipvl_domain').val('');
	$('#ipvl_oaid').val('');
}