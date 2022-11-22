package com.example.testrool.Http;

public class URLs {


    private String url = "http://192.168.3.250:8080/MeasurementAppServer/JsonTestServlet";//本地Tomcat


    //public static String IP = "192.168.3.250";
    //public static String IP = "10.0.2.2";
    public static String IP = "49.140.60.45";
//    public static String IP = "192.168.212.191";


    public static String PORT = "8080";

    public static String JSON_TEST_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/JsonTestServlet";

    public static String LOGIN_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/CheckLogin";

    public static String HISTORYITEM_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/History";
}
