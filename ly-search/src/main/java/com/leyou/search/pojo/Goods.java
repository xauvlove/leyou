package com.leyou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {

    /**
     * 加了 Field 的按照指定的
     * 不加 es 会进行推测
     */

    @Id
    private Long id;
    //所有需要被搜索的信息，标题、分类、品牌等
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;
    //副标题  不需要被检测搜索  因此不需要分词处理
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    private Long brandId;
    // cid 用于做搜索过滤
    private Long cid1;
    private Long cid2;
    private Long cid3;
    // createTime 用于搜素过滤，例如  新品
    private Date createTime;
    //用于搜素过滤
    private Set<Long> price;
    //存储sku的json  用于解析sku
    @Field(type = FieldType.Keyword, index = false)
    private String skus;
    //规格参数， key是参数名
    private Map<String, Object> specs;
}
