package com.leyou.item.sevice;

import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyMarketException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryByPid(Long pid) {
        //通用mapper会将对象中的非空字段作为查询条件
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categoryList = categoryMapper.select(category);
        if(CollectionUtils.isEmpty(categoryList)) {
            throw new LyMarketException(LyMarketExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }

    public List<Category> queryCategoryByCids(List<Long> cids) {
        List<Category> categoryList = categoryMapper.selectByIdList(cids);
        if(CollectionUtils.isEmpty(categoryList)) {
            throw new LyMarketException(LyMarketExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }
}
