package com.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.bean.QueryResult;
import com.websocket.bean.User;
import com.websocket.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.websocket.Code.*;
import static com.websocket.Constant.KEY_TOKEN;
import static com.websocket.Constant.KEY_USER;

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

    public QueryResult<Map<String, Object>> login(ServletContext context, String username, String password) {
        QueryResult<Map<String, Object>> queryResult = null;
        ArrayList<User> users = getAllUsers(context);
        if (null != users) {
            for (User user : users) {
                if (user.getName().equals(username)) {
                    if (user.getPwd().equals(password)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(KEY_USER, user);
                        map.put(KEY_TOKEN, UUID.randomUUID().toString().replace("-", ""));
                        queryResult = new QueryResult<>(CODE_SUCCESS, map);
                    } else {
                        queryResult = new QueryResult<>(CODE_WRONG_PASSWORD, null);
                    }
                    break;
                }
            }
        }
        if (null == queryResult) {
            queryResult = new QueryResult<>(CODE_NO_SUCH_USER, null);
        }
        return queryResult;
    }

    public QueryResult<User> register(ServletContext context, String username, String password) {
        QueryResult<User> queryResult;
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
                queryResult = new QueryResult<>(CODE_SUCCESS, user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                queryResult = new QueryResult<>(CODE_STORAGE_ERROR, null);
            }
        } else {
            queryResult = new QueryResult<>(CODE_USER_ALREADY_EXISTS, null);
        }
        return queryResult;
    }
}
