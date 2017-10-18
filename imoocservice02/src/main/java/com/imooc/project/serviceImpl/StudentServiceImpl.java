package com.imooc.project.serviceImpl;


import com.imooc.project.entity.Student;
import com.imooc.project.mapper.StudentMapper;
import com.imooc.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/9/12.
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Student getStudentByID(Integer id) {
        return studentMapper.getStudentByID(id);
    }
}
