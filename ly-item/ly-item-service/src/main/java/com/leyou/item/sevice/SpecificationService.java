package com.leyou.item.sevice;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyMarketException;
import com.leyou.common.utils.TransferTToKUtil;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.vo.SpecGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Autowired
    private TransferTToKUtil<SpecGroup, SpecGroupVO> tToKUtil;

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

    public List<SpecGroupVO> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        List<SpecGroupVO> specGroupVOList = Lists.newArrayList();
        specGroups.forEach(data -> {
            try {
                specGroupVOList.add(tToKUtil.transferTToK(data, SpecGroupVO.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //查询组内参数
        List<SpecParam> specParamList = querySpecParam(null, cid, null);
        Map<Long, List<SpecParam>> paramMap = Maps.newHashMap();
        specParamList.forEach(data -> {
            if(!paramMap.containsKey(data.getGroupId())) {
                paramMap.put(data.getGroupId(), Lists.newArrayList());
            }
            paramMap.get(data.getGroupId()).add(data);
        });
        specGroupVOList.forEach(data -> {
            data.setParamList(paramMap.get(data.getId()));
        });

        return specGroupVOList;
    }
}
