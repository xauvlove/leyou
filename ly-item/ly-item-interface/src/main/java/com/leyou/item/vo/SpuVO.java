package com.leyou.item.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class SpuVO {
    private Long id;
    private Long brandId;
    //@JsonIgnore
    private Long cid1;
    //@JsonIgnore
    private Long cid2;
    //@JsonIgnore
    private Long cid3;
    private String title;
    private String subTitle;
    private Boolean saleable;
    @JsonIgnore
    private Boolean valid;
    private Date createTime;
    @JsonIgnore
    private Date lastUpdateTime;

    //品牌名
    private String bname;
    //分类名
    private String cname;

    private List<Sku> skus;
    private SpuDetail spuDetail;

    public SpuVO(){}

    public SpuVO(Long id, Long brandId, String title, String subTitle, Boolean saleable,
                 Date createTime) {
        this.id = id;
        this.brandId= brandId;
        this.title = title;
        this.subTitle = subTitle;
        this.saleable = saleable;
        this.createTime = createTime;
    }

    public SpuVO(Long id, Long brandId, String title, String subTitle, Boolean saleable,
                 Date createTime, String bname, String name) {
        this.id = id;
        this.brandId= brandId;
        this.title = title;
        this.subTitle = subTitle;
        this.saleable = saleable;
        this.createTime = createTime;
        this.bname = bname;
        this.cname = cname;
    }

    public SpuVO(Long id, Long brandId, Long cid1, Long cid2, Long cid3,
                 String title, String subTitle, Boolean saleable, Date createTime, String bname, String cname) {
        this.id = id;
        this.brandId = brandId;
        this.cid1 = cid1;
        this.cid2 = cid2;
        this.cid3 = cid3;
        this.title = title;
        this.subTitle = subTitle;
        this.saleable = saleable;
        this.createTime = createTime;
        this.cname = cname;
        this.bname = bname;
    }
}
