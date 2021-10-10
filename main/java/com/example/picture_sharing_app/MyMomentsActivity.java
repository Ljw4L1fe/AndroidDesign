package com.example.picture_sharing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MyMomentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MomentsAdapter momentsAdapter = null;
    private static int index = 0;
    private SmartRefreshLayout refreshLayout;//刷新布局
    private static Handler handler = new Handler();
    private TextView tvnopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moments);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mymoments_toolbar);
        setSupportActionBar(myToolbar);
        recyclerView = findViewById(R.id.my_comments_list);
        LinearLayoutManager llm = new LinearLayoutManager(MyMomentsActivity.this);
        recyclerView.setLayoutManager(llm);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {  //返回按钮点击事件
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout = findViewById(R.id.mymoments_refresh);
        LinearLayoutManager ll = new LinearLayoutManager(MyMomentsActivity.this);
        recyclerView.setLayoutManager(ll);
        flashView(index);//获取数据
        refreshData();
        //下拉刷新
        refreshLayout.setRefreshHeader(new ClassicsHeader(MyMomentsActivity.this));
        //上拉加载
        refreshLayout.setRefreshFooter(new ClassicsFooter(MyMomentsActivity.this));
        //为下来刷新添加事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //momentsAdapter=null;
                Server.ThreadToServer(new flash(0, false, User.userInfo.get("uid").getAsInt()), 3);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        int i = 10;
                        while (!myCacheInfo.moreFinished) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                            if (i == 0) break;
                        }
                        if (myCacheInfo.moreFinished) {
                            myCacheInfo.moreFinished = false;
                            index = 0;
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    momentsAdapter.flash(false);
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
                index += 5;
                Server.ThreadToServer(new flash(index, false, User.userInfo.get("uid").getAsInt()), 3);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        int i = 10;
                        while (!myCacheInfo.moreFinished) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i--;
                            if (i == 0) break;
                        }
                        if (myCacheInfo.moreFinished) {
                            myCacheInfo.moreFinished = false;
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    momentsAdapter.add(myCacheInfo.moreNotes);
                                    refreshlayout.finishLoadMore(0);//传入false表示加载失败
                                }
                            };
                            handler.post(runnable);
                        }
                    }
                }.start();


            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void flashView(int index) {
        Server.ThreadToServer(new flash(index, false, User.userInfo.get("uid").getAsInt()), 2);
    }

    private void refreshData() {
        //这里写刷新数据的方法
        new Thread() {
            @Override
            public void run() {
                super.run();
                int i = 10;
                try {
                    while (!myCacheInfo.finished) {
                        sleep(1000);
                        i--;
                        if (i == 0) break;
                    }
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            momentsAdapter = new MomentsAdapter(MyMomentsActivity.this, R.layout.list_item, myCacheInfo.notes);
                            recyclerView.setAdapter(momentsAdapter);
                            //监听item点击事件

                            momentsAdapter.setOnItemClickListener(new MomentsAdapter.OnItemClickListener() {

                                @Override
                                public void OnItemClick(View v, int pos) {
                                    Intent i = new Intent(MyMomentsActivity.this, MomentActivity.class);
                                    i.putExtra("pos", pos);
                                    i.putExtra("mode", false);
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
    }
}