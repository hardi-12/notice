package com.kjsieit.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {
    TextView tvStatus;
    Button btnSend;
    EditText etEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        tvStatus = findViewById(R.id.tvStatus);
        btnSend = findViewById(R.id.btnSend);
        etEmail = findViewById(R.id.etEmail);
        auth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pattern = "[a-zA-Z0-9._-]+@somaiya.edu";
                if (email.matches(pattern)){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toasty.success(ForgotPassword.this, "Reset link send to email.\nIt may take a while", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ForgotPassword.this, IntroActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toasty.error(ForgotPassword.this, "Failure :( \n"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    tvStatus.setText("Invalid Email ID \nPlease enter ...@somaiya.edu ID");
                    etEmail.requestFocus();
                    etEmail.setText("");
                }
            }
        });
    }
}
