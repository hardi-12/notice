package com.kjsieit.noticeboard.noticeTypes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.resource;
import com.kjsieit.noticeboard.models.resourceSubject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import es.dmoral.toasty.Toasty;

public class AddResources extends AppCompatActivity {

    TextView tvResFile;
    TextInputEditText tvResTitle, tvResAuthor, tvResPublication, tvResDescription, tvResLink;
    TextInputLayout link, author, publication;
    DatabaseReference reference, subRefeence;
    Toolbar toolbar;
    Button btnResFile;
    AlertDialog.Builder builder;
    StorageReference storageReference;
    String filename = System.currentTimeMillis()+"";
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> savedList = new ArrayList<>(), codeList;
    int counter = 0;
    StringBuilder stringBuilder;
    ToggleSwitch toggleSwitch;
    Spinner spResDept, spResSem, spResSub;
    LinkedHashSet<String> semLHS, subTitleLHS, subCodeLHS;
    ArrayAdapter<String> semAdapter, subAdapter;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resources);

        tvResTitle = findViewById(R.id.tvResTitle);
        tvResAuthor = findViewById(R.id.tvResAuthor);
        tvResPublication = findViewById(R.id.tvResPublication);
        tvResDescription = findViewById(R.id.tvResDescription);
        tvResLink = findViewById(R.id.tvResLink);
        btnResFile = findViewById(R.id.btnResFile);
        tvResFile = findViewById(R.id.tvResFile);
        link = findViewById(R.id.link);
        author = findViewById(R.id.author);
        publication = findViewById(R.id.publication);
        toggleSwitch = findViewById(R.id.switchResourceType);

        spResDept = findViewById(R.id.spResDept);
        spResSem = findViewById(R.id.spResSem);
        spResSub = findViewById(R.id.spResSub);
        subRefeence = FirebaseDatabase.getInstance().getReference("resourceSubjects");

        spResDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spResDept.getSelectedItemPosition() > 0) {
                    semLHS = new LinkedHashSet<String>() {{ add("Select Semester"); }};
                    String dept = spResDept.getSelectedItem().toString();
                    subRefeence.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                resourceSubject subject = child.getValue(resourceSubject.class);
                                assert subject != null;
                                if (subject.getSubjectDept().equals(dept)) {
                                    semLHS.add(subject.getSubjectSem());
                                }
                            }
                            semAdapter = new ArrayAdapter<>(AddResources.this, R.layout.resource_spinner, new ArrayList<>(semLHS));
                            spResSem.setVisibility(View.VISIBLE);
                            spResSem.setAdapter(semAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    spResSem.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spResSem.setVisibility(View.GONE);
            }
        });

        spResSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spResSem.getSelectedItemPosition() > 0) {
                    subTitleLHS = new LinkedHashSet<String>() {{ add("Select Subject"); }};
                    subCodeLHS = new LinkedHashSet<String>() {{ add("Select Code"); }};
                    String dept = spResDept.getSelectedItem().toString(), sem = spResSem.getSelectedItem().toString();
                    subRefeence.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                resourceSubject subject = child.getValue(resourceSubject.class);
                                assert subject != null;
                                if (subject.getSubjectDept().equals(dept) && subject.getSubjectSem().equals(sem)) {
                                    subTitleLHS.add(subject.getSubjectTitle());
                                    subCodeLHS.add(subject.getSubjectCode());
                                }
                            }
                            subAdapter = new ArrayAdapter<>(AddResources.this, R.layout.resource_spinner, new ArrayList<>(subTitleLHS));
                            spResSub.setVisibility(View.VISIBLE);
                            spResSub.setAdapter(subAdapter);
                            codeList = new ArrayList<>(subCodeLHS);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    spResSub.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spResSub.setVisibility(View.GONE);
            }
        });

        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                switch (position){
                    case 0:
                        author.setVisibility(View.VISIBLE);
                        publication.setVisibility(View.VISIBLE);
                        btnResFile.setVisibility(View.VISIBLE);
                        tvResFile.setVisibility(View.VISIBLE);
                        link.setVisibility(View.GONE);
                        break;
                    case 1:
                        author.setVisibility(View.GONE);
                        publication.setVisibility(View.GONE);
                        btnResFile.setVisibility(View.VISIBLE);
                        tvResFile.setVisibility(View.VISIBLE);
                        link.setVisibility(View.GONE);
                        break;
                    case 2:
                        author.setVisibility(View.GONE);
                        publication.setVisibility(View.GONE);
                        btnResFile.setVisibility(View.GONE);
                        tvResFile.setVisibility(View.GONE);
                        link.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        builder = new AlertDialog.Builder(this);
        reference = FirebaseDatabase.getInstance().getReference().child("References");
        storageReference = FirebaseStorage.getInstance().getReference("resource");

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Add Resources");

        btnResFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(AddResources.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectFiles();
                    }
                    else requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                else selectFiles();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String title = tvResTitle.getText().toString();
        final String author = tvResAuthor.getText().toString();
        final String publication = tvResPublication.getText().toString();
        final String description = tvResDescription.getText().toString();
        final String link = tvResLink.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String dept = spResDept.getSelectedItem().toString();
        String sem = "", subCode = "";
        int codePos = 0, togglePos = toggleSwitch.getCheckedTogglePosition();

        if (title.isEmpty()) {
            tvResTitle.setError("Cannot be empty");
            tvResTitle.requestFocus();
        }

        if (spResSem.getVisibility() != View.GONE) {
            if (spResSub.getVisibility() != View.GONE) {
                sem = spResSem.getSelectedItem().toString();
                codePos = spResSub.getSelectedItemPosition();
                if (codePos == 0) {
                    Toasty.warning(this, "Select Subject", Toast.LENGTH_SHORT).show();
                    spResSub.requestFocus();
                }
                else subCode = codeList.get(codePos);
            } else Toasty.warning(this, "Select Semester", Toast.LENGTH_SHORT).show();
        } else Toasty.warning(this, "Select Department", Toast.LENGTH_SHORT).show();

        if (togglePos == 2) {
            if (link.isEmpty()) {
                tvResLink.setError("Please enter a valid Link");
                tvResLink.requestFocus();
            }
        } else Toasty.warning(this, "Select a file", Toast.LENGTH_SHORT).show();

        if (!link.isEmpty() && !URLUtil.isValidUrl(link)) {
            tvResLink.setError("Please enter a valid Link");
            tvResLink.requestFocus();
        }

        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && codePos > 0) {
                resource n = new resource(title, author, publication, subCode, description, upload, link, sem, dept);
                reference.child(filename).setValue(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddResources.this, "Resource Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddResources.this, "Resource Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                if (uriList.size() != 0) {
                    final ProgressDialog progressDialog = new ProgressDialog(AddResources.this);
                    progressDialog.setMessage("Uploading 0/"+uriList.size());
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    for (int i = 0; i < uriList.size(); i++) {
                        final int finalI = i;
                        FirebaseStorage.getInstance().getReference(filename).child(String.valueOf(i)).putFile(uriList.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    FirebaseStorage.getInstance().getReference(filename).child(String.valueOf(finalI)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            counter++;
                                            progressDialog.setMessage("Uploaded "+counter+"/"+uriList.size());
                                            if (task.isSuccessful()) {
                                                savedList.add(task.getResult().toString());
                                            }
                                            else {
                                                Toast.makeText(AddResources.this, "Could Not Save "+finalI, Toast.LENGTH_SHORT).show();
                                                Log.i("Error", task.getException().toString());
                                            }
                                            if (counter == uriList.size()) {
                                                progressDialog.setMessage("Saving Uploaded Files To Database");
//                                                HashMap<String, Object> hashMap = new HashMap<>();
//                                                for (int i = 0; i < savedList.size(); i++) {
//                                                    hashMap.put("File " + i, savedList.get(i));
//                                                }
                                                reference.child(filename).child("link").setValue(/*hashMap*/savedList.get(0))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(AddResources.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddResources.this, "Could Not Save to database", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //
                            }
                        });
                    }
                }
                else finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getSemDept(int id, View view) {
        String data = view.getResources().getResourceName(id);
        data = data.substring(data.length()-2);
        String sem = "", dept = "";
        switch (data.charAt(0)) {
            case '1': sem = "Sem 1"; break;
            case '2': sem = "Sem 2"; break;
            case '3': sem = "Sem 3"; break;
            case '4': sem = "Sem 4"; break;
            case '5': sem = "Sem 5"; break;
            case '6': sem = "Sem 6"; break;
            case '7': sem = "Sem 7"; break;
            case '8': sem = "Sem 8"; break;
        }

        switch (data.charAt(1)) {
            case '1': dept = "CS"; break;
            case '2': dept = "IT"; break;
            case '3': dept = "EXTC"; break;
            case '4': dept = "ETRX"; break;
            case '5': dept = "AI-DS"; break;
        }
        return dept+" "+sem;
    }

    private void selectFiles() {
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("*/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false), 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFiles();
            } else
                Toast.makeText(AddResources.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK && data != null) {
                ClipData clipData = data.getClipData();
                uriList.clear();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uriList.add(clipData.getItemAt(i).getUri());
                    }
                    tvResFile.setText(clipData.getItemCount()+" files selected");
                    Toasty.success(AddResources.this, clipData.getItemCount()+" files selected", Toast.LENGTH_SHORT).show();
                } else  {
                    uriList.add(data.getData());
                    tvResFile.setText("1 file selected");
                    Toasty.success(AddResources.this, "1 file selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
