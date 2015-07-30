package cn.onecloud.dao.cmdb;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import cn.onecloud.util.page.cmdb.Page;


@Repository("utilCmdbDao")
public abstract class UtilDao {

	public HibernateTemplate ht;
	@Resource(name="hibernateTemplate2")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Object obj) {
		ht.save(obj);
	}

//r
	@SuppressWarnings("unchecked")
	public boolean chkExit(int id, String obj) {
		List<Integer> objs = ht.find("select obj.id from " + obj + " obj where obj.id=" + id);
		return objs.size()==0? false: true;
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjsByPage(final String countSql, final String selectSql,
			final Page page, final List<String> params) {
		return ht.executeFind(
				new HibernateCallback<List<T>>() {
					public List<T> doInHibernate(Session s) throws HibernateException, SQLException {
						Query query = s.createQuery(countSql);
						for(int i=0; i<params.size(); i++) {
							query.setParameter(i, params.get(i));
						}
						page.countPage(((Long) query.list().get(0)).intValue());
						if(page.getCountNums() == 0) {
							return Collections.emptyList();
						}
						
						query = s.createQuery(selectSql)
							.setFirstResult(page.getFirstNum())
							.setMaxResults(page.getNum());
						for(int i=0; i<params.size(); i++) {
							query.setParameter(i, params.get(i));
						}
						return query.list();
					}
				}
			);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjs(final String sql, final int num) {
		return ht.executeFind(
			new HibernateCallback<List<T>>() {
				public List<T> doInHibernate(Session s) throws HibernateException, SQLException {
					return s.createQuery(sql).setMaxResults(num).list();
				}
			}
		);
	}
	/**
	 * 获取对象
	 * @param num 为0表示获取全部
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjs(final String sql, final List<String> params, final int num) {
		return ht.executeFind(
			new HibernateCallback<List<T>>() {
				public List<T> doInHibernate(Session s) throws HibernateException, SQLException {
					Query query = s.createQuery(sql);
					if(num != 0) {
						query.setMaxResults(num);
					}
					for(int i=0; i<params.size(); i++) {
						query.setParameter(i, params.get(i));
					}
					return query.list();
				}
			}
		);
	}
//u
	public int bulkUpdate(final String updateSql, final List<String> params) {
		return ht.executeWithNativeSession(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(updateSql);
				if (params != null) {
					for (int i = 0; i < params.size(); i++) {
						queryObject.setParameter(i, params.get(i));
					}
				}
				return queryObject.executeUpdate();
			}
		});
	}
	/**
	 * 更新全部属性
	 */
	public void update(Object obj) {
		ht.saveOrUpdate(obj);
	}
//d
	/**
	 * 删除
	 * @param id 对象id
	 * @param obj 类名
	 */
	public void delete(String id, String obj) {
		bulkUpdate("delete from " + obj + " obj where obj.id=" + id, null);
	}
	
	public void delete(Object obj){
		ht.delete(obj);
	}
	
	public <T> List<T> findall (String sql,final Class<T> clazz)
	{
		@SuppressWarnings("unchecked")
		final List<T> list = (List<T>)ht.find(sql);
		return list;
	}
	/**
	 * 使用sql语句查询
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getObjsBySql(final String sql) {
		return ht.executeFind(
				new HibernateCallback<List<Object[]>>() {
					public List<Object[]> doInHibernate(Session s) throws HibernateException, SQLException {
						Query query = s.createSQLQuery(sql);
						return query.list();
					}
				}
			);
	}
}
