package com.mboot.mapper.db1;

import com.mboot.entity.User;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
public interface Db1UserMapper {

	User findById(Long id);

	Integer insert(User user);
}
