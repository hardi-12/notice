package com.example.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class EventDetails extends AppCompatActivity {
    TextView tvEventTitle, tvEventTimeLeft, tvEventConductedBy, tvEventClass, tvEventVenue, tvEventSpeaker, tvEventDate, tvEventTime,
            tvEventDuration, tvEventDescription, tvEventRegLink, tvEventSpeakerContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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

        Intent i = getIntent();

        tvEventTitle.setText(i.getStringExtra("title"));
        tvEventTimeLeft.setText(i.getStringExtra("time_left"));
        tvEventConductedBy.setText(i.getStringExtra("conducted_by"));
        tvEventClass.setText(i.getStringExtra("year"));
        tvEventVenue.setText(i.getStringExtra("venue"));
        tvEventSpeaker.setText(i.getStringExtra("speaker"));
        tvEventDate.setText(i.getStringExtra("date"));
        tvEventTime.setText(i.getStringExtra("time"));
        tvEventDuration.setText(i.getStringExtra("duration"));
        tvEventDescription.setText(i.getStringExtra("description"));
        tvEventRegLink.setText(i.getStringExtra("reg_link"));
        tvEventSpeakerContact.setText(i.getStringExtra("contact"));
    }
}