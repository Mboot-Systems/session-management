package com.mboot.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HeadRowHeight(value = 40)
public class UserExcel {

	@ExcelProperty(value = "username", index = 0)
	@ColumnWidth(value = 15)
	private String username;

	@ExcelProperty(value = "nickname", index = 1)
	@ColumnWidth(value = 15)
	private String nickname;

	@ExcelProperty(value = "password", index = 2)
	@ColumnWidth(value = 20)
	private String password;

	@ExcelProperty(value = "email", index = 3)
	@ColumnWidth(value = 20)
	private String email;

	@ExcelProperty(value = "avatar", index = 4)
	@ColumnWidth(value = 20)
	private String avatar;

	@ExcelProperty(value = "Status\\r0 normal, 1 disabled", index = 5)
	@ColumnWidth(value = 20)
	private String status;

	@ExcelProperty(value = "Registration time", index = 6)
	@ColumnWidth(value = 20)
	private String createdTime;
}
