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
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}