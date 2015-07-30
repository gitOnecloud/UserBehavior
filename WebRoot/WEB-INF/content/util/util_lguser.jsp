<%@ page contentType="text/html; charset=UTF-8"  %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div class="demo-info">
	<div class="demo-tip icon-tip"></div>
	<div>提示：1.重启tomcat后，重新登录后的用户才能获取；2.数据保存在内存中，无法查看多服务器中所有用户。</div>
</div>
<s:property value="json" escapeHtml="false"/>