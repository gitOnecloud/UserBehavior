package cn.onecloud.service.cmdb.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.server.CabinetDao;
import cn.onecloud.dao.cmdb.server.RoomDao;
import cn.onecloud.dao.cmdb.server.ServerDao;
import cn.onecloud.model.cmdb.server.Cabinet;
import cn.onecloud.model.cmdb.server.Room;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.RoomPage;
import cn.onecloud.util.pojo.cmdb.ServerName;

@Component("roomManager")
public class RoomManagerImpl implements RoomManager {
	@Resource(name="roomDao")
	private RoomDao rdao;
	@Resource(name="cabinetDao")
	private CabinetDao cdao;
	@Resource(name="serverDao")
	private ServerDao sdao;
//c
	public String save(Room room) {
		if(room==null || StaticMethod.StrSize(room.getName())<1 ||
				room.getCabStyle()==null) {
			return StaticMethod.inputError;
		}
		rdao.save(room);
		return StaticMethod.addSucc;
	}
//r
	/**
	 * 根据ID查找机房信息以及机柜信息
	 */
	public String findById(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		try {
			Integer.parseInt(json);
		} catch(NumberFormatException e) {
			return StaticMethod.inputError;
		}
		Style style = rdao.getRoomStyle(json);
		if(style == null) {
			return StaticMethod.inputError;
		}
		JSONObject room = new JSONObject();
		JSONObject styleJson = new JSONObject();
		styleJson.put("horInterval", style.getHorInterval());
		styleJson.put("horNum", style.getHorNum());
		styleJson.put("verInterval", style.getVerInterval());
		styleJson.put("verNum", style.getVerNum());
		styleJson.put("insideHeight", style.getInsideHeight());
		styleJson.put("insideWidth", style.getInsideWidth());
		room.put("style", styleJson);
		List<Cabinet> cabinets = cdao.getCabsByRId(json);
		JSONArray array = new JSONArray();
		for(Cabinet cabinet : cabinets) {
			JSONObject roomJson = new JSONObject();
			roomJson.put("id", cabinet.getId());
			roomJson.put("name", cabinet.getName());
			roomJson.put("sort", cabinet.getSort());
			array.add(roomJson);
		}
		room.put("cabinets", array);
		return room.toString();
	}
	/**
	 * 分页查找
	 */
	public JSONObject findByPage(RoomPage page) {
		List<Room> rooms = rdao.getAllByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Room room : rooms) {
			JSONObject roomJson = new JSONObject();
			roomJson.put("id", room.getId());
			roomJson.put("name", room.getName());
			roomJson.put("remark", room.getRemark());
			roomJson.put("sId", room.getCabStyle().getId());
			roomJson.put("sName", room.getCabStyle().getName());
			
			array.add(roomJson);
		}
		json.put("rows", array);
		return json;
	}
	/**
	 * 查找全部样式
	 */
	public JSONArray findAll() {
		List<Room> rooms = rdao.getAll();
		JSONArray array = new JSONArray();
		for(Room room : rooms) {
			JSONObject styleJson = new JSONObject();
			styleJson.put("id", room.getId());
			styleJson.put("name", room.getName());
			//给tree使用
			styleJson.put("text", room.getName());
			array.add(styleJson);
		}
		return array;
	}
	/**
	 * 
	 * @param json
	 * @return 0-机房名称 1-excel输出流
	 * @throws IOException
	 */
	public List<Object> download(String json) throws IOException {
		int rid = 0;
		try {
			rid = Integer.parseInt(json);
		} catch(NumberFormatException e) {
			return null;
		}
		List<Object> result = new ArrayList<Object>();//结果返回
		List<String[]> server_nosort = new ArrayList<String[]>();//未排序好的服务器
		List<String> cab_nosort = new ArrayList<String>();//未排序好的机柜
		List<Integer> cabinet_maxHor = new ArrayList<Integer>();//机柜最大横数
		Room room = rdao.getRoomById(rid);
		result.add(room.getName());
		//初始化机房的机柜
		int cab_num = room.getCabStyle().getHorNum()*
				room.getCabStyle().getVerNum();//机柜容量
		List<Cabinet> cabinet_sort = new ArrayList<Cabinet>();//排序好的机柜
		for(int i=0; i<cab_num; i++) {
			cabinet_sort.add(null);
		}
		List<Cabinet> cabinets = cdao.getCabinetsByRId(json);
		List<List<List<ServerName>>> cab_sort = new ArrayList<List<List<ServerName>>>();//排序好的机柜
		//排序机柜
		for(Cabinet cab : cabinets) {
			if(cab.getSort()<1 || cab.getSort()>cab_num) {//未排序好的机柜
				cab_nosort.add(cab.getName());
			} else {
				cabinet_sort.set(cab.getSort()-1, cab);
			}
		}
		//找出每一大行，最大机柜行数
		for(int index=0; index<cabinet_sort.size(); index++) {
			Cabinet cab = cabinet_sort.get(index);
			//计算机房每一大行中，最大机柜行数
			if(index%room.getCabStyle().getVerNum() == 0) {
				cabinet_maxHor.add(0);
			}
			if(cab == null) {
				continue;
			}
			if(cab.getCabStyle().getHorNum() > cabinet_maxHor.get(cabinet_maxHor.size()-1)) {
				cabinet_maxHor.set(cabinet_maxHor.size()-1, cab.getCabStyle().getHorNum());
			}
		}
		int room_horNum = -1;//定位当前机房行数
		List<List<Integer>> cab_titleNum = new ArrayList<List<Integer>>();//每一大行中，机柜占的列数
		List<Integer> cab_titleNumTemp = new ArrayList<Integer>();//机柜列数
		for(int index=0; index<cabinet_sort.size(); index++) {
			Cabinet cab = cabinet_sort.get(index);
			if(index%room.getCabStyle().getVerNum() == 0) {
				room_horNum ++;
				if(index != 0) {//记录机柜列数
					List<Integer> cab_Temp = new ArrayList<Integer>();
					cab_Temp.addAll(cab_titleNumTemp);
					cab_titleNum.add(cab_Temp);
					cab_titleNumTemp.clear();
				}
			}
			if(cab == null) {
				cab_sort.add(null);
				continue;
			}
			//记录机柜列数
			cab_titleNumTemp.add(cab.getCabStyle().getVerNum());
			//初始化机柜
			int cabinet_num = cab.getCabStyle().getHorNum()*
					cab.getCabStyle().getVerNum();//机柜容量
			List<ServerName> server_sort = new ArrayList<ServerName>();//排序好的服务器
			for(int i=0; i<cabinet_num; i++) {
				server_sort.add(null);
			}
			List<Server> servers = sdao.getSvsByCId(Integer.toString(cab.getId()));
			//过滤未排序好的服务器
			for(Server server : servers) {
				if(server.getSort()<1 || server.getSort()>cabinet_num
						|| server_sort.get(server.getSort()-1) != null) {//未排序好的服务器
					server_nosort.add(new String[]{server.getIp(), cab.getName()});
				} else {
					server_sort.set(server.getSort()-1,
							new ServerName(server.getIp(), server.getIsActive(), server.getRemark()));
				}
			}
			//添加机柜名
			List<List<ServerName>> hor_sort = new ArrayList<List<ServerName>>();//机柜横排序
			
			List<ServerName> cab_name = new ArrayList<ServerName>();//机柜名
			cab_name.add(new ServerName(cab.getName()));
			for(int i=1; i<cab.getCabStyle().getVerNum(); i++) {
				cab_name.add(null);
			}
			hor_sort.add(cab_name);
			//添加服务器
			for(int i=0; i<server_sort.size(); i+=cab.getCabStyle().getVerNum()) {
				List<ServerName> cab_server = new ArrayList<ServerName>();//一行机柜中的服务器
				for(int j=0; j<cab.getCabStyle().getVerNum(); j++) {
					cab_server.add(server_sort.get(i+j));
				}
				hor_sort.add(cab_server);
			}
			//行数不够的机柜，补充完整
			for(; hor_sort.size()-1<cabinet_maxHor.get(room_horNum);) {
				List<ServerName> cab_server = new ArrayList<ServerName>();
				for(int j=0; j<cab.getCabStyle().getVerNum(); j++) {
					cab_server.add(null);
				}
				hor_sort.add(cab_server);
			}
			cab_sort.add(hor_sort);
		}
		//记录每一大行中机柜的列数
		cab_titleNum.add(cab_titleNumTemp);
		List<Integer> title_num = new ArrayList<Integer>();//机柜名在表中的行位置
		List<List<ServerName>> sort_cab = new ArrayList<List<ServerName>>();//排序好的机柜
		//将机柜按机房列数排序，格式也由原来以机柜为单位，改为以行为单位
		for(int i=0; i<cabinet_maxHor.size(); i++) {
			title_num.add(sort_cab.size());//记录标题所在行数
			for(int j=0; j<=cabinet_maxHor.get(i); j++) {
				List<ServerName> cab3 = new ArrayList<ServerName>();
				for(int k=0; k<room.getCabStyle().getVerNum(); k++) {
					List<List<ServerName>> cab = cab_sort.get(k+room.getCabStyle().getVerNum()*i);
					if(cab == null) {
						continue;
					}
					cab3.addAll(cab.get(j));
				}
				sort_cab.add(cab3);
			}
		}
		/*for(List<Integer> temp : cab_titleNum) {
			System.out.println("******");
			String str = "";
			for(int tmp : temp) {
				str += "-" + tmp;
			}
			System.out.println(str);
		}*/
		/*for(List<String> hor_sort : sort_cab) {
			if(hor_sort==null) continue;
			System.out.println("******");
			String str = "";
			for(String temp : hor_sort) {
				str += "-" + temp;
			}
			System.out.println(str);
		}*/
		/*for(List<List<String>> hor_sort : cab_sort) {
			if(hor_sort==null) continue;
			System.out.println("******");
			for(List<String> temp : hor_sort) {
				String str = "";
				for(String tmp : temp) {
					str += "-" + tmp;
				}
				System.out.println(str);
			}
		}
		for(int i : cabinet_maxHor) {
			System.out.println("Cab_maxNum: " + i);
		}*/
		
		HSSFWorkbook workbook = getWorkBook(sort_cab, title_num, cab_titleNum, cab_nosort, server_nosort);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		byte[] ba = output.toByteArray();
		InputStream excelStream = new ByteArrayInputStream(ba);
		output.flush();
		output.close();
		result.add(excelStream);
		return result;
	}
	/**
	 * 
	 * @param sort_cab 数据，以行为单位
	 * @param title_num 标题所在行数
	 * @param cab_titleNum 每个标题的列数
	 * @param cab_nosort 未排序好的机柜
	 * @param server_nosort 未排序好的服务器
	 * @return
	 */
	private HSSFWorkbook getWorkBook(List<List<ServerName>> sort_cab, List<Integer> title_num,
			List<List<Integer>> cab_titleNum, List<String> cab_nosort, List<String[]> server_nosort) {
		//创建工作表
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		//服务器样式
		HSSFCellStyle cellstyle = workbook.createCellStyle();
		cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		cellstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中      
		cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
		
		//服务器样式2 故障或停机状态
		HSSFCellStyle cellstyle3 = workbook.createCellStyle();
		cellstyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充格式
		cellstyle3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);//填充颜色
		cellstyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		cellstyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellstyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellstyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellstyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
		cellstyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
		
		//机柜名样式
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   //粗体显示
		HSSFCellStyle cellstyle2 = workbook.createCellStyle();
		cellstyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充格式
		cellstyle2.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);//填充颜色
		cellstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		cellstyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellstyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellstyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中      
		cellstyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
		cellstyle2.setFont(font);
		
		//所有注释的容器
		HSSFPatriarch patr = sheet.createDrawingPatriarch();
		int succ_counts = 0;//正常服务器统计
		int fail_counts = 0;//问题服务器统计
		//输出机柜名以及服务器
		for(int i=0; i<sort_cab.size(); i++) {
			List<ServerName> cab = sort_cab.get(i);
			HSSFRow row = sheet.createRow(i);//创建第i行
			boolean isTitle = false;
			if(title_num.contains(i)) {
				row.setHeight((short) 500);
				isTitle = true;
			} else {
				row.setHeight((short) 320);
			}
			HSSFCell cell;
			for(int j=0;j<cab.size();j++) {
				cell = row.createCell(j);//创建第j列
				ServerName serverName = cab.get(j);
				if(serverName == null) {
					cell.setCellValue(new HSSFRichTextString(""));
				} else {
					cell.setCellValue(new HSSFRichTextString(serverName.getName()));
				}
				if(isTitle) {
					cell.setCellStyle(cellstyle2);
				} else {
					if(serverName != null) {
						if(serverName.getType() == 0) {
							cell.setCellStyle(cellstyle3);
							fail_counts ++;
						} else {
							cell.setCellStyle(cellstyle);
							succ_counts ++;
						}
						if(StaticMethod.StrSize(serverName.getRemark())>1) {
							HSSFComment comment = patr.createComment(new HSSFClientAnchor(0,0,0,0,(short)3,3,(short)4,8));
							comment.setString(new HSSFRichTextString(serverName.getRemark()));
							cell.setCellComment(comment);
						}
					} else {
						cell.setCellStyle(cellstyle);
					}
				}
				sheet.setColumnWidth(j, 3500);
			}
		}
		//合并机柜名单元格
		for(int i=0; i<cab_titleNum.size(); i++) {
			List<Integer> temp = cab_titleNum.get(i);
			int index = 0;//列位置
			for(int tmp : temp) {
				if(tmp > 1) {//需要合并单元格
					sheet.addMergedRegion(new CellRangeAddress(
							title_num.get(i), title_num.get(i), index, index+tmp-1));
				}
				index += tmp;
			}
		}
		//未排序好的机柜
		if(cab_nosort.size() > 0) {
			HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
			row.setHeight((short) 500);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("未排序好的机柜");
			cell.setCellStyle(cellstyle2);
			cell = row.createCell(1);
			cell.setCellStyle(cellstyle2);
			sheet.addMergedRegion(new CellRangeAddress(
					sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 1));
			for(String str : cab_nosort) {
				row = sheet.createRow(sheet.getLastRowNum()+1);//创建第i行
				row.setHeight((short) 320);
				cell = row.createCell(0);//创建第j列
				cell.setCellValue(new HSSFRichTextString(str));
				cell.setCellStyle(cellstyle);
			}
		}
		//未排序好的服务器
		if(server_nosort.size() > 0) {
			HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
			row.setHeight((short) 500);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("未排序好的服务器");
			cell.setCellStyle(cellstyle2);
			cell = row.createCell(1);
			cell.setCellStyle(cellstyle2);
			sheet.addMergedRegion(new CellRangeAddress(
					sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 1));
			for(String[] str : server_nosort) {
				row = sheet.createRow(sheet.getLastRowNum()+1);//创建第i行
				row.setHeight((short) 320);
				cell = row.createCell(0);//创建第j列
				cell.setCellValue(new HSSFRichTextString(str[0]));
				cell.setCellStyle(cellstyle);
				cell = row.createCell(1);//创建第j列
				cell.setCellValue(new HSSFRichTextString(str[1]));
				cell.setCellStyle(cellstyle);
			}
		}
		//统计
		HSSFRow row = sheet.createRow(sheet.getLastRowNum()+2);
		row.setHeight((short) 500);
		HSSFCell cell = row.createCell(0);
		if(fail_counts == 0) {
			cell.setCellValue("共 "+succ_counts+" 台服务器。");
		} else {
			cell.setCellValue("共 "+(succ_counts+fail_counts)+" 台服务器，包含 "+fail_counts+" 台问题服务器。");
		}
		cell.setCellStyle(cellstyle);
		for(int i=0; i<3; i++) {
			cell = row.createCell(i+1);
			cell.setCellStyle(cellstyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(
				sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 3));
		//备注
		row = sheet.createRow(sheet.getLastRowNum()+2);
		row.setHeight((short) 500);
		cell = row.createCell(0);
		cell.setCellValue("黄色代表问题服务器，故障、停机、或者不存在");
		cell.setCellStyle(cellstyle3);
		for(int i=0; i<3; i++) {
			cell = row.createCell(i+1);
			cell.setCellStyle(cellstyle3);
		}
		sheet.addMergedRegion(new CellRangeAddress(
				sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 3));
		return workbook;
	}
//u
	public String change(Room room) {
		if(room==null || room.getId()==0 || StaticMethod.StrSize(room.getName())<1 ||
				room.getCabStyle()==null) {
			return StaticMethod.inputError;
		}
		rdao.update(room);
		return StaticMethod.changeSucc;
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
					rdao.delete(s, "Room");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
}
