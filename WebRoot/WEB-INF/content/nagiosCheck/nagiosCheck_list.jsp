<%@ page contentType="text/html; charset=UTF-8"  %>
<br />
<script type="text/javascript">
$('#nagiosCheck_igd_add').combogrid({
    panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'name',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	toolbar: '#ncli_toolbar3',//工具条
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
	queryParams: {
    	"page.name": {"id":"ncli_name3","type":"text"}
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
       {field: "name", title: "组件名称", width: 80, align: 'center'},
       {field: "port", title: "端口", width: 60, align: 'center',
    	   formatter: function(value, row ,index) {
    		   if(value == 0) {
    			   return '';
    		   } else {
    			   return value;
    		   }
       }},
       {field: "pname", title: "父组件", width: 80, align: 'center'},
       {field: "type", title: "类型", width: 60, align: 'center'},
       {field: "register", title: "注册组件", width: 40, align: 'center' ,
    	   formatter: function(value, row ,index) {
    		   if(value == 1) {
    			   return '是';
    		   } else {
    			   return '';
    		   }
       }}
     ]]
});

$('#nagiosCheck_nagios').combogrid({
	panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'checkName',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	toolbar: '#ncli_toolbar4',//工具条
	url: 'nagiosCheck_nagios_list.action?_=' + Math.random(),
	queryParams: {
    	"page.checkName": {"id":"ncli_name4","type":"text"}
    }, paramsName: {
    	page: "page.page",
    	num: "page.num"
    }, columns: [[
       {field: "checkName", title: "监控名称", width: 80, align: 'center'},
       {field: "description", title: "描述", width: 60, align: 'center'},
       {field: "stoptime", title: "监控间隔", width: 80, align: 'center'},
       {field: "command", title: "监控命令", width: 60, align: 'center'}
     ]]
});
</script>
<div id="nagiosCheck_toolbar" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		<a href="javascript:;" class="easyui-linkbutton"
			onclick="updateIcheck_change=0;$('#nagiosCheck_add').dialog('open').dialog('setTitle','添加组件监控');"
			data-options="iconCls:'icon-add','plain':true">添加</a>&ensp;
		<!-- <a href="javascript:;" class="easyui-linkbutton" onclick=""
			data-options="'iconCls':'icon-edit','plain':true">编辑</a>&ensp; -->
		<a href="javascript:;" class="easyui-linkbutton" onclick="nagiosCheck_remove()"
			data-options="'iconCls':'icon-remove','plain':true">删除</a>&ensp;
	</div>
	<div style="padding: 5px 0 0 4px;">
		监控名称：<input id="nagiosCheck_checkName" class="igdli_input120"/>
		&ensp;组件：<input id="nagiosCheck_igd" />
		&ensp;监控间隔：<input id="nagiosCheck_stoptime" class="igdli_input120"/>
		&ensp;命令：<input id="nagiosCheck_command" class="igdli_input120"/>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#nagiosCheck_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="nagiosCheck_clearData();">清除</a>
		&ensp;<a href="javascript:void(0);" onclick="nagiosCheck_add()" class="easyui-linkbutton">添加监控</a>
		<!-- &ensp;<a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#freshNagios').dialog('open')">刷新配置</a> -->
		&ensp;<a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#freshNagiosMs').dialog('open');">刷新配置</a>
	</div>
</div>

<div id="ncli_toolbar3" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		组件名称：<input id="ncli_name3" class="igdli_input160" />
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#nagiosCheck_igd_add').combogrid('grid').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>

<div id="ncli_toolbar4" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		监控名称：<input id="ncli_name4" class="igdli_input160" />
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#nagiosCheck_nagios').combogrid('grid').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>

<table id="nagiosCheck_table"></table>
<div id="nagios_add" style="padding: 12px;">
	<input id="nagios_id" type="hidden"/>
	<div >监控名称：<input id="nagios_checkName" class="igdli_input160" /><span id="resultVerify" style="color: red;"></span></div>
	<div style="padding-top:8px;">监控间隔：
		<select id="nagios_stoptime" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
			<option value="1" selected="selected">1</option>
			<option value="5">5</option>
			<option value="10">10</option>
			<option value="30">30</option>
			<option value="720">720</option>
		</select>
		<span style="color: red;">单位：分钟</span>
	</div>
	<div style="padding-top:8px;">命	&ensp;&ensp;&ensp;令：<input id="nagios_command" class="igdli_input160"/></div>
	<div style="padding-top:8px;">描 &ensp;&ensp;&ensp;述：<input id="nagios_description" class="igdli_input160"/></div>
</div>
<div id="nagiosCheck_add" style="padding:12px;">
	<div>组件：<input id="nagiosCheck_igd_add" /></div>
	<div style="padding-top:8px;">监控：<input id="nagiosCheck_nagios"/></div>
	<div style="padding-top:8px;">启用：<input type="radio" name="isActive" value="1"/>是 <input type="radio" name="isActive" value="0"/>否</div>
</div>
<div id="freshNagios" style="padding:12px;">
	<div>Monitor &ensp;&ensp;&ensp;&ensp;Server：  <select id="freshMornitor" class="easyui-combobox"style="width: 120px"></select></div>
	<div style="padding-top:8px;">待刷新的Satelittle：  <select id="freshSatellite" style="width: 120px" class="easyui-combobox"></select></div>
	<div style="padding-top:8px;">待&ensp;刷&ensp;新&ensp;的Basket&ensp;：  <select id="freshBasket" class="easyui-combobox" style="width: 120px"></select></div>
	<div style="padding-top:8px;"><span id="resultVerifyBasket" style="color: red;"></span></div>
</div>
<div id="freshNagiosMs" style="padding:12px;">
	<div style="padding-top:8px;">待刷新的Satelittle：  <select id="freshSatelliteMs" style="width: 120px" class="easyui-combobox"></select></div>
	<div style="padding-top:8px;" id="ncli_report">是否汇报MainIDC：
		<input type="radio" name="ncli_report" checked="checked"/>是
		<input type="radio" name="ncli_report"/>否</div>
</div>
<br /><br />
