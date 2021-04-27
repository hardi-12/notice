package com.kjsieit.noticeboard.ui.viewAll;

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

import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.adapter.NoticeAdapter;
import com.kjsieit.noticeboard.models.notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class NewUpdatesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    public NewUpdatesFragment(){} // an empty public constructor required
    DatabaseReference reference, useref;
    RecyclerView list_view;
    ArrayList<notice> itemlist, displayList;
    SearchView searchView_home;
    ImageView ivPopup_home;
    NoticeAdapter adapterClass;
    String sem;
    SwipeRefreshLayout refresh;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_updates, container, false);

        reference= FirebaseDatabase.getInstance().getReference().child("notice");

        searchView_home = view.findViewById(R.id.SearchBar_home);
        ivPopup_home = view.findViewById(R.id.ivPopup_home);
        refresh = view.findViewById(R.id.refresh);
        user = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list_view=view.findViewById(R.id.list_view);
        list_view.setLayoutManager(linearLayoutManager);

        ivPopup_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setOnMenuItemClickListener(NewUpdatesFragment.this);
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

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayNotice();
                refresh.setRefreshing(false);
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
                    displayList = new ArrayList<>();
                    for (int j = itemlist.size()-1; j >= 0; j--) {
                        displayList.add(itemlist.get(j));
                    }
                    NoticeAdapter adapterClass = new NoticeAdapter(displayList);
                    list_view.setAdapter(adapterClass);
                }

                if(searchView_home != null){
                    searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void displayNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    itemlist = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        notice n = child.getValue(notice.class);
                        itemlist.add(n);
                    }
                    displayList = new ArrayList<>();
                    for (int j = itemlist.size()-1; j >= 0; j--) {
                        displayList.add(itemlist.get(j));
                    }
                    NoticeAdapter adapterClass = new NoticeAdapter(displayList);
                    list_view.setAdapter(adapterClass);
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
            if(object.getTitle().toLowerCase().contains(s.toLowerCase()) || object.getUpload().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        NoticeAdapter adapterClass = new NoticeAdapter(myList);
        list_view.setAdapter(adapterClass);
    }

    private String semConvert(String sem) {
        int sum = 0;
        for (int i = 0; i < sem.length(); i++) {
            char c = sem.charAt(i);
            switch (c) {
                case 'I': sum = sum + 1; break;
                case 'V': sum = sum + 5; break;
            }
        }
        return String.valueOf(sum);
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
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
                                itemlist.add(n);
                            }
                            NoticeAdapter adapterClass = new NoticeAdapter(itemlist);
                            list_view.setAdapter(adapterClass);
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
//                    if(object.getSem().equals(sem)) {
//                        myList.add(object);
//                    }
                    if (object.getSem().contains(semConvert(sem)))
                        myList.add(object);
                }
                list_view.setAdapter(new NoticeAdapter(myList));
                break;

            case R.id.it_home_MyNotice:
                myList.clear();
                for (notice object : itemlist) {
                    if(object.getUpload().equals(user.getEmail())) {
                        myList.add(object);
                    }
                }
                list_view.setAdapter(new NoticeAdapter(myList));
                break;
        }
        return true;
    }
}
