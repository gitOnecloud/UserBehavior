package cn.onecloud.service.billing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.billing.BillingDao;
import cn.onecloud.model.billing.Billing;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.billing.BillingPage;

@Service("billingManager")
public class BillingManager {

	private BillingDao bdao;
	@Resource(name="billingDao")
	public void setBdao(BillingDao bdao) {
		this.bdao = bdao;
	}
//r
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String findAll(BillingPage page) {
		Billing billing = bdao.getAcount(page);
		if(billing == null) {
			return "{\"status\":1,\"mess\":\"未找到！\"}";
		}
		JSONObject result = new JSONObject();
		result.put("user", billing.getUser());
		result.put("appname", billing.getAppname());
		result.put("oaid", billing.getOaid());
		List<Billing> billPages = new ArrayList<Billing>();
		if(page.getDate().matches("\\d{4}")) {//按年
			Calendar day = StaticMethod.YearToDate(page.getDate());
			for(int i=1; i<13; i++) {
				day.set(Calendar.MONTH, i-1);
				Billing bill = bdao.getAllByBIdOnMonth(day, billing.getAccountId());
				bill.setDay(i + "月");
				billPages.add(bill);
			}
		} else {
			List<Billing> bills = bdao.getAllByCId(page, billing.getAccountId());
			if(page.getDate().length() > 7) {//按天
				/*for(int i=0; i<24; i++) {
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
				}*/
			} else {//按月
				Map<Integer, Integer> dayIndex = new HashMap<Integer, Integer>();//日期和流量的索引
				Calendar day = Calendar.getInstance();
				day.set(Integer.parseInt(page.getDate().substring(0, 4)),
						Integer.parseInt(page.getDate().substring(5))-2, 27);
				while(true) {
					day.add(Calendar.DATE, 1);
					int date = day.get(Calendar.DATE);
					if(date != 1) {
						dayIndex.put(date, billPages.size());
						billPages.add(new Billing(date+"日"));
					} else {
						break;
					}
				}
				for(int i=1; i<28; i++) {
					dayIndex.put(i, billPages.size());
					billPages.add(new Billing(i+"日"));
				}
				for(Billing bill : bills) {
					int i = dayIndex.get(bill.getDate().getDate());
					bill.setDay(billPages.get(i).getDay());
					billPages.set(i, bill);
				}
			}
		}
		JSONArray array = new JSONArray();
		for(Billing bill : billPages) {
			JSONObject billJson = new JSONObject();
			billJson.put("date", bill.getDay());
			billJson.put("cpuAmount", bill.getCpuAmount());
			billJson.put("cpuFee", bill.getCpuFee());
			billJson.put("memoryAmount", bill.getMemoryAmount());
			billJson.put("memoryFee", bill.getMemoryFee());
			billJson.put("bandwidthAmount", bill.getBandwidthAmount());
			billJson.put("bandwidthFee", bill.getBandwidthFee());
			billJson.put("storageAmount", bill.getStorageAmount());
			billJson.put("storageFee", bill.getStorageFee());
			billJson.put("vmAmount", bill.getVmAmount());
			billJson.put("vmFee", bill.getVmFee());
			billJson.put("totalFee", bill.getTotalFee());
			array.add(billJson);
		}
		result.put("billpage", array);
		result.put("date", page.getDate());
		return result.toString();
	}

}
