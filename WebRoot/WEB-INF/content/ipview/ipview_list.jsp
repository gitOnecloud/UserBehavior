<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var ipvl_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div style="padding: 30px 0 0 40px;">
<div style="padding-bottom: 10px;">
一级域名：<input id="ipvl_aliases" type="text" class="easyui-validatebox">&emsp;
二级域名：<input id="ipvl_domain" type="text" class="easyui-validatebox">&emsp;
OAID：<input id="ipvl_oaid" type="text" class="easyui-validatebox">&emsp;
<a href="javascript:void(0)" class="easyui-linkbutton"
	onclick="ipvl_searchApp()" data-options="iconCls:'icon-search'">搜索</a>
<a href="javascript:void(0)" class="easyui-linkbutton" onclick="ipvl_clearForm()">清除</a>
</div>

<span>排行：</span>
<select id="ipvl_sort" class="easyui-combobox" style="width: 90px;">
    <option value="0" selected="selected">ip数</option>
    <option value="1">浏览数</option>
</select>
<span style="padding-left: 20px;">时间：</span>
<select id="ipvl_datesort" class="easyui-combobox" style="width: 60px;">
    <option value="0" selected="selected">按月</option>
    <option value="1">按天</option>
</select>&ensp;
<input id="ipvl_date" type="text" class="easyui-datebox" style="width:100px">&ensp;
<a id="ipvl_pre" href="javascript:;" class="easyui-linkbutton" onclick="ipvl_prenext(-1)">上一月</a>
<a id="ipvl_next" href="javascript:;" class="easyui-linkbutton" onclick="ipvl_prenext(1)">下一月</a>&ensp;
<span style="padding-left: 20px;">单位：</span>
<select id="ipvl_ipunit" class="easyui-combobox" style="width: 60px;">
    <option value="10000">万个</option>
    <option value="100">百个</option>
    <option value="1" selected="selected">个</option>
</select>
<select id="ipvl_viewunit" class="easyui-combobox" style="width: 60px;">
    <option value="10000" selected="selected">万次</option>
    <option value="100">百次</option>
    <option value="1">次</option>
</select>&emsp;
<a href="javascript:;" class="easyui-linkbutton" onclick="ipvl_showdata()">报表</a>&ensp;
<div style="padding-top: 10px;">
	<span id="ipvl_appdata"></span>
</div>
</div>
<div id="ipvl_datawindow" class="easyui-window"
	title="各省IP访问量 报表" style="width:600px; height:400px"
	data-options="closed: true, collapsible: false, minimizable: false">
<div id="ipvl_data" style="padding: 15px 20px"></div>
</div>
<div id="ipvl_container" style="min-width: 800px; height: 480px; margin: 0 auto"></div>

