package com.imooc.project.controller;


import com.imooc.project.entity.Student;
import com.imooc.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/9/12.
 */
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value="getStudentByID",method = RequestMethod.GET)
    public Student getStudentByID(@RequestParam Integer id){

        return studentService.getStudentByID(id);
    }


}
