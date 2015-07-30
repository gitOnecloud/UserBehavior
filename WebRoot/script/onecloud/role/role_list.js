var roli_zSetting = {
	edit: {
		enable: true,
		showRemoveBtn: false
	}, data: {
		simpleData: {
			enable: true
		}
	}, view: {
		dblClickExpand: false,
		addHoverDom: roli_addHoverDom,
		removeHoverDom: roli_removeHoverDom
	}, callback: {
		beforeRename: roli_beforeRename,
		onDblClick: function(event, treeId, treeNode) {
			index_updateTab("查看角色", "role_show.action?role.id=" + treeNode.id + "&", true);
		}
	}
};
var roli_treeObj = $.fn.zTree.init($("#roli_treeDemo"), roli_zSetting, roli_zNodes);

function roli_addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if(treeNode.editNameFlag || $("#roli_addBtn_" + treeNode.id).length > 0)
		return;
	var addStr = "<span id='roli_addBtn_" + treeNode.id + "' class='button add' title='增加'></span>";
	sObj.append(addStr);
	var btn = $("#roli_addBtn_" + treeNode.id);
	if(btn) {
		btn.bind("click", function() {
			roli_newRole(treeNode.id, treeNode.name);
		});
	}
};
function roli_removeHoverDom(treeId, treeNode) {
	$("#roli_addBtn_" + treeNode.id).unbind().remove();
}
function roli_beforeRename(treeId, treeNode, newName) {
	if (newName.length == 0) {
		index_mess("名称为空！", 4);
		roli_cancelEdit();
		return false;
	}
	if(treeNode.name == newName) {
		return true;
	}
	if(confirm("更改《" + treeNode.name + "》为《" + newName + "》吗？")) {
		index_mess("更改中...", 0);
		var isSucc = false;
		$.ajax({
			async: false,
			url: "role_changeN_js.action?_=" + Math.random(),
			data: {
				"role.id": treeNode.id,
				"role.name": newName
			}, type: "post", dataType: "json",
			success: function(data) {
				if(data.status == 0) {
					index_mess("更改成功！", 2);
					isSucc = true;
				} else {
					index_mess(data.mess, 1);
					if(data.login == false)
						index_login();				
				}
			}
		});
		if(isSucc) {
			return true;
		} else {
			roli_cancelEdit();
			return false;
		}
	} else {
		roli_cancelEdit();
		return false;
	}
}
function roli_cancelEdit() {
	setTimeout(function() {
		roli_treeObj.cancelEditName();
	}, 100);
}
/**
 * 添加角色
 * @param pId
 */
function roli_newRole(pId, name) {
	//直接修改role_addInput.jsp页面
	if(pId == null) {
		pId = 0;
		name = "无";
	}
	var str = pId + "_" + roli_zNodes.length + "_" + name;
	index_openTab("添加角色", "role_addInput.action?json=" + str + "&", true);
}
/**
 * 更新顺序
 */
function roli_changeNodes() {
	index_mess("更新中...", 0);
	var nodes = roli_treeObj.transformToArray(roli_treeObj.getNodes());
	var newNodes = new Array();
	var zNodes = new Array();
	var newNode;
	$.each(nodes, function(key, node) {
		newNode = {"id": node.id, "pId": -1, "sort": -1};
		if(key<roli_zNodes.length && node.id==roli_zNodes[key].id) {
			if(roli_zNodes[key].sort != key)
				newNode.sort = key;
			if(node.pId != roli_zNodes[key].pId) {
				if(node.pId == null)
					newNode.pId = 0;
				else
					newNode.pId = node.pId;
			}
		} else {
			var oldNode = roli_findNodeById(node.id);
			if(oldNode == null) {
				newNode.sort = key;
				newNode.pId = node.pId;
			} else {
				if(oldNode.sort != key)
					newNode.sort = key;
				if(node.pId != oldNode.pId) {
					if(node.pId == null)
						newNode.pId = 0;
					else
						newNode.pId = node.pId;
				}
			}
		}
		newNodes.push(newNode);
		zNodes.push({
			id: node.id,
			name: node.name,
			sort: key,
			pId: node.pId
		});
	});
	var str = "";
	$.each(newNodes, function(key, val) {
		if(val.pId!=-1 || val.sort!=-1) {
			str = str + "roles=" + val.id + "_" + val.pId + "_" + val.sort + "&";
		}
	});
	if(str != "") {
		$.post("role_changeSort_js.action?_=" + Math.random(), str, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false)
					index_login();
			} else {
				index_mess("更新成功！", 2);
				roli_zNodes = zNodes;
			}
		}, "json");
	} else {
		index_mess("未更新！", 2);
	}
}
function roli_findNodeById(id) {
	for(var i in roli_zNodes) {
		if(roli_zNodes[i].id == id) {
			return roli_zNodes[i];
		}
	}
	return null;
}