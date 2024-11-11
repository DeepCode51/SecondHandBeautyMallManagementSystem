package com.xjr.mzmall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Category;
import com.xjr.mzmall.enums.ResultEnum;
import com.xjr.mzmall.mapper.CategoryMapper;
import com.xjr.mzmall.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getCategoryList(LayuiPage layuiPage, String categoryName) {
        Long curPage = layuiPage.getPage();
        Long pageLimit = layuiPage.getLimit();
        if (Objects.isNull(curPage)) {
            curPage = 1l;
        }
        if (Objects.isNull(pageLimit)) {
            pageLimit = 10l;
        }
        Page<Category> categoryPage = new Page<>(curPage, pageLimit);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(categoryName)) {
            queryWrapper.like("category_name", categoryName);
        }
        Page<Category> page = this.page(categoryPage, queryWrapper);
        return Result.success(ResultEnum.SUCCESS.getMessage(), page.getRecords(), page.getTotal());
    }

    @Override
    public Result deleteByIds(String ids) {
        List<Integer> list = CommonUtils.StringsToList(ids);
        this.removeByIds(list);
        return Result.success();
    }

    @Override
    public Result updateCategory(Category category) {
        this.updateById(category);
        return Result.success();
    }

    @Override
    public Result createCategory(Category category) {
        this.save(category);
        return Result.success();
    }

    @Override
    public List<Category> topFive() {
        return categoryMapper.topFive();
    }
}
