package com.example.testrool.bean;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private static LoggedInUser loggedInUser = new LoggedInUser();

    private Integer userId;
    private String displayName;
    private String email;

    private LoggedInUser() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private LoggedInUser(Integer userId, String displayName, String email) {
        this.displayName = displayName;
        this.userId = userId;
        this.email = email;
    }

    public static LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Integer userId, String displayName, String email) {
        loggedInUser = new LoggedInUser(userId, displayName, email);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}