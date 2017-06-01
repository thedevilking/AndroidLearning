package wwtao.demo.androidlearning.view.home.asyctask;

import com.annimon.stream.Optional;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;

/**
 * Created by wangweitao04 on 17/5/27.
 */

public class AsyncTaskFragment extends Fragment {
    @BindView(R.id.tvAsyncTask)
    TextView tvAsyncTask;

    @BindView(R.id.btnStartDownload)
    Button btnStart;

    @BindView(R.id.btnCancelDownload)
    Button btnCancel;

    @BindView(R.id.progressAsyncTask)
    ProgressBar progressBar;

    LoadPageTask asyncTask = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart.setOnClickListener(v -> {
            asyncTask = new LoadPageTask();
            asyncTask.execute(new Object());
        });
        btnCancel.setOnClickListener(v -> Optional.ofNullable(asyncTask)
                .ifPresent(task -> {
                    task.cancel(true);
                }));
    }

    private class LoadPageTask extends AsyncTask<Object, Double, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            Random random = new Random();
            double total = random.nextInt(200) + 200;
            double now = 0;
            try {
                while (now <= total) {
                    now += random.nextInt(10);
                    publishProgress(Math.min(now, total) / total * 100d);

                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Object();
        }

        @Override
        protected void onPreExecute() {
            //异步任务之前的操作
            btnStart.setEnabled(false);
            btnCancel.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            tvAsyncTask.setText(String.format("加载中 %.2f%%", 0.00));
        }

        @Override
        protected void onPostExecute(Object o) {
            //执行完异步任务后的修改
            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            tvAsyncTask.setText("加载完成");
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            btnStart.setEnabled(false);
            btnCancel.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            double percent = Optional.ofNullable(values).filter(value -> value.length > 0)
                    .map(value -> value[0])
                    .orElse(0.00d);
            tvAsyncTask.setText(String.format("加载中 %.2f%%", percent));
        }

        @Override
        protected void onCancelled() {
            btnStart.setEnabled(true);
            btnCancel.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
            tvAsyncTask.setText("取消加载");
        }
    }

}
