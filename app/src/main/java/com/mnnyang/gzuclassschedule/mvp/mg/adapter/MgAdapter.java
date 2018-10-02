package com.mnnyang.gzuclassschedule.mvp.mg.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.utils.spec.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by mnnyang on 17-11-4.
 */

public class MgAdapter extends RecyclerBaseAdapter<CourseGroup> {

    public interface MgListener extends RecyclerBaseAdapter.ItemClickListener {
        void onEditClick(View view, Long csNameId, RecyclerBaseAdapter.ViewHolder holder);

        void onDelClick(View view, Long csNameId, ViewHolder holder);
    }

    private long currentCsNameIdTag;

    public void setCurrentCsNameIdTag(long csNameId) {
        this.currentCsNameIdTag = csNameId;

        //只存在一个课表的时候, 默认就为该课表
        if (getData().size() == 1) {
            currentCsNameIdTag = -1;
        }
    }


    public MgAdapter(int itemLayoutId, @NonNull List<CourseGroup> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        String name = getData().get(position).getCgName();
        holder.setText(R.id.tv_title, name);
        long nameId = getData().get(position).getCgId();
        holder.itemView.setTag(nameId);

        //只存在一个课表的时候, 默认就为该课表
        if (currentCsNameIdTag == -1) {
            holder.itemView.setBackgroundColor(0x10000000);
            return;
        }

        if (currentCsNameIdTag == nameId) {
            holder.itemView.setBackgroundColor(0x10000000);
        } else {
            holder.itemView.setBackgroundColor(0xffffff);
        }
    }

    @Override
    protected void setItemEvent(final ViewHolder holder) {
        super.setItemEvent(holder);
        holder.getView(R.id.iv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MgListener) itemClickListener).onEditClick(v,
                        (Long) holder.itemView.getTag(), holder);
            }
        });

        holder.getView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MgListener) itemClickListener).onDelClick(v,
                        (Long) holder.itemView.getTag(), holder);
            }
        });

    }
}
