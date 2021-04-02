package com.example.noticeboard.noticeTypes;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.noticeboard.R;
import com.example.noticeboard.models.notice;
import com.example.noticeboard.models.resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import io.ghyeok.stickyswitch.widget.StickySwitch;

public class AddResources extends AppCompatActivity {

    TextView tvResFile;
    TextInputEditText tvResTitle, tvResAuthor, tvResPublication, tvResSubject, tvResDescription, tvResLink;
    TextInputLayout link;
    DatabaseReference reference;
    Toolbar toolbar;
    Button btnDeptFile;
    StorageReference storageReference;
    String filename = System.currentTimeMillis()+"";
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> savedList = new ArrayList<>();
    int counter = 0;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resources);

        tvResTitle = findViewById(R.id.tvResSubject);
        tvResAuthor = findViewById(R.id.tvResAuthor);
        tvResPublication = findViewById(R.id.tvResPublication);
        tvResSubject = findViewById(R.id.tvResSubject);
        tvResDescription = findViewById(R.id.tvResDescription);
        btnDeptFile = findViewById(R.id.btnDeptFile);
        tvResFile = findViewById(R.id.tvResFile);
        tvResLink = findViewById(R.id.tvResLink);
        link = findViewById(R.id.link);
        reference = FirebaseDatabase.getInstance().getReference().child("References");
        storageReference = FirebaseStorage.getInstance().getReference("resource");

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Add Resources");

        StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@org.jetbrains.annotations.NotNull @NotNull StickySwitch.Direction direction, @org.jetbrains.annotations.NotNull @NotNull String text) {
                if(direction == StickySwitch.Direction.LEFT) {
                    btnDeptFile.setVisibility(View.VISIBLE);
                    tvResFile.setVisibility(View.VISIBLE);
                    link.setVisibility(View.GONE);
                }
                else {
                    btnDeptFile.setVisibility(View.GONE);
                    tvResFile.setVisibility(View.GONE);
                    link.setVisibility(View.VISIBLE);
                }
            }
        });

        btnDeptFile.setOnClickListener(new View.OnClickListener() {
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
        final String subject = tvResSubject.getText().toString();
        final String description = tvResDescription.getText().toString();
        final String link = tvResLink.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (title.isEmpty()) {
            tvResTitle.setError("Cannot be empty");
            tvResTitle.requestFocus();
        }
        if (subject.isEmpty()) {
            tvResSubject.setError("Cannot be empty");
            tvResSubject.requestFocus();
        }

        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !subject.isEmpty()) {
                resource n = new resource(title, author, publication, subject, description, upload, link);
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
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                for (int i = 0; i < savedList.size(); i++) {
                                                    hashMap.put("File " + i, savedList.get(i));
                                                }
                                                reference.child(filename).child("files").updateChildren(hashMap)
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

    private void selectFiles() {
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("*/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true), 2);
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
