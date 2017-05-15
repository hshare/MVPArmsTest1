package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.utils.RxUtils;
import me.jessyan.mvparms.demo.mvp.contract.HotsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.HotBean;
import me.jessyan.mvparms.demo.mvp.model.entity.InfoContentListBean;
import me.jessyan.mvparms.demo.mvp.model.entity.TabsBean;
import me.jessyan.mvparms.demo.mvp.model.entity.User;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HotsAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.UserAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;

import javax.inject.Inject;


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
@ActivityScope
public class HotsPresenter extends BasePresenter<HotsContract.Model, HotsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private DefaultAdapter mAdapter;
    private List<InfoContentListBean> infoContentListBeanList = new ArrayList<>();

    @Inject
    public HotsPresenter(HotsContract.Model model, HotsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void requestTabs() {
        mModel.getTabs()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                })
                .compose(RxUtils.bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<List<TabsBean>>(mErrorHandler) {
                    @Override
                    public void onNext(List<TabsBean> tabsBeanList) {
                        mRootView.setTabs(tabsBeanList);
                    }
                });
    }

    public void requestHots(int page, String id) {
        if (mAdapter == null) {
            mAdapter = new HotsAdapter(infoContentListBeanList);
            mRootView.setAdapter(mAdapter);//设置Adapter
        }
        mModel.getHots(page, id)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    Timber.tag("huzeliang").w("doAfterTerminate---------------");
                    mRootView.hideLoading();
                })
                .compose(RxUtils.bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<HotBean>(mErrorHandler) {
                    @Override
                    public void onNext(HotBean hotBeanList) {
                        if (hotBeanList != null && hotBeanList.getInfo_content_list() != null
                                && hotBeanList.getInfo_content_list().size() != 0) {
                            if (page == 1) {
                                infoContentListBeanList.clear();
                            }
                            infoContentListBeanList.addAll(hotBeanList.getInfo_content_list());
                            mAdapter.notifyDataSetChanged();
                            for (int i = 0; i < infoContentListBeanList.size(); i++) {
                                Timber.tag("huzeliang").w("data:" + infoContentListBeanList.get(i).getTitle());
                            }
                        } else {
                            Timber.tag("huzeliang").w("hotBeanList == null");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}