package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.ApplicationInfo;
import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.ApplicationInfoPage;

@Repository("applicationInfoDao")
public class ApplicationInfoDao extends UtilDao
{
	public List<ApplicationInfo> getAllPage(ApplicationInfoPage page)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("where 1=1");
		List<String> params = new ArrayList<String>();
		if (StaticMethod.StrSize(page.getOaid()) > 0)
		{
			sql.append(" and apinfo.oaid like (?)");
			params.add("%" + page.getOaid() + "%");
		}
		if (StaticMethod.StrSize(page.getDescription()) > 0)
		{
			sql.append(" and apinfo.description like (?)");
			params.add("%"+page.getDescription()+"%");
		}
		if (StaticMethod.Str2Int(page.getIsMonitor()) != -1)
		{
			sql.append(" and apinfo.isMonitor = "+page.getIsMonitor());
		}
		if (StaticMethod.Str2Int(page.getSatellite()) != -1)
		{
			sql.append("and apinfo.arenaSatellite.id = "+page.getSatellite());
		}
		return getObjsByPage("select count(apinfo.id) from  ApplicationInfo apinfo "
				+ sql, "from ApplicationInfo apinfo "+sql, page, params);
	}
	
	public List<Arena> getAllSatellite()
	{
		return getObjs("select new Arena(a.id,a.name) from Arena a, Satellite s where a.id=s.slArena.id",new ArrayList<String>(), 0);
	}

	public List<Object[]> findSatelliteIP(String arenaName)
	{
		//return super.getObjsBySql("select ip , arenaName from ingredientview where arenaName='"+arenaName+"' and (ingredientName='MonitorServer' or ingredientName='MonitorClient') limit 1");
		return super.getObjsBySql("select ip, arenaName from ingredientview where arenaName='"+arenaName+"' and ingredientName='Old_Monitor_Server' limit 1");
	}
}
