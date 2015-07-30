package cn.onecloud.dao.userbehavior;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.userbehavior.PageViewAll;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;


@Repository("pageViewAllDao")
public class PageViewAllDao extends UtilDao {

	/**
	 * 获取各应用的浏览数
	 */
	@SuppressWarnings("unchecked")
	public List<PageViewAll> getAllOnApp(TrafficAllPage page) {
		return super.ht.find("select new PageViewAll(" +
				"app.user, app.appname, app.oaid, app.domain," +
				"sum(pa.amount) as hit)" +
				"from PageViewAll pa, AppInfo app " +
				"where " + StaticMethod.DateScope(page, "pa") + 
				" and pa.appinfo.id is not null and app.oaid!='NULL' and pa.appinfo.id=app.id " +
				"group by app.oaid order by hit desc");
	}
	/**
	 * 根据backend获取某一云架构的浏览数
	 */
	@SuppressWarnings("unchecked")
	public List<PageViewAll> getAllByBId(TrafficAllPage page, int appId) {
		return super.ht.find("select new PageViewAll(pa.date, pa.hour, sum(pa.amount) as hit)" +
				"from PageViewAll pa " +
				"where " + StaticMethod.DateScope(page, "pa") + 
				(appId==0? "": " and pa.appinfo.id=" + appId) +
				" group by pa." + (page.getDate().length()>7?"hour":"date"));
	}

	/**
	 * 按时间获取全部的浏览数
	 */
	@SuppressWarnings("unchecked")
	public List<PageViewAll> getAll(TrafficAllPage page) {
		return super.ht.find("select new PageViewAll(pa.date, pa.hour, sum(pa.amount) as hit)" +
				"from PageViewAll pa " +
				"where " + StaticMethod.DateScope(page, "pa") + 
				" group by pa." + (page.getDate().length()>7?"hour":"date"));
	}
	/**
	 * 获取一个月总浏览数
	 */
	public PageViewAll getAllByMonth(Calendar day) {
		return (PageViewAll) super.ht.find("select new PageViewAll(sum(pa.amount) as hit)" +
				"from PageViewAll pa " +
				"where " + StaticMethod.getDateSql(day,
						StaticMethod.DateToMouth(day.getTime()), "pa")).get(0);
	}
	/**
	 * 获取某个应用一个月总浏览数
	 * @param day 日期范围
	 * @param appId backend ID
	 */
	public PageViewAll getAllByBIdOnMonth(Calendar day, int appId) {
		return (PageViewAll) super.ht.find("select new PageViewAll(sum(pa.amount) as hit)" +
				"from PageViewAll pa " +
				"where " + StaticMethod.getDateSql(day,
					StaticMethod.DateToMouth(day.getTime()), "pa") +
				(appId==0? "": " and pa.appinfo.id=" + appId)).get(0);
	}
}
