package com.trendingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.trendingnews.fragment.FragmentBriefing;
import com.trendingnews.fragment.FragmentHome;
import com.trendingnews.R;
import com.trendingnews.fragment.FragmentPremium;
import com.trendingnews.fragment.FragmentTrending;

import io.ak1.BubbleTabBar;

public class ActivityMain extends AppCompatActivity {

    BubbleTabBar bubbleTabBar;

    ImageView img_search_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        img_search_helper = findViewById(R.id.img_search_helper);

        img_search_helper.setOnClickListener(view -> {
            startActivity(new Intent(ActivityMain.this, ActivitySearchArticle.class));
        });


        bubbleTabBar.addBubbleListener(i -> {
            switch (i) {
                case R.id.navigation_home:
                    pushFragment(new FragmentHome());
                    break;
                case R.id.navigation_briefing:
                    pushFragment(new FragmentBriefing());
                    break;
                case R.id.navigation_trending:
                    pushFragment(new FragmentTrending());
                    break;
                case R.id.navigation_premium:
                    pushFragment(new FragmentPremium());
                    break;

            }
        });
        pushFragment(new FragmentHome());
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }

        }
    }


}
