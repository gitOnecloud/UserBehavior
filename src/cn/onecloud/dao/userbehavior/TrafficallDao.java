package cn.onecloud.dao.userbehavior;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.userbehavior.TrafficAll;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;


@Repository("trafficallDao")
public class TrafficallDao extends UtilDao {

	/**
	 * 获取各应用的所有流量
	 */
	@SuppressWarnings("unchecked")
	public List<TrafficAll> getAllOnApp(TrafficAllPage page) {
		return super.ht.find("select new TrafficAll(" +
				"app.user, app.appname, app.oaid, app.domain," +
				"sum(ta.request_traffic) as up, sum(ta.response_traffic) as down)" +
				"from TrafficAll ta, AppInfo app " +
				"where " + StaticMethod.DateScope(page, "ta") + 
				" and ta.appinfo.id is not null and app.oaid!='NULL' and ta.appinfo.id=app.id " +
				"group by app.oaid order by down desc");
	}
	/**
	 * 根据backend获取某一云架构的流量
	 */
	@SuppressWarnings("unchecked")
	public List<TrafficAll> getAllByBId(TrafficAllPage page, int appId) {
		return super.ht.find("select new TrafficAll(ta.date, ta.hour," +
				"sum(ta.request_traffic) as up, sum(ta.response_traffic) as down)" +
				"from TrafficAll ta " +
				"where " + StaticMethod.DateScope(page, "ta") +
				(appId==0? "": " and ta.appinfo.id=" + appId) +
				" group by ta." + (page.getDate().length()>7?"hour":"date"));
	}
	/**
	 * 按时间获取全部的流量
	 */
	@SuppressWarnings("unchecked")
	public List<TrafficAll> getAll(TrafficAllPage page) {
		return super.ht.find("select new TrafficAll(ta.date, ta.hour," +
			"sum(ta.request_traffic) as up, sum(ta.response_traffic) as down)" +
			"from TrafficAll ta " +
			"where " + StaticMethod.DateScope(page, "ta") + 
			" group by ta." + (page.getDate().length()>7?"hour":"date"));
	}
	/**
	 * 获取一个月总流量
	 */
	public TrafficAll getAllByMonth(Calendar day) {
		return (TrafficAll) super.ht.find("select new TrafficAll(" +
				"sum(ta.request_traffic) as up, sum(ta.response_traffic) as down)" +
				"from TrafficAll ta " +
				"where " + StaticMethod.getDateSql(day,
						StaticMethod.DateToMouth(day.getTime()), "ta")).get(0);
	}
	/**
	 * 获取某个应用一个月总流量
	 * @param day 日期范围
	 * @param id backend ID
	 */
	public TrafficAll getAllByBIdOnMonth(Calendar day, int appId) {
		return (TrafficAll) super.ht.find("select new TrafficAll(" +
				"sum(ta.request_traffic) as up, sum(ta.response_traffic) as down)" +
				"from TrafficAll ta " +
				"where " + StaticMethod.getDateSql(day,
					StaticMethod.DateToMouth(day.getTime()), "ta") +
					(appId==0? "": " and ta.appinfo.id=" + appId)).get(0);
	}
}
