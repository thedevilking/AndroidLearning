package wwtao.demo.androidlearning.view.home.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;

/**
 * Created by wangweitao04 on 17/6/1.
 */

public class BroadcastReceiverFragment extends Fragment {
    @BindView(R.id.tvReceiver)
    TextView textView;

    @BindView(R.id.btnReceiver)
    Button button;

    boolean isRegisterReceiver = false;

    SMSReceiver smsBroadCastReceiver = new SMSReceiver();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_receiver, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        showRegisterButton();
        button.setOnClickListener(v -> {
            if (isRegisterReceiver) {
                getActivity().unregisterReceiver(smsBroadCastReceiver);
                isRegisterReceiver = false;
            } else {
                IntentFilter intentFilter = new IntentFilter("android.intent.action.TIMEZONE_CHANGED");
                getActivity().registerReceiver(smsBroadCastReceiver, intentFilter, null, null);
                isRegisterReceiver = true;
            }
            showRegisterButton();
        });

    }

    private void showRegisterButton() {
        if (isRegisterReceiver) {
            button.setText("取消监听时区变化");
        } else {
            button.setText("监听时区变化");
        }
    }

    public class SMSReceiver extends BroadcastReceiver {
        int id = 1;

        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuilder stringBuilder = new StringBuilder(textView.getText());
            stringBuilder.append("\n");
            stringBuilder.append(String.format("id:%d\t时区发生改变", id++));
            textView.setText(stringBuilder.toString());
        }

    }
}
