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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long spuId) {

        Spu spu = goodsClient.querySpuById(spuId);
        List<Sku> skuList = goodsClient.querySkuBySpuId(spuId);
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        categoryClient.queryCategoryByIds(Arrays.asList(
                spu.getCid1(), spu.getCid2(), spu.getCid3()));

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", spu.getTitle());
        model.put("subTitle", spu.getSubTitle());
        model.put("skus", skuList);
        model.put("detail", spuDetail);
        model.put("brand", brand);
        try{
            List<SpecGroupVO> specGroupVOS = specificationClient.queryGroupByCid(spu.getCid3());
            model.put("specs", specGroupVOS);
        } catch (Exception e) {
            e.printStackTrace();
            model.put("specs", "specGroupVOS");
        }

        return model;
    }

    public void createHtml(Long spuId) {
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        String path = "D:\\apps\\java-develop\\workspace\\leyou-html";
        File dest = new File(path, spuId + ".html");
        if(dest.exists()) {
            dest.delete();
        }
        try(PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
            //生成html
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("生成静态页异常", e);
        }
    }

    public void deleteHtml(Long spuId) {
        String path = "D:\\apps\\java-develop\\workspace\\leyou-html";
        File dest = new File(path, spuId + ".html");
        if(dest.exists()) {
            dest.delete();
        }
    }
}
