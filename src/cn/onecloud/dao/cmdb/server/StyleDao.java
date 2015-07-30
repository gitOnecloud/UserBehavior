package cn.onecloud.dao.cmdb.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.StylePage;

@Repository("styleDao")
public class StyleDao extends UtilDao {

//c
//r
	/**
	 * 获取全部样式
	 */
	@SuppressWarnings("unchecked")
	public List<Style> getAll() {
		return super.ht.find("select new Style(s.id, s.name) from Style s");
	}
	/**
	 * 分页查找
	 */
	public List<Style> getAllByPage(StylePage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			whereSql.append(" and style.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		return getObjsByPage("select count(style.id) from Style style " + whereSql.toString(),
				"from Style style " + whereSql.toString(),
				page, params);
	}
//u
//d

}
