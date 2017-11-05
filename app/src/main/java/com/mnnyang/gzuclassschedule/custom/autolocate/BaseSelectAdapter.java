package com.mnnyang.gzuclassschedule.custom.autolocate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mnnyang on 17-11-05.
 */

public abstract class BaseSelectAdapter<T> extends RecyclerView.Adapter<BaseSelectAdapter.ViewHolder>
        implements AutoLocateHorizontalView.IAutoLocateHorizontalView {
    private Context context;
    protected ArrayList<T> data;
    private View view;
    private int layoutId;

    public BaseSelectAdapter(Context context, ArrayList<T> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    @Override
    public BaseSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public View getItemView() {
        return view;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;

        ViewHolder(View itemView) {
            super(itemView);
            if (mViews == null)
                mViews = new SparseArray<View>();
        }

        public <V extends View> V getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (V) view;
        }

        public BaseSelectAdapter.ViewHolder setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }
    }
}
