package com.mboot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */

@Data
@TableName("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = -5144055068797033748L;

	@TableId(type = IdType.AUTO)
	private Long id;

	@NotBlank(message = "Username can not be empty")
	private String username;

	private String nickname;

	private String password;

	private String email;

	private String avatar;

	private Integer status = 0;

	private Date createdTime;

	private String createdBy;

	private Date updatedTime;

	private String updatedBy;

	public User(String username, String nickname, String password, String email, String avatar, Integer status,
			Date createdTime, String createdBy, Date updatedTime, String updatedBy) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.email = email;
		this.avatar = avatar;
		this.status = status;
		this.createdTime = createdTime;
		this.createdBy = createdBy;
		this.updatedTime = updatedTime;
		this.updatedBy = updatedBy;
	}
}
