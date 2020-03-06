package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LyMarketExceptionEnum {
    PRICE_CANNOT_BE_NULL(404, "价格不能为空"),
    CATEGORY_NOT_FOUND(404, "商品分类未查到"),
    BRAND_NOT_FOUND(404, "品牌未找到"),
    BRAND_SAVE_ERROR(500, "新增品牌失败"),
    UPLOAD_IMAGE_ERROR(500, "logo上传失败"),
    INVALID_IMAGE_TYPE(400, "无效的图片类型"),
    INVALID_IMAGE_CONTENT(400, "无效的图片"),
    BRAND_DELETE_ERROR(400, "删除失败"),
    SPEC_GROUP_NOTFOUND(500, "查询规格组失败"),
    SPEC_PARAM_NOTFOUND(500, "查询规格参数失败"),
    SPEC_PARAM_INSERT_ERROR(500, "规格参数创建失败"),
    GOODS_NOTFOUND(500, "查询商品失败"),
    GOODS_INSERT_FAILED_ERROR(500, "新增商品失败"),
    DETAIL_QUERY_FAILED_ERROR(500, "查询商品详情失败"),
    STOCK_IS_EMPTY_ERROR(500, "商品库存不足"),
    ;
    private int code;
    private String msg;

}
