package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.arena.Basket;
import cn.onecloud.model.cmdb.arena.Rouyer;
import cn.onecloud.model.cmdb.arena.Satellite;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.ArenaPage;

@Repository("arenaDao")
public class ArenaDao extends UtilDao {

	
	/**
	 * 获取全部列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Arena> getList() {
		return super.ht.find("from Arena");
	}
	
	public Arena getParty(int id){
		return super.ht.get(Arena.class, id);
	}
	
	public List<Arena> getAllByPage(ArenaPage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			whereSql.append(" and arena.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		if(StaticMethod.StrSize(page.getDomain()) > 0) {
			whereSql.append(" and arena.domain like(?)");
			params.add("%" + page.getDomain() + "%");
		}
		if(StaticMethod.StrSize(page.getType()) > 0) {
			whereSql.append(" and arena.type like(?)");
			params.add("%" + page.getType() + "%");
		}
		return getObjsByPage("select count(arena.id) from Arena arena " + whereSql.toString(),
					"from Arena arena " + whereSql.toString() +
					" order by arena.id",
				page, params);
	}
	/**
	 * 统计服务器数量
	 */
	public long countServer(int id) {
		return (Long) ht.find("select count(obj.id) " +
				"from Ingredient obj " +
				"where obj.igParty.id=" + id).get(0);
	}
	/**
	 * 统计vip数量
	 */
	public long countVip(int id) {
		return (Long) ht.find("select count(obj.id) " +
				"from Vip obj " +
				"where obj.viParty.id=" + id).get(0);
	}
	/**
	 * 统计Basket数量
	 */
	public long countBasket(int id) {
		return (Long) ht.find("select count(obj.id) " +
				"from Basket obj " +
				"where obj.bkSatellite.id=" + id).get(0);
	}
	public long countSatellite(int id) {
		return (Long) ht.find("select count(obj.id) " +
				"from Satellite obj " +
				"where obj.slRouyer.id=" + id).get(0);
	}
	/**
	 * 获取全部rouyer
	 */
	@SuppressWarnings("unchecked")
	public List<Rouyer> getRouyer() {
		return super.ht.find("from Rouyer");
	}
	/**
	 * 获取全部Satellite
	 */
	@SuppressWarnings("unchecked")
	public List<Satellite> getSatellite() {
		return super.ht.find("from Satellite");
	}
	/**
	 * 获取全部Basket
	 */
	@SuppressWarnings("unchecked")
	public List<Basket> getBasket() {
		return super.ht.find("from Basket");
	}
	/**
	 * 获取全部Satellite名字
	 */
	@SuppressWarnings("unchecked")
	public List<Arena> getSatelliteName() {
		return super.ht.find("from Arena where type='satellite'");
	}
//d
	/**
	 * 删除basket
	 */
	public void delete(int aId) {
		ht.bulkUpdate("delete from Basket bt" +
				" where bt.bkArena=" + aId);
		
	}

}
