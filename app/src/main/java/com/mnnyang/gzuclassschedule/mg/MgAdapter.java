package com.mnnyang.gzuclassschedule.mg;

import android.support.annotation.NonNull;
import android.view.View;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.utils.spec.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by mnnyang on 17-11-4.
 */

public class MgAdapter extends RecyclerBaseAdapter<CsItem> {
    public String currentName;

    public MgAdapter setCurrentName(String currentName) {
        this.currentName = currentName;
        return this;
    }

    public MgAdapter(int itemLayoutId, @NonNull List<CsItem> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        String name = getData().get(position).getCsName().getName();
        holder.setText(R.id.tv_title, name);
        int nameId = getData().get(position).getCsName().getCsNameId();
        holder.itemView.setTag(nameId);

        if (currentName.equals(name)) {
            holder.getView(R.id.iv_done).setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(0x10000000);
        } else {
            holder.getView(R.id.iv_done).setVisibility(View.INVISIBLE);
            holder.itemView.setBackgroundColor(0xffffff);
        }
    }
}
