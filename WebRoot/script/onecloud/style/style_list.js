var stli_data = {};

$('#stli_table').datagrid({
	url: 'style_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#stli_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"stli_name","type":"text"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "name", title: "名称", width: 80, align: 'center'},
       {field: 'horNum', title: '横数', width: 60, align: 'center'},
       {field: 'horInterval', title: '横间隔', width: 60, align: 'center'},
       {field: 'verNum', title: '竖数', width: 60, align: 'center'},
       {field: 'verInterval', title: '竖间隔', width: 60, align: 'center'},
       {field: 'insideWidth', title: '服务器宽(px)', width: 60, align: 'center'},
       {field: 'insideHeight', title: '服务器高(px)', width: 60, align: 'center'},
	   {field: 'action', title: '操作', width: 60, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="stli_editrow(' + index + ')">编辑</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="stli_remove([' + row.id + '])">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
	]]
});

/**
 * 添加窗口
 */
$('#stli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	width: 320,
	height: 340,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			//name
			var name = $('#stli_addname').val();
			if(name.length == 0) {
				index_mess("请输入名称！", 4);
				$('#stli_addname').focus();
				return false;
			}
			if(name.length > 50) {
				index_mess("字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
				$('#stli_addname').focus();
				return false;
			}
			params['style.name'] = name;
			//name -end
			params['style.horNum'] = $('#stli_addhorNum').numberbox('getValue');
			if(! params['style.horNum'].match(/^\d.*$/)) {
				params['style.horNum'] = 0;
			}
			params['style.horInterval'] = $('#stli_addhorInterval').numberbox('getValue');
			if(! params['style.horInterval'].match(/^\d.*$/)) {
				params['style.horInterval'] = 0;
			}
			params['style.verNum'] = $('#stli_addverNum').numberbox('getValue');
			if(! params['style.verNum'].match(/^\d.*$/)) {
				params['style.verNum'] = 0;
			}
			params['style.verInterval'] = $('#stli_addverInterval').numberbox('getValue');
			if(! params['style.verInterval'].match(/^\d.*$/)) {
				params['style.verInterval'] = 0;
			}
			params['style.insideWidth'] = $('#stli_addinsideWidth').numberbox('getValue');
			if(! params['style.insideWidth'].match(/^\d.*$/)) {
				params['style.insideWidth'] = 0;
			}
			params['style.insideHeight'] = $('#stli_addinsideHeight').numberbox('getValue');
			if(! params['style.insideHeight'].match(/^\d.*$/)) {
				params['style.insideHeight'] = 0;
			}
			
			var url = '';
			if(stli_data.change) {
				params['style.id'] = stli_data.id;
				url = "style_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "style_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#stli_add').dialog('close');
				if(stli_data.change) {
					$('#stli_table').datagrid('reload');
				} else {
					$('#stli_table').datagrid('loadLast');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#stli_add').dialog('close');
		}
	}], onOpen: function() {
		if(!stli_data.change) {
			stli_changeValues({});
		}
	},
});

/**
 * 更新添加页面的值
 */
function stli_changeValues(row) {
	$('#stli_addname').val(row.name);
	$('#stli_addhorNum').numberbox('setValue', row.horNum);
	$('#stli_addhorInterval').numberbox('setValue', row.horInterval);
	$('#stli_addverNum').numberbox('setValue', row.verNum);
	$('#stli_addverInterval').numberbox('setValue', row.verInterval);
	$('#stli_addinsideWidth').numberbox('setValue', row.insideWidth);
	$('#stli_addinsideHeight').numberbox('setValue', row.insideHeight);
}
/**
 * 批量删除
 */
function stli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#stli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除样式！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个样式吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("style_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#stli_table').datagrid('reload');
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
function stli_opench(row) {
	stli_data.id = row.id;
	stli_data.change = true;
	$('#stli_add').dialog('open').dialog('setTitle','更新样式 - ' + row.name);
	stli_changeValues(row);
}
/**
 * 编辑
 */
function stli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#stli_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个样式！", 4);
			return;
		}
	} else {
		row = $('#stli_table').datagrid('getRows')[rowId];
	}
	stli_opench(row);
}
/**
 * 清除搜索数据
 */
function stli_clearData() {
	$('#stli_name').val('');
}

