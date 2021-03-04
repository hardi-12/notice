package com.example.noticeboard.tabbedActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.noticeboard.R;
import com.example.noticeboard.adapter.JsoupAdapter;
import com.example.noticeboard.event;
import com.example.noticeboard.ui.viewAll.EventsFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class EventFragmentJsoup extends Fragment {
    RecyclerView list_events;
    Button view_all_events;
    TextView no_events;
    ArrayList<event> eventList = new ArrayList<>();
    JsoupAdapter jsoupAdapter;
    String title, description, time_left, speaker, contact, duration, venue, time, conducted_by, reg_link, date, year, conducted_byF,
            yearF, venueF, speakerF, dateF, timeF, durationF;
    ProgressBar loading;

    public EventFragmentJsoup() {}  // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_jsoup, container, false);

        list_events = view.findViewById(R.id.list_events);
        list_events.setLayoutManager(new LinearLayoutManager(view.getContext()));
        no_events = view.findViewById(R.id.no_events);
        view_all_events = view.findViewById(R.id.view_all_events);
        jsoupAdapter = new JsoupAdapter(eventList, view.getContext());
        list_events.setAdapter(jsoupAdapter);
        loading = view.findViewById(R.id.loading);
        Data data = new Data();
        data.execute();

        view_all_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new EventsFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    public class Data extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
            view_all_events.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.setVisibility(View.GONE);
            view_all_events.setVisibility(View.VISIBLE);
            jsoupAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventList.clear();
            try {
                String url = "https://kjsieit.in/sims/eventmanager/index.php";
                Document document = Jsoup.connect(url).get();
                Elements element = document.select("div.col-12");
                int size = element.size();
                for (int i = 0; i < 4; i++) {
                    title = element.select("div.card-body").select("div.row").select("div.col-10").select("h2.card-title").eq(i).text();
                    description = element.select("p.card-text").eq(i).text();
                    time_left = element.select("div.card-body").select("div.row").select("div.col-2").eq(i).text();
                    speaker = element.select("ul.list-group").select("li#speaker_contact").eq(i).text();
                    speakerF = speaker.substring(speaker.indexOf("Speaker") +9, speaker.length() -7);
                    year = element.select("ul.list-group").select("div.row").select("li#class").eq(i).text();
                    yearF = year.substring(year.indexOf("Class") +7);
                    date = element.select("ul.list-group").select("div.row").select("li#date").eq(i).text();
                    dateF = date.substring(date.indexOf("Date") +6);
                    time = element.select("ul.list-group").select("div.row").select("li#time").eq(i).text();
                    timeF = time.substring(time.indexOf("Time") +6);
                    venue = element.select("ul.list-group").select("div.row").select("li#venue").eq(i).text();
                    venueF = venue.substring(venue.indexOf("Venue") +7);
                    duration = element.select("ul.list-group").select("div.row").select("li#duration").eq(i).text();
                    durationF = duration.substring(duration.indexOf("Duration") +10);
                    conducted_by = element.select("ul.list-group").select("div.row").select("li#conducted_by").eq(i).text();
                    conducted_byF = conducted_by.substring(conducted_by.indexOf("Conducted") +14);
                    reg_link = element.select("ul.list-group").select("li#link-reg-form").select("a#link-reg-form").eq(i).attr("href");
                    contact = element.select("ul.list-group").select("li.list-group-item").select("a.badge").eq(i).attr("href");
                    eventList.add(new event(title, description, time_left, speakerF, contact, durationF, venueF,
                            timeF, conducted_byF, reg_link, dateF, yearF));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}