package com.asuscomm.yangyinetwork.boilerplate;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.asuscomm.yangyinetwork.boilerplate.databinding.ActivityMainBinding;
import com.asuscomm.yangyinetwork.boilerplate.viewmodel.MainViewModel;
import com.lge.hardware.IRBlaster.IRBlaster;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MainViewModel mViewModel;
    private IRBlaster mIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setMainviewmodel(mViewModel);
        mViewModel.attachView(binding);
        mViewModel.setContext(this);
    }
}
