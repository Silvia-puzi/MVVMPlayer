package com.example.administrator.mvvmplayer.viewModule;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.adapter.ViewPagerAdapter;
import com.example.administrator.mvvmplayer.databinding.ActivityMainBinding;
import com.example.administrator.mvvmplayer.view.fragment.MusicFragment1;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.viewModule
 *  @文件名:   MainViewModule
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/3 10:48
 *  @描述：    TODO
 */
public class MainViewModule {
    private static final String TAG = "MainViewModule";

    private ActivityMainBinding mMainBinding;
    private AppCompatActivity mActivity;

    public MainViewModule(ActivityMainBinding mainBinding, AppCompatActivity activity) {
        this.mMainBinding = mainBinding;
        this.mActivity = activity;
        init();
    }

    public void init(){
        final DrawerLayout mainDrawer = mMainBinding.mainDrawer;
        Toolbar mainToolbar = mMainBinding.mainToolbar;
        NavigationView navigationView = mMainBinding.navigationView;
        TabLayout mainTablayout = mMainBinding.mainTablayout;
        ViewPager mainViewpager = mMainBinding.mainViewpager;


        mainToolbar.setTitle("Listen");
        mainToolbar.setNavigationIcon(R.drawable.music_player);
        mActivity.setSupportActionBar(mainToolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mainDrawer.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_page:
                        break;
                    case R.id.nav_favorite:
                        break;
                    case R.id.nav_logout:
                        break;
                    case R.id.nav_name:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(mActivity.getSupportFragmentManager());
        adapter.addFragment(new MusicFragment1(), "Tab 1");
        adapter.addFragment(new MusicFragment1(), "Tab 2");
        adapter.addFragment(new MusicFragment1(), "Tab 3");
        mainViewpager.setAdapter(adapter);

        mainTablayout.setupWithViewPager(mainViewpager);

    }




}
