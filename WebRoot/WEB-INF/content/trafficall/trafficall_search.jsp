<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var tsearch_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div style="padding: 30px 0 0 40px;">
<div style="padding-bottom: 10px;">
一级域名：<input id="tsearch_aliases" type="text" class="easyui-validatebox">&emsp;
二级域名：<input id="tsearch_domain" type="text" class="easyui-validatebox">&emsp;
OAID：<input id="tsearch_oaid" type="text" class="easyui-validatebox">&emsp;
<a href="javascript:void(0)" class="easyui-linkbutton"
	onclick="tsearch_searchApp()" data-options="iconCls:'icon-search'">搜索</a>
<a href="javascript:void(0)" class="easyui-linkbutton" onclick="tsearch_clearForm()">清除</a>
</div>

<span>时间：</span>
<select id="tsearch_datesort" class="easyui-combobox" style="width: 60px;">
	<option value="2">按年</option>
    <option value="0" selected="selected">按月</option>
    <option value="1">按天</option>
</select>&ensp;
<input id="tsearch_date" type="text" class="easyui-datebox" style="width:100px">&ensp;
<a id="tsearch_pre" href="javascript:;" class="easyui-linkbutton" onclick="tsearch_prenext(-1)">上一月</a>
<a id="tsearch_next" href="javascript:;" class="easyui-linkbutton" onclick="tsearch_prenext(1)">下一月</a>&ensp;
<span style="padding-left: 20px;">单位：</span>
<select id="tsearch_trafficunit" class="easyui-combobox" style="width: 50px;">
	<option value="1099511627776">TB</option>
    <option value="1073741824">GB</option>
    <option value="1048576" selected="selected">MB</option>
    <option value="1024">KB</option>
</select>
<select id="tsearch_pageunit" class="easyui-combobox" style="width: 66px;">
	<option value="1000000">百万次</option>
    <option value="10000" selected="selected">万次</option>
    <option value="1">次</option>
</select>&emsp;
<a href="javascript:;" class="easyui-linkbutton" onclick="tsearch_showdata()">报表</a>&ensp;
<div style="padding-top: 10px;">
	<span id="tsearch_appdata"></span>
</div>
</div>
<div id="tsearch_datawindow" class="easyui-window"
	title="流量与浏览数 报表" style="width: 600px; height: 400px"
	data-options="closed: true, collapsible: false, minimizable: false">
	<div id="tsearch_data" style="padding: 15px 20px"></div>
</div>
<div id="tsearch_container" style="min-width: 800px; height: 480px; margin: 0 auto"></div>

