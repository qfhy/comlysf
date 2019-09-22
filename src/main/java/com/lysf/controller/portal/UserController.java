package com.lysf.controller.portal;

import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login")
    public Result login(@RequestParam("username") String username,@RequestParam("password") String password, HttpSession session){
        Result result = userService.login(username,password);
        if (result.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,result.getData());
        }
        System.out.println(username+"     "+password);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public Result logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return Result.createSuccess();
    }



    @ResponseBody
    @RequestMapping(value = "/register")
    public Result register(@RequestParam("username")String username,@RequestParam("password")String password,
                           @RequestParam("phone") String phone, @RequestParam("email") String email,
                           @RequestParam("question")String qustion,@RequestParam("answer")String answer){
        return userService.register(username,password,phone,email,qustion,answer);
    }

    @ResponseBody
    @RequestMapping(value = "/checkValid",method = RequestMethod.POST)
    public Result<String> checkValid(@RequestParam("str") String str,@RequestParam("type")String type){
        return userService.checkValid(str,type);
    }

    @ResponseBody
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public Result getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return Result.createSuccess(user);
        }
        return Result.createErrorMessage("用户未登录");
    }

    @ResponseBody
    @RequestMapping(value = "/forgetGetQuestion",method = RequestMethod.POST)
    public Result forgetGetQuestion(@RequestParam("username")String username){
        return userService.selectQuestion(username);
    }

    @ResponseBody
    @RequestMapping(value = "/forgetCheckAnswer",method = RequestMethod.POST)
    public Result forgetCheckAnswer(@RequestParam("username")String username,@RequestParam("question") String question,@RequestParam("answer")String answer){
        return userService.checkAnswer(username,question,answer);
    }

    @ResponseBody
    @RequestMapping(value = "/foegetRestPassword",method = RequestMethod.POST)
    public Result foegetRestPassword(@RequestParam("username") String username,@RequestParam("passwordNew") String passwordNew,@RequestParam("forgetToken")String forgetToken){
        return userService.forgetRestPassword(username,passwordNew,forgetToken);
    }

    @ResponseBody
    @RequestMapping(value = "/restPassword",method = RequestMethod.POST)
    public Result restPassword(HttpSession session,@RequestParam("passwordOld")String passwordOld,@RequestParam("passwordNew")String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorMessage("用户未登录");
        }
        return userService.restPassword(user,passwordOld,passwordNew);
    }

    @ResponseBody
    @RequestMapping(value = "/updateInfomation",method = RequestMethod.POST)
    public Result updateInfomation(HttpSession session,@RequestBody User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser==null) {
            return Result.createErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        Result result = userService.updateInformation(user);
        if (result.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,result.getData());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getInformation",method = RequestMethod.POST)
    public Result getInformation(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser==null) {
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录status=10");
        }
        return userService.getInformation(currentUser.getId());
    }

















}
