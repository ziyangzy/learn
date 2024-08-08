package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author
 * @version:1.0
 * @description
 * @create 2024-07-13
 */
@SpringBootTest
public class TeachplanMapperTests {
    
    @Autowired
    TeachplanMapper teachplanMapper;
    @Test
    public void testTeachplanMapper() {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(117L);
        System.out.println(teachplanDtos);
    
    }
}
