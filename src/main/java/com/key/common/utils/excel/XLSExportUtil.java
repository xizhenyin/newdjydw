package com.key.common.utils.excel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 生成导出Excel文件对象
 *
 */
public class XLSExportUtil {
	// 设置cell编码解决中文高位字节截断
	// 定制日期格式
	private static String DATE_FORMAT = " m/d/yy "; // "m/d/yy h:mm"
	// 定制浮点数格式
	private static String NUMBER_FORMAT = " #,##0.00 ";
	private String xlsFileName;
	private String path;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFSheet currentSheet;
	
	
	public XSSFSheet getCurrentSheet() {
		return currentSheet;
	}

	public void setCurrentSheet(XSSFSheet currentSheet) {
		this.currentSheet = currentSheet;
	}

	/**
	 * 初始化Excel
	 * 
	 * @param fileName
	 *            导出文件名
	 */
	public XLSExportUtil(String fileName,String path) {
		this.xlsFileName = fileName;
		this.path=path;
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public XSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(XSSFSheet sheet) {
		this.sheet = sheet;
	}

	public XSSFRow getRow() {
		return row;
	}

	public void setRow(XSSFRow row) {
		this.row = row;
	}

	public void nextSheet() {
		this.sheet = workbook.createSheet();
	}
	/** */
	/**
	 * 导出Excel文件
	 * 
	 * @throws XLSException
	 */
	public void exportXLS() throws Exception {
		try {
			File file=new File(path);
			if(!file.exists()) {
				file.mkdirs();
			}
			FileOutputStream fOut = new FileOutputStream(path+File.separator+xlsFileName);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new Exception(" 生成导出Excel文件出错! ", e);
		} catch (IOException e) {
			throw new Exception(" 写入Excel文件出错! ", e);
		}

	}

	/** */
	/**
	 * 增加一行
	 * 
	 * @param index
	 *            行号
	 */
	public void createRow(int index) {
		this.row = this.sheet.createRow(index);
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	@SuppressWarnings("deprecation")
	public void setCell(int index, String value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}

	public void setCell(XSSFRow row,int index, String value) {
		XSSFCell cell = row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}
	
	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	@SuppressWarnings("deprecation")
	public void setCell(int index, Calendar value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellValue(value.getTime());
		XSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
	//	cellStyle.setDataFormat(XSSFDataFormat.getBuiltinFormat(DATE_FORMAT)); // 设置cell样式为定制的日期格式
		cell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, int value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	/** */
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, double value) {
		XSSFCell cell = this.row.createCell((short) index);
		cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		XSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		XSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}

}
