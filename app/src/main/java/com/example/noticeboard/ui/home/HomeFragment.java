package com.example.noticeboard.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.ViewPagerAdapter;
import com.example.noticeboard.tabbedActivity.EventFragmentJsoup;
import com.example.noticeboard.tabbedActivity.fragment_one;
import com.example.noticeboard.tabbedActivity.fragment_two;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    public HomeFragment(){} // an empty public constructor required
    ArrayList<Integer> photos = new ArrayList<>();
    ViewFlipper viewFlipper;
    TabLayout tabLayout;
    ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewFlipper = view.findViewById(R.id.viewFlipper);
        photos.add(R.drawable.slide_1);
        photos.add(R.drawable.slide_2);
        photos.add(R.drawable.slide_3);
        photos.add(R.drawable.slide_4);
        photos.add(R.drawable.slide_5);
        for (int i=0 ; i<photos.size();i++) {
            setFlipperImage(photos.get(i));
        }

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragent(new fragment_one(), "new updates");
        viewPagerAdapter.addFragent(new fragment_two(), "priority notices");
        viewPagerAdapter.addFragent(new EventFragmentJsoup(), "events");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setFlipperImage(Integer integer) {
        ImageView image = new ImageView(getContext());
        image.setImageResource(integer);
        viewFlipper.addView(image);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(getContext(),R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(),R.anim.slide_out_right);
    }
    @SuppressLint("SimpleDateFormat")
    public static String getToday(String format){
        Date date = new Date();
        return new SimpleDateFormat(format).format(date);
    }
}
