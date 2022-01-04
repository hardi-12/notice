package com.kjsieit.noticeboard.ui.resources;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.adapter.ResourceAdapter;
import com.kjsieit.noticeboard.models.resource;

import java.util.ArrayList;

public class Resources extends AppCompatActivity {

    SearchView searchView_resource;
    RecyclerView list_view_resource;
    DatabaseReference databaseReference;
    ArrayList<resource> itemlist = new ArrayList<>();
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
//        getSupportActionBar().setTitle("");

        searchView_resource = findViewById(R.id.SearchBar_resource);
        list_view_resource = findViewById(R.id.list_view_resource);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Resources.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        list_view_resource.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("References");
        code = getIntent().getStringExtra("code");
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    itemlist = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        resource n = child.getValue(resource.class);
                        if (n.getSubject().equals(code))
                            itemlist.add(n);
                    }
                    list_view_resource.setAdapter(new ResourceAdapter(itemlist, Resources.this));
                }
                if(searchView_resource != null){
                    searchView_resource.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            search(query);
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            search(newText);
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void search(String query) {
        ArrayList<resource> myList = new ArrayList<>();
        for (resource object : itemlist) {
            if(object.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    object.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                    object.getPublication().toLowerCase().contains(query.toLowerCase()) ||
                    object.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                    object.getSubject().toLowerCase().contains(query.toLowerCase())) {
                myList.add(object);
            }
        }
        ResourceAdapter adapterClass = new ResourceAdapter(myList, Resources.this);
        list_view_resource.setAdapter(adapterClass);
    }
}