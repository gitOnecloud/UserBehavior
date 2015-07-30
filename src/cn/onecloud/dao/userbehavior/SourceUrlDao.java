package cn.onecloud.dao.userbehavior;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.userbehavior.PispowerSourceUrl;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.userbehavior.Page;

@Repository("sourceUrlDao")
public class SourceUrlDao extends UtilDao {
	
	@SuppressWarnings("unchecked")
	public List<PispowerSourceUrl> getAll(Page page) {
		return super.ht.find("select new PispowerSourceUrl(" +
				"su.sourceURL, SUM(su.amount) as hit)" +
				"from PispowerSourceUrl su " +
				"where " + StaticMethod.DateScope(page, "su") + 
				"group by su.sourceURL order by hit desc");
	}
}
