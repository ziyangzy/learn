package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description 课程计划编辑接口
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
 @Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
 @RestController
public class TeachplanController {
    
    @Autowired
    TeachplanService teachplanService;
    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        return teachplanTree;
    }
    
    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan( @RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }
    @ApiOperation("课程计划删除")
    @ApiImplicitParam(value = "id",name = "课程计划Id",required = true,dataType = "Long",paramType = "path")
    @DeleteMapping("/teachplan/{teachplanid}")
    public void deleteTeachplan( @PathVariable Long teachplanid){
        
        teachplanService.deleteTeachplan(teachplanid);
    }
    @ApiOperation("课程计划排序")
    @ApiImplicitParam(value = "id",name = "课程计划Id",required = true,dataType = "Long",paramType = "path")
    @PostMapping("/teachplan/movedown/{teachplanid}")
    public void movedownTeachplan( @PathVariable Long teachplanid){
        
        teachplanService.movedownTeachplan(teachplanid);
    }
    @ApiOperation("课程计划排序")
    @ApiImplicitParam(value = "id",name = "课程计划Id",required = true,dataType = "Long",paramType = "path")
    @PostMapping("/teachplan/moveup/{teachplanid}")
    public void moveupTeachplan( @PathVariable Long teachplanid){
        
        teachplanService.moveupTeachplan(teachplanid);
    }
    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }
    
}