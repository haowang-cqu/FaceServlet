package cn.iamwh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Rect {
    /**
     * 人脸举行的最左边
     */
    @JSONField(name = "left")
    public int left;
    /**
     * 人脸矩形的最上边
     */
    @JSONField(name = "top")
    public int top;
    /**
     * 人脸矩形的最右边
     */
    @JSONField(name = "right")
    public int right;
    /**
     * 人脸矩形的最下边
     */
    @JSONField(name = "bottom")
    public int bottom;

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}