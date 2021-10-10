package com.example.picture_sharing_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MomentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<noter> notes;
    private static final String TAG = "EmptyAdapter";
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;
    private Context mContext;
    private int resourceId;

    public MomentsAdapter(@NonNull Context context, int resourceId, List<noter> data) {
        this.mContext = context;
        this.notes = data;
        this.resourceId = resourceId;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
            return new RecyclerView.ViewHolder(emptyView) {
            };
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder vh = (MyViewHolder) holder;
            noter not = notes.get(position);
            String[] time = not.space.split("_");
            vh.tvTime.setText(time[0] + "/" + time[1] + "/" + time[2]);
            Bitmap headBit = BitmapFactory.decodeByteArray(not.headByte, 0, not.headByte.length);
            vh.ivHead.setImageBitmap(headBit);
            vh.tvUsername.setText(not.author);
            Bitmap bit = BitmapFactory.decodeByteArray(not.imageByte, 0, not.imageByte.length);
            vh.ivImage.setImageBitmap(bit);
        }
    }

    @Override
    public int getItemCount() {
        if (notes.size() == 0) {
            return 1;
        } else {
            return notes.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        if (notes.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        //如果有数据，则使用ITEM的布局
        return VIEW_TYPE_ITEM;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvTime;
        ImageView ivImage;
        ImageView ivHead;

        public MyViewHolder(View view) {
            super(view);
            tvUsername = view.findViewById(R.id.main_username);
            tvTime = view.findViewById(R.id.main_time);
            ivImage = view.findViewById(R.id.main_image);
            ivHead = view.findViewById(R.id.main_head);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if (onItemClickListener != null) {
                        int i = getLayoutPosition();
                        onItemClickListener.OnItemClick(v, i);

                    }
                }
            });
        }
    }

    //ItemClick的回调接口
    public interface OnItemClickListener {
        void OnItemClick(View v, int pos);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<noter> note) {
        //增加数据
        int position = notes.size();
        notes.addAll(position, note);
        notifyItemInserted(position);

    }

    public void flash(boolean mode) {
        if (mode) {
            notes = cacheInfo.moreNotes;
            cacheInfo.notes = cacheInfo.moreNotes;
        } else {
            notes = myCacheInfo.moreNotes;
            myCacheInfo.notes = cacheInfo.moreNotes;
        }
        int position = notes.size();
        notifyDataSetChanged();
    }

}
