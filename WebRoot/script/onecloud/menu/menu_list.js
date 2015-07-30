var memuli_setting = {
	edit : {
		enable : true,
		showRenameBtn : false
	},
	check : {
		enable : true,
		nocheckInherit : true
	},
	data : {
		simpleData : {
			enable : true
		}
	}, view: {
		addHoverDom: memuli_addHoverDom,
		removeHoverDom: memuli_removeHoverDom
	
	}, callback: {
		beforeRemove: memuli_beforeRemove,
		onClick: memuli_onClick
	}
};

var memuli_treeObj = $.fn.zTree.init($("#memuli_treeDemo"), memuli_setting, memuli_zNodes);

function memuli_beforeRemove(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.selectNode(treeNode);
	if(treeNode.isParent) {
		index_mess("存在子菜单！", 3);
		return false;
	}
	var isSucc = false;
	if(confirm("确认删除《" + treeNode.name + "》吗？")) {
		index_mess("删除中...", 0);
		$.ajax({
			async: false,
			url: "menu_remove_js.action?_=" + Math.random(),
			data: {"menu.id": treeNode.id},
			dataType: "json",
			success: function(data) {
				if(data.status == "0") {
					$("#memuli_aid").val("");
					$("#memuli_atid").val("");
					$("#memuli_aname").val("");
					$("#memuli_aurl").val("");
					index_mess("删除成功！", 2);
					isSucc = true;
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			}
		});
	}
	return isSucc;
}
function memuli_onClick(event, treeId, treeNode) {
    var node = memuli_findNodeById(treeNode.id);
    $("#memuli_aid").val(node.id);
    $("#memuli_atid").val(treeNode.tId);
    $("#memuli_aname").val(node.name);
    $("#memuli_aurl").val(node.url);
}
function memuli_changeAuth() {
	index_mess("更新中...", 0);
	var aid = $("#memuli_aid").val();
	if(aid == "") {
		index_mess("未选择菜单！", 1);
		return;
	}
	var aname = $("#memuli_aname").val();
	if(aname == "") {
		index_mess("菜单名字为空！", 1);
		return;
	}
	var aurl = $("#memuli_aurl").val();
	if(aurl == "") {
		index_mess("菜单地址为空！", 1);
		return;
	}
	var paras = {"menu.id": aid, "menu.name": aname, "menu.url": aurl};
	$.post("menu_change_js.action?_=" + Math.random(), paras, function(data) {
		if(data.status == 0) {
			var node = memuli_treeObj.getNodeByTId($("#memuli_atid").val());
			node.name = aname;
			memuli_treeObj.updateNode(node);
			memuli_addHoverDom(0, node);
			var old = memuli_findNodeById(aid);
			old.name = aname;
			old.url = aurl;
			index_mess("更新成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
function memuli_addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#memuli_addBtn_" + treeNode.id).length > 0)
		return;
	var addStr = "<span class='button add' id='memuli_addBtn_"
		+ treeNode.id + "' title='增加' onfocus='this.blur();'></span>";
	sObj.append(addStr);
	var btn = $("#memuli_addBtn_" + treeNode.id);
	if (btn) {
		btn.bind("click", function() {
			$('#memuli_dd').dialog('open').dialog('setTitle', "添加菜单到《"+treeNode.name+"》下");
			$('#memuli_form').form('clear');
			memuli_addNode = treeNode;
		});
	}
};
function memuli_removeHoverDom(treeId, treeNode) {
	$("#memuli_addBtn_" + treeNode.id).unbind().remove();
};
var memuli_addNode;
/**
 * 打开添加到顶部框
 */
function memuli_addAuth() {
	$('#memuli_dd').dialog('open').dialog('setTitle','添加菜单到顶部');
	$('#memuli_form').form('clear');
	memuli_addNode = null;
}
/**
 * 更新菜单顺序
 */
function memuli_changeNodes() {
	index_mess("更新中...", 0);
	var nodes = memuli_treeObj.transformToArray(memuli_treeObj.getNodes());
	var newNodes = new Array();
	var zNodes = new Array();
	$.each(nodes, function(key, node) {
		var newNode = {"id": node.id, "pid": -1, "check": -1, "sort": -1};
		if(key<memuli_zNodes.length && node.id==memuli_zNodes[key].id) {
			if(memuli_zNodes[key].sort != key)
				newNode.sort = key;
			if(node.pId!=memuli_zNodes[key].pId) {
				if(node.pId == null)
					newNode.pid = 0;
				else
					newNode.pid = node.pId;
			}
			if(memuli_zNodes[key].checked) {
				if(!node.checked && !node.getCheckStatus().half) {
					newNode.check = 0;
				}
			} else {
				if(node.checked || node.getCheckStatus().half) {
					newNode.check = 1;
				}
			}
		} else {
			var oldNode = memuli_findNodeById(node.id);
			if(oldNode == null) {
				newNode.sort = key;
				newNode.pid = node.pId;
				newNode.check = node.checked ? 1 : 0;
			} else {
				if(oldNode.sort != key)
					newNode.sort = key;
				if(node.pId != oldNode.pId) {
					if(node.pId == null)
						newNode.pid = 0;
					else
						newNode.pid = node.pId;
				}
				if(oldNode.checked) {
					if(!node.checked && !node.getCheckStatus().half) {
						newNode.check = 0;
					}
				} else {
					if(node.checked || node.getCheckStatus().half) {
						newNode.check = 1;
					}
				}
			}
		}
		newNodes.push(newNode);
		zNodes.push({
			id: node.id,
			name: node.name,
			url: node.url,
			sort: key,
			checked: (node.checked||node.getCheckStatus().half),
			pId: node.pId
		});
	});
	var str = "";
	$.each(newNodes, function(key, val) {
		if(val.pid!=-1 || val.check!=-1 || val.sort!=-1) {
			str = str + "menus=" + val.id + "_" + val.pid + "_" + val.check + "_" + val.sort + "&";
		}
	});
	if(str != "") {
		$.post("menu_changeMenu_js.action?_=" + Math.random(), str, function(data) {
			if(data.status == 0) {
				index_mess("更新成功！", 2);
				memuli_zNodes = zNodes;
			} else {
				index_mess(data.mess, 1);
				if(data.login == false)
					index_login();
			}
		}, "json");
	} else {
		index_mess("未更新！", 2);
	}
}
function memuli_findNodeById(id) {
	for(var i in memuli_zNodes) {
		if(memuli_zNodes[i].id == id) {
			return memuli_zNodes[i];
		}
	}
	return null;
}
var memuli_check = false;
function memuli_checkAll() {
	if(memuli_check) {
		memuli_treeObj.checkAllNodes(false);
		memuli_check = false;
	} else {
		memuli_treeObj.checkAllNodes(true);
		memuli_check = true;
	}
}

$('#memuli_dd').dialog({
	closed: true,
	buttons:[{
		text:'添加',
		iconCls:'icon-ok',
		handler:function(){
			if(!$("#memuli_form").form('validate')) {
				return;
			}
			index_mess("正在添加...", 0);
			var treeName = $("#memuli_name").val();
			var url = $("#memuli_url").val();
			var params; 
			if(memuli_addNode) {
				params = {
					"menu.name": treeName,
					"menu.url": url,
					"menu.sort": memuli_zNodes.length,
					"menu.parent.id": memuli_addNode.id
				};
			} else {
				params = {
					"menu.name": treeName,
					"menu.url": url,
					"menu.sort": memuli_zNodes.length
				};
			}
			$.post("menu_add_js.action?_=" + Math.random(), params, function(data) {
				if(data.status == 0) {
					var newNode = {
						id: data.id,
						name: treeName,
						url: url,
						sort: memuli_zNodes.length,
						checked: true
					};
					if(memuli_addNode) {
						//刚添加必须将isParent设置为false，否则会出现添加两个叶子节点的现象
						newNode.pId = memuli_addNode.id;
						newNode.isParent = false;
						memuli_treeObj.addNodes(memuli_addNode, newNode);
						memuli_zNodes.push(newNode);
					} else {
						memuli_treeObj.addNodes(null, newNode);
						memuli_zNodes.push(newNode);
					}
					index_mess("添加成功！", 2);
					$('#memuli_dd').dialog('close');
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();
				}
			}, "json");
		}
	},{
		text:'&nbsp;取消&nbsp;',
		handler:function(){
			$('#memuli_dd').dialog('close');
		}
	}]
});