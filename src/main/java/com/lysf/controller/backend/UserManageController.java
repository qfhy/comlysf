package com.lysf.controller.backend;

import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.User;
import com.lysf.service.UserService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    private final UserService userService;
    @Autowired
    public UserManageController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestParam("username") String username,@RequestParam("password") String password, HttpSession session){
        Result result = userService.login(username,password);
        if (result.isSuccess()){
            User user = (User) result.getData();
            if (user.getRole()== Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);
                return result;
            }else {
                return Result.createErrorMessage("该用户不是管理员");
            }
        }
        return result;
    }

}
