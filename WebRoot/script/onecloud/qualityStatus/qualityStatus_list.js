var ipvl_ipviewData = [[],[],[]],
ipvl_byIpData = [[],[],[]];
/**
 * 初始化数据
 */
function qualityStatus_initData() {
	if(qualityStatus_data.status == 1) {
		$('#qualityStatus_infodata').html(qualityStatus_data.mess);
		return ;
	}
	$('#qualityStatus_infodata').html('云架构：' + qualityStatus_data.appname +
			'&emsp;用户：' + qualityStatus_data.user +
			'&emsp;OAID：' + qualityStatus_data.oaid +
			'<br />一级域名：' + qualityStatus_data.aliases +
			'<br />二级域名：' + qualityStatus_data.domain);
	qualityStatus_data.viewunit = $('#qualityStatus_viewunit').combobox('getValue');
	var qualityStatusData = new Array();
	qualityStatusData =qualityStatus_data.info;
	//冒泡排序浏览数
	index_sortData(qualityStatusData, 'amount');
	//添加进图标数据
	ipvl_ipviewData = [[],[]];
	$.each(qualityStatusData, function(index, value) {
		ipvl_ipviewData[0].push(value.status);
		ipvl_ipviewData[1].push((value.amount/qualityStatus_data.viewunit).toFixed(1)*1);
	});
}

//初始化数据
qualityStatus_initData();
//按下enter键搜索
$('#qualityStatus_aliases').keydown(function(e) {
	if(e.keyCode==13) {
		qualityStatus_searchApp();
	}
});
$('#qualityStatus_domain').keydown(function(e) {
	if(e.keyCode==13) {
		qualityStatus_searchApp();
	}
});
$('#qualityStatus_oaid').keydown(function(e) {
	if(e.keyCode==13) {
		qualityStatus_searchApp();
	}
});
//日期选择框
$('#ipvl_date').datebox({
	onSelect: function(date){
		qualityStatus_changeData(date);
	},
});
/**
 * 更新数据
 */
function qualityStatus_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	if($('#qualityStatus_datesort').combobox('getValue') == '1') {//按天显示
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
	var str = $('#qualityStatus_aliases').val();
	if(str != '') {
		params["page.aliases"] = str;
	}
	str = $('#qualityStatus_domain').val();
	if(str != '') {
		params["page.domain"] = str;
	}
	str = $('#qualityStatus_oaid').val();
	if(str != '') {
		params["page.oaid"] = str;
	}
	index_mess('读取中...', 0);
	$.getJSON('qualityStatus_list_js.action', params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		qualityStatus_data = data;
		qualityStatus_initData();
		ipvl_updateData();
		ipvl_setShowDate(qualityStatus_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function ipvl_setShowDate(date) {
	if(qualityStatus_data.status == 1) {
		return ;
	}
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	$('#ipvl_date').datebox('setValue', date);
}
ipvl_setShowDate(qualityStatus_data.date);
//时间范围选择框
$('#qualityStatus_datesort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		var date = $('#ipvl_date').datebox('getValue');
		if(record.value == '0') {//按月显示
			date = date.substring(0, 7);
			$('#qualityStatus_pre').linkbutton({text:'上一月'});
			$('#qualityStatus_next').linkbutton({text:'下一月'});
		} else {
			$('#qualityStatus_pre').linkbutton({text:'上一天'});
			$('#qualityStatus_next').linkbutton({text:'下一天'});
		}
		ipvl_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function qualityStatus_prenext(flag) {
	var date = new Date($('#ipvl_date').datebox('getValue'));
	if($('#qualityStatus_datesort').combobox('getValue') == '1') {//按天显示
		date.setTime(date.getTime() + 1000*60*60*24*flag);
	} else {
		date.setMonth(date.getMonth() + flag); 
	}
	qualityStatus_changeData(date);
}
//浏览数单位选择框
$('#qualityStatus_viewunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(qualityStatus_data.status == 1) {
			index_mess(qualityStatus_data.mess, 3);
			return ;
		}
		var chart = $('#qualityStatus_container').highcharts();
		chart.yAxis[0].setTitle({
			text: '次数 (' + record.text + ')'
		});
		chart.series[0].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		qualityStatus_initData();
		ipvl_updateData();
	},
});
/**
 * 更新图表
 */
function ipvl_updateData() {
	var chart = $('#qualityStatus_container').highcharts();
	chart.setTitle({
			text: qualityStatus_showTitle()
		},{
			text: qualityStatus_showSubTile()
		});
	chart.xAxis[0].setCategories(ipvl_ipviewData[0]);
    chart.series[0].setData(ipvl_ipviewData[1]);
    //chart.series[1].setData(ipviewData[2]);
}
/**
 * 标题
 */
function qualityStatus_showTitle() {
	if(qualityStatus_data.status == 1) {
		return qualityStatus_data.mess;
	}
	return qualityStatus_data.date+' 应用访问情况';
}
/**
 * 小标题
 */
function qualityStatus_showSubTile() {
	if(qualityStatus_data.status == 1) {
		return qualityStatus_data.mess;
	}
	return '';
}
//图表
$('#qualityStatus_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: qualityStatus_showTitle()
    },
    subtitle: {
    	text: qualityStatus_showSubTile()
    },
    xAxis: {
        categories: ipvl_ipviewData[0],
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
            text: '次数 (' + $('#qualityStatus_viewunit').combobox('getText') +')'
        },
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
        name: '返回码次数',
        data: ipvl_ipviewData[1],
        tooltip: {
            valueSuffix: ' ' + $('#qualityStatus_viewunit').combobox('getText')
        }
    }]
});
/**
 * 显示原始数据
 */
function qualityStatus_showdata() {
	$('#qualityStatus_datawindow').window('open');
	var str = '<h2>' + qualityStatus_showTitle() +
		'</h2><h3>云架构：' + qualityStatus_data.appname +
		'&emsp;用户：' + qualityStatus_data.user +
		'&emsp;OAID：' + qualityStatus_data.oaid +
		'<br />一级域名：' + qualityStatus_data.aliases +
		'<br />二级域名：' + qualityStatus_data.domain +
		'<br />' + qualityStatus_showSubTile() +
		'</h3><table width="100%"><tr><td>状态码</td><td>次数</td></tr>';
	    str += qualityStatus_createData(qualityStatus_data.info);
	str += '</table>';
	$('#qualityStatus_data').html(str);
}
/**
 *   生成报表数据
 * @param qualityStatus
 * @returns {String}
 */
function qualityStatus_createData(qualityStatus){
	var str= '';
	//冒泡排序浏览数
	index_sortData(qualityStatus, 'amount');
	$.each(qualityStatus, function(index,value){
		str += '<tr><td>'+value.status+ '</td><td>'+
		value.amount+ '</td><td>';
	});
	return str;
}
/**
 * 搜索应用
 */
function qualityStatus_searchApp() {
	qualityStatus_changeData(new Date($('#ipvl_date').datebox('getValue')));
}
/**
 * 清除表单数据
 */
function qualityStatus_clearForm() {
	$('#qualityStatus_aliases').val('');
	$('#qualityStatus_domain').val('');
	$('#qualityStatus_oaid').val('');
}