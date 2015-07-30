package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.IngredientCheck;
import cn.onecloud.model.cmdb.NagiosCheck;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.NagiosCheckPage;

@Repository("nagiosCheckDao")
public class IngredientCheckDao extends UtilDao
{
	public List<IngredientCheck> getAllPage(NagiosCheckPage page)
	{
		StringBuilder searchSql = new StringBuilder();
		searchSql.append("where 1=1");
		List<String> params = new ArrayList<String>();
		if (StaticMethod.Str2Int(page.getIgdefine())>0)
		{
			searchSql.append("and icheck.igDefine.id="+page.getIgdefine());
		}
		if (StaticMethod.Str2Int(page.getStoptime())>0)
		{
			searchSql.append("and icheck.nagiosCheck.stoptime="+page.getStoptime());
		}
		if (StaticMethod.StrSize(page.getCheckName())>0)
		{
			searchSql.append("and icheck.nagiosCheck.checkName like (?)");
			params.add("%" + page.getCheckName() + "%");
		}
		if (StaticMethod.StrSize(page.getCheckCommand())>0)
		{
			searchSql.append("and icheck.nagiosCheck.checkCommand like (?)");
			params.add("%" + page.getCheckCommand() + "%");
		}
		
		return getObjsByPage(
				"select count(icheck.id) from IngredientCheck icheck "
						+ searchSql,
				"select new IngredientCheck(icheck.id,igd.id,igd.name,ncheck.id,ncheck.checkName,ncheck.description,ncheck.stoptime,ncheck.checkCommand,icheck.isActive) "
				+ "from NagiosCheck ncheck ,IngredientCheck icheck,IngredientDefine igd "
						+ searchSql + "and icheck.nagiosCheck.id=ncheck.id and icheck.igDefine.id = igd.id",
				page, params);
	}
	
	public List<NagiosCheck> verifyCheckName(String checkName)
	{
		List<String> params = new ArrayList<String>();
		params.add(checkName);
		return getObjs("from NagiosCheck where checkName = ?", params,1);
	}
	
	public List<NagiosCheck> getNagiosAllPage (NagiosCheckPage page)
	{
		StringBuilder searchSql = new StringBuilder();
		searchSql.append(" where 1=1");
		List<String> params = new ArrayList<String>();
		if (StaticMethod.StrSize(page.getCheckName())>0)
		{
			searchSql.append(" and ncheck.checkName like(?)");
			params.add("%"+page.getCheckName()+"%");
		}
		return getObjsByPage(
				"select count(ncheck.id) from NagiosCheck ncheck "
						+ searchSql,
				" from NagiosCheck ncheck"+searchSql,
				page, params);
	}
	
	public List<Object[]> getNagiosMornitor()
	{
		return super.getObjsBySql("select arenaName ,ingredientName,ip,parentName from ingredientview where ingredientName='Old_Monitor_Server'");
	}
	
	public List<Object[]> getNagiosSatellite(String arenaName)
	{
		return super
				.getObjsBySql("select a.name ,a.id from arena a, satellite s where a.id=s.arena_id and s.rouyer_id in (select s.rouyer_id from arena a, satellite s where  a.name="
						+ "'" + arenaName + "'" + " and a.id=s.arena_id )");
	}
	
	public List<Object[]> getNagiosBasket(int arenaId)
	{
		return super.getObjsBySql("select a.name,a.id from arena a, basket b where a.id= b.arena_id and b.satellite_id in ("
				+ "select s.id from satellite s where s.arena_id = "+arenaId+")");
	}
	
	public List<Object[]> verifyBasketUnique(int arenaId)
	{
		return super.getObjsBySql("select b.id from basket b where b.arena_id ="+arenaId);
	}
}
