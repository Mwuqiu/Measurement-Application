package com.example.testrool.Http;


import android.util.Log;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    static OkHttpClient client = new OkHttpClient();

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {

        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.d("success", result.toString());
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("fail", "返回参数为空 ！");
        return null;
    }

    public static Boolean recvOver = false;
    public static String result = null;

    public static String postToServer(String url, Object jsonObject) throws InterruptedException {
        result = null;
        recvOver = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.post(jsonObject, url);
                Log.i("res", result);
            }
        }).start();

        while (true) {
            synchronized (recvOver) {
                if (recvOver) {
                    break;
                }
            }
        }
        return result;
    }

    public static void post(Object param, String url) {
        String str = null;
        if(param == null)  str = "";
        else str = param.toString();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), str);

        try {
            Request request = new Request.Builder().url(url).post(requestBody).build();
            try (Response response = client.newCall(request).execute()) {
                String tmp = response.body().string();
                if (null == tmp || "".equals(tmp)) {
                    throw new Exception("返回了一个空值！");
                }
                result = tmp;

            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            Log.i("TAG", "post: post请求失败:" + e);
            result = "{'flag':0,'message':'" + e.toString() + "'}";
        }
        recvOver = true;
    }


}

