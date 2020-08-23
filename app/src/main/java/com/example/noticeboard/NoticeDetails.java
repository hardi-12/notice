package com.example.noticeboard;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NoticeDetails extends AppCompatActivity {

    TextView tvDetailTitle, tvDetailUploadBy, tvDetailDate, tvDetailDept, tvDetailSem, tvDetailSubject, tvDetailNotice,
            tvDetailTime, tvDetailLastDate, tvDashTym;
    String title, department, semester, subject, notice, date, current_date, upload, time, key;
    DatabaseReference reference, referenceSeen, referenceUser;
    String file_url, typ;
    long enterTime, exitTime;
    String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailUploadBy = findViewById(R.id.tvDetailUploadBy);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailDept = findViewById(R.id.tvDetailDept);
        tvDetailSem = findViewById(R.id.tvDetailSem);
        tvDetailSubject = findViewById(R.id.tvDetailSubject);
        tvDetailNotice = findViewById(R.id.tvDetailNotice);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailLastDate = findViewById(R.id.tvDetailLastDate);
        tvDashTym = findViewById(R.id.tvDashTym);
        reference = FirebaseDatabase.getInstance().getReference().child("notice");
        referenceSeen = FirebaseDatabase.getInstance().getReference("seen");
        referenceUser = FirebaseDatabase.getInstance().getReference("user");

        Intent i = getIntent();
        title = i.getStringExtra("title");
        department = i.getStringExtra("dept");
        semester = i.getStringExtra("sem");
        subject = i.getStringExtra("subject");
        notice = i.getStringExtra("notice");
        date = i.getStringExtra("date");
        current_date = i.getStringExtra("currdate");
        upload = i.getStringExtra("upload");
        time = i.getStringExtra("time");
        key = i.getStringExtra("key");

        tvDetailTitle.setText(title);
        tvDetailUploadBy.setText(upload);
        tvDetailDate.setText(current_date);
        tvDetailDept.setText(department);
        tvDetailSem.setText(semester);
        tvDetailSubject.setText(subject);
        tvDetailSubject.setPaintFlags(tvDetailSubject.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDetailNotice.setText(notice);
        if(!time.equals("")) {
            tvDashTym.setVisibility(View.VISIBLE);
            tvDetailTime.setVisibility(View.VISIBLE);
            tvDetailTime.setText(time);
        }
        else {
            tvDashTym.setVisibility(View.GONE);
            tvDetailTime.setVisibility(View.GONE);
        }
        tvDetailLastDate.setText(date);

        referenceUser.child(user.replace(".", "_dot_")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                typ = snapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (typ.equals("admin")) {
            getMenuInflater().inflate(R.menu.notice_details_staff, menu);
        }
        else getMenuInflater().inflate(R.menu.notice_details_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itShare:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/*");
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT, title+"\nfor "+department+"\n"+semester+"\n"+current_date+"\n"+notice+"\nLast date : "+date+"\n"+time);
                startActivity(Intent.createChooser(i,"Share using..."));
                break;

            case R.id.itSeen:
                startActivity(new Intent(NoticeDetails.this, NoticeSeen.class).putExtra("key", key));
                break;

            case R.id.itFiles:
                startActivity(new Intent(NoticeDetails.this, NoticeFiles.class).putExtra("key", key));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        enterTime = System.currentTimeMillis();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        final double readTime = (wordCount(title) + wordCount(subject) + wordCount(notice)) * 0.1;   //fast reader - 0.375
//        exitTime = System.currentTimeMillis();
//        final double screenTime = ((exitTime - enterTime) / 1000) % 60;
//        final String asd = wordCount(title) + wordCount(subject) + wordCount(notice)+" word(s)\nRead time : "+readTime+"s\nScreen time : "+screenTime+"s\n";
//
//        referenceSeen.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(key).hasChild(user.replace(".", "_dot_"))) {
//                    Log.i("Seen", key+"\t\t\t\t"+user);
//                }
//                else {
//                    if (readTime > screenTime) { Toast.makeText(getApplicationContext(), asd+"Did not read", Toast.LENGTH_LONG).show(); }
//                    else if (readTime <= screenTime) {
//                        Toast.makeText(getApplicationContext(), asd+"Read", Toast.LENGTH_LONG).show();
//                        HashMap<String, Object> hashMap = new HashMap<>();
//                        hashMap.put("email", user);
//                        hashMap.put("timeStamp", new SimpleDateFormat("hh:mm:ss a dd-MM-yyyy", Locale.getDefault()).format(new Date()));
//                        referenceSeen.child(key).child(user.replace(".", "_dot_")).setValue(hashMap);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toasty.info(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    public int wordCount (String text) {
//        String[] array = text.split("\\s+");
//        return array.length;
//    }
}
