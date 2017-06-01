package wwtao.demo.androidlearning.view.home.notification.detail;

import com.google.common.base.Strings;

import com.annimon.stream.Optional;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;

/**
 * Created by wangweitao04 on 17/5/31.
 */

public class MessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvMessageDetail)
    TextView tvMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String message = Optional.ofNullable(getIntent())
                .map(tempIntent -> tempIntent.getStringExtra("message"))
//                .map(bundle -> bundle.getString("message"))
                .orElse("");
        tvMessage.setText(message);
    }
}
