package cn.onecloud.util;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshShell {
	/**
	 * 利用JSch包实现远程主机SHELL命令执行
	 * @param ip 主机IP
	 * @param user 主机登陆用户名
	 * @param psw 主机登陆密码
	 * @param port 主机ssh2登陆端口，如果取默认值，传-1
	 * @param privateKey 密钥文件路径
	 * @param passphrase 密钥的密码
	 */
	public static String exec(String ip, String user, String psw, int port,
			String command) {
		Session session = null;
		Channel channel = null;
		InputStream in = null;
		try {
			JSch jsch = new JSch();
			// 设置密钥和密码
			/*
			 * if (privateKey != null && !"".equals(privateKey)) { if
			 * (passphrase != null && "".equals(passphrase)) { //设置带口令的密钥
			 * jsch.addIdentity(privateKey, passphrase); } else { //设置不带口令的密钥
			 * jsch.addIdentity(privateKey); } }
			 */
			session = jsch.getSession(user, ip, port);
			session.setPassword(psw);
			// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			// 设置登陆超时时间
			session.connect(30000);
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			in = channel.getInputStream();
			channel.connect();
			StringBuilder result = new StringBuilder();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					result.append(new String(tmp, 0, i));
					// System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					// System.out.println("exit-status: "+channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				session.disconnect();
			} catch (Exception e) {
			}
			try {
				channel.disconnect();
			} catch (Exception e) {
			}
		}
	}
}
