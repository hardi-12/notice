package com.example.noticeboard.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.UserListAdapter;
import com.example.noticeboard.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    public ListFragment() {}// Required empty public constructor
    RecyclerView lvUsers;
    ArrayList<user> users_list;
    ArrayList<user> list = new ArrayList<>();
    UserListAdapter arrayAdapter;
    DatabaseReference reference;
    SearchView SearchBar_users;
    ImageView ivPopup_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        lvUsers = view.findViewById(R.id.lvUsers);
        SearchBar_users = view.findViewById(R.id.SearchBar_users);
        ivPopup_users = view.findViewById(R.id.ivPopup_users);
        lvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        reference = FirebaseDatabase.getInstance().getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users_list = new ArrayList<>();
                users_list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    users_list.add(ds.getValue(user.class));
                }
                arrayAdapter = new UserListAdapter(users_list);
                lvUsers.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivPopup_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setOnMenuItemClickListener(ListFragment.this);
                popupMenu.inflate(R.menu.user_filter);
                popupMenu.show();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(SearchBar_users != null){
            SearchBar_users.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<user> list = new ArrayList<>();
                    for (user abc : users_list) {
                        if (abc.getName().toLowerCase().contains(newText.toLowerCase())) {
                            list.add(abc);
                        }
                    }
                    lvUsers.setAdapter(new UserListAdapter(list));
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_user_admins:
                list.clear();
                for (user admin : users_list) {
                    if (admin.getType().equals("admin")) {
                        list.add(admin);
                    }
                }
                lvUsers.setAdapter(new UserListAdapter(list));
                break;

            case R.id.it_user_students:
                list.clear();
                for (user student : users_list) {
                    if (student.getType().equals("student")) {
                        list.add(student);
                    }
                }
                lvUsers.setAdapter(new UserListAdapter(list));
                break;

            case R.id.it_user_all:
                lvUsers.setAdapter(new UserListAdapter(users_list));
                break;
        }
        return true;
    }
}
