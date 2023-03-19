package com.trendingnews.activity;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.trendingnews.R;
import com.trendingnews.adapter.MyAdapterMultiMedia;

import java.util.ArrayList;
import java.util.List;

public class ActivityMultiMedia extends AppCompatActivity {

    ImageView header_back;
    TextView header_title;


    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);


        header_back = findViewById(R.id.header_back);
        header_title = findViewById(R.id.header_title);
        String Title = getIntent().getStringExtra("Title");
        header_title.setText("" + Title);

        header_back.setOnClickListener(view -> {
            finish();
        });


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));
        tabLayout.addTab(tabLayout.newTab().setText("Podcast"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final MyAdapterMultiMedia adapter = new MyAdapterMultiMedia(this, getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
