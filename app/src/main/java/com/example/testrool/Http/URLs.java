package com.example.testrool.Http;

public class URLs {


    //public static String IP = "192.168.3.250";
    //public static String IP = "10.0.2.2";
    public static String IP = "49.140.60.45";
//    public static String IP = "192.168.212.191";


    public static String PORT = "8080";

    public static String JSON_TEST_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/JsonTestServlet";

    public static String LOGIN_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/CheckLogin";

    public static String HISTORYITEM_SERVLET = "http://" + IP + ":" + PORT + "/MeasurementAppServer/History";
}
