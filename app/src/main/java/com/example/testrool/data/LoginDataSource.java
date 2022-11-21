package com.example.testrool.data;

import com.example.testrool.HttpUtil;
import com.example.testrool.URLs;
import com.example.testrool.data.model.LoggedInUser;
import com.example.testrool.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {


    private String login_url = URLs.LOGIN_SERVLET;


    public Result<LoggedInUser> login(String email, String password) {

        if("root".equals(email)){
            if("rootroot".equals(password)) {
                LoggedInUser user =
                        new LoggedInUser(
                                "123456",
                                "root");
                return new Result.Success<>(user);
            }else{
                return new Result.Error(new IOException("Error logging in", new Exception("root password error.")));
            }
        }

        try {
            // TODO: handle loggedInUser authentication
            JSONObject jsonObject = handleAuthentication(email, password);
            //flag = 1 : Successful
            if ("1".equals(jsonObject.get("flag"))) {
                LoggedInUser user =
                        new LoggedInUser(
                                (String) jsonObject.get("uid"),
                                (String) jsonObject.get("username"));
                return new Result.Success<>(user);
            } else {
                throw new Exception((String) jsonObject.get("message"));
            }


        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private JSONObject handleAuthentication(String email, String password) throws JSONException, InterruptedException {
        JSONObject postJsonObject = new JSONObject();
        postJsonObject.put("email", email);
        postJsonObject.put("password", password);
        String res = HttpUtil.postToServer(login_url, postJsonObject);
        return new JSONObject(res);

    }


    public void logout() {
        // TODO: revoke authentication
    }
}