package com.example.noticeboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noticeboard.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class AdminSignUpActivity extends AppCompatActivity {

    EditText etAdminName, etAdminIDNumber, etAdminPhoneNumber, etAdminEmail, etAdminPassword;
    Button btnAdminSignUp;
    Spinner spDepartment, spDesignation;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    ProgressDialog loading;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        etAdminName = findViewById(R.id.etAdminName);
        etAdminIDNumber = findViewById(R.id.etAdminIDNumber);
        etAdminPhoneNumber = findViewById(R.id.etAdminPhoneNumber);
        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminSignUp = findViewById(R.id.btnAdminSignUp);
        spDepartment = findViewById(R.id.spDepartment);
        spDesignation = findViewById(R.id.spDesignation);
        loading = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");

        btnAdminSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, ID, phone, email, password, department, designation;
                name = etAdminName.getText().toString();
                ID = etAdminIDNumber.getText().toString();
                phone = etAdminPhoneNumber.getText().toString();
                email = etAdminEmail.getText().toString();
                password = etAdminPassword.getText().toString();
                department = spDepartment.getSelectedItem().toString();
                designation = spDesignation.getSelectedItem().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@somaiya.edu";
                final String key = email.replace(".", "_dot_");
                if (password.length() < 7){
                    etAdminPassword.setError("At-least 8 characters required");
                    etAdminPassword.requestFocus();
                }
                if (!email.matches(emailpattern)){
                    etAdminEmail.setError("Kindly use @somaiya.edu ID");
                    etAdminEmail.requestFocus();
                }
                if (phone.length() != 10){
                    etAdminPhoneNumber.setError("Invalid Phone number");
                    etAdminPhoneNumber.requestFocus();
                }
                if (ID.length() != 10){
                    etAdminIDNumber.setError("Please enter correct ID number");
                    etAdminIDNumber.requestFocus();
                }
                if (name.length() < 4){
                    etAdminName.setError("Invalid Name");
                    etAdminName.requestFocus();
                }
                if (department.equals("Select Department")) {
                    Toasty.error(AdminSignUpActivity.this, "Select Department", Toast.LENGTH_SHORT).show();
                }
                if (designation.equals("Select Designation")) {
                    Toasty.error(AdminSignUpActivity.this, "Select Designation", Toast.LENGTH_SHORT).show();
                }
                if (department.equals("Select Department") && designation.equals("Select Designation")) {
                    Toasty.error(AdminSignUpActivity.this, "Select Department & Designation", Toast.LENGTH_SHORT).show();
                }
                else if (password.length() > 7 && email.matches(emailpattern) && phone.length() == 10 && ID.length() == 10 && name.length() > 4
                        && !department.equals("Select Department") && !designation.equals("Select Designation")) {
                    loading.setTitle("Create Account");
                    loading.setMessage("Please wait, while we are checking the credentials.");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                    reference.orderByChild("id_number").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                Toasty.warning(AdminSignUpActivity.this, "User with given ID card number already exists", Toast.LENGTH_LONG).show();
                                etAdminIDNumber.requestFocus();
                            }
                            else {
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            user u = new user(name, phone, department, email, "", "admin", designation, ID);
                                            reference.child(key).setValue(u);
                                            reference.child(key).child("superAdmin").setValue("false");
                                            startActivity(new Intent(AdminSignUpActivity.this, Dashboard.class));
                                            loading.dismiss();
                                            finish();
                                        }
                                        else {
                                            loading.dismiss();
                                            Toasty.warning(AdminSignUpActivity.this, "Account creation failed\n"+task.getException(), Toast.LENGTH_LONG).show();
                                            etAdminEmail.requestFocus();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            loading.dismiss();
                            Toasty.error(AdminSignUpActivity.this, "Error : "+databaseError, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
