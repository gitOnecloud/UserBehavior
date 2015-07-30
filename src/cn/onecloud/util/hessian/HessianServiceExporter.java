package cn.onecloud.util.hessian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.caucho.HessianExporter;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import cn.onecloud.model.cmdb.HessianClient;
import cn.onecloud.service.cmdb.HessianClientManager;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.rsa.RSACoder;

/**
 * 通过spring代理hessian
 * 使用自定义类替换org.springframework.remoting.caucho.HessianServiceExporter
 * 加入安全控制
 */
public class HessianServiceExporter extends HessianExporter implements HttpRequestHandler {
	//读取客户端rsa密钥
	private HessianClientManager hessianClientManager;
	/**
	 * Processes the incoming Hessian request and creates a Hessian response.
	 */
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (!"POST".equals(request.getMethod())) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(),
					new String[] {"POST"}, "HessianServiceExporter only supports POST requests");
		}
		//检查参数是否正确
		String domain = request.getHeader("Hessian-Client");
		if(StaticMethod.StrSize(domain) < 1) {
			System.out.println(this.getClass() + " => 参数错误，Domain: " + domain);
			throw new ServletException("No permission: domain error!");
		}
		//检查ip和域名是否允许
		String clientHost = StaticMethod.getRemoteAddr(request);
		HessianClient client = hessianClientManager.findHcByDomain(clientHost, domain);
		if(client == null) {
			System.out.println(this.getClass() + " => 非法访问，IP: " + clientHost + ", Domain: " + domain);
			//throw new SecurityException("非法访问。");
			throw new ServletException("No permission: ip illegal or domain error!");
		}
		//System.out.println("IP: " + clientHost + ", Domain: " + domain);
		//进行rsa解码
		long clientTime = 0;
		try {
			String encode = request.getHeader("Hessian-Encoding");
			String code = RSACoder.decryptByPrivateKey(encode, client.getPrikey(), client.getModkey());
			clientTime = Long.parseLong(code);
		} catch(Exception e) {
			System.out.println(this.getClass() + " => 解码失败: clientTime=" + clientTime);
			throw new ServletException("No permission: decoded error!");
		}
		//检查是否超时
		//System.out.println("Hessian-Encoding: " + clientTime);
		long serverTime = System.currentTimeMillis();
		if((clientTime-serverTime)>10000 || (serverTime-clientTime)>10000) {//超过10s表示超时
			System.out.println(this.getClass() + " => 超时: serverTime - clientTime = " + (serverTime-clientTime));
			throw new ServletException("No permission: timeout!");
		}
		//end
		
		response.setContentType(CONTENT_TYPE_HESSIAN);
		try {
		  invoke(request.getInputStream(), response.getOutputStream());
		}
		catch (Throwable ex) {
		  throw new NestedServletException("Hessian skeleton invocation failed", ex);
		}
	}
//set get
	public HessianClientManager getHessianClientManager() {
		return hessianClientManager;
	}
	public void setHessianClientManager(HessianClientManager hessianClientManager) {
		this.hessianClientManager = hessianClientManager;
	}

}
