package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
/**
 * @description 课程计划service接口实现类
 * @author Mr.M
 * @date 2022/9/9 11:14
 * @version 1.0
 */
 @Service
public class TeachplanServiceImpl implements TeachplanService {

  @Autowired
 TeachplanMapper teachplanMapper;
  @Autowired
  TeachplanMediaMapper teachplanMediaMapper;
 @Override
 public List<TeachplanDto> findTeachplanTree(Long courseId) {
  return teachplanMapper.selectTreeNodes(courseId);
 }
 
 @Transactional
 @Override
 public void saveTeachplan(SaveTeachplanDto teachplanDto) {
  
  //课程计划id
  Long id = teachplanDto.getId();
  //修改课程计划
  if(id!=null){
   Teachplan teachplan = teachplanMapper.selectById(id);
   BeanUtils.copyProperties(teachplanDto,teachplan);
   teachplanMapper.updateById(teachplan);
  }else{
   //取出同父同级别的课程计划数量
   int sort = getTeachplanSort(teachplanDto.getCourseId(), teachplanDto.getParentid());
   Teachplan teachplanNew = new Teachplan();
   //设置排序号
   teachplanNew.setOrderby(sort+1);
   BeanUtils.copyProperties(teachplanDto,teachplanNew);
   
   teachplanMapper.insert(teachplanNew);
   
  }
  
 }
 
 /**
  * @param id 课程计划id
  * @return void
  * @description 删除课程计划
  * @author Mr.M
  * @date 2022/9/9 13:39
  */
 @Override
 public void deleteTeachplan(Long id) {
  //看大章还是小章 大章判断其下没有小章才可删
 
  if (teachplanMapper.selectById(id).getParentid() == 0) {
   LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
   queryWrapper.eq(Teachplan::getParentid, id);
   int i = teachplanMapper.selectCount(queryWrapper);
   if (i > 0) {
    XueChengPlusException.cast(120409,"课程计划信息还有子级信息，无法操作");
   }
   teachplanMapper.deleteById(id);
   
  }else{
   //删小章时删媒资
   teachplanMapper.deleteById(id);
   LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
   queryWrapper.eq(TeachplanMedia::getTeachplanId, id);
   teachplanMediaMapper.delete(queryWrapper);
  }
 }
 
 
 @Override
 public void movedownTeachplan(Long teachplanid) {
  //获取parentid  在pid一样的里找大点的teachplan
  Teachplan teachplan1 = teachplanMapper.selectById(teachplanid);
  Long parentid = teachplanMapper.selectById(teachplanid).getParentid();
  Integer orderby = teachplanMapper.selectById(teachplanid).getOrderby();
  LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
  queryWrapper.eq(Teachplan::getParentid, parentid);
  queryWrapper.gt(Teachplan::getOrderby, orderby) // 查找 orderBy 大于给定参数的记录
          .orderByAsc(Teachplan::getOrderby); // 按 orderBy 字段升序排序
  queryWrapper.last("LIMIT 1"); // 限制只查询一条记录
  Teachplan teachplan =teachplanMapper.selectOne(queryWrapper);
  if(teachplan==null){
         return;
  }
  // 有则交换orderby值
  Integer i = teachplan.getOrderby();
  teachplan.setOrderby(teachplan1.getOrderby());
  teachplan1.setOrderby(i);
  teachplanMapper.updateById(teachplan);
  teachplanMapper.updateById(teachplan1);
  
 }
 
 @Override
 public void moveupTeachplan(Long teachplanid) {
  //获取parentid  在pid一样的里找小点的teachplan
  Teachplan teachplan1 = teachplanMapper.selectById(teachplanid);
  Long parentid = teachplanMapper.selectById(teachplanid).getParentid();
  Integer orderby = teachplanMapper.selectById(teachplanid).getOrderby();
  LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
  queryWrapper.eq(Teachplan::getParentid, parentid);
  queryWrapper.lt(Teachplan::getOrderby, orderby) // 查找 orderBy 小于给定参数的记录
          .orderByAsc(Teachplan::getOrderby); // 按 orderBy 字段升序排序
  queryWrapper.last("LIMIT 1"); // 限制只查询一条记录
  Teachplan teachplan =teachplanMapper.selectOne(queryWrapper);
  if(teachplan==null){
   return;
  }
  // 有则交换orderby值
  Integer i = teachplan.getOrderby();
  teachplan.setOrderby(teachplan1.getOrderby());
  teachplan1.setOrderby(i);
  teachplanMapper.updateById(teachplan);
  teachplanMapper.updateById(teachplan1);
 
 
 }
 
 /**
  * @description 获取最新的排序号
  * @param courseId  课程id
  * @param parentId  父课程计划id
  * @return int 最新排序号
  * @author Mr.M
  * @date 2022/9/9 13:43
  */
 private int getTeachplanSort(long courseId,long parentId){
  LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
  queryWrapper.eq(Teachplan::getCourseId,courseId);
  queryWrapper.eq(Teachplan::getParentid,parentId);
  List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
  // 找到最大的值
  Optional<Teachplan> maxTeachplan = teachplans.stream()
          .max(Comparator.comparing(Teachplan::getOrderby));
  int sort = 1;
  if (maxTeachplan.orElse(null) != null) {
   sort = maxTeachplan.orElse(null).getOrderby();
  }
  return sort;
 }
 
 @Transactional
 @Override
 public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
  //教学计划id
  Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
  Teachplan teachplan = teachplanMapper.selectById(teachplanId);
  if(teachplan==null){
   XueChengPlusException.cast("教学计划不存在");
  }
  Integer grade = teachplan.getGrade();
  if(grade!=2){
   XueChengPlusException.cast("只允许第二级教学计划绑定媒资文件");
  }
  //课程id
  Long courseId = teachplan.getCourseId();
  
  //先删除原来该教学计划绑定的媒资
  teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId,teachplanId));
  
  //再添加教学计划与媒资的绑定关系
  TeachplanMedia teachplanMedia = new TeachplanMedia();
  teachplanMedia.setCourseId(courseId);
  teachplanMedia.setTeachplanId(teachplanId);
  teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
  teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
  teachplanMedia.setCreateDate(LocalDateTime.now());
  teachplanMediaMapper.insert(teachplanMedia);
  return teachplanMedia;
 }
 
}