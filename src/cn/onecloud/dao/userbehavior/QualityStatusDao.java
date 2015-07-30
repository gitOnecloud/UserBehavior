package cn.onecloud.dao.userbehavior;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

@Repository
public class QualityStatusDao extends UtilDao
{
	public List<Object[]> getQualityStatusInfo (TrafficAllPage page, int appId)
	{
		return super.getObjsBySql("select status ,SUM(amount)  from page_view_all where status is not null and  "
				+StaticMethod.DateScope(page, "page_view_all")+"and backend_id="+appId+" group by status order by status ");
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getStatus ()
	{
		return super.ht.find("select distinct status from PageViewAll where status is not null group by status order by status");
	}
	
	public List<Object[]> getAllQualityStatusInfo(TrafficAllPage page)
	{
		return super.getObjsBySql("select status ,SUM(amount)  from page_view_all where status is not null and  "
				+StaticMethod.DateScope(page, "page_view_all")+" group by status order by status ");
	}
}
