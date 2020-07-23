package com.example.noticeboard.tabbedActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.NoticeAdapter;
import com.example.noticeboard.notice;
import com.example.noticeboard.ui.search.SearchFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_one extends Fragment {

    DatabaseReference reference,reference_updates;
    RecyclerView list_view_one, list_updates;
    ArrayList<notice> itemlist, sortedList, updates,noticeList;
    NoticeAdapter adapterClass;
    Button view_all_one;

    public fragment_one() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        reference= FirebaseDatabase.getInstance().getReference().child("notice");
        reference.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list_view_one=view.findViewById(R.id.list_view_one);
        list_view_one.setLayoutManager(linearLayoutManager);

        view_all_one = view.findViewById(R.id.view_all_one);
        view_all_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new SearchFragment();
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
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    itemlist = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        notice n = child.getValue(notice.class);
                        itemlist.add(n);
                    }
                    sortedList = new ArrayList<>();
                    int j = 0;
                    for (int i = itemlist.size() - 1 ; i >= 0 ; i--) {
                        if(j<4) {
                            sortedList.add(itemlist.get(i));
                            j++;
                        }
                        else {
                            break;
                        }
                    }
                    adapterClass = new NoticeAdapter(sortedList);
                    list_view_one.setAdapter(adapterClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}