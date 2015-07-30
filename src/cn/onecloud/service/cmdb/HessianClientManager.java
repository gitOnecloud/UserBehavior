package cn.onecloud.service.cmdb;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.HessianClientDao;
import cn.onecloud.model.cmdb.HessianClient;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.HessianClientPage;
import cn.onecloud.util.rsa.RSACoder;

@Component("hessianClientManager")
public class HessianClientManager {
	@Resource(name="hessianClientDao")
	private HessianClientDao hcDao;
//c
	/**
	 * 添加
	 */
	public String save(HessianClient hc) {
		if(hc==null || StaticMethod.StrSize(hc.getName(), 50)<1 ||
				StaticMethod.StrSize(hc.getDomain(), 63)<1 ||
				StaticMethod.StrSize(hc.getIp(), 255)<1) {
			return StaticMethod.inputError;
		}
		setRsakey(hc);
		hcDao.save(hc);
		return StaticMethod.addSucc;
	}
//r
	public JSONObject findByPage(HessianClientPage page) {
		List<HessianClient> hcs = hcDao.getAllByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(HessianClient hc : hcs) {
			JSONObject hcJSON = new JSONObject();
			hcJSON.put("id", hc.getId());
			hcJSON.put("name", hc.getName());
			hcJSON.put("domain", hc.getDomain());
			hcJSON.put("pubKey", hc.getPubkey());
			hcJSON.put("priKey", hc.getPrikey());
			hcJSON.put("modKey", hc.getModkey());
			hcJSON.put("ip", hc.getIp());
			array.add(hcJSON);
		}
		json.put("rows", array);
		return json;
	}
	/**
	 * 根据ip和域名获取密钥
	 */
	public HessianClient findHcByDomain(String ip, String domain) {
		return hcDao.getHcByDomain(ip, domain);
	}
//u
	public String change(HessianClient hc) {
		if(hc==null || hc.getId()==0 || 
				StaticMethod.StrSize(hc.getName(), 50)<1 ||
				StaticMethod.StrSize(hc.getDomain(), 63)<1 ||
				StaticMethod.StrSize(hc.getIp(), 255)<1) {
			return StaticMethod.inputError;
		}
		hcDao.update(hc);
		return StaticMethod.changeSucc;
	}
	/**
	 * 更新客户端的RSA密钥，同时返回密钥
	 */
	public String changeRSA(String hcId) {
		HessianClient hc = new HessianClient();
		try {
			hc.setId(Integer.parseInt(hcId));
		} catch(NumberFormatException e) {
			return StaticMethod.inputError;
		}
		setRsakey(hc);
		hcDao.updateRSA(hc);
		JSONObject hcJSON = new JSONObject();
		hcJSON.put("pubKey", hc.getPubkey());
		hcJSON.put("priKey", hc.getPrikey());
		hcJSON.put("modKey", hc.getModkey());
		hcJSON.put("status", 0);
		hcJSON.put("mess", "更新成功 ！");
		return hcJSON.toString();
	}
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		Set<Integer> ids = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("a")) {
			try {
				id = Integer.parseInt(s);
				if(ids.add(id)) {
					hcDao.delete(s, "HessianClient");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
//other
	/**
	 * 设置rsa密钥到HessianClient
	 */
	private void setRsakey(HessianClient hc) {
		KeyPair keyPair = RSACoder.initKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		hc.setPubkey(publicKey.getPublicExponent().toString(16));
		hc.setPrikey(privateKey.getPrivateExponent().toString(16));
		hc.setModkey(publicKey.getModulus().toString(16));
	}
}
