package com.mboot.mapper.db2;

import com.mboot.entity.User;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
public interface Db2UserMapper {

	User findById(Long id);

	Integer insert(User user);
}
