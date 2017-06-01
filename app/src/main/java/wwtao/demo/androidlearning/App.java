package wwtao.demo.androidlearning;

import android.app.Application;
import android.util.Log;

/**
 * Created by wangweitao04 on 17/5/27.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("androidLearning", "app onCreate");
    }
}
