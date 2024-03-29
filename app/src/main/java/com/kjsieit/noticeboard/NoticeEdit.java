package com.kjsieit.noticeboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class NoticeEdit extends AppCompatActivity {
    DatabaseReference databaseReference;
    EditText etEditNoticeTitle, etEditNoticeSubject, etEditNoticeNotice, etEditNoticeContact;
    TextView tvEditNoticeUploadBy, tvEditNoticeType, tvEditNoticeCDate, tvEditNoticeDate, tvEditNoticeTime,
            tvEditNoticeSem, tvEditNoticeDept;
    ImageButton ibEditNoticeDate, ibEditNoticeTime;
    Button btnUpdateNotice;
    String ampm;
    Boolean timeStatus;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cbCS, cbIT, cbEXTC, cbETRX, cbAI_DS;
    StringBuilder sem, dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_edit);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etEditNoticeTitle = findViewById(R.id.etEditNoticeTitle);
        etEditNoticeSubject = findViewById(R.id.etEditNoticeSubject);
        etEditNoticeNotice = findViewById(R.id.etEditNoticeNotice);
        tvEditNoticeUploadBy = findViewById(R.id.tvEditNoticeUploadBy);
        tvEditNoticeType = findViewById(R.id.tvEditNoticeType);
        tvEditNoticeCDate = findViewById(R.id.tvEditNoticeCDate);
        tvEditNoticeDate = findViewById(R.id.tvEditNoticeDate);
        tvEditNoticeTime = findViewById(R.id.tvEditNoticeTime);
        tvEditNoticeSem = findViewById(R.id.tvEditNoticeSem);
        tvEditNoticeDept = findViewById(R.id.tvEditNoticeDept);
        ibEditNoticeDate = findViewById(R.id.ibEditNoticeDate);
        ibEditNoticeTime = findViewById(R.id.ibEditNoticeTime);
        etEditNoticeContact = findViewById(R.id.etEditNoticeContact);
        btnUpdateNotice = findViewById(R.id.btnUpdateNotice);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);
        cb8 = findViewById(R.id.cb8);
        cbCS = findViewById(R.id.cbCS);
        cbIT = findViewById(R.id.cbIT);
        cbEXTC = findViewById(R.id.cbEXTC);
        cbETRX = findViewById(R.id.cbETRX);
        cbAI_DS = findViewById(R.id.cbAI_DS);

        databaseReference = FirebaseDatabase.getInstance().getReference("notice");
        final String key = getIntent().getStringExtra("key");

        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title,branch,sem,subject,notice,date,cdate,upload,time,type, contact;
                title = dataSnapshot.child("title").getValue().toString();
                branch = dataSnapshot.child("branch").getValue().toString();
                sem = dataSnapshot.child("sem").getValue().toString();
                subject = dataSnapshot.child("subject").getValue().toString();
                notice = dataSnapshot.child("notice").getValue().toString();
                date = dataSnapshot.child("date").getValue().toString();
                cdate = dataSnapshot.child("cdate").getValue().toString();
                upload = dataSnapshot.child("upload").getValue().toString();
                time = dataSnapshot.child("time").getValue().toString();
                type = dataSnapshot.child("type").getValue().toString();
                contact = dataSnapshot.child("contact").getValue().toString();

                etEditNoticeTitle.setText(title);
                etEditNoticeSubject.setText(subject);
                etEditNoticeNotice.setText(notice);
                tvEditNoticeUploadBy.setText(upload);
                tvEditNoticeType.setText(type);
                tvEditNoticeCDate.setText(cdate);
                tvEditNoticeDate.setText(date);
                etEditNoticeContact.setText(contact);

                if (time.equals("")) {
                    timeStatus = true;
                }
                else timeStatus = false;

                if (timeStatus) {
                    tvEditNoticeTime.setVisibility(View.GONE);
                    ibEditNoticeTime.setVisibility(View.GONE);
                }
                else {
                    ibEditNoticeTime.setVisibility(View.VISIBLE);
                    tvEditNoticeTime.setVisibility(View.VISIBLE);
                    tvEditNoticeTime.setText(time);
                }

                if (sem.contains("1")) { cb1.setChecked(true); }
                if (sem.contains("2")) { cb2.setChecked(true); }
                if (sem.contains("3")) { cb3.setChecked(true); }
                if (sem.contains("4")) { cb4.setChecked(true); }
                if (sem.contains("5")) { cb5.setChecked(true); }
                if (sem.contains("6")) { cb6.setChecked(true); }
                if (sem.contains("7")) { cb7.setChecked(true); }
                if (sem.contains("8")) { cb8.setChecked(true); }
                if (branch.contains("CS")) { cbCS.setChecked(true); }
                if (branch.contains("IT")) { cbIT.setChecked(true); }
                if (branch.contains("EXTC")) { cbEXTC.setChecked(true); }
                if (branch.contains("ETRX")) { cbETRX.setChecked(true); }
                if (branch.contains("AI-DS")) { cbAI_DS.setChecked(true); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(NoticeEdit.this, "Error : "+databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        ibEditNoticeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog  datePickerDialog = new DatePickerDialog(NoticeEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvEditNoticeDate.setText(String.format("%02d-%02d-%04d",dayOfMonth,monthOfYear+1,year));
                    }}, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        ibEditNoticeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        }
                        else ampm = "AM";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        tvEditNoticeTime.setText(String.format(String.format("%02d:%02d ", hourOfDay, minute) + ampm));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        btnUpdateNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sem = new StringBuilder();
                dept = new StringBuilder();
                sem.append("Sem");
                if (cb1.isChecked()) { sem.append(" 1,"); }
                if (cb2.isChecked()) { sem.append(" 2,"); }
                if (cb3.isChecked()) { sem.append(" 3,"); }
                if (cb4.isChecked()) { sem.append(" 4,"); }
                if (cb5.isChecked()) { sem.append(" 5,"); }
                if (cb6.isChecked()) { sem.append(" 6,"); }
                if (cb7.isChecked()) { sem.append(" 7,"); }
                if (cb8.isChecked()) { sem.append(" 8"); }
                if (cbCS.isChecked()) { dept.append(" CS,"); }
                if (cbIT.isChecked()) { dept.append(" IT,"); }
                if (cbEXTC.isChecked()) { dept.append(" EXTC,"); }
                if (cbETRX.isChecked()) { dept.append(" ETRX,"); }
                if (cbAI_DS.isChecked()) { dept.append(" AI-DS"); }

                String finalSem, finalDept;
                if (sem.toString().substring(sem.toString().length()-1).equals(",")) {
                    finalSem = sem.toString().substring(0, sem.toString().length()-1);
                }
                else finalSem = sem.toString();
                if (dept.toString().substring(dept.toString().length()-1).equals(",")) {
                    finalDept = dept.toString().substring(0, dept.toString().length()-1);
                }
                else finalDept = dept.toString();

                String contact = etEditNoticeContact.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@somaiya.edu";
                if (etEditNoticeTitle.getText().toString().isEmpty()) { etEditNoticeTitle.setError("Cannot be empty"); }
                if (!(contact.length() ==10 || contact.matches(emailPattern))) { etEditNoticeContact.setError("Please enter valid email ID or Contact no."); }
                if (etEditNoticeNotice.getText().toString().isEmpty()) { etEditNoticeNotice.setError("Cannot be empty"); }
                if (sem.toString().equals("Sem") || dept.toString().isEmpty()) { Toasty.warning(NoticeEdit.this, "Select at-least one semester or department", Toast.LENGTH_SHORT).show(); }

                else if (!etEditNoticeTitle.getText().toString().isEmpty() && !etEditNoticeNotice.getText().toString().isEmpty() && !sem.toString().equals("Sem") &&
                        !dept.toString().isEmpty() && (contact.length() == 10 || contact.matches(emailPattern))) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("title", etEditNoticeTitle.getText().toString());
                    hashMap.put("subject", etEditNoticeSubject.getText().toString());
                    hashMap.put("notice", etEditNoticeNotice.getText().toString());
                    hashMap.put("sem", finalSem);
                    hashMap.put("branch", finalDept);
                    hashMap.put("date", tvEditNoticeDate.getText().toString());
                    hashMap.put("contact", etEditNoticeContact.getText().toString());

                    if (timeStatus) {
                        hashMap.put("time", "");
                    }
                    else hashMap.put("time", tvEditNoticeTime.getText().toString());

                    databaseReference.child(key).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(NoticeEdit.this, "Notice Update Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(NoticeEdit.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
