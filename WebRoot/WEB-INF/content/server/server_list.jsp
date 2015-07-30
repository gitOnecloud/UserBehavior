<%@ page contentType="text/html; charset=UTF-8"  %>
<br />
<div id="svli_toolbar" style="padding:5px;height:auto;">
	<div style="padding-top: 5px; padding-left: 4px;">
		IP段：<select id="svli_ip" name="server_ip" class="easyui-combobox" style="width:135px;"
				data-options="panelHeight:'auto',valueField:'id',textField:'text'">
				<option></option>
				<option>172.16.1.</option>
				<option>172.16.2.</option>
				<option>172.16.3.</option>
				<option>172.16.4.</option>
				<option>172.16.8.</option>
				<option>172.16.9.</option>
				<option>172.17.0.</option>
				<option>172.17.1.</option>
			</select>
		&ensp;内存：<input id="svli_memory" class="igdli_input100" />
		&ensp;操作系统：<input id="svli_operationName" class="igdli_input160" />
		&ensp;主机名：<input id="svli_hostname" class="igdli_input160" />
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#svli_table').datagrid('load');" data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="svli_clear()">清除</a>	
		<a href="javascript:;" class="easyui-menubutton" id="svli_export"
			data-options="plain:true, iconCls:'icon-download'">导出</a>&ensp;
		<a href="javascript:;" class="easyui-linkbutton" 
			onclick="$('#svli_chcabinet').dialog('open');"
			data-options="'iconCls':'icon-edit','plain':true">更改机柜</a>&ensp;
	</div>
</div>
<table id="svli_table"></table>
<!-- 导出下拉菜单 -->
<div id="svli_export_menu" style="width:100px;">  
	<div>导出当前</div>
    <div>导出全部</div>
</div>
<div id="svli_add" style="padding:12px;">
	<div class="svli_adddiv">
		<label class="svli_addlabel">MAC：</label>
		<input id="svli_addmac" class="igdli_input360"/></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">主板：</label>
		<input id="svli_addmotherboard" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">CPU：</label>
		<input id="svli_addcpu" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">内存：</label>
		<input id="svli_addmemory" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">硬盘：</label>
		<input id="svli_addstorage" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">RAID：</label>
		<input id="svli_addraid" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">操作系统：</label>
		<input id="svli_addoperationName" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">网关：</label>
		<input id="svli_adddefaultGateway" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">最大打开文件数：</label>
		<input id="svli_addopenfile" class="igdli_input360" /></div>
	<div class="svli_adddiv">
		<label class="svli_addlabel">主机名：</label>
		<input id="svli_addhostname" class="igdli_input360" /></div>
</div>
<!-- 服务器位置 -->
<div id="svli_position" style="padding:20px;">
	<div style="padding-bottom: 8px;">
		<span>图中的方向为面对服务器正面时的方向</span>
		<span id="svli_posi_mess"></span>
	</div>
	<div class="sortable_div">
		<!-- 背景 -->
		<div id ="svli_sortable_bg" class="sortable_bg"></div>
		<ul id="svli_sortable" class="sortable"></ul>
	</div>
</div>
<!-- 机柜位置 -->
<div id="svli_cab_position" style="padding:20px;">
	<div style="padding-bottom: 8px;">
		<span>图中的方向为面对机柜正面时的方向</span>
	</div>
	<div class="sortable_div">
		<!-- 背景 -->
		<div id ="svli_cab_sortable_bg" class="sortable_bg"></div>
		<ul id="svli_cab_sortable" class="sortable"></ul>
	</div>
</div>
<div id="svli_chcabinet" title="移动选中服务器" class="easyui-dialog"
	style="width:500px;height:370px;padding:10px 20px;"
	data-options="closed:true,buttons:'#svli_chcabinet_btn'">
	<div class="usli_ftitle">
		<span>请选择要移动到的机柜</span>
	</div>
	<div id="svli_chcabinet_toolbar" style="padding:5px;height:auto;">
		<div style="padding: 5px 0 0 4px;">
			名称：<input id="svli_chcabinet_name" class="igdli_input120"/>&ensp;
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="$('#svli_chcabinet_table').datagrid('load');"
				data-options="plain:true,iconCls:'icon-search'">查找</a>
		</div>
	</div>
	<table id="svli_chcabinet_table"></table>
</div>
<div id="svli_chcabinet_btn">
	<a href="javascript:;" class="easyui-linkbutton" onclick="svli_changeCabinet()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#svli_chcabinet').dialog('close');"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<br /><br />