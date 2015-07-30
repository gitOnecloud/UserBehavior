package cn.onecloud.service.cmdb;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;


import cn.onecloud.dao.cmdb.LogDao;
import cn.onecloud.model.cmdb.Log;
import cn.onecloud.util.page.cmdb.LogPage;

@Component("logManager")
public class LogManager {
	private LogDao logDao;
	@Resource(name="logDao")
	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
	
	@Resource(name="export2ExcelManager")
	private Export2EcxelManager export2ExcelManager;
	
	@SuppressWarnings("unchecked")
	public String getLog(LogPage page){
		List<Log> logs = logDao.getLog(page);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		obj.put("total", page.getCountNums());
		JSONArray rows = new JSONArray();
		for(Log l : logs){
			JSONObject row = new JSONObject();
			row.put("id", l.getId());
			row.put("IP", l.getIp());
			row.put("content", l.getContent());
			row.put("type", l.getType());
			row.put("createTime", sdf.format(l.getCreateTime()).toString());
			row.put("updateTime", sdf.format(l.getUpdateTime()).toString());
			rows.add(row);
		}
		obj.put("rows", rows);
		
		return obj.toJSONString();
	}
	
	public String addLog(String ip, String content, String type){
		Log log = new Log();
		log.setIp(ip);
		log.setContent(content);
		log.setType(type);
		log.setCreateTime(new Date());
		log.setUpdateTime(new Date());
		logDao.addLog(log);
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}
	
	public String updateLog(int id, String ip, String content, String type){
		Log log = findLogById(id);
		log.setIp(ip);
		log.setContent(content);
		log.setType(type);
		log.setUpdateTime(new Date());
		logDao.updateLog(log);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
	
	public Log findLogById(int id){
		return logDao.findLogById(id);
	}
	
	public String deleteLogByid(List<Integer> idList){
		for(int i=0; i<idList.size(); i++){
			logDao.deleteLogByid(idList.get(i));
		}
		return "{\"status\":0,\"mess\":\"删除成功！\"}";
	}
	
	public InputStream exportAll2Excel(LogPage page) throws Exception {
		List<Log> log_list = logDao.getLog(page);
		String[] columnNames = new String[]{"IP","内容","类型","创建时间","更新时间"};
		String[] columnMethods = new String[]{"getIp","getContent","getType","getCreateTime","getUpdateTime"};
		int[] columnWidths = new int[] {5000, 18000, 5000, 5200, 5200};
		return export2ExcelManager.export2Excel(
				columnNames, columnMethods, columnWidths, log_list);
	}
}
