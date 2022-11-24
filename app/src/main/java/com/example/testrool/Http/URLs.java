package com.example.testrool.Http;

public class URLs {


    //public static String IP = "192.168.3.250";
    //public static String IP = "10.0.2.2";
    public static String IP = "49.140.60.45";
//    public static String IP = "192.168.212.191";


    public static String PORT = "8080";

    public static String preURL = "http://" + IP + ":" + PORT + "/MeasurementAppServer/";

    public static String JSON_TEST_SERVLET = preURL + "JsonTestServlet";

    public static String LOGIN_SERVLET = preURL + "CheckLogin";

    public static String HISTORYITEM_SERVLET = preURL + "History";

    public static String UPLOAD_MODEL_SERVLET = preURL + "UploadModel";

    public static String GET_MODEL_SERVLET = preURL + "GetModels";
}
