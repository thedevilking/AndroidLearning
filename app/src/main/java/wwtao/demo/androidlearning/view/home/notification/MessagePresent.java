package wwtao.demo.androidlearning.view.home.notification;


import com.annimon.stream.Optional;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import wwtao.demo.androidlearning.R;
import wwtao.demo.androidlearning.model.MessageModel;
import wwtao.demo.androidlearning.view.home.notification.detail.MessageDetailActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by wangweitao04 on 17/5/31.
 */

public class MessagePresent {
    MessageViewImpl messageView;

    MessageModel messageModel = new MessageModel();

    public MessagePresent(MessageViewImpl messageViewImpl) {
        this.messageView = messageViewImpl;
    }

    public void generateNotification(String param) {
        new Thread(() -> {
            String message = messageModel.getMessage(param);

            Optional.ofNullable(messageView)
                    .ifPresent(view -> {
                        messageView.addMessage(message);
                    });
        }).start();
    }


}
