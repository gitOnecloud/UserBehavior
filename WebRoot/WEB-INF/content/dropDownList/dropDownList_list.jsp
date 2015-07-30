<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<style type="text/css">
	#ddlField_fm{
		margin:0;
		padding:10px 30px;
	}
	#ddlValue_fm{
		margin:0;
		padding:10px 30px;
	}
	.dropDownList_ftitle{
		font-size:14px;
		font-weight:bold;
		color:#666;
		padding:5px 0;
		margin-bottom:10px;
		border-bottom:1px solid #ccc;
	}
	.dropDownList_fitem{
		margin-bottom:5px;
	}
	.dropDownList_fitem label{
		padding-right: 5px;
	}
</style>
<script type="text/javascript" src="script/easyui/plugins/datagrid-detailview.js"></script>
	
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
			<div>提示：数据字典名称请使用英文、数字或下划线
	    </div>
	</div>
	
	<table id="dropDownList_table" 
			data-options="iconCls:'icon-edit',nowrap:true,fitColumns:true,rownumbers:true,singleSelect:true,
			pagination:true,striped: true,url:'dropDownList_listTable.action'"  
			toolbar="#dropDownList_tb"
			>
	</table>
	
	<div id="dropDownList_tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addField()">增加数据字典</a>
		</div>
		<div>
			名称：
			<input id="dropDownList_tb_name" style="width:170px;height:20px">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-search" onclick="ddl_find()">搜索</a>
		</div>
	</div>
	
	<div id="ddlField_dlg" class="easyui-dialog" style="width:400px;height:220px;padding:10px 20px"
		 closed="true" buttons="#ddlField_dlg-buttons">
		 <div class="dropDownList_ftitle"><span id='ddlField_dlg_title'>添加数据字典</span></div>
		 <form id="ddlField_fm" method="post" novalidate>
			<div class="dropDownList_fitem">
			    <label>名称:</label>
				<input id="ddlField_name" name="field_name" class="easyui-validatebox" style="width:150px;height:20px" 
					   required="true" validType="isletterOrNum">  
			</div>
			<div class="dropDownList_fitem">
			    <label>备注:</label>
				<input id="ddlField_comment" name="field_comment" class="easyui-validatebox" style="width:150px;height:20px" 
					   required="true">
			</div>
		 </form>
	</div>
	
	<div id="ddlField_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveField()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#ddlField_dlg').dialog('close')">取消</a>
	</div>
	
	<div id="ddlValue_dlg" class="easyui-dialog" style="width:400px;height:250px;padding:10px 20px"
		 closed="true" buttons="#ddlValue_dlg-buttons">
		 <div class="dropDownList_ftitle"><span id='ddlValue_dlg_title'>添加选项</span></div>
		 <form id="ddlValue_fm" method="post" novalidate>
			<div class="dropDownList_fitem">
				<div class="dropDownList_fitem" style="float:left;width:80px;">
					<label>值:</label>
				</div>
				<div class="dropDownList_fitem" style="float:left;width:200px;">
					<input name="value_name" class="easyui-validatebox" style="width:150px;height:20px" required="true">
				</div>
			</div>
			<div class="dropDownList_fitem">
				<div class="dropDownList_fitem" style="float:left;width:80px;">
					<label>是否有效:</label>
				</div>
				<div class="dropDownList_fitem" style="float:left;width:200px;">
					<select id="valid" name="value_valid" class="easyui-combobox" style="width: 150px;"
							data-options="editable: false,required: true,panelHeight:'auto'">
						<option value="true">true</option>
						<option value="false">false</option>
					</select>
				</div>
			</div>
			<div class="dropDownList_fitem">
				<div class="dropDownList_fitem" style="float:left;width:80px;">
					<label>顺序号:</label>
				</div>
				<div class="dropDownList_fitem" style="float:left;width:200px;">
					<input name="seq" class="easyui-validatebox" style="width:150px;height:20px" required="true">
				</div>
			</div>
		 </form>
	</div>
	
	<div id="ddlValue_dlg-buttons">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveValue()">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#ddlValue_dlg').dialog('close')">取消</a>
	</div>