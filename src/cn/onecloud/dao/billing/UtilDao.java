package cn.onecloud.dao.billing;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository("utilDao")
public abstract class UtilDao {

	public HibernateTemplate ht;
	@Resource(name="hibernateTemplate3")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void add(Object obj) {
		ht.save(obj);
	}

//r
	@SuppressWarnings("unchecked")
	public boolean chkExit(int id, String obj) {
		List<Integer> objs = ht.find("select obj.id from " + obj + " obj where obj.id=" + id);
		return objs.size()==0? false: true;
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
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjs(final String sql, final List<String> params, final int num) {
		return ht.executeFind(
			new HibernateCallback<List<T>>() {
				public List<T> doInHibernate(Session s) throws HibernateException, SQLException {
					Query query = s.createQuery(sql)
						.setMaxResults(num);
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
//d
	public void delete(String id, String obj) {
		bulkUpdate("delete from " + obj + " obj where obj.id=" + id, null);
	}

}
