package com.example.picture_sharing_app;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
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
    private MomentsAdapter momentsAdapter = null;
    private RecyclerView recyclerView;
    private Context context = null;
    private SmartRefreshLayout refreshLayout ;//刷新布局
    private static int index=0;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        System.out.println("fresh one time");
        if(!cacheInfo.finished) {
            flashView(index);
        }else {
            refreshData();
        }


        //下拉刷新
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        //上拉加载
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        //为下来刷新添加事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //momentsAdapter=null;
                Server.ThreadToServer(new flash(0,true,-1),1);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        int i=10;
                        while (!cacheInfo.moreFinished) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                            if (i == 0) break;
                        }
                        if(cacheInfo.moreFinished){
                            cacheInfo.moreFinished=false;
                            index=0;
                            Runnable runnable=new Runnable() {
                                @Override
                                public void run() {
                                    momentsAdapter.flash(true);
                                    refreshlayout.finishRefresh(0/*,false*/);//传入false表示刷新失败
                                }
                            };
                            handler.post(runnable);
                        }
                    }
                }.start();

            }
        });
        //为上拉下载添加事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                index+=5;
                Server.ThreadToServer(new flash(index,true,-1),1);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        int i=10;
                        while (!cacheInfo.moreFinished) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                            if (i == 0) break;
                        }
                        if(cacheInfo.moreFinished){
                            cacheInfo.moreFinished=false;
                            Runnable runnable=new Runnable() {
                                @Override
                                public void run() {
                                    momentsAdapter.add(cacheInfo.moreNotes);
                                    refreshlayout.finishLoadMore(0);//传入false表示加载失败
                                }
                            };
                            handler.post(runnable);
                        }
                    }
                }.start();


            }
        });


        return view;
    }

    private void flashView(int index) {
        Server.ThreadToServer(new flash(index,true,-1),0);
        refreshData();
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
                            //监听item点击事件

                            momentsAdapter.setOnItemClickListener(new MomentsAdapter.OnItemClickListener() {

                                @Override
                                public void OnItemClick(View v, int pos) {
                                    Toast.makeText(getActivity(),pos+"", Toast.LENGTH_SHORT).show();//获取item的用户名数据
                                    Intent i = new Intent(context,MomentActivity.class);
                                    i.putExtra("pos",pos);
                                    i.putExtra("mode",true);
                                    startActivity(i);
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
        // LinearLayoutManager llm = new LinearLayoutManager(context);
        //recyclerView.setLayoutManager(llm);
    }

}

