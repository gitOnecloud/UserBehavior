package cn.onecloud.service.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Component;

@Component("staticData")
public class StaticData {
	public static Map<String, Short> colorMap = new HashMap<String,Short>();
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		Enumeration colors = HSSFColor.getTripletHash().elements(); 
		while (colors.hasMoreElements()) { 
			HSSFColor color = (HSSFColor) colors.nextElement();
			short[] cl =  color.getTriplet();
			colorMap.put(ShortToHex(cl[0]) +
					ShortToHex(cl[1]) +
					ShortToHex(cl[2]), color.getIndex());
		}
		/*int i = 1;
		for (Iterator it = colorMap.keySet().iterator(); it.hasNext();) {
			String hexString = (String) it.next();
			System.out.println("<h1 style='background-color:#" + hexString + "'>第 " + i + " 种</h1>");
			i++;
		}*/
	}
	private String ShortToHex(Short s) {
		String str = Integer.toHexString(s);
		if(str.equals("0")) {
			return "00";
		}
		return str;
	}
	/*public static void main(String[] args) {
		init();
	}*/
}
