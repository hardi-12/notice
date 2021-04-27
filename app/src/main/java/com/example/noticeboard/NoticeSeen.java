package com.example.noticeboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.noticeboard.adapter.SeenUnseenAdapter;
import com.example.noticeboard.tabbedActivity.fragmentSeen;
import com.example.noticeboard.tabbedActivity.fragmentUnseen;
import com.google.android.material.tabs.TabLayout;

public class NoticeSeen extends AppCompatActivity {

    TabLayout tabLayoutNotice;
    ViewPager viewPagerNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_seen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        tabLayoutNotice = findViewById(R.id.tabLayoutNotice);
        viewPagerNotice = findViewById(R.id.viewPagerNotice);
        setupViewPager(viewPagerNotice);
        tabLayoutNotice.setupWithViewPager(viewPagerNotice);
    }

    private void setupViewPager(ViewPager viewPager) {
        SeenUnseenAdapter seenUnseenAdapter = new SeenUnseenAdapter(getSupportFragmentManager());
        seenUnseenAdapter.addFragent(new fragmentSeen(), "Seen");
        seenUnseenAdapter.addFragent(new fragmentUnseen(), "Unseen");
        viewPager.setAdapter(seenUnseenAdapter);
    }
}