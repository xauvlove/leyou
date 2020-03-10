package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.sevice.SpecificationService;
import com.leyou.item.vo.SpecGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }

    /**
     * 根据gid查询规格参数，gid是 分组id，例如：主体、操作系统
     * @param gid
     * @return

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParamByGid(@RequestParam("gid") Long gid) {
        return ResponseEntity.ok(specificationService.querySpecParamByGid(gid));
    }*/

    /**
     * 查询规格参数的集合
     * @param gid 根据gid查询规格参数，gid是 分组id，例如：主体、操作系统
     * @param cid 根据cid查询规格参数，cid是分类id，例如手机，对讲机
     * @return

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParamList(
            @RequestParam(value = "gid", required = false) Long gid, @RequestParam(value = "cid", required = false) Long cid) {
        if(gid != null && cid == null) {
            return ResponseEntity.ok(specificationService.querySpecParamByGid(gid));
        } else if(gid == null && cid != null){
            return ResponseEntity.ok(specificationService.querySpecParamByCid(cid));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }*/

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamList(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = true) Boolean searching) {
            return ResponseEntity.ok(specificationService.querySpecParam(gid, cid, searching));
    }

    @PostMapping("/param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam specParam) {
        specificationService.saveSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据分类查组内参数
     * @param cid
     * @return
     */
    @GetMapping("/spec/group")
    public ResponseEntity<List<SpecGroupVO>> queryListByCid(@RequestParam("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }
}
