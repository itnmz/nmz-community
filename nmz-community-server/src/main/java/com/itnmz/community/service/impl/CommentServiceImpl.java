package com.itnmz.community.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.itnmz.community.mapper.CommentMapper;
import com.itnmz.community.service.ICommentService;
import com.itnmz.community.model.entity.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
}
