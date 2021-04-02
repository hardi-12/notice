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

public class StudentSignUpActivity extends AppCompatActivity {

    EditText etStudentName, etStudentIDNumber, etStudentPhoneNumber, etStudentEmail, etStudentPassword;
    Button btnStudentSignUp;
    Spinner spDepartment, spSemester;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    ProgressDialog loading;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        etStudentName = findViewById(R.id.etStudentName);
        etStudentIDNumber = findViewById(R.id.etStudentIDNumber);
        etStudentPhoneNumber = findViewById(R.id.etStudentPhoneNumber);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        etStudentPassword = findViewById(R.id.etStudentPassword);
        btnStudentSignUp = findViewById(R.id.btnStudentSignUp);
        spDepartment = findViewById(R.id.spDepartment);
        spSemester = findViewById(R.id.spSemester);
        loading = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");

        btnStudentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, ID, phone, email, password, department, semester;
                name = etStudentName.getText().toString();
                ID = etStudentIDNumber.getText().toString();
                phone = etStudentPhoneNumber.getText().toString();
                email = etStudentEmail.getText().toString();
                password = etStudentPassword.getText().toString();
                department = spDepartment.getSelectedItem().toString();
                semester = spSemester.getSelectedItem().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@somaiya.edu";
                final String key = email.replace(".", "_dot_");
                if (password.length() < 7){
                    etStudentPassword.setError("At-least 8 characters required");
                    etStudentPassword.requestFocus();
                }
                if (!email.matches(emailpattern)){
                    etStudentEmail.setError("Kindly use @somaiya.edu ID");
                    etStudentEmail.requestFocus();
                }
                if (phone.length() != 10){
                    etStudentPhoneNumber.setError("Invalid Phone number");
                    etStudentPhoneNumber.requestFocus();
                }
                if (ID.length() != 10){
                    etStudentIDNumber.setError("Please enter correct ID number");
                    etStudentIDNumber.requestFocus();
                }
                if (name.length() < 4){
                    etStudentName.setError("Invalid Name");
                    etStudentName.requestFocus();
                }
                if (department.equals("Select Department")) {
                    Toasty.error(StudentSignUpActivity.this, "Select Department", Toast.LENGTH_SHORT).show();
                }
                if (semester.equals("Select Semester")) {
                    Toasty.error(StudentSignUpActivity.this, "Select Semester", Toast.LENGTH_SHORT).show();
                }
                if (department.equals("Select Department") && semester.equals("Select Semester")) {
                    Toasty.error(StudentSignUpActivity.this, "Select Department & Semester", Toast.LENGTH_SHORT).show();
                }
                else if (password.length() > 7 && email.matches(emailpattern) && phone.length() == 10 && ID.length() == 10 && name.length() > 4
                        && !department.equals("Select Department") && !semester.equals("Select Semester")) {
                    loading.setTitle("Create Account");
                    loading.setMessage("Please wait, while we are checking the credentials.");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                    reference.orderByChild("id_number").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                Toasty.warning(StudentSignUpActivity.this, "User with given ID card number already exists", Toast.LENGTH_LONG).show();
                                etStudentIDNumber.requestFocus();
                            }
                            else {
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            user u = new user(name, phone, department, email, semester, "student", "", ID);
                                            reference.child(key).setValue(u);
                                            Intent i = new Intent(StudentSignUpActivity.this, DashboardStudent.class);
                                            startActivity(i);
                                            loading.dismiss();
                                            finish();
                                        }
                                        else {
                                            loading.dismiss();
                                            Toasty.warning(StudentSignUpActivity.this, "Account creation failed\n"+task.getException(), Toast.LENGTH_LONG).show();
                                            etStudentEmail.requestFocus();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(StudentSignUpActivity.this, "Error : "+databaseError, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
