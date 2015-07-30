<%@ page contentType="text/html; charset=UTF-8"  %>
<br />

<script type="text/javascript">
/**
 * 添加框组件
 */
$('#serverli_addigdefine').combogrid({
	panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'name',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	multiple: true, //多选
	toolbar: '#igli_toolbar3',//工具条
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
	queryParams: {
    	"page.name": {"id":"igli_name3","type":"text"}
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

/**
 * 修改框组件
 */
$('#serverli_updateigdefine').combogrid({
	panelHeight: 250,
	panelWidth: 500,
	idField: 'id',
	textField: 'name',
	fitColumns: true,//自动调整单元格宽度
	pagination: true,//分页
	multiple: true, //多选
	toolbar: '#igli_toolbar4',//工具条
	url: 'ingredientdefine_list_js.action?_=' + Math.random(),
	queryParams: {
    	"page.name": {"id":"igli_name4","type":"text"}
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
</script>

<div id="igli_toolbar" style="padding:5px;height:auto;">
	<div style="padding-top: 5px; padding-left: 4px;">
		<a href="javascript:;" id="tr_server_add" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="server_add()">增加</a>
		<a href="javascript:;" id="tr_server_update" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="server_update()">修改</a>
		<a href="javascript:;" id="tr_server_delete" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="server_delete()">删除</a>
		<a href="javascript:;" id='tr_server_export' class="easyui-menubutton" iconCls="icon-download">导出</a>
	</div>
	
	<div style="padding-top: 5px; padding-left: 4px;">
		<!--环境：<select id="igli_party" name="igli_party" style="width:76px;">
				<option id='party_id' value=''></option>
			</select> 
		 &ensp;组件<select id="igli_idf" name="igli_idf" style="width:76px;">
					<option id='idf_id' value=''>全部</option>
				</select>  -->
			      环境：<select id="igli_party" class="easyui-combobox" data-options="panelHeight:138,editable:false">
			      </select>
		&ensp;组件：<input id="igli_igf" value='' class="igdli_input60"/>
		&ensp;状态：<select id="igli_isActive" class="easyui-combobox" data-options="width:86,panelHeight:138,editable:false">
			      	<option value=''>全部</option>
			      	<option value='1'>启用</option>
			      	<option value='0'>停用</option>
			      </select>
		&ensp;类型：<select id="igli_host" class="easyui-combobox" data-options="width:86,panelHeight:138,editable:false">
			      	<option value=''>全部</option>
			      	<option value='1'>物理机</option>
			      	<option value='0'>云主机</option>
			      </select>
		<!-- &ensp;IP：<input id="igli_ip" class="igdli_input100" /> -->
		&ensp;IP：<select id="igli_ip" class="easyui-combobox" data-options="width:120,panelHeight:138,editable:true">
			      	<option></option>
			      	<option>172.16.1.</option>
			      	<option>172.16.2.</option>
			      	<option>172.16.3.</option>
			      	<option>172.16.4.</option>
			      	<option>172.16.8.</option>
			      	<option>172.16.9.</option>
			      	<option>172.17.0.</option>
			      	<option>172.17.2.</option>
			      	<option>192.168.10.</option>
			      </select>
		&ensp;<a href="javascript:;" class="easyui-linkbutton" onclick="$('#igli_table').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="igli_clearData();">清除</a>
	</div>
</div>
<div id="igli_toolbar3" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		组件名称：<input id="igli_name3" class="igdli_input160" />
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#serverli_addigdefine').combogrid('grid').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>
<div id="igli_toolbar4" style="padding:5px;height:auto;">
	<div style="padding: 5px 0 0 4px;">
		组件名称：<input id="igli_name4" class="igdli_input160" />
		&ensp;<a href="javascript:;" class="easyui-linkbutton"
			onclick="$('#serverli_updateigdefine').combogrid('grid').datagrid('load');"
			data-options="iconCls:'icon-search'">查找</a>
	</div>
</div>

<table id="igli_table"></table>

<div id="server_detail_dd" class="easyui-dialog" style="width:750px;height:280px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
	<form id='detail_form' method='post'>
	<div class="fitem">
				<input type="text" id="dserver_id" name="server_id" hidden="hidden">
	</div>
	</form>
	<table id="server_detail"></table>
</div>
	
<div id="server_add_dd" class="easyui-dialog" style="width:350px;height:380px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
		<div class="usli_ftitle">服务器信息</div>
		<form id='server_add_form' method='post'>
			<div class="fitem">
				<label>I&ensp;&ensp;P：</label>
				<input type="text" name="ip">
			</div>
			<div class="fitem">
				<label>（例：172.16.1.2..9,172.16.1.12..14）</label>
			</div>
			<div class="fitem">
				<label>环境：</label>
				<select id="serverli_addparty" name="party_id" class="easyui-combobox" data-options="width:155,panelHeight:138,editable:false">
				</select>
			</div>
			<div class="fitem">
				<label>组件：</label>
				<!-- <select id="serverli_addigdefine" name="define_id" class="easyui-combobox" data-options="width:160,panelHeight:138">
				</select> -->
				<input id="serverli_addigdefine" name="define_id" value='' data-options="width:155,panelHeight:138,editable:false"/>
			</div>
			<div class="fitem">
				<label>启用：</label>
				<input type="radio" name="isActive" value="1" checked>是
				<input type="radio" name="isActive" value="0">否
			</div>
			<div class="fitem">
				<label>是否云主机：</label>
				<input type="checkbox" name="is" onclick="t.disabled=!this.checked?true:false">是
				<input type="text" name="isVm" id="t" disabled>
			</div>
			<div class="fitem">
				<label>备注：</label>
				<textarea name='remark' rows="3" cols="30"></textarea>
			</div>
		</form>
	</div>
	
	<div id="server_update_dd" class="easyui-dialog" style="width:350px;height:350px;padding:10px 20px;overflow:hidden;"
						data-options="closed:true,buttons:'#usli_dlg-buttons'">
		<div class="usli_ftitle">服务器信息</div>
		<form id='server_update_form' method='post'>
			<div class="fitem">
				<input type="text" id="bserver_id" name="server_id" hidden="hidden">
			</div>
			<div class="fitem">
				<input type="text" id="big_id" name=ig_id hidden="hidden">
			</div>
			<div class="fitem">
				<label>I&ensp;&ensp;P：</label>
				<input type="text" id="bip" name="ip" readonly="readonly">
			</div>
			<div class="fitem">
				<label>环境：</label>
				<select id="serverli_updateparty" name="party_id" class="easyui-combobox" data-options="width:155,panelHeight:138,editable:false">
				</select>
			</div>
			<div class="fitem">
				<label>组件：</label>
				<!-- <select id="serverli_updateigdefine" name="define_id" class="easyui-combobox" data-options="width:160,panelHeight:138">
				</select> -->
				<input id="serverli_updateigdefine" name="define_id" value='' data-options="width:155,panelHeight:138,editable:false"/>
			</div>
			<div class="fitem">
				<label>启用：</label>
				<input type="radio" id="bisActive1" name="isActive" value="1">是
				<input type="radio" id="bisActive2" name="isActive" value="0">否
			</div>
			<div class="fitem">
				<label>备注：</label>
				<textarea id='bremark' name='remark' rows="3" cols="30"></textarea>
			</div>
		</form>
	</div>
	<!-- 导出下拉菜单 -->
	<div id="serverli_export_menu" style="width:100px;">  
		<div>导出当前</div>
	    <div>导出全部</div>  
	</div>
<!-- nagios监控设置 -->
<div id="igdli_nagios" style="padding:10px 20px;">
	<ul id="igdli_nagios_ul"></ul>
</div>
<br /><br />
