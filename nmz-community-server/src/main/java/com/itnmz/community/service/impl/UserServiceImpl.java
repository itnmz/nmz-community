package com.itnmz.community.service.impl;


import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.itnmz.community.model.entity.User;
import com.itnmz.community.mapper.UserMapper;
import com.itnmz.community.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
