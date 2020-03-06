package com.leyou.item.sevice;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyMarketException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
@Slf4j
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows,
                                              String sortedBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)) {
            //过滤条件
            // where 'name' like "%x" or letter == 'x' order by id desc
            example.createCriteria().orLike("name", "%"+key+"%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if(StringUtils.isNotBlank(sortedBy)) {
            String orderByClause = sortedBy + (desc?" DESC":" ASC");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(brands)) {
            throw new LyMarketException(LyMarketExceptionEnum.BRAND_NOT_FOUND);
         }
        //解析分页结果
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);
        return new PageResult<>(brandPageInfo.getTotal(), brands);
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);//新增brand后自动回显id
        if(count != 1) {
            throw new LyMarketException(LyMarketExceptionEnum.BRAND_SAVE_ERROR);
        }
        //中间表新增
        // insert into tb_category_brand values(cid, brand);
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count != 1) {
                throw new LyMarketException(LyMarketExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    public void deleteBrandById(Long id) {
        Brand brand = new Brand();
        brand.setId(id);
        int count = brandMapper.deleteByPrimaryKey(id);
        if(count != 1) {
            log.error("BrandService deleteBrandById");
            throw new LyMarketException(LyMarketExceptionEnum.BRAND_DELETE_ERROR);
        }
    }

    public Brand queryBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(brand == null) {
            log.error("BrandService queryBrandById");
            throw new LyMarketException(LyMarketExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * select b.* from tb_brand b inner join tb_category_brand cb on
     * b.id = cb.brand_id where c.category_id=cid;
     *
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {
        // select * from tb_brand where 
        /* if(CollectionUtils.isEmpty(brandList)) {
            log.info("BrandService queryBrandByCid");
            throw new LyException();
        }*/
        return brandMapper.queryBrandsByCid(cid);
    }

    public void updateBrand(Brand brand) {
        int count = brandMapper.updateByPrimaryKey(brand);
        if(count != 1) {
            log.error("updateBrand");
            throw new LyMarketException();
        }
    }
}
