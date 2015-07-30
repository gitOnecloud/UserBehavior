package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.server.ServerDao;
import cn.onecloud.model.cmdb.Ingredient;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientPage;

@Repository("ingredientDao")
public class IngredientDao extends UtilDao{

	private ServerDao svDao;
	
	@SuppressWarnings("unchecked")
	public List<Ingredient> getCurrentIngredientlist(IngredientPage page){
		StringBuilder whereSql = getwhereSql(page);
		if(whereSql == null) {return null;}
		whereSql.append(" group by ig.igServer,ig.igParty");
		whereSql.append(" order by ig.igParty,inet_aton(ig.igServer.ip) ASC");
		List<Ingredient> ig_list =  super.ht.find("select new Ingredient(ig.id, ig.igServer," +
				"ig.isActive, ig.createTime, ig.updateTime,ig.igParty)" +
				"from Ingredient ig " + whereSql.toString());
		return ig_list;
	}
	
	/**
	 * 根据分页条件获取数据
	 */
	public List<Ingredient> getByPage(IngredientPage page) {
		StringBuilder whereSql = getwhereSql(page);
		List<String> params = new ArrayList<String>();
		if(whereSql == null) {page.countPage(0); return null;}
		whereSql.append(" group by ig.igServer,ig.igParty");
		whereSql.append(" order by inet_aton(ig.igServer.ip) ASC");
		List<Ingredient> ig_list = getObjsByPage("select count(ig.id) from Ingredient ig",
				"select new Ingredient(ig.id, ig.igServer," +
						"ig.isActive, ig.createTime, ig.updateTime,ig.igParty)" +
						"from Ingredient ig " + whereSql.toString(),
					page, params);
		//List<?> ig_lists = getCountNum("select count(*) from (select count(*) from ingredient ig group by ig.igServer_id) as t");
		//System.out.println(ig_lists.get(0).getClass().toString());
		List<?> ig_lists = super.ht.find("from Ingredient ig " + whereSql.toString());
		int c = ig_lists.size();
		//System.out.println(c);
		if(c==0){
			page.setzero();
		}else{
			page.countPage(c);
		}
		return ig_list;
	}
	
	@SuppressWarnings("unchecked")
	public List<?> getCountList(IngredientPage page){
		StringBuilder whereSql = getwhereSql(page);
		if(whereSql == null) {return null;}
		whereSql.append(" group by ig.igServer,ig.igParty");
		List<Ingredient> ig_list =  super.ht.find("select count(*) from Ingredient ig " + whereSql.toString());
		return ig_list;
	}
//	@SuppressWarnings("unchecked")
//	public <T> List<T> getCountNum(final String sql){
//		return super.ht.executeFind(
//				new HibernateCallback<List<T>>() {
//					public List<T> doInHibernate(Session s){
//						Query query = s.createSQLQuery(sql);
//						return query.list();
//					}
//				}
//			);
//	}
	
	@SuppressWarnings("unchecked")
	public List<Ingredient> findIngredient(String igServer_id,String igParty_id){
		return super.ht.find("from Ingredient ig where ig.igServer = '"+igServer_id+"' and ig.igParty = '"+igParty_id+"'");
	}
	
	@SuppressWarnings("unchecked")
	public List<Ingredient> findIngredient(String igServer_id){
		return super.ht.find("from Ingredient ig where ig.igServer = '"+igServer_id+"'");
	}
	
	public Ingredient getIngredient(String igServer_id){
		return (Ingredient) super.ht.find("from Ingredient ig where ig.igServer = '"+igServer_id+"'").get(0);
	}
	
	public Ingredient getIngredient(int id){
		return  super.ht.get(Ingredient.class, id);
	}
	
	
	private StringBuilder getwhereSql(IngredientPage page){
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		if(page != null){
			if(StaticMethod.StrSize(page.getParty_id()) > 0) {
				whereSql.append(" and ig.igParty ="+ page.getParty_id());
			}
			
			if(StaticMethod.StrSize(page.getIdf_id()) > 0) {
				whereSql.append(" and ig.igDefine=" + page.getIdf_id());
			}
			if(StaticMethod.StrSize(page.getIsActive()) > 0) {
				whereSql.append(" and ig.isActive=" + page.getIsActive());
			}
			if(StaticMethod.StrSize(page.getHost()) > 0) {
				if(page.getHost().equals("1")){
					whereSql.append(" and ig.igServer.host is null ");
				}else if(page.getHost().equals("0")){
					whereSql.append(" and ig.igServer.host is not null ");
				}
			}

			if(StaticMethod.StrSize(page.getIp()) > 0){
				if(StaticMethod.isip(page.getIp())==true){
					Server sv = svDao.getServer("ip",page.getIp());
					if(sv!=null){
						whereSql.append(" and (ig.igServer.ip='"+page.getIp()+"' or ig.igServer.host = '"+sv.getId()+"') ");
					}else{
						return null;
					}
				}else{
					whereSql.append(" and ig.igServer.ip like('%"+page.getIp()+"%')");
					//params.add("%" + page.getIp() + "%" );
				}
			}
		}
		return whereSql;
	}
	
	
	
	@Resource(name="serverDao")
	public void setSvDao(ServerDao svDao) {
		this.svDao = svDao;
	}

	/**
	 * 更新状态
	 */
	public void updateStatus(int serverId, int igdId, int status) {
		ht.bulkUpdate("update Ingredient ig" +
				" set ig.isActive=" + status +
				" where ig.igServer=" + serverId +
				"   and ig.igDefine=" + igdId);
	}
}
