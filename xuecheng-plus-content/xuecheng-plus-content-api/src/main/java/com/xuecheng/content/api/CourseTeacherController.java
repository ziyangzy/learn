package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description 课程老师编辑接口
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
@Api(value = "师资信息管理接口",tags = "师资信息管理接口")
@RestController
public class CourseTeacherController {
    @Autowired
    CourseTeacherService courseTeacherService;
    
    @ApiOperation("讲师信息查询")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseTeacher(@PathVariable Long courseId) {
        return courseTeacherService.getCourseTeacher(courseId);
    }
    
    @ApiOperation("添加/修改讲师")
    @PostMapping("/courseTeacher")
    public CourseTeacher setCourseTeacher(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.setCourseTeacher(courseTeacher);
    }
    
    @ApiOperation("删除讲师")
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long id) {
        courseTeacherService.deleteCourseTeacher(courseId, id);
    }
}
