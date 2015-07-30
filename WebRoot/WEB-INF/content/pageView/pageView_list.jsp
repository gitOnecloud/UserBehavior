<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var pvl_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div style="padding: 30px 0 10px 40px;">
<span style="padding-left: 20px;">时间：</span>
<select id="pvl_datesort" class="easyui-combobox" style="width: 60px;">
    <option value="0" selected="selected">按月</option>
    <option value="1">按天</option>
</select>&ensp;
<input id="pvl_date" type="text" class="easyui-datebox" style="width:100px">&ensp;
<a id="pvl_pre" href="javascript:;" class="easyui-linkbutton" onclick="pvl_prenext(-1)">上一月</a>
<a id="pvl_next" href="javascript:;" class="easyui-linkbutton" onclick="pvl_prenext(1)">下一月</a>&ensp;
<span style="padding-left: 20px;">单位：</span>
<select id="pvl_viewunit" class="easyui-combobox" style="width: 60px;">
    <option value="10000">万次</option>
    <option value="100">百次</option>
    <option value="1" selected="selected">次</option>
</select>&emsp;
<a href="javascript:;" class="easyui-linkbutton" onclick="pvl_showdata()">报表</a>&ensp;

</div>
<div id="pvl_datawindow" class="easyui-window"
	title="Pispower页面访问量 报表" style="width:600px; height:400px"
	data-options="closed: true, collapsible: false, minimizable: false">
<div id="pvl_data" style="padding: 15px 20px"></div>
</div>
<div id="pvl_container" style="min-width: 800px; height: 480px; margin: 0 auto"></div>

