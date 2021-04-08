package cn.iamwh;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import static com.arcsoft.face.toolkit.ImageFactory.getGrayData;
import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;
import com.arcsoft.face.toolkit.ImageInfoEx;
import java.nio.file.Path;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.*;
import com.arcsoft.face.toolkit.ImageInfo;

import java.util.ArrayList;
import java.util.List;


public class FaceUtil {
    // For Windows
    private static final String LIB_PATH = "C:\\Users\\wh\\Desktop\\Face\\ArcFace\\libs\\WIN64";
    private static final String APP_ID = "HEfKQcD6gsA2nPaFxyX6wPuAtEtRDxBRSmnVmS8z2s8s";
    private static final String SDK_KEY = "CtMurZDHNxpCG4f978a612vD2JFcVHMbfdvgp7VciDj5";
    // For Linux
    // private static final String LIB_PATH = "/ArcFace";
    // private static final String APP_ID = "HEfKQcD6gsA2nPaFxyX6wPuAtEtRDxBRSmnVmS8z2s8s";
    // private static final String SDK_KEY = "CtMurZDHNxpCG4f978a612vCtBgSQCUZX9zNNpNkXXPF";

    private final FaceEngine faceEngine;

    public FaceUtil() {
        faceEngine = new FaceEngine(LIB_PATH);
        activateFaceEngine();
        initFaceEngine();
    }

    public void activateFaceEngine() {
        int errorCode = faceEngine.activeOnline(APP_ID, SDK_KEY);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
            System.out.println("错误码: " + errorCode);
        }
    }

    public void initFaceEngine() {
        // 引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        // 功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        // 初始化引擎
        int errorCode = faceEngine.init(engineConfiguration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
            System.out.println("错误码: " + errorCode);
        }
    }

    public void unInitFaceEngine() {
        int errorCode = faceEngine.unInit();
        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("引擎卸载失败");
            System.out.println("错误码: " + errorCode);
        }
    }

    public String getVersionInfo() {
        return faceEngine.getVersion().toString();
    }

    // 图片信息整合
    public ImageInfoEx getImageInfoEx(Path image, String mode) {
        ImageInfo imageInfo;
        if ("RGB".equals(mode)) {
            imageInfo = getRGBData(image.toFile());
        } else {
            imageInfo = getGrayData(image.toFile());
        }
        ImageInfoEx imageInfoEx = new ImageInfoEx();
        imageInfoEx.setHeight(imageInfo.getHeight());
        imageInfoEx.setWidth(imageInfo.getWidth());
        imageInfoEx.setImageFormat(imageInfo.getImageFormat());
        imageInfoEx.setImageDataPlanes(new byte[][]{imageInfo.getImageData()});
        imageInfoEx.setImageStrides(new int[]{imageInfo.getWidth() * 3});
        return imageInfoEx;
    }

    // 人脸检测
    public List<FaceInfo> detect(Path image, String mode) {
        ImageInfoEx imageInfoEx = getImageInfoEx(image, mode);
        List<FaceInfo> faceInfoList = new ArrayList<>();
        int errorCode = faceEngine.detectFaces(imageInfoEx, DetectModel.ASF_DETECT_MODEL_RGB, faceInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("检测出错");
        }
        return faceInfoList;
    }

    // 人脸框检测
    public Rect getFaceRect(Path image) {
        List<FaceInfo> faceInfoList = detect(image, "RGB");
        if (faceInfoList.size() == 0) {
            return null;
        }
        Rect rect = new Rect();
        rect.setLeft(faceInfoList.get(0).getRect().getLeft());
        rect.setTop(faceInfoList.get(0).getRect().getTop());
        rect.setRight(faceInfoList.get(0).getRect().getRight());
        rect.setBottom(faceInfoList.get(0).getRect().getBottom());
        return rect;
    }

    // 人脸数量
    public int getFaceNumber(Path image) {
        List<FaceInfo> faceInfoList = detect(image, "RGB");
        return faceInfoList.size();
    }

    // 年龄
    public int getAge(Path image) {
        ImageInfoEx imageInfoEx = getImageInfoEx(image, "RGB");
        List<FaceInfo> faceInfoList = detect(image, "RGB");
        // 没有检测到人脸则返回 -1
        if (faceInfoList.size() == 0) {
            return -1;
        }
        //人脸属性检测
        FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportAge(true);
        faceEngine.process(imageInfoEx, faceInfoList, configuration);
        List<AgeInfo> ageInfoList = new ArrayList<>();
        faceEngine.getAge(ageInfoList);
        // 返回第一个人脸的年龄
        return ageInfoList.get(0).getAge();
    }

    // 性别
    public int getGender(Path image) {
        ImageInfoEx imageInfoEx = getImageInfoEx(image, "RGB");
        List<FaceInfo> faceInfoList = detect(image, "RGB");
        // 没有检测到人脸则返回 -1
        if (faceInfoList.size() == 0) {
            return -1;
        }
        //人脸属性检测
        FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportGender(true);
        faceEngine.process(imageInfoEx, faceInfoList, configuration);
        List<GenderInfo> genderInfoList = new ArrayList<>();
        faceEngine.getGender(genderInfoList);
        // 返回第一个人脸的性别
        return genderInfoList.get(0).getGender();
    }

    // 活体
    public int getLiveness(Path image, String mode) {
        faceEngine.setLivenessParam(0.5f, 0.7f);
        ImageInfoEx imageInfoEx = getImageInfoEx(image, mode);
        List<FaceInfo> faceInfoList = detect(image, mode);
        // 没有检测到人脸则返回 -1
        if (faceInfoList.size() == 0) {
            return -1;
        }
        FunctionConfiguration configuration = new FunctionConfiguration();
        if ("RGB".equals(mode)) {
            configuration.setSupportLiveness(true);
            faceEngine.process(imageInfoEx, faceInfoList, configuration);
            List<LivenessInfo> livenessInfoList = new ArrayList<>();
            faceEngine.getLiveness(livenessInfoList);
            return livenessInfoList.get(0).getLiveness();
        } else {
            configuration.setSupportIRLiveness(true);
            faceEngine.processIr(imageInfoEx, faceInfoList, configuration);
            List<IrLivenessInfo> irLivenessInfo = new ArrayList<>();
            faceEngine.getLivenessIr(irLivenessInfo);
            return irLivenessInfo.get(0).getLiveness();
        }
    }
}
