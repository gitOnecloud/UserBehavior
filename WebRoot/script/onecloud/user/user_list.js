$('#usli_table').datagrid({
    url: 'user_list_js.action?_=' + Math.random(),
    striped: true,//条纹
    fitColumns: true,//自动调整单元格宽度
    rownumbers: true,//显示行号
    //singleSelect: true,//单选
    pagination: true,//分页
    toolbar: '#usli_toolbar',
    queryParams: {
    	"page.name": {"id":"usli_name","type":"text"},
    	"page.account": {"id":"usli_account","type":"text"},
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
        {field: 'ck', checkbox:true},
        {field: 'id', title: 'ID', width: 10, align: 'center'},
        {field: 'user.name', title: '名字', width: 20},
        {field: 'user.account', title: '账号', width: 20},
        {field: 'role', title: '角色', width: 25},
        {field: 'roleId', hidden: true},
    ]], onDblClickRow: function(index, data) {
    	usli_editUser(data);
    }
});
function usli_search() {
	$('#usli_table').datagrid("load");
}
var usli_url = "";
/**
 * 弹出添加用户窗
 */
function usli_newUser() {
	$("#usli_ac_span").html("");
	$("#usli_pw_span").html("为空，使用默认123456。");
	$('#usli_dlg').dialog('open').dialog('setTitle','添加用户');
	$('#usli_fm').form('clear');
	usli_url = 'user_save_js.action';
	//0:未更改 1:添加 2:修改
	usli_row.edit = 1;
}
var usli_row = {};//更改前row数据
/**
 * 弹出修改用户窗
 */
function usli_editUser(data) {
	if(data == null) usli_row = $('#usli_table').datagrid('getSelected');
	else usli_row = data;
	if (usli_row){
		$("#usli_ac_span").html("更改后，密码为空置123456。");
		$("#usli_pw_span").html("为空，不更改。");
		$('#usli_dlg').dialog('open').dialog('setTitle','修改用户');
		usli_row["user.password"] = "";
		$('#usli_fm').form('load',usli_row);
		usli_url = 'user_change_js.action?user.id='+usli_row.id;
		usli_row.edit = 2;
	} else {
		index_mess("请先选择", 4);
	}
}
function usli_removeUser() {
	if(usli_row && usli_row.edit==2) {
		index_mess("正在修改，不能删除！", 4);
		return;
	}
	var row =  $('#usli_table').datagrid('getSelected');
	if (row){
		$.messager.prompt('删除用户-' + row["user.name"] + '-确定', '请输入你的密码:', function(r){
			if (r){
				index_mess("删除中...", 0);
				$.post("user_remove_js.action", {"user.id":row.id,"user.password":r}, function(data) {
					if(data.status == 0) {
						index_mess("删除成功！", 2);
						$('#usli_table').datagrid('reload');
					} else {
						index_mess(data.mess, 1);
						if(data.login == false) {
							index_login();
						}
						if(data.password == false) {
							return false;
						}
					}
				}, "json");
			}
		},"password");
	} else {
		index_mess("请先选择用户！", 4);
	}
}
/**
 * 添加或更改用户到数据库
 */
function usli_saveUser() {
	if(!$("#usli_fm").form('validate')) {
		return;
	}
	//0:user.name 1:user.account 2:user.password 3-Last:roleId Last:depmId
	var params = $("#usli_fm").serializeArray();
	if(usli_url.substring(5, 6) == "c") {
		if(params[0].value == usli_row["user.name"]) {
			params[0].value = "";
		}
		if(params[1].value == usli_row["user.account"]) {
			if(params[2].value == "")
				params[1].value = "";
		}
		var rids = "", newrids = "";
		$.each(usli_row.roleId, function(k, v) {
			rids = rids + v + ",";
		});
		for(var i=3;i<params.length-1;i++) {
			newrids = newrids + params[i].value + ",";
		}
		if(rids == newrids) {
			if(params.length > 4)
				params[3].value = "-1";
			else
				params.push({"name": "roleId","value": "-1"});
		}
	}
	index_mess("操作中...", 0);
	$.post(usli_url, params, function(data) {
		if(data.status == 0) {
			$('#usli_dlg').dialog('close');
			if(usli_url == "user_save_js.action") {
				index_mess("添加成功！", 2);
			} else {
				index_mess("修改成功！", 2);
			}
			$('#usli_table').datagrid('reload');
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			if(data.account == false) {
				$("#usli_ac_span").html(data.mess);
			}
		}
	}, "json");
}
setTimeout(function() {
	//角色下拉多选框
	$('#usli_roles').combobox({
		valueField: 'id',
		textField: 'name',
		data: usli_RDs.roles,
		editable: false,
		multiple: true,
		formatter: function(row){
			return '<img class="usli_item" src="script/easyui/themes/icons/role.png"/><span class="usli_item">'+row.name+'</span>';
		}
	});
	//更改角色树
	$('#usli_role').tree({
		checkbox: true,
		cascadeCheck: false,
		data: index_changeTree(usli_RDs.roles),
		onClick: function(node) {
			$('#usli_role').tree(node.checked? 'uncheck': 'check', node.target);
		}, onDblClick: function(node) {
			$('#usli_role').tree('toggle', node.target);
		}
	});
}, 500);
/**
 * 打开更改角色对话框
 */
function usli_openChrole() {
	$('#usli_chrole').dialog('open');
}
/**
 * 为用户更改角色
 */
function usli_chrole() {
	var users = $('#usli_table').datagrid("getChecked");
	if(users.length == 0) {
		index_mess("请先选择用户！", 4);
		return;
	}
	var roles = $('#usli_role').tree('getChecked');
	if(roles.length == 0) {
		index_mess("请先选择角色！", 4);
		return;
	}
	var param = $('#usli_chtype').combobox('getValue') + "A";
	$.each(roles, function(k, role) {
		param = param + role.id + "a";
	});
	param = param + "A";
	$.each(users, function(k, user) {
		param = param + user.id + "a";
	});
	index_mess("更改中...", 0);
	$.getJSON("user_changeRole_js.action?json=" + param + "&_=" + Math.random(), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
		} else {
			index_mess("更改成功！", 2);
			$('#usli_chrole').dialog('close');
			$('#usli_table').datagrid('reload');
		}
	});
}
