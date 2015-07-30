<%@ page contentType="text/html; charset=UTF-8"  %>
<br />

<div id="partyli_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="partyli_openadd()"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" onclick="partyli_editrow()"
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp;
		<!-- <a href="javascript:;" class="easyui-linkbutton" onclick="partyli_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp; -->
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#partyli_table').treegrid('reload')"
			data-options="'iconCls':'icon-reload','plain':true">刷新</a>&ensp;
		
	</div>
	<!-- <div style="padding: 5px 0 0 4px;">
		环境名称：<input id="partyli_name" class="igdli_input160"/>
		&ensp;域名后缀：<input id="partyli_domain" class="igdli_input160"/>
		&ensp;类型：<select id="partyli_type" class="easyui-combobox"
			data-options="width:86, panelHeight:96,editable:false">
			<option value="">全部</option>
			<option value="basket">basket</option>
			<option value="satellite">satellite</option>
			<option value="rouyer">rouyer</option>
		</select>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#partyli_table').treegrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="partyli_clearData();">清除</a>
	</div> -->
</div>
<table id="partyli_table"></table>
<div id="partyli_add" style="padding:12px;">
	<div class="arenali_adddiv">
		<label class="arenali_addlabel">环境类型：</label>
		<select id="partyli_addtype">
			<option value="satellite">satellite</option>
			<option value="basket">basket</option>
			<option value="rouyer">rouyer</option>
		</select></div>
	<div class="arenali_adddiv">
		<label class="arenali_addlabel">环境名称：</label>
		<input id="partyli_addname" class="igdli_input200"/></div>
	<div class="arenali_adddiv">
		<label class="arenali_addlabel">所属Rouyer：</label>
		<input id="partyli_addrouye" /></div>
	<div class="arenali_adddiv">
		<label class="arenali_addlabel">所属Satellite：</label>
		<input id="partyli_addsatellite" /> 多选</div>
	<div class="arenali_adddiv">
		<label class="arenali_addlabel">域名后缀：</label>
		<input id="partyli_adddomain" class="igdli_input200"/></div>
	<div id="partyli_addstatus" class="arenali_adddiv">
		<label class="arenali_addlabel">MainIDC：</label>
		<input type="radio" name="partyli_addstatus">是&emsp;
		<input type="radio" name="partyli_addstatus" checked="checked">否</div>
</div>
<br /><br />
