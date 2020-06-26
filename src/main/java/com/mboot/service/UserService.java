package com.mboot.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.plugins.Page;
import com.mboot.entity.User;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
public interface UserService {

	void insert(User user);

	void insertTestTransactional(User user);

	void update(User user);

	void deleteById(Long userId);

	Page<User> findAll(Page<User> page);

	List<User> findAll();

	User findById(Long userId);

	User findByUsername(String username);

	String login(HttpServletResponse response, String username, String password);

	User getByToken(HttpServletResponse response, String token);

	User getByToken(String token);

	User getLoginUser(HttpServletRequest request);

	User getLoginUser(HttpServletRequest request, HttpServletResponse response);

	void batchInsertOrUpdate(List<User> userList);

}
