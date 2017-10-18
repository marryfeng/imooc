package com.imooc.project.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/22.
 */

public class Student implements Serializable{
    private static final long serialVersionUID = 2076533977759512826L;
    private Integer id;
    private String sname;
    private Integer age;

    public Student(){}

    public Student(Integer id, String sname, Integer age) {
        this.id = id;
        this.sname = sname;
        this.age = age;
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
