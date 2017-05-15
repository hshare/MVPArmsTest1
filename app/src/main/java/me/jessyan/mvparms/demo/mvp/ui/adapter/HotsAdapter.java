package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.InfoContentListBean;
import me.jessyan.mvparms.demo.mvp.model.entity.User;
import me.jessyan.mvparms.demo.mvp.ui.holder.HotsItemHolder;
import me.jessyan.mvparms.demo.mvp.ui.holder.UserItemHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class HotsAdapter extends DefaultAdapter<InfoContentListBean> {
    public HotsAdapter(List<InfoContentListBean> infos) {
        super(infos);
    }

    public void setData(List<InfoContentListBean> infos){
        this.mInfos = infos;
        notifyDataSetChanged();
    }

    @Override
    public BaseHolder<InfoContentListBean> getHolder(View v, int viewType) {
        return new HotsItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list_hots;
    }
}
