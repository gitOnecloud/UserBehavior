var partyli_data = {
	'idName': {},//用来保存id和name的索引
	'change': true,//是否打开修改页面
	'id': 0,//要更新的partyid
};
var partyli_change = true;//是否打开修改页面
var partyli_id = 0;//要更新的partyid

$('#partyli_table').treegrid({
	url: 'arena_list_js.action?_=' + Math.random(),
    //striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    singleSelect: false,//多选
    toolbar: '#partyli_toolbar',//工具条
    /*pagination: true,//分页
    queryParams: {
    	"page.name": {"id":"partyli_name","type":"text"},
    	"page.type": {"id":"partyli_type","type":"combobox"},
    	"page.domain": {"id":"partyli_domain","type":"text"},
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
	},*/
    idField: 'id',
    treeField: 'name',
    columns: [[
       /*{field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},*/
       {field: "name", title: "Name", width: 80},
       {field: "domain", title: "Domain", width: 80, align: 'center'},
       {field: "primary", title: "MainIDC", width: 40, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '是';
    		   } else {
    			   return '';
    		   }
       }},
       {field: "type", title: "Type", width: 80, align: 'center'},
	   {field: 'action', title: 'Action', width: 60, align: 'center',
			formatter: function(value, row, index) {
				var e = '<a href="javascript:;" onclick="partyli_editrow(\'' + row.id + '\')">编辑</a>&nbsp;&nbsp;';
				var d = '<a href="javascript:;" onclick="partyli_remove(\'' + row.id + '\')">删除</a>&nbsp;&nbsp;';
				return e + d;
			}}
	]], onLoadSuccess: function(row, data) {
		partyli_data.rouyer = [];
		partyli_data.satellite = [];
		partyli_data.basket = {};
		$.each(data.rows, function(k, v) {
			if(v.type == 'rouyer') {
				partyli_data.rouyer.push({
					'value': v.rId,
					'text': v.name
				});
			} else if(v.type == 'satellite') {
				partyli_data.satellite.push({
					'value': v.sId,
					'text': v.name
				});
			} else {
				if(partyli_data.basket[v.aId] == null) {
					partyli_data.basket[v.aId] = [v.sId];
				} else {
					partyli_data.basket[v.aId].push(v.sId);
				}
			}
		});
		$('#partyli_addrouye').combobox({
			width: 202,
			panelHeight: 'auto',
			editable: false,
			data: partyli_data.rouyer,
		});
		$('#partyli_addsatellite').combobox({
			width: 202,
			panelHeight: 'auto',
			editable: false,
			multiple: true,
			data: partyli_data.satellite,
		});
	}
});

/**
 * 添加窗口
 */
$('#partyli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	title: "添加环境",
	width: 400,
	height: 250,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			var name = $('#partyli_addname').val();
			if(name.length == 0) {
				index_mess("请输入环境名称！", 4);
				$('#partyli_addname').focus();
				return false;
			}
			if(name.length > 50) {
				index_mess("环境名称字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
				$('#partyli_addname').focus();
				return false;
			}
			params['arena.name'] = name;
			params['arena.type'] = $('#partyli_addtype').combobox('getValue');
			if(params['arena.type'] == 'satellite') {
				if(partyli_data.change) {
					params['satellite.id'] = partyli_data.sId;
					params['satellite.slArena.id'] = partyli_data.id;
				}
				params['satellite.slRouyer.id'] = $('#partyli_addrouye').combobox('getValue');
				if(params['satellite.slRouyer.id'] == '') {
					index_mess("请先选择Rouyer！", 4);
					return false;
				}
				name = $('#partyli_adddomain').val();
				if(name.length > 50) {
					index_mess("Domain字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
					$('#partyli_adddomain').focus();
					return false;
				} else {
					params['satellite.domain'] = name;
				}
				params['satellite.isPrimary'] = $('#partyli_addstatus input:first').is(':checked')? 1: 2;
			} else if(params['arena.type'] == 'basket') {
				params['json'] = $('#partyli_addsatellite').combobox('getValues').join('-');
				if(params['json'] == '') {
					index_mess("请先选择Satellite！", 4);
					return false;
				}
			}
			
			var url = '';
			if(partyli_data.change) {
				params['arena.id'] = partyli_data.id;
				url = "arena_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "arena_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#partyli_add').dialog('close');
				$('#partyli_table').treegrid('reload');
			}, 'json');
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#partyli_add').dialog('close');
		}
	}]
});
/**
 * 环境类型
 */
$('#partyli_addtype').combobox({
	width: 102,
	panelHeight: 80,
	editable:false,
	onSelect: function(row) {
		partyli_setaddtype(row.value);
	}
});
/**
 * 设置添加选择框
 */
function partyli_setaddtype(type) {
	if(type == 'satellite') {
		$('#partyli_addrouye').parent().show();
		$('#partyli_adddomain').parent().show();
		$('#partyli_addstatus').show();
	} else {
		$('#partyli_addrouye').parent().hide();
		$('#partyli_adddomain').parent().hide();
		$('#partyli_addstatus').hide();
	}
	if(type == 'basket') {
		$('#partyli_addsatellite').parent().show();
	} else {
		$('#partyli_addsatellite').parent().hide();
	}
}
/**
 * 更新添加页面的值
 */
function partyli_changeValues(row) {
	if(partyli_data.change) {
		$('#partyli_addtype').combobox('disable');
	} else {
		$('#partyli_addtype').combobox('enable');
		$('#partyli_addsatellite').combobox([]);
		$('#partyli_adddomain').val('');
	}
	$('#partyli_addtype').combobox('setValue', row.type);
	partyli_setaddtype(row.type);
	$('#partyli_addname').val(row.name);
	if(row.type == 'satellite') {
		partyli_data.sId = row.sId;
		$('#partyli_addrouye').combobox('setValue', row.rId);
		if(row.primary == 1) {
			$('#partyli_addstatus input:first').attr("checked",'true');
		} else {
			$('#partyli_addstatus input:eq(1)').attr("checked",'true');
		}
		$('#partyli_adddomain').val(row.domain);
	} else if(row.type == 'basket') {
		$('#partyli_addsatellite').combobox('setValues',
				partyli_data.basket[row.aId]);
	}
}
/**
 * 批量删除
 */
function partyli_remove(rowId) {
	var row = $('#partyli_table').treegrid('find', rowId);
	if(row.children != null) {
		if(row.type == 'satellite') {
			index_mess('存在' + row.children.length + '个Basket，不能删除！', 4);
			return false;
		} else if(row.type == 'rouyer') {
			index_mess('存在' + row.children.length + '个Satellite，不能删除！', 4);
			return false;
		}
	}
	$.messager.confirm('提示', '确定要删除 ' + row.name + ' 吗?', function(r){
		if (r){
			var param = {
				'arena.id': row.aId,
				'arena.type': row.type,
			};
			if(row.type == 'satellite') {
				param['json'] = row.sId;
			} else if(row.type == 'basket') {
				param['json'] = row.bId;
			} else {
				param['json'] = row.rId;
			}
			index_mess("删除中...", 0);
			$.getJSON("arena_remove_js.action?_=" + Math.random(), param, function(data) {
				if(data.status == 0) {
					$('#partyli_table').treegrid('reload');
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
function partyli_opench(row) {
	partyli_data.id = row.aId;
	partyli_data.change = true;
	$('#partyli_add').dialog('open').dialog('setTitle','更新环境 - ' + row.name);
	partyli_changeValues(row);
}
/**
 * 编辑
 */
function partyli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#partyli_table').treegrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个环境！", 4);
			return;
		}
	} else {
		//row = $('#partyli_table').treegrid('getRows')[rowId];
		row = $('#partyli_table').treegrid('find', rowId);
	}
	partyli_opench(row);
}
/**
 * 清除搜索数据
 */
function partyli_clearData() {
	$('#partyli_name').val('');
	$('#partyli_domain').val('');
	$('#partyli_type').combobox('setValue', '');
}
/**
 * 打开添加窗口
 */
function partyli_openadd() {
	partyli_data.change = false;
	partyli_changeValues({
		type: 'satellite',
	});
	$('#partyli_add').dialog('open').dialog('setTitle','添加环境');
}
