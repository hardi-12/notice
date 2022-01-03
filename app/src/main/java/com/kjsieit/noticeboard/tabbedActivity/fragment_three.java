package com.kjsieit.noticeboard.tabbedActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kjsieit.noticeboard.Dashboard;
import com.kjsieit.noticeboard.R;
import com.kjsieit.noticeboard.adapter.JsoupAdapter;
import com.kjsieit.noticeboard.models.event;
import com.kjsieit.noticeboard.ui.viewAll.EventsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.util.ArrayList;
import java.util.Objects;

public class fragment_three extends Fragment {
    RecyclerView list_events;
    Button view_all_events;
    TextView no_events;
    ArrayList<event> eventList = new ArrayList<>();
    JsoupAdapter jsoupAdapter;
    ProgressBar loading;
    String url = "http://ems.kjsieit.in/api/getEvents.php?status=confirm&when=upcomming&who=all";
    RequestQueue requestQueue;

    public fragment_three() {}  // Required empty public constructor

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_jsoup, container, false);

        list_events = view.findViewById(R.id.list_events);
        list_events.setLayoutManager(new LinearLayoutManager(view.getContext()));
        no_events = view.findViewById(R.id.no_events);
        view_all_events = view.findViewById(R.id.view_all_events);
        loading = view.findViewById(R.id.loading);
        requestQueue = Volley.newRequestQueue(requireContext());

        view_all_events.setOnClickListener(v -> {
            Fragment newFragment = new EventsFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

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
                jsoupAdapter = new JsoupAdapter(eventList, view.getContext());
                list_events.setAdapter(jsoupAdapter);
            }
        }, error -> {
            
        });

        requestQueue.add(jsonArrayRequest);

        return view;
    }

//    @SuppressLint("StaticFieldLeak")
//    public class Data extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loading.setVisibility(View.VISIBLE);
//            view_all_events.setVisibility(View.INVISIBLE);
//        }
//
//        @SuppressLint("NotifyDataSetChanged")
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            loading.setVisibility(View.GONE);
//            view_all_events.setVisibility(View.VISIBLE);
//            jsoupAdapter.notifyDataSetChanged();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            eventList.clear();
//            try {
//                String url = "http://ems.kjsieit.in/";
//                Document document = Jsoup.connect(url).get();
//                Elements element = document.select("div.col-12").select("div.event_cards");
//                int size = element.size();
//                System.out.println("Count cards: "+size);
//                for (int i = 0; i < 4; i++) {
//                    title = element.select("div.card-body").select("div.col-10").select("h2.card-title").eq(i).text();
//                    description = element.select("p.card-text").eq(i).text();
//                    time_left = element.select("div.card-body").select("div.text-right").select("div.text-danger").eq(i).text();
//
//                    ArrayList<String> data = new ArrayList<>();
//                    Element table = element.select("table").get(i); //select the first table.
//                    Elements rows = table.select("tr");
//                    for (int j = 0; j < rows.size(); j++) {
//                        Element row = rows.get(j);
//                        Elements cols = row.select("td");
//                        for(int k = 0; k < cols.size(); k++)
//                            data.add(cols.eq(k).text());
//                    }
//
//                    speaker = data.get(1);
//                    year = data.get(3);
//                    venue = data.get(5);
//                    date = data.get(7);
//                    duration = data.get(9);
//                    time = data.get(11);
//                    conducted_by = data.get(13);
//                    reg_link = data.get(15);
//
//                    eventList.add(new event(title, description, time_left, speaker, "", duration, venue,
//                            time, conducted_by, reg_link, date, year));
////                    System.out.println("Details: "+title+" "+ description+" "+ time_left+" "+ speakerF+" "+ contact+" "+ durationF+" "+ venueF+" "+
////                            timeF+" "+ conducted_byF+" "+ reg_link+" "+ dateF+" "+ yearF);
//                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}