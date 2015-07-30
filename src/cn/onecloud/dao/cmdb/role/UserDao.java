package cn.onecloud.dao.cmdb.role;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.menu.User;
import cn.onecloud.util.page.cmdb.UserPage;

@Repository("userDao")
public class UserDao extends UtilDao {

//c
//r
	/**
	 * 分页查找用户的id name account department
	 */
	@SuppressWarnings("unchecked")
	public List<User> getU_inadOnPage(final UserPage page) {
		final StringBuilder sql = new StringBuilder();
		if(page.getName()!=null && page.getName().length()!=0) {
			sql.append(" and u.name like '" + page.getName().replace("'", "") + "%'");
		}
		if(page.getAccount()!=null && page.getAccount().length()!=0) {
			sql.append(" and u.account like '" + page.getAccount().replace("'", "") + "%'");
		}
		if(sql.length() != 0)
			sql.replace(0, 4, "where");
		long nums = (Long) ht.find("select count(*) from User u " + sql.toString()).get(0);
		page.countPage((int) nums);
		if(nums == 0) {
			return Collections.emptyList();
		}
		return ht.executeFind(
			new HibernateCallback<List<User>>() {
				public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
					List<User> result = s.createQuery("select new User(u.id, u.name, u.account)" +
							"from User u " + sql.toString() + " order by u.id")
						.setFirstResult(page.getFirstNum())
						.setMaxResults(page.getNum())
						.list();
					return result;
				}
			}
		);
	}
	/**
	 * 查找用户的id name account department
	 * @param id
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	public User getU_inadById(int id) {
		List<User> users = ht.find("select new User(u.id, u.name, u.account) " +
				"from User u where u.id=?", id);
		if(users.size() > 0)
			return users.get(0);
		else
			return null;
	}*/
	/**
	 * 根据账号获取密码
	 * @param account 账号
	 * @return 密码
	 */
	@SuppressWarnings("unchecked")
	public String getU_passByAccount(String account) {
		List<String> strs = ht.find("select u.password from User u where u.account=?", account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return null;
	}
	/**
	 * 根据账号获取用户信息, 前提:此账户一定存在
	 * @param account 账号
	 * @return user
	 */
	public User getUserByU_a(String account) {
		return (User) ht.find("from User u where u.account=?", account).get(0);
	}
	/**
	 * 根据账号查找用户个数，在注册和更改时用于判断此账号是否已经有人使用
	 * @param account
	 * @param uId 使用于更改账号时的查询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getU_NumByAc(String account, int uId) {
		List<Integer> uids = ht.find("select u.id " +
				"from User u where u.account=?", account);
		if(uids.size() > 0) {
			if(uId == uids.get(0)) {
				return 0;
			} else {
				return uids.size();
			}
		} else {
			return 0;
		}
	}
	/**
	 * 检查是否存在
	 * @return true 存在 false 不存在
	 */
	@SuppressWarnings("unchecked")
	public boolean chkExit(int uid) {
		List<Integer> uids = ht.find("select u.id from User u where u.id=" + uid);
		return uids.size()==0? false: true;
	}
//u
	/**
	 * 更改用户信息
	 * @param user
	 */
	public void update(User user) {
		String sql = "update User u set ";
		if(!user.getName().isEmpty()) {
			sql = sql.concat("u.name='" + user.getName() + "',");
		}
		if(!user.getAccount().isEmpty()) {
			sql = sql.concat("u.account='" + user.getAccount() + "'," +
					"u.password='" + user.getPassword() + "',");
		}
		if(!sql.equals("update User u set ")) {
			sql = sql.substring(0, sql.length()-1)
					.concat(" where u.id=" + user.getId());
			ht.bulkUpdate(sql);
		}
	}
	/**
	 * 根据账号更改密码
	 * @param user
	 */
	public void updatePW(User user) {
		ht.bulkUpdate("update User u set u.password=? where u.account=?", user.getPassword(), user.getAccount());
	}
//d
	public void delete(User user) {
		ht.delete(user);
	}
	public User getUserByName(String username) {
		return (User) ht.find("from User where name=?",username).get(0);
	}
	@SuppressWarnings("unchecked")
	public List<String> getAllUserName() {
		List<String> usernames = ht.find("select u.name from User u");
		return usernames;
	}

}
