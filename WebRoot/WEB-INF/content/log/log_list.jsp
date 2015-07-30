<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<style type="text/css">
	#log_fm{
		margin:0;
		padding:10px 30px;
	}
	.log_ftitle{
		font-size:14px;
		font-weight:bold;
		color:#666;
		padding:5px 0;
		margin-bottom:10px;
		border-bottom:1px solid #ccc;
	}
	.log_fitem{
		margin-bottom:5px;
	}
	.log_fitem label{
		padding-right: 5px;
	}
</style>

<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.日期格式：yyyy-mm-dd；&nbsp;&nbsp;2.可按IP、创建时间或更新时间进行全局排序
    </div>
</div>
	
	<table id="log_table" 
			data-options="iconCls:'icon-edit',nowrap:true,fitColumns:true,rownumbers:true,
			pagination:true,striped: true,url:'log_listTable.action',remoteSort:true"  
			toolbar="#log_tb"
			>
		<thead>
			<tr>
				<th data-options="field:'cb',checkbox:true,width:40">全选</th>
				<th data-options="field:'IP',width:80,align:'center',sortable:true">IP</th>
				<th data-options="field:'content',width:200,align:'center'">内容</th>
				<th data-options="field:'type',width:50,align:'center'">类型</th>
				<th data-options="field:'createTime',width:80,align:'center',sortable:true">创建时间</th>
				<th data-options="field:'updateTime',width:80,align:'center',sortable:true">更新时间</th>
				<th data-options="field:'operation',width:80,align:'center',
					formatter:function(value, rowData, rowIndex){
					          return log_operation(value, rowData, rowIndex)}">操作</th>
			</tr>
		</thead>
	</table>
	
	<div id="log_tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="log_new()">添加</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="log_edit(null)">编辑</a>
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="log_remove(null)">删除</a>
			<a href="javascript:;" id='log_export' class="easyui-menubutton" iconCls="icon-download">导出</a>
		</div>
		<div>
			创建时间： <input id="log_tb_sdate" name="sdate" class="easyui-datebox" style="width:90px">
			&nbsp;~&nbsp;<input id="log_tb_edate" name="edate" class="easyui-datebox" style="width:90px">
			&nbsp;&nbsp; IP：
			<input id="log_tb_ip" style="width:170px;height:20px">
			&nbsp;&nbsp; 类型：
			<input id="log_tb_type" class="easyui-combobox" style="width:90px"
				   data-options="valueField:'value',textField:'text',panelHeight:'auto',editable:false">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" onclick="log_find()">搜索</a>
			<span style="position: absolute;right: 10px"><input id="log_searchbox"></span>
		</div>
	</div>
	
	<div id="log_dlg" class="easyui-dialog" style="width:580px;height:330px;padding:10px 20px"
		 closed="true" buttons="#log_dlg-buttons">
		 <div class="log_ftitle"><span id='log_dlg_title'>添加日志</span></div>
		 <form id="log_fm" method="post" novalidate>
		 	<div class="log_fitem">
				<div class="log_fitem">
					<div class="log_fitem" style="float:left;width: 220px;">
						<label>IP&nbsp;&nbsp;&nbsp;&nbsp;:</label>
						<input id="log_ip" name="IP" style="width:130px;height:20px">
					</div>
					<div class="log_fitem" style="float:right;width:220px;">
						<label>类型:</label>
						<input id="log_type" name="type" class="easyui-combobox" style="width:100px" editable="false"
						   data-options="required:true,panelHeight:'auto',valueField:'value',textField:'text'">
					</div>
				</div>
			</div>
			<div style="clear:both"></div>
			<div class="log_fitem">
				<div class="log_fitem" style="float:left;width: 35px;">
				    <label>内容:</label>
				</div>
				<div class="log_fitem" style="float:left;width:400px;">
					<textarea name="content" class="easyui-validatebox" style="width:380px;height:150px;" rows="3" required="true"></textarea>
				</div>
			</div>
		 </form>
	</div>
	
	<div id="log_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="log_save()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#log_dlg').dialog('close')">取消</a>
	</div>
	
	<!-- 双击查看详情对话框 -->
	<div id="log_detail_dlg" class="easyui-dialog" style="width:500px;height:350px;padding:10px 20px"
			closed="true" buttons="#log_detail_dlg-buttons">
		<span id="log_dlg_detail"></span>
	<div id="log_detail_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:$('#log_detail_dlg').dialog('close')">OK</a>
	</div>
	
	<!-- 导出下拉菜单 -->
	<div id="log_export_menu" style="width:100px;">  
		<div>导出当前</div>
	    <div>导出全部</div>  
	</div>