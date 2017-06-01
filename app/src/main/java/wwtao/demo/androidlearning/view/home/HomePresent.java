package wwtao.demo.androidlearning.view.home;

import java.util.List;

import wwtao.demo.androidlearning.model.HomeModel;

/**
 * Created by wangweitao04 on 17/5/27.
 */

public class HomePresent {
    HomeViewImpl homeView;
    HomeModel homeModel = new HomeModel();

    public HomePresent(HomeViewImpl homeView) {
        this.homeView = homeView;
    }

    public void initHome() {
        List<String> list = homeModel.getTabStrings();
        homeView.setTabs(list);
    }
}
