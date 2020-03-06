package com.leyou.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 *
 * @param <T> 插入到数据库的对象
 * @param * Long 这是ID，一般我们的ID都是Long类型
 * @RegisterMapper 是启用注解
 */
@RegisterMapper
public interface BaseMapper<T> extends
        Mapper<T>, IdListMapper<T, Long>, InsertListMapper<T> {
}
