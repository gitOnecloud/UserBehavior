<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var menuRole_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.把权限赋予对应角色。
	</div>
</div>
<div class="menuRole_body">
	<div class="menuRole_choose">
		<span>当前权限：<b id="menuRole_crole">未选择</b></span>
	</div>
	<div class="menuRole_aTree">
		<ul id="menuRole_aTree" class="ztree"></ul>
	</div>
	<div class="menuRole_center">
		<span>&lArr; 权限<br >&emsp; 角色 &rArr;</span>
	</div>
	<div class="menuRole_change">
		<a href="javascript:;" onclick="menuRole_change()"
			class="easyui-linkbutton" data-options="iconCls:'icon-edit'">更新</a>
		<a href="javascript:;" onclick="menuRole_chkall()" class="easyui-linkbutton" >全选</a>
	</div>
	<div class="menuRole_aTree menuRole_rTree">
		<ul id="menuRole_rTree" class="ztree"></ul>
	</div>
</div>
