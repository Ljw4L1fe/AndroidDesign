package com.example.picture_sharing_app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private String[] username = null;
    private String[] time = null;
    private List<Moments> commentsList = new ArrayList<>();
    private MomentsAdapter momentsAdapter = null;
    private RecyclerView recyclerView;
    private Context context = null;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.comments_list);
        momentsAdapter = new MomentsAdapter(context, R.layout.list_item, commentsList);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(momentsAdapter);
        //监听item点击事件
        momentsAdapter.setOnItemClickListener(new MomentsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, Moments moments) {
                Toast.makeText(getActivity(),"我是item", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void initData() {
        int length;
        username = getResources().getStringArray(R.array.username);
        time = getResources().getStringArray(R.array.time);
        TypedArray images = getResources().obtainTypedArray(R.array.image);
        TypedArray heads = getResources().obtainTypedArray(R.array.head);
        if (username.length > time.length) {
            length = time.length;
        } else {
            length = username.length;
        }
        for (int i = 0; i < length; i++) {
            Moments moments = new Moments();
            moments.setmUsername(username[i]);
            moments.setmTime(time[i]);
            moments.setmImageId(images.getResourceId(i, 0));
            moments.setmImageId(heads.getResourceId(i, 0));
            commentsList.add(moments);
        }
    }

}