<%@ page contentType="text/html; charset=UTF-8"  %>
<br />
<div id="hcli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="hcli_data.change=false;$('#hcli_add').dialog('open').dialog('setTitle','添加客户端');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="hcli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="hcli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		名称：<input id="hcli_name" class="igdli_input120"/>
		&ensp;域名：<input id="hcli_domain" class="igdli_input120"/>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#hcli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="hcli_clearData();">清除</a>
	</div>
</div>
<table id="hcli_table"></table>
<div id="hcli_add" style="padding:25px 20px;">
	<div>名称：<input id="hcli_addName" class="igdli_input260"/></div>
	<div style="padding-top:8px;">域名：<input id="hcli_addDomain" class="igdli_input260"/></div>
	<div style="padding-top:8px;">I&emsp;P：<textarea id='hcli_addIp' rows="2" cols="21"></textarea>
		<br />格式为 #127.0.0.1#192.168.0.101#</div>
</div>
<!-- 详情窗口 -->
<div id="hcli_detail" style="padding:25px 20px;font-size:16px;">
	<div id="hcli_detailName"></div>
	<div id="hcli_detailRsa"></div>
</div>
<br /><br />
