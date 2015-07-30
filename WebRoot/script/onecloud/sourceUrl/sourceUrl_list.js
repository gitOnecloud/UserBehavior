var sul_pageviewData = [[],[]];
/**
 * 初始化数据
 */
function sul_initData() {
	if(sul_data.status == 1) {
		$('#sul_appdata').html(sul_data.mess);
		return ;
	}
	sul_data.amount = 0;
	sul_data.otherAmount = 0;
	sul_data.otherIpCount = 0;
	sul_data.viewunit = $('#sul_viewunit').combobox('getValue');
	var pageviewData = new Array();
	$.each(sul_data.sourceurl, function(k, v){//按访问数排序
		sul_data.amount += v.amount;
		if(pageviewData.length < 20) {
			if(v.url==null || v.url=='') {
				sul_data.otherAmount += v.amount;
				sul_data.otherIpCount += v.ipsum;
			} if(v.url.indexOf("pispower.com") < 0) {//去掉内部网页
				pageviewData.push({
					'url': v.url,
					'amount': (v.amount/sul_data.viewunit).toFixed(1)*1,
				});
			}
		}/* else {
			sul_data.otherAmount += v.amount;
			sul_data.otherIpCount += v.ipsum;
		}*/
	});
	if(sul_data.otherAmount != 0) {
		pageviewData.push({
			'url': '其他',
			'amount': (sul_data.otherAmount/sul_data.viewunit).toFixed(1)*1,
		});
	}
	//冒泡排序访问数
	index_sortData(pageviewData, 'amount');
	//添加进图标数据
	sul_pageviewData = [[],[]];
	$.each(pageviewData, function(k, v) {
		sul_pageviewData[0].push(v.url);
		sul_pageviewData[1].push(v.amount);
	});
}

//初始化数据
sul_initData();
//日期选择框
$('#sul_date').datebox({
	onSelect: function(date){
		sul_changeData(date);
	},
});
/**
 * 更新数据
 */
function sul_changeData(date) {
	var dateStr = date.getFullYear() + '-';
	if(date.getMonth() < 9) {
		dateStr += '0';
	}
	dateStr += (date.getMonth()+1);
	if($('#sul_datesort').combobox('getValue') == '1') {//按天显示
		dateStr += '-';
		if(date.getDate() < 10) {
			dateStr += '0';
		}
		dateStr += date.getDate();
	}
	sul_getData(dateStr);
}
/**
 * 读取数据
 */
function sul_getData(dateStr) {
	index_mess('读取中...', 0);
	$.getJSON('sourceUrl_list_js.action', 'page.date=' + dateStr, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		sul_data = data;
		sul_initData();
		sul_updateData();
		sul_setShowDate(sul_data.date);
	});
}
/**
 * 设置日期输入框数据
 */
function sul_setShowDate(date) {
	if(sul_data.status == 1) {
		return ;
	}
	if(date.length == 7) {//只有年月，日期置为01
		date += '-01';
	}
	$('#sul_date').datebox('setValue', date);
}
sul_setShowDate(sul_data.date);
//时间范围选择框
$('#sul_datesort').combobox({
	panelHeight: 60,
	editable: false,
	onSelect: function(record){
		var date = $('#sul_date').datebox('getValue');
		if(record.value == '0') {//按月显示
			date = date.substring(0, 7);
			$('#sul_pre').linkbutton({text:'上一月'});
			$('#sul_next').linkbutton({text:'下一月'});
		} else {
			$('#sul_pre').linkbutton({text:'上一天'});
			$('#sul_next').linkbutton({text:'下一天'});
		}
		sul_getData(date);
	},
});
/**
 * 上/下一月/天 切换
 */
function sul_prenext(flag) {
	var date = new Date($('#sul_date').datebox('getValue'));
	if($('#sul_datesort').combobox('getValue') == '1') {//按天显示
		date.setTime(date.getTime() + 1000*60*60*24*flag);
	} else {
		date.setMonth(date.getMonth() + flag); 
	}
	sul_changeData(date);
}
//访问数单位选择框
$('#sul_viewunit').combobox({
	panelHeight: 80,
	editable: false,
	onSelect: function(record){
		if(sul_data.status == 1) {
			index_mess(sul_data.mess, 3);
			return ;
		}
		var chart = $('#sul_container').highcharts();
		chart.yAxis[0].setTitle({
			text: '访问数 (' + record.text + ')'
		});
		chart.series[0].update({
			tooltip: {
	            valueSuffix: ' ' + record.text
	        }
		});
		sul_initData();
		sul_updateData();
	},
});
/**
 * 更新图表
 */
function sul_updateData() {
	var chart = $('#sul_container').highcharts();
	chart.setTitle({
			text: sul_showTitle()
		},{
			text: sul_showSubTile()
		});
	chart.xAxis[0].setCategories(sul_pageviewData[0]);
    chart.series[0].setData(sul_pageviewData[1]);
}
/**
 * 标题
 */
function sul_showTitle() {
	if(sul_data.status == 1) {
		return sul_data.mess;
	}
	return sul_data.date + ' Pispower访问来源';
}
/**
 * 小标题
 */
function sul_showSubTile() {
	if(sul_data.status == 1) {
		return sul_data.mess;
	}
	return ' 浏览' + index_formatPage(sul_data.amount);
}
//图表
$('#sul_container').highcharts({
    chart: {
        type: 'column'
    },
    title: {
        text: sul_showTitle()
    },
    subtitle: {
    	text: sul_showSubTile()
    },
    xAxis: {
        categories: sul_pageviewData[0],
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
            text: '访问数 (' + $('#sul_viewunit').combobox('getText') +')'
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
        name: '访问数',
        data: sul_pageviewData[1],
        tooltip: {
            valueSuffix: ' ' + $('#sul_viewunit').combobox('getText')
        }
    }]
});
/**
 * 显示原始数据
 */
function sul_showdata() {
	$('#sul_datawindow').window('open');
	var str = '<h2>' + sul_showTitle() +
		'</h2><h3>' + sul_showSubTile() +
		'</h3><table width="100%"><tr><td>访问来源</td><td>访问数</td></tr>';
	$.each(sul_data.sourceurl, function(k, v) {
		str += '<tr><td>' +
			v.url + '</td><td>' +
			index_formatPage(v.amount)+ '</td></tr>';
	});
	str += '</table>';
	$('#sul_data').html(str);
}

