package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerHotsComponent;
import me.jessyan.mvparms.demo.di.module.HotsModule;
import me.jessyan.mvparms.demo.mvp.contract.HotsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.TabsBean;
import me.jessyan.mvparms.demo.mvp.presenter.HotsPresenter;
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
 * Created by hcare on 2017/5/13.
 */

public class HotsFragment extends BaseFragment<HotsPresenter> implements HotsContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private LayoutInflater mInflater;

    public static HotsFragment newInstance(String id) {
        HotsFragment fragment = new HotsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerHotsComponent
                .builder()
                .appComponent(appComponent)
                .hotsModule(new HotsModule(this))//请将HotsModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        mInflater = inflater;
        return inflater.inflate(R.layout.fragment_hots, container, false);
    }

    private int pageNo = 1;

    @Override
    public void initData() {
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setSwipeStyle(SwipeToLoadLayout.STYLE.ABOVE);
        View view1 = getActivity().getLayoutInflater().inflate(R.layout.layout_google_hook_header, swipeToLoadLayout, false);
        swipeToLoadLayout.setRefreshHeaderView(view1);
        View view2 = getActivity().getLayoutInflater().inflate(R.layout.layout_google_hook_footer, swipeToLoadLayout, false);
        swipeToLoadLayout.setLoadMoreFooterView(view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getArguments() != null) {
            Timber.tag("huzeliang").w("id = " + getArguments().getString("id", "0") + "pageNO:" + pageNo);
            mPresenter.requestHots(pageNo, getArguments().getString("id", "0"));
        } else {
            Timber.tag("huzeliang").w("getArguments() != null");
        }
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

    }


    @Override
    public void showLoading() {
//        swipeToLoadLayout.setLoadingMore(true);
    }

    @Override
    public void hideLoading() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
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

    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setTabs(List<TabsBean> tabsBeanList) {

    }


    @Override
    public void onRefresh() {
        pageNo = 1;
        mPresenter.requestHots(pageNo, getArguments().getString("id", "0"));
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        mPresenter.requestHots(pageNo, getArguments().getString("id", "0"));
    }
}