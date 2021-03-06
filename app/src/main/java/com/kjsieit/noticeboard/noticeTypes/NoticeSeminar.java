package com.kjsieit.noticeboard.noticeTypes;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.notice;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoticeSeminar extends AppCompatActivity {

    ImageButton ibSeminarDate, ibSeminarTime;
    TextView tvSeminarDate, tvSeminarDept, tvSeminarSem, tvSeminarTime, tvSeminarFile, tvSeminarSemData, tvSeminarDeptData, tvSeminarContact;
    EditText tvSeminarTitle, tvSeminarSubject, tvSeminarNotice;
    Calendar calendar;
    String ampm, url;
    DatabaseReference reference;
    Toolbar toolbar;
    Button btnSeminarFile;
    Switch switchSeminar;
    Uri uri;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    AlertDialog.Builder builder;
    CheckBox cb_semI, cb_semII, cb_semIII, cb_semIV, cb_semV, cb_semVI, cb_semVII, cb_semVIII, cb_CS, cb_IT, cb_EXTC, cb_ETRX, cb_AI_DS;
    StringBuilder data = new StringBuilder();


    /**
     * NO CHANGES MADE IN THIS FILE
     * THIS FILE IS NOT USED ANYMORE
     * **/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_seminar);

        ibSeminarDate = findViewById(R.id.ibSeminarDate);
        ibSeminarTime = findViewById(R.id.ibSeminarTime);
        tvSeminarDate = findViewById(R.id.tvSeminarDate);
        tvSeminarDept = findViewById(R.id.tvSeminarDept);
        tvSeminarSem = findViewById(R.id.tvSeminarSem);
        tvSeminarTime = findViewById(R.id.tvSeminarTime);
        tvSeminarTitle = findViewById(R.id.tvSeminarTitle);
        tvSeminarSubject = findViewById(R.id.tvSeminarSubject);
        tvSeminarNotice = findViewById(R.id.tvSeminarNotice);
        tvSeminarSemData = findViewById(R.id.tvSeminarSemData);
        tvSeminarDeptData = findViewById(R.id.tvSeminarDeptData);
        calendar = Calendar.getInstance();
        tvSeminarFile = findViewById(R.id.tvSeminarFile);
        btnSeminarFile = findViewById(R.id.btnSeminarFile);
        switchSeminar = findViewById(R.id.switchSeminar);
        reference = FirebaseDatabase.getInstance().getReference("notice");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        builder = new AlertDialog.Builder(this);
        tvSeminarContact = findViewById(R.id.tvSeminarContact);

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Event Section");

        ibSeminarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog  datePickerDialog = new DatePickerDialog(NoticeSeminar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvSeminarDate.setText(String.format("%02d-%02d-%04d",dayOfMonth,monthOfYear+1,year));
                    }}, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        ibSeminarTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeSeminar.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        }
                        else ampm = "AM";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        tvSeminarTime.setText(String.format(String.format("%02d:%02d ", hourOfDay, minute) + ampm));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        switchSeminar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchSeminar.isChecked()) {
                    tvSeminarFile.setVisibility(View.VISIBLE);
                    btnSeminarFile.setVisibility(View.VISIBLE);
                }
                else {
                    tvSeminarFile.setVisibility(View.INVISIBLE);
                    btnSeminarFile.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSeminarFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NoticeSeminar.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFiles();
                }
                else ActivityCompat.requestPermissions(NoticeSeminar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });

        tvSeminarSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(NoticeSeminar.this).inflate(R.layout.dialog_box_semester, null);
                cb_semI = view.findViewById(R.id.cb_semI);
                cb_semII = view.findViewById(R.id.cb_semII);
                cb_semIII = view.findViewById(R.id.cb_semIII);
                cb_semIV = view.findViewById(R.id.cb_semIV);
                cb_semV = view.findViewById(R.id.cb_semV);
                cb_semVI = view.findViewById(R.id.cb_semVI);
                cb_semVII = view.findViewById(R.id.cb_semVII);
                cb_semVIII = view.findViewById(R.id.cb_semVIII);
                builder.setView(view);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.delete(0, data.length()).append("Sem");
                        if (cb_semI.isChecked()) { data.append(" 1,"); }
                        if (cb_semII.isChecked()) { data.append(" 2,"); }
                        if (cb_semIII.isChecked()) { data.append(" 3,"); }
                        if (cb_semIV.isChecked()) { data.append(" 4,"); }
                        if (cb_semV.isChecked()) { data.append(" 5,"); }
                        if (cb_semVI.isChecked()) { data.append(" 6,"); }
                        if (cb_semVII.isChecked()) { data.append(" 7,"); }
                        if (cb_semVIII.isChecked()) { data.append(" 8"); }
                        if (!cb_semI.isChecked() && !cb_semII.isChecked() && !cb_semIII.isChecked() && !cb_semIV.isChecked() && !cb_semV.isChecked() && !cb_semVI.isChecked() && !cb_semVII.isChecked() && !cb_semVIII.isChecked()) {
                            Toast.makeText(view.getContext(), "Please select at-least one semester", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (data.toString().substring(data.toString().length()-1).equals(",")) {
                                tvSeminarSemData.setText(data.toString().substring(0, data.toString().length()-1));
                            }
                            else tvSeminarSemData.setText(data);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        tvSeminarDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(NoticeSeminar.this).inflate(R.layout.dialog_box_department, null);
                cb_CS = view.findViewById(R.id.cb_CS);
                cb_IT = view.findViewById(R.id.cb_IT);
                cb_EXTC = view.findViewById(R.id.cb_EXTC);
                cb_ETRX = view.findViewById(R.id.cb_ETRX);
                cb_AI_DS = view.findViewById(R.id.cb_AI_DS);
                builder.setView(view);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.delete(0, data.length());
                        if (cb_CS.isChecked()) { data.append(" CS,"); }
                        if (cb_IT.isChecked()) { data.append(" IT,"); }
                        if (cb_EXTC.isChecked()) { data.append(" EXTC,"); }
                        if (cb_ETRX.isChecked()) { data.append(" ETRX,"); }
                        if (cb_AI_DS.isChecked()) { data.append(" AI-DS"); }
                        if (!cb_CS.isChecked() && !cb_IT.isChecked() && !cb_EXTC.isChecked() && !cb_ETRX.isChecked() && !cb_AI_DS.isChecked()) {
                            Toast.makeText(view.getContext(), "Please select at-least one department", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (data.toString().substring(data.toString().length()-1).equals(",")) {
                                tvSeminarDeptData.setText(data.toString().substring(0, data.toString().length()-1));
                            }
                            else tvSeminarDeptData.setText(data);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
        final String title = tvSeminarTitle.getText().toString();
        final String date = tvSeminarDate.getText().toString();
        final String time = tvSeminarTime.getText().toString();
        final String semester = tvSeminarSemData.getText().toString();
        final String department = tvSeminarDeptData.getText().toString();
        final String subject = tvSeminarSubject.getText().toString();
        final String notice = tvSeminarNotice.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String current_date = sdf.format(new Date());
        final String filename = System.currentTimeMillis()+"";
        final String contact = tvSeminarContact.getText().toString();
        if (title.isEmpty()) {
            tvSeminarTitle.setError("Cannot be empty");
            tvSeminarTitle.requestFocus();
        }
        if (date.equals("Select Date")) {
            tvSeminarDate.setError("Select Date");
        }
        if (notice.isEmpty()) {
            tvSeminarNotice.setError("Cannot be empty");
            tvSeminarNotice.requestFocus();
        }
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "[a-zA-Z0-9._-]+@somaiya.edu";
        if (!(contact.length() ==10 || contact.matches(emailPattern))) {
            tvSeminarContact.setError("Please enter valid email ID or Contact no.");
            tvSeminarContact.requestFocus();
        }
        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !date.equals("Select Date") && !semester.equals("Select Semester")
                    && !department.equals("Select Department") && !subject.isEmpty() && !notice.isEmpty() && uri != null && contact!=null) {

                progressDialog = new ProgressDialog(NoticeSeminar.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Uploading....");
                progressDialog.setProgress(0);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                storageReference.child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                com.kjsieit.noticeboard.models.notice n = new notice(title, department, semester, subject, notice, date, current_date,
                                        upload, time, "Event Section", filename, contact);
                                url = uri.toString();
                                reference.child(filename).setValue(n);
                                Toast.makeText(NoticeSeminar.this, "Done", Toast.LENGTH_SHORT).show();

                                reference.child(filename).child("files").setValue(url);
                                progressDialog.dismiss();

                                new AlertDialog.Builder(NoticeSeminar.this).setMessage("Do you want to notice_details_student this Notice ?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                Intent i = new Intent(Intent.ACTION_SEND);
                                                i.setType("text/*");
                                                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                                                i.putExtra(Intent.EXTRA_TEXT, title+"\nfor "+department+"\n"+semester+"\n"+current_date+"\n"+notice+"\nLast date : "+date+"\nPFA below\n"+url);
                                                startActivity(Intent.createChooser(i,"Share using..."));
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NoticeSeminar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeSeminar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        int CurrProgress = (int) ((100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress(CurrProgress);
                    }
                });

            }
            else {
                notice n = new notice(title, department, semester, subject, notice, date, current_date,
                        upload, time, "Event Section", filename, contact);
                reference.child(filename).setValue(n);
                Toast.makeText(NoticeSeminar.this, "Notice added successfully", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(this).setMessage("Do you want to notice_details_student this Notice ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/*");
                                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                                i.putExtra(Intent.EXTRA_TEXT, title+"\nfor "+department+"\n"+semester+"\n"+current_date+"\n"+notice+"\nLast date : "+date);
                                startActivity(Intent.createChooser(i,"Share using..."));
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //File upload from here
    public void selectFiles() {
        Intent i = new Intent();
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 86);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFiles();
        }
        else Toast.makeText(NoticeSeminar.this, "Please provide permissions...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String dataaaa = data.getData().getLastPathSegment();
            if (dataaaa.contains("/")) {
                String name = dataaaa.substring(dataaaa.lastIndexOf("/")+1);
                tvSeminarFile.setText(name);
            }
            else tvSeminarFile.setText(dataaaa);
        }
        else Toast.makeText(NoticeSeminar.this, "Please select a file...", Toast.LENGTH_SHORT).show();
    }
}
