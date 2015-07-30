var svli_data = {};
$('#svli_table').datagrid({
	url: 'server_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    rownumbers: true,//显示行号
    pagination: true,//分页
    fitColumns: true,//自动调整单元格宽度
    toolbar: '#svli_toolbar',//工具条
    queryParams: {
    	"spage.ip": {"id":"svli_ip","type":"combobox"},
    	"spage.memory": {"id":"svli_memory","type":"text"},
    	"spage.operationName": {"id":"svli_operationName","type":"text"},
    	"spage.hostname": {"id":"svli_hostname","type":"text"},
    },
    paramsName: {
    	page: "spage.page",
    	num: "spage.num"
    },
	columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "ip", title: "IP", width:16, align: 'center',
    	   formatter: function(value, row, index) {
    		   return '<a href="javascript:;" onclick="svli_findIG(' + index + ')">' + value + '</a>';
       }},
       {field: "mac", title: "mac", width:18, align: 'center'},
       {field: "motherboard", title: "主板", width:18, align: 'center'},
       {field: "cpu", title: "cpu", width:30, align: 'center'},
       {field: "memory", title: "内存", width:5, align: 'center'},
       {field: "storage", title: "硬盘", width:8, align: 'center'},
       {field: "raid", title: "raid", width:5, align: 'center'},
       {field: "operationName", title: "操作系统", width:15, align: 'center'},
       {field: "defaultGateway", title: "网关", width:15, align: 'center'},
       {field: "openfile", title: "最大打开文件数", width:8, align: 'center'},
       {field: "hostname", title: "主机名", width:18, align: 'center'},
       {field: "cName", title: "机柜", width:10, align: 'center',
    	   formatter: function(value, row, index) {
    		   if(value != null) {
    			   return '<a href="javascript:;" onclick="svli_position(' + index + ')">' + value + '</a>&ensp;';
    		   } else {
    			   return '';
    		   }
			}},
       {field: 'action', title: '操作', width: 5, align: 'center',
			formatter: function(value, row, index) {
				return '<a href="javascript:;" onclick="svli_editrow(' + index + ')">编辑</a>';
			}}
	]], onLoadSuccess: function() {
		if(svli_data.reload) {
			svli_editrow(svli_data.rowId);
		}
		svli_data.reload = false;
	}
});
/**
 * 点击ip查看组件
 * @param index
 */
function svli_findIG(rowIndex) {
	if(! index_tabs.tabs('exists', '组件IP管理')) {
		index_openTab('组件IP管理', 'ingredient_list.action', true);
		setTimeout(function() {
			svli_openIG(rowIndex);
		}, 2000);
	} else {
		index_tabs.tabs('select', '组件IP管理');
		igli_clearData();
		svli_openIG(rowIndex);
	}
}
/**
 * 打开组件查询页面
 */
function svli_openIG(rowIndex) {
	var row = $('#svli_table').datagrid('getRows')[rowIndex];
	$('#igli_ip').combobox('setValue', row.ip);
	$('#igli_table').datagrid('load');
}
/**
 * 导出按钮
 */
svli_data.downloadMenu = $('#svli_export').menubutton({
	menu: '#svli_export_menu',
});
$(svli_data.downloadMenu.menubutton('options').menu).menu({
	onClick: function (item) { 
        if(item.text == "导出全部"){
        	var form = $("<form>");
            form.attr('style', 'display:none');
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', 'server_download.action');
            $('body').append(form);
            form.submit();
            form.remove();
        } else if(item.text == "导出当前"){
        	var form = $("<form>");
            form.attr('style', 'display:none');
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', 'server_download.action');
            form.append(svli_createInput('spage.ip', $('#svli_ip').combobox('getValue')));
            form.append(svli_createInput('spage.memory', $('#svli_memory').val()));
            form.append(svli_createInput('spage.operationName', $('#svli_operationName').val()));
            form.append(svli_createInput('spage.hostname', $('#svli_hostname').val()));
            $('body').append(form);
            form.submit();
            form.remove();
        }
    } 
});
/**
 * 生成input框
 */
function svli_createInput(name, value) {
	 var input = $('<input>');  
     input.attr('type', 'hidden');  
     input.attr('name', name);  
     input.attr('value', value);
     return input;
}
/**
 * 编辑窗口
 */
$('#svli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	//title: "添加组件",
	width: 550,
	height: 410,
	buttons: [{
		text: '调用nagios更新',
		iconCls: 'icon-reload',
		handler: function() {
			index_mess('更新中...', 0);
			$.post("server_updatedetails.action?server_id=" + svli_data.serverId, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					return false;
				}
				svli_data.reload = true;
				$('#svli_table').datagrid('reload');
				index_mess(data.mess, 2);
			}, "json");
		}
	}, {
		text: '保存输入',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			params['server.serverHW.id'] = svli_data.hwId;
			params['server.serverHW.hdServer.id'] = svli_data.serverId;
			params['server.serverHW.mac'] = $('#svli_addmac').val();
			params['server.serverHW.motherboard'] = $('#svli_addmotherboard').val();
			params['server.serverHW.cpu'] = $('#svli_addcpu').val();
			params['server.serverHW.memory'] = $('#svli_addmemory').val();
			params['server.serverHW.storage'] = $('#svli_addstorage').val();
			params['server.serverHW.raid'] = $('#svli_addraid').val();
			params['server.serverSW.id'] = svli_data.swId;
			params['server.serverSW.swServer.id'] = svli_data.serverId;
			params['server.serverSW.operationName'] = $('#svli_addoperationName').val();
			params['server.serverSW.defaultGateway'] = $('#svli_adddefaultGateway').val();
			params['server.serverSW.openfile'] = $('#svli_addopenfile').val();
			params['server.serverSW.hostname'] = $('#svli_addhostname').val();
			index_mess('更新中...', 0);
			$.post('server_changeHSW_js.action', params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					if(data.login == false) {
						index_login();
					}
					return false;
				}
				svli_data.reload = true;
				$('#svli_table').datagrid('reload');
				index_mess(data.mess, 2);
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#svli_add').dialog('close');
		}
	}]
});
/**
 * 打开更新页面
 */
function svli_editrow(rowId) {
	svli_data.rowId = rowId;
	var row = $('#svli_table').datagrid('getRows')[rowId];
	svli_data.serverId = row.id;
	svli_data.hwId = row.hwId;
	svli_data.swId = row.swId;
	$('#svli_add').dialog('open').dialog('setTitle','服务器信息 - ' + row.ip);
	$('#svli_addmac').val(row.mac);
	$('#svli_addmotherboard').val(row.motherboard);
	$('#svli_addcpu').val(row.cpu);
	$('#svli_addmemory').val(row.memory);
	$('#svli_addstorage').val(row.storage);
	$('#svli_addraid').val(row.raid);
	$('#svli_addoperationName').val(row.operationName);
	$('#svli_adddefaultGateway').val(row.defaultGateway);
	$('#svli_addopenfile').val(row.openfile);
	$('#svli_addhostname').val(row.hostname);
}
/**
 * 清除查询输入
 */
function svli_clear() {
	$('#svli_ip').combobox('setValue', '');
	$('#svli_memory').val('');
	$('#svli_operationName').val('');
	$('#svli_hostname').val('');
}
/**
 * 服务器位置窗口
 */
$('#svli_position').dialog({
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
			$.each($('#svli_sortable li'), function(k, v) {
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
			$('#svli_position').dialog('close');
		}
	}]
});
/**
 * 查看服务器位置
 */
function svli_position(rowIndex) {
	var row = $('#svli_table').datagrid('getRows')[rowIndex];
	$('#svli_position').dialog('open').dialog('setTitle','服务器位置 - ' + row.ip + '(' + row.cName + ')');
	$('#svli_posi_mess').html('<a href="javascript:;" onclick="svli_cab_position(' + rowIndex + ')">机柜位置</a>');
	svli_setPosition(row.cId, row.id);
}
/**
 * 在机柜上设置服务器位置
 */
function svli_setPosition(cabId, serverId) {
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
				str = str + '<li ' + (serverId==v.id?'class="highlight"':'') + ' value="' + v.id + '">' + v.ip + '</li>';
			} else {
				str = str + '<li></li>';
			}
		});
		$.each(cab_noSort, function(k, v) {
			str = str + '<li ' + (serverId==v.id?'class="highlight"':'') + ' value="' + v.id + '">' + v.ip + '</li>';
		});
		$('#svli_sortable').html(str);
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
		$('#svli_sortable').sortable(li_css);
		$('#svli_sortable li').css(li_css);
		$('#svli_sortable').css({
			'width': style.outWidth,
			'margin': style.space + 1
		});
		$('#svli_sortable_bg').css({
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
		$('#svli_sortable_bg').html(str);
	});
}
//这段代码与cablinet_list.js room_list.js相同，更改时需要同时更改
/**
 * 机柜位置窗口
 */
$('#svli_cab_position').dialog({
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
			$.each($('#svli_cab_sortable li'), function(k, v) {
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
			$('#svli_cab_position').dialog('close');
		}
	}]
});
/**
 * 查看机柜位置
 */
function svli_cab_position(rowIndex) {
	var row = $('#svli_table').datagrid('getRows')[rowIndex];
	$('#svli_cab_position').dialog('open').dialog('setTitle','机柜位置 - ' + row.cName + '(' + row.rName + ')');
	svli_cab_setPosition(row.rId, row.cId);
}
/**
 * 在机房上设置机柜位置
 */
function svli_cab_setPosition(roomId, cabId) {
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
		$('#svli_cab_sortable').html(str);
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
		$('#svli_cab_sortable').sortable(li_css);
		$('#svli_cab_sortable li').css(li_css);
		$('#svli_cab_sortable').css({
			'width': style.outWidth,
			'margin': style.space + 1
		});
		$('#svli_cab_sortable_bg').css({
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
		$('#svli_cab_sortable_bg').html(str);
	});
}
//end
/**
 * 设置移动位置的机柜
 */
$('#svli_chcabinet_table').datagrid({
	url: 'cabinet_list_js.action?_=' + Math.random(),
	singleSelect: true,
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#svli_chcabinet_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"svli_chcabinet_name","type":"text"},
    },
    pageSize: 5,
    pageList: [5, 10, 15, 20],
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", width: 10, align: 'center'},
       {field: "name", title: "机柜名称", width: 45, align: 'center'},
       {field: "rName", title: "所在机房", width: 45, align: 'center'}
	]]
});
/**
 * 为服务器更改机柜
 */
function svli_changeCabinet() {
	var servers = $('#svli_table').datagrid('getChecked');
	if(servers.length == 0) {
		index_mess("请先选择服务器！", 4);
		return;
	}
	var cabinets = $('#svli_chcabinet_table').datagrid('getChecked');
	if(cabinets.length == 0) {
		index_mess("请先选择机柜！", 4);
		return;
	}
	var sv_ids = new Array();
	$.each(servers, function(k, v) {
		sv_ids.push(v.id);
	});
	index_mess("移动中...", 0);
	$.getJSON("server_changeCabinet_js.action?json=" + cabinets[0].id + "A" + sv_ids.join('a') + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("移动成功！", 2);
			$('#svli_chcabinet').dialog('close');
			$('#svli_table').datagrid('reload');
		}
	});
}