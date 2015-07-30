package cn.onecloud.dao.userbehavior;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.userbehavior.PispowerPageView;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.Page;

@Repository("pageViewDao")
public class PageViewDao extends UtilDao {

	@SuppressWarnings("unchecked")
	public List<PispowerPageView> getAll(Page page) {
		return super.ht.find("select new PispowerPageView(" +
				"url.descrip, SUM(pv.amount) as hit)" +
				"from PispowerPageView pv, PispowerUrl url " +
				"where " + StaticMethod.DateScope(page, "pv") + 
				" and pv.url.id=url.id " +
				"group by url.descrip order by hit desc");
	}

}
