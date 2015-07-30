/**
 * idName 用来保存id和name的索引
 */
var vipli_data = {'idName': {}};
var vipli_change = true;//是否打开修改页面
var vipli_id = 0;//要更新的vipid
/**
 * 组件查询框的工具栏，添加到这里是防止重复加载
 */
if($('#vipli_toolbar2').length == 0) {
	$('body').append('<div id="vipli_toolbar2" style="padding:5px;height:auto;">'+
			'<div style="padding: 5px 0 0 4px;">'+
			'组件名称：<input id="vipli_name2" class="igdli_input160" />'+
			'&ensp;<a href="javascript:;" class="easyui-linkbutton"'+
				'onclick="$(\'#vipli_igd\').combogrid(\'grid\').datagrid(\'load\');"'+
				'data-options="iconCls:\'icon-search\'">查找</a>'+
		'</div></div>');
} else {
	$('#vipli_name2').val('');
}
/**
 * 组件查询框
 */
$('#vipli_igd').combogrid({
	panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'name',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	toolbar: '#vipli_toolbar2',//工具条
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
	queryParams: {
    	"page.name": {"id":"vipli_name2","type":"text"}
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

$('#vipli_table').datagrid({
	url: 'vip_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#vipli_toolbar',//工具条
    queryParams: {
    	"page.party": {"id":"vipli_party","type":"combobox"},
    	"page.igdefine": {"id":"vipli_igd","type":"combogrid"},
    	"page.vip": {"id":"vipli_vip","type":"text"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
	   {field: "ptId", hidden: true},
       {field: "ptName", title: "环境名称", width: 80, align: 'center'},
       {field: "igdId", hidden: true},
       {field: "igdName", title: "组件名称", width: 80, align: 'center',
	       formatter: function(value, row, index) {
	    	   return '<a href="javascript:;" onclick="vipli_findIG(' + index + ')">' + value + '</a>';
	       }},
       {field: "ip", title: "VIP", width: 80, align: 'center'},
       {field: "description", title: "备注", width: 60, align: 'center'},
	   {field: 'action', title: '操作', width: 60, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="vipli_editrow(' + index + ')">编辑</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="vipli_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
	]]
});

/**
 * 读取环境信息
 */
$.getJSON('arena_all_js.action', function(data) {
	var searchData = [{'value': '', 'text': '全部'}];
	$.each(data, function(k, v) {
		searchData.push(v);
	});
	$('#vipli_party').combobox('loadData', searchData);
	$('#vipli_addparty').combobox('loadData', data);
});

/**
 * 添加窗口
 */
$('#vipli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	title: "添加vip",
	width: 340,
	height: 260,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			var party = $('#vipli_addparty').combobox('getValue');
			if(! party.match(/^\d.*$/)) {
				index_mess('请先选择环境！', 4);
				$('#vipli_addparty').focus();
				return false;
			}
			params['vip.viParty.id'] = $('#vipli_addparty').combobox('getValue');
			var igdefine = vipli_data.idName[$('#vipli_addigdefine').combobox('getText')];
			if(igdefine == null) {
				index_mess('组件选择有误！', 4);
				$('#vipli_addigdefine').focus();
				return false;
			}
			params['vip.igDefine.id'] = igdefine;
			var vip = $('#vipli_addvip').val();
			if(vip.length == 0) {
				index_mess("请输入vip！", 4);
				$('#vipli_addvip').focus();
				return false;
			}
			if(vip.length > 50) {
				index_mess("字数超出 " + (vip.length - 50) + " 个！限制 50 个！", 4);
				$('#vipli_addvip').focus();
				return false;
			}
			params['vip.ip'] = vip;
			params['vip.description'] = $('#vipli_addDesc').val();
			var url = '';
			if(vipli_change) {
				params['vip.id'] = vipli_id;
				url = "vip_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "vip_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#vipli_add').dialog('close');
				if(vipli_change) {
					$('#vipli_table').datagrid('reload');
				} else {
					$('#vipli_table').datagrid('loadLast');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#vipli_add').dialog('close');
		}
	}], onOpen: function() {
		$('#vipli_addigdefine').combobox({
			onChange: function(newValue, oldValue) {
				if(newValue==null || newValue=='' || newValue==oldValue) {
					return;
				}
				$.post("ingredientdefine_names_js.action", {'json': newValue}, function(data) {
					if(data.length != 0) {
						$('#vipli_addigdefine').combobox('loadData',data);
						$.each(data, function(k, v) {
							vipli_data.idName[v.text] = v.id;
						});
					}
				}, "json");
			}
		});
		if(!vipli_change) {
			vipli_changeValues('', '', '', '');
		}
	},
});

/**
 * 更新添加页面的值
 */
function vipli_changeValues(party, parent, vip, desc) {
	$('#vipli_addparty').combobox('setValue', party);
	$('#vipli_addigdefine').combobox('setValue', parent);
	$('#vipli_addvip').val(vip);
	$('#vipli_addDesc').val(desc);
}
/**
 * 批量删除
 */
function vipli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#vipli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除vip！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个vip吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("vip_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#vipli_table').datagrid('reload');
		    		index_mess(data.mess, 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    		}
	    	});
		}
	});
}
/**
 * 打开编辑窗口
 */
function vipli_opench(row) {
	vipli_id = row.id;
	vipli_change = true;
	$('#vipli_add').dialog('open').dialog('setTitle','更新VIP - ' + row.ip);
	vipli_changeValues(row.ptId, row.igdName, row.ip, row.description);
}
/**
 * 编辑
 */
function vipli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#vipli_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个vip！", 4);
			return;
		}
	} else {
		row = $('#vipli_table').datagrid('getRows')[rowId];
	}
	vipli_opench(row);
}
/**
 * 清除搜索数据
 */
function vipli_clearData() {
	$('#vipli_party').combobox('setValue', '');
	$('#vipli_igd').combogrid('clear');
	$('#vipli_vip').val('');
}
/**
 * 跳转到组件管理页面
 */
function vipli_findIG(rowIndex) {
	if(! index_tabs.tabs('exists', '组件管理')) {
		index_openTab('组件管理', 'ingredientdefine_list.action', true);
		setTimeout(function() {
			vipli_openIG(rowIndex);
		}, 2000);
	} else {
		index_tabs.tabs('select', '组件管理');
		igdfli_clear();
		vipli_openIG(rowIndex);
	}
}
/**
 * 打开组件查询页面
 */
function vipli_openIG(rowIndex) {
	var row = $('#vipli_table').datagrid('getRows')[rowIndex];
	igdfli_clear();
	$('#igdfli_name').val(row.igdName);
	$('#igdfli_table').datagrid('load');
}
