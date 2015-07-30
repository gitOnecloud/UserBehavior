var bill_billingData = [[],[],[],[],[],[],[]];
/**
 * 初始化数据
 */
function bili_initData() {
	if(bili_data.status == 1) {
		$('#bili_appdata').html(bili_data.mess);
		return ;
	}
	$('#bili_appdata').html('云架构：' + bili_data.appname +
			'&emsp;用户：' + bili_data.user +
			'&emsp;OAID：' + bili_data.oaid);
	bili_data.totalCount = 0;
	bili_data.noVmCount = 0;
	bili_data.feeunit = $('#bili_feeunit').combobox('getValue');
	bill_billingData = [[],[],[],[],[],[],[]];//day cpu memory bandwidth storage vm total
	var showVm = false;
	switch($('#bili_datesort').combobox('getValue')) {
		case '1' ://按天
			break;
		case '2' ://按年
			showVm = true;
			break;
		case '0': //按月
			showVm = false;
			break;
	}
	$.each(bili_data.billpage, function(k, v){
		bili_data.totalCount += v.totalFee;
		bill_billingData[0].push(v.date);
		bill_billingData[1].push((v.cpuFee/bili_data.feeunit).toFixed(2)*1);
		bill_billingData[2].push((v.memoryFee/bili_data.feeunit).toFixed(2)*1);
		bill_billingData[3].push((v.bandwidthFee/bili_data.feeunit).toFixed(2)*1);
		bill_billingData[4].push((v.storageFee/bili_data.feeunit).toFixed(2)*1);
		bill_billingData[5].push((v.vmFee/bili_data.feeunit).toFixed(2)*1);
		if(showVm) {
			bili_data.noVmCount += v.totalFee;
			bill_billingData[6].push((v.totalFee/bili_data.feeunit).toFixed(2)*1);
		} else {
			bili_data.noVmCount += v.totalFee - v.vmFee;
			bill_billingData[6].push(((v.totalFee-v.vmFee)/bili_data.feeunit).toFixed(2)*1);
		}
	});
}

//初始化数据
bili_initData();
//按下enter键搜索
$('#bili_appname').keydown(function(e) {
	if(e.keyCode==13) {
		bili_searchApp();
	}
});
$('#bili_oaid').keydown(function(e) {
	if(e.keyCode==13) {
		bili_searchApp();
	}
});
//日期选择框
$('#bili_date').datebox({
	onSelect: function(date){
		bili_changeData(date);
	},
});
/**
 * 日期输入框选择后更新数据
 */
function bili_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	switch($('#bili_datesort').combobox('getValue')) {
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
		case '0': //按月
			break;
	}
	bili_getData(dateStr);
}
/**
 * 读取数据
 */
function bili_getData(dateStr) {
	var params = {};
	params["page.date"] = dateStr;
	var str = $('#bili_appname').val();
	if(str != '') {
		params["page.appname"] = str;
	}
	str = $('#bili_oaid').val();
	if(str != '') {
		params["page.oaid"] = str;
	}
	index_mess('读取中...', 0);
	$.getJSON('billing_list_js.action', params, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		bili_data = data;
		bili_initData();
		bili_updateData();
		bili_setShowDate(bili_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function bili_setShowDate(date) {
	if(bili_data.status == 1) {
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
	$('#bili_date').datebox('setValue', date);
}
bili_setShowDate(bili_data.date);
//时间范围选择框
$('#bili_datesort').combobox({
	panelHeight: 65,
	editable: false,
	onSelect: function(record){
		var date = $('#bili_date').datebox('getValue');
		switch(record.value) {
		case '1' :
			$('#bili_pre').linkbutton({text:'上一天'});
			$('#bili_next').linkbutton({text:'下一天'});
			break;
		case '2' :
			date = date.substring(0, 4);
			$('#bili_pre').linkbutton({text:'上一年'});
			$('#bili_next').linkbutton({text:'下一年'});
			$('#bili_container').highcharts().series[4].show();
			break;
		default:
			date = date.substring(0, 7);
			$('#bili_pre').linkbutton({text:'上一月'});
			$('#bili_next').linkbutton({text:'下一月'});
			$('#bili_container').highcharts().series[4].hide();
	}
		bili_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function bili_prenext(flag) {
	var date = new Date($('#bili_date').datebox('getValue'));
	switch($('#bili_datesort').combobox('getValue')) {
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
	bili_changeData(date);
}
//费用单位选择框
$('#bili_feeunit').combobox({
	panelHeight: 65,
	editable: false,
	onSelect: function(record){
		if(bili_data.status == 1) {
			index_mess(bili_data.mess, 3);
			return ;
		}
		var chart = $('#bili_container').highcharts();
		chart.yAxis[0].setTitle({
			text: '费用 (' + record.text + ')'
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
		chart.series[2].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		chart.series[3].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		chart.series[4].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		chart.series[5].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		bili_initData();
		bili_updateData();
	},
});
/**
 * 更新图表
 */
function bili_updateData() {
	var chart = $('#bili_container').highcharts();
	chart.setTitle({
			text: bili_showTitle()
		},{
			text: bili_showSubTile()
		});
	chart.xAxis[0].setCategories(bill_billingData[0]);
    chart.series[0].setData(bill_billingData[1]);
    chart.series[1].setData(bill_billingData[2]);
    chart.series[2].setData(bill_billingData[3]);
    chart.series[3].setData(bill_billingData[4]);
    chart.series[4].setData(bill_billingData[5]);
    chart.series[5].setData(bill_billingData[6]);
}
/**
 * 标题
 */
function bili_showTitle() {
	if(bili_data.status == 1) {
		return bili_data.mess;
	}
	return bili_data.date + ' 各项资源费用';
}
/**
 * 小标题
 */
function bili_showSubTile() {
	if(bili_data.status == 1) {
		return bili_data.mess;
	}
	var mess = '';
	switch($('#bili_datesort').combobox('getValue')) {
		case '1' ://按天
			break;
		case '2' ://按年
			break;
		case '0': //按月
			mess = '（不含云主机）';
			break;
	}
	//return '总费用' + index_formatFee(bili_data.totalCount);
	return '总费用' + index_formatFee(bili_data.noVmCount) + mess;
}
//图表
$('#bili_container').highcharts({
    chart: {
        type: 'column'
    }, credits: {
    	enabled: false,
	}, title: {
        text: bili_showTitle()
    }, subtitle: {
    	text: bili_showSubTile()
    }, xAxis: {
        categories: bill_billingData[0],
    }, yAxis: [{
    	min: 0,
        title: {
            text: '费用 (' + $('#bili_feeunit').combobox('getText') + ')'
        },
    /*}, {
    	min: 0,
    	title: {
            text: '浏览数 (' + $('#bili_feeunit').combobox('getText') + ')',
        },
        opposite: true,*/
    }], tooltip: {
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
        name: 'cpu',
        data: bill_billingData[1],
        tooltip: {
            valueSuffix: ' ' + $('#bili_feeunit').combobox('getText')
        }
    }, {
        name: '内存',
        data: bill_billingData[2],
        tooltip: {
            valueSuffix: ' ' + $('#bili_feeunit').combobox('getText')
        }
    }, {
        name: '流量',
        data: bill_billingData[3],
        tooltip: {
            valueSuffix: ' ' + $('#bili_feeunit').combobox('getText')
        }
    }, {
        name: '储存',
        data: bill_billingData[4],
        tooltip: {
            valueSuffix: ' ' + $('#bili_feeunit').combobox('getText')
        }
    }, {
        name: '云主机',
        data: bill_billingData[5],
        tooltip: {
            valueSuffix: ' ' + $('#bili_feeunit').combobox('getText')
        }
    }, {
        name: '总费用',
        type: 'spline',
        data: bill_billingData[6],
        //yAxis: 1,
        tooltip: {
            valueSuffix: ' ' + $('#bili_feeunit').combobox('getText')
        }
    }]
});
//将云主机费用隐藏
$('#bili_container').highcharts().series[4].hide();
/**
 * 显示原始数据
 */
function bili_showdata() {
	$('#bili_datawindow').window('open');
	var str = '<h2>' + bili_showTitle() +
		'</h2><h3>云架构：' + bili_data.appname +
		'&emsp;用户：' + bili_data.user +
		'&emsp;OAID：' + bili_data.oaid +
		'<br />总费用' + index_formatFee(bili_data.totalCount) +
		'</h3><table width="100%"><tr><td>时间</td><td>cpu使用</td><td>cpu费用</td>' +
		'<td>内存使用</td><td>内存费用</td>' +
		'<td>流量使用</td><td>流量费用</td>' +
		'<td>存储使用</td><td>存储费用</td>' +
		'<td>云主机使用</td><td>云主机费用</td>' +
		'<td>总费用</td></tr>';
	$.each(bili_data.billpage, function(k, v) {
		str += '<tr><td>' +
			v.date + '</td><td>' +
			index_formatHour(v.cpuAmount) + '</td><td>' +
			index_formatFee(v.cpuFee) + '</td><td>' +
			index_formatTraffic(v.memoryAmount, 3)+ '</td><td>' +
			index_formatFee(v.memoryFee)+ '</td><td>' +
			index_formatTraffic(v.bandwidthAmount, 3)+ '</td><td>' +
			index_formatFee(v.bandwidthFee)+ '</td><td>' +
			index_formatTraffic(v.storageAmount, 3)+ '</td><td>' +
			index_formatFee(v.storageFee)+ '</td><td>' +
			v.vmAmount + '台</td><td>' +
			index_formatFee(v.vmFee)+ '</td><td>' +
			index_formatFee(v.totalFee)+ '</td><td>' +
			'</tr>';
	});
	str += '</table>';
	$('#bili_data').html(str);
}
/**
 * 搜索应用
 */
function bili_searchApp() {
	bili_changeData(new Date($('#bili_date').datebox('getValue')));
}
/**
 * 清除表单数据
 */
function bili_clearForm() {
	$('#bili_appname').val('');
	$('#bili_oaid').val('');
}