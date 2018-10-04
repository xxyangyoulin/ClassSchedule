package com.mnnyang.gzuclassschedule;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * RecyclerView.MusicAdapter 封装基类<br>
 * Created by mnnyang on 17-4-19.
 */

public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<RecyclerBaseAdapter.ViewHolder> {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;


    private View header;
    private View footer;

    private int itemLayoutId;
    List<T> data;


    public RecyclerBaseAdapter(@LayoutRes int itemLayoutId, @NonNull List<T> data) {
        this.itemLayoutId = itemLayoutId;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(header);
        }
        if (viewType == TYPE_FOOTER) {
            return new ViewHolder(footer);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerBaseAdapter.ViewHolder holder, int position) {
        if (position == 0 && header != null) {
            convertHeaderView(holder, position);
        } else if (position == getItemCount() - 1 && footer != null) {
            convertFooterView(holder, position);
        } else {
            convert(holder, header != null ? position - 1 : position);
            setItemEvent(holder);
        }
    }


    protected abstract void convert(ViewHolder holder, int position);

    protected void convertFooterView(ViewHolder holder, int position) {
    }

    protected void convertHeaderView(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        count += (header == null) ? 0 : 1;
        count += (footer == null) ? 0 : 1;
        return count;
    }

    protected void setItemEvent(final ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(v, holder);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (header != null) {
                return TYPE_HEADER;
            }
        }
        if (position == getItemCount() - 1) {
            if (footer != null) {
                return TYPE_FOOTER;
            }
        }
        return TYPE_NORMAL;
    }

    private ItemClickListener itemClickListener;

    public RecyclerBaseAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public List<T> getData() {
        return data;
    }


    public interface ItemClickListener {
        void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder);

        void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder);
    }

    public View getHeader() {
        return header;
    }

    public RecyclerBaseAdapter setHeader(View header) {
        this.header = header;
        return this;
    }

    public View getFooter() {
        return footer;
    }

    public RecyclerBaseAdapter setFooter(View footer) {
        this.footer = footer;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public RecyclerBaseAdapter.ViewHolder setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
            return this;
        }

        public RecyclerBaseAdapter.ViewHolder setImageResource(int viewId, int resId) {
            ImageView view = getView(viewId);
            view.setImageResource(resId);
            return this;
        }

        public RecyclerBaseAdapter.ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }
    }
}