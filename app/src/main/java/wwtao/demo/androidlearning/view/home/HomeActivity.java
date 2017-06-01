package wwtao.demo.androidlearning.view.home;

import com.annimon.stream.Optional;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;
import wwtao.demo.androidlearning.view.home.asyctask.AsyncTaskFragment;
import wwtao.demo.androidlearning.view.home.empty.EmptyFragment;
import wwtao.demo.androidlearning.view.home.notification.MessageFragment;
import wwtao.demo.androidlearning.view.home.service.ServiceFragment;
import wwtao.demo.androidlearning.view.home.sms.BroadcastReceiverFragment;

public class HomeActivity extends AppCompatActivity implements HomeViewImpl {

    @BindView(R.id.tabLayoutHome)
    TabLayout tabLayout;

    @BindView(R.id.viewPagerHome)
    ViewPager viewPager;

    HomePresent homePresent;

    List<String> tabList = new ArrayList<>();
    private FragmentPagerAdapter homeViewPageAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new AsyncTaskFragment();
            } else if (position == 1) {
                return new MessageFragment();
            } else if (position == 2) {
                return new ServiceFragment();
            } else if (position == 3) {
                return new BroadcastReceiverFragment();
            } else {
                return new EmptyFragment();
            }
        }

        @Override
        public int getCount() {
            return Optional.ofNullable(tabList)
                    .map(list -> list.size())
                    .orElse(0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Optional.ofNullable(tabList)
                    .filter(list -> position >= 0 && position < list.size())
                    .map(list -> list.get(position))
                    .orElse(null);
        }
    };

    @Override
    public void setTabs(List<String> list) {
        synchronized (tabList) {
            tabList.clear();
            tabList.addAll(list);
        }
        homeViewPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserData(List<HomeData> list) {

    }

    @Override
    public void clearRefresh() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        viewPager.setAdapter(homeViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        homePresent = new HomePresent(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        homePresent.initHome();
    }
}
