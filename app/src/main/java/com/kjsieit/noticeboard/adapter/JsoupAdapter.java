package com.kjsieit.noticeboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kjsieit.noticeboard.EventDetails;
import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.models.event;

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
        final String id, event_name, event_title, end_time, start_time, duration, venue, classC, description, speaker_name, conducted_by, organised_by, eventcoord, eventcoorddesg, amount, series_name, series_title, registration_link, speaker_contact, department, participants, speaker_desg;
        id = eventList.get(position).getId();
        event_name = eventList.get(position).getEvent_name();
        event_title = eventList.get(position).getEvent_title();
        end_time = eventList.get(position).getEnd_time();
        start_time = eventList.get(position).getStart_time();
        duration = eventList.get(position).getDuration();
        venue = eventList.get(position).getVenue();
        classC = eventList.get(position).getClassC();
        description = eventList.get(position).getDescription();
        speaker_name = eventList.get(position).getSpeaker_name();
        conducted_by = eventList.get(position).getConducted_by();
        organised_by = eventList.get(position).getOrganised_by();
        eventcoord = eventList.get(position).getEventcoord();
        eventcoorddesg = eventList.get(position).getEventcoorddesg();
        amount = eventList.get(position).getAmount();
        series_name = eventList.get(position).getSeries_name();
        series_title = eventList.get(position).getSeries_title();
        registration_link = eventList.get(position).getRegistration_link();
        speaker_contact = eventList.get(position).getSpeaker_contact();
        department = eventList.get(position).getDepartment();
        participants = eventList.get(position).getParticipants();
        speaker_desg = eventList.get(position).getSpeaker_desg();


        String cls = null;
        if(classC.equals("CE_FE,CE_SE,CE_TE,CE_BE,IT_FE,IT_SE,IT_TE,IT_BE,EXTC_FE,EXTC_SE,EXTC_TE,EXTC_BE,ETRX_FE,ETRX_SE,ETRX_TE,ETRX_BE,AI-DS_FE,AI-DS_SE,AI-DS_TE,AI-DS_BE"))
            cls = "All Students";
        else
            cls = classC;

        String displayDate = null, displayTime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputDate = new SimpleDateFormat("dd\nMMM\nyyyy");
        DateFormat outputTime = new SimpleDateFormat("hh:mm a");
        try {
            displayDate = outputDate.format(inputFormat.parse(start_time));
            displayTime = outputTime.format(inputFormat.parse(start_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvPrintFullDate.setText(displayDate);
        holder.tvPrintTime.setText(displayTime);
        holder.tvPrintTitle.setText(event_name);
        holder.tvPrintClass.setText("Class : "+cls);
        holder.tvPrintVenue.setText("Venue : "+venue);
        holder.tvPrintDuration .setText("Duration : "+duration);
        holder.tvPrintConductedBy.setText("Conducted By : "+conducted_by);
        holder.tvPrintSpeakerName.setText("Speaker : "+speaker_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EventDetails.class);
                i.putExtra("id", id);
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
