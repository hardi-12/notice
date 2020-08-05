package com.example.noticeboard.ui.superAdmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.noticeboard.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class superAdminFragment extends Fragment {

    ListView lvsuperAdmin;
    Button btnUpdate;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
    ArrayList<String> emailList = new ArrayList<>();
    ArrayList<String> superAdminList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public superAdminFragment() {}   // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_super_admin, container, false);

        lvsuperAdmin = view.findViewById(R.id.lvsuperAdmin);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("type").getValue().toString().equals("admin")) {
                        String email = ds.child("email").getValue().toString();
                        String superAdmin = ds.child("superAdmin").getValue().toString();
                        emailList.add(email);
                        superAdminList.add(superAdmin);
                    }
                }
                adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_multiple_choice, emailList);
                adapter.notifyDataSetChanged();
                lvsuperAdmin.setAdapter(adapter);
                for (int i = 0; i < emailList.size(); i++) {
                    if (superAdminList.get(i).equals("true")) {
                        lvsuperAdmin.setItemChecked(i, true);
                    }
                    else {
                        lvsuperAdmin.setItemChecked(i, false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < emailList.size(); i++) {
                    if (lvsuperAdmin.isItemChecked(i)) {
                        reference.child(emailList.get(i).replace(".", "_dot_")).child("superAdmin").setValue("true");
                    }
                    else reference.child(emailList.get(i).replace(".", "_dot_")).child("superAdmin").setValue("false");
                }
            }
        });
        return view;
    }
}