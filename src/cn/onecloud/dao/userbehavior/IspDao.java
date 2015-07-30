package cn.onecloud.dao.userbehavior;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.userbehavior.IpView;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.Page;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

@Repository("ispDao")
public class IspDao extends UtilDao {
	@SuppressWarnings("unchecked")
	public List<IpView> getAll(Page page, int appId) {
		return super.ht.find("select new IpView(" +
				"isp.isp, SUM(iv.amount) as hit)" +
				"from IpView iv, Isp isp " +
				"where " + StaticMethod.DateScope(page, "iv") +
				(appId==0? "": " and iv.appinfo.id=" + appId) +
				" and iv.isp.id=isp.id " +
				"group by isp.isp order by hit desc");
		
	}

	public List<Object[]> getIpSum(TrafficAllPage page, int appId) {
		return super.getObjsBySql("select isp, count(ip) as ipnums from (" +
			"select isp.isp, ip_view.ip " +
			"from ip_view, isp " +
			"where " + StaticMethod.DateScope(page, "ip_view") +
				(appId==0? "": " and ip_view.backend_id=" + appId) +
				" and ip_view.isp_id=isp.id " +
				"group by ip_view.ip) as ipcount " +
			"group by isp order by ipnums desc");
	}
}
