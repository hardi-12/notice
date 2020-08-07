package com.example.noticeboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.EventDetails;
import com.example.noticeboard.R;
import com.example.noticeboard.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JsoupAdapter extends RecyclerView.Adapter<JsoupAdapter.viewholder> {

    ArrayList<event> eventList;
    Context context;

    public JsoupAdapter(ArrayList<event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public JsoupAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_format, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JsoupAdapter.viewholder holder, int position) {
        final String title, description, time_left, speaker, contact, duration, venue, time, conducted_by, date, year, reg_link;
        String displayDate = null;
        title = eventList.get(position).getTitle();
        description = eventList.get(position).getDescription();
        time_left = eventList.get(position).getTime_left();
        speaker = eventList.get(position).getSpeaker();
        contact = eventList.get(position).getContact();
        duration = eventList.get(position).getDuration();
        venue = eventList.get(position).getVenue();
        time = eventList.get(position).getTime();
        conducted_by = eventList.get(position).getConducted_by();
        date = eventList.get(position).getDate();
        year = eventList.get(position).getYear();
        reg_link = eventList.get(position).getReg_link();
        DateFormat inputFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM");
        try {
            displayDate = outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvPrintFullDate.setText(displayDate);
        holder.tvPrintTime.setText(time);
        holder.tvPrintTitle.setText(title);
        holder.tvPrintClass.setText("Class : "+year); // its Class:TE according to website
        holder.tvPrintVenue.setText("Venue : "+venue);
        holder.tvPrintDuration .setText("Duration : "+duration);
        holder.tvPrintConductedBy.setText("Conducted By : "+conducted_by);
        holder.tvPrintSpeakerName.setText("Speaker : "+speaker);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EventDetails.class);
                i.putExtra("title", title);
                i.putExtra("description", description);
                i.putExtra("time_left", time_left);
                i.putExtra("speaker", speaker);
                i.putExtra("contact", contact);
                i.putExtra("duration", duration);
                i.putExtra("venue", venue);
                i.putExtra("time", time);
                i.putExtra("conducted_by", conducted_by);
                i.putExtra("date", date);
                i.putExtra("year", year);
                i.putExtra("reg_link", reg_link);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class viewholder extends RecyclerView.ViewHolder {
        TextView tvPrintFullDate, tvPrintTime, tvPrintTitle, tvPrintClass, tvPrintVenue, tvPrintDuration, tvPrintConductedBy, tvPrintSpeakerName;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvPrintFullDate = itemView.findViewById(R.id.tvPrintFullDate);
            tvPrintTime = itemView.findViewById(R.id.tvPrintTime);
            tvPrintTitle = itemView.findViewById(R.id.tvPrintTitle);
            tvPrintClass = itemView.findViewById(R.id.tvPrintClass);
            tvPrintVenue = itemView.findViewById(R.id.tvPrintVenue);
            tvPrintDuration = itemView.findViewById(R.id.tvPrintDuration);
            tvPrintConductedBy = itemView.findViewById(R.id.tvPrintConductedBy);
            tvPrintSpeakerName = itemView.findViewById(R.id.tvPrintSpeakerName);
        }
    }
}
