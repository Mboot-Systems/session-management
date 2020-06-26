package com.mboot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.mboot.entity.User;
import com.mboot.exception.GlobalException;
import com.mboot.mapper.db1.UserMapper;
import com.mboot.result.CodeMsg;
import com.mboot.service.UserService;
import com.mboot.utils.RedisUtil;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Service
public class UserServiceImpl implements UserService {

	public static final String COOKIE_NAME_TOKEN = "token";

	/**
	 * token Expiry time, 2 days
	 */
	public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void insert(User user) {
		userMapper.insert(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertTestTransactional(User user) {
		userMapper.insert(user);

		System.out.println(1 / 0);
	}

	@Override
	public void update(User user) {
		userMapper.updateById(user);
	}

	@Override
	public void deleteById(Long userId) {
		userMapper.deleteById(userId);
	}

	@Override
	public Page<User> findAll(Page<User> page) {
		return page.setRecords(userMapper.findAll(page));
	}

	@Override
	public List<User> findAll() {
		return userMapper.selectList(null);
	}

	@Override
	public User findById(Long userId) {
		return userMapper.selectById(userId);
	}

	@Override
	public User findByUsername(String username) {
		return userMapper.findByUsername(username);
	}

	@Override
	public String login(HttpServletResponse response, String username, String password) {

		User user = findByUsername(username);
		if (user == null) {
			throw new GlobalException(CodeMsg.USERNAME_NOT_EXIST);
		}

		String dbPass = user.getPassword();
		if (!password.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}

		String token = UUID.randomUUID().toString().replace("-", "");
		addCookie(response, token, user);
		return token;
	}

	@Override
	public User getByToken(HttpServletResponse response, String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		User user = JSON.parseObject(redisUtil.get(COOKIE_NAME_TOKEN + "::" + token), User.class);
		if (user == null) {
			throw new GlobalException(CodeMsg.USER_NOT_LOGIN);
		}
		if (response != null) {
			addCookie(response, token, user);
		}
		return user;
	}

	@Override
	public User getByToken(String token) {
		return getByToken(null, token);
	}

	@Override
	public User getLoginUser(HttpServletRequest request) {
		return getLoginUser(request, null);
	}

	@Override
	public User getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(UserServiceImpl.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, UserServiceImpl.COOKIE_NAME_TOKEN);
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			// return null;
			throw new GlobalException(CodeMsg.USER_NOT_LOGIN);
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		if (response == null) {
			return getByToken(token);
		}
		return getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			// return null;
			throw new GlobalException(CodeMsg.TOKEN_INVALID);
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	private void addCookie(HttpServletResponse response, String token, User user) {
		// sho token's arrival redis
		redisUtil.set(COOKIE_NAME_TOKEN + "::" + token, JSON.toJSONString(user), TOKEN_EXPIRE);
		// General token copy cookie
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(TOKEN_EXPIRE);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchInsertOrUpdate(List<User> userList) {
		List<String> usernameList = userList.stream().map(p -> p.getUsername()).collect(Collectors.toList());
		List<String> existUsernameList = userMapper.getExistByUsernameList(usernameList);

		List<User> needInsertList = new ArrayList<>();
		List<User> needUpdateList = new ArrayList<>();
		for (User user : userList) {
			if (existUsernameList.contains(user.getUsername())) {
				needUpdateList.add(user);
			} else {
				needInsertList.add(user);
			}
		}

		userMapper.batchInsert(needInsertList);

		userMapper.batchUpdate(needUpdateList);
	}
}
