
package com.xuecheng.learning.service;

import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import org.springframework.stereotype.Service;

/**
 * @author Mr.M
 * @version 1.0
 * @description 我的课程表service接口
 * @date 2022/10/2 16:07
 */
public interface MyCourseTablesService {
    
    /**
     * @param userId   用户id
     * @param courseId 课程id
     * @return com.xuecheng.learning.model.dto.XcChooseCourseDto
     * @description 添加选课
     * @author Mr.M
     * @date 2022/10/24 17:33
     */
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId);
    
    /**
     * @param userId
     * @param courseId
     * @return XcCourseTablesDto 学习资格状态 [{"code":"702001","desc":"正常学习"},{"code":"702002","desc":"没有选课或选课后没有支付"},{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
     * @description 判断学习资格
     * @author Mr.M
     * @date 2022/10/3 7:37
     */
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId);
    
    boolean saveChooseCourseStauts(String choosecourseId);   
    /**
     * @description 我的课程表
     * @param params
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.learning.model.po.XcCourseTables>
     * @author Mr.M
     * @date 2022/10/27 9:24
     */
    
    
    public PageResult<XcCourseTables> mycourestabls(MyCourseTableParams params);
    
}