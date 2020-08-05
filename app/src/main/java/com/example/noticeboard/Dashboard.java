package com.example.noticeboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.noticeboard.noticeTypes.NoticeDepartment;
import com.example.noticeboard.noticeTypes.NoticeExamCell;
import com.example.noticeboard.noticeTypes.NoticeStudent;
import com.example.noticeboard.ui.about.AboutFragment;
import com.example.noticeboard.ui.home.HomeFragment;
import com.example.noticeboard.ui.list.ListFragment;
import com.example.noticeboard.ui.profile.ProfileFragment;
import com.example.noticeboard.ui.superAdmin.superAdminFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    NavigationView navigationView;
    TextView tvDashEmail, tvDashName, tvDashType;
    FirebaseUser user;
    private BottomSheetBehavior mBottomSheetBehavior1;
    CardView deptNotice, examNotice, studentNotice, eventNotice;
    CoordinatorLayout other;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("user");
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view_admin);

        other = findViewById(R.id.other);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(0);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mBottomSheetBehavior1.setPeekHeight(120);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mBottomSheetBehavior1.setPeekHeight(0);
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        examNotice = findViewById(R.id.examNotice);
        deptNotice = findViewById(R.id.deptNotice);
        studentNotice = findViewById(R.id.studentNotice);
        eventNotice = findViewById(R.id.eventNotice);

        examNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, NoticeExamCell.class);
                startActivity(i);
            }
        });

        eventNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Dashboard.this, NoticeSeminar.class);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://eventkj.000webhostapp.com/index.php")));
            }
        });

        deptNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, NoticeDepartment.class);
                startActivity(i);
            }
        });

        studentNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, NoticeStudent.class);
                startActivity(i);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        tvDashEmail = headerView.findViewById(R.id.tvDashEmail);
        tvDashName = headerView.findViewById(R.id.tvDashName);
        tvDashType = headerView.findViewById(R.id.tvDashType);
        navigationView.setNavigationItemSelectedListener(this);

        String em = user.getEmail().replace(".","_dot_");
        tvDashEmail.setText(user.getEmail());
        reference.child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String type = dataSnapshot.child("designation").getValue().toString();
                String superAdmin = dataSnapshot.child("superAdmin").getValue().toString();

                tvDashName.setText(name);
                tvDashType.setText(type);
                if (superAdmin.equals("false")) {
                    navigationView.getMenu().findItem(R.id.nav_list_superAdmin).setVisible(false);
                }
                else navigationView.getMenu().findItem(R.id.nav_list_superAdmin).setVisible(true);
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
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayoutAdmin), "Email not verified", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAction("Verify now!!!", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(Dashboard.this, "Email verification link sent to "+user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(Dashboard.this, "Email verification failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itAddNotice) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_signout:
                firebaseAuth.signOut();
                Intent i = new Intent(this,IntroActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_svv:
                Intent k = new Intent(Dashboard.this,web_view.class);
                k.putExtra("link","https://myaccount.somaiya.edu/#/login");
                startActivity(k);
                break;

            case R.id.nav_website:
                Intent j = new Intent(Dashboard.this,web_view.class);
                j.putExtra("link","https://kjsieit.somaiya.edu/en");
                startActivity(j);
                break;

            case R.id.nav_about :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AboutFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_list_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ListFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_list_superAdmin:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new superAdminFragment()).addToBackStack(null).commit();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
