package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;
 
public interface CourseTeacherService {
    /**
     * 查询教师信息
     * @param courseId 课程id
     * @return 教师信息
     */
    List<CourseTeacher> getCourseTeacher(Long courseId);
 
    /**
     * 添加教师信息
     * @param courseTeacher 要添加的信息
     * @return 返回添加的信息
     */
    CourseTeacher setCourseTeacher(CourseTeacher courseTeacher);
 
    //删除教师信息
    void deleteCourseTeacher(Long courseId, Long id);
}