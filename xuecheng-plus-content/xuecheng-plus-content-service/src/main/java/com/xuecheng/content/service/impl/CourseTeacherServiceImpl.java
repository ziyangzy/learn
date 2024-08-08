package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
 
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {
 
    @Autowired
    CourseTeacherMapper courseTeacherMapper;
    //查询讲师信息
    @Override
    public List<CourseTeacher> getCourseTeacher(Long courseId) {
        //根据课程id查询讲师信息
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseId);
        return courseTeacherMapper.selectList(queryWrapper);
    }
 
    //添加，修改讲师信息
    @Override
    public CourseTeacher setCourseTeacher(CourseTeacher courseTeacher) {
        //根据id判断是修改还是删除
        if(courseTeacher.getId()==null){
            //证明是添加
            courseTeacher.setCreateDate(LocalDateTime.now());
            courseTeacherMapper.insert(courseTeacher);
            return courseTeacherMapper.selectById(courseTeacher);
        }else{
            //证明是修改
            courseTeacherMapper.updateById(courseTeacher);
            return courseTeacherMapper.selectById(courseTeacher);
        }
    }
 
    //删除讲师信息
    @Override
    public void deleteCourseTeacher(Long courseId, Long id) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseId).eq(CourseTeacher::getId,id);
        CourseTeacher courseTeacher = courseTeacherMapper.selectOne(queryWrapper);
        if(courseTeacher==null){
            XueChengPlusException.cast("未知错误，请稍后重试");
        }
        courseTeacherMapper.deleteById(courseTeacher);
    }
    
}