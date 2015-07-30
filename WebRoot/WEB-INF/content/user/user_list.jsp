<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var usli_RDs = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.双击用户可以进入修改；
		2.搜索时可以用“_”下划线代替一个未知字符。</div>
</div>

<div id="usli_toolbar" style="padding:5px;height:auto">
	<div>
		<a href="javascript:;" class="easyui-linkbutton" onclick="usli_newUser()"
			data-options="iconCls:'icon-add',plain:true">添加用户</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="usli_editUser()"
			data-options="iconCls:'icon-edit',plain:true">修改用户</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="usli_removeUser()"
			data-options="iconCls:'icon-remove',plain:true">删除用户</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="usli_openChrole()"
			data-options="iconCls:'icon-edit',plain:true">更改角色</a>
	</div>
	<div style="padding-top: 5px; padding-left: 4px;">
		名字: <input id="usli_name" style="height: 20px;line-height: 20px;width: 150px; margin-right: 8px;">
		账号: <input id="usli_account" style="height: 20px;line-height: 20px;width: 150px;">
		<a href="javascript:;" class="easyui-linkbutton" onclick="usli_search()"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>

<table id="usli_table"></table>

<div id="usli_dlg" class="easyui-dialog" style="width:420px;height:280px;padding:10px 20px;overflow:hidden;"
	data-options="closed:true,buttons:'#usli_dlg-buttons',onClose:function(){usli_row.edit=0;}">
	<div class="usli_ftitle">用户 信息</div>
	<form id="usli_fm" method="post">
		<div class="usli_fitem">
			<label>名字:</label><input name="user.name" class="easyui-validatebox" data-options="required:true">
		</div>
		<div class="usli_fitem">
			<label>账号:</label><input name="user.account" class="easyui-validatebox" data-options="required:true">
			<span id="usli_ac_span"></span>
		</div>
		<div class="usli_fitem">
			<label>密码:</label><input name="user.password" type="password">
			<span id="usli_pw_span"></span>
		</div>
		<div class="usli_fitem">
			<label>角色:</label><select id="usli_roles" name="roleId" style="width:200px;"></select>
		</div>
	</form>
</div>
<div id="usli_dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton" onclick="usli_saveUser()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#usli_dlg').dialog('close');"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<div id="usli_chrole" title="为选中用户更改角色" class="easyui-dialog"
	style="width:460px;height:300px;padding:10px 20px;"
	data-options="closed:true,buttons:'#usli_chrole_btn'">
	<div class="usli_ftitle">
		<select id="usli_chtype" class="easyui-combobox" style="width: 52px;"
				data-options="editable: false, panelHeight: 72">
			<option value="1">替换</option>
			<option value="2">添加</option>
			<option value="3">删除</option>
		</select>
		<span>&ensp;角色</span>
	</div>
	<ul id="usli_role" style="margin-bottom: 18px;"></ul>
</div>
<div id="usli_chrole_btn">
	<a href="javascript:;" class="easyui-linkbutton" onclick="usli_chrole()"
		data-options="iconCls:'icon-ok'">更改</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#usli_chrole').dialog('close');"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>