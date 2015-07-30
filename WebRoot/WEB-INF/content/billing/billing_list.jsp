<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<script type="text/javascript">
var bili_data = <s:property value="json" escapeHtml="false"/>;
</script>
<div style="padding: 30px 0 0 40px;">
<div style="padding-bottom: 10px;">
云架构：<input id="bili_appname" type="text" class="easyui-validatebox">&emsp;
OAID：<input id="bili_oaid" type="text" class="easyui-validatebox">&emsp;
<a href="javascript:void(0)" class="easyui-linkbutton"
	onclick="bili_searchApp()" data-options="iconCls:'icon-search'">搜索</a>
<a href="javascript:void(0)" class="easyui-linkbutton" onclick="bili_clearForm()">清除</a>
</div>

<span>时间：</span>
<select id="bili_datesort" class="easyui-combobox" style="width: 60px;">
	<option value="2">按年</option>
    <option value="0" selected="selected">按月</option>
    <!-- <option value="1">按天</option> -->
</select>&ensp;
<input id="bili_date" type="text" class="easyui-datebox" style="width:100px">&ensp;
<a id="bili_pre" href="javascript:;" class="easyui-linkbutton" onclick="bili_prenext(-1)">上一月</a>
<a id="bili_next" href="javascript:;" class="easyui-linkbutton" onclick="bili_prenext(1)">下一月</a>&ensp;
<span style="padding-left: 20px;">单位：</span>
<select id="bili_feeunit" class="easyui-combobox" style="width: 66px;">
    <option value="10000000">万元</option>
    <option value="1000" selected="selected">元</option>
</select>&emsp;
<a href="javascript:;" class="easyui-linkbutton" onclick="bili_showdata()">报表</a>&ensp;
<div style="padding-top: 10px;">
	<span id="bili_appdata"></span>
</div>
</div>
<div id="bili_datawindow" class="easyui-window"
	title="资源使用和费用 报表" style="width: 800px; height: 400px"
	data-options="closed: true, collapsible: false, minimizable: false">
	<div id="bili_data" style="padding: 15px 20px"></div>
</div>
<div id="bili_container" style="min-width: 800px; height: 480px; margin: 0 auto"></div>

