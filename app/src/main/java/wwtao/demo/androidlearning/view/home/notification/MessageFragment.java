package wwtao.demo.androidlearning.view.home.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;
import wwtao.demo.androidlearning.view.home.notification.detail.MessageDetailActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by wangweitao04 on 17/5/31.
 */

public class MessageFragment extends Fragment implements MessageViewImpl {

    @BindView(R.id.etMessageFragment)
    EditText editText;
    @BindView(R.id.tvMessageFragment)
    TextView textView;
    @BindView(R.id.btnMessageFragment)
    Button button;
    @BindView(R.id.recyclerViewMessageFragment)
    RecyclerView recyclerView;

    MessagePresent messagePresent;
    MessageAdapter messageAdapter = new MessageAdapter();
    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messagePresent = new MessagePresent(this);
        initView();
    }

    private void initView() {
        button.setOnClickListener(v -> {
            messagePresent.generateNotification(editText.getText().toString());
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void addMessage(String message) {
        sendNotification(message);
        handler.post(() -> {
            messageAdapter.getMessageList().add(0, message);
            messageAdapter.notifyDataSetChanged();
        });
    }

    private void sendNotification(String message) {
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
        intent.putExtra("message", message);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(getActivity())
                .setSmallIcon(R.drawable.img_notification)
                .setContentTitle("MessageFragment")
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        mNotifyMgr.notify(0, notification);
    }
}
