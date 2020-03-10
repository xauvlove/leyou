package com.leyou.page.service;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.vo.SpecGroupVO;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadModel(Long spuId) {

        Spu spu = goodsClient.querySpuById(spuId);
        List<Sku> skuList = goodsClient.querySkuBySpuId(spuId);
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        categoryClient.queryCategoryByIds(Arrays.asList(
                spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<SpecGroupVO> specGroupVOS = specificationClient.queryGroupByCid(spu.getCid3());

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", spu.getTitle());
        model.put("subTitle", spu.getSubTitle());
        model.put("skus", skuList);
        model.put("detail", spuDetail);
        model.put("brand", brand);
        model.put("specs", specGroupVOS);

        return model;
    }
}
