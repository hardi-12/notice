package com.example.noticeboard.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.R;
import com.example.noticeboard.user;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.viewHolder> {

    ArrayList<user> userArrayList;

    public UserListAdapter(ArrayList<user> userArrayList) {
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist, parent, false);
        return new UserListAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.tvUserName.setText(userArrayList.get(position).getName());
        holder.tvDepartment.setText(userArrayList.get(position).getDepartment());
        holder.tvEmail.setText(userArrayList.get(position).getEmail());
        if(userArrayList.get(position).getDesignation().equals(""))
            holder.tvSemDeg.setText(userArrayList.get(position).getSemester());
        else
            holder.tvSemDeg.setText(userArrayList.get(position).getDesignation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to E-mail : "+userArrayList.get(position).getEmail());
                builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        v.getContext().startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"+userArrayList.get(position).getEmail())));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName,tvEmail,tvDepartment,tvSemDeg;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvSemDeg = itemView.findViewById(R.id.tvSemDeg);
        }
    }
}
