package com.example.testrool.bean;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private static LoggedInUser loggedInUser = new LoggedInUser();

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

    private Integer userId;
    private String displayName;

    private LoggedInUser() {

    }
    private LoggedInUser(Integer userId, String displayName) {
        this.displayName = displayName;
        this.userId = userId;
    }

    public static LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Integer userId, String displayName) {
        loggedInUser = new LoggedInUser(userId, displayName);
    }

}