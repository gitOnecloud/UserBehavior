/**
 * idName 用来保存id和name的索引
 */
var igdfli_data = {'idName': {}};
var igdfli_change = true;//是否打开修改页面
var igdfli_id = 0;//要更新的组件id

$('#igdfli_table').datagrid({
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#igdfli_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"igdfli_name","type":"text"},
    	"page.port": {"id":"igdfli_port","type":"text"},
    	"page.type": {"id":"igdfli_type","type":"combobox"},
    	"page.register": {"id":"igdfli_register","type":"combobox"}
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "name", title: "组件名称", width: 80, align: 'center',
    	   formatter: function(value, row, index) {
    		   return '<a href="javascript:;" onclick="igdfli_findIG(' + index + ')">' + value + '</a>';
       }},
       {field: "port", title: "端口", width: 60, align: 'center'},
       {field: "pid", hidden: true},
       {field: "pname", title: "父组件", width: 80, align: 'center'},
       {field: "type", title: "类型", width: 60, align: 'center'},
       {field: "register", title: "注册组件", width: 40, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '是';
    		   } else {
    			   return '';
    		   }
       }},
       {field: "color", title: "颜色", width: 30, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   return '<span style="background-color:#' + value + ';padding:3px 5px">#' + value + '</span>';
       }},
	   {field: 'action', title: '操作', width: 70, align: 'center',
			formatter: function(value, row, index) {
				return '<a href="javascript:;" onclick="igdfli_children(' + index + ')">子组件</a>&nbsp;&nbsp;' +
				'<a href="javascript:;" onclick="igdfli_editrow(' + index + ')">编辑</a>&nbsp;&nbsp;' +
				'<a href="javascript:;" onclick="igdfli_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
			}}
	]]
});
/**
 * 添加窗口
 */
$('#igdfli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	title: "添加组件",
	width: 400,
	height: 270,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			var name = $('#igdfli_addname').val();
			if(name.length == 0) {
				index_mess("请输入组件名称！", 4);
				$('#igdfli_addname').focus();
				return false;
			}
			if(name.length > 50) {
				index_mess("字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
				$('#igdfli_addname').focus();
				return false;
			}
			params['igdefine.name'] = name;
			
			var pid = $('#igdfli_addparent').combobox('getText');
			if(pid != '') {
				pid = igdfli_data.idName[pid];
				if(pid == null) {
					index_mess('父组件选择有误！', 4);
					$('#igdfli_addparent').focus();
					return false;
				} else {
					params['igdefine.parent.id'] = pid;
				}
			}
			params['igdefine.port'] = $('#igdfli_addport').val();
			params['igdefine.type'] = $('#igdfli_addtype').combobox('getValue');
			params['igdefine.color'] = $('#igdfli_addcolor').combobox('getValue');
			params['igdefine.register'] = $('#igdfli_addregister input:first').is(':checked')? 1: 0;
			var url = '';
			if(igdfli_change) {
				params['igdefine.id'] = igdfli_id;
				url = "ingredientdefine_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "ingredientdefine_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				//igdfli_changeValues('', '', 'basket', 1, '', 'ffffff');
				$('#igdfli_add').dialog('close');
				if(igdfli_change) {
					$('#igdfli_table').datagrid('reload');
				} else {
					$('#igdfli_table').datagrid('loadLast');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#igdfli_add').dialog('close');
		}
	}], onOpen: function() {
		if(!igdfli_change) {
			igdfli_changeValues('', '', 'basket', 1, '', 'ffffff');
		}
		$('#igdfli_addname').focus();
	},
});
/**
 * 添加窗口中的父组件
 */
$('#igdfli_addparent').combobox({
	/*remotePName: 'json',
	mode: 'remote',
	url: 'ingredientdefine_names_js.action',*/
	onChange: function(newValue, oldValue) {
		if(newValue==null || newValue=='' || newValue==oldValue) {
			return;
		}
		$.post("ingredientdefine_names_js.action", {'json': newValue}, function(data) {
			if(data.length != 0) {
				$('#igdfli_addparent').combobox('loadData', data);
				$.each(data, function(k, v) {
					igdfli_data.idName[v.text] = v.id;
				});
			}
		}, "json");
	}
});
/**
 * 颜色选择框
 */
$('#igdfli_addcolor').combobox({
	width: 102,
	panelHeight: 'auto',
	editable: false,
	data: [{
		text: 'ffffff',
		value: 'ffffff'
	},{
		text: '00ff00',
		value: '00ff00'
	},{
		text: 'c0c0c0',
		value: 'c0c0c0'
	},{
		text: 'ffcc00',
		value: 'ffcc00'
	},{
		text: 'ff0000',
		value: 'ff0000'
	},{
		text: '00ccff',
		value: '00ccff'
	},{
		text: '333333',
		value: '333333'
	}], formatter: function(row) {
		var s = '<span style="background-color:#' + row.value + ';padding:1px 4px;">' + row.text + '</span>';
		return s;
	}, onSelect: function(row) {
		$('#igdfli_addcolorspan').css('background-color', '#' + row.value); 
	}
	
});
/**
 * 更新添加页面的值
 */
function igdfli_changeValues(name, port, type, register, parent, color) {
	$('#igdfli_addname').val(name);
	if(port == 0) {
		port = '';
	}
	$('#igdfli_addport').val(port);
	
	$('#igdfli_addtype').combobox('setValue', type);
	if(register == 1) {
		$('#igdfli_addregister input:first').attr("checked",'true');
	} else {
		$('#igdfli_addregister input:eq(1)').attr("checked",'true');
	}
	$('#igdfli_addparent').combobox('setValue', parent);
	$('#igdfli_addcolor').combobox('setValue', color);
	$('#igdfli_addcolorspan').css('background-color', '#' + color); 
}
/**
 * 批量删除
 */
function igdfli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#igdfli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除组件！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个组件吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("ingredientdefine_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#igdfli_table').datagrid('reload');
		    		index_mess(data.mess, 2);
	    		} else {
	    			index_mess(data.mess, 1);
	    		}
	    	});
		}
	});
}
/**
 * 更新组件
 */
function igdfli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#igdfli_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个组件！", 4);
			return;
		}
	} else {
		row = $('#igdfli_table').datagrid('getRows')[rowId];
	}
	igdfli_id = row.id;
	igdfli_change = true;
	$('#igdfli_add').dialog('open').dialog('setTitle','更新组件 - ' + row.name);
	igdfli_changeValues(row.name, row.port, row.type, row.register, row.pname, row.color);
	$('#igdfli_addparent').combobox('reload');
}
/**
 * 根据组件id查询服务器
 */
function igdfli_findIG(rowIndex) {
	if(! index_tabs.tabs('exists', '组件IP管理')) {
		index_openTab('组件IP管理', 'ingredient_list.action', true);
		setTimeout(function() {
			igdfli_openIG(rowIndex);
		}, 2000);
	} else {
		index_tabs.tabs('select', '组件IP管理');
		igli_clearData();
		igdfli_openIG(rowIndex);
	}
}
/**
 * 打开组件查询页面
 */
function igdfli_openIG(rowIndex) {
	var row = $('#igdfli_table').datagrid('getRows')[rowIndex];
	$('#igli_igf').combogrid('setValue', row.id);
	$('#igli_name2').val(row.name);
	$('#igli_igf').combogrid('grid').datagrid('load');
	$('#igli_table').datagrid('load');
}
/**
 * 打开子组件
 * @param rowIndex
 */
function igdfli_children(rowIndex) {
	var row = $('#igdfli_table').datagrid('getRows')[rowIndex];
	$('#igdfli_children').dialog('open').dialog('setTitle', row.name + ' 组件查看');
	index_mess("读取中...", 0);
	$.getJSON('ingredientdefine_children_js.action?json=' + row.id, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			return false;
		}
		index_mess("读取成功！", 2);
		$('#igdfli_childTree').tree({
			data: index_changeTree(data),
			onDblClick: function(node) {
				igdfli_viewigd(node.text);
			}, onLoadSuccess: function(node, data) {
				var node = $('#igdfli_childTree').tree('find', row.id);
				$('#igdfli_childTree').tree('select', node.target);
			}
		});
	});
}
/**
 * 查看组件
 */
function igdfli_viewigd(text) {
	if(text == null) {
		text = $('#igdfli_childTree').tree('getSelected').text;
	}
	igdfli_clear();
	$('#igdfli_name').val(text);
	$('#igdfli_table').datagrid('load');
}
/**
 * 清除查询输入
 */
function igdfli_clear() {
	$('#igdfli_name').val('');
	$('#igdfli_port').val('');
	$('#igdfli_type').combobox('setValue', '');
	$('#igdfli_register').combobox('setValue', '-1');
}