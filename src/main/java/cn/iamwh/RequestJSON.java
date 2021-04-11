package cn.iamwh;

import com.alibaba.fastjson.annotation.JSONField;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestJSON {
    // for windows
    public static final String DATASET_BASEDIR = "C:/Users/wh/Desktop/Face/img";
    // for linux
    // public static final String DATASET_BASEDIR = "/dataset";

    @JSONField(name = "taskName")
    private String taskName;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "dataset")
    private String dataset;

    @JSONField(name = "image")
    private String image;

    public Path getImagePath() {
        return Paths.get(DATASET_BASEDIR, dataset, image);
    }

    public RequestJSON(String type, String dataset, String image) {
        this.type = type;
        this.dataset = dataset;
        this.image = image;
    }

    public RequestJSON() {
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDataset() {
        return dataset;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
