var hcli_data = {};
$('#hcli_table').datagrid({
	url: 'hessianClient_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    pagination: true,//分页
    toolbar: '#hcli_toolbar',//工具条
    queryParams: {
    	"page.name": {"id":"hcli_name","type":"text"},
    	"page.domain": {"id":"hcli_domain","type":"text"},
    },
    paramsName: {
    	page: "page.page",
    	num: "page.num"
    },
    columns: [[
       {field: 'ck', checkbox:true},
	   {field: "id", title: "ID", align: 'center'},
       {field: "name", title: "名称", width: 60, align: 'center'},
       {field: "domain", title: "域名", width: 80, align: 'center',
	       formatter: function(value, row, index) {
	    	   return '<a href="' + value + '" target="_blank">' + value + '</a>';
	       }},
	   {field: "ip", title: "允许IP", width: 80, align: 'center'},
	   {field: 'action', title: '操作', width: 50, align: 'center',
			formatter: function(value, row, index) {
				return '<a href="javascript:;" onclick="hcli_detail(' + index + ')">详情</a>&emsp;' +
					'<a href="javascript:;" onclick="hcli_editrow(' + index + ')">编辑</a>&emsp;' +
					'<a href="javascript:;" onclick="hcli_remove([' + row.id + '])">删除</a>';
			}}
	]]
});

/**
 * 添加窗口
 */
$('#hcli_add').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	width: 400,
	height: 260,
	buttons: [{
		text: '保存',
		iconCls: 'icon-ok',
		handler: function() {
			var params = {};
			//name
			var name = $('#hcli_addName').val();
			if(name.length == 0) {
				index_mess("请输入名称！", 4);
				$('#hcli_addName').focus();
				return false;
			}
			if(name.length > 50) {
				index_mess("字数超出 " + (name.length - 50) + " 个！限制 50 个！", 4);
				$('#hcli_addName').focus();
				return false;
			}
			params['hessianClient.name'] = name;
			//domain
			var domain = $('#hcli_addDomain').val();
			if(domain.length == 0) {
				index_mess("请输入域名！", 4);
				$('#hcli_addDomain').focus();
				return false;
			}
			if(domain.length > 63) {
				index_mess("字数超出 " + (domain.length - 63) + " 个！限制 63 个！", 4);
				$('#hcli_addDomain').focus();
				return false;
			}
			params['hessianClient.domain'] = domain;
			//ip
			var ip = $('#hcli_addIp').val();
			if(ip.length == 0) {
				index_mess("请输入IP！", 4);
				$('#hcli_addIp').focus();
				return false;
			}
			if(ip.length > 255) {
				index_mess("字数超出 " + (ip.length - 255) + " 个！限制 255 个！", 4);
				$('#hcli_addIp').focus();
				return false;
			}
			params['hessianClient.ip'] = ip;
			//ip - end
			var url = '';
			if(hcli_data.change) {
				params['hessianClient.id'] = hcli_data.id;
				url = "hessianClient_change_js.action";
				index_mess("更新中...", 0);
			} else {
				url = "hessianClient_add_js.action";
				index_mess("添加中...", 0);
			}
			$.post(url, params, function(data) {
				if(data.status == 1) {
					index_mess(data.mess, 1);
					return false;
				}
				index_mess(data.mess, 2);
				$('#hcli_add').dialog('close');
				if(hcli_data.change) {
					$('#hcli_table').datagrid('reload');
				} else {
					$('#hcli_table').datagrid('loadLast');
				}
			}, "json");
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#hcli_add').dialog('close');
		}
	}], onOpen: function() {
		if(!hcli_data.change) {
			hcli_changeValues({});
		}
	},
});

/**
 * 更新添加页面的值
 */
function hcli_changeValues(row) {
	$('#hcli_addName').val(row.name);
	$('#hcli_addDomain').val(row.domain);
	$('#hcli_addIp').val(row.ip);
}
/**
 * 批量删除
 */
function hcli_remove(ids) {
	if(ids == null) {
		ids = new Array();
		var nps = $('#hcli_table').datagrid("getChecked");
		if(nps.length == 0) {
			index_mess("请先选择要删除客户端！", 4);
			return;
		}
		$.each(nps, function(k, np) {
			ids.push(np.id);
		});
	}
	$.messager.confirm('提示', '确定要删除选中的 ' + ids.length + ' 个客户端吗?', function(r){
		if (r){
			index_mess("删除中...", 0);
			$.getJSON("hessianClient_remove_js.action?json=" + ids.join('a') + "&_=" + Math.random(), function(data) {
				if(data.status == 0) {
					$('#hcli_table').datagrid('reload');
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
function hcli_opench(row) {
	hcli_data.id = row.id;
	hcli_data.change = true;
	$('#hcli_add').dialog('open').dialog('setTitle','更新客户端 - ' + row.name);
	hcli_changeValues(row);
}
/**
 * 编辑
 */
function hcli_editrow(rowId) {
	var row = null;
	if(rowId == null) {
		row = $('#hcli_table').datagrid("getSelected");
		if(row == null) {
			index_mess("请先选择一个客户端！", 4);
			return;
		}
	} else {
		row = $('#hcli_table').datagrid('getRows')[rowId];
	}
	hcli_opench(row);
}
/**
 * 清除搜索数据
 */
function hcli_clearData() {
	$('#hcli_name').val('');
	$('#hcli_domain').val('');
}
/**
 * 查看客户端详情
 */
function hcli_detail(rowId) {
	var row = $('#hcli_table').datagrid('getRows')[rowId];
	hcli_data.id = row.id;
	$('#hcli_detail').dialog('open').dialog('setTitle','客户端详情 - ' + row.name);
	$('#hcli_detailName').html('<b>名字：</b>' + row.name +
			'<br /><b>域名：</b>' + row.domain +
			'<br /><b>I&emsp;P：</b>' + row.ip);
	$('#hcli_detailRsa').html('<br /><b>公钥：</b>' + row.pubKey +
			'<br /><b>私钥：</b><br />' + row.priKey +
			'<br /><b>模数：</b><br />' + row.modKey + '<br />');
}
/**
 * 客户端详情窗口
 */
$('#hcli_detail').dialog({
	closed: true,
	resizable: true,
	//modal: true,锁定其余部分
	width: 460,
	height: 320,
	buttons: [{
		text: '更新RSA密钥',
		iconCls: 'icon-ok',
		handler: function() {
			$.messager.confirm('提示', '更新RSA密钥后，需要同步客户端。<br />是否要继续更新?', function(r){
				if (r){
					index_mess("更新中...", 0);
					$.getJSON("hessianClient_changeRSA_js.action?json=" + hcli_data.id + "&_=" + Math.random(), function(data) {
						if(data.status == 0) {
							$('#hcli_detailRsa').html('<br /><b>公钥：</b>' + data.pubKey +
								'<br /><b>私钥：</b><br />' + data.priKey +
								'<br /><b>模数：</b><br />' + data.modKey + '<br />');
							$('#hcli_table').datagrid('reload');
				    		index_mess(data.mess, 2);
			    		} else {
			    			index_mess(data.mess, 1);
			    		}
			    	});
				}
			});
		}
	},{
		text:'取消',
		iconCls: 'icon-cancel',
		handler: function(){
			$('#hcli_detail').dialog('close');
		}
	}], onOpen: function() {
		if(!hcli_data.change) {
			hcli_changeValues({});
		}
	},
});

