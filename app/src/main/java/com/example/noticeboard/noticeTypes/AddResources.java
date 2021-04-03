package com.example.noticeboard.noticeTypes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.noticeboard.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import es.dmoral.toasty.Toasty;

public class AddResources extends AppCompatActivity {

    TextView tvResFile, tvResSemDept, tvResSemDeptData;
    TextInputEditText tvResTitle, tvResAuthor, tvResPublication, tvResSubject, tvResDescription, tvResLink;
    TextInputLayout link, author, publication;
    DatabaseReference reference;
    Toolbar toolbar;
    Button btnResFile;
    AlertDialog.Builder builder;
    StorageReference storageReference;
    String filename = System.currentTimeMillis()+"";
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> savedList = new ArrayList<>();
    int counter = 0;
    StringBuilder stringBuilder;
    ToggleSwitch toggleSwitch;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resources);

        tvResTitle = findViewById(R.id.tvResTitle);
        tvResAuthor = findViewById(R.id.tvResAuthor);
        tvResPublication = findViewById(R.id.tvResPublication);
        tvResSemDept = findViewById(R.id.tvResSemDept);
        tvResSemDeptData = findViewById(R.id.tvResSemDeptData);
        tvResSubject = findViewById(R.id.tvResSubject);
        tvResDescription = findViewById(R.id.tvResDescription);
        tvResLink = findViewById(R.id.tvResLink);
        btnResFile = findViewById(R.id.btnResFile);
        tvResFile = findViewById(R.id.tvResFile);
        link = findViewById(R.id.link);
        author = findViewById(R.id.author);
        publication = findViewById(R.id.publication);
        toggleSwitch = findViewById(R.id.switchResourceType);

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

        tvResSemDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View view = LayoutInflater.from(AddResources.this).inflate(R.layout.dialog_box_resources, null);
                CheckBox cb11, cb12, cb13, cb14, cb15, cb21, cb22, cb23, cb24, cb25, cb31, cb32, cb33, cb34, cb35, cb41, cb42, cb43, cb44, cb45, 
                        cb51, cb52, cb53, cb54, cb55, cb61, cb62, cb63, cb64, cb65, cb71, cb72, cb73, cb74, cb75, cb81, cb82, cb83, cb84, cb85;
                cb11 = view.findViewById(R.id.cb11);
                cb12 = view.findViewById(R.id.cb12);
                cb13 = view.findViewById(R.id.cb13);
                cb14 = view.findViewById(R.id.cb14);
                cb15 = view.findViewById(R.id.cb15);
                cb21 = view.findViewById(R.id.cb21);
                cb22 = view.findViewById(R.id.cb22);
                cb23 = view.findViewById(R.id.cb23);
                cb24 = view.findViewById(R.id.cb24);
                cb25 = view.findViewById(R.id.cb25);
                cb31 = view.findViewById(R.id.cb31);
                cb32 = view.findViewById(R.id.cb32);
                cb33 = view.findViewById(R.id.cb33);
                cb34 = view.findViewById(R.id.cb34);
                cb35 = view.findViewById(R.id.cb35);
                cb41 = view.findViewById(R.id.cb41);
                cb42 = view.findViewById(R.id.cb42);
                cb43 = view.findViewById(R.id.cb43);
                cb44 = view.findViewById(R.id.cb44);
                cb45 = view.findViewById(R.id.cb45);
                cb51 = view.findViewById(R.id.cb51);
                cb52 = view.findViewById(R.id.cb52);
                cb53 = view.findViewById(R.id.cb53);
                cb54 = view.findViewById(R.id.cb54);
                cb55 = view.findViewById(R.id.cb55);
                cb61 = view.findViewById(R.id.cb61);
                cb62 = view.findViewById(R.id.cb62);
                cb63 = view.findViewById(R.id.cb63);
                cb64 = view.findViewById(R.id.cb64);
                cb65 = view.findViewById(R.id.cb65);
                cb71 = view.findViewById(R.id.cb71);
                cb72 = view.findViewById(R.id.cb72);
                cb73 = view.findViewById(R.id.cb73);
                cb74 = view.findViewById(R.id.cb74);
                cb75 = view.findViewById(R.id.cb75);
                cb81 = view.findViewById(R.id.cb81);
                cb82 = view.findViewById(R.id.cb82);
                cb83 = view.findViewById(R.id.cb83);
                cb84 = view.findViewById(R.id.cb84);
                cb85 = view.findViewById(R.id.cb85);

                final CheckBox[] checkBoxes = new CheckBox[] {cb11, cb12, cb13, cb14, cb15, cb21, cb22, cb23, cb24, cb25, cb31, cb32, cb33, cb34, cb35, cb41, cb42, cb43, cb44, cb45,
                        cb51, cb52, cb53, cb54, cb55, cb61, cb62, cb63, cb64, cb65, cb71, cb72, cb73, cb74, cb75, cb81, cb82, cb83, cb84, cb85};
                builder.setView(view);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stringBuilder = new StringBuilder();
                        for (CheckBox box : checkBoxes) {
                            if (box.isChecked()) {
                                stringBuilder.append(getSemDept(box.getId(), view)).append(", ");
                            }
                        }
                        if (stringBuilder.length() == 0) {
                            tvResSemDeptData.setText("All combinations of semester and department");
                            for (CheckBox checkBox : checkBoxes) {
                                stringBuilder.append(getSemDept(checkBox.getId(), view)).append(", ");
                            }
                        }
                        else tvResSemDeptData.setText(stringBuilder);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

//        StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.stickySwitchRes);
//        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
//            @Override
//            public void onSelectedChange(@org.jetbrains.annotations.NotNull @NotNull StickySwitch.Direction direction, @org.jetbrains.annotations.NotNull @NotNull String text) {
//                if(direction == StickySwitch.Direction.LEFT) {
//                    btnResFile.setVisibility(View.VISIBLE);
//                    tvResFile.setVisibility(View.VISIBLE);
//                    link.setVisibility(View.GONE);
//                }
//                else {
//                    btnResFile.setVisibility(View.GONE);
//                    tvResFile.setVisibility(View.GONE);
//                    link.setVisibility(View.VISIBLE);
//                }
//            }
//        });



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
        final String subject = tvResSubject.getText().toString();
        final String description = tvResDescription.getText().toString();
        final String link = tvResLink.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String semDept = stringBuilder.toString();

        if (title.isEmpty()) {
            tvResTitle.setError("Cannot be empty");
            tvResTitle.requestFocus();
        }

        if (subject.isEmpty()) {
            tvResSubject.setError("Cannot be empty");
            tvResSubject.requestFocus();
        }

        if (!link.isEmpty() && !URLUtil.isValidUrl(link)) {
            tvResLink.setError("Please enter a valid Link");
            tvResLink.requestFocus();
        }

        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !subject.isEmpty()) {
                resource n = new resource(title, author, publication, subject, description, upload, link, semDept);
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
