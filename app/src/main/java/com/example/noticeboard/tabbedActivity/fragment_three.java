package com.example.noticeboard.tabbedActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.EventsAdapter;
import com.example.noticeboard.models.notice;
import com.example.noticeboard.ui.viewAll.EventsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.noticeboard.ui.home.HomeFragment.getToday;

public class fragment_three extends Fragment {

    DatabaseReference reference_updates;
    RecyclerView list_updates;
    ArrayList<notice> updates,noticeList;
    TextView no_events;
    Button view_all_events;

    public fragment_three() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        reference_updates = FirebaseDatabase.getInstance().getReference().child("notice");
        reference_updates.keepSynced(true);
        list_updates = view.findViewById(R.id.list_upcoming);
        no_events = view.findViewById(R.id.no_events);
        view_all_events = view.findViewById(R.id.view_all_events);
        view_all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new EventsFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference_updates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    noticeList = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        notice n = child.getValue(notice.class);
                        assert n != null;
                        if (n.getType().equals("Event Section")) {
                            noticeList.add(n);
                        }
                    }
                    if (noticeList.size() != 0) {
                        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
                        list_updates.setLayoutManager(linearLayout);
                        list_updates.setVisibility(View.VISIBLE);
                        no_events.setVisibility(View.GONE);

                        updates = new ArrayList<>();
                        for (int i = noticeList.size() - 1; i >= 0; i--) {
                            String date = noticeList.get(i).getDate();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Jan-20-2015 1:30:55 PM
                            Date d;
                            Date d1;
                            String today = getToday("dd-MM-yyyy");
                            try {
                                d = sdf.parse(date);
                                d1 = sdf.parse(today);
                                assert d1 != null;
                                if (d1.compareTo(d) < 0) {// not expired
                                    updates.add(noticeList.get(i));
                                } else {
                                    assert d != null;
                                    if (d.compareTo(d1) == 0) {// both date are same
                                        updates.add(noticeList.get(i));
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        EventsAdapter eventsAdapter = new EventsAdapter(updates);
                        list_updates.setAdapter(eventsAdapter);
                    } else {
                        list_updates.setVisibility(View.GONE);
                        no_events.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}