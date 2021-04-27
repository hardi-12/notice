package com.kjsieit.noticeboard.tabbedActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kjsieit.noticeboard.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragmentUnseen extends Fragment {

    ListView lvUnseen;
    ArrayList<String> seen_list = new ArrayList<>(), unseen_list = new ArrayList<>();

    public fragmentUnseen() {}  // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unseen, container, false);

        lvUnseen = view.findViewById(R.id.lvUnseen);

        String key = getActivity().getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference("seen").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seen_list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    seen_list.add(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

        FirebaseDatabase.getInstance().getReference("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unseen_list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    if (!seen_list.contains(email)) {
                        unseen_list.add(email);
                    }
                }
                lvUnseen.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, unseen_list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}