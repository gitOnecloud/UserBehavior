<%@ page contentType="text/html; charset=UTF-8"  %>
<br />

<div id="vipli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="vipli_change=false;$('#vipli_add').dialog('open').dialog('setTitle','添加组件');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="vipli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="vipli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		环境：<select id="vipli_party" class="easyui-combobox"
			data-options="panelHeight:138,editable:false"></select>
		&ensp;组件：<input id="vipli_igd" />
		&ensp;VIP：<input id="vipli_vip" class="igdli_input120"/>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#vipli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="vipli_clearData();">清除</a>
	</div>
</div>
<table id="vipli_table"></table>
<div id="vipli_add" style="padding:12px;">
	<div>环境：<select id="vipli_addparty" class="easyui-combobox"
		data-options="width:160,panelHeight:138,editable:false"></select></div>
	<div style="padding-top:8px;">组件：<select id="vipli_addigdefine" 
		class="easyui-combobox" data-options="width:160,panelHeight:138">
		</select></div>
	<div style="padding-top:8px;">VIP：<input id="vipli_addvip" class="igdli_input160"/></div>
	<div style="padding-top:8px;">备注：<textarea id='vipli_addDesc' rows="2" cols="20"></textarea></div>
</div>
<br /><br />
