package cn.iamwh;

import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@WebServlet("/face")
public class FaceServlet extends HttpServlet {
    FaceUtil faceUtil;
    Counter counter;

    @Override
    public void init() {
        faceUtil = new FaceUtil();
        counter = new Counter();
        System.out.println("INFO: init servlet OK.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println("That's all right, but you should use POST method.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 读取请求内容
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = req.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            // 解析JSON数据
            String postBody = stringBuilder.toString();
            RequestJSON requestJSON = JSON.parseObject(postBody, RequestJSON.class);
            // 处理请求
            ResponseJSON responseJSON = handle(requestJSON);
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.println(JSON.toJSONString(responseJSON));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ResponseJSON handle(RequestJSON requestJSON) {
        ResponseJSON responseJSON = new ResponseJSON(requestJSON);
        int result = -1;
        String status = "OK";
        // 判断图片文件是否存在
        Path imagePath = requestJSON.getImagePath();
        if (!imagePath.toFile().exists()) {
            responseJSON.setStatus("image not exists");
            return responseJSON;
        }
        switch (requestJSON.getType()) {
            case "number":
                result = faceUtil.getFaceNumber(imagePath);
                break;
            case "age":
                result = faceUtil.getAge(imagePath);
                break;
            case "gender":
                result = faceUtil.getGender(imagePath);
                break;
            case "liveRGB":
                result = faceUtil.getLive(imagePath, "RGB");
                break;
            case "liveIR":
                result = faceUtil.getLive(imagePath, "IR");
                break;
            case "rect":
                Rect rect = faceUtil.getFaceRect(imagePath);
                if (null == rect) {
                    result = 0;
                    status = "no face detected";
                } else {
                    result = 1;
                    responseJSON.setRect(rect);
                }
                break;
            default:
                status = "wrong type";
                break;
        }
        counter.countAddOne(requestJSON.getTaskName());
        responseJSON.setResult(result);
        responseJSON.setStatus(status);
        return responseJSON;
    }

    @Override
    public void destroy() {
        faceUtil.unInitFaceEngine();
    }
}