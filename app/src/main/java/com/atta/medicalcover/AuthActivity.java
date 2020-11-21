package com.atta.medicalcover;

import android.os.Bundle;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class AuthActivity extends AppCompatActivity {

    private ViewFragmentPagerAdapter adapter;

    ViewPager2 viewPager;

    TabLayout tabs;

    // tab titles
    private String[] titles = new String[]{"Register", "Login"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        viewPager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);

        adapter = new ViewFragmentPagerAdapter(this);

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabs, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();
    }
}