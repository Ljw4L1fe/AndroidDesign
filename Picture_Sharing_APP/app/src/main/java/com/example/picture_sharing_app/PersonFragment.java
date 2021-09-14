package com.example.picture_sharing_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PersonFragment extends Fragment {
    private String[] username = null;
    private String[] time = null;
    private List<Moments> commentsList = new ArrayList<>();
    private MomentsAdapter momentsAdapter = null;
    private RecyclerView recyclerView;
    private Context context = null;
    private Button btndesignprofile;
    private ImageView ivsetting;

    public PersonFragment() {
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
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        btndesignprofile=view.findViewById(R.id.btn_person_profile);
        recyclerView = view.findViewById(R.id.person_comments_list);
        ivsetting=view.findViewById(R.id.person_gotosetting);
        momentsAdapter = new MomentsAdapter(context, R.layout.list_item, commentsList);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(momentsAdapter);
        btndesignprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                startActivity(i);
            }
        });
        ivsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SettingActivity.class);
                startActivity(i);
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