package com.example.noticeboard.adapter;

import android.content.Intent;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.NoticeDetails;
import com.example.noticeboard.R;
import com.example.noticeboard.models.notice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.viewholder> {

    ArrayList<notice> noticeList;
    Boolean fileStatus;
    public EventsAdapter(ArrayList<notice> noticeList) {
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public EventsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_format, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventsAdapter.viewholder holder, final int position) {
        final String title, dept, sem, subject, notice, date, currdate, upload, time, key, contact,dayOfTheWeek,day;
        title = noticeList.get(position).getTitle();
        dept = noticeList.get(position).getBranch();
        sem = noticeList.get(position).getSem();
        subject = noticeList.get(position).getSubject();
        notice = noticeList.get(position).getNotice();
        date = noticeList.get(position).getDate();
        currdate = noticeList.get(position).getCdate();
        upload = noticeList.get(position).getUpload();
        time = noticeList.get(position).getTime();
        key = noticeList.get(position).getKey();
        contact = noticeList.get(position).getContact();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = format.parse(date);
            dayOfTheWeek = (String) DateFormat.format("MMM", date1); // Thursday
            day          = (String) DateFormat.format("dd", date1); // 20
            holder.tvPrintLDate.setText(day);
            holder.month.setText(dayOfTheWeek);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvPrintTitle.setText(title);
        holder.tvPrintDate.setText(currdate);
        holder.tvPrintContact.setText(""+contact);
        holder.tvPrintContact.setMovementMethod(LinkMovementMethod.getInstance());
        holder.time.setText(time);

        FirebaseDatabase.getInstance().getReference("user").child(upload.replace(".", "_dot_")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String dept = dataSnapshot.child("department").getValue().toString();
                holder.tvPrintUpload.setText(name+"\n"+dept);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NoticeDetails.class);
                i.putExtra("title", title);
                i.putExtra("dept", dept);
                i.putExtra("sem", sem);
                i.putExtra("subject", subject);
                i.putExtra("notice", notice);
                i.putExtra("date", date);
                i.putExtra("currdate", currdate);
                i.putExtra("upload", upload);
                i.putExtra("time", time);
                i.putExtra("key", key);
                v.getContext().startActivity(i);
            }
        });

        FirebaseDatabase.getInstance().getReference("notice").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("files")) {
                    fileStatus = true;
                    String file_name = dataSnapshot.child("files").getValue().toString();
                }
                else fileStatus = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    static class viewholder extends RecyclerView.ViewHolder {

        TextView tvPrintTitle, tvPrintUpload, tvPrintDate, tvPrintLDate, tvPrintContact,month, time;

        public viewholder(@NonNull View itemView) {
            super(itemView);
//            tvPrintTitle = itemView.findViewById(R.id.tvPrintTitle);
//            tvPrintUpload = itemView.findViewById(R.id.tvPrintUpload);
//            tvPrintDate = itemView.findViewById(R.id.tvPrintDate);
//            tvPrintLDate = itemView.findViewById(R.id.tvPrintLDate);
//            tvPrintContact = itemView.findViewById(R.id.tvPrintContact);
//            month = itemView.findViewById(R.id.textView8);
//            time = itemView.findViewById(R.id.tvPrintTime);
        }
    }
}
