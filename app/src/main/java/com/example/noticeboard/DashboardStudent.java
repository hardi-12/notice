package com.example.noticeboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.noticeboard.ui.about.AboutFragment;
import com.example.noticeboard.ui.home.HomeFragment;
import com.example.noticeboard.ui.list.ListFragment;
import com.example.noticeboard.ui.profile.ProfileFragment;
import com.example.noticeboard.ui.resources.ResourceFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class DashboardStudent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference, ref;
    String userSem;
    NavigationView navigationView;
    TextView tvDashEmailSt, tvDashNameSt, tvDashTypeSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("user");
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_st);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view_student);

        View headerView = navigationView.getHeaderView(0);
        tvDashEmailSt = headerView.findViewById(R.id.tvDashEmailSt);
        navigationView.setNavigationItemSelectedListener(this);
        tvDashEmailSt = headerView.findViewById(R.id.tvDashEmailSt);
        tvDashNameSt = headerView.findViewById(R.id.tvDashNameSt);
        tvDashTypeSt = headerView.findViewById(R.id.tvDashTypeSt);
        navigationView.setNavigationItemSelectedListener(this);

        String em = user.getEmail().replace(".","_dot_");
        tvDashEmailSt.setText(user.getEmail());
        reference.child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String type = dataSnapshot.child("type").getValue().toString();

                tvDashNameSt.setText(name);
                tvDashTypeSt.setText(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        if (!user.isEmailVerified()) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayoutStudent), "Email not verified", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("Verify now!!!", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DashboardStudent.this, "Email verification link sent to "+user, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(DashboardStudent.this, "Email verification failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            snackbar.show();

//            int daysBetween = 0;
//            String userSem = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                String curr_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//                daysBetween = ((int) ChronoUnit.DAYS.between(LocalDate.parse(curr_date), LocalDate.parse("2022-06-30")));
//            }
//
//            if (daysBetween > 0 && daysBetween <= 182) { userSem = "Semester VIII"; }
//            else if (daysBetween > 182 && daysBetween <= 366) { userSem = "Semester VII"; }
//            else if (daysBetween > 366 && daysBetween <= 548) { userSem = "Semester VI"; }
//            else if (daysBetween > 548 && daysBetween <= 732) { userSem = "Semester V"; }
//            else if (daysBetween > 732 && daysBetween <= 914) { userSem = "Semester IV"; }
//            else if (daysBetween > 914 && daysBetween <= 1098) { userSem = "Semester III"; }
//            else if (daysBetween > 1098 && daysBetween <= 1280) { userSem = "Semester II"; }
//            else if (daysBetween > 1280 && daysBetween <= 1464) { userSem = "Semester I"; }
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("semester", userSem);
//            FirebaseDatabase.getInstance().getReference("user").child(user.getEmail().replace(".", "_dot_"))
//                    .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Log.i("SemUpdate", "Success");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.i("SemUpdate", "Failure");
//                }
//            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_signout:
                firebaseAuth.getInstance().signOut();
                Intent i = new Intent(this,IntroActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment())/*.addToBackStack(null)*/.commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_website:
                Intent j = new Intent(DashboardStudent.this,web_view.class);
                j.putExtra("link","https://kjsieit.somaiya.edu/en");
                startActivity(j);
                break;

            case R.id.nav_about :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AboutFragment())/*.addToBackStack(null)*/.commit();
                break;

            case R.id.nav_list_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ListFragment())/*.addToBackStack(null)*/.commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_SIMS:
                startActivity(new Intent(DashboardStudent.this,web_view.class)
                        .putExtra("link","https://www.kjsieit.in/sims/student/login.php"));
                break;
            case R.id.nav_resources:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ResourceFragment())/*.addToBackStack(null)*/.commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
