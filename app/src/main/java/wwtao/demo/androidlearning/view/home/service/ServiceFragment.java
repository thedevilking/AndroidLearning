package wwtao.demo.androidlearning.view.home.service;

import com.google.common.base.Strings;

import com.annimon.stream.Optional;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;
import wwtao.demo.androidlearning.service.MessageService;
import wwtao.demo.androidlearning.view.home.notification.MessageAdapter;

/**
 * Created by wangweitao04 on 17/6/1.
 */

public class ServiceFragment extends Fragment {
    @BindView(R.id.btnStartServiceFragment)
    Button btnStart;

    @BindView(R.id.btnStopServiceFragment)
    Button btnStop;

    @BindView(R.id.btnBindServiceFragment)
    Button btnBind;

    @BindView(R.id.btnUnBindServiceFragment)
    Button btnUnBind;

    @BindView(R.id.recyclerViewServiceFragment)
    RecyclerView recyclerView;

    MessageAdapter messageAdapter = new MessageAdapter();
    Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(messageAdapter);

        Intent intent = new Intent(getActivity(), MessageService.class);
        btnStart.setOnClickListener(v -> {
            getActivity().startService(intent);
        });

        btnStop.setOnClickListener(v -> {
            getActivity().stopService(intent);
        });

        btnBind.setOnClickListener(v -> {
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        });

        btnUnBind.setOnClickListener(v -> {
            Optional.ofNullable(binder).ifPresent(tempBinder -> {
                binder.removeOnMessageChangeListener(onMessageChangeListener);
                getActivity().unbindService(serviceConnection);
                binder = null;
                messageAdapter.getMessageList().clear();
                messageAdapter.notifyDataSetChanged();
            });
        });
    }

    private MessageService.MessageServiceBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = Optional.ofNullable(service)
                    .filter(tempService -> tempService instanceof MessageService.MessageServiceBinder)
                    .map(tempService -> (MessageService.MessageServiceBinder) tempService)
                    .orElse(null);
            binder.addOnMessageChangeListener(onMessageChangeListener);
            mHandler.post(() -> {
                messageAdapter.getMessageList().addAll(binder.getMessageList());
                messageAdapter.notifyDataSetChanged();
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            binder.removeOnMessageChangeListener(onMessageChangeListener);
        }
    };

    private MessageService.OnMessageChangeListener onMessageChangeListener = message -> {
        Optional.ofNullable(messageAdapter)
                .filter(adapter -> !Strings.isNullOrEmpty(message))
                .ifPresent(adapter -> {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            messageAdapter.getMessageList().add(0, message);
                            messageAdapter.notifyDataSetChanged();
                        }
                    });
                });
    };
}
