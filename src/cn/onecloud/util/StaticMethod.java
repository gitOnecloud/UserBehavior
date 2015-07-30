package cn.onecloud.util;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cn.onecloud.util.page.userbehavior.Page;

public class StaticMethod {

	private static Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static NumberFormat formatter = new DecimalFormat("#0.0");
	/**
	 * 将时间转换为2013-02-24 22:31:13格式
	 */
	public static String DateToString(Date date) {
		return ft.format(date);
	}
	
	private static Format ft1 = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 将时间转换为2013-02-24格式
	 */
	public static String DateToDay(Date date) {
		return ft1.format(date);
	}
	/**
	 * 将2013-02-24格式转换为时间
	 */
	public static Calendar DayToDDate(String day) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime((Date) ft1.parseObject(day));
		} catch (ParseException e) {}
		return cal;
	}
	
	private static Format ft2 = new SimpleDateFormat("yyyy-MM");
	/**
	 * 将时间转换为2013-02格式
	 */
	public static String DateToMouth(Date date) {
		return ft2.format(date);
	}
	/**
	 * 将2013-02格式转换为时间
	 */
	public static Calendar MouthToDate(String day) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime((Date) ft2.parseObject(day));
		} catch (ParseException e) {}
		return cal;
	}
	private static Format ftyear = new SimpleDateFormat("yyyy");
	/**
	 * 将2013格式转换为时间
	 */
	public static Calendar YearToDate(String day) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime((Date) ftyear.parseObject(day));
		} catch (ParseException e) {}
		return cal;
	}
	/**
	 * 根据输入的时间，返回时间范围
	 */
	public static String DateScope(Page page, String table) {
		String date = page.getDate();
		if(date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {//按天
			return table + ".date='" + date + "'";
		} else if(date.matches("\\d{4}-\\d{1,2}")) {//按月
			Calendar day = MouthToDate(date);
			return getDateSql(day, date, table);
		} else {//默认显示按月的最近
			Calendar cal = Calendar.getInstance();
			/*if(cal.get(Calendar.DATE) < 28) {//28号以后显示这个月的数据，其他显示上个月的
				cal.add(Calendar.MONTH, -1);
			}*/
			date = DateToMouth(cal.getTime());
			page.setDate(date);
			return getDateSql(cal, date, table);
		}
	}
	/**
	 * 查询语句中时间的范围，上个月28到这个月27
	 */
	public static String getDateSql(Calendar cal, String date, String table) {
		Calendar day = Calendar.getInstance();
		day.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 27);
		day.add(Calendar.MONTH, -1);
		return table + ".date>'" + DateToDay(day.getTime()) +
				"' and " + table + ".date<'" + date + "-28'";
	}
	/**
	 * 返回字符串长度
	 * @return 空:-1
	 */
	public static int StrSize(String str) {
		if(str == null) {
			return -1;
		}
		return str.length();
	}
	/**
	 * 返回字符串长度，可定义最多长度
	 * @param max 最大长度
	 * @return 空:-1  超出最大长度：-2
	 */
	public static int StrSize(String str, int max) {
		if(str == null) {
			return -1;
		}
		if(str.length() > max) {
			return -2;
		}
		return str.length();
	}
	/**
	 * 字符串转换成数字
	 * * @return error: -1
	 */
	public static int Str2Int(String i) {
		try {
			return Integer.parseInt(i);
		} catch(Exception e) {
			return -1;
		}
	}
	/**
	 * 类似javascript array的join，将数组转换成字符串，中间用字符隔开
	 * @param array 数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String Array2Str(Set<Integer> array, String separator) {
		if(array.size() == 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for(int i : array) {
			str.append(i + separator);
		}
		return str.substring(0, str.length()-1);
	}
	
	public static final String inputError = FailMess("输入有误！");
	public static final String addSucc = SuccMess("添加成功！");
	public static final String changeSucc = SuccMess("更新成功！");
	public static final String removeSucc = SuccMess("删除成功！");
	public static final String verifyError = FailMess("数据已存在");
	public static final String verifySucc = SuccMess("数据不存在，可添加");
	public static final String verifyUniqueSucc = SuccMess("该Basket无关联其他Satallite");
	public static final String verifyUniqueError = FailMess("该Basket关联其他Satallite，请慎重选择");
	
	/**
	 * 成功返回的json
	 * @return {'status':0,'mess':'message'}
	 */
	public static String SuccMess(String str) {
		return JsonMess(0, str);
	}
	/**
	 * 失败返回的json
	 * @return {'status':1,'mess':'message'}
	 */
	public static String FailMess(String str) {
		return JsonMess(1, str);
	}
	public static String JsonMess(int sta, String str) {
		return "{\"status\":" + sta + ",\"mess\":\"" + str + "\"}";
	}
	/**
	 * @return {'status':sta,'key':value}
	 */
	public static String JsonMess(int sta, String key, String value) {
		return "{\"status\":" + sta + ",\"" + key + "\":" + value + "}";
	}
	
	/**
	 * 外网代理存在时，也能正确获取ip
	 * @return ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		//System.out.println(ip);
		return ip;
	}
	/**
	 * 判断ip格式
	 * @param IP
	 * @return
	 */
	 public static boolean isip(String IP){//判断是否是一个IP
		 	if(IP.length()==0||IP==null) return false;
	        boolean b = false; 
	        while(IP.startsWith(" ")){ 
	            IP= IP.substring(1,IP.length()).trim(); 
	        } 
	        while(IP.endsWith(" ")){ 
	            IP= IP.substring(0,IP.length()-1).trim(); 
	        }
	        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){ 
	            String s[] = IP.split("\\."); 
	            if(Integer.parseInt(s[0])<255) 
	                if(Integer.parseInt(s[1])<255) 
	                    if(Integer.parseInt(s[2])<255) 
	                        if(Integer.parseInt(s[3])<255) 
	                            b = true; 
	        } 
	        return b; 
	 }
	 
	 /**
	  * 处理ip 172.16.1.1..2,172.16.2.3..4
	  * @param ip
	  * @return
	  */
	 public static List<String> doip(String ip){
		 List<String> ip_list=new ArrayList<String>();
			String[] ip_com = ip.split(",");
			for(int i=0;i<ip_com.length;i++){
				String[] ips = ip_com[i].split("\\.\\.");
				if(ips.length==2){
					boolean is = isip(ips[0]);
					if(is==false){
						return null;
					}
					String[] com = ips[0].split("\\.");
					int n = Integer.valueOf(ips[1]) - Integer.valueOf(com[3]);
					if(n<0){
						return null;
					}
					for(int b=0;b<=n;b++){
						ip_list.add(com[0]+"."+com[1]+"."+com[2]+"."+(Integer.valueOf(com[3])+b));
					}
				}else{
					boolean is = isip(ips[0]);
					if(is==false){
						return null;
					}
					ip_list.add(ips[0]);
				}
			}
			return ip_list;
	 }
	
	 /**
	  * 判断字符串是否是整数
	  */
	 public static boolean isInteger(String value) {
	  try {
	   Integer.parseInt(value);
	   return true;
	  } catch (NumberFormatException e) {
	   return false;
	  }
	 }

	 /**
	  * 判断字符串是否是浮点数
	  */
	 public static boolean isDouble(String value) {
	  try {
	   Double.parseDouble(value);
	   if (value.contains("."))
	    return true;
	   return false;
	  } catch (NumberFormatException e) {
	   return false;
	  }
	 }

	 /**
	  * 判断字符串是否是数字
	  */
	 public static boolean isNumber(String value) {
	  return isInteger(value) || isDouble(value);
	 }
	/**
	 * 把浮点数四舍五入保留一位小数点，返回.
	 * @param number
	 * @return
	 */
	 public static double numberToString(double number)
	 {
		 return Double.parseDouble(formatter.format(number));
	 }
	/**
	 * 类似javascript array的join，将数组转换成字符串，中间用字符隔开
	 * @param array 数组
	 * @param separator 分隔符
	 * @return
	 */
	public static String Set2Str(Set<Integer> array, String separator) {
		if(array.size() == 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for(int i : array) {
			str.append(i + separator);
		}
		return str.substring(0, str.length()-1);
	}
	/**
	 * 字符串转16进制，可以用于解决中文编码问题
	 */
	public static String Str2Hex(String str) {
		try {
			return new BigInteger(1, str.getBytes("UTF-8")).toString(16);
		} catch (Exception e) {
			return str;
		}
	}
	/**
	 * 16进制转字符串，可以用于解决中文编码问题
	 */
	public static String hex2Str(String str) {
		try {
			byte[] bt = new BigInteger(str, 16).toByteArray();
			if(bt[0] == 0) {
				return new String(bt, 1, bt.length-1, "UTF-8");
			} else {
				return new String(bt, "UTF-8");
			}
		} catch (Exception e) {
			return str;
		}
	}
}
