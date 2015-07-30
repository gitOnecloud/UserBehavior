package cn.onecloud.service.cmdb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.IngredientDao;
import cn.onecloud.dao.cmdb.IngredientDefineDao;
import cn.onecloud.dao.cmdb.ArenaDao;
import cn.onecloud.dao.cmdb.server.ServerDao;
import cn.onecloud.model.cmdb.Ingredient;
import cn.onecloud.model.cmdb.IngredientDefine;
import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientPage;

@Component("ingredientManager")
public class IngredientManagerImpl implements IngredientManager {
	
	private IngredientDao igDao;
	private IngredientDefineDao igdDao;
	private ArenaDao pDao;
	private ServerDao sDao;
	
	
	@SuppressWarnings("unchecked")
	public String get_list(IngredientPage page){
		List<Ingredient> ig_list = igDao.getByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		if(ig_list!=null&&ig_list.size()!=0){
			for(Ingredient ig : ig_list){
				JSONObject idJson = new JSONObject();
				Server s = sDao.getServer(ig.getIgServer().getId());
				Arena p = pDao.getParty(ig.getIgParty().getId());
				//IngredientDefine igd = igdDao.getIngredientDefine(ig.getIgDefine().getId());
				List<Ingredient> ig_id_list = igDao.findIngredient(String.valueOf(ig.getIgServer().getId()),String.valueOf(ig.getIgParty().getId()));
				String idf = "", idf_id = "", ifd_active = "";
				for(Ingredient ig_id : ig_id_list){
					idf = idf + ig_id.getIgDefine().getName() + ",";
					idf_id = idf_id + ig_id.getIgDefine().getId() + ",";
					ifd_active = ifd_active + ig_id.getIsActive() + ",";
				}
				idJson.put("ig_id", ig.getId());  //无效id
				idJson.put("ip", s.getIp());
				idJson.put("server_id", s.getId());  //id
				idJson.put("party", p.getName());
				idJson.put("party_id", p.getId());  //id
				idJson.put("idf", idf.substring(0, idf.length()-1));
				idJson.put("idf_id", idf_id.substring(0, idf_id.length()-1));   //id
				idJson.put("ifd_active", ifd_active.substring(0, ifd_active.length()-1));//各组件状态
				idJson.put("isActive", s.getIsActive());
				if(s.getHost()==null)  idJson.put("host_ip", "");
				else idJson.put("host_ip", s.getHost().getIp());
				idJson.put("remark", s.getRemark());
				array.add(idJson);
			}
		}
		json.put("rows", array);
		return json.toJSONString();
	}
	
	
	/**
	 * 环境列表
	 * @return
	 */
	public String party_list(){
		String json = "";
		List<Arena> party_list =  pDao.getList();
		String party_id ="[";
		String staff ="[";
		if(party_list!=null&&party_list.size()!=0){
			for(Arena p : party_list){
				party_id = party_id + "\""+String.valueOf(p.getId())+"\",";
				staff = staff + "\""+p.getName()+"\",";
			}
		}
		party_id = party_id + "\"\"]";
		staff = staff + "\"\"]";
		json = "{\"status\":0,\"party_id\":"+party_id+",\"staff\":"+staff+"}";
		return json;
	}
	
	
	/**
	 * 组件列表
	 * @return
	 */
	public String idf_list(){
		String json = "";
		List<IngredientDefine> idf_list =  igdDao.getList();
		String idf_id = "[";
		String staff ="[";
		if(idf_list!=null&&idf_list.size()!=0){
			for(IngredientDefine idf : idf_list){
				idf_id = idf_id +"\""+idf.getId()+"\",";
				staff = staff + "\""+idf.getName()+"\",";
			}
		}
		idf_id = idf_id + "\"\"]";
		staff = staff + "\"\"]";
		json = "{\"status\":0,\"idf_id\":"+idf_id+",\"staff\":"+staff+"}";
		return json;
	}
	
//u
	/**
	 * nagios组件监控更新
	 * @param json serverId A igdId_igdId A status_status => 5A3A0
	 * @return
	 */
	public String changeStatus(String json) {
		if(StaticMethod.StrSize(json) < 5) {
			return StaticMethod.inputError;
		}
		String[] str = json.split("A");
		if(str.length != 3) {
			return StaticMethod.inputError;
		}
		Set<Integer> idSet = new HashSet<Integer>();
		int serverId = 0;
		try {
			serverId = Integer.parseInt(str[0]);
		} catch(NumberFormatException e) {
			return StaticMethod.inputError;
		}
		String[] ids = str[1].split("_");
		String[] actives = str[2].split("_");
		if(ids.length==0 || ids.length !=actives.length) {
			return StaticMethod.inputError;
		}
		int id = 0;
		for(int i=0; i<ids.length; i++) {
			try {
				id = Integer.parseInt(ids[i]);
				if(idSet.add(id)) {
					igDao.updateStatus(serverId, id, Integer.parseInt(actives[i]));
				}
			} catch(NumberFormatException e) {}
		}
		
		return StaticMethod.changeSucc;
	}
//set get
	@Resource(name="arenaDao")
	public void setpDao(ArenaDao pDao) {
		this.pDao = pDao;
	}
	@Resource(name="ingredientDao")
	public void setIgDao(IngredientDao igDao) {
		this.igDao = igDao;
	}
	@Resource(name="ingredientDefineDao")
	public void setIgdDao(IngredientDefineDao igdDao) {
		this.igdDao = igdDao;
	}
	@Resource(name="serverDao")
	public void setsDao(ServerDao sDao) {
		this.sDao = sDao;
	}
}
