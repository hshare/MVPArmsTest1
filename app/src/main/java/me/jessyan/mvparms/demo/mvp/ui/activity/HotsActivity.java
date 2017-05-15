package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerHotsComponent;
import me.jessyan.mvparms.demo.di.module.HotsModule;
import me.jessyan.mvparms.demo.mvp.contract.HotsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.TabsBean;
import me.jessyan.mvparms.demo.mvp.presenter.HotsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.FragmentAdapter;
import me.jessyan.mvparms.demo.mvp.ui.fragment.HotsFragment;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * 功能/模块 ：***
 *
 * @author huzeliang
 * @version 1.0 ${date} ${time}
 * @see ***
 * @since ***
 */
public class HotsActivity extends BaseActivity<HotsPresenter> implements HotsContract.View {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHotsComponent
                .builder()
                .appComponent(appComponent)
                .hotsModule(new HotsModule(this)) //请将HotsModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public int initView() {
        return R.layout.activity_hots;
    }

    @Override
    public void initData() {
        mPresenter.requestTabs();
        setTitle("剑三通");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
    }

    @Override
    public void setTabs(List<TabsBean> tabsBeanList) {
        if (tabsBeanList != null) {
            Timber.tag("huzeliang").w("tabsBeanList != null");

            List<String> ids = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            for (int i = 0; i < tabsBeanList.size(); i++) {
                Timber.tag("huzeliang").w("idid = " + tabsBeanList.get(i).getId());
                mTabLayout.addTab(mTabLayout.newTab().setText(tabsBeanList.get(i).getText()));
                ids.add(tabsBeanList.get(i).getId());
                titles.add(tabsBeanList.get(i).getText());
            }
            FragmentAdapter adapter =
                    new FragmentAdapter(getSupportFragmentManager(), ids, titles);
            mViewPager.setOffscreenPageLimit(ids.size());
            mViewPager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setTabsFromPagerAdapter(adapter);
        } else {
            Timber.tag("huzeliang").w("tabsBeanList == null");
        }
    }

}