package com.imooc.project.controller;

import com.imooc.project.entity.Imooc_user;
import com.imooc.project.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/25.
 */
@Api(value = "imooc_user的controller层",tags ={"用户登录与密码重置功能实现"})
@RestController
public class OldUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;



    @ApiOperation(value = "向用户表插入数据")
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public void insertImoocUser(){
        Imooc_user imooc_user=new Imooc_user();
        imooc_user.setUserName("lisi");
        imooc_user.setUserPassword("1234");
        imooc_user.setTestifyNumber("111");
        imooc_user.setUserQuestion("你的名字是什么？");
        imooc_user.setQuesAnswer("lisi");
        imooc_user.setUserEmail("123@163.com");
        imooc_user.setUserPhone("12345678");
        imooc_user.setUserRole("1");
        userService.insertUser(imooc_user);
    }
    @ApiOperation(value = "根据用户id查询数据")
    @GetMapping("/get")
    public Imooc_user selectById(@RequestParam("user_id") Integer id ){
        return userService.selectById(id);
    }
    @ApiOperation(value = "用户注册信息")
    @RequestMapping(value = "register",method = RequestMethod.POST)
    //@RequestBody是将json格式的数据转为响应的java对象，否则出错
    public Map userRegister(@RequestBody Imooc_user imooc_user){
        //Map<String,Object> model=new HashMap<String,Object>();
        Imooc_user user=new Imooc_user();
        user.setUserName(imooc_user.getUserName());
        String password=DigestUtils.md5DigestAsHex(imooc_user.getUserPassword().getBytes());
        user.setUserPassword(password);
        user.setUserPhone(imooc_user.getUserPhone());
        user.setUserRole(imooc_user.getUserRole());
        user.setUserEmail(imooc_user.getUserEmail());
        user.setUserQuestion(imooc_user.getUserQuestion());
        user.setQuesAnswer(imooc_user.getQuesAnswer());
        user.setTestifyNumber(imooc_user.getTestifyNumber());
        Map model=userService.saveUser(user);
        return model;
    }

    @ApiOperation(value = "用户登录")
    @GetMapping("login")
    public Map<String,Object> userLogin(@RequestParam String userName, @RequestParam String userPassword, HttpServletRequest request){
      Map<String,Object> model=userService.testifyUser(userName,userPassword);
      if (model.get("status").equals("0")){
          HttpSession session=request.getSession();
         Object u=model.get("msg");
          session.setAttribute("user","u");
      }
      return model;
    }

    @ApiOperation(value = "根据用户名获取用户信息")
    @GetMapping(value = "/getInfoByUsername")
     public Map<String,Object> getInfoByUsername(@RequestParam String userName){
        Imooc_user user=userService.selectByUsername(userName);
        Map<String,Object> model=new HashMap<String,Object>();
        if (user!=null){
            model.put("status","0");
            model.put("msg","用户名存在");
        }else{
            model.put("status","1");
            model.put("msg","用户名不存在");
        }
        return model;
    }

    @ApiOperation(value = "向用户邮箱发送链接")
    @GetMapping(value = "/sendMail")
    public Map sendMail(@RequestParam String userEmail) throws Exception{
        Map<String,Object> model=new HashMap<String,Object>();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(userEmail);
        mimeMessageHelper.setFrom("marryfeng9998@163.com");
        mimeMessageHelper.setSubject("Spring Boot Mail 发送邮件找回密码");

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head>");
        sb.append("<body><a>http://baidu.com</a><p>hello!this is spring mail test。</p></body>");
        sb.append("</html>");

        // 启用html
        mimeMessageHelper.setText(sb.toString(), true);
        // 发送邮件
        mailSender.send(mimeMessage);

        System.out.println("邮件已发送");
        model.put("status","0");
        model.put("msg","邮件发送成功");
         return model;

    }

    @ApiOperation(value = "用户重置密码")
    @PostMapping("/resetPassword")
    public Map resetPassword(@RequestParam String userName,@RequestParam String userPassword){
        Map<String ,Object> model=new HashMap<String,Object>();
        Imooc_user user=userService.selectByUsername(userName);
       if (user!=null){
         String newPassword=DigestUtils.md5DigestAsHex(userPassword.getBytes());
         user.setUserPassword(newPassword);
         int i=userService.updateUser(user);
         if (i!=0){
             model.put("status","0");
             model.put("msg","重置密码成功");
         }else{
             model.put("status","1");
             model.put("msg","重置密码失败");

         }
       }
        return model;
    }

    @ApiOperation(value = "根据用户问题找回密码")
    @GetMapping("/findAnswer")
    public Map findPassword(@RequestParam String userName,@RequestParam String quesAnswer){
        Map<String ,Object> model=new HashMap<String,Object>();
        Imooc_user user=userService.selectByUsername(userName);
        if (user!=null){
            String qAnswer=user.getQuesAnswer();
            if (qAnswer.equals(quesAnswer)){
                model.put("status","0");
                model.put("msg","问题回答正确，正在跳转到设置密码页面");
            }else{
                model.put("status","1");
                model.put("msg","问题回答错误，重置密码失败");
            }
        }
        return model;
    }



}
