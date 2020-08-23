package com.example.noticeboard.ui.viewAll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.NoticeAdapter;
import com.example.noticeboard.notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PriorityNoticeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    DatabaseReference reference, useref;
    RecyclerView list_view_priority;
    ArrayList<notice> itemlist, displayList;
    SearchView SearchBar_priority;
    ImageView ivPopup_priority;
    String sem;
    SwipeRefreshLayout refreshPriority;
    FirebaseUser user;

    public PriorityNoticeFragment() {}   // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_priority_notice, container, false);

        reference= FirebaseDatabase.getInstance().getReference().child("notice");

        SearchBar_priority = view.findViewById(R.id.SearchBar_priority);
        ivPopup_priority = view.findViewById(R.id.ivPopup_priority);
        refreshPriority = view.findViewById(R.id.refreshPriority);
        user = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        list_view_priority=view.findViewById(R.id.list_view_priority);
        list_view_priority.setLayoutManager(linearLayoutManager);

        ivPopup_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setOnMenuItemClickListener(PriorityNoticeFragment.this);
                popupMenu.inflate(R.menu.dashboard_filter);
                popupMenu.show();
            }
        });

        useref = FirebaseDatabase.getInstance().getReference("user");
        String em = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "_dot_");
        useref.child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sem = dataSnapshot.child("semester").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refreshPriority.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayNotice();
                refreshPriority.setRefreshing(false);
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
                        if (n.getType().equals("Exam Section")) {
                            itemlist.add(n);
                        }
                    }
                    displayList = new ArrayList<>();
                    for (int i = itemlist.size()-1 ;i >=0; i-- ) {
                        displayList.add(itemlist.get(i));
                    }
                    list_view_priority.setAdapter(new NoticeAdapter(displayList));
                }

                if(SearchBar_priority != null){
                    SearchBar_priority.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayNotice () {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    itemlist = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        notice n = child.getValue(notice.class);
                        if (n.getType().equals("Exam Section")) {
                            itemlist.add(n);
                        }
                    }
                    displayList = new ArrayList<>();
                    for (int i = itemlist.size()-1 ;i >=0; i-- ) {
                        displayList.add(itemlist.get(i));
                    }
                    list_view_priority.setAdapter(new NoticeAdapter(displayList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getContext(), "Error : "+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void search(String s) {
        ArrayList<notice> myList = new ArrayList();
        for (notice object : itemlist) {
            if(object.getTitle().toLowerCase().contains(s.toLowerCase()) /*|| object.getUpload().toLowerCase().contains(s.toLowerCase())*/) {
                myList.add(object);
            }
        }
        NoticeAdapter adapterClass = new NoticeAdapter(myList);
        list_view_priority.setAdapter(adapterClass);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        ArrayList<notice> myList = new ArrayList();
        switch (item.getItemId()) {
            case R.id.it_home_New_Old :
                displayNotice();
                break;

            case R.id.it_home_Old_New :
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            itemlist = new ArrayList<>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                notice n = child.getValue(notice.class);
                                if (n.getType().equals("Exam Section")) {
                                    itemlist.add(n);
                                }
                            }
                            list_view_priority.setAdapter(new NoticeAdapter(itemlist));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toasty.error(getContext(), "Error : "+databaseError, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.it_home_MySem :
                myList.clear();
                for (notice object : itemlist) {
                    if(object.getSem().equals(sem)) {
                        myList.add(object);
                    }
                }
                list_view_priority.setAdapter(new NoticeAdapter(myList));
                break;

            case R.id.it_home_MyNotice:
                myList.clear();
                for (notice object : itemlist) {
                    if(object.getUpload().equals(user.getEmail())) {
                        myList.add(object);
                    }
                }
                list_view_priority.setAdapter(new NoticeAdapter(myList));
                break;
        }
        return true;
    }
}