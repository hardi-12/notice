package com.kjsieit.noticeboard.ui.resources;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kjsieit.noticeboard.R;

public class ResourceFragment extends Fragment {
    public ResourceFragment() {}

    Button btnCS, btnIT, btnEXTC, btnETRX, btnAI_DS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        btnCS = view.findViewById(R.id.btnCS);
        btnIT = view.findViewById(R.id.btnIT);
        btnEXTC = view.findViewById(R.id.btnEXTC);
        btnETRX = view.findViewById(R.id.btnETRX);
        btnAI_DS = view.findViewById(R.id.btnAI_DS);

        btnCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ResourceSemester.class);
                i.putExtra("dept", "CS");
                startActivity(i);
            }
        });

        btnIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ResourceSemester.class);
                i.putExtra("dept", "IT");
                startActivity(i);
            }
        });

        btnEXTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ResourceSemester.class);
                i.putExtra("dept", "EXTC");
                startActivity(i);
            }
        });

        btnETRX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ResourceSemester.class);
                i.putExtra("dept", "ETRX");
                startActivity(i);
            }
        });

        btnAI_DS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ResourceSemester.class);
                i.putExtra("dept", "AI-DS");
                startActivity(i);
            }
        });

        return view;
    }
}
