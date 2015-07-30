package cn.onecloud.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import cn.onecloud.exception.InvokeShellException;

public class LocalShell {
	public static String exec(String command) {
		Process p = null;
		InputStreamReader ir = null;
		LineNumberReader input = null;
		try {
			p = Runtime.getRuntime().exec(command);
			ir = new InputStreamReader(p.getInputStream());
			input = new LineNumberReader(ir);
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = input.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				input.close();
			} catch (Exception e) {
			}
			try {
				ir.close();
			} catch (Exception e) {
			}
			try {
				p.destroy();
			} catch (Exception e) {
			}
		}
	}
	
	public static String invokeShell(String command)
			throws InvokeShellException
	{
		Process p = null;
		InputStreamReader ir = null;
		LineNumberReader input = null;
		StringBuilder result;
		int exitValue;
		try
		{
			//IO 问题
			p = Runtime.getRuntime().exec(command);
			ir = new InputStreamReader(p.getInputStream());
			input = new LineNumberReader(ir);
			String line;
			result = new StringBuilder();
			exitValue = p.waitFor();
			while ((line = input.readLine()) != null)
			{
				result.append(line);
			}
			if (exitValue != 0)
			{
				throw new InvokeShellException(result.toString());
			}
			return result.toString();
		} catch (IOException e)
		{
			throw new InvokeShellException("出现IO问题");
		} catch (InterruptedException e)
		{
			throw new InvokeShellException("子进程尚未终止");
		}finally
		{
			try
			{
				input.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				ir.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			p.destroy();
		}
	}
}