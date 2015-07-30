package cn.onecloud.service.cmdb.role;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.cmdb.role.RoleDao;
import cn.onecloud.dao.cmdb.role.UserDao;
import cn.onecloud.dao.cmdb.role.User_RoleDao;
import cn.onecloud.model.cmdb.menu.Role;
import cn.onecloud.model.cmdb.menu.User;
import cn.onecloud.model.cmdb.menu.User_Role;
import cn.onecloud.util.page.cmdb.UserPage;


@Service("userManager")
public class UserManager {

	private UserDao userDao;
	@Resource(name="userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	private User_RoleDao user_RoleDao;
	@Resource(name="userRoleDao")
	public void setUser_RoleDao(User_RoleDao user_RoleDao) {
		this.user_RoleDao = user_RoleDao;
	}
	private RoleDao roleDao;
	@Resource(name="roleDao")
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	private RoleManager rm;
	@Resource(name="roleManager")
	public void setRoleManager(RoleManager rm) {
		this.rm = rm;
	}
	private PasswordEncoder passwordEncoder;
	@Resource(name="passwordEncoder")
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	@Autowired
	private SessionRegistry sessionRegistry;
	private Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//c
	public String save(User user, int[] roleId) {
		user.setAccount(user.getAccount().replaceAll("'", "").replaceAll("\"", "")
				.replaceAll("\\\\", "").toLowerCase());
		if(userDao.getU_NumByAc(user.getAccount(), 0) != 0) {
			return "{\"status\":1,\"mess\":\"此账户已存在！\",\"account\":false}";
		}
		user.setName(user.getName().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
		user.setPassword(passwordEncoder.encodePassword(
				user.getPassword(), user.getAccount()));
		userDao.save(user);
		if(roleId!=null) {
			for(int i=0; i<roleId.length; i++) {
				user_RoleDao.save(new User_Role(user.getId(), roleId[i]));
			}
		}
		return "{\"status\":0}";
	}
//r
	@SuppressWarnings("unchecked")
	public String findU_inadOnPage(UserPage page) {
		List<User> users = userDao.getU_inadOnPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(int i=0;i<users.size();i++) {
			List<User_Role> urs = user_RoleDao.get_U_RByUId(users.get(i).getId());
			String rnames = "";
			List<Integer> rids = new ArrayList<Integer>();
			for(int j=0;j<urs.size();j++) {
				Role role = urs.get(j).getRole();
				if(j < 3) {
					rnames = rnames.concat(role.getName() + ",");
				}
				rids.add(role.getId());
			}
			if(urs.size() > 0) {
				if(urs.size() > 3)
					rnames = rnames.substring(0, rnames.length() -1 ).concat("...");
				else
					rnames = rnames.substring(0, rnames.length() -1 );
			}
			JSONObject userJson = new JSONObject();
			userJson.put("id", users.get(i).getId());
			userJson.put("user.name", users.get(i).getName());
			userJson.put("user.account", users.get(i).getAccount());
			userJson.put("role", rnames);
			userJson.put("roleId", rids);
			array.add(userJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	/*public User findU_inadrById(int id) {
		User user = userDao.getU_inadById(id);
		if(user == null)
			return null;
		user.setUser_roles(user_RoleDao.get_U_RByUId(id));
		return user;
	}*/
	public String findU_passByAccount(String account) {
		return userDao.getU_passByAccount(account);
	}
	
	
	public String findDRs() {
		return "{\"roles\":" + rm.findRoles() + "}";
	}
//u
	public String change(User user, int[] roleId) {
		user.setAccount(user.getAccount().replaceAll("'", "").replaceAll("\"", "")
				.replaceAll("\\\\", "").toLowerCase());
		if(userDao.getU_NumByAc(user.getAccount(), user.getId()) != 0) {
			return "{\"status\":1,\"mess\":\"此账户已存在！\",\"account\":false}";
		}
		user.setName(user.getName().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
		if(!user.getAccount().isEmpty()) {
			user.setPassword(passwordEncoder.encodePassword(
					user.getPassword(), user.getAccount()));
		}
		userDao.update(user);
		if(roleId == null) {
			user_RoleDao.delete(user.getId());
		} else if(roleId[0] != -1) {
			user_RoleDao.delete(user.getId());
			for(int i=0; i<roleId.length; i++) {
				user_RoleDao.save(new User_Role(user.getId(), roleId[i]));
			}
		}
		return "{\"status\":0}";
	}
	public String changePW(User user) {
		String account = SecuManager.currentAccount();
		String password = userDao.getU_passByAccount(account);
		if(passwordEncoder.encodePassword(user.getAccount().toLowerCase(), account).equals(password)) {
			user.setAccount(account);
			user.setPassword(passwordEncoder.encodePassword(user.getPassword(), account));
			userDao.updatePW(user);
			return "{\"status\":0}";
		} else {
			return "{\"status\":1,\"mess\":\"密码错误！\",\"password\":false}";
		}
	}
	public String changeRole(String json) {
		if(json==null || json.length()==0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		String[] ids = json.split("A");
		if(ids.length < 3) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		int changeType = 1;
		try {
			changeType = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		int rid = 0;
		Set<Integer> rids = new HashSet<Integer>();
		List<Integer> roleIds = new ArrayList<Integer>();
		for(String role : ids[1].split("a")) {
			try {
				rid = Integer.parseInt(role);
			} catch(NumberFormatException e) {}
			if(rids.add(rid) && roleDao.chkExit(rid)) {
				roleIds.add(rid);
			}
		}
		if(roleIds.size() == 0) {
			return "{\"status\":1,\"mess\":\"角色不存在！\"}";
		}
		int uid = 0;
		Set<Integer> uids = new HashSet<Integer>();
		List<Integer> userIds = new ArrayList<Integer>();
		for(String user : ids[2].split("a")) {
			try {
				uid = Integer.parseInt(user);
			} catch(NumberFormatException e) {}
			if(uids.add(uid) && userDao.chkExit(uid)) {
				userIds.add(uid);
			}
		}
		if(userIds.size() == 0) {
			return "{\"status\":1,\"mess\":\"用户不存在！\"}";
		}
		switch(changeType) {
			case 2://增加
				for(int userId : userIds) {
					for(int roleId : roleIds) {
						user_RoleDao.delete(userId, roleId);
						user_RoleDao.save(new User_Role(userId, roleId));
					}
				}
				break;
			case 3://删除
				for(int userId : userIds) {
					for(int roleId : roleIds) {
						user_RoleDao.delete(userId, roleId);
					}
				}
				break;
			default://替换
				for(int userId : userIds) {
					user_RoleDao.delete(userId);
					for(int roleId : roleIds) {
						user_RoleDao.save(new User_Role(userId, roleId));
					}
				}
		}
		return "{\"status\":0}";
	}
//d
	public String remove(User user) {
		String account = SecuManager.currentAccount();
		String password = userDao.getU_passByAccount(account);
		if(passwordEncoder.encodePassword(user.getPassword(), account).equals(password)) {
			user_RoleDao.delete(user.getId());
			userDao.delete(user);
			return "{\"status\":0}";
		} else {
			return "{\"status\":1,\"mess\":\"密码错误！\",\"password\":false}";
		}
	}
//o
	public void makePassword(String account, String password) {
		System.out.println(account + " -> "+passwordEncoder.encodePassword(account, password));
	}
	public int loginUserNums() {
		return sessionRegistry.getUserNum();
	}
	public String findlgUser() {
		List<Object> slist = sessionRegistry.getAllPrincipals();
		StringBuilder json = new StringBuilder();
		json.append("共 " + sessionRegistry.getUserNum() + " 用户登录， " +
				sessionRegistry.getSessionNum() + " 处登录，服务器时间：" +
				ft.format(new Date()) + "<br>");
		for(int i=0; i<slist.size(); i++) {
			org.springframework.security.core.userdetails.User u = 
					(org.springframework.security.core.userdetails.User) slist.get(i);
			json.append("第 " + (i+1) + " 个用户，账号： " + u.getUsername() + "<br>");
			//包括被限制登录用户
			List<SessionInformation> ilist = sessionRegistry.getAllSessions(u, true);
			json.append("----" + ilist.size() + " 处登录<br>");
			for(int j=0; j<ilist.size(); j++) {
				SessionInformation sif = ilist.get(j);
				u = (org.springframework.security.core.userdetails.User) sif.getPrincipal();
				json.append("--------" + (j+1) +
						" 用户名：" + u.getStr()[0] +
						" 最后访问时间：" + ft.format(sif.getLastRequest()) +
						" ip：" +  sif.getRemoteIp() +
						//" session:" + sif.getSessionId() +
						//" 限制登录：" + sif.isExpired() +
						"<br>");
				//强制退出1.将其限制，2.将其移除，使用第一种比较好
				//sif.expireNow();
				//sessionRegistry.removeSessionInformation(sif.getSessionId());
			}
			json.append("<br>");
		}
		return json.toString();
	}
//
	public User getUserByName(String username) {
		return userDao.getUserByName(username);
	}

	@SuppressWarnings("rawtypes")
	public String getAllUserName() {
		String json = "[";
		String user_name;
		List usernames = userDao.getAllUserName();
		Iterator it = usernames.iterator();
		while(it.hasNext()){
			user_name = (String) it.next();
			json = json.concat("{\"id\":\""+user_name+"\",\"text\":\""+user_name+"\"},");
		}
		if(!usernames.isEmpty()){
			json = json.substring(0, json.length()-1);//去掉逗号
		}
		json = json.concat("]");
		return json;
	}

}
