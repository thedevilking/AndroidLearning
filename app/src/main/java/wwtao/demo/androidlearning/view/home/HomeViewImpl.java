package wwtao.demo.androidlearning.view.home;

import java.util.List;

/**
 * Created by wangweitao04 on 17/5/27.
 */

public interface HomeViewImpl {

    void setTabs(List<String> list);

    void setUserData(List<HomeData> list);

    void clearRefresh();
}
