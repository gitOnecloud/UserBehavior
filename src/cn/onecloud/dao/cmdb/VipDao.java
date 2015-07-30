package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.Vip;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.VipPage;

@Repository("vipDao")
public class VipDao extends UtilDao {
//r
	/**
	 * 根据分页条件获取数据
	 */
	public List<Vip> getAllByPage(VipPage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.Str2Int(page.getParty()) > 0) {
			whereSql.append(" and vip.viParty.id=" + page.getParty());
		}
		if(StaticMethod.Str2Int(page.getIgdefine()) > 0) {
			whereSql.append(" and vip.igDefine.id=" + page.getIgdefine());
		}
		if(StaticMethod.StrSize(page.getVip()) > 0) {
			whereSql.append(" and vip.ip like(?)");
			params.add(page.getVip() + "%");
		}
		return getObjsByPage("select count(vip.id) from Vip vip " + whereSql.toString(),
				"select new Vip(vip.id, vip.ip, vip.description, igd.id, igd.name, pt.id, pt.name)" +
					"from Vip vip, IngredientDefine igd, Arena pt " + whereSql.toString() +
					" and vip.igDefine.id=igd.id and vip.viParty.id=pt.id" +
					" order by vip.id",
				page, params);
	}
	
}
