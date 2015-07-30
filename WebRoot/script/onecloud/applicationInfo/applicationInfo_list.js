
var isUpdateInfo = false;

$('#application_arena,#application_arena_add').combobox({
		url:'applicationInfo_findSatellite.action',
		valueField:'arenaId',
		textField:'arenaName',
		panelHeight:'auto',
	});

$('#applicationinfo_add').dialog({
	closed: true,
	resizable: true,
	title: "添加应用监控",
	width: 300,
	height: 210,
	buttons:[{
		text:'保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			var oaid = $('#applicationinfo_oaid_add').val();
			if (!oaid){
				index_mess('请输入oaid！', 4);
				$('#applicationinfo_oaid_add').focus();
				return false;
			}
			params ['applicationInfo.oaid'] = oaid;
			var description = $('#applicationinfo_description_add').val();
			if(!description){
				index_mess('请输入应用描述！', 4);
				$('#applicationinfo_description_add').focus();
				return false;
			}
			params['applicationInfo.description'] = description;
			var satellite = $('#application_arena_add').combobox('getValue');
			if(!satellite){
				index_mess('请输入Satellite！', 4);
				$('#application_arena_add').focus();
				return false;
			}
			params['applicationInfo.arenaSatellite.id'] = satellite;
			params['applicationInfo.isMonitor'] = $(':radio[name="applicationinfo_isActive_add"]:checked').val();
			if (isUpdateInfo){
				params['applicationInfo.id'] = $('#applicationinfo_id_add').val();
				index_mess("更新中...", 0);
			}else{
				index_mess("添加中...", 0);
			}
			$.post('applicationInfo_save.action',params,function(data){
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#applicationinfo_add').dialog('close');
				clearInfoData();
				$('#applicationinfo_table').datagrid('load');
				isUpdateInfo = false;
			},'json');
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#applicationinfo_add').dialog('close');
			clearInfoData();
		}
	}]
});

function clearInfoData(){
	$('#applicationinfo_oaid_add').val('');
	$('#applicationinfo_description_add').val('');
	$('#application_arena_add').combobox('clear');
	$(':radio[name="applicationinfo_isActive_add"]').eq(0).attr("checked",true); 
}

function clearQueryCondition (){
	$('#applicationinfo_oaid').val('');
	$('#applicationinfo_description').val('');
	$('#application_monitor').attr('value','1'); 
	$('#application_arena').combobox('clear');
}
$('#applicationinfo_table').datagrid({
	url:'applicationInfo_list_js.action?_=' + Math.random(),
	striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    queryParams: {
    	"page.oaid": {"id":"applicationinfo_oaid","type":"text"},
    	"page.description": {"id":"applicationinfo_description","type":"text"},
    	"page.isMonitor": {"id":"application_monitor","type":"text"},
    	"page.satellite": {"id":"application_arena","type":"combobox"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns:[[
       {field: "id", title: "ID", align: 'center',hidden:true},
	   {field: "satelliteId", hidden: true},
       {field: "oaid", title: "oaid", width: 60, align: 'center'},
       {field: "description",title: "描述", width:60,align:'center'},
       {field: "isMonitor", title:'监控',width:60,align:'center'},
       {field: "satelliteName", title: "Satellite", width: 70, align: 'center',},
	   {field: 'action', title: '操作', width: 60, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="appinfo_editor(' + index + ')">编辑</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="appinfo_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
    ]]
});

function appinfo_remove(rowId){
	if(rowId == null) {
		var nps = $('#applicationinfo_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除的应用监控！", 4);
			return;
		}
	}
	$.messager.confirm('提示', '确定要删除选中的应用监控吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("applicationInfo_remove.action?removeAppInfoId=" + rowId + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#applicationinfo_table').datagrid('reload');
		    		index_mess(data.mess, 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    		}
	    	});
		}
	});
}

function freshConfig(){
	if (!$('#application_arena').combobox('getValue')){
		$.messager.alert('提示','请选择好Nagios服务器!','info');
		return;
	}
	index_mess("更新中...", 0);
	$.post('applicationInfo_freshNagiosConfig.action?arenaName='
			+ $('#application_arena').combobox('getText'), function(data) {
				if (data.status == 0){
					index_mess("更新成功", 2);
				}else{
					index_mess(data.mess,1 );
				}
	}, 'json');
}

function appinfo_editor(rowId){
	var row = null;
	if(rowId == null) {
		row = $('#applicationinfo_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择条记录！", 4);
			return;
		}
	} else {
		row = $('#applicationinfo_table').datagrid('getRows')[rowId];
	}
	appinfo_opench(row);
}

function appinfo_opench(row){
	isUpdateInfo = true;
	$('#applicationinfo_id_add').val(row.id);
	$('#applicationinfo_add').dialog('open').dialog('setTitle','更新应用监控 ' + row.oaid);
	$('#applicationinfo_oaid_add').val(row.oaid);
	$('#applicationinfo_description_add').val(row.description);
	$('#application_arena_add').combobox('setValue',row.satelliteId);
	if(row.isMonitor=='是'){
		$(':radio[name="applicationinfo_isActive_add"]').eq(0).attr("checked",true); 
	}else{
		$(':radio[name="applicationinfo_isActive_add"]').eq(1).attr("checked",true); 
	}
}
//第二个刷新配置
$('#freshSatelliteMs2').combobox({
	url: 'arena_satellite_js.action',
	valueField: 'satelliteId',
	textField: 'satelliteName',
	panelHeight:'auto'
});
/**
 *  第二个刷新配置
 */
$('#freshNagiosMs2').dialog({
	closed: true,
	resizable: true,
	title: "刷新应用监控",
	width: 330,
	height: 210,
	buttons:[{
		text:'保存',
		iconCls: 'icon-ok',
		handler: function() {
			var satellite = $('#freshSatelliteMs2').combobox('getText');
			if(!satellite){
				index_mess('请选择要更新的Satellite！', 4);
				return false;
			}
			var isReport = $('#ncli_report2 input:eq(0)').is(':checked')? 1: 0;
			var shellParams = " -H 172.16.1.71 -c config_app -t 600 -a "
				+ satellite + " " + isReport;
			index_mess("更新中...", 0);
			$.post('nagiosCheck_updateNagios.action',{"shellParams":shellParams},function(data){
				if (data.status == 0){
					index_mess("更新成功", 2);
					closeFreshNagiosMs2();
				}else{
					index_mess(data.mess,1 );
					closeFreshNagiosMs2();
				}
			},'json');
		}
	},{
		text:'取消',
		iconCls: 'icon-ok',
		handler: function() {
			closeFreshNagiosMs2();
		}
	}]
});
function closeFreshNagiosMs2(){
	$('#freshSatelliteMs2').combobox('clear');
	$('#ncli_report2 input:eq(0)').attr("checked",'true');
	$('#freshNagiosMs2').dialog('close');
}
