package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.IngredientDefine;
import cn.onecloud.service.cmdb.IngredientDefineManager;
import cn.onecloud.util.page.cmdb.IngredientDefinePage;

@SuppressWarnings("serial")
@Component("ingredientdefine")
@Scope("prototype")//必须注解为多态
public class IngredientDefineAction extends CommonAction {
	
	@Resource(name="ingredientDefineManager")
	private IngredientDefineManager idm;
	private IngredientDefine igdefine;
	private IngredientDefinePage page;
	
	
//c
	public String add_js() {
		json = idm.save(igdefine);
		return JSON;
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new IngredientDefinePage();
		}
		json = idm.findByPage(page);
		return JSON;
	}
	/**
	 * 输入前半部分名字获取完整组件名
	 */
	public String names_js() {
		json = idm.findNames(json);
		return JSON;
	}
	public String children_js() {
		json = idm.findChildren(json);
		return JSON;
	}
	/**
	 * 通过输入ids获取组件
	 * @return
	 */
	public String listByIds_js() {
		json = idm.findIdfByIds(json);
		return JSON;
	}
//u
	public String change_js() {
		json = idm.change(igdefine);
		return JSON;
	}
//d
	public String remove_js() {
		json = idm.remove(json);
		return JSON;
	}
//set get 
	public IngredientDefinePage getPage() {
		return page;
	}
	public void setPage(IngredientDefinePage page) {
		this.page = page;
	}
	public IngredientDefine getIgdefine() {
		return igdefine;
	}
	public void setIgdefine(IngredientDefine igdefine) {
		this.igdefine = igdefine;
	}
	
}
