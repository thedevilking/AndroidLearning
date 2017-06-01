package wwtao.demo.androidlearning.model;

import java.text.DateFormat;

/**
 * Created by wangweitao04 on 17/5/31.
 */

public class MessageModel {
    static int num = 1;

    public String getMessage(String param) {
        return String.format("向后台传入参数(%s),从后台获取MESSAGE(ID:%d)", param, num++);
    }
}
