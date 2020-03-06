package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.LyCodeExceptionEnum;
import com.leyou.common.exception.LyCodeException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.TransferTToKUtil;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.vo.GoodsVO;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private TransferTToKUtil<Goods, GoodsVO> tToKUtil;
    @Autowired
    private GoodsRepository goodsRepository;

    public GoodsVO buildGoodVO(Spu spu) {
        Goods goods = buildGoods(spu);
        GoodsVO goodsVO = null;
        try {
            goodsVO = tToKUtil.transferTToK(goods, GoodsVO.class);
        } catch (Exception e) {
            throw new LyCodeException(LyCodeExceptionEnum.USE_TRANSFERTTOK_NOARGCONSTRUCTOR_ERROR);
        }
        return goodsVO;
    }

    public Goods buildGoods(Spu spu) {

        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(
                spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if(CollectionUtils.isEmpty(categories)) {
            categories.add(new Category());
        }
        List<String> catNameList = categories.stream().map(
                Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if(brand == null) {
            brand = new Brand();
            brand.setName("");
        }
        String all = spu.getTitle() + StringUtils.join(catNameList, " ") + brand.getName();

        //查询sku
        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());
        List<Map<String, Object>> skus = new ArrayList<>();
        skuList.forEach(sku -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("price", sku.getPrice());
            map.put("title", sku.getTitle());
            //图片以 ',' 隔开
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skus.add(map);
        });
        //查询价格
        Set<Long> priceSet = skuList.stream().map(Sku::getPrice).collect(Collectors.toSet());
        //查询规格参数
        List<SpecParam> specParams = specificationClient.querySpecParamList(
                null, spu.getCid3(), true);
        SpuDetail spuDetail = goodsClient.queryDetailById(spu.getId());
        Map<Long, String> genericSpec = JsonUtils.parseMap(
                spuDetail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(
                spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});
        // key:规格参数名字  value:规格参数值
        Map<String, Object> specMap = new HashMap<>();
        specParams.forEach(data -> {
            String key = data.getName();
            assert genericSpec != null;
            assert specialSpec != null;
            Object value = "";
            if(data.getGeneric()) {
                value = genericSpec.get(data.getId());
                if(data.getNumeric()) {
                    value = chooseSegment(value.toString(), data);
                }
            } else {
                value = specialSpec.get(data.getId());
            }
            specMap.put(key, value);
        });

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setPrice(priceSet);
        goods.setSkus(JsonUtils.serialize(skus));
        goods.setSpecs(specMap);
        goods.setAll(all);
        return goods;
    }



    private String chooseSegment(String value, SpecParam p) {
        double val = Double.parseDouble(value);
        String result = "其它";
        //保存数值段
        String pSegments = p.getSegments();
        if(pSegments == null) return "";
        for(String segment: pSegments.split(",")) {
            String[] segs = segment.split("-");
            //获取数值范围
            double begin = Double.parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2) {
                end = Double.parseDouble(segs[1]);
            }
            if(val >= begin && val < end) {
                if(segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if(begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public String buildEasyGoodsForSearch(Spu spu) throws FileNotFoundException {

        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if(brand == null) {
            brand.setName("未知品牌");
        }
        if(StringUtils.isEmpty(spu.getTitle())) {
            spu.setTitle("未知标题");
        }
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if(CollectionUtils.isEmpty(categories)) {
            Category category = new Category();
            category.setName("未知分类");
            categories.add(category);
        }
        String all = spu.getTitle() + brand.getName() + StringUtils.join(categories, " ").replaceAll("\"", "");

        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());
        List<Map<String, Object>> skus = new ArrayList<>();
        skuList.forEach(sku -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("price", sku.getPrice());
            map.put("title", sku.getTitle());
            //图片以 ',' 隔开
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skus.add(map);
        });

        String skuStr = JsonUtils.serialize(skus).replaceAll("\"", "");
        String subTitle = spu.getSubTitle().replaceAll("\"", "");

        Goods goods = new Goods();

        goods.setAll(all);
        goods.setSubTitle(subTitle);
        goods.setSkus(skuStr);


        String str = "{\r\n\"all\":\"" + all+"\",\r\n"
                +"\"subTitle\":\""+subTitle+"\",\r\n"
                +"\"skus\":\""+skuStr+"\"\r\n}";
        FileOutputStream fileOutputStream = new FileOutputStream("d:/1.txt", true);
        PrintWriter pw = new PrintWriter(fileOutputStream);
        pw.println(str);
        pw.flush();
        pw.close();
        return str;
    }

    public PageResult<Goods> search(SearchRequest searchRequest) {
        int page = searchRequest.getPage() - 1;
        int size = searchRequest.getSize();
        //创建
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        //分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        //过滤
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        //查询
        Page<Goods> result = goodsRepository.search(queryBuilder.build());
        //解析
        long total = result.getTotalElements();
        int pages = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        return new PageResult<>(total, pages, goodsList);
    }
}
