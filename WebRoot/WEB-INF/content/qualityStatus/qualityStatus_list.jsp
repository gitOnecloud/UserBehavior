<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var qualityStatus_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div style="padding: 30px 0 0 40px;">
<div style="padding-bottom: 10px;">
一级域名：<input id="qualityStatus_aliases" type="text" class="easyui-validatebox">&emsp;
二级域名：<input id="qualityStatus_domain" type="text" class="easyui-validatebox">&emsp;
OAID：<input id="qualityStatus_oaid" type="text" class="easyui-validatebox">&emsp;
<a href="javascript:void(0)" class="easyui-linkbutton"
	onclick="qualityStatus_searchApp()" data-options="iconCls:'icon-search'">搜索</a>
<a href="javascript:void(0)" class="easyui-linkbutton" onclick="qualityStatus_clearForm()">清除</a>
</div>

<span >时间：</span>
<select id="qualityStatus_datesort" class="easyui-combobox" style="width: 60px;">
    <option value="0" selected="selected">按月</option>
    <option value="1">按天</option>
</select>&ensp;
<input id="ipvl_date" type="text" class="easyui-datebox" style="width:100px">&ensp;
<a id="qualityStatus_pre" href="javascript:;" class="easyui-linkbutton" onclick="qualityStatus_prenext(-1)">上一月</a>
<a id="qualityStatus_next" href="javascript:;" class="easyui-linkbutton" onclick="qualityStatus_prenext(1)">下一月</a>&ensp;
<span style="padding-left: 20px;">单位：</span>
<select id="qualityStatus_viewunit" class="easyui-combobox" style="width: 60px;">
    <option value="100">百次</option>
    <option value="1" selected="selected">次</option>
    <option value="10000">万次</option>
</select>&emsp;
<a href="javascript:;" class="easyui-linkbutton" onclick="qualityStatus_showdata()">报表</a>&ensp;
<div style="padding-top: 10px;">
	<span id="qualityStatus_infodata"></span>
</div>
</div>
<div id="qualityStatus_datawindow" class="easyui-window"
	title="各应用服务质量" style="width:600px; height:400px"
	data-options="closed: true, collapsible: false, minimizable: false">
<div id="qualityStatus_data" style="padding: 15px 20px"></div>
</div>
<div id="qualityStatus_container" style="min-width: 800px; height: 480px; margin: 0 auto"></div>

