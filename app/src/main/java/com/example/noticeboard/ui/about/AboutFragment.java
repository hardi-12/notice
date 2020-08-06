package com.example.noticeboard.ui.about;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.noticeboard.AboutDepartment;
import com.example.noticeboard.R;
import com.example.noticeboard.web_view;

public class AboutFragment extends Fragment {
    Button btnFeedback, btnAboutIt;
    ImageButton ibInstagram, ibFacebook, ibLinkIn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        btnFeedback = view.findViewById(R.id.btnFeedback);
        ibInstagram = view.findViewById(R.id.ibInstagram);
        ibFacebook = view.findViewById(R.id.ibFacebook);
        ibLinkIn = view.findViewById(R.id.ibLinkIn);
        btnAboutIt = view.findViewById(R.id.btnAboutIt);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), web_view.class).putExtra("link", "https://docs.google.com/forms/d/e/1FAIpQLSdnD7VG7ZTc52h3FwnUMevMJNH8h1ophqvu9vYKlTxl7DCsEg/viewform?usp=sf_link"));
            }
        });

        btnAboutIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AboutDepartment.class));
            }
        });

        ibInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.instagram.com/it_kjsieit/")));
            }
        });

        ibFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/KJSIEITITDEPT")));
            }
        });

        ibLinkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.linkedin.com/school/kjsomaiya-institute-of-engineering-and-information-technology/")));
            }
        });
        return view;
    }
}