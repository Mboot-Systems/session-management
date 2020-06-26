package com.mboot.utils;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */

public class ExcelUtil {

	/**
	 * @param response
	 * @param data
	 * @param fileName
	 * @param sheetName
	 * @param clazz
	 * @throws Exception
	 */
	public static void writeExcel(HttpServletResponse response, List<? extends Object> data, String fileName,
			String sheetName, Class clazz) throws Exception {
		WriteCellStyle headWriteCellStyle = new WriteCellStyle();
		headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
		contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
				contentWriteCellStyle);
		EasyExcel.write(getOutputStream(fileName, response), clazz).excelType(ExcelTypeEnum.XLSX).sheet(sheetName)
				.registerWriteHandler(horizontalCellStyleStrategy).doWrite(data);
	}

	private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf8");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
		return response.getOutputStream();
	}
}
