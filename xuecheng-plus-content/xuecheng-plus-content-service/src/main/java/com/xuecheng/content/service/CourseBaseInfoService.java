package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @description 课程基本信息管理业务接口
 * @author Mr.M
 * @date 2022/9/6 21:42
 * @version 1.0
*/
public interface CourseBaseInfoService  {

/*
 * @description 课程查询接口
 * @param pageParams 分页参数
 * @param queryCourseParamsDto 条件条件
 * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
 * @author Mr.M
 * @date 2022/9/6 21:44
 */
  PageResult<CourseBase> queryCourseBaseList(Long companyId,PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
    
  CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);
  
 
    /*
     * @description 根据课程id查询课程基本信息，包括基本信息和营销信息
     * @param courseId 课程id
     * @author z
     * @return 课程详细信息
     * @date 2022/9/6 21:44
     */
    
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);
  
  /**
   * @description 修改课程信息
   * @param companyId  机构id
   * @param dto  课程信息
   * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
   * @author Mr.M
   * @date 2022/9/8 21:04
   */
  public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);
  
  void deleteCourseBase(Long companyId, Long courseId);
}