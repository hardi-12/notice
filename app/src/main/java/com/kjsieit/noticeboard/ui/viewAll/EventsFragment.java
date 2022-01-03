package com.kjsieit.noticeboard.ui.viewAll;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.adapter.JsoupAdapter;
import com.kjsieit.noticeboard.models.event;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    SearchView SearchBar_events;
    SwipeRefreshLayout refreshEvents;
    RecyclerView list_view_events;
    ArrayList<event> eventList = new ArrayList<>();
    JsoupAdapter jsoupAdapter;
    String title, description, time_left, speaker, contact, duration, venue, time, conducted_by, reg_link, date, year, conducted_byF,
            yearF, venueF, speakerF, dateF, timeF, durationF;
    RequestQueue requestQueue;
    String url = "http://ems.kjsieit.in/api/getEvents.php?status=confirm&who=all";

    public EventsFragment() {}  // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_events, container, false);

        SearchBar_events = view.findViewById(R.id.SearchBar_events);
        refreshEvents = view.findViewById(R.id.refreshEvents);
        list_view_events = view.findViewById(R.id.list_view_events);
        list_view_events.setLayoutManager(new LinearLayoutManager(getActivity()));
        jsoupAdapter = new JsoupAdapter(eventList, view.getContext());
        list_view_events.setAdapter(jsoupAdapter);
        requestQueue = Volley.newRequestQueue(requireContext());

        load();

        refreshEvents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        SearchBar_events.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query,view);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText,view);
                return true;
            }
        });
        return view;
    }

    public void load() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            eventList.clear();
            for (int i=0; i<response.length();i++) {
                try {
                    JSONObject rep = response.getJSONObject(i);
                    String id = rep.getString("id");
                    String event_name = rep.getString("event_name");
                    String event_title = rep.getString("event_title");
                    String end_time = rep.getString("end_time");
                    String start_time = rep.getString("start_time");
                    String duration = rep.getString("duration");
                    String venue = rep.getString("venue");
                    String classC = rep.getString("class");
                    String description = rep.getString("description");
                    String speaker_name = rep.getString("speaker_name");
                    String conducted_by = rep.getString("conducted_by");
                    String organised_by = rep.getString("organised_by");
                    String eventcoord = rep.getString("eventcoord");
                    String eventcoorddesg = rep.getString("eventcoorddesg");
                    String amount = rep.getString("amount");
                    String series_name = rep.getString("series_name");
                    String series_title = rep.getString("series_title");
                    String registration_link = rep.getString("registration_link");
                    String speaker_contact = rep.getString("speaker_contact");
                    String department = rep.getString("department");
                    String participants = rep.getString("participants");
                    String speaker_desg = rep.getString("speaker_desg");

                    eventList.add(new event(id, event_name, event_title, end_time, start_time, duration, venue, classC, description, speaker_name, conducted_by, organised_by, eventcoord, eventcoorddesg, amount, series_name, series_title, registration_link, speaker_contact, department, participants, speaker_desg));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsoupAdapter = new JsoupAdapter(eventList, getContext());
                list_view_events.setAdapter(jsoupAdapter);
            }
        }, error -> {

        });

        requestQueue.add(jsonArrayRequest);
    }

    private void search(String s, View view) {
        ArrayList<event> myList = new ArrayList();
        for (event object : eventList) {
            if(object.getEvent_name().toLowerCase().contains(s.toLowerCase()) || object.getStart_time().toLowerCase().contains(s.toLowerCase()) ) {
                myList.add(object);
            }
        }
        jsoupAdapter = new JsoupAdapter(myList, view.getContext());
        list_view_events.setAdapter(jsoupAdapter);
    }
}