
var updateIcheckId=0;
var updateIcheck_change=false;
var updateNagiosInformation=false;
if($('#nagiosCheck_toolbar2').length == 0) {
    $('body').append('<div id="nagiosCheck_toolbar2" style="padding:5px;height:auto;">'+
			'<div style="padding: 5px 0 0 4px;">'+
			'组件名称：<input id="nagiosCheck_name2" class="igdli_input160" />'+
			'&ensp;<a href="javascript:;" class="easyui-linkbutton"'+
				'onclick="$(\'#nagiosCheck_igd\').combogrid(\'grid\').datagrid(\'load\');"'+
				'data-options="iconCls:\'icon-search\'">查找</a>'+
		'</div></div>');
} else {
	$('#nagiosCheck_name2').val('');
}
$('#nagiosCheck_igd').combogrid({
	panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'name',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	toolbar: '#nagiosCheck_toolbar2',//工具条
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
	queryParams: {
    	"page.name": {"id":"nagiosCheck_name2","type":"text"}
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
       {field: "name", title: "组件名称", width: 80, align: 'center'},
       {field: "port", title: "端口", width: 60, align: 'center',
    	   formatter: function(value, row ,index) {
    		   if(value == 0) {
    			   return '';
    		   } else {
    			   return value;
    		   }
       }},
       {field: "pname", title: "父组件", width: 80, align: 'center'},
       {field: "type", title: "类型", width: 60, align: 'center'},
       {field: "register", title: "注册组件", width: 40, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '是';
    		   } else {
    			   return '';
    		   }
       }}
     ]]
});


$('#nagiosCheck_table').datagrid({
	url: 'nagiosCheck_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#nagiosCheck_toolbar',//工具条
    queryParams: {
    	"page.checkName": {"id":"nagiosCheck_checkName","type":"text"},
    	"page.igdefine": {"id":"nagiosCheck_igd","type":"combogrid"},
    	"page.stoptime": {"id":"nagiosCheck_stoptime","type":"text"},
    	"page.checkCommand": {"id":"nagiosCheck_command","type":"text"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center',hidden:true},
	   {field: "ncheckId", hidden: true},
       {field: "checkName", title: "监控名称", width: 60, align: 'center',
		   formatter:function(value,row,index){
			   return '<a href="javascript:void(0);" onclick="nagiosCheck_update(' + index + ')">'+value+'</a>';
		   }},
       {field: "description",title: "描述", width:60,align:'center'},
       {field: "igdId", hidden: true},
       {field: "igdName", title: "组件名称", width: 70, align: 'center',
	       formatter: function(value, row, index) {
	    	   return '<a href="javascript:;" onclick="nagiosCheck_findIG(' + index + ')">' + value + '</a>';
	       }},
       {field: "stoptime", title: "监控间隔", width: 25, align: 'center'},
       {field: "isActive", title: "启用", width: 25, align: 'center'},
       {field: "command", title: "命令", width:180,align:'center'},
	   {field: 'action', title: '操作', width: 60, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="nagiosCheck_editrow(' + index + ')">编辑</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="nagiosCheck_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
	]]
});
/**
 * update the nagios check information.
 * @param rowIndex
 */
function nagiosCheck_update(rowIndex){
	var row = $('#nagiosCheck_table').datagrid('getRows')[rowIndex];
	updateNagiosInformation = true;
	$('#nagios_add').dialog('open').dialog('setTitle','修改监控');
	$('#nagios_checkName').val(row.checkName);
	$('#nagios_command').val(row.command);
	$('#nagios_description').val(row.description);
	$('#nagios_stoptime').combobox('setValue',row.stoptime);
	$('#nagios_id').val(row.ncheckId);
}
function nagiosCheck_add() {
	updateNagiosInformation = false;
	$('#nagios_add').dialog('open').dialog('setTitle','添加监控');
	$('#nagios_checkName').val('');
	$('#nagios_command').val('');
	$('#nagios_description').val('');
	$('#nagios_stoptime').combobox('setValue', 10);
}

/**
 *  Delete the ingredientCheck by Id
 * @param ingredientCheckId
 */
function nagiosCheck_remove(ingredientCheckId){
	if(ingredientCheckId == null) {
		ingredientCheckId = new Array();
		var nps = $('#nagiosCheck_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除监控！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ingredientCheckId.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ingredientCheckId.length + ' 个监控吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("nagiosCheck_remove_js.action?json=" + ingredientCheckId.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#nagiosCheck_table').datagrid('reload');
		    		index_mess(data.mess, 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    		}
	    	});
		}
	});
}

function nagiosCheck_editrow(rowId){
	var row = null;
	if(rowId == null) {
		row = $('#nagiosCheck_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择条记录！", 4);
			return;
		}
	} else {
		row = $('#nagiosCheck_table').datagrid('getRows')[rowId];
	}
	nagiosCheck_opench(row);
}

function nagiosCheck_opench(row){
	updateIcheckId = row.id;
	updateIcheck_change = true;
	$('#nagiosCheck_add').dialog('open').dialog('setTitle','更新组件监控 ' + row.igdName);
	$('#nagiosCheck_igd_add').combogrid('setValue',row.igdId);
	$('#nagiosCheck_nagios').combogrid('setValue',row.ncheckId);
	if(row.isActive == '是'){
		$(':radio[name="isActive"]').eq(0).attr("checked",true);
	}else{
		$(':radio[name="isActive"]').eq(1).attr("checked",true);
	}
}
/**
 *  Clear the query parameter.
 */
function nagiosCheck_clearData() {
	$('#nagiosCheck_checkName').val('');
	$('#nagiosCheck_igd').combogrid('clear');
	$('#nagiosCheck_stoptime').val('');
	$('#nagiosCheck_command').val('');
}
/**
 *  Clear the nagios parameter.
 */
function nagios_clearData() {
	$('#nagios_checkName').val('');
	$('#nagios_stoptime').combobox('setValue', '1');
	$('#nagios_command').val('');
	$('#nagios_description').val('');
}
/**
 *  Clear the data of adding the nagios check.
 */
function addNagiosCheck_clearData (){
	$('#nagiosCheck_igd_add').combogrid('clear');
	$('#nagiosCheck_nagios').combogrid('clear');
}
/**
 * 跳转到组件管理页面
 */
function nagiosCheck_findIG(rowIndex) {
	if(! index_tabs.tabs('exists', '组件管理')) {
		index_openTab('组件管理', 'ingredientdefine_list.action', true);
		setTimeout(function() {
			nagiosCheck_openIG(rowIndex);
		}, 2000);
	} else {
		index_tabs.tabs('select', '组件管理');
		igdfli_clear();
		nagiosCheck_openIG(rowIndex);
	}
}
/**
 * 打开组件查询页面
 */
function nagiosCheck_openIG(rowIndex) {
	var row = $('#nagiosCheck_table').datagrid('getRows')[rowIndex];
	igdfli_clear();
	$('#igdfli_name').val(row.igdName);
	$('#igdfli_table').datagrid('load');
}
/**
 * 
 */
$('#nagios_add').dialog({
	closed: true,
	resizable: true,
	title: "添加监控指令",
	width: 340,
	height: 230,
	buttons:[{
		text:'保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			var checkName = $('#nagios_checkName').val();
			if (!checkName.trim()){
				index_mess('请输入监控名！', 4);
				$('#nagios_checkName').focus();
				return false;
			}
			params['nagiosCheck.checkName']=checkName.trim();
			params['nagiosCheck.stoptime']=$('#nagios_stoptime').combobox('getValue');
			var command = $('#nagios_command').val();
			if (!command.trim()){
				index_mess('请输入监控命令！', 4);
				$('#nagios_command').focus();
				return false;
			}
			params['nagiosCheck.checkCommand']=command;
			var description = $('#nagios_description').val();
			if (!description.trim()){
				index_mess('请输入描述！', 4);
				$('#nagios_description').focus();
				return false;
			}
			params['nagiosCheck.description']=description;
			if ($('#resultVerify').html()){
				index_mess('监控名已存在', 3);
				return false;
			}
			if (updateNagiosInformation){
				params['nagiosCheck.id']=$('#nagios_id').val();
			}
			index_mess("添加中...", 0);
			$.post('nagiosCheck_addNagios.action', params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#nagios_add').dialog('close');
				nagios_clearData();
				updateNagiosInformation = false;
				$('#nagiosCheck_table').datagrid('reload');
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#nagios_add').dialog('close');
		}
	}]
});
/**
 *  ************************************
 *  ************************************
 * 	************************************
 */
$('#nagiosCheck_add').dialog({
	closed: true,
	resizable: true,
	title: "添加组件监控",
	width: 300,
	height: 185,
	buttons:[{
		text:'保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			var igdId = $('#nagiosCheck_igd_add').combogrid('getValue');
			if (!igdId){
				index_mess('请选择要监控的组件！', 4);
				$('#nagiosCheck_igd_add').focus();
				return false;
			}
			params['igdId']=igdId;
			var nagioaId = $('#nagiosCheck_nagios').combogrid('getValue');
			if (!nagioaId) {
				index_mess('请选择要监控命令！', 4);
				$('#nagiosCheck_nagios').focus();
				return false;
			}
			params['ncheckId']=nagioaId;
			var isActive = $(':radio[name="isActive"]:checked').val();
			if (!isActive){
				index_mess('请选择是否启用！', 4);
				return false;
			}
			params['isActive'] = isActive;
			index_mess("添加中...", 0);
			var url = '';
			if (updateIcheck_change){
				params['icheckId'] = updateIcheckId;
				url = "nagiosCheck_updateNagiosCheck.action";
				index_mess("更新中...", 0);
			}else{
				url ='nagiosCheck_addNagiosCheck.action';
				index_mess("添加中...", 0);
			}
			$.post(url,params,function(data){
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#nagiosCheck_add').dialog('close');
				addNagiosCheck_clearData();
				$('#nagiosCheck_table').datagrid('reload');
			},'json');
		}
	},{
		text:'取消',
		iconCls: 'icon-ok',
		handler: function() {
			$('#nagiosCheck_add').dialog('close');
			addNagiosCheck_clearData();
		}
	}]
});
/**
 *  Open the dialog of fresh nagios 
 */
$('#freshNagios').dialog({
	closed: true,
	resizable: true,
	title: "刷新Nagios配置",
	width: 330,
	height: 210,
	buttons:[{
		text:'保存',
		iconCls: 'icon-ok',
		handler: function() {
			var mornitor = $('#freshMornitor').combobox('getValue');
			if(!mornitor){
				index_mess('请选择要Mornitor！', 4);
				return false;
			}
			var satellite = $('#freshSatellite').combobox('getText');
			if(!satellite){
				index_mess('请选择要更新的Satellite！', 4);
				return false;
			}
			var basket = $('#freshBasket').combobox('getText');
			var shellParams = " -H "+mornitor+" -c configure_nagios -t 600 -a "+satellite;
			if(basket){
				shellParams = shellParams+","+basket;
			}
			index_mess("更新中...", 0);
			$.post('nagiosCheck_updateNagios.action',{"shellParams":shellParams},function(data){
				if (data.status == 0){
					index_mess("更新成功", 2);
					closeFreshNagios();
				}else{
					index_mess(data.mess,1 );
					closeFreshNagios();
				}
			},'json');
		}
	},{
		text:'取消',
		iconCls: 'icon-ok',
		handler: function() {
			closeFreshNagios();
		}
	}]
});

function closeFreshNagios(){
	$('#freshMornitor').combobox('clear');
	$('#freshSatellite').combobox('clear');
	$('#freshBasket').combobox('clear');
	$('#resultVerifyBasket').html('');
	$('#freshNagios').dialog('close');
}
/**
 *   Verify the checkName is not exist.
 */
$(function(){
	$('#nagios_checkName').blur(function(){
		var checkName = $('#nagios_checkName').val();
		$.post('nagiosCheck_verifyCheckName.action',{'verfyCheckName':checkName.trim()},function(data){
			if(data.status == 1) {
				$('#resultVerify').html(" 已存在");
			}else{
				$('#resultVerify').html("");
			}
		},'json');
	});
	$('#freshMornitor').combobox({
		url:'nagiosCheck_mornitor_js.action',
		valueField:'ip',
		textField:'arenaName',
		panelHeight:'auto',
		onSelect:function(res){
			$('#freshSatellite').combobox('clear');
			url='nagiosCheck_satelittle_js.action?arenaName='+res.arenaName;
			$('#freshSatellite').combobox('reload',url);
		}
	});
	$('#freshSatellite').combobox({
		valueField:'satelliteId',
		textField:'satelliteName',
		panelHeight:'auto',
		onSelect:function(res){
			$('#freshBasket').combobox('clear');
			url='nagiosCheck_basket_js.action?arenaId='+res.satelliteId;
			$('#freshBasket').combobox('reload',url);
		}
	});
	$('#freshBasket').combobox({
		valueField:'basketId',
		textField:'basketName',
		panelHeight:'auto',
		multiple:true,
		onSelect:function(res){
			$.post('nagiosCheck_verifyBasketUnique.action?arenaId='+res.basketId,function(data){
				if(data.status == 1){
					$('#resultVerifyBasket').html(data.mess);
				}else{
					$('#resultVerifyBasket').html('');
				}
			},'json');
		}
	});
	$('#freshSatelliteMs').combobox({
		url: 'arena_satellite_js.action',
		valueField: 'satelliteId',
		textField: 'satelliteName',
		panelHeight:'auto'
	});
});
/**
 *  Open the dialog of fresh nagios with param report MainIDC
 */
$('#freshNagiosMs').dialog({
	closed: true,
	resizable: true,
	title: "刷新Nagios配置",
	width: 330,
	height: 210,
	buttons:[{
		text:'保存',
		iconCls: 'icon-ok',
		handler: function() {
			var satellite = $('#freshSatelliteMs').combobox('getText');
			if(!satellite){
				index_mess('请选择要更新的Satellite！', 4);
				return false;
			}
			var isReport = $('#ncli_report input:eq(0)').is(':checked')? 1: 0;
			var shellParams = " -H 172.16.1.71 -c nagios_ruby -t 600 -a "
				+ satellite + " " + isReport;
			index_mess("更新中...", 0);
			$.post('nagiosCheck_updateNagios.action',{"shellParams":shellParams},function(data){
				if (data.status == 0){
					index_mess("更新成功", 2);
					closeFreshNagiosMs();
				}else{
					index_mess(data.mess,1 );
					closeFreshNagiosMs();
				}
			},'json');
		}
	},{
		text:'取消',
		iconCls: 'icon-ok',
		handler: function() {
			closeFreshNagiosMs();
		}
	}]
});

function closeFreshNagiosMs(){
	$('#freshSatelliteMs').combobox('clear');
	$('#ncli_report input:eq(0)').attr("checked",'true');
	$('#freshNagiosMs').dialog('close');
}
