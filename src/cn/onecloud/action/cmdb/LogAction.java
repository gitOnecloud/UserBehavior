package cn.onecloud.action.cmdb;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.service.cmdb.LogManager;
import cn.onecloud.util.page.cmdb.LogPage;

@SuppressWarnings("serial")
@Component("log")
@Scope("prototype")//必须注解为多态
public class LogAction extends CommonAction {
//依赖注入	
	@Resource(name="logManager")
	private LogManager logManager;
	
	private LogPage page;
	
	private int id;
	private List<Integer> idList;
	private Date sdate;
	private Date edate;
	private String IP;
	private String content;
	private String type;
	
	private String keyword;
	private String sort;	//排序字段
	private String order;	//顺序：desc、asc
	
	private String downloadFileName;
	private InputStream excelStream;

	public String list() {
		return SUCCESS;
	}
	
	public String listTable(){  //按时间倒序显示第一页
		if(page == null){
			page = new LogPage();
		}
		if(keyword != null && !keyword.equals("")){
			page.setKeyword(keyword);
		}
		if(sort != null && order != null){
			page.setSort(sort);
			page.setOrder(order);
		}else{		//默认排序
			page.setSort("createTime");
			page.setOrder("desc");
		}
		json = logManager.getLog(page);
		return JSON;
	}
	
	public String addLog(){
		json = logManager.addLog(IP, content, type);
		return JSON;
	}
	
	public String updateLog(){
		json = logManager.updateLog(id, IP, content, type);
		return JSON;
	}
	
	public String delLog(){
		json = logManager.deleteLogByid(idList);
		return JSON;
	}
	
	public String exportAll2Excel() throws Exception{
		excelStream = logManager.exportAll2Excel(new LogPage());
		downloadFileName=new String("运维日志".getBytes("GBK"), "ISO-8859-1");
		return "excel";
	}
	
	public String exportCurrent2Excel() throws Exception{
		if(page == null){
			page = new LogPage();
		}
		page.setSdate(sdate);
		page.setEdate(edate);
		page.setIp(IP);
		page.setType(type);
		excelStream = logManager.exportAll2Excel(page);
		downloadFileName=new String("运维日志".getBytes("GBK"), "ISO-8859-1");
		return "excel";
	}

	public LogPage getPage() {
		return page;
	}

	public void setPage(LogPage page) {
		this.page = page;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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
	
}
