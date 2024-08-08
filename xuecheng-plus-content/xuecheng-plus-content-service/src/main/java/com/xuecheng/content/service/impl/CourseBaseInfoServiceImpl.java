package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description 课程信息管理业务接口实现类
 * @author Mr.M
 * @date 2022/9/6 21:45
 * @version 1.0
 */
@Service
public class CourseBaseInfoServiceImpl  implements CourseBaseInfoService {
    
    
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;
    
    @Override
    public PageResult<CourseBase> queryCourseBaseList(Long companyId,PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        
        
        //构建查询条件对象
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //构建查询条件，根据课程名称查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        //构建查询条件，根据课程审核状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
//构建查询条件，根据课程发布状态查询
//todo:根据课程发布状态查询 
        queryWrapper.eq(CourseBase::getCompanyId,companyId);
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());
        //分页对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<CourseBase> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return courseBasePageResult;
        
        
    }
    
    /**
     * @description 添加课程基本信息
     * @param companyId  教学机构id
     * @param dto  课程基本信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @author Mr.M
     * @date 2022/9/7 17:51
     */
    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto dto) {
    
//        //合法性校验
//        if (StringUtils.isBlank(dto.getName())) {
//            XueChengPlusException.cast("课程名称为空");
//        }
//    
//        if (StringUtils.isBlank(dto.getMt())) {
//            XueChengPlusException.cast("课程分类为空");
//        }
//    
//        if (StringUtils.isBlank(dto.getSt())) {
//            XueChengPlusException.cast("课程分类为空");
//        }
//    
//        if (StringUtils.isBlank(dto.getGrade())) {
//            XueChengPlusException.cast("课程等级为空");
//        }
//    
//        if (StringUtils.isBlank(dto.getTeachmode())) {
//            XueChengPlusException.cast("教育模式为空");
//        }
//    
//        if (StringUtils.isBlank(dto.getUsers())) {
//            XueChengPlusException.cast("适应人群为空");
//        }
//    
//        if (StringUtils.isBlank(dto.getCharge())) {
//            XueChengPlusException.cast("收费规则为空");
//        }
        //新增对象
        CourseBase courseBaseNew = new CourseBase();
        //将填写的课程信息赋值给新增对象
        BeanUtils.copyProperties(dto,courseBaseNew);
        //设置审核状态
        courseBaseNew.setAuditStatus("202002");
        //设置发布状态
        courseBaseNew.setStatus("203001");
        //机构id
        courseBaseNew.setCompanyId(companyId);
        //添加时间
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBaseNew);
        if(insert<=0){
            XueChengPlusException.cast("新增课程基本信息失败");
        }
//todo:向课程营销表保存课程营销信息
//todo:查询课程基本信息及营销信息并返回
        CourseMarket courseMarketNew = new CourseMarket();
        Long courseId = courseBaseNew.getId();
        BeanUtils.copyProperties(dto,courseMarketNew);
        courseMarketNew.setId(courseId);
        int i = saveCourseMarket(courseMarketNew);
        if(i<=0){
            XueChengPlusException.cast("保存课程营销信息失败");
        }
        //查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);
    }
    
    //保存课程营销信息
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //收费规则
        String charge = courseMarketNew.getCharge();
        if(StringUtils.isBlank(charge)){
            XueChengPlusException.cast("收费规则没有选择");
        }
        //收费规则为收费
        if(charge.equals("201001")){
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue()<=0){
                XueChengPlusException.cast("课程为收费价格不能为空且必须大于0");
            }
        }
        //根据id从课程营销表查询
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseMarketNew.getId());
        if(courseMarketObj == null){
            return courseMarketMapper.insert(courseMarketNew);
        }else{
            BeanUtils.copyProperties(courseMarketNew,courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            return courseMarketMapper.updateById(courseMarketObj);
        }
    }
    //根据课程id查询课程基本信息，包括基本信息和营销信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        
        
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        
        //查询分类名称
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());
        
        return courseBaseInfoDto;
        
    }
    
    /**
     * @description 修改课程信息
     * @param companyId  机构id
     * @param dto  课程信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @author Mr.M
     * @date 2022/9/8 21:04
     */
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
    
        //课程id
        Long courseId = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase==null){
            XueChengPlusException.cast("课程不存在");
        }
    
        //校验本机构只能修改本机构的课程
        if(!courseBase.getCompanyId().equals(companyId)){
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }
    
        //封装基本信息的数据
        BeanUtils.copyProperties(dto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());
    
        //更新课程基本信息
        int i = courseBaseMapper.updateById(courseBase);
    
        //封装营销信息的数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarket);
        saveCourseMarket(courseMarket);
        //查询课程信息
        CourseBaseInfoDto courseBaseInfo = this.getCourseBaseInfo(courseId);
        return courseBaseInfo;
    
    }
    
    @Autowired
    CourseTeacherMapper courseteacherMapper;
    public void deleteCourseBase(Long companyId, Long courseId) {
        //根据id查询课程
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        //判断课程是否是该机构的
        if (!courseBase.getCompanyId().equals(companyId)) {
            XueChengPlusException.cast("本机构只能删除本机构课程");
        }
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        CourseTeacher courseTeacher = courseteacherMapper.selectOne(queryWrapper);
        if (courseTeacher != null) {
            //删除讲师信息
            courseteacherMapper.deleteById(courseTeacher);
        }
        LambdaQueryWrapper<CourseMarket> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(CourseMarket::getId, courseId);
        CourseMarket courseMarket = courseMarketMapper.selectOne(Wrapper);
        if (courseMarket != null) {
            //删除收费信息
            courseMarketMapper.deleteById(courseMarket);
        }
        LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachplanMedia::getCourseId, courseId);
        TeachplanMedia teachplanMedia = teachplanMediaMapper.selectOne(wrapper);
        if (teachplanMedia != null) {
            //删除媒资信息
            courseMarketMapper.deleteById(courseTeacher);
        }
        LambdaQueryWrapper<Teachplan> qwrapper = new LambdaQueryWrapper<>();
        qwrapper.eq(Teachplan::getCourseId, courseId);
        List<Teachplan> teachplan = teachplanMapper.selectList(qwrapper);
        if (teachplan != null) {
            for (Teachplan t:
                    teachplan ) {
                //删除课程计划
                teachplanMapper.deleteById(t);
            }
        }
        //删除课程信息
        courseBaseMapper.deleteById(courseBase);
        
    }
}