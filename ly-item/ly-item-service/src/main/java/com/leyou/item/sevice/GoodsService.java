package com.leyou.item.sevice;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.LyCodeExceptionEnum;
import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyCodeException;
import com.leyou.common.exception.LyMarketException;
import com.leyou.common.utils.TransferTToKUtil;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.vo.SpuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private TransferTToKUtil<SpuVO, Spu> spuVOToSpuUtil;
    @Autowired
    private TransferTToKUtil<Spu, SpuVO> spuToSpuVOUtil;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;


    public PageResult<SpuVO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if(saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //默认排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        // Mapper 在查询的时候，返回的是 List 的实现类 Page
        List<Spu> spuList = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(spuList)) {
            throw new LyMarketException(LyMarketExceptionEnum.GOODS_NOTFOUND);
        }
        //不要解析 spuVOList 因为 Mapper 在查询的时候，返回的是 List 的实现类 Page
        PageInfo<Spu> info = new PageInfo<>(spuList);
        //转换 Spu 到 SpuVO
        List<SpuVO> spuVOList = new ArrayList<>(spuList.size());
        spuList.forEach(data -> {
            //spuVOList.add(SpuUtils.transferSpuToSpuVO(data));
            try {
                spuVOList.add(spuToSpuVOUtil.transferTToK(data, SpuVO.class));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("GoodService querySpuByPage");
                throw new LyCodeException(LyCodeExceptionEnum.USE_TRANSFERTTOK_NOARGCONSTRUCTOR_ERROR);
            }
        });
        loadCategoryAndBrandName(spuList, spuVOList);
        return new PageResult<>(info.getTotal(), spuVOList);
    }

    private void loadCategoryAndBrandName(List<Spu> spuList, List<SpuVO> spuVOList) {
        final int[] i = {0};
        spuList.forEach(data -> {
            //处理分类名
            List<String> names = categoryService.queryCategoryByCids(Arrays.asList(data.getCid1(),
                    data.getCid2(), data.getCid3())).stream().map(Category::getName).collect(Collectors.toList());
            //设置品牌名，用 “/” 隔开List的字段
            spuVOList.get(i[0]).setCname(StringUtils.join(names, "/"));
            //处理品牌名
            Brand brand = brandService.queryBrandById(data.getBrandId());
            spuVOList.get(i[0]).setBname(brand.getName());
            i[0] += 1;
        });
    }

    /**
     * 共4张表
     * //新增 spu
     * //新增 spuDetail
     * //新增sku
     * //新增库存
     * @param spuVO
     */
    @Transactional
    public void saveGoods(SpuVO spuVO) {
        //新增 spu
        Spu spu = null;
        try {
            spu = spuVOToSpuUtil.transferTToK(spuVO, Spu.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("GoodService querySpuByPage");
            throw new LyCodeException(LyCodeExceptionEnum.USE_TRANSFERTTOK_NOARGCONSTRUCTOR_ERROR);
        }
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if(count != 1) {
            log.error("GoodsService saveGoods");
            throw new LyMarketException(LyMarketExceptionEnum.GOODS_INSERT_FAILED_ERROR);
        }
        //新增 spuDetail
        SpuDetail spuDetail = spuVO.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(spuDetail);
        if(count != 1) {
            log.error("GoodsService saveGoods");
            throw new LyMarketException(LyMarketExceptionEnum.GOODS_INSERT_FAILED_ERROR);
        }

        List<Stock> stockList = new ArrayList<>();
        //新增sku
        List<Sku> skuList = spuVO.getSkus();
        for (Sku sku: skuList) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if(count != 1) {
                log.error("GoodsService saveGoods");
                throw new LyMarketException(LyMarketExceptionEnum.GOODS_INSERT_FAILED_ERROR);
            }
            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }
        count = stockMapper.insertList(stockList);
        if(count < stockList.size()) {
            log.error("GoodsService saveGoods");
            throw new LyMarketException(LyMarketExceptionEnum.GOODS_INSERT_FAILED_ERROR);
        }
        //发送mq消息  生成静态页
        amqpTemplate.convertAndSend("item.insert", spu.getId());
    }

    public SpuDetail queryDetailById(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if(spuDetail == null) {
            spuDetail = new SpuDetail();
            spuDetail.setSpuId(id);
            return spuDetail;
        }
        return spuDetail;
    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)) {
            throw new LyMarketException(LyMarketExceptionEnum.GOODS_NOTFOUND);
        }
        List<Long> stockIdList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(stockIdList);
        if(CollectionUtils.isEmpty(stockList)) {
            throw new LyMarketException(LyMarketExceptionEnum.STOCK_IS_EMPTY_ERROR);
        }
        Map<Long, Integer> skuIdAndStock = stockList.stream().
                collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(data -> {
            data.setStock(skuIdAndStock.get(data.getId()));

        });
        return skuList;
    }

    public SpuVO querySpuVOById(Long id) throws Exception {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null) {
            throw new LyMarketException(LyMarketExceptionEnum.GOODS_NOTFOUND);
        }
        List<Sku> skuList = querySkuBySpuId(id);
        SpuVO spuVO = spuToSpuVOUtil.transferTToK(spu, SpuVO.class);
        spuVO.setSkus(skuList);
        spuVO.setSpuDetail(queryDetailById(id));
        return spuVO;
    }
}