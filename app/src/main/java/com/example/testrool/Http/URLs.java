package com.example.testrool.Http;

public class URLs {


    //public static String IP = "192.168.3.250";
//    public static String IP = "10.0.2.2";
//    public static String IP = "49.140.60.45";
//    public static String IP = "192.168.212.191";
    private static String IP;

    private static String PORT = "8080";

    //private static String JSON_TEST_SERVLET= "http://" + IP + ":" + PORT + "/MeasurementAppServer/JsonTestServlet";

    //private static String LOGIN_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/CheckLogin";

    //private static String HISTORYITEM_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/History";

    public static void setIP(String IP) {
        URLs.IP = IP;
    }

    private static String preURL() {
        return "http://" + IP + ":" + PORT + "/MeasurementAppServer/";
    }

    public static String getJsonTestServlet() {
        return preURL() + "JsonTestServlet";
    }

    public static String getLoginServlet() {
        return preURL() + "CheckLogin";
    }

    public static String getModelUploadServlet() {
        return preURL() + "UploadModel";
    }

    public static String getModelServlet() {
        return preURL() + "/GetModels";
    }

    public static String getHistoryitemServlet() {
        return preURL() + "History";
    }

    public static String getHistoryUploadServlet() {
        return preURL() + "UploadHistoryItem";
    }
}
