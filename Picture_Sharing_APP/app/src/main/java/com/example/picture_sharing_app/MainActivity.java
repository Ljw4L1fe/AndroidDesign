package com.example.picture_sharing_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        //构建导航id  menu 与 navigation中的id要相对应
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_person)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //与navcontroller相关联
        NavigationUI.setupWithNavController(navView, navController);
        //设置BottomNavigationView点击事件
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //跳转相应fragment
                navController.navigate(menuItem.getItemId());
                //返回false会有一个点击悬浮效果，返回true则不会有该效果
                return false;
            }
        });
        int flag = getIntent().getIntExtra("flag", 0);
        int flagg = getIntent().getIntExtra("flagg", 1);
        if (flag == 2 || flagg == 2) {
            navController.navigate(flag);
        }
    }
}