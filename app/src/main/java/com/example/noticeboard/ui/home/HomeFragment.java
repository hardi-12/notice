package com.example.noticeboard.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.noticeboard.adapter.EventsAdapter;
import com.example.noticeboard.adapter.NoticeAdapter;
import com.example.noticeboard.R;
import com.example.noticeboard.notice;
import com.example.noticeboard.tabbedActivity.ViewPagerAdapter;
import com.example.noticeboard.tabbedActivity.fragment_one;
import com.example.noticeboard.tabbedActivity.fragment_three;
import com.example.noticeboard.tabbedActivity.fragment_two;
import com.example.noticeboard.ui.search.SearchFragment;
import com.example.noticeboard.web_view;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    public HomeFragment(){} // an empty public constructor required
    ArrayList<Integer> photos = new ArrayList<>();
    ViewFlipper viewFlipper;
//    DatabaseReference reference,reference_updates;
//    RecyclerView list_view, list_updates;
//    ArrayList<notice> itemlist, sortedList, updates,noticeList;
//    NoticeAdapter adapterClass;
    Button view_all, Result, Research, view_all_update;
    TextView no_events;
    TabLayout tabLayout;
    ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        //View Flipper Part
        viewFlipper = view.findViewById(R.id.viewFlipper);
        photos.add(R.drawable.slide_1);
        photos.add(R.drawable.slide_2);
        photos.add(R.drawable.slide_3);
        photos.add(R.drawable.slide_4);
        photos.add(R.drawable.slide_5);
        for (int i=0 ; i<photos.size();i++) {
            setFlipperImage(photos.get(i));
        }

//        reference= FirebaseDatabase.getInstance().getReference().child("notice");
//        reference.keepSynced(true);
//
//        reference_updates = FirebaseDatabase.getInstance().getReference().child("notice");
//        reference_updates.keepSynced(true);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        list_view=view.findViewById(R.id.list_view);
//        list_view.setLayoutManager(linearLayoutManager);
//
//        list_updates = view.findViewById(R.id.list_upcoming);
//        no_events = view.findViewById(R.id.no_events);
//
//        view_all = view.findViewById(R.id.view_all);
//        view_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment newFragment = new SearchFragment();
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
//
//        view_all_update = view.findViewById(R.id.view_all_events);
//        view_all_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: 22-07-2020 Create new activity for updates/events
//            }
//        });

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Result = view.findViewById(R.id.Result);
        Research = view.findViewById(R.id.Research);

        Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getContext(), web_view.class);
                k.putExtra("link","https://kjsieit.somaiya.edu/en/result");
                startActivity(k);
            }
        });

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragent(new fragment_one(), "one");
        viewPagerAdapter.addFragent(new fragment_two(), "two");
        viewPagerAdapter.addFragent(new fragment_three(), "three");
        viewPager.setAdapter(viewPagerAdapter);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    itemlist = new ArrayList<>();
//                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//                        notice n = child.getValue(notice.class);
//                        itemlist.add(n);
//                    }
//                    sortedList = new ArrayList<>();
//                    int j = 0;
//                    for (int i = itemlist.size() - 1 ; i >= 0 ; i--) {
//                        if(j<4) {
//                            sortedList.add(itemlist.get(i));
//                            j++;
//                        }
//                        else {
//                            break;
//                        }
//                    }
//                    adapterClass = new NoticeAdapter(sortedList);
//                    list_view.setAdapter(adapterClass);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        reference_updates.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                       noticeList = new ArrayList<>();
//                        for (DataSnapshot child : snapshot.getChildren()) {
//                            notice n = child.getValue(notice.class);
//                            assert n != null;
//                            if(n.getType().equals("Event Section")) {
//                                noticeList.add(n);
//                            }
//                        }
//                        if(noticeList.size() != 0) {
//                            LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
//                            list_updates.setLayoutManager(linearLayout);
//                            list_updates.setVisibility(View.VISIBLE);
//                            no_events.setVisibility(View.GONE);
//
//                            updates = new ArrayList<>();
//                            for (int i = noticeList.size() - 1 ; i >= 0 ; i--) {
//                                String date = noticeList.get(i).getDate();
//                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =  new SimpleDateFormat("dd-MM-yyyy"); // Jan-20-2015 1:30:55 PM
//                                Date d;
//                                Date d1;
//                                String today= getToday("dd-MM-yyyy");
//                                try {
//                                    d = sdf.parse(date);
//                                    d1 = sdf.parse(today);
//                                    assert d1 != null;
//                                    if(d1.compareTo(d) <0){// not expired
//                                        updates.add(noticeList.get(i));
//                                    }else {
//                                        assert d != null;
//                                        if(d.compareTo(d1)==0){// both date are same
//                                            updates.add(noticeList.get(i));
//                                        }
//
//                                    }
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            EventsAdapter eventsAdapter = new EventsAdapter(updates);
//                            list_updates.setAdapter(eventsAdapter);
//                        }
//                        else {
//                            list_updates.setVisibility(View.GONE);
//                            no_events.setVisibility(View.VISIBLE);
//                        }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void setFlipperImage(Integer integer) {
        ImageView image = new ImageView(getContext());
        image.setImageResource(integer);
        viewFlipper.addView(image);

        viewFlipper.setFlipInterval(2500);
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
