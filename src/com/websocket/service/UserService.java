package com.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.bean.LoginResult;
import com.websocket.bean.RegisterResult;
import com.websocket.bean.User;
import com.websocket.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static com.websocket.bean.LoginResult.CODE_NO_SUCH_USER;
import static com.websocket.bean.LoginResult.CODE_WRONG_PASSWORD;
import static com.websocket.bean.RegisterResult.CODE_STORAGE_ERROR;
import static com.websocket.bean.RegisterResult.CODE_USER_ALREADY_EXISTS;

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
                        loginResult = new LoginResult(false, CODE_WRONG_PASSWORD, null, null);
                    }
                    break;
                }
            }
        }
        if (null == loginResult) {
            loginResult = new LoginResult(false, CODE_NO_SUCH_USER, null, null);
        }
        return loginResult;
    }

    public RegisterResult register(ServletContext context, String username, String password) {
        RegisterResult registerResult;
        ArrayList<User> users = getAllUsers(context);
        boolean exists = false;
        if (null != users) {
            for (User user : users) {
                if (user.getName().equals(username)) {
                    exists = true;
                    break;
                }
            }
        } else {
            users = new ArrayList<>();
        }
        if (!exists) {
            User user = new User(UUID.randomUUID().toString().replace("-", ""),
                    username, password);
            users.add(user);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = mapper.writeValueAsString(users);
                String path = context.getRealPath("/storage/user.json");
                FileUtil.saveText(json, path);
                registerResult = new RegisterResult(true, 0, user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                registerResult = new RegisterResult(false, CODE_STORAGE_ERROR, null);
            }
        } else {
            registerResult = new RegisterResult(false, CODE_USER_ALREADY_EXISTS, null);
        }
        return registerResult;
    }
}
