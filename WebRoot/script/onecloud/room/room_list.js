
$('#rmli_table').datagrid({
	url: 'room_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#rmli_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"rmli_name","type":"text"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "name", title: "名称", width: 80, align: 'center'},
       {field: "sName", title: "样式", width: 80, align: 'center',
	       formatter: function(value, row, index) {
	    	   return '<a href="javascript:;" onclick="rmli_position(' + index + ')">' + value + '</a>';
	       }},
	   {field: "remark", title: "备注", width: 60, align: 'center'},
	   {field: 'action', title: '操作', width: 80, align: 'center',
			formatter: function(value, row, index) {
				return '<a href="javascript:;" onclick="rmli_download(' + index + ')">导出</a>&emsp;' +
					'<a href="javascript:;" onclick="rmli_editrow(' + index + ')">编辑</a>&emsp;' +
					'<a href="javascript:;" onclick="rmli_remove([' + row.id + '])">删除</a>';
			}}
	]]
});

/**
 * 设置机房样式下拉框
 */
$('#rmli_addstyle').combobox({
	valueField: 'id',
	textField: 'name',
	editable: false,
	width: 160,
	panelHeight: 138,
	data: rmli_data.styles
});;

/**
 * 添加窗口
 */
$('#rmli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	width: 320,
	height: 260,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			//name
			var name = $('#rmli_addname').val();
			if(name.length == 0) {
				index_mess("请输入名称！", 4);
				$('#rmli_addname').focus();
				return false;
			}
			if(name.length > 50) {
				index_mess("字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
				$('#rmli_addname').focus();
				return false;
			}
			params['room.name'] = name;
			//style
			var style = $('#rmli_addstyle').combobox('getValue');
			if(! style.match(/^\d.*$/)) {
				index_mess('请先选择样式！', 4);
				$('#rmli_addstyle').focus();
				return false;
			}
			params['room.cabStyle.id'] = style;
			//style - end
			params['room.remark'] = $('#rmli_addRemark').val();
			var url = '';
			if(rmli_data.change) {
				params['room.id'] = rmli_data.id;
				url = "room_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "room_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#rmli_add').dialog('close');
				if(rmli_data.change) {
					$('#rmli_table').datagrid('reload');
				} else {
					$('#rmli_table').datagrid('loadLast');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#rmli_add').dialog('close');
		}
	}], onOpen: function() {
		if(!rmli_data.change) {
			rmli_changeValues({});
		}
	},
});

/**
 * 更新添加页面的值
 */
function rmli_changeValues(row) {
	$('#rmli_addstyle').combobox('setValue', row.sId);
	$('#rmli_addname').val(row.name);
	$('#rmli_addRemark').val(row.remark);
}
/**
 * 批量删除
 */
function rmli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#rmli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除机房！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个机房吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("room_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#rmli_table').datagrid('reload');
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
function rmli_opench(row) {
	rmli_data.id = row.id;
	rmli_data.change = true;
	$('#rmli_add').dialog('open').dialog('setTitle','更新机房 - ' + row.name);
	rmli_changeValues(row);
}
/**
 * 编辑
 */
function rmli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#rmli_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个机房！", 4);
			return;
		}
	} else {
		row = $('#rmli_table').datagrid('getRows')[rowId];
	}
	rmli_opench(row);
}
/**
 * 清除搜索数据
 */
function rmli_clearData() {
	$('#rmli_name').val('');
}
/**
 * 跳转到组件管理页面
 */
function rmli_findStyle(rowIndex) {
	if(! index_tabs.tabs('exists', '组件管理')) {
		index_openTab('组件管理', 'ingredientdefine_list.action', true);
		setTimeout(function() {
			rmli_openIG(rowIndex);
		}, 2000);
	} else {
		index_tabs.tabs('select', '组件管理');
		igdfli_clear();
		rmli_openIG(rowIndex);
	}
}
/**
 * 打开组件查询页面
 */
function rmli_openIG(rowIndex) {
	var row = $('#rmli_table').datagrid('getRows')[rowIndex];
	igdfli_clear();
	$('#igdfli_name').val(row.igdName);
	$('#igdfli_table').datagrid('load');
}
/**
 * 机柜位置窗口
 */
$('#rmli_position').dialog({
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
			$.each($('#rmli_sortable li'), function(k, v) {
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
			$('#rmli_position').dialog('close');
		}
	}]
});
/**
 * 查看机柜位置
 */
function rmli_position(rowIndex) {
	var row = $('#rmli_table').datagrid('getRows')[rowIndex];
	$('#rmli_position').dialog('open').dialog('setTitle','机柜分布 - ' + row.name);
	rmli_setPosition(row.id);
}
/**
 * 在机房上设置机柜位置
 */
function rmli_setPosition(roomId) {
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
				str = str + '<li value="' + v.id + '">' + v.name + '</li>';
			} else {
				str = str + '<li></li>';
			}
		});
		$.each(cab_noSort, function(k, v) {
			str = str + '<li value="' + v.id + '">' + v.name + '</li>';
		});
		$('#rmli_sortable').html(str);
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
		$('#rmli_sortable').sortable(li_css);
		$('#rmli_sortable li').css(li_css);
		$('#rmli_sortable').css({
			'width': style.outWidth,
			'margin': style.space + 1
		});
		$('#rmli_sortable_bg').css({
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
		$('#rmli_sortable_bg').html(str);
	});
}
/**
 * 导出机房服务器excel
 */
function rmli_download(rowIndex) {
	var row = $('#rmli_table').datagrid('getRows')[rowIndex];
	var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', 'room_download_rd.action');
    form.append(rmli_createInput('json', row.id));
    $('body').append(form);
    form.submit();
    form.remove();
}
/**
 * 生成input框
 */
function rmli_createInput(name, value) {
	 var input = $('<input>');  
     input.attr('type', 'hidden');  
     input.attr('name', name);  
     input.attr('value', value);
     return input;
}