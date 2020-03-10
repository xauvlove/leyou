package com.leyou.item.vo;


import com.leyou.item.pojo.SpecParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class SpecGroupVO {
    private Long id;
    //商品分类
    private Long cid;
    //规格
    private String name;

    private List<SpecParam> paramList;
}
