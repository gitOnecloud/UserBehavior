package cn.onecloud.dao.cmdb.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.server.Cabinet;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.CabinetPage;

@Repository("cabinetDao")
public class CabinetDao extends UtilDao {

//c
	
//r
	/**
	 * 分页查找
	 */
	public List<Cabinet> getAllByPage(CabinetPage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			whereSql.append(" and cabinet.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		return getObjsByPage("select count(cabinet.id) from Cabinet cabinet " + whereSql.toString(),
				"select new Cabinet(cabinet.id, cabinet.name, cabinet.remark, style.id, style.name, room.id, room.name)" +
					"from Cabinet cabinet, Style style, Room room " + whereSql.toString() +
					" and cabinet.cabStyle.id=style.id and cabinet.cabRoom.id=room.id" +
					" order by room.id,cabinet.id",
				page, params);
	}
	/**
	 * 根据机房id获取机柜
	 */
	@SuppressWarnings("unchecked")
	public List<Cabinet> getCabsByRId(String rId) {
		return ht.find("select new Cabinet(cabinet.id, cabinet.name, cabinet.sort)" +
				" from Cabinet cabinet" +
				" where cabinet.cabRoom.id=" + rId);
	}
	/**
	 * 根据id获取名称，以及机房信息
	 */
	@SuppressWarnings("unchecked")
	public List<Cabinet> getCabients(String ids) {
		return ht.find("select new Cabinet(cabinet.id, cabinet.name, room.id, room.name)" +
				" from Cabinet cabinet, Room room" +
				" where cabinet.id in(" + ids + ")" +
				"  and cabinet.cabRoom.id=room.id");
	}
	/**
	 * 根据机柜id获取样式
	 * @return 空返回null
	 */
	@SuppressWarnings("unchecked")
	public Style getCabStyle(String cId) {
		List<Style> styles = ht.find("from Style style" +
				" where style.id=" +
				"  (select cab.cabStyle.id" +
				"   from Cabinet cab" +
				"   where cab.id=" + cId + ")");
		if(styles.size() == 0) {
			return null;
		}
		return styles.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<Cabinet> getCabinetsByRId(String rId) {
		return ht.find("from Cabinet cabinet" +
				" where cabinet.cabRoom.id=" + rId +
				" order by cabinet.sort");
	}
//u
	/**
	 * 为机柜设置顺序
	 */
	public void updateSort(String id, String sort) {
		ht.bulkUpdate("update Cabinet cabinet" +
				" set sort=" + sort +
				" where cabinet.id=" + id);
	}
	/**
	 * 为机柜更改机房
	 */
	public void updateRoom(int cId, int rId) {
		ht.bulkUpdate("update Cabinet cabinet" +
				" set cabinet.cabRoom.id=" + rId +
				" where cabinet.id=" + cId);
	}
//d
	
}
