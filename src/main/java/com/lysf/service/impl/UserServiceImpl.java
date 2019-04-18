package com.lysf.service.impl;

import com.lysf.common.Const;
import com.lysf.common.TokenCache;
import com.lysf.dao.UserMapper;
import com.lysf.dto.Result;

import com.lysf.entity.User;
import com.lysf.service.UserService;
import com.lysf.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public Result login(String username, String password) {
        int i = userMapper.checkName(username);
        if (i==0){
            return Result.createErrorMessage("用户不存在");
        }
        //MD5加密  未使用
        String md5 = MD5Util.MD5EncodeUtf8(password);//真正使用这个加密功能的时候，之只需要将md5当做密码进行检测
        User user = userMapper.checkNameAndPass(username,md5);
        if (user==null){
            return Result.createErrorMessage("用户密码不正确");
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.createSuccess("登陆成功",user);
    }

    @Override
    public Result register(User user) {
        Result checkValidResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!checkValidResponse.isSuccess()){
            return checkValidResponse;
        }
        checkValidResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if (!checkValidResponse.isSuccess()){
            return checkValidResponse;
        }
        //MD5加密 未使用
        String md5 = MD5Util.MD5EncodeUtf8(user.getPassword());
        user.setPassword(md5);
        //使用insertSelective方法进行信息的填充，可以自动过滤掉那些空变量
        user.setRole(Const.Role.ROLE_CUSTOMER);
        int i = userMapper.insertSelective(user);
        if (i==0){
            return Result.createErrorMessage("注册失败");
        }
        return Result.createSuccessMessage("创建成功");
    }

    public Result<String> checkValid(String str, String type){
        if (StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                int i = userMapper.checkName(str);
                if (i>0){
                    return Result.createErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                int i = userMapper.checkEmail(str);
                if (i>0){
                    return Result.createErrorMessage("Email已存在");
                }
            }
        }else{
            return Result.createErrorMessage("参数类型不可为空");
        }
        return Result.createSuccessMessage("检校成功");
    }

    public Result selectQuestion(String username){
        Result checkValidResponse = this.checkValid(username,Const.USERNAME);
        if (checkValidResponse.isSuccess()){
            return Result.createErrorMessage("用户不存在");
        }
        String question = userMapper.getQuestionByName(username);
        if (StringUtils.isNotBlank(question)){
            return Result.createSuccess(question);
        }
        return Result.createErrorMessage("密保问题为空");
    }

    public Result checkAnswer(String username,String question,String answer){
        int i = userMapper.checkAnswer(username,question,answer);
        if (i==0){
            return Result.createErrorMessage("密保答案错误");
        }
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
        return Result.createSuccess(forgetToken);
    }


    public Result forgetRestPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return Result.createErrorMessage("参数错误，token需要传递");
        }
        Result checkValidResponse = this.checkValid(username,Const.USERNAME);
        if (checkValidResponse.isSuccess()){
            return Result.createErrorMessage("用户不存在");
        }
        String cache = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(cache)){
            return Result.createErrorMessage("token无效或过期");
        }
        if (StringUtils.equals(forgetToken,cache)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int i = userMapper.restPassword(username,md5Password);
            if (i>0){
                return Result.createSuccessMessage("密码修改成功");
            }
            return Result.createErrorMessage("密码修改失败");
        }else{
            return Result.createErrorMessage("token错误，请重新获取重置密码的token");
        }
    }

    public Result restPassword(User user,String passwordOld,String passwordNew){
        int i = userMapper.checkPasswordById(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (i==0){
            return Result.createErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        i = userMapper.updateByPrimaryKeySelective(user);
        if (i>0){
            return Result.createSuccessMessage("修改密码成功");
        }
        return Result.createErrorMessage("修改密码失败");
    }

    public Result updateInformation(User user){
        //username是不可以更改的
        //email也要进行检校，检校新的email是不是已经存在，并且与存在的email如果相同的话，不能是我们当前的用户的
        int i = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (i>0){
            return Result.createErrorMessage("email已存在，请更换email后再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        i = userMapper.updateByPrimaryKeySelective(updateUser);
        if (i>0){
            return Result.createSuccess("用户信息更新成功",updateUser);
        }
        return Result.createErrorMessage("用户信息更新失败");
    }

    public Result getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return Result.createErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.createSuccess(user);
    }

    //后台管理员权限检校  backend
    @Override
    public Result checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return Result.createSuccess();
        }
        return Result.createError();
    }

}
