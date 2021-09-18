package com.example.picture_sharing_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.ViewHolder> {
    private List<Moments> mMomentData;
    private Context mContext;
    private int resourceId;

    public MomentsAdapter(@NonNull Context context, int resourceId, List<Moments> data) {
        this.mContext = context;
        this.mMomentData = data;
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
        Moments moment = mMomentData.get(position);
        holder.tvTime.setText(moment.getmTime());
        holder.tvUsername.setText(moment.getmUsername());

        if (moment.getmImageId() != -1) {
            holder.ivImage.setImageResource(moment.getmImageId());
            holder.ivHead.setImageResource(moment.getmImageId());
        }
    }

    @Override
    public int getItemCount() {
        return mMomentData.size();
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
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mMomentData.get(getLayoutPosition()));
                    }
                }
            });

        }
    }
    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, Moments moments);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
