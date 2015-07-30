package cn.onecloud.dao.userbehavior;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.userbehavior.AppInfo;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;


@Repository("appInfoDao")
public class AppInfoDao extends UtilDao {

	public AppInfo getAppInfo(TrafficAllPage page) {
		List<String> params = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		if(page.getAliases()!=null && !page.getAliases().isEmpty()) {
			sql.append("and ai.aliases like (?)");
			params.add("%" + page.getAliases() + "%");
		}
		if(page.getDomain()!=null && !page.getDomain().isEmpty()) {
			sql.append(" and ai.domain like (?)");
			params.add("%" + page.getDomain() + "%");
		}
		if(page.getOaid()!=null && !page.getOaid().isEmpty()) {
			sql.append(" and ai.oaid like (?)");
			params.add("%" + page.getOaid());
		}
		//如果不查询就返回全部
		if(params.size() == 0) {
			return new AppInfo(0, "全部", "全部", "全部", "全部", "全部");
		}
		List<AppInfo> apps = super.getObjs("from AppInfo ai where 1=1 " + sql, params, 1);
		if(apps.size() == 0) {
			return null;
		}
		return apps.get(0);
	}

	
}
