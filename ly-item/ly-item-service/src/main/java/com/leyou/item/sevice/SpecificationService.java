package com.leyou.item.sevice;

import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyMarketException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(list)) {
            throw new LyMarketException(LyMarketExceptionEnum.SPEC_GROUP_NOTFOUND);
        }
        return list;
    }

    /*public List<SpecParam> querySpecParamByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOTFOUND);
        }
        return list;
    }*/

    /*public List<SpecParam> querySpecParamByCid(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOTFOUND);
        }
        return list;
    }*/

    public List<SpecParam> querySpecParam(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)) {
            throw new LyMarketException(LyMarketExceptionEnum.SPEC_PARAM_NOTFOUND);
        }
        return list;
    }

    public void saveSpecParam(SpecParam specParam) {
        int count = specParamMapper.insert(specParam);
        if(count != 1) {
            throw new LyMarketException(LyMarketExceptionEnum.SPEC_PARAM_INSERT_ERROR);
        }
    }
}
