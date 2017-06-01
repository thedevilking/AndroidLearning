package wwtao.demo.androidlearning.service;

import com.google.common.collect.Lists;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import wwtao.demo.androidlearning.R;
import wwtao.demo.androidlearning.model.MessageModel;
import wwtao.demo.androidlearning.view.home.notification.detail.MessageDetailActivity;

/**
 * Created by wangweitao04 on 17/6/1.
 */

public class MessageService extends Service {
    MessageServiceBinder messageServiceBinder;
    MessageThread messageThread;

    @Override
    public void onCreate() {
        super.onCreate();
        messageThread = new MessageThread();
        messageThread.addOnMessageChangeListener(message -> {
            sendNotification(message, this);
        });
        messageThread.start();
        messageServiceBinder = new MessageServiceBinder(messageThread);
        Toast.makeText(this, "MessageService onCreate", Toast.LENGTH_LONG).show();
        Log.i("MessageService", "MessageService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "MessageService onBind", Toast.LENGTH_LONG).show();
        Log.i("MessageService", "MessageService onBind");
        return messageServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "MessageService onUnBind", Toast.LENGTH_LONG).show();
        Log.i("MessageService", "MessageService onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MessageService onDestroy", Toast.LENGTH_LONG).show();
        Log.i("MessageService", "MessageService onDestroy");
        Optional.ofNullable(messageThread).ifPresent(tempThread -> {
            tempThread.interrupt();
        });
        super.onDestroy();
    }

    private void sendNotification(String message, Context context) {
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra("message", message);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.img_notification)
                .setContentTitle("MessageFragment")
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        mNotifyMgr.notify(0, notification);
    }

    public static class MessageServiceBinder extends Binder {
        MessageThread thread;

        public MessageServiceBinder(MessageThread messageThread) {
            thread = messageThread;
        }

        public void addOnMessageChangeListener(OnMessageChangeListener onMessageChangeListener) {
            Optional.ofNullable(thread)
                    .ifPresent(tempThread ->
                            tempThread.addOnMessageChangeListener(onMessageChangeListener));
        }

        public void removeOnMessageChangeListener(OnMessageChangeListener onMessageChangeListener) {
            Optional.ofNullable(thread)
                    .ifPresent(tempThread ->
                            tempThread.removeMessageChangeListener(onMessageChangeListener));
        }

        public List<String> getMessageList() {
            return Optional.ofNullable(thread)
                    .map(MessageThread::getMessageList)
                    .orElse(Lists.newArrayList());
        }

        public void clearMessageList() {
            Optional.ofNullable(thread)
                    .ifPresent(tempThread -> tempThread.getMessageList().clear());
        }
    }

    public interface OnMessageChangeListener {
        void onNewMessage(String message);
    }

    private static class MessageThread extends Thread {
        MessageModel messageModel = new MessageModel();
        List<OnMessageChangeListener> messageChangeListeners = Lists.newArrayList();
        List<String> messageList = Lists.newArrayList();

        public MessageThread() {

        }

        public List<String> getMessageList() {
            return messageList;
        }

        public void addOnMessageChangeListener(OnMessageChangeListener onMessageChangeListener) {
            Optional.ofNullable(onMessageChangeListener)
                    .ifPresent(listener -> messageChangeListeners.add(listener));
        }

        public void removeMessageChangeListener(OnMessageChangeListener onMessageChangeListener) {
            Optional.ofNullable(onMessageChangeListener)
                    .ifPresent(listener -> messageChangeListeners.remove(listener));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String newMessage = messageModel.getMessage("Service Request");
                    messageList.add(0, newMessage);
                    Stream.of(messageChangeListeners)
                            .forEach(onMessageChangeListener -> onMessageChangeListener.onNewMessage(newMessage));
                    Log.i("MessageService", "service get message:" + newMessage);
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                // don't care
            }
        }
    }
}
