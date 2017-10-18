package com.imooc.project.mapper;

import com.imooc.project.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator on 2017/10/18.
 */
@Mapper
public interface StudentMapper {
    Student getStudentByID(Integer id);

}
