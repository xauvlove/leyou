package com.leyou.item.utils;

import com.leyou.item.pojo.Spu;
import com.leyou.item.vo.SpuVO;

public class SpuUtils<T, K> {

    public static SpuVO transferSpuToSpuVO(Spu spu) {
        SpuVO spuVO = new SpuVO(spu.getId(), spu.getBrandId(), spu.getTitle(),
                spu.getSubTitle(), spu.getSaleable(), spu.getCreateTime());
        spuVO.setId(spu.getId());
        spuVO.setBrandId(spu.getBrandId());
        spuVO.setTitle(spu.getTitle());
        return spuVO;
    }
}

