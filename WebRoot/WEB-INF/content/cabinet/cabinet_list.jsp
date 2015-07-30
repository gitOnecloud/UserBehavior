<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var cbli_data = <s:property value="json" escapeHtml="false"/>;
</script>
<br />
<div id="cbli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="cbli_data.change=false;$('#cbli_add').dialog('open').dialog('setTitle','添加机柜');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="cbli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="cbli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#cbli_chroom').dialog('open');"
			data-options="'iconCls':'icon-edit','plain':true">更改机房</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		名称：<input id="cbli_name" class="igdli_input120"/>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#cbli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="cbli_clearData();">清除</a>
	</div>
</div>
<table id="cbli_table"></table>
<div id="cbli_add" style="padding:25px 20px;">
	<div>名称：<input id="cbli_addname" class="igdli_input160"/></div>
	<div style="padding-top:8px;">机房：<select id="cbli_addroom"></select></div>
	<div style="padding-top:8px;">样式：<select id="cbli_addstyle"></select></div>
	<div style="padding-top:8px;">备注：<textarea id='cbli_addRemark' rows="2" cols="20"></textarea></div>
</div>
<div id="cbli_chroom" title="移动选中机柜" class="easyui-dialog"
	style="width:460px;height:300px;padding:10px 20px;"
	data-options="closed:true,buttons:'#cbli_chroom_btn'">
	<div class="usli_ftitle">
		<span>请选择要移动到的机房</span>
	</div>
	<ul id="cbli_room" style="margin-bottom: 18px;"></ul>
</div>
<div id="cbli_chroom_btn">
	<a href="javascript:;" class="easyui-linkbutton" onclick="cbli_changeRoom()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#cbli_chroom').dialog('close');"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<!-- 机柜位置 -->
<div id="cbli_position" style="padding:20px;">
	<div style="padding-bottom: 8px;">
		<span>图中的方向为面对机柜正面时的方向</span>
	</div>
	<div class="sortable_div">
		<!-- 背景 -->
		<div id ="cbli_sortable_bg" class="sortable_bg"></div>
		<ul id="cbli_sortable" class="sortable"></ul>
	</div>
</div>
<!-- 服务器位置 -->
<div id="cbli_sv_position" style="padding:20px;">
	<div style="padding-bottom: 8px;">
		<span>图中的方向为面对服务器正面时的方向</span>
	</div>
	<div class="sortable_div">
		<!-- 背景 -->
		<div id ="cbli_sv_sortable_bg" class="sortable_bg"></div>
		<ul id="cbli_sv_sortable" class="sortable"></ul>
	</div>
</div>
<br /><br />
