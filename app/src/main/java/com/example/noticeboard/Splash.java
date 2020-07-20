package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    Animation animation;
    LottieAnimationView l;
    FirebaseUser user;
    DatabaseReference reference;
    String mail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    animation= AnimationUtils.loadAnimation(Splash.this,R.anim.annimation_1);
                    l=findViewById(R.id.l);
                    l.setAnimation(animation);
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                if (user != null) {
                    mail = user.getEmail().replace(".", "_dot_");
                    reference.child(mail).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String typ = dataSnapshot.child("type").getValue().toString();
                            if (typ.equals("student")) {
                                startActivity(new Intent(Splash.this, DashboardStudent.class));
                                finish();
                            }
                            if (typ.equals("admin")) {
                                startActivity(new Intent(Splash.this, Dashboard.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Splash.this, "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    startActivity(new Intent(Splash.this, IntroActivity.class));
                    finish();
                }

            }
        }).start();
    }
}