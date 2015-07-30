package cn.onecloud.action.cmdb.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.server.Room;
import cn.onecloud.service.cmdb.server.RoomManager;
import cn.onecloud.service.cmdb.server.StyleManager;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.RoomPage;

@SuppressWarnings("serial")
@Component("room")
@Scope("prototype")//必须注解为多态
public class RoomAction extends CommonAction {
	
	@Resource(name="roomManager")
	private RoomManager rm;
	@Resource(name="styleManager")
	private StyleManager sm;
	
	private Room room;
	private RoomPage page;
	private String downloadFileName;
	private InputStream excelStream;
//c
	public String add_js() {
		json = rm.save(room);
		return JSON;
	}
//r
	public String list() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("styles", sm.findAll());
		json = jsonObj.toString();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new RoomPage();
		}
		json = rm.findByPage(page).toString();
		return JSON;
	}
	/**
	 * 获取机房样式以及机柜
	 */
	public String detail_js() {
		json = rm.findById(json);
		return JSON;
	}
	/**
	 * 导出机房服务器excel
	 */
	public String download_rd() throws IOException {
		List<Object> result = rm.download(json);
		if(result == null) {
			json = StaticMethod.inputError;
			return JSON;
		}
		excelStream = (InputStream) result.get(1);
		downloadFileName = new String((result.get(0)+"服务器分布"+StaticMethod.DateToDay(new Date())).getBytes("GBK"), "ISO-8859-1");
		return EXCEL;
	}
//u
	public String change_js() {
		json = rm.change(room);
		return JSON;
	}
//d
	public String remove_js() {
		json = rm.remove(json);
		return JSON;
	}
//set get
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public RoomPage getPage() {
		return page;
	}
	public void setPage(RoomPage page) {
		this.page = page;
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
