package cn.iamwh;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import java.io.IOException;

public class ProgressThread implements Runnable {
    private final String taskName;

    ProgressThread(String taskName) {
        this.taskName = taskName;
    }

    public void run() {
        System.out.println("=================post count=================");
        MediaType json = MediaType.parse("application/json;charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject postData = new JSONObject();
        postData.put("taskName", taskName);
        RequestBody requestBody = RequestBody.create(json, postData.toJSONString());
        String url = "http://chuadongf.vaiwan.com/userKmmy/progress";
        String fake_api = "http://192.168.80.1:9999/progress";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                System.out.println(response.body().string());
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}