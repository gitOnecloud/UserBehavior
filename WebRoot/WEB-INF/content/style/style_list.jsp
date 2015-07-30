<%@ page contentType="text/html; charset=UTF-8"  %>
<br />
<div id="stli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="stli_data.change=false;$('#stli_add').dialog('open').dialog('setTitle','添加样式');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="stli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="stli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		名称：<input id="stli_name" class="igdli_input120"/>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#stli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="stli_clearData();">清除</a>
	</div>
</div>
<table id="stli_table"></table>
<div id="stli_add" style="padding:25px 20px;">
	<div>名&emsp;称：<input id="stli_addname" class="igdli_input160"/></div>
	<div style="padding-top:8px;">横&emsp;数：<input id="stli_addhorNum"
		class="igdli_input160 easyui-numberbox" data-options="min:0,precision:0"/></div>
	<div style="padding-top:8px;">横间隔：<input id="stli_addhorInterval"
		class="igdli_input160 easyui-numberbox" data-options="min:0,precision:0"/></div>
	<div style="padding-top:8px;">竖&emsp;数：<input id="stli_addverNum"
		class="igdli_input160 easyui-numberbox" data-options="min:0,precision:0"/></div>
	<div style="padding-top:8px;">竖间隔：<input id="stli_addverInterval"
		class="igdli_input160 easyui-numberbox" data-options="min:0,precision:0"/></div>
	<div style="padding-top:8px;">服务器宽：<input id="stli_addinsideWidth"
		class="igdli_input120 easyui-numberbox" data-options="min:0,precision:0"/> px</div>
	<div style="padding-top:8px;">服务器高：<input id="stli_addinsideHeight"
		class="igdli_input120 easyui-numberbox" data-options="min:0,precision:0"/> px</div>
</div>
<br /><br />
