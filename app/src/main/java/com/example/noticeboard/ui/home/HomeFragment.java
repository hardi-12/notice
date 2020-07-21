package com.example.noticeboard.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.Dashboard;
import com.example.noticeboard.NoticeAdapter;
import com.example.noticeboard.R;
import com.example.noticeboard.notice;
import com.example.noticeboard.ui.search.SearchFragment;
import com.example.noticeboard.web_view;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    public HomeFragment(){} // an empty public constructor required
    ArrayList<Integer> photos = new ArrayList<>();
    ViewFlipper viewFlipper;
    DatabaseReference reference;
    RecyclerView list_view;
    ArrayList<notice> itemlist, sortedList;
    NoticeAdapter adapterClass;
    Button view_all, Result, Research;

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

        reference= FirebaseDatabase.getInstance().getReference().child("notice");
        reference.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list_view=view.findViewById(R.id.list_view);
        list_view.setLayoutManager(linearLayoutManager);

        view_all = view.findViewById(R.id.view_all);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new SearchFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Result = view.findViewById(R.id.Result);
        Research = view.findViewById(R.id.Research);

        Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getContext(), web_view.class);
                k.putExtra("link","https://myaccount.somaiya.edu/#/login");
                startActivity(k);
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
                    list_view.setAdapter(adapterClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
}
