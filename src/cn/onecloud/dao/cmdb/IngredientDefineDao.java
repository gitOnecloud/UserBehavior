package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.IngredientDefine;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientDefinePage;

@Repository("ingredientDefineDao")
public class IngredientDefineDao extends UtilDao {

//r
	/**
	 * 根据分页条件获取数据
	 */
	public List<IngredientDefine> getByPage(IngredientDefinePage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			whereSql.append(" and igd.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		if(StaticMethod.StrSize(page.getPort()) > 0) {
			whereSql.append(" and igd.port like(?)");
			params.add("%" + page.getPort() + "%");
		}
		if(StaticMethod.StrSize(page.getType()) > 0) {
			whereSql.append(" and igd.type=?");
			params.add(page.getType());
		}
		if(page.getRegister() != -1) {
			whereSql.append(" and igd.register=" + page.getRegister());
		}
		return getObjsByPage("select count(igd.id) from IngredientDefine igd " +whereSql.toString(),
				"select new IngredientDefine(igd.id, igd.name, igd.port," +
					"igd.parent.id, igd.type, igd.register, igd.color)" +
					"from IngredientDefine igd " + whereSql.toString(),
				page, params);
	}
	
	public IngredientDefine getIngredientDefine(int id){
		return super.ht.get(IngredientDefine.class, id);
	}
	
	/**
	 * 根据组件id，获取名称
	 * @param list2Str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IngredientDefine> getNameByIds(String ids) {
		return super.ht.find("select new IngredientDefine(igd.id, igd.name)" +
				"from IngredientDefine igd " +
				"where igd.id in(" + ids +")");
	}
	
	/**
	 * 获取列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IngredientDefine> getList() {
		return super.ht.find("select new IngredientDefine(igd.id, igd.name)" +
				"from IngredientDefine igd ");
	}
	
	
	/**
	 * 根据前半部分名字获取完整组件名
	 * @param name
	 * @return
	 */
	public List<IngredientDefine> getNameByPart(String name) {
		List<String> params = new ArrayList<String>();
		params.add(name + "%");
		return super.getObjs("select new IngredientDefine(igd.id, igd.name)" +
				"from IngredientDefine igd " +
				"where igd.name like(?)", params, 6);
	}
	/**
	 * 统计子节点数量
	 */
	public long countChildren(int id) {
		return (Long) ht.find("select count(obj.id) " +
				"from IngredientDefine obj " +
				"where obj.parent.id=" + id).get(0);
	}
	/**
	 * 统计此组件的服务器数量
	 */
	public long countServer(int id) {
		return (Long) ht.find("select count(obj.id) " +
				"from Ingredient obj " +
				"where obj.igDefine.id=" + id).get(0);
	}
	/**
	 * 根据id组查询子组件
	 */
	@SuppressWarnings("unchecked")
	public List<IngredientDefine> getChildren(String ids) {
		return  ht.find("select new IngredientDefine(id, name, obj.parent.id) " +
				"from IngredientDefine obj " +
				"where obj.parent.id in(" + ids + ")");
	}
	/**
	 * 查找父组件id
	 * @return 返回父组件id，没有返回0
	 */
	@SuppressWarnings("unchecked")
	public int getParentId(int id) {
		List<Integer> pId = ht.find("select ig.parent.id from IngredientDefine ig " +
				"where ig.id=" + id);
		if(pId.size() > 0) {
			if(pId.get(0) != null) {
				return pId.get(0);
			}
			return 0;
		}
		return 0;
	}
	/**
	 * 通过ids获取组件
	 */
	@SuppressWarnings("unchecked")
	public List<IngredientDefine> getIdfByIds(String ids) {
		return ht.find("from IngredientDefine ig where ig.id in(" + ids + ")");
	}
	
}
