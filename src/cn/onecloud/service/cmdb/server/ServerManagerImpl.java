package cn.onecloud.service.cmdb.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Component;


import cn.onecloud.dao.cmdb.ArenaDao;
import cn.onecloud.dao.cmdb.HardwareDao;
import cn.onecloud.dao.cmdb.IngredientDao;
import cn.onecloud.dao.cmdb.IngredientDefineDao;
import cn.onecloud.dao.cmdb.LogDao;
import cn.onecloud.dao.cmdb.SoftwareDao;
import cn.onecloud.dao.cmdb.server.CabinetDao;
import cn.onecloud.dao.cmdb.server.ServerDao;
import cn.onecloud.model.cmdb.Hardware;
import cn.onecloud.model.cmdb.Ingredient;
import cn.onecloud.model.cmdb.IngredientDefine;
import cn.onecloud.model.cmdb.Log;
import cn.onecloud.model.cmdb.Software;
import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.server.Cabinet;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.service.cmdb.Export2EcxelManager;
import cn.onecloud.service.util.Export2Excel;
import cn.onecloud.util.LocalShell;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientPage;
import cn.onecloud.util.page.cmdb.ServerPage;

@Component("serverManager")
public class ServerManagerImpl implements ServerManager {
	
	private ArenaDao aDao;
	private ServerDao svDao;
	private HardwareDao hwDao;
	private SoftwareDao swDao;
	private IngredientDao igDao;
	private IngredientDefineDao igDefineDao;
	
	
	@Resource(name="cabinetDao")
	private CabinetDao cdao;
	
	private LogDao logDao;
	@Resource(name="logDao")
	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
	@Resource(name="export2ExcelManager")
	private Export2EcxelManager export2ExcelManager;

	/**
	 * 获取execl所需的list
	 * @return
	 */
	public List<List<String>> get_current_server_list(IngredientPage page){
		List<List<String>> server_list = new ArrayList<List<String>>();
		List<List<String>> hb_server_list = new ArrayList<List<String>>();
		List<Ingredient> ig_list = igDao.getCurrentIngredientlist(page);
		if(ig_list!=null&&ig_list.size()!=0){
			int i = 0;
			//int c = ((java.math.BigInteger)igDao.getCountNum("select max(c) from (select count(*) as c from ingredient ig group by ig.igServer_id) as t").get(0)).intValue();
			List<?> igCountList = igDao.getCountList(page);
			int c = 0;
			for(int cc=0;cc<igCountList.size();cc++){
				if(c < ((Long)igCountList.get(cc)).intValue()) 
					c = ((Long)igCountList.get(cc)).intValue();
			}
			for(Ingredient ig : ig_list){
				List<String> server_com = new ArrayList<String>();
				Server s = svDao.getServer(ig.getIgServer().getId());
				Arena p = aDao.getParty(ig.getIgParty().getId());
				if(i!=ig.getIgParty().getId()){
					List<String> party_com1 = new ArrayList<String>();
					party_com1.add("");
					party_com1.add("");
					server_list.add(party_com1);
					
					List<String> party_com2 = new ArrayList<String>();
					party_com2.add(p.getName());
					server_list.add(party_com2);
					i = ig.getIgParty().getId();
				}
				server_com.add(s.getIp());
				
				if(s.getHost()==null)  server_com.add("");
				else{
					Server h = svDao.getServer(s.getHost().getId());
					server_com.add(h.getIp());
				} 
				
				List<Ingredient> ig_id_list = igDao.findIngredient(String.valueOf(ig.getIgServer().getId()),String.valueOf(ig.getIgParty().getId()));
				for(Ingredient ig_id : ig_id_list){
					//System.out.println(ig_id.getIgDefine().getId()+"aaaaaaaaa");
					IngredientDefine igd = igDefineDao.getIngredientDefine(ig_id.getIgDefine().getId());
					server_com.add(igd.getName()+"#"+igd.getColor());
				}
				if(ig_id_list.size() < c){
					for(int x=0;x<c-ig_id_list.size();x++){
						server_com.add("");
					}
				}
				server_com.add(s.getRemark());
				server_list.add(server_com);
			}
			
			
			Iterator<List<String>> hb_server_lists = server_list.iterator();
			List<String> fsl = hb_server_lists.next();
			String[] f_ips = null;
			String[] ips = null;
			int ipo = 0;
			int o = 0;
			
			while(hb_server_lists.hasNext()){
				if(fsl.size()<3){
					hb_server_list.add(fsl);
					fsl = hb_server_lists.next();
					//f_ips = fsl.get(0).split("//.");
					if(!hb_server_lists.hasNext()){
						hb_server_list.add(fsl);
					}
					continue;
				}else{
					f_ips = fsl.get(0).split("\\.");
				}
				List<String> sl = hb_server_lists.next();
				if(sl.size()<3){
					hb_server_list.add(fsl);
					fsl = sl;
					//f_ips = fsl.get(0).split("//.");
					continue;
				}else{
					ips = sl.get(0).split("\\.");
				}
				
				//if(fsl.size() == sl.size()){
					if(f_ips[0].equals(ips[0])&&f_ips[1].equals(ips[1])&&f_ips[2].equals(ips[2])
							&&(String.valueOf((Integer.valueOf(f_ips[3])+1))).equals(ips[3])){
						//System.out.println(fsl.size());
						for(int q=1;q<fsl.size()-1;q++){
							if(!((fsl.get(q)).equals(sl.get(q)))){
								o=1;
							}
						}
					}else{
						String ip = "";
						if(ipo==0){
							ip = f_ips[0]+"."+f_ips[1]+"."+f_ips[2]+"."+f_ips[3];
						}else{
							String[] c_ips = f_ips;
							int cc = Integer.valueOf(c_ips[3])-ipo;
							ip = c_ips[0] +"."+ c_ips[1] +"."+ c_ips[2] +"."+ String.valueOf(cc) +".."+ c_ips[3];
						}
						fsl.set(0, ip);
						hb_server_list.add(fsl);
						fsl = sl;
						ipo = 0;
						o = 0;
						
						if(!hb_server_lists.hasNext()){
							//ipo++;
							//String ip = "";
								//String[] c_ips = f_ips;
								//int cc = Integer.valueOf(c_ips[3])-ipo;
							//	ip = ips[0] +"."+ ips[1] +"."+ ips[2] +"."+ ips[3];
							//sl.set(0, ip);
							hb_server_list.add(sl);
						}
						continue;
					}
				//}
				if(o==1){
					String ip = "";
					if(ipo==0){
						ip = f_ips[0]+"."+f_ips[1]+"."+f_ips[2]+"."+f_ips[3];
					}else{
						String[] c_ips = f_ips;
						int cc = Integer.valueOf(c_ips[3])-ipo;
						ip = c_ips[0] +"."+ c_ips[1] +"."+ c_ips[2] +"."+ String.valueOf(cc) +".."+ c_ips[3];
					}
					fsl.set(0, ip);
					hb_server_list.add(fsl);
					fsl = sl;
					ipo = 0;
					o = 0;
					
					if(!hb_server_lists.hasNext()){
						//ipo++;
						//ip = ips[0] +"."+ ips[1] +"."+ ips[2] +"."+ ips[3];
						//sl.set(0, ip);
						hb_server_list.add(sl);
					}
					continue;
				}
				if(hb_server_lists.hasNext()){
					//hb_server_list.add(fsl);
					fsl = sl;
					//ipo = 0;
					ipo++;
				}else{
					//ipo++;
					String ip = "";
					
						String[] c_ips = f_ips;
						int cc = Integer.valueOf(c_ips[3])-ipo;
						ip = c_ips[0] +"."+ c_ips[1] +"."+ c_ips[2] +"."+ String.valueOf(cc) +".."+ ips[3];
					
					fsl.set(0, ip);
					hb_server_list.add(fsl);
				}
			}
		}
		return hb_server_list;
	}
	
	
	/**
	 * 查找服务器软硬件信息
	 * @param server_id
	 * @return
	 */
	public String findHwSw(int server_id){
		Hardware hw = hwDao.getHardware(server_id);
		Software sw = swDao.getSoftware(server_id);
		if(hw==null||sw==null) return "{\"total\":0 ,\"rows\":[]}";
		String json = "";
		json = "{\"total\":0 ," +
				"\"rows\":[";
		json = json.concat("{\"hw_p\":\"cpu型号\",\"hw_c\":\""+hw.getCpu()+"\",\"sw_p\":\"主机名\",\"sw_c\":\""+sw.getHostname()+"\" },");
		json = json.concat("{\"hw_p\":\"内存大小\",\"hw_c\":\""+hw.getMemory()+"\",\"sw_p\":\"操作系统\",\"sw_c\":\""+sw.getOperationName()+"\" },");
		json = json.concat("{\"hw_p\":\"硬盘\",\"hw_c\":\""+hw.getStorage()+"\",\"sw_p\":\"版本\",\"sw_c\":\""+sw.getOperationVersion()+"\" },");
		json = json.concat("{\"hw_p\":\"磁盘阵列方案\",\"hw_c\":\""+hw.getRaid()+"\",\"sw_p\":\"默认网关\",\"sw_c\":\""+sw.getDefaultGateway()+"\" },");
		json = json.concat("{\"hw_p\":\"主板信息\",\"hw_c\":\""+hw.getMotherboard()+"\",\"sw_p\":\"最大打开文件数\",\"sw_c\":\""+sw.getOpenfile()+"\" },");
		json = json.concat("{\"hw_p\":\"MAC地址\",\"hw_c\":\""+hw.getMac()+"\",\"sw_p\":\" \",\"sw_c\":\" \" },");
		json = json.substring(0, json.length()-1).concat("]}");
		return json;
	}
	
	/**
	 * 更新软硬件信息
	 * @param server_id
	 * @return
	 */
	public String updateHwSw(int server_id){
		String json = "";
		Date date = new Date();
		Hardware hw = hwDao.getHardware(server_id);
		Software sw = swDao.getSoftware(server_id);
		Server sv = svDao.getServer(server_id);
		String info = null;
		try {
			//info = SshShell.exec(sv.getIp(), "clouder", "engine", 22,
				//"sh /home/clouder/vs/program/nagios/libexec/onecloud/get_info.sh all");
			info = LocalShell.exec("/home/clouder/vs/program/nagios/libexec/check_nrpe -H " +
				sv.getIp() + " -c get_info -a all");
			net.sf.json.JSONObject jo = net.sf.json.JSONObject.fromObject(info);
			hw.setCpu(jo.getString("cpu"));
			hw.setMac(jo.getString("mac"));
			hw.setMemory(jo.getString("memory"));
			hw.setRaid(jo.getString("raid"));
			hw.setStorage(jo.getString("storage"));
			hw.setMotherboard(jo.getString("motherboard"));
			sw.setDefaultGateway(jo.getString("gateway"));
			sw.setHostname(jo.getString("hostname"));
			sw.setOpenfile(jo.getString("openfile"));
			sw.setOperationName(jo.getString("operation"));
			sv.setUpdateTime(date);
			hwDao.update(hw);
			swDao.update(sw);
			svDao.update(sv);
			json = "{\"status\":0,\"mess\":\"软硬件信息更新成功！\"}";
		} catch(Exception e) {
			e.printStackTrace();
			Log log = new Log();
			log.setIp(sv.getIp());
			log.setContent("调用nagios获取信息失败，获取到的信息如下：" + info);
			log.setCreateTime(date);
			log.setUpdateTime(date);
			logDao.addLog(log);
			json = "{\"status\":1,\"mess\":\"软硬件信息更新失败！\"}";
		}
		return json;
	}
	
	/**
	 * 添加 server handware software ingredient
	 * @param ip_list
	 * @param isActive
	 * @param isVm
	 * @param party_id
	 * @param define_id
	 * @return
	 */
	public String addServer(List<String> ip_list,String isActive, String isVm,String party_id,String define_id,String remark){
		String json = "";
		Date date = new Date();
		try {
			for(String ip:ip_list){
				Server old_sv = svDao.getServer("ip",ip);
				Server sv = new Server();
				if(old_sv!=null){
					//json = "{\"status\":1,\"mess\":\""+ip+"已存在！\"}";
					//return json;
					sv = old_sv;
				}else{	
					sv.setIp(ip);
					sv.setIsActive(Integer.valueOf(isActive));
					sv.setCreateTime(date);
					sv.setUpdateTime(date);
					sv.setRemark(JSONValue.escape(remark));
						
					if(isVm!=null&&isVm.length()!=0){
						Server vm = svDao.getServer("ip",isVm);
						if(vm!=null) sv.setHost(vm);
						else {json = "{\"status\":1,\"mess\":\"物理机"+isVm+"不存在！\"}"; return json;}
					}else{
						sv.setHost(null);
					}
					svDao.save(sv);
					Hardware hw = new Hardware();
					hw.setHdServer(sv);
					Software sw = new Software();
					sw.setSwServer(sv);
					String info = null;
					try {
						info = LocalShell.exec("/home/clouder/vs/program/nagios/libexec/check_nrpe -H " +
							ip + " -c get_info -a all");
						net.sf.json.JSONObject jo = net.sf.json.JSONObject.fromObject(info);
						hw.setCpu(jo.getString("cpu"));
						hw.setMac(jo.getString("mac"));
						hw.setMemory(jo.getString("memory"));
						hw.setRaid(jo.getString("raid"));
						hw.setStorage(jo.getString("storage"));
						hw.setMotherboard(jo.getString("motherboard"));
						sw.setDefaultGateway(jo.getString("gateway"));
						sw.setHostname(jo.getString("hostname"));
						sw.setOpenfile(jo.getString("openfile"));
						sw.setOperationName(jo.getString("operation"));
					} catch(Exception e) {
						e.printStackTrace();
						Log log = new Log();
						log.setIp(ip);
						log.setContent("调用nagios获取信息失败，获取到的信息如下：" + info);
						log.setCreateTime(date);
						log.setUpdateTime(date);
						logDao.addLog(log);
					}
					hwDao.save(hw);
					swDao.save(sw);
				}
				
				String[] array_define_id = (define_id.replaceAll(" ", "")).split(",");
				for(String igf_id : array_define_id){
					Ingredient ig =new Ingredient();
					ig.setCreateTime(date);
					ig.setUpdateTime(date);
					Arena pt = new Arena();
					pt.setId(Integer.valueOf(party_id));
					ig.setIgParty(pt);
					ig.setIgServer(sv);
					ig.setIsActive(Integer.valueOf(isActive));
					IngredientDefine idf = new IngredientDefine();
					idf.setId(Integer.valueOf(igf_id));
					ig.setIgDefine(idf);
					igDao.save(ig);
				}
				
			}
			json = "{\"status\":0,\"mess\":\"服务器添加成功！\"}";
		} catch (NumberFormatException e1) {
			json = "{\"status\":1,\"mess\":\"服务器添加失败！\"}";
		}
		return json;
	}
	
	/**
	 * 修改 server  ingredient
	 * @param server_id
	 * @param isActive
	 * @param party_id
	 * @param define_id
	 * @return
	 */
	public String updateServer(String server_id,String isActive,String party_id,String define_id,String remark){
		String json = "";
		Date date = new Date();
		try {
			Server sv = svDao.getServer(Integer.valueOf(server_id));
			sv.setIsActive(Integer.valueOf(isActive));
			sv.setUpdateTime(date);
			sv.setRemark(JSONValue.escape(remark));
			svDao.update(sv);
			
			Ingredient ig = igDao.getIngredient(server_id);
			List<Ingredient> ig_id_list = igDao.findIngredient(String.valueOf(server_id),String.valueOf(ig.getIgParty().getId()));
			for(Ingredient d_ig : ig_id_list){
				igDao.delete(d_ig);
			}
			String[] array_define_id = (define_id.replaceAll(" ", "")).split(",");
			for(String igf_id : array_define_id){
				Ingredient agent_add_ig = new Ingredient();
				agent_add_ig.setIgServer(ig.getIgServer());
				agent_add_ig.setCreateTime(ig.getCreateTime());
				agent_add_ig.setUpdateTime(date);
				//需要改进：更改后不要改变原来的状态
				agent_add_ig.setIsActive(Integer.valueOf(isActive));
				Arena p = new Arena();
				p.setId(Integer.valueOf(party_id));
				agent_add_ig.setIgParty(p);
				IngredientDefine igd = new IngredientDefine();
				igd.setId(Integer.valueOf(igf_id));
				agent_add_ig.setIgDefine(igd);
				igDao.save(agent_add_ig);
			}
			
			json = "{\"status\":0,\"mess\":\"服务器修改成功！\"}";
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			json = "{\"status\":1,\"mess\":\"服务器修改失败！\"}";
		}
		return json;
	}
	
	/**
	 * 删除 server handware software ingredient
	 * @param array_server_id
	 * @return
	 */
	public String deleteServer(String[] array_server_id,String[] array_party_id){
		String json = "";
		String ip_com = "";
		String ip_com2 = "";
		for(String server_id:array_server_id){
			Server sv = svDao.getServer(Integer.valueOf(server_id));
			Server c_vm = svDao.getServer("host", server_id); //转换了
			if(c_vm!=null){
				ip_com = ip_com + sv.getIp() + ",";
			}
		}
		
		if(ip_com.length()==0){
			for(int i=0;i<array_server_id.length;i++){
				List<Ingredient> ig_id_list = igDao.findIngredient(String.valueOf(array_server_id[i]),String.valueOf(array_party_id[i]));
				Ingredient ig = ig_id_list.get(0);
				Server sv = svDao.getServer(ig.getIgServer().getId());
				Hardware hw = hwDao.getHardware(ig.getIgServer().getId());
				Software sw = swDao.getSoftware(ig.getIgServer().getId());
				for(Ingredient d_ig : ig_id_list){
					igDao.delete(d_ig);
				}
				List<Ingredient> pd_ig_id_list = igDao.findIngredient(String.valueOf(array_server_id[i]));
				if(pd_ig_id_list==null||pd_ig_id_list.size()==0){
					if(hw!=null) hwDao.delete(hw);
					if(sw!=null) swDao.delete(sw);
					svDao.delete(sv);
				}
				ip_com2 = ip_com2 + sv.getIp() + ",";
			}	
			json = "{\"status\":0,\"mess\":\""+ip_com2+"删除成功！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\""+ip_com+"有云主机，请先删除云主机！\"}"; 
		}
		return json;
	}
	
	/**
	 * 查看服务器软硬件信息
	 */
	@SuppressWarnings("unchecked")
	public String findServerByPage(ServerPage page) {
		List<Server> servers = svDao.getByPage(page, false);
		Set<Integer> cabIds = new HashSet<Integer>();
		//获取机柜名称-begin
		for(Server server : servers) {
			if(server.getSvCabinet() != null) {
				cabIds.add(server.getSvCabinet().getId());
			}
		}
		Map<Integer, Cabinet> cabIdName = new HashMap<Integer, Cabinet>();
		if(cabIds.size() != 0) {
			List<Cabinet> cabs = cdao.getCabients(StaticMethod.Array2Str(cabIds, ","));
			for(Cabinet cab : cabs) {
				cabIdName.put(cab.getId(), cab);
			}
		}
		//获取机柜名称-end
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Server server : servers) {
			JSONObject serverJson = new JSONObject();
			serverJson.put("id", server.getId());
			serverJson.put("ip", server.getIp());
			//serverJson.put("host", server.getHost());
			serverJson.put("hwId", server.getServerHW().getId());
			serverJson.put("mac", server.getServerHW().getMac());
			serverJson.put("cpu", server.getServerHW().getCpu());
			serverJson.put("memory", server.getServerHW().getMemory());
			serverJson.put("storage", server.getServerHW().getStorage());
			serverJson.put("motherboard", server.getServerHW().getMotherboard());
			serverJson.put("raid", server.getServerHW().getRaid());
			serverJson.put("swId", server.getServerSW().getId());
			serverJson.put("operationName", server.getServerSW().getOperationName());
			serverJson.put("defaultGateway", server.getServerSW().getDefaultGateway());
			serverJson.put("openfile", server.getServerSW().getOpenfile());
			serverJson.put("hostname", server.getServerSW().getHostname());
			if(server.getSvCabinet() != null) {
				serverJson.put("cId", server.getSvCabinet().getId());
				Cabinet cab = cabIdName.get(server.getSvCabinet().getId());
				serverJson.put("cName", cab.getName());
				serverJson.put("rId", cab.getCabRoom().getId());
				serverJson.put("rName", cab.getCabRoom().getName());
			}
			array.add(serverJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	/**
	 * 更新手动输入的软硬件信息
	 */
	public String changeHSW(Server server) {
		svDao.update(server.getServerHW());
		svDao.update(server.getServerSW());
		return StaticMethod.changeSucc;
	}
	/**
	 * 导出excel
	 * @param spage 
	 * @throws Exception 
	 */
	public InputStream download(ServerPage page) throws Exception {
		List<Server> servers = svDao.getByPage(page, true);
		String[] columnNames = new String[]{"IP","MAC","主板","CPU","内存","硬盘","RAID",
				"操作系统","网关","最大打开文件数","主机名"};
		String[] columnMethods = new String[]{"getIp", "getServerHW.getMac",
				"getServerHW.getMotherboard",
				"getServerHW.getCpu", "getServerHW.getMemory",
				"getServerHW.getStorage", "getServerHW.getRaid",
				"getServerSW.getOperationName", "getServerSW.getDefaultGateway",
				"getServerSW.getOpenfile", "getServerSW.getHostname"};
		int[] columnWidths = new int[] {3600, 5000, 6000, 12200, 1800, 2800, 2600, 6400, 3400, 3800, 7800};
		return export2ExcelManager.export2Excel(columnNames, columnMethods, columnWidths, servers);
	}
	/**
	 * 更改服务器在机柜的顺序
	 * @param json 格式为id1_sort A id2_sort
	 */
	public String changeSort(String json) {
		if(StaticMethod.StrSize(json) < 3) {
			return StaticMethod.inputError;
		}
		Set<Integer> ids = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("A")) {
			String[] str = s.split("_");
			try {
				id = Integer.parseInt(str[0]);
				Integer.parseInt(str[1]);
				if(ids.add(id)) {
					svDao.updateSort(str[0], str[1]);
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.changeSucc;
	}
	/**
	 * 为服务器更改机柜
	 * @param json cabinetId A sId a sId
	 */
	public String changeCabinet(String json) {
		if(StaticMethod.StrSize(json) < 3) {
			return StaticMethod.inputError;
		}
		String[] ids = json.split("A");
		int cId = 0;
		try {
			cId = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		if(! svDao.chkExit(cId, "Cabinet")) {
			return StaticMethod.FailMess("机柜不存在！");
		}
		if(ids.length > 1) {
			int sId = 0;
			Set<Integer> uids = new HashSet<Integer>();
			for(String cablinet : ids[1].split("a")) {
				try {
					sId = Integer.parseInt(cablinet);
				} catch(NumberFormatException e) {}
				if(uids.add(sId)) {
					svDao.updateCabinet(sId, cId);
				}
			}
		}
		return StaticMethod.changeSucc;
	}
	/**
	 * 导出全部service列表
	 */
	public InputStream currentexport(IngredientPage page) throws Exception {
		List<List<String>> sv_list = new ArrayList<List<String>>();
		if(page.getParty_id().equals("0")){
			sv_list = this.get_current_server_list(null);
		}else{
			sv_list = this.get_current_server_list(page);
		}
		
		int i = 0;
		if(sv_list.size()>2){
			i = sv_list.get(2).size()-2;
		}
		
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("IP");
		columnNames.add("物理机");
		for(int b=1;b<i;b++){
			columnNames.add("组件"+b);
		}
		columnNames.add("备注");
		
		List<String> pageNames = new ArrayList<String>();
		//pageNames.add("导出条件");
		pageNames.add("环境");
		pageNames.add("组件");
		pageNames.add("状态");
		pageNames.add("类型");
		pageNames.add("IP匹配");
		
		List<String> pagevalue = new ArrayList<String>();
		//pagevalue.add("");
		pagevalue.add(page.getParty());
		pagevalue.add(page.getIdf());
		pagevalue.add(page.getIsActiceName());
		pagevalue.add(page.getHostname());
		pagevalue.add(page.getIp());
    	
    	return Export2Excel.serverexport2Excel(columnNames,pageNames,pagevalue,sv_list);
	}
//set get
	@Resource(name="serverDao")
	public void setSvDao(ServerDao svDao) {
		this.svDao = svDao;
	}
	@Resource(name="hardwareDao")
	public void setHwDao(HardwareDao hwDao) {
		this.hwDao = hwDao;
	}
	@Resource(name="softwareDao")
	public void setSwDao(SoftwareDao swDao) {
		this.swDao = swDao;
	}
	@Resource(name="ingredientDao")
	public void setIgDao(IngredientDao igDao) {
		this.igDao = igDao;
	}
	@Resource(name="arenaDao")
	public void setaDao(ArenaDao aDao) {
		this.aDao = aDao;
	}
	@Resource(name="ingredientDefineDao")
	public void setIgDefineDao(IngredientDefineDao igDefineDao) {
		this.igDefineDao = igDefineDao;
	}

}
