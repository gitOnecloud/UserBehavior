<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var rmli_data = <s:property value="json" escapeHtml="false"/>;
</script>
<br />
<div id="rmli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="rmli_data.change=false;$('#rmli_add').dialog('open').dialog('setTitle','添加机房');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="rmli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="rmli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		名称：<input id="rmli_name" class="igdli_input120"/>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#rmli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="rmli_clearData();">清除</a>
	</div>
</div>
<table id="rmli_table"></table>
<div id="rmli_add" style="padding:25px 20px;">
	<div>名称：<input id="rmli_addname" class="igdli_input160"/></div>
	<div style="padding-top:8px;">样式：<select id="rmli_addstyle"></select></div>
	<div style="padding-top:8px;">备注：<textarea id='rmli_addRemark' rows="2" cols="20"></textarea></div>
</div>
<!-- 机柜位置 -->
<div id="rmli_position" style="padding:20px;">
	<div style="padding-bottom: 8px;">
		<span>图中的方向为面对机柜正面时的方向</span>
	</div>
	<div class="sortable_div">
		<!-- 背景 -->
		<div id ="rmli_sortable_bg" class="sortable_bg"></div>
		<ul id="rmli_sortable" class="sortable"></ul>
	</div>
</div>
<br /><br />
