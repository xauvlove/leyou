package com.leyou.item.pojo;

import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spu_new_detail")
public class SpuDetail {
    @Id
    private Long spuId;
    //描述
    private String description;
    //商品特殊规格
    private String specialSpec;
    //商品全局规格属性
    private String genericSpec;
    //包装清单
    private String packingList;
    //售后服务
    private String afterService;
}
