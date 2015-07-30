<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var tall_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div style="padding: 30px 0 10px 40px;">
<span>排行：</span>
<select id="tall_sort" class="easyui-combobox" style="width: 90px;">
    <option value="0">上行流量</option>
    <option value="1" selected="selected">下行流量</option>
    <option value="2">浏览数</option>
</select>
<span style="padding-left: 20px;">时间：</span>
<select id="tall_datesort" class="easyui-combobox" style="width: 60px;">
    <option value="0" selected="selected">按月</option>
    <option value="1">按天</option>
</select>&ensp;
<input id="tall_date" type="text" class="easyui-datebox" style="width:100px">&ensp;
<a id="tall_pre" href="javascript:;" class="easyui-linkbutton" onclick="tall_prenext(-1)">上一月</a>
<a id="tall_next" href="javascript:;" class="easyui-linkbutton" onclick="tall_prenext(1)">下一月</a>&ensp;
<span style="padding-left: 20px;">单位：</span>
<select id="tall_trafficunit" class="easyui-combobox" style="width: 50px;">
    <option value="1073741824" selected="selected">GB</option>
    <option value="1048576">MB</option>
    <option value="1024">KB</option>
</select>
<select id="tall_pageunit" class="easyui-combobox" style="width: 60px;">
    <option value="10000" selected="selected">万次</option>
    <option value="100">百次</option>
    <option value="1">次</option>
</select>&emsp;
<a href="javascript:;" class="easyui-linkbutton" onclick="tall_showdata()">报表</a>&ensp;

</div>
<div id="tall_datawindow" class="easyui-window"
	title="流量与浏览数 报表" style="width:800px;height:400px"
	data-options="closed: true, collapsible: false, minimizable: false">
<div id="tall_data" style="padding: 15px 20px"></div>
</div>
<div id="tall_container" style="min-width: 800px; height: 480px; margin: 0 auto"></div>

