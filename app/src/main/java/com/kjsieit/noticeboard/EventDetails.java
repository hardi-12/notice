package com.kjsieit.noticeboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class EventDetails extends AppCompatActivity {
    TextView tvEventTitle, tvEventTimeLeft, tvEventConductedBy, tvEventClass, tvEventVenue, tvEventSpeaker, tvEventDate, tvEventTime,
            tvEventDuration, tvEventDescription, tvEventRegLink, tvEventSpeakerContact;
    String url = "http://ems.kjsieit.in/api/getEvent.php?field=id&value=";
    RequestQueue requestQueue;
    LinearLayout reg_link,speaker_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requestQueue = Volley.newRequestQueue(this);

        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventTimeLeft = findViewById(R.id.tvEventTimeLeft);
        tvEventConductedBy = findViewById(R.id.tvEventConductedBy);
        tvEventClass = findViewById(R.id.tvEventClass);
        tvEventVenue = findViewById(R.id.tvEventVenue);
        tvEventSpeaker = findViewById(R.id.tvEventSpeaker);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvEventDuration = findViewById(R.id.tvEventDuration);
        tvEventDescription = findViewById(R.id.tvEventDescription);
        tvEventRegLink = findViewById(R.id.tvEventRegLink);
        tvEventSpeakerContact = findViewById(R.id.tvEventSpeakerContact);
        reg_link = findViewById(R.id.reg_link);
        speaker_contact = findViewById(R.id.speaker_contact);

        Intent i = getIntent();
        url = url + i.getStringExtra("id");
        System.out.println(url);

        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            System.out.println("Response: "+response);
            try {
                tvEventTitle.setText(response.getString("event_name"));
                tvEventTimeLeft.setText(response.getString("event_title"));
                tvEventConductedBy.setText(response.getString("conducted_by")+"\n"+response.getString("organised_by"));
                String cls = null;
                if(response.getString("class").equals("CE_FE,CE_SE,CE_TE,CE_BE,IT_FE,IT_SE,IT_TE,IT_BE,EXTC_FE,EXTC_SE,EXTC_TE,EXTC_BE,ETRX_FE,ETRX_SE,ETRX_TE,ETRX_BE,AI-DS_FE,AI-DS_SE,AI-DS_TE,AI-DS_BE"))
                    cls = "All Students";
                else
                    cls = response.getString("class");
                tvEventClass.setText(cls);
                tvEventVenue.setText(response.getString("venue"));
                tvEventSpeaker.setText(response.getString("speaker_name")+"\n"+response.getString("speaker_desg"));
                @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                @SuppressLint("SimpleDateFormat") DateFormat outputDate = new SimpleDateFormat("dd MMM yyyy");
                @SuppressLint("SimpleDateFormat") DateFormat outputTime = new SimpleDateFormat("hh:mm a");

                String displayDate = null, startTime = null, endTime = null;
                try {
                    displayDate = outputDate.format(Objects.requireNonNull(inputFormat.parse(response.getString("start_time"))));
                    startTime = outputTime.format(Objects.requireNonNull(inputFormat.parse(response.getString("start_time"))));
                    endTime = outputTime.format(Objects.requireNonNull(inputFormat.parse(response.getString("end_time"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                tvEventDate.setText(displayDate);
                tvEventTime.setText(startTime+" to "+endTime);
                tvEventDuration.setText(response.getString("duration"));
                tvEventDescription.setText(response.getString("description"));
                String link = response.getString("registration_link");
                String contact = response.getString("speaker_contact");
                if(link.equals("null"))
                    reg_link.setVisibility(View.GONE);
                else
                    tvEventRegLink.setText(link);
                if(contact.equals("null"))
                    speaker_contact.setVisibility(View.GONE);
                else
                    tvEventSpeakerContact.setText(response.getString("speaker_contact"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        requestQueue.add(jsonObjectRequest);
    }
}