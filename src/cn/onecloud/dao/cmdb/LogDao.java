package cn.onecloud.dao.cmdb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.Log;
import cn.onecloud.util.page.cmdb.LogPage;

@Repository("logDao")
public class LogDao extends UtilDao {
	
	public List<Log> getLog(final LogPage page){
		String countHql = "select count(id) from Log where 1=1 ";
		String hql = "select new Log(l.id,l.ip,l.content,l.type,l.createTime,l.updateTime) " +
						   "from Log l where 1=1 ";
		StringBuffer conditionHql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		if(page.getIp() != null && !page.getIp().equals("")){
			conditionHql.append("and ip like (?) ");
			params.add("%" + page.getIp() + "%");
		}
		if((page.getSdate() != null && !page.getSdate().equals("")) 
			&& (page.getEdate() != null && !page.getEdate().equals(""))){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			conditionHql.append("and date_format(createTime,'%Y-%m-%d')>=date_format('"+sdf.format(page.getSdate())+"','%Y-%m-%d') " +
					   "and date_format(createTime,'%Y-%m-%d')<=date_format('"+sdf.format(page.getEdate())+"','%Y-%m-%d') ");
		}
		if(page.getType() != null && !page.getType().equals("")){
			conditionHql.append("and type=? ");
			params.add(page.getType());
		}
		if(page.getKeyword() != null){
			conditionHql.append("and content like (?) ");
			params.add("%" + page.getKeyword() + "%");
		}
		if(page.getSort() != null && page.getOrder()!= null){
			hql = hql + conditionHql + "order by " + page.getSort() +" " + page.getOrder();
		}
		return getObjsByPage(countHql + conditionHql, 
				   			 hql,
				             page, 
				             params); 
	}
	
	public void addLog(Log log){
		ht.save(log);
	}
	
	public void updateLog(Log log){
		ht.update(log);
	}
	
	public Log findLogById(int id){
		return (Log)ht.get(Log.class, id);
	}
	
	public void deleteLogByid(int id){
		ht.bulkUpdate("delete Log l where l.id=?", id);
	}
}
