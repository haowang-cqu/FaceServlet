package cn.iamwh;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestEngine {
    public static void main(String[] args) {
        FaceUtil faceUtil = new FaceUtil();
        System.out.println(faceUtil.getVersionInfo());
        Path testImage = Paths.get("C:\\Users\\wh\\Desktop\\Face\\a.jpg");
        int number = faceUtil.getFaceNumber(testImage);
        System.out.println("人脸数量: " + number);
        int gender = faceUtil.getGender(testImage);
        System.out.println("性别: " + gender);
        int age = faceUtil.getAge(testImage);
        System.out.println("年龄: " + age);
        int live = faceUtil.getLiveness(testImage, "RGB");
        System.out.println("活体: " + live);
        Rect rect = faceUtil.getFaceRect(testImage);
        System.out.println("矩形: " + rect);
        faceUtil.unInitFaceEngine();
    }
}
