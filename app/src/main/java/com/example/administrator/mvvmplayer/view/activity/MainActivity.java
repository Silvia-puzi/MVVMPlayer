package com.example.administrator.mvvmplayer.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.databinding.ActivityMainBinding;
import com.example.administrator.mvvmplayer.viewModule.MainViewModule;

public class MainActivity extends AppCompatActivity {

//    private User mUser;
    private ActivityMainBinding mMainBinding;
    private MainViewModule mMainViewModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //创建ViewModel
        mMainViewModule = new MainViewModule(mMainBinding,this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMainBinding.mainDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
