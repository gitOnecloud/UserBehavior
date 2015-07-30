package cn.onecloud.dao.cmdb.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.server.Room;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.RoomPage;

@Repository("roomDao")
public class RoomDao extends UtilDao {
//c
//r
	@SuppressWarnings("unchecked")
	public List<Room> getAll() {
		return super.ht.find("select new Room(r.id, r.name) from Room r");
	}
	/**
	 * 分页查找
	 */
	public List<Room> getAllByPage(RoomPage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getName()) > 0) {
			whereSql.append(" and room.name like(?)");
			params.add("%" + page.getName() + "%");
		}
		return getObjsByPage("select count(room.id) from Room room " + whereSql.toString(),
				"select new Room(room.id, room.name, room.remark, style.id, style.name)" +
					"from Room room, Style style " + whereSql.toString() +
					" and room.cabStyle.id=style.id order by room.id",
				page, params);
	}
	/**
	 * 根据机房id获取样式
	 * @return 空返回null
	 */
	@SuppressWarnings("unchecked")
	public Style getRoomStyle(String rId) {
		List<Style> styles = ht.find("from Style style" +
				" where style.id=" +
				"  (select room.cabStyle.id" +
				"   from Room room" +
				"   where room.id=" + rId + ")");
		if(styles.size() == 0) {
			return null;
		}
		return styles.get(0);
	}
	/**
	 * 根据id查找机房信息及样式
	 */
	public Room getRoomById(int rId) {
		return ht.get(Room.class, rId);
	}
//u
//d
}
