package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
public interface CategoryService extends IService<Category> {
    Result getCategoryList(LayuiPage layuiPage, String categoryName);
    Result deleteByIds(String ids);
    Result updateCategory(Category category);
    Result createCategory(Category category);
    List<Category> topFive();
}
