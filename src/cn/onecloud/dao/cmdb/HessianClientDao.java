package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.HessianClient;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.HessianClientPage;

@Repository("hessianClientDao")
public class HessianClientDao extends UtilDao {

	
//c
//r
	public List<HessianClient> getAllByPage(HessianClientPage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			whereSql.append(" and hc.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		if(StaticMethod.StrSize(page.getDomain()) > 0) {
			whereSql.append(" and hc.domain like(?)");
			params.add("%" + page.getDomain() + "%");
		}
		return getObjsByPage("select count(*) from HessianClient hc " + whereSql.toString(),
				"from HessianClient hc " + whereSql.toString(),
				page, params);
	}
	/**
	 * 根据ip和域名获取密钥
	 */
	public HessianClient getHcByDomain(String ip, String domain) {
		List<String> params = new ArrayList<String>();
		params.add(domain);
		params.add("%#" + ip + "%#");
		List<HessianClient> hcs = getObjs("from HessianClient hc" +
				" where hc.domain=? and hc.ip like(?)", params, 1);
		if(hcs.size() == 0) {
			return null;
		} else {
			return hcs.get(0);
		}
	}
//u
	/**
	 * 更新rsa密钥
	 */
	public void updateRSA(HessianClient hc) {
		ht.bulkUpdate("update HessianClient hc" +
				" set hc.pubkey='" + hc.getPubkey() +
				"', hc.prikey='" + hc.getPrikey() +
				"', hc.modkey='" + hc.getModkey() +
				"' where hc.id=" + hc.getId());
	}
//d

}
