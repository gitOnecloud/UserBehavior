package cn.onecloud.service.userbehavior;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.userbehavior.AppInfoDao;
import cn.onecloud.dao.userbehavior.PageViewAllDao;
import cn.onecloud.dao.userbehavior.TrafficallDao;
import cn.onecloud.model.userbehavior.AppInfo;
import cn.onecloud.model.userbehavior.PageViewAll;
import cn.onecloud.model.userbehavior.TrafficAll;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

@Service("trafficallManager")
public class TrafficallManager {

	private TrafficallDao tad;
	@Resource(name="trafficallDao")
	public void setTad(TrafficallDao tad) {
		this.tad = tad;
	}
	private PageViewAllDao pad;
	@Resource(name="pageViewAllDao")
	public void setPad(PageViewAllDao pad) {
		this.pad = pad;
	}
	private AppInfoDao aid;
	@Resource(name="appInfoDao")
	public void setAid(AppInfoDao aid) {
		this.aid = aid;
	}
	
	/**
	 * 获取一定范围内全部云架构的上下行流量和浏览数
	 * @param page 按月或者按天
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String findAll(TrafficAllPage page) {
		List<TrafficAll> talls = tad.getAllOnApp(page);
		List<PageViewAll> palls = pad.getAllOnApp(page);
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		for(TrafficAll tall : talls) {
			JSONObject tallJson = new JSONObject();
			AppInfo ai = tall.getAppinfo();
			tallJson.put("user", ai.getUser());
			tallJson.put("appname", ai.getAppname());
			tallJson.put("oaid", ai.getOaid());
			tallJson.put("domain", ai.getDomain());
			tallJson.put("request_traffic", tall.getRequest_traffic());
			tallJson.put("response_traffic", tall.getResponse_traffic());
			array.add(tallJson);
		}
		result.put("trafficall", array);
		JSONArray array2 = new JSONArray();
		for(PageViewAll pall : palls) {
			JSONObject pallJson = new JSONObject();
			AppInfo ai = pall.getAppinfo();
			pallJson.put("user", ai.getUser());
			pallJson.put("appname", ai.getAppname());
			pallJson.put("oaid", ai.getOaid());
			pallJson.put("domain", ai.getDomain());
			pallJson.put("amount", pall.getAmount());
			array2.add(pallJson);
		}
		result.put("pageviewall", array2);
		result.put("date", page.getDate());
		return result.toString();
	}
	/**
	 * 获取一定范围内某一个云架构的上下行流量和浏览数
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String findApp(TrafficAllPage page) {
		AppInfo ai = aid.getAppInfo(page);
		if(ai == null) {
			return "{\"status\":1,\"mess\":\"未找到！\"}";
		}
		JSONObject result = new JSONObject();
		result.put("user", ai.getUser());
		result.put("appname", ai.getAppname());
		result.put("oaid", ai.getOaid());
		result.put("domain", ai.getDomain());
		result.put("aliases", ai.getAliases());
		//使用TrafficAll来暂时保存日期(domain)和浏览数(id)
		List<TrafficAll> trafficPages = new ArrayList<TrafficAll>();
		if(page.getDate().matches("\\d{4}")) {//按年
			Calendar day = StaticMethod.YearToDate(page.getDate());
			for(int i=1; i<13; i++) {
				day.set(Calendar.MONTH, i-1);
				TrafficAll tall = tad.getAllByBIdOnMonth(day, ai.getId());
				PageViewAll pall = pad.getAllByBIdOnMonth(day, ai.getId());
				trafficPages.add(new TrafficAll(i+"月",
						tall.getRequest_traffic(), tall.getResponse_traffic(),
						pall.getAmount(),0,0));
				
			}
		} else {
			List<TrafficAll> talls = tad.getAllByBId(page, ai.getId());
			List<PageViewAll> palls = pad.getAllByBId(page, ai.getId());
			if(page.getDate().length() > 7) {//按天
				for(int i=0; i<24; i++) {
					trafficPages.add(new TrafficAll(i+"时", 0, 0, 0));
				}
				for(TrafficAll tall : talls) {
					TrafficAll traffic = trafficPages.get(tall.getHour());
					traffic.setRequest_traffic(tall.getRequest_traffic());
					traffic.setResponse_traffic(tall.getResponse_traffic());
					traffic.setUploadBandWidth(tall.getRequest_traffic()*8.0/(3600*1024));
					traffic.setDownBandWidth(tall.getResponse_traffic()*8.0/(3600*1024));
				}
				for(PageViewAll pall : palls) {
					TrafficAll traffic = trafficPages.get(pall.getHour());
					traffic.setId(pall.getAmount());
				}
			} else {//按月
				Map<Integer, Integer> dayIndex = new HashMap<Integer, Integer>();//日期和流量的索引
				Calendar day = Calendar.getInstance();
				day.set(Integer.parseInt(page.getDate().substring(0, 4)),
						Integer.parseInt(page.getDate().substring(5))-2, 27);
				while(true) {
					day.add(Calendar.DATE, 1);
					int date = day.get(Calendar.DATE);
					if(date != 1) {
						dayIndex.put(date, trafficPages.size());
						trafficPages.add(new TrafficAll(date+"日", 0, 0, 0));
					} else {
						break;
					}
				}
				for(int i=1; i<28; i++) {
					dayIndex.put(i, trafficPages.size());
					trafficPages.add(new TrafficAll(i+"日", 0, 0, 0));
				}
				for(TrafficAll tall : talls) {
					TrafficAll traffic = trafficPages.get(dayIndex.get(tall.getDate().getDate()));
					traffic.setRequest_traffic(tall.getRequest_traffic());
					traffic.setResponse_traffic(tall.getResponse_traffic());
					traffic.setUploadBandWidth(tall.getRequest_traffic()*8.0/(24*3600*1024));
					traffic.setDownBandWidth(tall.getResponse_traffic()*8.0/(24*600*1024));
				}
				for(PageViewAll pall : palls) {
					TrafficAll traffic = trafficPages.get(dayIndex.get(pall.getDate().getDate()));
					traffic.setId(pall.getAmount());
				}
			}
		}
		JSONArray array = new JSONArray();
		for(TrafficAll tall : trafficPages) {
			JSONObject tallJson = new JSONObject();
			tallJson.put("date", tall.getDomain());
			tallJson.put("request_traffic", tall.getRequest_traffic());
			tallJson.put("response_traffic", tall.getResponse_traffic());
			tallJson.put("amount", tall.getId());
			tallJson.put("uploadBandWidth", tall.getUploadBandWidth() == null?0:StaticMethod.numberToString(tall.getUploadBandWidth()));
			tallJson.put("downBandWidth", tall.getDownBandWidth() == null?0:StaticMethod.numberToString(tall.getDownBandWidth()));
			array.add(tallJson);
		}
		result.put("trafficpage", array);
		result.put("date", page.getDate());
		return result.toString();
	}
	/**
	 * 获取总的流量和浏览数
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String findSum(TrafficAllPage page) {
		JSONObject result = new JSONObject();
		//使用TrafficAll来暂时保存日期(domain)和浏览数(id)
		List<TrafficAll> trafficPages = new ArrayList<TrafficAll>();
		if(page.getDate().matches("\\d{4}")) {//按年
			Calendar day = StaticMethod.YearToDate(page.getDate());
			for(int i=1; i<13; i++) {
				day.set(Calendar.MONTH, i-1);
				TrafficAll tall = tad.getAllByMonth(day);
				PageViewAll pall = pad.getAllByMonth(day);
				trafficPages.add(new TrafficAll(i+"月",
						tall.getRequest_traffic(), tall.getResponse_traffic(),
						pall.getAmount()));
				
			}
		} else {
			List<TrafficAll> talls = tad.getAll(page);
			List<PageViewAll> palls = pad.getAll(page);
			if(page.getDate().length() > 7) {//按天
				for(int i=0; i<24; i++) {
					trafficPages.add(new TrafficAll(i+"时", 0, 0, 0));
				}
				for(TrafficAll tall : talls) {
					TrafficAll traffic = trafficPages.get(tall.getHour());
					traffic.setRequest_traffic(tall.getRequest_traffic());
					traffic.setResponse_traffic(tall.getResponse_traffic());
				}
				for(PageViewAll pall : palls) {
					TrafficAll traffic = trafficPages.get(pall.getHour());
					traffic.setId(pall.getAmount());
				}
			} else if(page.getDate().length() > 4) {//按月
				Map<Integer, Integer> dayIndex = new HashMap<Integer, Integer>();//日期和流量的索引
				Calendar day = Calendar.getInstance();
				day.set(Integer.parseInt(page.getDate().substring(0, 4)),
						Integer.parseInt(page.getDate().substring(5))-2, 27);
				while(true) {
					day.add(Calendar.DATE, 1);
					int date = day.get(Calendar.DATE);
					if(date != 1) {
						dayIndex.put(date, trafficPages.size());
						trafficPages.add(new TrafficAll(date+"日", 0, 0, 0));
					} else {
						break;
					}
				}
				for(int i=1; i<28; i++) {
					dayIndex.put(i, trafficPages.size());
					trafficPages.add(new TrafficAll(i+"日", 0, 0, 0));
				}
				for(TrafficAll tall : talls) {
					TrafficAll traffic = trafficPages.get(dayIndex.get(tall.getDate().getDate()));
					traffic.setRequest_traffic(tall.getRequest_traffic());
					traffic.setResponse_traffic(tall.getResponse_traffic());
				}
				for(PageViewAll pall : palls) {
					TrafficAll traffic = trafficPages.get(dayIndex.get(pall.getDate().getDate()));
					traffic.setId(pall.getAmount());
				}
			}
		}
		JSONArray array = new JSONArray();
		for(TrafficAll tall : trafficPages) {
			JSONObject tallJson = new JSONObject();
			tallJson.put("date", tall.getDomain());
			tallJson.put("request_traffic", tall.getRequest_traffic());
			tallJson.put("response_traffic", tall.getResponse_traffic());
			tallJson.put("amount", tall.getId());
			array.add(tallJson);
		}
		result.put("trafficpage", array);
		result.put("date", page.getDate());
		return result.toString();
	}

}
