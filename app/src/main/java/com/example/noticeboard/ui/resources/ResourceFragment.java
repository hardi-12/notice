package com.example.noticeboard.ui.resources;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.ResourceAdapter;
import com.example.noticeboard.models.resource;
import com.example.noticeboard.ui.viewAll.EventsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResourceFragment extends Fragment {
    public ResourceFragment() {}

    SearchView searchView_resource;
    RecyclerView list_view_resource;
    DatabaseReference databaseReference;
    ArrayList<resource> itemlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        searchView_resource = view.findViewById(R.id.SearchBar_resource);
        list_view_resource = view.findViewById(R.id.list_view_resource);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        list_view_resource.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("References");
        return view;
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
                        itemlist.add(n);

                    }
                    list_view_resource.setAdapter(new ResourceAdapter(itemlist, getContext() ));
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
        ResourceAdapter adapterClass = new ResourceAdapter(myList, getContext());
        list_view_resource.setAdapter(adapterClass);
    }

}
