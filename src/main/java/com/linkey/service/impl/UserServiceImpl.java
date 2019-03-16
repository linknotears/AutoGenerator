package com.linkey.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.linkey.entity.User;
import com.linkey.mapper.UserMapper;
import com.linkey.service.UserService;
import org.springframework.stereotype.Service;
/**
 *  服务实现类
 *
 * @author linkey
 * @since 2019-03-12
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	private UserMapper userMapper;
	@Autowired
	public UserServiceImpl(UserMapper userMapper){
		this.baseMapper = userMapper;
		this.userMapper = userMapper;
	}
	
}
