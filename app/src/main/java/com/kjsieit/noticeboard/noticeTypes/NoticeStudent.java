package com.kjsieit.noticeboard.noticeTypes;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class NoticeStudent extends AppCompatActivity {
    ImageButton ibSportsDate, ibSportsTime;
    TextView tvSportsDate, tvSportsDept, tvSportsSem, tvSportsTime, tvSportsFile, tvSportsSemData, tvSportsDeptData, tvSportsContact;
    EditText tvSportsTitle, tvSportsSubject, tvSportsNotice;
    Calendar calendar;
    String ampm;
    DatabaseReference reference;
    Button btnSportsFile;
    Switch switchSports;
    StorageReference storageReference;
    AlertDialog.Builder builder;
    CheckBox cb_semI, cb_semII, cb_semIII, cb_semIV, cb_semV, cb_semVI, cb_semVII, cb_semVIII, cb_CS, cb_IT, cb_EXTC, cb_ETRX, cb_AI_DS;
    StringBuilder data = new StringBuilder();
    String filename = System.currentTimeMillis()+"";
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> savedList = new ArrayList<>();
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_sports);
        setTitle("Student Section");

        ibSportsDate = findViewById(R.id.ibSportsDate);
        ibSportsTime = findViewById(R.id.ibSportsTime);
        tvSportsDate = findViewById(R.id.tvSportsDate);
        tvSportsDept = findViewById(R.id.tvSportsDept);
        tvSportsSem = findViewById(R.id.tvSportsSem);
        tvSportsTime = findViewById(R.id.tvSportsTime);
        tvSportsTitle = findViewById(R.id.tvSportsTitle);
        tvSportsSubject = findViewById(R.id.tvSportsSubject);
        tvSportsNotice = findViewById(R.id.tvSportsNotice);
        tvSportsSemData = findViewById(R.id.tvSportsSemData);
        tvSportsDeptData = findViewById(R.id.tvSportsDeptData);
        calendar = Calendar.getInstance();
        tvSportsFile = findViewById(R.id.tvSportsFile);
        btnSportsFile = findViewById(R.id.btnSportsFile);
        switchSports = findViewById(R.id.switchSports);
        reference = FirebaseDatabase.getInstance().getReference("notice");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        builder = new AlertDialog.Builder(this);
        tvSportsContact = findViewById(R.id.tvSportsContact);

        ibSportsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog  datePickerDialog = new DatePickerDialog(NoticeStudent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvSportsDate.setText(String.format("%02d-%02d-%04d",dayOfMonth,monthOfYear+1,year));
                    }}, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        ibSportsTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeStudent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        }
                        else ampm = "AM";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        tvSportsTime.setText(String.format(String.format("%02d:%02d ", hourOfDay, minute) + ampm));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        switchSports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchSports.isChecked()) {
                    tvSportsFile.setVisibility(View.VISIBLE);
                    btnSportsFile.setVisibility(View.VISIBLE);
                }
                else {
                    tvSportsFile.setVisibility(View.INVISIBLE);
                    btnSportsFile.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSportsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NoticeStudent.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFiles();
                }
                else ActivityCompat.requestPermissions(NoticeStudent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });

        tvSportsSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(NoticeStudent.this).inflate(R.layout.dialog_box_semester, null);
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
                                tvSportsSemData.setText(data.toString().substring(0, data.toString().length()-1));
                            }
                            else tvSportsSemData.setText(data);
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

        tvSportsDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(NoticeStudent.this).inflate(R.layout.dialog_box_department, null);
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
                                tvSportsDeptData.setText(data.toString().substring(0, data.toString().length()-1));
                            }
                            else tvSportsDeptData.setText(data);
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
        final String title = tvSportsTitle.getText().toString();
        final String date = tvSportsDate.getText().toString();
        final String time = tvSportsTime.getText().toString();
        final String semester = tvSportsSemData.getText().toString();
        final String department = tvSportsDeptData.getText().toString();
        final String subject = tvSportsSubject.getText().toString();
        final String notice = tvSportsNotice.getText().toString();
        final String contact = tvSportsContact.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final String current_date = sdf.format(new Date());
        if (date.equals("Select Date")) {
            tvSportsDate.setError("Select Date");
        }
        if (semester.equals("Selected Semester")) {
            Toasty.error(NoticeStudent.this, "Select Semester", Toast.LENGTH_SHORT).show();
        }
        if (department.equals("Selected Department")) {
            Toasty.error(NoticeStudent.this, "Select Department", Toast.LENGTH_SHORT).show();
        }
        if (semester.equals("Selected Semester") && department.equals("Selected Department")) {
            Toasty.error(NoticeStudent.this, "Select Semester & Department", Toast.LENGTH_SHORT).show();
        }
//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "[a-zA-Z0-9._-]+@somaiya.edu";
        if (!(contact.length() ==10 || contact.matches(emailPattern))) {
            tvSportsContact.setError("Please enter valid email ID or Contact no.");
            tvSportsContact.requestFocus();
        }
        if (notice.isEmpty()) {
            tvSportsNotice.setError("Cannot be empty");
            tvSportsNotice.requestFocus();
        }
        if (title.isEmpty()) {
            tvSportsTitle.setError("Cannot be empty");
            tvSportsTitle.requestFocus();
        }
        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !date.equals("Select Date") && !semester.equals("Selected Semester") &&
                    !department.equals("Selected Department")&& !notice.isEmpty() && (contact.length() == 10 || contact.matches(emailPattern))) {
                com.kjsieit.noticeboard.models.notice n = new notice(title, department, semester, subject, notice, date, current_date, upload, time, "Student Section", filename, contact);
                reference.child(filename).setValue(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NoticeStudent.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeStudent.this, "Notice Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                if (uriList.size() != 0) {
                    final ProgressDialog progressDialog = new ProgressDialog(NoticeStudent.this);
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
                                                Toast.makeText(NoticeStudent.this, "Could Not Save "+finalI, Toast.LENGTH_SHORT).show();
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
                                                                Toast.makeText(NoticeStudent.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(NoticeStudent.this, "Could Not Save to database", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NoticeStudent.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

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
                    tvSportsFile.setText(clipData.getItemCount()+" files selected");
                    Toasty.success(NoticeStudent.this, clipData.getItemCount()+" files selected", Toast.LENGTH_SHORT).show();
                } else  {
                    uriList.add(data.getData());
                    tvSportsFile.setText("1 file selected");
                    Toasty.success(NoticeStudent.this, "1 file selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
