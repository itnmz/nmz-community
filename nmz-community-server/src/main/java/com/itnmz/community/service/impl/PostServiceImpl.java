package com.itnmz.community.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.itnmz.community.model.entity.Post;
import com.itnmz.community.mapper.PostMapper;
import com.itnmz.community.service.IPostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
}
