package cn.iamwh;

import java.util.HashMap;

public class Counter {
    private final HashMap<String, Integer> count = new HashMap<String, Integer>();

    public void countAddOne(String taskName) {
        if (count.containsKey(taskName)) {
            count.put(taskName, count.get(taskName)+1);
            // 计数满 100 发送一次进度更新到后端
            if (count.get(taskName) % 100 == 0) {
                System.out.println(taskName + ": " + count.get(taskName));
                new Thread(new ProgressThread(taskName)).start();
            }
        } else {
            count.put(taskName, 1);
        }
    }
}