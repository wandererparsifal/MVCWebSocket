package com.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.bean.LoginResult;
import com.websocket.bean.User;
import com.websocket.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class UserService {

    public ArrayList<User> getAllUsers(ServletContext context) {
        String path = context.getRealPath("/storage/user.json");
        String usersJson = FileUtil.readFile(path);
        ArrayList<User> users = null;
        if (!StringUtils.isEmpty(usersJson)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                users = mapper.readValue(usersJson,
                        mapper.getTypeFactory().constructParametricType(ArrayList.class, User.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public LoginResult login(ServletContext context, String username, String password) {
        LoginResult loginResult = null;
        ArrayList<User> users = getAllUsers(context);
        if (null != users) {
            for (User user : users) {
                if (user.getName().equals(username)) {
                    if (user.getPwd().equals(password)) {
                        loginResult = new LoginResult(true, 0, user, "");
                    } else {
                        loginResult = new LoginResult(false, 302, null, null);
                    }
                    break;
                }
            }
        }
        if (null == loginResult) {
            loginResult = new LoginResult(false, 301, null, null);
        }
        return loginResult;
    }
}
