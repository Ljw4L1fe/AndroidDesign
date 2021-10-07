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

public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.ViewHolder> {
    private static List<noter> notes;
    private Context mContext;
    private int resourceId;

    public MomentsAdapter(@NonNull Context context, int resourceId, List<noter> data) {
        this.mContext = context;
        this.notes = data;
        this.resourceId = resourceId;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //绑定数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        noter not = notes.get(position);
        String[] time=not.space.split("_");
        holder.tvTime.setText(time[0]+"/"+time[1]+"/"+time[2]);
        Bitmap headBit = BitmapFactory.decodeByteArray(not.headByte, 0, not.headByte.length);
        holder.ivHead.setImageBitmap(headBit);
        holder.tvUsername.setText(not.author);
        Bitmap bit = BitmapFactory.decodeByteArray(not.imageByte, 0, not.imageByte.length);
        holder.ivImage.setImageBitmap(bit);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvTime;
        ImageView ivImage;
        ImageView ivHead;

        public ViewHolder(View view) {
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
                        int i=getLayoutPosition();
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
        System.out.println("position:"+position );
        notes.addAll(position, note);
        notifyItemInserted(position);

    }
    public void flash(){
        notes=cacheInfo.moreNotes;
        int position = notes.size();
        System.out.println("position:"+position );
        notifyDataSetChanged();
    }

}
