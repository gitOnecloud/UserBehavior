<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>onecloud行为分析</title>
<base href="<%=basePath%>"/>

<!-- <link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" /> -->
<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="script/ztree/css/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" type="text/css" href="css/new.css">

<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="script/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="script/highcharts/highcharts.js"></script>
<!-- <script type="text/javascript" src="script/highcharts/exporting.js"></script> -->
<script type="text/javascript" src="script/jquery/jquery.sortable.min.js"></script>

<script type="text/javascript" src="script/onecloud/util/mess.js"></script>
<script type="text/javascript" src="script/onecloud/util/date.js"></script>
<script type="text/javascript" src="script/index.js"></script>

<script type="text/javascript">
var index_zNodes = <s:property value="json" escapeHtml="false"/>;
var index_user = {"id": "<s:property value="user.id[0]" escapeHtml="false"/>",
	"name": "<s:property value="user.str[0]" escapeHtml="false"/>"};
</script>
<style type="text/css">
</style>
</head>
<body>
<body>
<div class="index_prompt" id="index_prompt" style="display: none"></div>
<!-- 首页主体 begin 分为上 下左 下右 -->
  <div class="easyui-layout" data-options="fit: true">
	<div style="padding-right:30px;" data-options="region:'north',border: false" class="new_header2">
		<div style="float: left;">
			<span><img src="images/new_header.png" /> </span>
		</div>
		<div style="float: right;">
			<div style="float: left;padding-top: 36px;">
				<span id="index_user"></span>
				<!-- <a href="javascript:;" class="easyui-linkbutton" onclick="index_login()">登录</a> -->
				<a id="index_loa" href="j_spring_security_logout" class="easyui-linkbutton" >退出</a>
				<a id="index_cpa" href="javascript:;" class="easyui-linkbutton" onclick="index_changePS()">修改密码</a>
			</div>
		</div>
    </div>
    <div title="菜单栏" style="width:218px;overflow-y:auto;"
		data-options="region:'west',split:true" class="new_left">
		<ul id="index_treeDemo" class="ztree"></ul>
	</div>
	<div data-options="region:'center'">
		<div id="mainbody" class="easyui-tabs" data-options="fit:true,border:false">
			<!-- <div title="主体部分" class="tabcontent"
				data-options="tools:[{
			        iconCls:'icon-mini-refresh',
			        handler:function(){loadIndex(true);}}]">
			</div> -->
		</div>
	</div>
</div>
<!-- 首页主体 end -->
<!-- 登录框 -->
<div id="index_dd" style="width:350px;height:200px;padding:25px 0 30px 30px">
	<form id='index_form' action='j_spring_security_check' method='POST'>
	<table>
		<tr><td>用户名:</td>
			<td>
				<input id="j_username" type='text' name='j_username'/>
				<input type="hidden" name="j_ajax"/>
			</td>
		</tr>
		<tr style="height: 30px;"><td>密&nbsp;&nbsp;码:</td>
			<td><input id="j_password" type='password' name='j_password'/></td>
		</tr>
		<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='_spring_security_remember_me' /></td>
			<td>记住密码</td>
		</tr>
	</table>
	</form>
</div>
<!-- 更改密码框 begin-->
<div id="index_ps" class="easyui-dialog" style="width:400px;height:210px;padding:25px 20px 30px 20px"
	data-options="closed:true,title:'更改密码',buttons:'#index_dlg-buttons'">
	<form id="index_fm" method="post">
		<div class="usli_fitem">
			<label>原始密码:</label><input name="user.account" type="password" style="width:150px;" class="easyui-validatebox" data-options="required:true">
			<span id="index_pw_span"></span>
		</div>
		<div class="usli_fitem">
			<label>新的密码:</label><input name="user.password" type="password" style="width:150px;">
			<span>为空，使用123456。</span>
		</div>
		<div class="usli_fitem">
			<label>确定密码:</label><input name="user.name" type="password" style="width:150px;">
		</div>
	</form>
</div>
<div id="index_dlg-buttons">
	<a href="javascript:;" class="easyui-linkbutton" onclick="index_changePW()"
		data-options="iconCls:'icon-ok'">保存</a>
	<a href="javascript:;" class="easyui-linkbutton" onclick="javascript:$('#index_ps').dialog('close')"
		data-options="iconCls:'icon-cancel'">取消</a>
</div>
<!-- 更改密码框 end-->
</body>
</html>