package com.itnmz.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itnmz.community.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
