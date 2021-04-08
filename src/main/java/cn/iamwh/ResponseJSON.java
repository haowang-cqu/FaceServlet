package cn.iamwh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class ResponseJSON {
    @JSONField(name = "status")
    private String status;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "dataset")
    private String dataset;

    @JSONField(name = "image")
    private String image;

    @JSONField(name = "result")
    private int result;

    @JSONField(name = "rect")
    private Rect rect;

    public ResponseJSON() {
    }

    public ResponseJSON(RequestJSON requestJSON) {
        type = requestJSON.getType();
        dataset = requestJSON.getDataset();
        image = requestJSON.getImage();
        result = -1;
    }

    public String getType() {
        return type;
    }

    public String getDataset() {
        return dataset;
    }

    public String getImage() {
        return image;
    }

    public int getResult() {
        return result;
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

    public void setResult(int result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
