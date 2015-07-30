package cn.onecloud.dao.cmdb.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.ServerPage;

@Repository("serverDao")
public class ServerDao extends UtilDao {

	/**
	 * 查找软硬件信息
	 * @param isAll true:获取全部 false:分页
	 */
	public List<Server> getByPage(ServerPage page, boolean isAll) {
		StringBuilder whereSql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		whereSql.append(" from Server sv, Hardware hw, Software sw where 1=1");
		if(StaticMethod.StrSize(page.getIp()) > 0){
			whereSql.append(" and ( sv.ip like(?)");
			params.add("%" + page.getIp() + "%" );
			if(StaticMethod.isip(page.getIp()) == true){
				Server sv = getServer("ip",page.getIp());
				if(sv!=null) {
					whereSql.append(" or sv.host=" + sv.getId());
				}
			}
			whereSql.append(')');
		}
		if(StaticMethod.StrSize(page.getMemory()) > 0) {
			whereSql.append(" and hw.memory like(?)");
			params.add(page.getMemory() + "%" );
		}
		if(StaticMethod.StrSize(page.getOperationName()) > 0) {
			whereSql.append(" and sw.operationName like(?)");
			params.add(page.getOperationName() + "%" );
		}
		if(StaticMethod.StrSize(page.getHostname()) > 0) {
			whereSql.append(" and sw.hostname like(?)");
			params.add(page.getHostname() + "%" );
		}
		whereSql.append(" and hw.hdServer.id=sv.id and sw.swServer.id=sv.id ");
		String selectSql = "select new Server(sv.id, sv.ip, sv.host.id, sv.svCabinet.id," +
				"hw.id, hw.mac, hw.motherboard, hw.cpu, hw.memory, hw.storage, hw.raid," +
				"sw.id, sw.operationName, sw.defaultGateway, sw.openfile, sw.hostname)";
		String orderSql = " order by inet_aton(sv.ip) ASC";
		if(isAll) {
			return super.getObjs(selectSql + whereSql.toString() + orderSql, params, 0);
		}
		return getObjsByPage("select count(sv.id) " + whereSql.toString(),
				selectSql + whereSql.toString() + orderSql,
				page, params);
	}

	public Server getServer(int id){
		return (Server) super.ht.get(Server.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public Server getServer(String pro,String value){
		List<Server> sv_list =  super.ht.find("from Server obj where obj."+pro+"='"+value+"'");
		if(sv_list.size()==0||sv_list==null){
			return null;
		}else{
			return sv_list.get(0);
		}
	}
	/**
	 * 根据机柜获取服务器
	 */
	@SuppressWarnings("unchecked")
	public List<Server> getSvsByCId(String cId) {
		return ht.find("select new Server(server.id, server.ip, server.sort, server.isActive, server.remark)" +
				" from Server server" +
				" where server.svCabinet.id=" + cId);
	}
	/**
	 * 设置服务器在机柜中的顺序
	 */
	public void updateSort(String sId, String sort) {
		ht.bulkUpdate("update Server server" +
				" set sort=" + sort +
				" where server.id=" + sId);
	}
	/**
	 * 为服务器更改机柜
	 */
	public void updateCabinet(int sId, int cId) {
		ht.bulkUpdate("update Server server" +
				" set server.svCabinet.id=" + cId +
				" where server.id=" + sId);
	}
}
