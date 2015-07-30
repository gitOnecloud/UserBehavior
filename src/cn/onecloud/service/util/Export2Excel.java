package cn.onecloud.service.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class Export2Excel {

	
	@SuppressWarnings("unchecked")
	public static InputStream serverexport2Excel(List<String> columnNames ,List<String> pageNames , List<String> pagevalue , List<?> list) throws Exception{
		if(list == null){
			throw new Exception("Exception in export2Excel(columnNames,columnMethods,list),the list is null");
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(); //行
		HSSFRow row;  //列
		HSSFCell cell; //格
		//HSSFCellStyle cellstyle = header_cellstyle(workbook);
		
		//设置选择条件
		for(int i=0;i<pageNames.size();i++){
			sheet.setColumnWidth(i, 5000);   //设置列宽
			row = sheet.createRow(i);
			row.setHeight((short) 400);  //设置行高
			cell = row.createCell(0);    
			cell.setCellValue(new HSSFRichTextString(pageNames.get(i)));
			cell.setCellStyle(header_cellstyle(workbook,1));
			cell = row.createCell(1);    
			cell.setCellValue(new HSSFRichTextString(pagevalue.get(i)));
			cell.setCellStyle(header_cellstyle(workbook,0));
		}
		
		row = sheet.createRow(pageNames.size()+1);//空白行
		row = sheet.createRow(pageNames.size()+2);
		
		//设置列名
		row = sheet.createRow(pageNames.size()+3);
		for(int i=0;i < columnNames.size();i++){
			sheet.setColumnWidth(i, 5000);   //设置列宽
			cell = row.createCell(i);    //创建第i列
			cell.setCellValue(new HSSFRichTextString(columnNames.get(i)));
			cell.setCellStyle(header_cellstyle(workbook,1));
		}
		
		//输出各行数据
		List<String> list_object;
		Object value;
		String cellvalue;
		String[] value_com = null;
		for(int i=0;i < list.size();i++){
			list_object = (List<String>) list.get(i);
			row = sheet.createRow(i+pageNames.size()+4);   //创建第i+1行
			for(int j = 0;j < list_object.size();j++){
				cell = row.createCell(j); //创建第j列
				value = list_object.get(j);
				if(value != null){
					cellvalue = value.toString();
					value_com = cellvalue.split("#");
				}else{
					cellvalue = "";
				}
				cellvalue = value_com[0];
				cellvalue = cellvalue.replace("\\r\\n", "\r\n");
				cellvalue = cellvalue.replace("\\\\", "\\");
				cellvalue = cellvalue.replace("\\/", "/");
				cellvalue = cellvalue.trim();
				cell.setCellValue(new HSSFRichTextString(cellvalue));
				
				if(list_object.size()==1){
					cell.setCellStyle(header_cellstyle(workbook,1));
				}
				else if(value_com.length==2){
					cell.setCellStyle(content_cellstyle(workbook,value_com[1]));
				}else{
					//cell.setCellStyle(content_cellstyle(workbook,"ffffff"));
				}
			}
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		byte[] ba = output.toByteArray();
		InputStream excelStream = new ByteArrayInputStream(ba);
		output.flush();
		output.close();
		return excelStream;
	}

	
	public static HSSFCellStyle header_cellstyle(HSSFWorkbook workbook , int color){
		HSSFFont font = workbook.createFont();
		HSSFCellStyle cellstyle = workbook.createCellStyle();
		cellstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);   //填充颜色
		cellstyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		cellstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   //粗体显示
		cellstyle.setFont(font);
		//cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中      
		cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
		return cellstyle;
	}
	
	public static HSSFCellStyle content_cellstyle(HSSFWorkbook workbook , String color){
		HSSFFont font = workbook.createFont();
		HSSFCellStyle cellstyle = workbook.createCellStyle();
		cellstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);   
		cellstyle.setFillForegroundColor(StaticData.colorMap.get(color));//填充颜色
		
		cellstyle.setFont(font);
		cellstyle.setWrapText(true);    //设置自动换行
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
		cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
		return cellstyle;
	}
}
