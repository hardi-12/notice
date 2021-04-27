package com.kjsieit.noticeboard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kjsieit.noticeboard.adapter.NoticeFilesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoticeFiles extends AppCompatActivity {
    RecyclerView rvFiles;
    DatabaseReference reference;
    TextView tvNoFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_files);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        rvFiles = findViewById(R.id.rvFiles);
        reference = FirebaseDatabase.getInstance().getReference("notice");
        tvNoFiles = findViewById(R.id.tvNoFiles);

        final String key = getIntent().getStringExtra("key");

        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("files")) {
                    tvNoFiles.setVisibility(View.GONE);
                    long size = snapshot.child("files").getChildrenCount();
                    for (int i = 0; i < size; i++) {
                        String url = snapshot.child("files").child("File "+i).getValue().toString();
                        ((NoticeFilesAdapter)rvFiles.getAdapter()).update("Attachment "+(i+1), url);
                    }
                }
                else tvNoFiles.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rvFiles.setLayoutManager(new LinearLayoutManager(this));
        NoticeFilesAdapter noticeFilesAdapter = new NoticeFilesAdapter(rvFiles, getApplicationContext(), new ArrayList<String>(), new ArrayList<String>());
        rvFiles.setAdapter(noticeFilesAdapter);
    }
}