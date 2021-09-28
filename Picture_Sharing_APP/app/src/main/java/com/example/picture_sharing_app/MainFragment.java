package com.example.picture_sharing_app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private String[] username = null;
    private String[] time = null;
    private List<noter> data;//要设置的数据
    private MomentsAdapter momentsAdapter = null;
    private RecyclerView recyclerView;
    private Context context = null;
    private SmartRefreshLayout refreshLayout;//刷新布局

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheInfo.finished = false;
        flashView(0, 0);
    }

    private static Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.comments_list);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshData();
        //下拉刷新
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        //上拉加载
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        //为下来刷新添加事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                flashView(0, 0);
                refreshData();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        //为上拉下载添加事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        return view;
    }

    private void flashView(int index, int option) {
        cacheInfo.lengths = null;
        cacheInfo.finished = false;
        cacheInfo.notes = null;
        Server.ThreadToServer(new flash(option), option);
    }

    private void refreshData() {
        //这里写刷新数据的方法
        new Thread() {
            @Override
            public void run() {
                super.run();
                int i = 10;
                try {
                    while (!cacheInfo.finished) {
                        sleep(1000);
                        i--;
                        if (i == 0) break;
                    }
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            momentsAdapter = new MomentsAdapter(context, R.layout.list_item, cacheInfo.notes);
                            recyclerView.setAdapter(momentsAdapter);
                            momentsAdapter.notifyDataSetChanged();
                            //监听item点击事件
                            momentsAdapter.setOnItemClickListener(new MomentsAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(View v, noter moments) {
                                    Toast.makeText(getActivity(), "我是item", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    };
                    handler.post(runnable);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
    }

}

