var menuRole_aTree = $.fn.zTree.init($("#menuRole_aTree"), {
	data : {
		simpleData : {
			enable : true
		}
	}, callback: {
		beforeClick: function(event, treeNode) {
			if(menuRole_chk()) {
				$.messager.confirm('提示','要保存修改吗?',function(r){
					if(r) {
						menuRole_change(true);
					} else {
						menuRole_onSelect(treeNode);
					}
				});
			} else {
				menuRole_onSelect(treeNode);
			}
			return false;
		}
	}
}, menuRole_data.auths);
function menuRole_onSelect(treeNode) {
	menuRole_aTree.selectNode(treeNode);
	$("#menuRole_crole").html(treeNode.name);
	var rids = menuRole_data.a_rid[treeNode.id];
	menuRole_rTree.checkAllNodes(false);
	if(rids != null) {
		$.each(rids, function(k, rid) {
			menuRole_rTree.checkNode(menuRole_rTree.getNodeByParam("id", rid), true);
		});
	}
}
var menuRole_rTree = $.fn.zTree.init($("#menuRole_rTree"), {
	check: {
		enable: true,
		chkboxType: {"Y": "", "N": ""}
	}, data : {
		simpleData : {
			enable : true
		}
	}
}, menuRole_data.roles);
menuRole_data.a_rid = {};
$.each(menuRole_data.ro_aus, function(key, menuRole) {
	if(menuRole_data.a_rid[menuRole.aid] == null) {
		menuRole_data.a_rid[menuRole.aid] = [menuRole.rid];
	} else {
		menuRole_data.a_rid[menuRole.aid].push(menuRole.rid);
	}
});
/**
 * 全选角色
 */
function menuRole_chkall() {
	var nodes = menuRole_rTree.getCheckedNodes(true);
	if(nodes.length == menuRole_data.roles.length) {
		menuRole_rTree.checkAllNodes(false);
	} else {
		menuRole_rTree.checkAllNodes(true);
	}
}
/**
 * 更新角色
 */
function menuRole_change(flag) {
	if(! flag) {
		var auth = menuRole_aTree.getSelectedNodes();
		if(auth.length == 0) {
			index_mess("未选择权限！", 4);
			return;
		}
		if(!menuRole_chk(auth)) {
			index_mess("未更新！", 4);
			return;
		}
	}
	index_mess("更新中...", 0);
	$.get("menu_changeRole_js.action?json=" + menuRole_data.aid + "A" + menuRole_data.rids.join("a") +
			"&_=" + Math.random(), function(data) {
		if(data.status == 0) {
			menuRole_data.a_rid[menuRole_data.aid] = menuRole_data.rids;
			index_mess("更新成功！", 2);
		} else {
			index_mess(data.mess, 1);
			if(data.login == false)
				index_login();
		}
	}, "json");
}
/**
 * 检查角色是否更改
 * @returns {Boolean} true 有更新 false 未更新
 */
function menuRole_chk(selectAuth) {
	var auth;
	if(selectAuth == null) {
		auth = menuRole_aTree.getSelectedNodes();
	} else {
		auth = selectAuth;
	}
	if(auth.length == 0) {
		return false;
	}
	var rids = menuRole_data.a_rid[auth[0].id];
	if(rids == null) {
		rids = new Array();
	}
	menuRole_data.aid = auth[0].id;
	menuRole_data.rids = new Array();
	var nodes = menuRole_rTree.getCheckedNodes(true);
	$.each(nodes, function(k, node) {
		menuRole_data.rids.push(node.id);
	});
	if(rids.length != nodes.length) {
		return true;
	}
	var ischange = false;
	$.each(rids, function(k, rid) {
		ischange = true;
		$.each(nodes, function(k, node) {
			if(rid == node.id) {
				ischange = false;
				return false;
			}
		});
		if(ischange) {
			return false;
		}
	});
	return ischange;
}
