
$('#cbli_table').datagrid({
	url: 'cabinet_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#cbli_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"cbli_name","type":"text"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "name", title: "名称", width: 80, align: 'center'},
       {field: "rName", title: "机房", width: 80, align: 'center'},
       {field: "sName", title: "样式", width: 80, align: 'center',
	       formatter: function(value, row, index) {
	    	   return '<a href="javascript:;" onclick="cbli_sv_position(' + index + ')">' + value + '</a>';
	       }},
	   {field: "remark", title: "备注", width: 60, align: 'center'},
	   {field: 'action', title: '操作', width: 60, align: 'center',
			formatter: function(value, row, index) {
				return '<a href="javascript:;" onclick="cbli_position(' + index + ')">位置</a>&emsp;' +
				'<a href="javascript:;" onclick="cbli_editrow(' + index + ')">编辑</a>&emsp;' +
				'<a href="javascript:;" onclick="cbli_remove([' + row.id + '])">删除</a>';
			}}
	]]
});

/**
 * 设置机柜样式下拉框
 */
$('#cbli_addstyle').combobox({
	valueField: 'id',
	textField: 'name',
	editable: false,
	width: 160,
	panelHeight: 138,
	data: cbli_data.styles
});
/**
 * 设置机房下拉框
 */
$('#cbli_addroom').combobox({
	valueField: 'id',
	textField: 'name',
	editable: false,
	width: 160,
	panelHeight: 138,
	data: cbli_data.rooms
});
/**
 * 设置移动位置的机房
 */
$('#cbli_room').tree({
	data: cbli_data.rooms,
	onDblClick: function(node) {
		$('#cbli_room').tree('toggle', node.target);
	}
});
/**
 * 添加窗口
 */
$('#cbli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	width: 320,
	height: 280,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			//name
			var name = $('#cbli_addname').val();
			if(name.length == 0) {
				index_mess("请输入名称！", 4);
				$('#cbli_addname').focus();
				return false;
			}
			if(name.length > 50) {
				index_mess("字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
				$('#cbli_addname').focus();
				return false;
			}
			params['cabinet.name'] = name;
			//room
			var style = $('#cbli_addroom').combobox('getValue');
			if(! style.match(/^\d.*$/)) {
				index_mess('请先选择机房！', 4);
				$('#cbli_addroom').focus();
				return false;
			}
			params['cabinet.cabRoom.id'] = style;
			//style
			var style = $('#cbli_addstyle').combobox('getValue');
			if(! style.match(/^\d.*$/)) {
				index_mess('请先选择样式！', 4);
				$('#cbli_addstyle').focus();
				return false;
			}
			params['cabinet.cabStyle.id'] = style;
			//style - end
			params['cabinet.remark'] = $('#cbli_addRemark').val();
			var url = '';
			if(cbli_data.change) {
				params['cabinet.id'] = cbli_data.id;
				url = "cabinet_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "cabinet_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#cbli_add').dialog('close');
				if(cbli_data.change) {
					$('#cbli_table').datagrid('reload');
				} else {
					$('#cbli_table').datagrid('loadLast');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#cbli_add').dialog('close');
		}
	}], onOpen: function() {
		if(!cbli_data.change) {
			cbli_changeValues({});
		}
	},
});

/**
 * 更新添加页面的值
 */
function cbli_changeValues(row) {
	$('#cbli_addroom').combobox('setValue', row.rId);
	$('#cbli_addstyle').combobox('setValue', row.sId);
	$('#cbli_addname').val(row.name);
	$('#cbli_addRemark').val(row.remark);
}
/**
 * 批量删除
 */
function cbli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#cbli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除机柜！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个机柜吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("cabinet_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#cbli_table').datagrid('reload');
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
function cbli_opench(row) {
	cbli_data.id = row.id;
	cbli_data.change = true;
	$('#cbli_add').dialog('open').dialog('setTitle','更新机柜 - ' + row.name);
	cbli_changeValues(row);
}
/**
 * 编辑
 */
function cbli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#cbli_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个机柜！", 4);
			return;
		}
	} else {
		row = $('#cbli_table').datagrid('getRows')[rowId];
	}
	cbli_opench(row);
}
/**
 * 清除搜索数据
 */
function cbli_clearData() {
	$('#cbli_name').val('');
}
/**
 * 机柜位置窗口
 */
$('#cbli_position').dialog({
	closed: true,
	resizable: true,
	maximizable: true,
	//modal: true,锁定其余部分
	width: 800,
	height: 500,
	buttons: [{
		text: '保存顺序',
		iconCls: 'icon-ok',
		handler: function() {
			var cab_sort = new Array();
			$.each($('#cbli_sortable li'), function(k, v) {
				var id = $(this).attr('value');
				if(id != 0) {
					cab_sort.push(id + '_' + (k+1));
				}
			});
			index_mess('更新中...', 0);
			$.getJSON('cabinet_changeSort_js.action?json='+cab_sort.join('A'), function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return;
				}
				index_mess(data.mess, 2);
			});
		}
	}, {
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#cbli_position').dialog('close');
		}
	}]
});
/**
 * 查看机柜位置
 */
function cbli_position(rowIndex) {
	var row = $('#cbli_table').datagrid('getRows')[rowIndex];
	$('#cbli_position').dialog('open').dialog('setTitle','机柜位置 - ' + row.name + '(' + row.rName + ')');
	cbli_setPosition(row.rId, row.id);
}
/**
 * 在机房上设置机柜位置
 */
function cbli_setPosition(roomId, cabId) {
	index_mess('读取中...', 0);
	$.getJSON('room_detail_js.action?json='+roomId, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		var style = data.style;
		style.space = 6;
		//添加机柜
		var cab_sort = {};
		for(var i=1; i<=style.verNum*style.horNum; i++) {
			cab_sort[i] = {};
		}
		var cab_noSort = new Array();
		$.each(data.cabinets, function (k, v) {
			//不能排序，或者位置被占用
			if(cab_sort[v.sort]==null || cab_sort[v.sort]['id']!=null) {
				cab_noSort.push(v);
			} else {
				cab_sort[v.sort] = v;
			}
		});
		var str = '';
		$.each(cab_sort, function (k, v) {
			if(v.id != null) {
				str = str + '<li ' + (cabId==v.id?'class="highlight"':'') + ' value="' + v.id + '">' + v.name + '</li>';
			} else {
				str = str + '<li></li>';
			}
		});
		$.each(cab_noSort, function(k, v) {
			str = str + '<li ' + (cabId==v.id?'class="highlight"':'') + ' value="' + v.id + '">' + v.name + '</li>';
		});
		$('#cbli_sortable').html(str);
		//设置机柜大小和背景线条
		if(cab_noSort.length != 0) {
			//在有未排序的机柜情况下，增加行数
			style.horNum = style.horNum + cab_noSort.length/style.verNum;
			if(style.horNum*10%10 != 0) {//有小数点，代表存在一台，需要加一行
				style.horNum = parseInt(style.horNum) + 1;
			}
			//style.horNum = parseInt(style.horNum + cab_noSort.length/style.verNum) + 1;
			//style.horNum = ((style.horNum + cab_noSort.length/style.verNum) | 0) + 1;
			//style.horNum = ((style.horNum + cab_noSort.length/style.verNum - 0.5).toFixed(0))*1 + 1;
			//style.horNum = Math.round((style.horNum + cab_noSort.length/style.verNum) - 0.5) + 1;
		}
		style.width = style.insideWidth + style.space*2;
		style.height = style.insideHeight + style.space*2;
		style.outWidth = style.width * style.verNum;
		style.outHeight = style.height * style.horNum;
		var li_css = {
			'width': style.insideWidth + 'px',
			'height': style.insideHeight + 'px',
			'line-height': style.insideHeight + 'px',
			'margin': (style.space-1) + 'px'
		};
		$('#cbli_sortable').sortable(li_css);
		$('#cbli_sortable li').css(li_css);
		$('#cbli_sortable').css({
			'width': style.outWidth,
			'margin': style.space + 1
		});
		$('#cbli_sortable_bg').css({
			'width': style.outWidth + style.space*2,
			'height': style.outHeight + style.space*2,
		});
		var str = '';
		if(style.horInterval != 0) {
			for(var i=1; i<style.horNum/style.horInterval; i++) {
				str = str + '<div class="bottom_border" ' +
					'style="margin:' + style.space + 'px;height:' + (style.height*style.horInterval*i) + 'px;width:' + style.outWidth + 'px"></div>';
			}
		}
		if(style.verInterval != 0) {
			for(var i=1; i<style.verNum/style.verInterval; i++) {
				str = str + '<div class="right_border" ' +
					'style="margin:' + style.space + 'px;height:' + style.outHeight + 'px;width:' + (style.width*style.verInterval*i) + 'px"></div>';
			}
		}
		$('#cbli_sortable_bg').html(str);
	});
}
/**
 * 为机柜更改机房
 */
function cbli_changeRoom() {
	var cabinets = $('#cbli_table').datagrid("getChecked");
	if(cabinets.length == 0) {
		index_mess("请先选择机柜！", 4);
		return;
	}
	var room = $('#cbli_room').tree('getSelected');
	if(room == null) {
		index_mess("请先选择机房！", 4);
		return;
	}
	var cab_ids = new Array();
	$.each(cabinets, function(k, v) {
		cab_ids.push(v.id);
	});
	index_mess("移动中...", 0);
	$.getJSON("cabinet_changeRoom_js.action?json=" + room.id + "A" + cab_ids.join('a') + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("移动成功！", 2);
			$('#cbli_chroom').dialog('close');
			$('#cbli_table').datagrid('reload');
		}
	});
}
//服务器位置 - begin
/**
 * 服务器位置窗口
 */
$('#cbli_sv_position').dialog({
	closed: true,
	resizable: true,
	maximizable: true,
	//modal: true,锁定其余部分
	width: 600,
	height: 500,
	buttons: [{
		text: '保存顺序',
		iconCls: 'icon-ok',
		handler: function() {
			var cab_sort = new Array();
			$.each($('#cbli_sv_sortable li'), function(k, v) {
				var id = $(this).attr('value');
				if(id != 0) {
					cab_sort.push(id + '_' + (k+1));
				}
			});
			index_mess('更新中...', 0);
			$.getJSON('server_changeSort_js.action?json='+cab_sort.join('A'), function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return;
				}
				index_mess(data.mess, 2);
			});
		}
	}, {
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#cbli_sv_position').dialog('close');
		}
	}]
});
/**
 * 查看服务器位置
 */
function cbli_sv_position(rowIndex) {
	var row = $('#cbli_table').datagrid('getRows')[rowIndex];
	$('#cbli_sv_position').dialog('open').dialog('setTitle','服务器分布 - ' + row.name + '(' + row.rName + ')');
	cbli_sv_setPosition(row.id);
}
/**
 * 在机柜上设置服务器位置
 */
function cbli_sv_setPosition(cabId) {
	index_mess('读取中...', 0);
	$.getJSON('cabinet_detail_js.action?json='+cabId, function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			return;
		}
		index_mess('读取成功！', 2);
		var style = data.style;
		style.space = 6;
		//添加服务器
		var cab_sort = {};
		for(var i=1; i<=style.verNum*style.horNum; i++) {
			cab_sort[i] = {};
		}
		var cab_noSort = new Array();
		$.each(data.servers, function (k, v) {
			//不能排序，或者位置被占用
			if(cab_sort[v.sort]==null || cab_sort[v.sort]['id']!=null) {
				cab_noSort.push(v);
			} else {
				cab_sort[v.sort] = v;
			}
		});
		var str = '';
		$.each(cab_sort, function (k, v) {
			if(v.id != null) {
				str = str + '<li value="' + v.id + '">' + v.ip + '</li>';
			} else {
				str = str + '<li></li>';
			}
		});
		$.each(cab_noSort, function(k, v) {
			str = str + '<li value="' + v.id + '">' + v.ip + '</li>';
		});
		$('#cbli_sv_sortable').html(str);
		//设置机柜大小和背景线条
		if(cab_noSort.length != 0) {
			//在有未排序的机柜情况下，增加行数
			style.horNum = style.horNum + cab_noSort.length/style.verNum;
			if(style.horNum*10%10 != 0) {//有小数点，代表存在一台，需要加一行
				style.horNum = parseInt(style.horNum) + 1;
			}
		}
		style.width = style.insideWidth + style.space*2;
		style.height = style.insideHeight + style.space*2;
		style.outWidth = style.width * style.verNum;
		style.outHeight = style.height * style.horNum;
		var li_css = {
			'width': style.insideWidth + 'px',
			'height': style.insideHeight + 'px',
			'line-height': style.insideHeight + 'px',
			'margin': (style.space-1) + 'px'
		};
		$('#cbli_sv_sortable').sortable(li_css);
		$('#cbli_sv_sortable li').css(li_css);
		$('#cbli_sv_sortable').css({
			'width': style.outWidth,
			'margin': style.space + 1
		});
		$('#cbli_sv_sortable_bg').css({
			'width': style.outWidth + style.space*2,
			'height': style.outHeight + style.space*2,
		});
		var str = '';
		if(style.horInterval != 0) {
			for(var i=1; i<style.horNum/style.horInterval; i++) {
				str = str + '<div class="bottom_border" ' +
					'style="margin:' + style.space + 'px;height:' + (style.height*style.horInterval*i) + 'px;width:' + style.outWidth + 'px"></div>';
			}
		}
		if(style.verInterval != 0) {
			for(var i=1; i<style.verNum/style.verInterval; i++) {
				str = str + '<div class="right_border" ' +
					'style="margin:' + style.space + 'px;height:' + style.outHeight + 'px;width:' + (style.width*style.verInterval*i) + 'px"></div>';
			}
		}
		$('#cbli_sv_sortable_bg').html(str);
	});
}
//服务器位置 - end