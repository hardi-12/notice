package com.kjsieit.noticeboard.ui.resources;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.resourceSubject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ResourceSubject extends AppCompatActivity {

    DatabaseReference reference;
    String sem, dept;
    ListView lvSubjects;
    ArrayList<String> subjectList = new ArrayList<>(), codeList = new ArrayList<>();
    String user = FirebaseAuth.getInstance().getCurrentUser().getEmail(), type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_subject);
        reference = FirebaseDatabase.getInstance().getReference("resourceSubjects");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ResourceSubject.this, android.R.layout.simple_list_item_1, subjectList);
        lvSubjects = findViewById(R.id.lvSubjects);
        lvSubjects.setAdapter(adapter);

        Intent i = getIntent();
        sem = i.getStringExtra("sem");
        dept = i.getStringExtra("dept");
        getSupportActionBar().setTitle("Select Subject - "+dept+" "+sem);

        FirebaseDatabase.getInstance().getReference("user").child(user.replace(".", "_dot_")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                type = snapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                resourceSubject subject = snapshot.getValue(resourceSubject.class);
                if (subject.getSubjectDept().equals(dept) && subject.getSubjectSem().equals(sem)) {
                    subjectList.add(subject.getSubjectTitle()+" - ("+subject.getSubjectCode()+")");
                    codeList.add(subject.getSubjectCode());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ResourceSubject.this, Resources.class).putExtra("code", codeList.get(position)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (type.equals("admin"))
            getMenuInflater().inflate(R.menu.resource_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResourceSubject.this);
        View view = LayoutInflater.from(ResourceSubject.this).inflate(R.layout.dialog_box_resource_subject,null);
        EditText etSubjectTitle, etSubjectCode;
        etSubjectTitle = view.findViewById(R.id.etSubjectTitle);
        etSubjectCode = view.findViewById(R.id.etSubjectCode);
        builder.setView(view).setMessage("").setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = etSubjectTitle.getText().toString(),
                                code = etSubjectCode.getText().toString();
                        if (title.isEmpty() || code.isEmpty())
                            Toasty.error(ResourceSubject.this ,"Provide data", Toasty.LENGTH_SHORT).show();
                        else {
                            resourceSubject subject = new resourceSubject(title, code, sem, dept);
                            reference.child(String.valueOf(System.currentTimeMillis())).setValue(subject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toasty.success(ResourceSubject.this ,"Subject created", Toasty.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toasty.error(ResourceSubject.this ,"Subject creation failed", Toasty.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return super.onOptionsItemSelected(item);
    }
}