package com.mnnyang.gzuclassschedule.mg;

import android.support.annotation.NonNull;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.utils.spec.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by mnnyang on 17-11-4.
 */

public class MgAdapter extends RecyclerBaseAdapter<CsItem> {
    public MgAdapter(int itemLayoutId, @NonNull List<CsItem> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        holder.setText(R.id.tv_title, getData().get(position).getCsName().getName());
        holder.itemView.setTag(getData().get(position).getCsName().getCsNameId());
    }
}
