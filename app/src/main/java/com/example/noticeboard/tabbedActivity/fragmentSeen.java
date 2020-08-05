package com.example.noticeboard.tabbedActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.noticeboard.NoticeSeen;
import com.example.noticeboard.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragmentSeen extends Fragment {
    ListView lvSeen;
    ArrayList<String> seen_list = new ArrayList<>();

    public fragmentSeen() {}   // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seen, container, false);

        lvSeen = view.findViewById(R.id.lvSeen);

        String key = getActivity().getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference("seen").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seen_list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    String timeStamp = ds.child("timeStamp").getValue().toString();
                    seen_list.add(email+"\n"+timeStamp);
                }
                if (getActivity() != null) {
                    lvSeen.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, seen_list));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

        return view;
    }
}