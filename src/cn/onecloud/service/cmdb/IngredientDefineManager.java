package cn.onecloud.service.cmdb;

import cn.onecloud.model.cmdb.IngredientDefine;
import cn.onecloud.util.page.cmdb.IngredientDefinePage;

public interface IngredientDefineManager {

//c
	public String save(IngredientDefine igdefine);
//r
	/**
	 * 分页获取组件信息
	 */
	public String findByPage(IngredientDefinePage page);
	
	/**
	 * 输入前半部分名字获取完整组件名
	 */
	public String findNames(String name);
	/**
	 * 查找组件之间父子关系
	 */
	public String findChildren(String id);
	/**
	 * 通过ids获取组件
	 * @param json 格式：id1-id2
	 */
	public String findIdfByIds(String json);
//u
	public String change(IngredientDefine igdefine);
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json);
}
