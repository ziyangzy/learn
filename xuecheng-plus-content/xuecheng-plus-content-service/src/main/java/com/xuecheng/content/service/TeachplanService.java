package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.*;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @description 课程基本信息管理业务接口
 * @author Mr.M
 * @date 2022/9/6 21:42
 * @version 1.0
 */
public interface TeachplanService {

/**
 * @description 查询课程计划树型结构
 * @param courseId  课程id
 * @return List<TeachplanDto>
 * @author Mr.M
 * @date 2022/9/9 11:13
*/
 public List<TeachplanDto> findTeachplanTree(Long courseId);
 
 /**
  * @description 新增保存修改课程计划
  * @param teachplanDto  课程计划信息
  * @return void
  * @author Mr.M
  * @date 2022/9/9 13:39
  */
 public void saveTeachplan(SaveTeachplanDto teachplanDto);
 /**
  * @description 删除课程计划
  * @param id  课程计划id
  * @return void
  * @author Mr.M
  * @date 2022/9/9 13:39
  */
 void deleteTeachplan(Long id);
 
 void movedownTeachplan(Long teachplanid);
 
 void moveupTeachplan(Long teachplanid);
 
 /**
  * @description 教学计划绑定媒资
  * @param bindTeachplanMediaDto
  * @return com.xuecheng.content.model.po.TeachplanMedia
  * @author Mr.M
  * @date 2022/9/14 22:20
  */
 public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);
}