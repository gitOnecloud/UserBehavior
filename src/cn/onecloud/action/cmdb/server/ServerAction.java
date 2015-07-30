package cn.onecloud.action.cmdb.server;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.service.cmdb.server.ServerManager;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientPage;
import cn.onecloud.util.page.cmdb.ServerPage;

@SuppressWarnings("serial")
@Component("server")
@Scope("prototype")//必须注解为多态
public class ServerAction extends CommonAction {
	@Resource(name="serverManager")
	private ServerManager svm;
	private IngredientPage  page;
	private ServerPage spage;
	private Server server;
	
	private String ig_id;  //无效
	private String array_server_id;
	private String array_party_id;
	
	private String ip;
	private String isActive;
	private String isVm;
	private String party_id;
	private String define_id;
	private String server_id;
	private String remark;
	
	private String downloadFileName;
	private InputStream excelStream;
	
	//导出全部service列表
	public String currentexport() throws Exception{
		excelStream = svm.currentexport(page);
		Date date = new Date();
		downloadFileName=new String(("服务器列表"+StaticMethod.DateToDay(date)).getBytes("GBK"), "ISO-8859-1");
		return "excel";
	}
		
	//查询软硬件信息
	public String details(){
		json = svm.findHwSw(Integer.valueOf(server_id));
		return JSON;
	}
	
	//更新软硬件信息
	public String updatedetails(){
		//调用更新方法
		json = svm.updateHwSw(Integer.valueOf(server_id));
		//json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		return JSON;
	}
	
	//添加服务器
	public String add(){
		if(ip.length()==0||ip==null){
			json = "{\"status\":1,\"mess\":\"IP不能为空！\"}";
			return JSON;
		}
		
		List<String> ip_list = StaticMethod.doip(ip);
		if(ip_list==null){
			json = "{\"status\":1,\"mess\":\"IP格式不正确！\"}";
			return JSON;
		}
		
		if(isVm!=null&&isVm.length()!=0&&StaticMethod.isip(isVm)==false){
			json = "{\"status\":1,\"mess\":\"物理机IP格式不正确！\"}";
			return JSON;
		}
		
		if(party_id==null||party_id.length()==0){
			json = "{\"status\":1,\"mess\":\"请选择环境！\"}";
			return JSON;
		}
		
		if(define_id==null||define_id.length()==0){
			json = "{\"status\":1,\"mess\":\"请选择组件！\"}";
			return JSON;
		}
		
		json = svm.addServer(ip_list,isActive,isVm,party_id,define_id,remark);
		return JSON;
	}
	
	//修改服务器信息
	public String update(){
		if(ip.length()==0||ip==null){
			json = "{\"status\":1,\"mess\":\"IP不能为空！\"}";
			return JSON;
		}
		
		List<String> ip_list = StaticMethod.doip(ip);
		if(ip_list==null){
			json = "{\"status\":1,\"mess\":\"IP格式不正确！\"}";
			return JSON;
		}
		
		if(isVm!=null&&isVm.length()!=0&&StaticMethod.isip(isVm)==false){
			json = "{\"status\":1,\"mess\":\"物理机IP格式不正确！\"}";
			return JSON;
		}
		
		if(party_id==null||party_id.length()==0){
			json = "{\"status\":1,\"mess\":\"请选择环境！\"}";
			return JSON;
		}
		
		if(define_id==null||define_id.length()==0){
			json = "{\"status\":1,\"mess\":\"请选择组件！\"}";
			return JSON;
		}
		
		json = svm.updateServer(server_id, isActive,party_id,define_id,remark);
		return JSON;
	}
	
	//删除服务器信息
	public String delete(){
		String[] com1  = array_server_id.split(",");
		String[] com2  = array_party_id.split(",");
		if(array_server_id!=null&&com1.length!=0){
			json = svm.deleteServer(com1,com2);
		}else{
			json = "{\"status\":1,\"mess\":\"请选择服务器！\"}";
		}
		return JSON;
	}
	
	/**
	 * 查看服务器软硬件信息
	 */
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		json = svm.findServerByPage(spage);
		return JSON;
	}
	/**
	 * 更新手动输入的软硬件信息
	 */
	public String changeHSW_js() {
		json = svm.changeHSW(server);
		return JSON;
	}
	/**
	 * 导出excel
	 */
	public String download() throws Exception {
		if(spage == null) {
			spage = new ServerPage();
		}
		excelStream = svm.download(spage);
		downloadFileName=new String(("服务器软硬件信息"+StaticMethod.DateToDay(new Date())).getBytes("GBK"), "ISO-8859-1");
		return EXCEL;
	}
	/**
	 * 更改在机柜中的顺序
	 */
	public String changeSort_js() {
		json = svm.changeSort(json);
		return JSON;
	}
	/**
	 * 更改机柜位置
	 */
	public String changeCabinet_js() {
		json = svm.changeCabinet(json);
		return JSON;
	}
//set get
	public IngredientPage getPage() {
		return page;
	}
	public void setPage(IngredientPage page) {
		this.page = page;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getIsVm() {
		return isVm;
	}
	public void setIsVm(String isVm) {
		this.isVm = isVm;
	}
	public String getParty_id() {
		return party_id;
	}
	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}
	public String getDefine_id() {
		return define_id;
	}
	public void setDefine_id(String define_id) {
		this.define_id = define_id;
	}
	public String getServer_id() {
		return server_id;
	}
	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}
	public String getIg_id() {
		return ig_id;
	}
	public void setIg_id(String ig_id) {
		this.ig_id = ig_id;
	}
	public String getArray_server_id() {
		return array_server_id;
	}
	public void setArray_server_id(String array_server_id) {
		this.array_server_id = array_server_id;
	}
	public String getDownloadFileName() {
		return downloadFileName;
	}
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getArray_party_id() {
		return array_party_id;
	}
	public void setArray_party_id(String array_party_id) {
		this.array_party_id = array_party_id;
	}
	public ServerPage getSpage() {
		return spage;
	}
	public void setSpage(ServerPage spage) {
		this.spage = spage;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
}
