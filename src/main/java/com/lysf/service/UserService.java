package com.lysf.service;

import com.lysf.dto.Result;
import com.lysf.entity.User;

public interface UserService {

    Result login(String username, String password);

    Result<String> checkValid(String str, String type);

    Result selectQuestion(String username);

    Result checkAnswer(String username,String question,String answer);

    Result forgetRestPassword(String username,String passwordNew,String forgetToken);

    Result restPassword(User user, String passwordOld, String passwordNew);

    Result updateInformation(User user);

    Result getInformation(Integer userId);

    Result checkAdminRole(User user);

    Result register(String username, String password, String phone, String email, String qustion, String answer);
}
