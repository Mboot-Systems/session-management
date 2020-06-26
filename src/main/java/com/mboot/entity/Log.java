package com.mboot.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Data
@TableName("log")
public class Log implements Serializable {

	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private String name;
	private Integer logType;
	private String requestUrl;
	private String requestType;
	private String requestParam;
	private String username;
	private String ip;
	private String ipInfo;
	private Integer costTime;
	@TableField(value = "del_flag")
	@TableLogic
	private Integer delFlag = 0;
	private String createBy;
	private Date createTime;
	private String updateBy;
	private Date updateTime;

}
