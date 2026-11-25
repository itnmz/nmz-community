package com.itnmz.community.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.itnmz.community.mapper.CategoryMapper;
import com.itnmz.community.service.ICategoryService;
import com.itnmz.community.model.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
}
