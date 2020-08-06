package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LoginAdmin extends AppCompatActivity {

    EditText etSignInEmail, etSignInPassword;
    Button btnSignIn, btnSignUp, btnForgotPassword;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    ProgressDialog loading;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        etSignInEmail = findViewById(R.id.etSignInEmail);
        etSignInPassword = findViewById(R.id.etSignInPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        loading = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user");

        if (user != null ) {
            startActivity(new Intent(LoginAdmin.this, Dashboard.class));
            finish();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etSignInEmail.getText().toString();
                final String password = etSignInPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()){
                    Toasty.info(LoginAdmin.this, "ENTER CREDENTIALS", Toast.LENGTH_SHORT).show();
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
                                if (typ.equals("admin")) {
                                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(LoginAdmin.this, Dashboard.class));
                                                loading.dismiss();
                                                etSignInEmail.setText("");
                                                etSignInPassword.setText("");
                                                etSignInEmail.requestFocus();
                                                finish();
                                            }
                                            else {
                                                Toasty.error(LoginAdmin.this, "Failure \n" + task.getException(), Toast.LENGTH_SHORT).show();
                                                loading.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toasty.error(LoginAdmin.this, "Failure : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            loading.dismiss();
                                        }
                                    });
                                }
                                else {
                                    Toasty.warning(LoginAdmin.this, "Only admin's can access, "+typ+"'s cannot", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    etSignInEmail.setText("");
                                    etSignInPassword.setText("");
                                    etSignInEmail.requestFocus();
                                    finish();
                                }
                            }
                            else {
                                Toasty.warning(LoginAdmin.this, "No such user exists", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(LoginAdmin.this, "Error : "+databaseError, Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    });
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginAdmin.this);
                alertDialog.setTitle("Password verification");
                alertDialog.setMessage("Enter password to create an Admin Account");

                final EditText input = new EditText(LoginAdmin.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Sign up",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(input.getText().toString().equals("Information Technology")) {
                                    startActivity(new Intent(LoginAdmin.this, AdminSignUpActivity.class));
                                }
                                else {
                                    Toast.makeText(LoginAdmin.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAdmin.this,ForgotPassword.class));
            }
        });
    }
}
