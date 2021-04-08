package cn.iamwh;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/face")
public class FaceServlet extends HttpServlet {
    FaceUtil faceUtil;

    @Override
    public void init() throws ServletException {
        System.out.println("-------init-------");
        faceUtil = new FaceUtil();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println("That's all right, but you should use POST method.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 读取请求内容
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        // 解析JSON数据
        String postBody = stringBuilder.toString();
        RequestJSON requestJSON = JSON.parseObject(postBody, RequestJSON.class);
        ResponseJSON responseJSON = handleRequest(requestJSON);
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println(JSON.toJSONString(responseJSON));
    }

    private ResponseJSON handleRequest(RequestJSON requestJSON) {
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
                result = faceUtil.getLiveness(imagePath, "RGB");
                break;
            case "liveIR":
                result = faceUtil.getLiveness(imagePath, "IR");
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
        responseJSON.setResult(result);
        responseJSON.setStatus(status);
        return responseJSON;
    }

    @Override
    public void destroy() {
        faceUtil.unInitFaceEngine();
    }
}
