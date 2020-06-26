package com.mboot.mapper.db1;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.mboot.entity.User;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	List<User> findAll(Pagination page);

	User findByUsername(String username);

	void batchInsert(List<User> users);

	void batchUpdate(List<User> users);

	List<String> getExistByUsernameList(List<String> usernameList);
}
