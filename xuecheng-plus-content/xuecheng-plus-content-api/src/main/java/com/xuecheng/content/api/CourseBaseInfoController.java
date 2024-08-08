package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.utils.StringUtil;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 课程信息编辑接口
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
@Api(value = "课程信息编辑接口",tags = "课程信息编辑接口")
@RestController
public class CourseBaseInfoController {
    
    @Autowired
    CourseBaseInfoService courseBaseInfoService;
    
 @ApiOperation("课程查询接口")
 @PostMapping("/course/list")
 @PreAuthorize("hasAuthority('course_find_list')")
  public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParams){
    
//     CourseBase courseBase = new CourseBase();
//     courseBase.setName("测试名称");
//     courseBase.setCreateDate(LocalDateTime.now());
//     List<CourseBase> courseBases = new ArrayList();
//     courseBases.add(courseBase);
//     PageResult pageResult = new PageResult<CourseBase>(courseBases,10,1,10);
//     return pageResult; 
     SecurityUtil.XcUser user = SecurityUtil.getUser();
     Long companyId = null;
     if (StringUtil.isNotEmpty(user.getCompanyId())){
         companyId = Long.valueOf(user.getCompanyId());
     }
     
     PageResult<CourseBase> pageResults =courseBaseInfoService.queryCourseBaseList(companyId,pageParams,queryCourseParams);
     return pageResults;
  }
    @ApiOperation("新增课程基础信息")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto){
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        return courseBaseInfoService.createCourseBase(companyId,addCourseDto);
    }
    
    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable("courseId") Long courseId){
        //取出当前用户身份
//    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        System.out.println(user);
        
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }    
    @ApiOperation("修改课程基础信息")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated EditCourseDto editCourseDto){
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.updateCourseBase(companyId, editCourseDto);
        return courseBaseInfoDto;
    }
    
    @ApiOperation("删除课程")
    @DeleteMapping("/course/{courseId}")
    public void deleteCourseBase(@PathVariable Long courseId){
        //获取到用户所属机构id，这里就先模拟一个，可以自行更改
        Long companyId = 22L;
        courseBaseInfoService.deleteCourseBase(companyId,courseId);
    }
    
}