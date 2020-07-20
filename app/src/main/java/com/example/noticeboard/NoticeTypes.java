package com.example.noticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class NoticeTypes extends AppCompatActivity {
    Button btnSports, btnExamCell, btnDepartment, btnSeminar, btnUploadSingleFile;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_types);
        btnSports = findViewById(R.id.btnSports);
        btnExamCell = findViewById(R.id.btnExamCell);
        btnDepartment = findViewById(R.id.btnDepartment);
        btnSeminar = findViewById(R.id.btnSeminar);
        btnUploadSingleFile = findViewById(R.id.btnUploadSingleFile);

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Notice Types");

        btnUploadSingleFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeTypes.this, SingleFileUpload.class));
            }
        });

        btnSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeTypes.this, NoticeSports.class));
            }
        });

        btnExamCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeTypes.this, NoticeExamCell.class));
            }
        });

        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeTypes.this, NoticeDepartment.class));
            }
        });

        btnSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeTypes.this, NoticeSeminar.class));
            }
        });
    }
}
