<%@ page contentType="text/html; charset=UTF-8"  %>
<br />
<div id="applicationinfo_toolbar" style="padding:5px;height:auto;">
	<div style="padding-top: 5px; padding-left: 4px;">
		oaid：<input id="applicationinfo_oaid" name="applicationinfo_oaid" class="igdli_input160" />
		&ensp;描述：<input id="applicationinfo_description" class="igdli_input160" />
		&ensp;Nagios服务器： <select id="application_arena" class="easyui-combobox"style="width: 120px"></select>
		&ensp;监控：
		<select id="application_monitor" style="line-height: 20px;height: 20px;">
			<option value="1" selected="selected">是</option>
			<option value="0">否</option>
		</select>
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#applicationinfo_table').datagrid('load');" data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="$('#applicationinfo_add').dialog('open').dialog('setTitle','新增应用监控');">新增oaid</a>
		<!-- &ensp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="freshConfig()">刷新配置</a> -->
		&ensp;<a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#freshNagiosMs2').dialog('open');">刷新配置</a>
		&ensp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearQueryCondition()">清除</a>	
	</div>
</div>
<table id="applicationinfo_table"></table>
<div id="applicationinfo_add" style="padding:12px;">
	<div><input type="hidden" id="applicationinfo_id_add"/></div>
	<div>oaid：<input id="applicationinfo_oaid_add" /></div>
	<div style="padding-top:8px;">描述：<input id="applicationinfo_description_add"/></div>
	<div style="padding-top:8px;">Satellite：<select id="application_arena_add" class="easyui-combobox" style="width: 125px"></select></div>
	<div style="padding-top:8px;">监控：<input type="radio" name="applicationinfo_isActive_add" value="1" checked="checked"/>是 <input type="radio" name="applicationinfo_isActive_add" value="0"/>否</div>
</div>
<div id="freshNagiosMs2" style="padding:12px;">
	<div style="padding-top:8px;">待刷新的Satelittle：  <select id="freshSatelliteMs2" style="width: 120px" class="easyui-combobox"></select></div>
	<div style="padding-top:8px;" id="ncli_report2">是否汇报MainIDC：
		<input type="radio" name="ncli_report2" checked="checked"/>是
		<input type="radio" name="ncli_report2"/>否</div>
</div>
<br /><br />