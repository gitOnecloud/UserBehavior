var pvl_pageviewData = [[],[]];
/**
 * 初始化数据
 */
function pvl_initData() {
	if(pvl_data.status == 1) {
		$('#pvl_appdata').html(pvl_data.mess);
		return ;
	}
	pvl_data.amount = 0;
	pvl_data.otherAmount = 0;
	pvl_data.otherIpCount = 0;
	pvl_data.viewunit = $('#pvl_viewunit').combobox('getValue');
	var pageviewData = new Array();
	$.each(pvl_data.pageview, function(k, v){//按浏览数排序
		pvl_data.amount += v.amount;
		if(pageviewData.length < 20) {
			if(v.url==null || v.url=='') {
				pvl_data.otherAmount += v.amount;
				pvl_data.otherIpCount += v.ipsum;
			} else {
				pageviewData.push({
					'url': v.url,
					'desc': v.url.length>10? v.url.substring(0, 9) + '...': v.url,
					'amount': (v.amount/pvl_data.viewunit).toFixed(1)*1,
				});
			}
		}/* else {
			pvl_data.otherAmount += v.amount;
			pvl_data.otherIpCount += v.ipsum;
		}*/
	});
	if(pvl_data.otherAmount != 0) {
		pageviewData.push({
			'url': '其他',
			'desc': '其他',
			'amount': (pvl_data.otherAmount/pvl_data.viewunit).toFixed(1)*1,
		});
	}
	//冒泡排序浏览数
	index_sortData(pageviewData, 'amount');
	//添加进图标数据
	pvl_pageviewData = [[],[]];
	$.each(pageviewData, function(k, v) {
		pvl_pageviewData[0].push(v.desc);
		pvl_pageviewData[1].push([v.url, v.amount]);
	});
}

//初始化数据
pvl_initData();
//日期选择框
$('#pvl_date').datebox({
	onSelect: function(date){
		pvl_changeData(date);
	},
});
/**
 * 更新数据
 */
function pvl_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	if($('#pvl_datesort').combobox('getValue') == '1') {//按天显示
		dateStr += '-';
		if(date.getDate() < 10) {
			dateStr += '0';
		}
		dateStr += date.getDate();
	}
	pvl_getData(dateStr);
}
/**
 * 读取数据
 */
function pvl_getData(dateStr) {
	index_mess('读取中...', 0);
	$.getJSON('pageView_list_js.action', 'page.date=' + dateStr, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		pvl_data = data;
		pvl_initData();
		pvl_updateData();
		pvl_setShowDate(pvl_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function pvl_setShowDate(date) {
	if(pvl_data.status == 1) {
		return ;
	}
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	$('#pvl_date').datebox('setValue', date);
}
pvl_setShowDate(pvl_data.date);
//时间范围选择框
$('#pvl_datesort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		var date = $('#pvl_date').datebox('getValue');
		if(record.value == '0') {//按月显示
			date = date.substring(0, 7);
			$('#pvl_pre').linkbutton({text:'上一月'});
			$('#pvl_next').linkbutton({text:'下一月'});
		} else {
			$('#pvl_pre').linkbutton({text:'上一天'});
			$('#pvl_next').linkbutton({text:'下一天'});
		}
		pvl_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function pvl_prenext(flag) {
	var date = new Date($('#pvl_date').datebox('getValue'));
	if($('#pvl_datesort').combobox('getValue') == '1') {//按天显示
		date.setTime(date.getTime() + 1000*60*60*24*flag);
	} else {
		date.setMonth(date.getMonth() + flag); 
	}
	pvl_changeData(date);
}
//浏览数单位选择框
$('#pvl_viewunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(pvl_data.status == 1) {
			index_mess(pvl_data.mess, 3);
			return ;
		}
		var chart = $('#pvl_container').highcharts();
		chart.yAxis[0].setTitle({
			text: '浏览数 (' + record.text + ')'
		});
		chart.series[0].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		pvl_initData();
		pvl_updateData();
	},
});
/**
 * 更新图表
 */
function pvl_updateData() {
	var chart = $('#pvl_container').highcharts();
	chart.setTitle({
			text: pvl_showTitle()
		},{
			text: pvl_showSubTile()
		});
	chart.xAxis[0].setCategories(pvl_pageviewData[0]);
    chart.series[0].setData(pvl_pageviewData[1]);
}
/**
 * 标题
 */
function pvl_showTitle() {
	if(pvl_data.status == 1) {
		return pvl_data.mess;
	}
	return pvl_data.date + ' Pispower页面访问量';
}
/**
 * 小标题
 */
function pvl_showSubTile() {
	if(pvl_data.status == 1) {
		return pvl_data.mess;
	}
	return ' 浏览' + index_formatPage(pvl_data.amount);
}
//图表
$('#pvl_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: pvl_showTitle()
    },
    subtitle: {
    	text: pvl_showSubTile()
    },
    xAxis: {
        categories: pvl_pageviewData[0],
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
    yAxis: {
    	min: 0,
        title: {
            text: '浏览数 (' + $('#pvl_viewunit').combobox('getText') +')'
        },
    },
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
        name: '浏览数',
        data: pvl_pageviewData[1],
        tooltip: {
            valueSuffix: ' ' + $('#pvl_viewunit').combobox('getText')
        }
    }]
});
/**
 * 显示原始数据
 */
function pvl_showdata() {
	$('#pvl_datawindow').window('open');
	var str = '<h2>' + pvl_showTitle() +
		'</h2><h3>' + pvl_showSubTile() +
		'</h3><table width="100%"><tr><td>页面</td><td>浏览数</td></tr>';
	$.each(pvl_data.pageview, function(k, v) {
		str += '<tr><td>' +
			v.url + '</td><td>' +
			index_formatPage(v.amount)+ '</td></tr>';
	});
	str += '</table>';
	$('#pvl_data').html(str);
}

