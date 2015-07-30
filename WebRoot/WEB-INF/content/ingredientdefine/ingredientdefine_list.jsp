<%@ page contentType="text/html; charset=UTF-8"  %>
<br />
<div id="igdfli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="igdfli_change=false;$('#igdfli_add').dialog('open').dialog('setTitle','添加组件');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="igdfli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="igdfli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		组件名称：<input id="igdfli_name" class="igdli_input160" />
		&ensp;端口：<input id="igdfli_port" class="igdli_input100" />
		&ensp;类型：<select id="igdfli_type" class="easyui-combobox"
			data-options="width:86, panelHeight:96,editable:false">
			<option value="">全部</option>
			<option value="basket">basket</option>
			<option value="satellite">satellite</option>
			<option value="rouyer">rouyer</option>
		</select>
		&ensp;注册组件：<select id="igdfli_register" class="easyui-combobox"
			data-options="width:56, panelHeight:76,editable:false">
			<option value="-1">全部</option>
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="$('#igdfli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="igdfli_clear()">清除</a>
	</div>
</div>
<table id="igdfli_table"></table>
<div id="igdfli_add" style="padding:12px;">
	<div>名称：<input id="igdfli_addname" class="igdli_input200"/></div>
	<div style="padding-top:8px;">端口：<input id="igdfli_addport" class="igdli_input120" /></div>
	<div style="padding-top:8px;">类型：<select id="igdfli_addtype"
		class="easyui-combobox"
		data-options="width:102, panelHeight:80,editable:false">
		<option value="basket">basket</option>
		<option value="satellite">satellite</option>
		<option value="rouyer">rouyer</option>
		</select></div>
	<div id="igdfli_addregister" style="padding-top:8px;">注册组件：
	<input type="radio" name="igdfli_addtop" checked="checked">是&emsp;
	<input type="radio" name="igdfli_addtop">否</div>
	<div style="padding-top:8px;">颜色：<select id="igdfli_addcolor"></select>
		<span id="igdfli_addcolorspan">&emsp;&emsp;</span></div>
	<div style="padding-top:8px;">父组件：<select id="igdfli_addparent"
	class="easyui-combobox" data-options="width:186, panelHeight:138">
	</select></div>
</div>
<!-- 查看子组件 begin -->
<div id="igdfli_children" class="easyui-dialog"
	style="width:460px;height:300px;padding:10px 20px;"
	data-options="closed:true,buttons:'#igdfli_children_btn'">
	<ul id="igdfli_childTree" style="margin-bottom: 18px;"></ul>
</div>
<div id="igdfli_children_btn">
	<a href="javascript:;" class="easyui-linkbutton" onclick="igdfli_viewigd()"
		data-options="iconCls:'icon-ok'">查看</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#igdfli_children').dialog('close');"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<!-- end -->
<br /><br />
