package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.service.cmdb.IngredientManager;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientPage;

@SuppressWarnings("serial")
@Component("ingredient")
@Scope("prototype")//必须注解为多态
public class IngredientAction extends CommonAction {
	
	@Resource(name="ingredientManager")
	private IngredientManager idcm;
	
	private IngredientPage page;
	
	
	public String list(){
		return SUCCESS;
	}

	public String list_js(){
		if (StaticMethod.isInteger(page.getIdf_id())==false){
			page.setIdf_id("");
		}
		json = idcm.get_list(page);
		return JSON;
	}
//u
	/**
	 * nagios组件监控更新
	 */
	public String changeStatus_js() {
		json = idcm.changeStatus(json);
		return JSON;
	}
	
	
//set get 
	public IngredientPage getPage() {
		return page;
	}
	public void setPage(IngredientPage page) {
		this.page = page;
	}
	
}
