package com.leyou.item.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "tb_sku")
@NoArgsConstructor
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    //商品特殊规格的键值对
    private String ownSpec;
    //商品特殊规格的下标
    private String indexes;
    //是否有效  如果商品删除了 那么就是不可用
    private Boolean enable;
    private Date createTime;
    private Date lastUpdateTime;
    //库存
    @Transient
    private Integer stock;
}
