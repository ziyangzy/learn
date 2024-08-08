package com.xuecheng.content.api;

import com.alibaba.fastjson.JSON;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @description 课程预览，发布
 * @author Mr.M
 * @date 2022/9/16 14:48
 * @version 1.0
 */
 @Controller
public class CoursePublishController {

@Autowired
CoursePublishService coursePublishService;
    @ApiOperation("获取课程发布信息")
    @ResponseBody
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
        //封装数据 
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        
        //查询课程发布表
        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
//        //先从缓存查询，缓存中有直接返回，没有再查询数据库
//        CoursePublish coursePublish = coursePublishService.getCoursePublishCache(courseId);
//        if(coursePublish == null){
//            return coursePreviewDto;
//        }
        
//        //开始向coursePreviewDto填充数据
//        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
//        BeanUtils.copyProperties(coursePublish,courseBaseInfoDto);
//        //课程计划信息
//        String teachplanJson = coursePublish.getTeachplan();
//        //转成List<TeachplanDto>
//        List<TeachplanDto> teachplanDtos = JSON.parseArray(teachplanJson, TeachplanDto.class);
//        coursePreviewDto.setCourseBase(courseBaseInfoDto);
//        coursePreviewDto.setTeachplans(teachplanDtos);
        return coursePublish;
        
    }

 @GetMapping("/coursepreview/{courseId}")
 public ModelAndView preview(@PathVariable("courseId") Long courseId){

      ModelAndView modelAndView = new ModelAndView();
     CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
     modelAndView.addObject("model",coursePreviewInfo);
      modelAndView.setViewName("course_template");
      return modelAndView;
  }
    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId){
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId,courseId);
    }
    @ApiOperation("课程发布")
    @ResponseBody
    @PostMapping ("/coursepublish/{courseId}")
    public void coursepublish(@PathVariable("courseId") Long courseId){
        Long companyId = 1232141425L;
        coursePublishService.publish(companyId,courseId);
    }
    
    @ApiOperation("获取课程发布信息")
    @ResponseBody
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getCoursePublishToPreview(@PathVariable("courseId") Long courseId) {
        //查询课程发布信息
        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
        if (coursePublish == null) {
            return new CoursePreviewDto();
        }
        
        //课程基本信息
        CourseBaseInfoDto courseBase = new CourseBaseInfoDto();
        BeanUtils.copyProperties(coursePublish, courseBase);
        //课程计划
        List<TeachplanDto> teachplans = JSON.parseArray(coursePublish.getTeachplan(), TeachplanDto.class);
        //课程发布表取出营销信息 
        Map map = JSON.parseObject(coursePublish.getMarket(), Map.class);
        String qq = (String)(map.get("qq"));
        String wechat = (String)(map.get("wechat"));
        String phone = (String)(map.get("phone"));
        courseBase.setQq(qq);
        courseBase.setWechat(wechat);
        courseBase.setPhone(phone);
        
        CoursePreviewDto coursePreviewInfo = new CoursePreviewDto();
        coursePreviewInfo.setCourseBase(courseBase);
        coursePreviewInfo.setTeachplans(teachplans);
        return coursePreviewInfo;
    }
}