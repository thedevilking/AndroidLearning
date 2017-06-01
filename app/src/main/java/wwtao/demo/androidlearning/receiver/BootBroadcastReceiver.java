package wwtao.demo.androidlearning.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import wwtao.demo.androidlearning.service.MessageService;

/**
 * Created by wangweitao04 on 17/6/1.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, MessageService.class);
//            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意，必须添加这个标记，否则启动会失败
            context.startService(serviceIntent);
        }
    }
}
