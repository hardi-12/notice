package com.kjsieit.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class LoginStudent extends AppCompatActivity {

    EditText etStudentSignInEmail, etStudentSignInPassword;
    Button btnStudentSignIn, btnStudentSignUp, btnStudentForgotPassword;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    ProgressDialog loading;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etStudentSignInEmail = findViewById(R.id.etStudentSignInEmail);
        etStudentSignInPassword = findViewById(R.id.etStudentSignInPassword);
        btnStudentSignIn = findViewById(R.id.btnStudentSignIn);
        btnStudentSignUp = findViewById(R.id.btnStudentSignUp);
        btnStudentForgotPassword = findViewById(R.id.btnStudentForgotPassword);
        loading=new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user");

        if (user != null ) {
            startActivity(new Intent(LoginStudent.this, DashboardStudent.class));
            finish();
        }

        btnStudentSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etStudentSignInEmail.getText().toString();
                final String password = etStudentSignInPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()){
                    Toasty.info(LoginStudent.this, "ENTER CREDENTIALS", Toast.LENGTH_SHORT).show();
                }
                else {
                    loading.setTitle("SIGNING IN");
                    loading.setMessage("Please wait, while we are checking the credentials.");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                    reference.orderByChild("email").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String typ = dataSnapshot.child(username.replace(".", "_dot_")).child("type").getValue().toString();
                                if (typ.equals("student")) {
                                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(LoginStudent.this, DashboardStudent.class));
                                                loading.dismiss();
                                                etStudentSignInEmail.setText("");
                                                etStudentSignInPassword.setText("");
                                                etStudentSignInEmail.requestFocus();
                                                finish();
                                            }
                                            else {
                                                Toasty.error(LoginStudent.this, "Failure \n" + task.getException(), Toast.LENGTH_SHORT).show();
                                                loading.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toasty.error(LoginStudent.this, "Failure : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            loading.dismiss();
                                        }
                                    });
                                }
                                else {
                                    Toasty.warning(LoginStudent.this, "Only student's can access, "+typ+"'s cannot", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    etStudentSignInEmail.setText("");
                                    etStudentSignInPassword.setText("");
                                    etStudentSignInEmail.requestFocus();
                                    finish();
                                }
                            }
                            else {
                                Toasty.warning(LoginStudent.this, "No such user exists", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(LoginStudent.this, "Error : "+databaseError, Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    });
                }
            }
        });

        btnStudentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginStudent.this, StudentSignUpActivity.class));
            }
        });

        btnStudentForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginStudent.this,ForgotPassword.class));
            }
        });
    }
}
