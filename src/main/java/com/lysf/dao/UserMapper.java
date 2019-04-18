package com.lysf.dao;

import com.lysf.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkName(String username);

    User checkNameAndPass(@Param("username") String username,@Param("password") String password);

    int checkEmail(String email);

    String getQuestionByName(String username);

    int checkAnswer(@Param("username")String username,@Param("question") String question,@Param("answer") String answer);

    int restPassword(@Param("username")String username,@Param("md5Password") String md5Password);

    int checkPasswordById(@Param("password") String password,@Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email")String email,@Param("id") Integer id);
}